package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.databinding.ItemSettingListPaymentBinding
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel

class SettingListPaymentViewHolder(
    itemView: View
) : AbstractViewHolder<SettingListPaymentModel>(itemView) {

    private val binding = ItemSettingListPaymentBinding.bind(itemView)

    override fun bind(element: SettingListPaymentModel?) {
        if (element == null) return
        with(binding) {
            cardImage.loadImage(element.cardTypeImage)
            cardBankName.text = element.bank
            cardNumber.text = itemView.context.getString(
                R.string.payment_label_card_info_setting,
                getCardType(element.isDebitOnline),
                element.maskedNumber?.takeLast(CARD_NUMBER_SHOWN_DIGIT)
            )
        }
    }

    private fun getCardType(isDebit: Boolean): String = if (isDebit) DEBIT_STRING else CREDIT_STRING

    companion object {
        val LAYOUT = R.layout.item_setting_list_payment
        private const val CARD_NUMBER_SHOWN_DIGIT = 4
        private const val DEBIT_STRING = "debit"
        private const val CREDIT_STRING = "kredit"
    }
}
