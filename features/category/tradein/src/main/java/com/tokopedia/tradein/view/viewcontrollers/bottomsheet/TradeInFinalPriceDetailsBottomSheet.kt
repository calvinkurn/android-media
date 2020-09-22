package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorder.common.view.DoubleTextView
import com.tokopedia.tradein.R
import kotlinx.android.synthetic.main.layout_activity_tradein_info.*

class TradeInFinalPriceDetailsBottomSheet: BottomSheetDialogFragment() {
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
        return inflater.inflate(R.layout.tradein_final_price_detail_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        linear_layout.removeAllViews()
        arguments?.getStringArrayList(DEVICE_REVIEW)?.let {
            for(review in it){
                val doubleTextView = DoubleTextView(activity, LinearLayout.HORIZONTAL)
                doubleTextView.apply {
                    setTopText(review.substringBefore(":"))
                    setTopTextSize(14.0f)
                    setTopTextColor(MethodChecker.getColor(context, R.color.clr_AD31353B))
                    setBottomTextSize(14.0f)
                    setBottomTextColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.clr_f531353b))
                    setBottomTextStyle("bold")
                    setBottomText(review.substringAfter(":"))
                }
                linear_layout.addView(doubleTextView)
            }
        }
        close_button.setOnClickListener {
            dialog?.hide()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet: FrameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return bottomSheetDialog
    }

}