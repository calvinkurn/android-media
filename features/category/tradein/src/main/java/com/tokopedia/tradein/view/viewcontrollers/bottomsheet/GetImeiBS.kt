package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4.Data.Service.Product.Features.MoneyIn
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.UnifyButton

class GetImeiBS : BottomSheetUnify() {
    private var moneyIn: MoneyIn? = null
    private var description: String? = null
    private var actionListener: ActionListener? = null
    private var contentView: View? = null

    companion object {
        private const val KEY_MONEY_IN = "KEY_MONEY_IN"
        private const val KEY_DESCRIPTION = "KEY_DESCRIPTION"

        @JvmStatic
        fun newInstance(): GetImeiBS {
            return GetImeiBS()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        setTitle(getString(R.string.select_shipping))
        showCloseIcon = false
        showKnob = true
        contentView = View.inflate(context,
                R.layout.tradein_bs_get_imei, null)

        val btnContinue = contentView?.findViewById<UnifyButton>(R.id.btn_continue)
        val etWrapper = contentView?.findViewById<TextAreaUnify>(R.id.wrapper_imei)
//        etWrapper?.setHelper("Tekan *#06# untuk cek IMEI atau dengan cara berikut")

        btnContinue?.setOnClickListener {
            //actionListener?.onCourierButtonClick(moneyIn?.shipperName, moneyIn?.textPrice)

            if (etWrapper?.textAreaInput?.text.toString().isEmpty()) {
                etWrapper?.isError = true
                etWrapper?.textAreaMessage = "Kamu belum memasukkan no. IMEI"
            } else {
                dismiss()
            }
        }
        setChild(contentView)
    }

    fun setActionListener(actionListener: ActionListener?) {
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onCourierButtonClick(shipperName: String?, price: String?)
    }
}