package com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getPercentFormatted
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import javax.inject.Inject

class ErrorMessageHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return try {
            context.getString(resId, *formatArgs)
        } catch (e: Exception) {
            ""
        }
    }

    fun getPriceMessage(
        criteria: ReservedProduct.Product.ProductCriteria,
        discountAmount: Long,
    ): String {
        return if (discountAmount < criteria.minFinalPrice) {
            getString(R.string.manageproductnonvar_min_message_format, criteria.minFinalPrice.getCurrencyFormatted())
        } else if (discountAmount > criteria.maxFinalPrice) {
            getString(R.string.manageproductnonvar_max_message_format, criteria.maxFinalPrice.getCurrencyFormatted())
        } else {
            getString(R.string.manageproductnonvar_range_message_format, criteria.minFinalPrice.getCurrencyFormatted(), criteria.maxFinalPrice.getCurrencyFormatted())
        }
    }

    fun getDiscountMessage(
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup,
    ): String {
        return if (discountSetup.discount < criteria.minDiscount) {
            getString(R.string.manageproductnonvar_min_message_format, criteria.minDiscount.getPercentFormatted())
        } else if (discountSetup.discount > criteria.maxDiscount) {
            getString(R.string.manageproductnonvar_max_message_format, criteria.maxDiscount.getPercentFormatted())
        } else {
            getString(R.string.manageproductnonvar_range_message_format, criteria.minDiscount.getPercentFormatted(), criteria.maxDiscount.getPercentFormatted())
        }
    }

    fun getStockMessage(
        criteria: ReservedProduct.Product.ProductCriteria,
        stock: Long,
    ): String {
        return if (stock < criteria.minCustomStock) {
            getString(R.string.manageproductnonvar_min_message_format, criteria.minCustomStock)
        } else if (stock > criteria.maxCustomStock) {
            getString(R.string.manageproductnonvar_max_message_format, criteria.maxCustomStock)
        } else {
            getString(R.string.manageproductnonvar_range_message_format, criteria.minCustomStock, criteria.maxCustomStock)
        }
    }
}
