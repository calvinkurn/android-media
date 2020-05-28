package com.tokopedia.topupbills.telco.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class DigitalProductBottomSheet : BottomSheetUnify() {

    private lateinit var details: TextView
    private lateinit var productPrice: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheet_product_see_more, null)
        setChild(view)
        initView(view)
    }

    private fun initView(view: View?) {
        view?.run {
            details = view.findViewById(R.id.details)
            productPrice = view.findViewById(R.id.product_price)

            arguments?.let {
                setTitle(it.getString(TITLE))
                details.text = it.getString(DETAILS)
                productPrice.text = it.getString(PRICE)
            }
        }
    }

    companion object {

        private const val TITLE = "title"
        private const val DETAILS = "details"
        private const val PRICE = "price"

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