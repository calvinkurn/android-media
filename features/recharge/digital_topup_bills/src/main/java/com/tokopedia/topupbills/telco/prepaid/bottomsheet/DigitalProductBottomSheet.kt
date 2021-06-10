package com.tokopedia.topupbills.telco.prepaid.bottomsheet

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.topupbills.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton

class DigitalProductBottomSheet : BottomSheetUnify() {

    private lateinit var details: TextView
    private lateinit var productPrice: TextView
    private lateinit var productPromoPrice: TextView
    private lateinit var selectItemBtn: UnifyButton
    private lateinit var listener: ActionListener

    private var productId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheet_product_see_more, null)
        setChild(view)
        initView(view)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    private fun initView(view: View?) {
        view?.run {
            details = view.findViewById(R.id.telco_details)
            productPrice = view.findViewById(R.id.telco_product_price)
            selectItemBtn = view.findViewById(R.id.telco_button_select_item)
            productPromoPrice = view.findViewById(R.id.telco_product_promo_price)

            arguments?.let {
                setTitle(it.getString(TITLE, ""))
                val promo_price = it.getString(PROMO_PRICE)
                val price = it.getString(PRICE)
                details.text = it.getString(DETAILS)
                productPrice.text = price

                if(!promo_price.isNullOrEmpty()){
                    productPrice.text = promo_price
                    productPromoPrice.text = price
                    productPromoPrice.paintFlags = productPromoPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    productPromoPrice.visibility = View.VISIBLE
                }

                selectItemBtn.setOnClickListener {
                    if (::listener.isInitialized) {
                        listener.onClickOnProduct()
                    } else {
                        activity?.let { it2 ->
                            Toaster.build(it2.findViewById(android.R.id.content),
                                    getString(R.string.digital_telco_error_try_again),
                                    Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                        }
                    }
                    dismiss()
                }
            }
        }
    }

    interface ActionListener {
        fun onClickOnProduct()
    }

    companion object {

        private const val TITLE = "title"
        private const val DETAILS = "details"
        private const val PRICE = "price"
        private const val PROMO_PRICE = "promo_price"

        fun newInstance(
                title: String,
                details: String,
                price: String,
                promoPrice : String?,
                listener: ActionListener
        ): DigitalProductBottomSheet {
            val fragment = DigitalProductBottomSheet().apply {
                setListener(listener)
            }
            val bundle = Bundle()
            bundle.putString(DETAILS, details)
            bundle.putString(PRICE, price)
            bundle.putString(TITLE, title)
            bundle.putString(PROMO_PRICE, promoPrice)
            fragment.arguments = bundle
            return fragment
        }
    }
}