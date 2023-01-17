package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.buyerorderdetail.databinding.EstimateRefundInfoBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class EstimateRefundInfoBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<EstimateRefundInfoBottomsheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EstimateRefundInfoBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(
            getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_estimate_refund_info_title)
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    companion object {
        const val TAG = "EstimateRefundInfoBottomSheet"

        fun newInstance(): EstimateRefundInfoBottomSheet {
            return EstimateRefundInfoBottomSheet()
        }
    }
}
