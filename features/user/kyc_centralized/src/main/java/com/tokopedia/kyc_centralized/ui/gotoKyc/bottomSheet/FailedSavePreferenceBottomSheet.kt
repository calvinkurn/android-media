package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutKycFailedSavePreferenceBinding
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class FailedSavePreferenceBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutKycFailedSavePreferenceBinding>()
    private var isReload = false

    private var dismissDialogWithDataListener: (Boolean) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutKycFailedSavePreferenceBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitImage()
        initListener()
    }

    private fun initListener() {
        binding?.btnTryAgain?.setOnClickListener {
            isReload = true
            dismiss()
        }
    }

    private fun loadInitImage() {
        binding?.ivFailed?.loadImageWithoutPlaceholder(
            getString(R.string.img_url_goto_kyc_fail_save_preference)
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissDialogWithDataListener(isReload)
    }

    fun setOnDismissWithDataListener(isReload: (Boolean) -> Unit) {
        dismissDialogWithDataListener = isReload
    }

}
