package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4.Data.Service.Product.Features.MoneyIn
import com.tokopedia.unifycomponents.UnifyButton

class GetImeiBS : BottomSheets() {
    private var moneyIn: MoneyIn? = null
    private var description: String? = null
    private var actionListener: ActionListener? = null

    companion object {
        private const val KEY_MONEY_IN = "KEY_MONEY_IN"
        private const val KEY_DESCRIPTION = "KEY_DESCRIPTION"

        @JvmStatic
        fun newInstance(): GetImeiBS {
            return GetImeiBS()
        }
    }

    override fun title(): String = getString(R.string.select_shipping)

    override fun state(): BottomSheetsState = BottomSheetsState.FLEXIBLE

    override fun getLayoutResourceId(): Int = R.layout.tradein_bs_get_imei

    override fun initView(view: View?) {
//        val shipperButton = view?.findViewById<TextView>(R.id.shipper_name)
//        val exchangeText = view?.findViewById<TextView>(R.id.exchange_text)
//        val price = view?.findViewById<TextView>(R.id.price)
        val btnContinue = view?.findViewById<UnifyButton>(R.id.btn_continue)
        val etWrapper = view?.findViewById<TkpdHintTextInputLayout>(R.id.wrapper_imei)
        val etImei = view?.findViewById<EditText>(R.id.et_imei)
        etWrapper?.setHelper("Tekan *#06# untuk cek IMEI atau dengan cara berikut")

//
//        shipperButton?.text = moneyIn?.shipperName
//        price?.text = moneyIn?.textPrice
//        exchangeText?.text = description
        btnContinue?.setOnClickListener {
            //actionListener?.onCourierButtonClick(moneyIn?.shipperName, moneyIn?.textPrice)

            when(etImei?.text.toString() ) {
                "" -> etWrapper?.error = "Kamu belum memasukkan no. IMEI"
            }

            dismiss()
        }
    }

    fun setActionListener(actionListener: ActionListener?) {
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onCourierButtonClick(shipperName: String?, price: String?)
    }
}