package com.tokopedia.tradein.view.bottomsheet

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.TradeInDetailModel.GetTradeInDetail.LogisticOption.DiagnosticReview
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.DoubleTextView

class TradeInDiagnosticReviewBS: BottomSheetUnify() {
    private var contentView: View? = null

    companion object {
        private const val DEVICE_REVIEW = "DEVICE_REVIEW"

        fun newInstance(deviceReview : ArrayList<DiagnosticReview>): TradeInDiagnosticReviewBS {
            return TradeInDiagnosticReviewBS().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(DEVICE_REVIEW, deviceReview)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        setTitle(getString(R.string.tradein_rincian_hp_kamu))
        contentView = View.inflate(context,
                R.layout.tradein_diagnostic_review_bs, null)
        contentView?.findViewById<LinearLayout>(R.id.linear_layout)?.removeAllViews()
        arguments?.getParcelableArrayList<DiagnosticReview>(DEVICE_REVIEW)?.let {
            val textSize = 14.0f
            for(review in it){
                val doubleTextView = DoubleTextView(activity, LinearLayout.HORIZONTAL)
                doubleTextView.apply {
                    setTopText(review.field)
                    setTopTextSize(textSize)
                    setTopTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
                    setBottomTextSize(textSize)
                    setBottomTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                    setBottomTextStyle("bold")
                    setBottomText(review.value)
                    setBottomGravity(Gravity.END)
                    setBottomTextGravity(Gravity.END)
                }
                contentView?.findViewById<LinearLayout>(R.id.linear_layout)?.addView(doubleTextView)
            }
        }
        setChild(contentView)
    }

}
