package com.tokopedia.product.addedit.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.product.addedit.R
import javax.inject.Inject

class VariantResourceProvider @Inject constructor(@ApplicationContext val context: Context?) {

    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    private fun getString(resId: Int, value: Any): String? {
        return try {
            context?.getString(resId, value)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    // product variant price string properties
    fun getEmptyProductPriceErrorMessage(): String {
        return getString(R.string.error_empty_price).orEmpty()
    }

    fun getMinLimitProductPriceErrorMessage(minPrice: Int): String {
        return getString(R.string.error_minimum_price, minPrice).orEmpty()
    }

    // product variant stock string properties
    fun getEmptyProductStockErrorMessage(): String {
        return getString(R.string.error_empty_stock).orEmpty()
    }

    fun getMinLimitProductStockErrorMessage(minStock: Int): String {
        return getString(R.string.error_minimum_stock, minStock).orEmpty()
    }

    fun getMaxLimitProductStockErrorMessage(maxStock: Int): String {
        return getString(R.string.error_max_stock, maxStock.getNumberFormatted()).orEmpty()
    }

    // product variant weight string properties
    fun getEmptyProductWeightErrorMessage(): String {
        return getString(R.string.error_empty_weight).orEmpty()
    }

    fun getMinLimitProductWeightErrorMessage(minWeight: Int): String {
        return getString(R.string.error_minimum_weight, minWeight).orEmpty()
    }

    fun getMaxLimitProductWeightErrorMessage(maxWeight: Int): String {
        return getString(R.string.error_max_weight, maxWeight.getNumberFormatted()).orEmpty()
    }

}