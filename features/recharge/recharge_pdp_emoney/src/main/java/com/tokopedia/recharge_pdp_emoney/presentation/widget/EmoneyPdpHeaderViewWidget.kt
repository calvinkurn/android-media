package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.WidgetEmoneyHeaderViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 29/03/21
 */
class EmoneyPdpHeaderViewWidget @JvmOverloads constructor(@NotNull context: Context,
                                                          attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding: WidgetEmoneyHeaderViewBinding
    
    init {
        binding = WidgetEmoneyHeaderViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    companion object {
        private const val MAX_CHAR_EMONEY_CARD_NUMBER_BLOCK = 4
    }

    var titleText: String = ""
        set(title) {
            field = title
            binding.emoneyHeaderViewTitle.text = title
        }

    var subtitleText: String = ""
        set(subtitle) {
            field = subtitle
            binding.emoneyHeaderViewSubtitle.text = subtitle
        }

    var buttonCtaText: String = ""
        set(buttonText) {
            field = buttonText
            binding.emoneyHeaderViewCtaButton.text = buttonText
        }

    var emoneyHeaderViewCardBalanceText: String = ""
        set(emoneyBalance) {
            field = emoneyBalance
            binding.emoneyHeaderViewCardBalance.text = emoneyBalance
        }

    var emoneyHeaderViewCardNumberText: String = ""
        set(emoneyCardNum) {
            field = emoneyCardNum
            binding.emoneyHeaderViewCardNumber.text = emoneyCardNum
        }

    var actionListener: ActionListener? = null

    fun setEmoneyHeaderViewButtonListener(listener: () -> Unit) {
        binding.emoneyHeaderViewCtaButton.setOnClickListener { listener.invoke() }
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

    fun configureUpdateBalanceWithCardNumber(cardNumber: String, cardBalance: String) {
        emoneyHeaderViewCardNumberText = separateNumber(cardNumber)
        emoneyHeaderViewCardBalanceText = cardBalance
        buttonCtaText = resources.getString(R.string.recharge_pdp_emoney_check_saldo_cta)
        setEmoneyHeaderViewButtonListener {
            actionListener?.onClickCheckBalance()
        }
        showEmoneyHeaderWithCardNum()
    }

    private fun separateNumber(number: String): String {
        var cardNumber = ""
        //to seperate emoney card number into 4
        //e.g. card number: 1234567890123456 will be converted to 1234 5678 9012 3456
        for ((i, char) in number.withIndex()) {
            if (i > 0 && i % MAX_CHAR_EMONEY_CARD_NUMBER_BLOCK == 0) cardNumber += " "
            cardNumber += char
        }
        return cardNumber
    }

    private fun showEmoneyHeaderWoCardNum() {
        binding.emoneyHeaderViewWoCardNum.show()
        binding.emoneyHeaderViewWithCardNum.hide()
    }

    private fun showEmoneyHeaderWithCardNum() {
        binding.emoneyHeaderViewWoCardNum.hide()
        binding.emoneyHeaderViewWithCardNum.show()
    }

    interface ActionListener {
        fun onClickCheckBalance()
    }
}
