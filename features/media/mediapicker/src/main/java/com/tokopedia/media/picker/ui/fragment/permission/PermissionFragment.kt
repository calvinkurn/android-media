package com.tokopedia.media.picker.ui.fragment.permission

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.databinding.FragmentPermissionBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.ui.fragment.permission.recyclers.adapter.PermissionAdapter
import com.tokopedia.media.picker.ui.fragment.permission.recyclers.utils.ItemDividerDecoration
import com.tokopedia.media.picker.ui.uimodel.PermissionUiModel
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class PermissionFragment : BaseDaggerFragment() {

    @Inject lateinit var cacheManager: ParamCacheManager

    private val binding by viewBinding<FragmentPermissionBinding>()
    private var listener: Listener? = null

    private val permissionList = mutableListOf<PermissionUiModel>()

    private val permissions by lazy {
        permissionList.map { it.name }
    }

    private val mAdapter by lazy { PermissionAdapter(permissionList) }
    private val param by lazy { cacheManager.get() }

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
        setupView()
    }

    override fun onResume() {
        super.onResume()
        onShowPermissionDialog()
    }

    private fun setupView() {
        initPermissionList()
        initPermissionDynamicWording()
        setupPermissionRecyclerView()

        binding?.txtTitle?.text = mTitle
        binding?.txtMessage?.text = mMessage
    }

    private fun initPermissionList() {
        permissionList.clear()
        permissionList.addAll(
            PermissionUiModel.get(
                param.pageType(),
                param.isImageModeOnly()
            )
        )
    }

    private fun initPermissionDynamicWording() {
        val (_title, _message) = when (param.pageType()) {
            PageType.CAMERA -> if (param.isImageModeOnly()) {
                Pair(
                    getString(R.string.picker_title_camera_photo_permission),
                    getString(R.string.picker_message_camera_photo_permission)
                )
            } else {
                Pair(
                    getString(R.string.picker_title_camera_video_permission),
                    getString(R.string.picker_message_camera_video_permission)
                )
            }
            PageType.GALLERY -> Pair(
                getString(R.string.picker_title_gallery_permission),
                getString(R.string.picker_message_gallery_permission)
            )
            else -> Pair(
                getString(R.string.picker_title_common_permission),
                getString(R.string.picker_message_common_permission)
            )
        }

        mTitle = _title
        mMessage = _message
    }

    private fun setupPermissionRecyclerView() {
        binding?.lstPermission?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ItemDividerDecoration(requireContext()))
            adapter = mAdapter
        }
    }

    private fun onShowPermissionDialog() {
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