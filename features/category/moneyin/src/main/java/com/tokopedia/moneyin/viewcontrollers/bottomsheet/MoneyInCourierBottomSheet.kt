package com.tokopedia.moneyin.viewcontrollers.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import com.tokopedia.moneyin.R
import com.tokopedia.moneyin.model.MoneyInCourierResponse.ResponseData.RatesV4.Data.Service.Product.Features.MoneyIn
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class MoneyInCourierBottomSheet : BottomSheetUnify() {
    private var contentView: View? = null
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun init() {
        showCloseIcon = false
        showKnob = false
        setTitle(getString(R.string.select_shipping))
        contentView = View.inflate(context,
                R.layout.money_in_bottom_sheet_courier, null)
        if(arguments!=null){
            moneyIn = arguments?.getParcelable(KEY_MONEY_IN)
            description = arguments?.getString(KEY_DESCRIPTION)
        }
        val shipperButton = contentView?.findViewById<Typography>(R.id.shipper_name)
        val exchangeText = contentView?.findViewById<Typography>(R.id.exchange_text)
        val price = contentView?.findViewById<Typography>(R.id.price)
        val parentLayout = contentView?.findViewById<ConstraintLayout>(R.id.parent_layout)

        shipperButton?.text = moneyIn?.shipperName
        price?.text = moneyIn?.textPrice
        exchangeText?.text = description
        parentLayout?.setOnClickListener {
            actionListener?.onCourierButtonClick(moneyIn?.shipperName, moneyIn?.textPrice)
            dismiss()
        }
        setChild(contentView)
    }

    fun setActionListener(actionListener: ActionListener?){
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onCourierButtonClick(shipperName:String?, price: String?)
    }
}