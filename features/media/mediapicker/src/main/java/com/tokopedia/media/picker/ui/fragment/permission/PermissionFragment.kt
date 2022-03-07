package com.tokopedia.media.picker.ui.fragment.permission

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.media.R
import com.tokopedia.media.common.types.PickerPageType
import com.tokopedia.media.databinding.FragmentPermissionBinding
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.fragment.permission.recyclers.adapter.PermissionAdapter
import com.tokopedia.media.picker.ui.fragment.permission.recyclers.utils.ItemDividerDecoration
import com.tokopedia.media.picker.ui.uimodel.PermissionUiModel
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.view.binding.viewBinding

open class PermissionFragment : BaseDaggerFragment() {

    private val binding by viewBinding<FragmentPermissionBinding>()
    private val permissionHelper by lazy { PermissionCheckerHelper() }

    private val _permissions = mutableListOf<PermissionUiModel>()
    private var listener: Listener? = null

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

    private fun setupView() {
        initPermission()
        setupPermissionList()
    }

    private fun initPermission() {
        _permissions.clear()

        when (PickerUiConfig.paramPage) {
            PickerPageType.COMMON -> {
                _permissions.addAll(listOf(
                    PermissionUiModel.storage(),
                    PermissionUiModel.camera(),
                    PermissionUiModel.microphone(),
                ))
            }
            PickerPageType.CAMERA -> {
                _permissions.addAll(listOf(
                    PermissionUiModel.camera(),
                    PermissionUiModel.microphone(),
                ))
            }
            PickerPageType.GALLERY -> {
                _permissions.add(
                    PermissionUiModel.storage(),
                )
            }
        }
    }

    private fun setupPermissionList() {
        binding?.lstPermission?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = PermissionAdapter(_permissions)

            addItemDecoration(ItemDividerDecoration(requireContext()))
        }
    }

    private fun setPermission() {
//        permissionHelper.checkPermissions(
//            this,
//            _permissions.toTypedArray(),
//            object : PermissionCheckerHelper.PermissionCheckListener {
//                override fun onPermissionDenied(permissionText: String) {
//                    permissionHelper.onPermissionDenied(requireContext(), permissionText)
//                }
//
//                override fun onNeverAskAgain(permissionText: String) {
//                    permissionHelper.onNeverAskAgain(requireContext(), permissionText)
//                }
//
//                override fun onPermissionGranted() {
//                    listener?.onPermissionGranted()
//                }
//        })
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        permissionHelper.onRequestPermissionsResult(
//            requireContext(),
//            requestCode,
//            permissions,
//            grantResults
//        )
//    }

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
    }

}