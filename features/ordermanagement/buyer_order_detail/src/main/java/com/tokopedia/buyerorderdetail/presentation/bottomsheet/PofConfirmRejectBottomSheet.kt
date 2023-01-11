package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorderdetail.databinding.PofConfirmRejectBottomsheetBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PofConfirmRejectBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<PofConfirmRejectBottomsheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PofConfirmRejectBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        showCloseIcon = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding?.run {
            ivPovConfirmReject.loadImage("")
            setBtnPrimaryCancellation()
            setBtnSecondaryBack()
        }
    }

    private fun setBtnPrimaryCancellation() {
        binding?.btnSecondaryBack?.setOnClickListener {
            showBtnPrimaryLoading()
        }
    }

    private fun showBtnPrimaryLoading() {
        binding?.btnSecondaryBack?.isLoading = true
    }

    private fun hideBtnPrimaryLoading() {
        binding?.btnSecondaryBack?.isLoading = false
    }

    private fun setBtnSecondaryBack() {
        binding?.btnPrimaryCancellation?.setOnClickListener {
            dismiss()
        }
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    companion object {
        const val TAG = "PofConfirmRejectBottomSheet"
    }
}

