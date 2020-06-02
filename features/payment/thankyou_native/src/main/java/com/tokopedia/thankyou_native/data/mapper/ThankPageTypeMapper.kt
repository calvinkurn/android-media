package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.thankyou_native.domain.model.ThanksPageData

const val MARKET_PLACE = "MarketPlace"
const val DIGITAL = "Digital"

sealed class ThankPageType(val tag : String)
object MarketPlaceThankPage : ThankPageType(MARKET_PLACE)
object DigitalThankPage : ThankPageType(DIGITAL)

object ThankPageTypeMapper {

    const val MARKETPLACE_MERCHANT_CODE = "tokopedia"
    const val DIGITAL_MERCHANT_CODE = "tokopediapulsa"

    fun getThankPageType(thanksPageData: ThanksPageData): ThankPageType {
        return when (thanksPageData.merchantCode) {
            MARKETPLACE_MERCHANT_CODE -> MarketPlaceThankPage
            else -> DigitalThankPage
        }
    }
}