package com.tokopedia.tokofood.common.domain

import com.google.gson.Gson
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood
import timber.log.Timber

object TokoFoodCartUtil {

    const val TOKOFOOD_BUSINESS_ID: Long = 1

    fun String.convertToCartMetadata(): CartMetadataTokoFood =
        try {
            Gson().fromJson(this, CartMetadataTokoFood::class.java)
        } catch (ex: Exception) {
            Timber.e(ex)
            CartMetadataTokoFood()
        }

}