package com.tokopedia.gopay_kyc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.gopay_kyc.R
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.gopay_kyc_upload_failed_layout.*

class GoPayKycUploadFailedBottomSheet : BottomSheetUnify() {
    private val childLayoutRes = R.layout.gopay_kyc_upload_failed_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun setDefaultParams() {
        setTitle(TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = getScreenHeight() / 2.toDp()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retryUploadButton.setOnClickListener {
            // retry Upload
        }
    }

    companion object {
        const val TITLE = "Upgrade GoPay Plus"
        private const val TAG = "KycFailedBottomSheet"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val fragment = GoPayKycUploadFailedBottomSheet()
            fragment.show(childFragmentManager, TAG)
        }
    }
}