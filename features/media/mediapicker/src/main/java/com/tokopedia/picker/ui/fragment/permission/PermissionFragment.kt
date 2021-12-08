package com.tokopedia.picker.ui.fragment.permission

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.FragmentPermissionBinding
import com.tokopedia.picker.ui.common.PickerPageType
import com.tokopedia.picker.ui.fragment.PickerUiConfig
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request
import com.tokopedia.utils.view.binding.viewBinding

class PermissionFragment : BaseDaggerFragment() {

    private val binding by viewBinding<FragmentPermissionBinding>()
    private val permissionHelper by lazy { PermissionCheckerHelper() }
    private val _permissions = mutableListOf<String>()
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
        binding?.btnAction?.setOnClickListener {
            setPermission()
        }
    }

    private fun setPermission() {
        when (PickerUiConfig.paramPage) {
            PickerPageType.COMMON -> {
                _permissions.addAll(listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ))
            }
            PickerPageType.CAMERA -> {
                _permissions.addAll(listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
                ))
            }
            PickerPageType.GALLERY -> {
                _permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        permissionHelper.request(
            requireActivity(),
            _permissions.toTypedArray(), {
                listener?.granted()
            }, {
                requireActivity().finish()
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionHelper.onRequestPermissionsResult(
            requireContext(),
            requestCode,
            _permissions.toTypedArray(),
            grantResults
        )
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
        fun granted()
    }

}