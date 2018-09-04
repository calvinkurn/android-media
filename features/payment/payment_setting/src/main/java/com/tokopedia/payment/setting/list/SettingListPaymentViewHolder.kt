package com.tokopedia.payment.setting.list

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.PaymentSettingRouter
import kotlinx.android.synthetic.main.item_setting_list_payment.view.*

class SettingListPaymentViewHolder(itemView : View?, val paymentSettingRouter: PaymentSettingRouter) : AbstractViewHolder<SettingListPaymentModel>(itemView) {

    override fun bind(element: SettingListPaymentModel?) {
        ImageHandler.LoadImage(itemView.imageCCBackground, getBackgroundAssets(element))
        itemView.imageCCBackground.setOnClickListener{
            //todo implement credit card detail
        }

        itemView.cardNumber.setText(getMaskedNumberSubString(element?.maskedNumber?:""))
        ImageHandler.LoadImage(itemView.cardImage, element?.cardType)
    }

    private fun getBackgroundAssets(item: SettingListPaymentModel?): String {
        val resourceUrl = paymentSettingRouter.getResourceUrlAssetPayment()
        val assetName = getBackgroundResource(item)
        val density = DisplayMetricUtils.getScreenDensity(itemView.context)

        return String.format(resourceUrl + FORMAT_URL_IMAGE, assetName, density, assetName)
    }

    private fun getBackgroundResource(item: SettingListPaymentModel?): String {
        when (item?.cardType?.toLowerCase()) {
            VISA -> return VISA_SMALL
            MASTERCARD -> return MASTERCARD_SMALL
            JCB -> return JCB_SMALL
            else -> return EXPIRED_SMALL
        }
    }

    private fun getMaskedNumberSubString(maskedNumber: String): String {
        val LAST_NUMBERS = 4
        val FOUR_STARS = " * * * * "

        return FOUR_STARS + maskedNumber.substring(maskedNumber.length - LAST_NUMBERS)
    }

    companion object {
        private val FORMAT_URL_IMAGE = "%s/%s/%s.png"
        private val VISA = "visa"
        private val MASTERCARD = "mastercard"
        private val JCB = "jcb"
        private val VISA_SMALL = "bg_visa_small"
        private val MASTERCARD_SMALL = "bg_mastercard_small"
        private val JCB_SMALL = "bg_jcb_small"
        private val EXPIRED_SMALL = "bg_expired_small"
        val LAYOUT = R.layout.item_setting_list_payment
    }
}
