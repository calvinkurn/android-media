package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.databinding.EstimateRefundInfoBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.lifecycle.autoClearedNullable

class PofEstimateRefundInfoBottomSheet: BottomSheetUnify() {

    private val title by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(ESTIMATE_REFUND_INFO_TITLE_KEY).orEmpty()
    }

    private val infoList by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(ESTIMATE_REFUND_INFO_LIST_KEY).orEmpty()
    }

    private var binding by autoClearedNullable<EstimateRefundInfoBottomsheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EstimateRefundInfoBottomsheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(title)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInfoList()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setupInfoList() {
        binding?.run {
            val infoListHtml = HtmlLinkHelper(root.context, infoList)
            tvPofEstimateRefundInfoBottomSheetDesc.text = infoListHtml.spannedString
        }
    }

    companion object {
        private val TAG = PofEstimateRefundInfoBottomSheet::class.java.simpleName
        const val ESTIMATE_REFUND_INFO_TITLE_KEY = "EstimateRefundInfoTitleKey"
        const val ESTIMATE_REFUND_INFO_LIST_KEY = "EstimateRefundInfoListKey"

        fun newInstance(title: String, info: String): PofEstimateRefundInfoBottomSheet {
            return PofEstimateRefundInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ESTIMATE_REFUND_INFO_TITLE_KEY, title)
                    putString(ESTIMATE_REFUND_INFO_LIST_KEY, info)
                }
            }
        }
    }
}
