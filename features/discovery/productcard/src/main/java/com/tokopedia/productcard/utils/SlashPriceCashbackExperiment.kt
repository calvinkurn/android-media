package com.tokopedia.productcard.utils

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.PageSource.SEARCH
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RollenceKey.PRODUCT_CARD_SLASHED_PRICE_CASHBACK_ALL
import com.tokopedia.remoteconfig.RollenceKey.PRODUCT_CARD_SLASHED_PRICE_CASHBACK_EXPERIMENT
import com.tokopedia.remoteconfig.RollenceKey.PRODUCT_CARD_SLASHED_PRICE_CASHBACK_SRP

internal enum class SlashPriceCashbackExperiment(val variant: String) {

    CONTROL("") {
        override fun isUnderExperiment(pageSource: ProductCardModel.PageSource): Boolean = false
    },
    SLASH_PRICE_CASHBACK_SRP(PRODUCT_CARD_SLASHED_PRICE_CASHBACK_SRP) {
        override fun isUnderExperiment(pageSource: ProductCardModel.PageSource): Boolean =
            pageSource == SEARCH
    },
    SLASH_PRICE_CASHBACK_ALL(PRODUCT_CARD_SLASHED_PRICE_CASHBACK_ALL) {
        override fun isUnderExperiment(pageSource: ProductCardModel.PageSource): Boolean = true
    };

    abstract fun isUnderExperiment(pageSource: ProductCardModel.PageSource): Boolean

    fun isControl(pageSource: ProductCardModel.PageSource) = !isUnderExperiment(pageSource)

    companion object  {
        fun get(remoteConfig: Lazy<RemoteConfig?>): SlashPriceCashbackExperiment =
            SlashPriceCashbackExperiment
                .values()
                .find { it.variant == getVariant(remoteConfig) }
                ?: CONTROL

        private fun getVariant(remoteConfig: Lazy<RemoteConfig?>) =
            try {
                remoteConfig.value?.getString(PRODUCT_CARD_SLASHED_PRICE_CASHBACK_EXPERIMENT)
            } catch (_: Throwable) {
                ""
            }
    }
}
