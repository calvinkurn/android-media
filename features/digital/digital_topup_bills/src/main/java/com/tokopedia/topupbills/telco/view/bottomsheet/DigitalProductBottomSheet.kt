package com.tokopedia.topupbills.telco.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.topupbills.R

class DigitalProductBottomSheet : BottomSheets() {

    private lateinit var details: TextView
    private lateinit var productPrice: TextView

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_product_see_more
    }

    override fun title(): String {
        arguments?.let {
            return it.getString(TITLE)
        }
        return ""
    }

    override fun initView(view: View?) {
        view?.run {
            details = view.findViewById(R.id.details)
            productPrice = view.findViewById(R.id.product_price)

            arguments?.let {
                details.setText(it.getString(DETAILS))
                productPrice.setText(it.getString(PRICE))
            }
        }
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        if (metrics.heightPixels > 0) {
            if (bottomSheetBehavior != null)
                bottomSheetBehavior.peekHeight = metrics.heightPixels / 3
        }
    }

    companion object {

        private val TITLE = "title"
        private val DETAILS = "details"
        private val PRICE = "price"

        fun newInstance(title: String, details: String, price: String): DigitalProductBottomSheet {
            val fragment = DigitalProductBottomSheet()
            val bundle = Bundle()
            bundle.putString(DETAILS, details)
            bundle.putString(PRICE, price)
            bundle.putString(TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }
}