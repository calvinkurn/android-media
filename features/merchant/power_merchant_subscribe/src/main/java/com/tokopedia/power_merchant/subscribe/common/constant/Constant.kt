package com.tokopedia.power_merchant.subscribe.common.constant

import android.content.Context
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PmFeatureUiModel

/**
 * Created By @ilhamsuaib on 26/02/21
 */

object Constant {

    object Url {
        const val FREE_SHIPPING_TERMS_AND_CONDITION = "https://seller.tokopedia.com/edu/bebas-ongkir/"
        const val POWER_MERCHANT_EDU = "https://seller.tokopedia.com/edu/power-merchant/"
        const val URL_BROADCAST_CHAT_TERMS_AND_CONDITION = "https://seller.tokopedia.com/edu/gratis-broadcast-chat/"
        const val URL_LEARN_MORE_BENEFIT = "https://seller.tokopedia.com/edu/power-merchant/"
        const val URL_SHOP_PERFORMANCE_TIPS = "https://seller.tokopedia.com/edu/?s=tingkatkan+performa"
        const val URL_PREMIUM_ACCOUNT = "https://m.tokopedia.com/payment/rekening-premium"
        const val URL_FREE_SHIPPING_INTERIM_PAGE = "https://m.tokopedia.com/bebas-ongkir"
    }

    object Subscription {

        fun getPmFeatureList(context: Context): List<PmFeatureUiModel> {
            return listOf(
                    PmFeatureUiModel(
                            imageResId = R.drawable.ic_free_shipping,
                            title = context.getString(R.string.power_merchant_bebas_ongkir),
                            description = context.getString(R.string.pm_bebas_ongkir_description),
                            clickableText = context.getString(R.string.pm_free_shipping_clickable_text),
                            clickableUrl = Url.FREE_SHIPPING_TERMS_AND_CONDITION
                    ),
                    PmFeatureUiModel(
                            imageResId = R.drawable.ic_free_shipping,
                            title = context.getString(R.string.power_merchant_bebas_ongkir),
                            description = context.getString(R.string.pm_bebas_ongkir_description),
                            clickableText = context.getString(R.string.pm_free_shipping_clickable_text),
                            clickableUrl = Url.FREE_SHIPPING_TERMS_AND_CONDITION
                    ),
                    PmFeatureUiModel(
                            imageResId = R.drawable.ic_free_shipping,
                            title = context.getString(R.string.power_merchant_bebas_ongkir),
                            description = context.getString(R.string.pm_bebas_ongkir_description),
                            clickableText = context.getString(R.string.pm_free_shipping_clickable_text),
                            clickableUrl = Url.FREE_SHIPPING_TERMS_AND_CONDITION
                    ),
                    PmFeatureUiModel(
                            imageResId = R.drawable.ic_free_shipping,
                            title = context.getString(R.string.power_merchant_bebas_ongkir),
                            description = context.getString(R.string.pm_bebas_ongkir_description),
                            clickableText = context.getString(R.string.pm_free_shipping_clickable_text),
                            clickableUrl = Url.FREE_SHIPPING_TERMS_AND_CONDITION
                    )
            )
        }
    }

    object ShopLevel {
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
    }
}