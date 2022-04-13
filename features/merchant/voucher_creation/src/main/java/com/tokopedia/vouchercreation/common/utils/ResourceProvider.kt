package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.vouchercreation.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext val context : Context) {

    fun getInvalidMinimalQuantityQuantityErrorMessage(): String {
        return context.getString(R.string.error_message_invalid_cashback_minimum_purchase_quantity)
    }

    fun getInvalidMinimalPurchaseNominalErrorMessage(): String {
        return context.getString(R.string.error_message_invalid_cashback_minimum_purchase_nominal)
    }

    fun getFormattedSku(): String {
        return context.getString(R.string.mvc_formatted_product_sku)
    }

    fun getFormattedProductPrice(): String {
        return context.getString(R.string.mvc_formatted_product_price)
    }

    fun getFormattedProductStatistic(): String {
        return context.getString(R.string.mvc_formatted_product_statistic)
    }

    fun getProductPrice() : String {
        return context.getString(R.string.placeholder_rupiah)
    }
}