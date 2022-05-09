package com.tokopedia.media.picker.ui.fragment.permission

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.databinding.FragmentPermissionBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.ui.fragment.permission.recyclers.adapter.PermissionAdapter
import com.tokopedia.media.picker.ui.fragment.permission.recyclers.utils.ItemDividerDecoration
import com.tokopedia.media.picker.ui.uimodel.PermissionUiModel
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class PermissionFragment : BaseDaggerFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding by viewBinding<FragmentPermissionBinding>()
    private var listener: Listener? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[PermissionViewModel::class.java]
    }

    private val mPermissionList = mutableListOf<PermissionUiModel>()
    private val mAdapter by lazy { PermissionAdapter(mPermissionList) }

    private var isPermissionRationale = false
    private var mTitle = ""
    private var mMessage = ""

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
        initObservable()
        initView()
    }

    override fun onResume() {
        super.onResume()
        onPrepareShowPermissionDialog()
    }

    private fun initView() {
        viewModel.getDynamicPermissionList()
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
    }

    private fun setupPermissionRecyclerView() {
        binding?.lstPermission?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ItemDividerDecoration(requireContext()))
            adapter = mAdapter
        }
    }

    private fun onPrepareShowPermissionDialog() {
        val permissions = viewModel.permissionCodeName.value?: return
        var permissionGrantedAmount = 0

        for (permission in permissions) {
            if (checkSelfPermission(requireContext(), permission) == PERMISSION_GRANTED) {
                mAdapter.updateState(permission, true)
                permissionGrantedAmount++
            }
        }

        if (permissions.size == permissionGrantedAmount) {
            listener?.onPermissionGranted()
        } else {
            onShowDialog(mTitle, mMessage)
        }
    }

    private fun onShowDialog(title: String, message: String) {
        val permissions = viewModel.permissionCodeName.value?: return
        if (isPermissionRationale) return

        DialogUnify(requireContext(), DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
            setImageDrawable(R.drawable.bg_picker_permission_illustration)
            setPrimaryCTAText(getString(R.string.picker_button_activation))
            setTitle(title)
            setDescription(message)
            setOverlayClose(false)

            setPrimaryCTAClickListener {
                requestPermissions(permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
                dismiss()
            }
        }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val deniedPermissions = mutableListOf<String>()
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (i in permissions.indices) {
                val permission = permissions[i]

                if (grantResults.isNotEmpty() && grantResults[i] == PERMISSION_DENIED) {
                    deniedPermissions.add(permission)
                    mAdapter.updateState(permission, false)
                } else {
                    mAdapter.updateState(permission, true)
                }
            }

            if (permissions.isNotEmpty() && deniedPermissions.isEmpty()) {
                listener?.onPermissionGranted()
            } else {
                binding?.permissionPage?.show()
                isPermissionRationale = true
            }
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

    override fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun getScreenName() = "Permission"

    interface Listener {
        fun onPermissionGranted()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 789
    }

}