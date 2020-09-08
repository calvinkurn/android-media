package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tradein.R
import com.tokopedia.unifycomponents.UnifyButton

class TradeInOutsideCoverageBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val PRODUCT_NAME = "PRODUCT_NAME"
        fun newInstance(productName: String): TradeInOutsideCoverageBottomSheet {
            return TradeInOutsideCoverageBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_NAME, productName)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tradein_outside_coverage_bottom_sheet, container, false)
        arguments?.getString(PRODUCT_NAME)?.let {
            view.findViewById<UnifyButton>(R.id.btn_similar_items).setOnClickListener {
                RouteManager.route(context, "tokopedia://search?q=${it}&trade_id=true&shipping=25")
                dialog?.hide()
            }
        }
        view.findViewById<UnifyButton>(R.id.btn_close).setOnClickListener {
            dialog?.hide()
        }
        return view
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