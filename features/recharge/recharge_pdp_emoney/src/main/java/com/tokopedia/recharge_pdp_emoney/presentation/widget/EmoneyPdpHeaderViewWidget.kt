package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_emoney_header_view.view.*
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 29/03/21
 */
class EmoneyPdpHeaderViewWidget @JvmOverloads constructor(@NotNull context: Context,
                                                          attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_emoney_header_view, this, true)
    }

    var titleText: String = ""
        set(title) {
            field = title
            emoneyHeaderViewTitle.text = title
        }

    var subtitleText: String = ""
        set(subtitle) {
            field = subtitle
            emoneyHeaderViewSubtitle.text = subtitle
        }

    var buttonCtaText: String = ""
        set(buttonText) {
            field = buttonText
            emoneyHeaderViewCtaButton.text = buttonText
        }

    var emoneyHeaderViewCardBalanceText: String = ""
        set(emoneyBalance) {
            field = emoneyBalance
            emoneyHeaderViewCardBalance.text = emoneyBalance
        }

    var emoneyHeaderViewCardNumberText: String = ""
        set(emoneyCardNum) {
            field = emoneyCardNum
            emoneyHeaderViewCardNumber.text = emoneyCardNum
        }

    var actionListener: ActionListener? = null

    fun setEmoneyHeaderViewButtonListener(listener: () -> Unit) {
        emoneyHeaderViewCtaButton.setOnClickListener { listener.invoke() }
    }

    fun configureCheckBalanceView() {
        titleText = resources.getString(R.string.recharge_pdp_emoney_check_saldo_title)
        subtitleText = resources.getString(R.string.recharge_pdp_emoney_check_saldo_description)
        buttonCtaText = resources.getString(R.string.recharge_pdp_emoney_check_saldo_cta)
        setEmoneyHeaderViewButtonListener {
            actionListener?.onClickCheckBalance()
        }
        showEmoneyHeaderWoCardNum()
    }

    fun configureUpdateBalanceView() {
        titleText = resources.getString(R.string.recharge_pdp_emoney_update_saldo_title)
        subtitleText = resources.getString(R.string.recharge_pdp_emoney_update_saldo_description)
        buttonCtaText = resources.getString(R.string.recharge_pdp_emoney_update_saldo_cta)
        setEmoneyHeaderViewButtonListener {
            actionListener?.onClickUpdateBalance()
        }
        showEmoneyHeaderWoCardNum()
    }

    fun configureUpdateBalanceWithCardNumber(cardNumber: String, cardBalance: String) {
        emoneyHeaderViewCardNumberText = cardNumber
        emoneyHeaderViewCardBalanceText = cardBalance
        buttonCtaText = resources.getString(R.string.recharge_pdp_emoney_update_saldo_cta)
        setEmoneyHeaderViewButtonListener {
            actionListener?.onClickUpdateBalance()
        }
        showEmoneyHeaderWithCardNum()
    }

    private fun showEmoneyHeaderWoCardNum() {
        emoneyHeaderViewWoCardNum.show()
        emoneyHeaderViewWithCardNum.hide()
    }

    private fun showEmoneyHeaderWithCardNum() {
        emoneyHeaderViewWoCardNum.hide()
        emoneyHeaderViewWithCardNum.show()
    }

    interface ActionListener {
        fun onClickCheckBalance()
        fun onClickUpdateBalance()
    }
}