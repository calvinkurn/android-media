package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.databinding.PartialOrderFulfillmentBottomsheetBinding
import com.tokopedia.buyerorderdetail.databinding.PofDetailRefundedBottomsheetBinding
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailRefundedUiModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PofDetailRefundedBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<PofDetailRefundedBottomsheetBinding>()

    private var pofDetailRefundedUiModel: PofDetailRefundedUiModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = PofDetailRefundedBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(
            getString(com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_detail_refunded_bottomsheet_title)
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDetailRefunded()
        setupView()
    }

    private fun setupDetailRefunded() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID)
            )
        }
        pofDetailRefundedUiModel = cacheManager?.get<PofDetailRefundedUiModel>(
            KEY_DETAIL_REFUNDED_UI_MODEL,
            PofDetailRefundedUiModel::class.java
        )
    }


    private fun setupView() {
        pofDetailRefundedUiModel?.let {
            binding?.run {
                tvPofUnfulfilledDetailLabel.text =
                    it.summaryInfoLabel
                tvPofUnfulfilledDetailValue.text = it.summaryInfoValue
                tvPofTotalRefundedLabel.text = it.refundTotalAmountLabel
                tvPofTotalRefundedValue.text = it.refundTotalAmountValue
                tvPofRefundedDetailFooter.text = it.summaryFooter
            }
        }
    }


    companion object {
        const val KEY_DETAIL_REFUNDED_UI_MODEL = "key_detail_refunded_ui_model"
        private const val KEY_CACHE_MANAGER_ID = "extra_cache_manager_id"

        fun newInstance(
            cacheManagerId: String
        ): PofDetailRefundedBottomSheet {
            return PofDetailRefundedBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(KEY_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }
}
