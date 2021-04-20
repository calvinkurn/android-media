package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
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
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.unifycomponents.UnifyButton
import java.net.URLEncoder

class TradeInOutsideCoverageBottomSheet : BottomSheetDialogFragment() {

    var tradeInAnalytics: TradeInAnalytics? = null

    companion object {
        private const val PRODUCT_NAME = "PRODUCT_NAME"
        const val ENCODING_UTF_8 = "UTF-8"
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
        arguments?.getString(PRODUCT_NAME)?.let { productName ->
            view.findViewById<UnifyButton>(R.id.btn_similar_items).setOnClickListener {
                tradeInAnalytics?.clickCoverageAreaSimilarItems()
                RouteManager.route(context, getString(R.string.tradein_search_deeplink, URLEncoder.encode(productName, ENCODING_UTF_8)))
                dialog?.dismiss()
            }
        }
        view.findViewById<UnifyButton>(R.id.btn_close).setOnClickListener {
            tradeInAnalytics?.clickCoverageAreaCloseSheet()
            dialog?.dismiss()
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