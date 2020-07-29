package com.tokopedia.topupbills.telco.prepaid.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.topupbills.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class DigitalProductBottomSheet : BottomSheetUnify() {

    private lateinit var details: TextView
    private lateinit var productPrice: TextView
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

            arguments?.let {
                setTitle(it.getString(TITLE, ""))
                details.text = it.getString(DETAILS)
                productPrice.text = it.getString(PRICE)

                selectItemBtn.setOnClickListener {
                    listener.onClickOnProduct()
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