package com.tokopedia.payment.setting.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.PaymentSettingRouter
import com.tokopedia.payment.setting.util.getBackgroundAssets
import com.tokopedia.payment.setting.util.getMaskedNumberSubStringPayment
import kotlinx.android.synthetic.main.item_setting_list_payment.view.*

class SettingListPaymentViewHolder(itemView : View?, val paymentSettingRouter: PaymentSettingRouter) : AbstractViewHolder<SettingListPaymentModel>(itemView) {

    override fun bind(element: SettingListPaymentModel?) {
        ImageHandler.LoadImage(itemView.imageCCBackground, element?.getBackgroundAssets(paymentSettingRouter, itemView.context))
        itemView.imageCCBackground.setOnClickListener{
            //todo implement credit card detail
        }

        itemView.cardNumber.setText(element?.maskedNumber?.getMaskedNumberSubStringPayment())
        ImageHandler.LoadImage(itemView.cardImage, element?.cardType)
    }

    companion object {
        val LAYOUT = R.layout.item_setting_list_payment
    }
}
