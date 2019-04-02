package com.tokopedia.payment.setting.util

import android.content.Context
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import kotlin.math.log

val FORMAT_URL_IMAGE = "%s/%s/%s.png"
val VISA = "visa"
val MASTERCARD = "mastercard"
val JCB = "jcb"
val VISA_SMALL = "bg_visa_small"
val MASTERCARD_SMALL = "bg_mastercard_small"
val JCB_SMALL = "bg_jcb_small"
val EXPIRED_SMALL = "bg_expired_small"
val VISA_LARGE = "bg_visa_large"
val MASTERCARD_LARGE = "bg_mastercard_large"
val JCB_LARGE = "bg_jcb_large"
val EXPIRED_LARGE = "bg_expired_large"

val AMEX_LOGO = "credit-card-amex.png"
val JCB_LOGO = "creditcard-jcb.png"
val VISA_LOGO = "creditcard-visa.png"
val MASTER_CARD_LOGO = "creditcard-mastercard.png"

fun SettingListPaymentModel.getLogoAsset() : String{
    var logo = VISA_LOGO
    when(this.cardType?.toLowerCase()){
        MASTERCARD -> logo = MASTER_CARD_LOGO
        JCB -> logo =  JCB_LOGO
        else -> logo =  VISA_LOGO
    }
    return PAYMENT_IMAGE_HOST + logo
}

fun SettingListPaymentModel.getLogoResource(): String {
    when (this.cardType?.toLowerCase()) {
        VISA -> return VISA_SMALL
        MASTERCARD -> return MASTERCARD_SMALL
        JCB -> return JCB_SMALL
        else -> return EXPIRED_SMALL
    }
}

fun SettingListPaymentModel.getBackgroundAssets(paymentSettingRouter : PaymentSettingRouter?,
                                                        context : Context?): String {
    val resourceUrl = paymentSettingRouter?.getResourceUrlAssetPayment()
    val assetName = getBackgroundResource()
    val density = DisplayMetricUtils.getScreenDensity(context)

    return String.format(resourceUrl + FORMAT_URL_IMAGE, assetName, density, assetName)
}

fun SettingListPaymentModel.getBackgroundAssetsLarge(paymentSettingRouter : PaymentSettingRouter?,
                                                context : Context?): String {
    val resourceUrl = paymentSettingRouter?.getResourceUrlAssetPayment()
    val assetName = getBackgroundResourceLarge()
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

fun SettingListPaymentModel.getBackgroundResourceLarge(): String {
    when (this.cardType?.toLowerCase()) {
        VISA -> return VISA_LARGE
        MASTERCARD -> return MASTERCARD_LARGE
        JCB -> return JCB_LARGE
        else -> return EXPIRED_LARGE
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