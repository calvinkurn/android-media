package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.databinding.PartialOrderFulfillmentBottomsheetBinding
import com.tokopedia.buyerorderdetail.databinding.PofDetailRefundedBottomsheetBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.ItemPofDetailRefundedAdapter
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailRefundedUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundSummaryUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofSummaryInfoUiModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PofDetailRefundedBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<PofDetailRefundedBottomsheetBinding>()

    private var pofRefundSummaryUiModel: PofRefundSummaryUiModel? = null

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

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setupDetailRefunded() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID)
            )
        }
        pofRefundSummaryUiModel = cacheManager?.get<PofRefundSummaryUiModel>(
            KEY_DETAIL_REFUNDED_UI_MODEL,
            PofRefundSummaryUiModel::class.java
        )
    }


    private fun setupView() {
        pofRefundSummaryUiModel?.let {
            binding?.run {
                setupRvDetailRefunded(it.detailsSummary)
                tvPofTotalRefundedLabel.text = it.totalAmountLabel
                tvPofTotalRefundedValue.text = it.totalAmountValue
                tvPofRefundedDetailFooter.text = it.footerInfo
            }
        }
    }

    private fun PofDetailRefundedBottomsheetBinding.setupRvDetailRefunded(detailSummaryList: List<PofSummaryInfoUiModel>) {
        rvDetailRefunded.run {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemPofDetailRefundedAdapter(detailSummaryList)
        }
    }


    companion object {
        private val TAG = PofDetailRefundedBottomSheet::class.java.simpleName
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
