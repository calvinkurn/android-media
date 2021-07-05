package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.utils.view.DoubleTextView
import com.tokopedia.tradein.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class TradeInFinalPriceDetailsBottomSheet: BottomSheetUnify() {
    private var contentView: View? = null

    companion object {
        private const val DEVICE_REVIEW = "DEVICE_REVIEW"

        fun newInstance(deviceReview : ArrayList<String>): TradeInFinalPriceDetailsBottomSheet {
            return TradeInFinalPriceDetailsBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(DEVICE_REVIEW, deviceReview)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = false
        showKnob = true
        setTitle(getString(R.string.tradein_detail_pengecekan))
        contentView = View.inflate(context,
                R.layout.tradein_final_price_detail_bottom_sheet, null)
        contentView?.findViewById<LinearLayout>(R.id.linear_layout)?.removeAllViews()
        arguments?.getStringArrayList(DEVICE_REVIEW)?.let {
            val textSize = 14.0f
            for(review in it){
                val doubleTextView = DoubleTextView(activity, LinearLayout.HORIZONTAL)
                doubleTextView.apply {
                    setTopText(review.substringBefore(":"))
                    setTopTextSize(textSize)
                    setTopTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    setBottomTextSize(textSize)
                    setBottomTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                    setBottomTextStyle("bold")
                    setBottomText(review.substringAfter(":"))
                    setBottomGravity(Gravity.END)
                }
                contentView?.findViewById<LinearLayout>(R.id.linear_layout)?.addView(doubleTextView)
            }
        }
        setChild(contentView)
    }

}