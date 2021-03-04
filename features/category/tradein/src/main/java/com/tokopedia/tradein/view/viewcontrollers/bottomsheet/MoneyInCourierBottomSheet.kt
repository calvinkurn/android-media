package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.widget.TextView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4.Data.Service.Product.Features.MoneyIn

class MoneyInCourierBottomSheet : BottomSheets() {
    private var moneyIn: MoneyIn? = null
    private var description: String? = null
    private var actionListener: ActionListener? = null

    companion object {
        private const val KEY_MONEY_IN = "KEY_MONEY_IN"
        private const val KEY_DESCRIPTION = "KEY_DESCRIPTION"

        @JvmStatic
        fun newInstance(moneyIn: MoneyIn?, description: String?): MoneyInCourierBottomSheet {
            return MoneyInCourierBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MONEY_IN,moneyIn)
                    putString(KEY_DESCRIPTION,description)
                }
            }
        }
    }

    override fun title(): String = getString(R.string.select_shipping)

    override fun state(): BottomSheetsState = BottomSheetsState.FLEXIBLE

    override fun getLayoutResourceId(): Int = R.layout.money_in_bottom_sheet_courier

    override fun initView(view: View?) {
        if(arguments!=null){
            moneyIn = arguments?.getParcelable(KEY_MONEY_IN)
            description = arguments?.getString(KEY_DESCRIPTION)
        }
        val shipperButton = view?.findViewById<TextView>(R.id.shipper_name)
        val exchangeText = view?.findViewById<TextView>(R.id.exchange_text)
        val price = view?.findViewById<TextView>(R.id.price)
        val parentLayout = view?.findViewById<ConstraintLayout>(R.id.parent_layout)

        shipperButton?.text = moneyIn?.shipperName
        price?.text = moneyIn?.textPrice
        exchangeText?.text = description
        parentLayout?.setOnClickListener {
            actionListener?.onCourierButtonClick(moneyIn?.shipperName, moneyIn?.textPrice)
            dismiss()
        }
    }

    fun setActionListener(actionListener: ActionListener?){
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onCourierButtonClick(shipperName:String?, price: String?)
    }
}