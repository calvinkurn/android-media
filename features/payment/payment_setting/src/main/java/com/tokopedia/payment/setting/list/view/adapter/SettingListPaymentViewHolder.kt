package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.getSpacedTextPayment
import kotlinx.android.synthetic.main.item_setting_list_payment.view.*

class SettingListPaymentViewHolder(itemView : View?) : AbstractViewHolder<SettingListPaymentModel>(itemView) {

    override fun bind(element: SettingListPaymentModel?) {
        if (element == null) return

        ImageHandler.LoadImage(itemView.cardImage, element.cardTypeImage)
        itemView.cardBankName.text = element.bank
        itemView.cardNumber.text = itemView.context.getString(
            R.string.payment_label_card_info_setting,
            element.maskedNumber?.takeLast(CARD_NUMBER_SHOWN_DIGIT),
        )
    }

    companion object {
        val LAYOUT = R.layout.item_setting_list_payment
        private const val CARD_NUMBER_SHOWN_DIGIT = 4
    }
}
