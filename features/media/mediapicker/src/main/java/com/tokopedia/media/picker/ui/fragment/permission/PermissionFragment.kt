package com.tokopedia.media.picker.ui.fragment.permission

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.databinding.FragmentPermissionBinding
import com.tokopedia.media.picker.ui.adapter.PermissionAdapter
import com.tokopedia.media.picker.ui.adapter.decoration.ItemDividerDecoration
import com.tokopedia.media.picker.utils.permission.PermissionManager
import com.tokopedia.media.picker.utils.permission.PermissionModel
import com.tokopedia.media.picker.utils.permission.PermissionRequestCallback
import com.tokopedia.media.picker.utils.permission.isGranted
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class PermissionFragment @Inject constructor(
    private var viewModelFactory: ViewModelProvider.Factory
) : BaseDaggerFragment(), PermissionRequestCallback {

    private val binding: FragmentPermissionBinding? by viewBinding()
    private var listener: Listener? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[PermissionViewModel::class.java]
    }

    private var permissionManager: PermissionManager? = null
    private val mPermissionList = mutableListOf<PermissionModel>()
    private val mAdapter by lazy { PermissionAdapter(mPermissionList) }

    private var isPermissionDialogShown = false

    private var mTitle = ""
    private var mMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager = PermissionManager.init(this, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_permission,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservable()
    }

    override fun onResume() {
        super.onResume()

        if (listener?.isRootPermissionGranted() == false) {
            viewModel.getDynamicPermissionList()
        }
    }

    override fun onGranted(permissions: List<String>) {
        updateUiState(permissions)
        if (permissions.size == mPermissionList.size) {
            listener?.onPermissionGranted()
        }
    }

    override fun onDenied(permissions: List<String>) {
        updateUiState(permissions)
    }

    override fun onPermissionPermanentlyDenied(permissions: List<String>) {
        updateUiState(permissions)
    }

    private fun initView() {
        viewModel.initOrCreateDynamicWording()
    }

    private fun initObservable() {
        viewModel.dynamicWording.observe(viewLifecycleOwner) {
            mTitle = getString(it.first)
            mMessage = getString(it.second)

            // set the title and message on boarding page
            binding?.txtTitle?.text = mTitle
            binding?.txtMessage?.text = mMessage
        }

        viewModel.permissionList.observe(viewLifecycleOwner) {
            mPermissionList.clear()
            mPermissionList.addAll(it)

            // setup recycler view after get the data
            setupPermissionRecyclerView()
        }

        viewModel.permissionCodeName.observe(viewLifecycleOwner) { permissions ->
            if (listener?.isRootPermissionGranted() == false) {
                updateUiState(permissions)
                onShowDialog(permissions, mTitle, mMessage)
            } else {
                listener?.onPermissionGranted()
            }
        }
    }

    private fun updateUiState(permissions: List<String>) {
        if (permissions.size <= mPermissionList.size && isPermissionDialogShown) {
            binding?.permissionPage?.show()
        }

        permissions.forEach {
            val isGranted = isGranted(requireContext(), it)
            mAdapter.updateState(it, isGranted)
        }
    }

    private fun setupPermissionRecyclerView() {
        binding?.lstPermission?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ItemDividerDecoration(requireContext()))
            adapter = mAdapter
        }
    }

    private fun onShowDialog(permissionCodeNameList: List<String>, title: String, message: String) {
        if (isPermissionDialogShown) return

        DialogUnify(requireContext(), DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            isPermissionDialogShown = true

            setImageDrawable(R.drawable.bg_picker_permission_illustration)
            setPrimaryCTAText(getString(R.string.picker_button_activation))
            setTitle(title)
            setDescription(message)
            setOverlayClose(false)

            setPrimaryCTAClickListener {
                permissionManager?.requestPermissions(
                    permissionCodeNameList,
                    PERMISSION_REQUEST_CODE
                )

                dismiss()
            }
        }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            permissionManager?.onRequestPermissionsResult(permissions, grantResults)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    override fun initInjector() {}

    override fun getScreenName() = "Permission"

    interface Listener {
        fun onPermissionGranted()
        fun isRootPermissionGranted(): Boolean
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 789
    }
}
