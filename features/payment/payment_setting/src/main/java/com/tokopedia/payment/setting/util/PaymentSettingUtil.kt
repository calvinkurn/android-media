package com.tokopedia.payment.setting.util

import android.content.Context
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.SettingListPaymentViewHolder
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel

val FORMAT_URL_IMAGE = "%s/%s/%s.png"
val VISA = "visa"
val MASTERCARD = "mastercard"
val JCB = "jcb"
val VISA_SMALL = "bg_visa_small"
val MASTERCARD_SMALL = "bg_mastercard_small"
val JCB_SMALL = "bg_jcb_small"
val EXPIRED_SMALL = "bg_expired_small"

fun SettingListPaymentModel.getBackgroundAssets(paymentSettingRouter : PaymentSettingRouter?,
                                                        context : Context?): String {
    val resourceUrl = paymentSettingRouter?.getResourceUrlAssetPayment()
    val assetName = getBackgroundResource()
    val density = DisplayMetricUtils.getScreenDensity(context)

    return String.format(resourceUrl + FORMAT_URL_IMAGE, assetName, density, assetName)
}

fun SettingListPaymentModel.getBackgroundResource(): String {
    when (this.cardType?.toLowerCase()) {
        VISA -> return VISA_SMALL
        MASTERCARD -> return MASTERCARD_SMALL
        JCB -> return JCB_SMALL
        else -> return EXPIRED_SMALL
    }
}

fun String.getMaskedNumberSubStringPayment(): String {
    val LAST_NUMBERS = 4
    val FOUR_STARS = " * * * * "

    return FOUR_STARS + this.substring(this.length - LAST_NUMBERS)
}


fun String.getSpacedTextPayment(): String {
    val DIGITS = 4
    val THREE_SPACE = 3

    val builder = StringBuilder()

    builder.append(this[0])
    for (i in 1 until this.length) {
        if (i % DIGITS == 0) {
            for (j in 0 until THREE_SPACE) {
                builder.append("\u0020")
            }
        } else {
            builder.append("\u0020")
        }
        builder.append(this[i])
    }

    return builder.toString()
}

fun SettingListPaymentModel.getExpiredDate(): String {
    return String.format("%s/%s", this.expiryMonth, this.expiryYear)
}