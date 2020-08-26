package com.tokopedia.product.addedit.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.addedit.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext val context: Context?) {

    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    // product name string properties

    fun getProductNameTips(): String? {
        return getString(R.string.label_add_product_name_tips)
    }

    fun getEmptyProductNameErrorMessage(): String? {
        return getString(R.string.error_empty_product_name)
    }

    fun getProductNameExistErrorMessage(): String? {
        return getString(R.string.error_product_name_exist)
    }

    fun getProductNameBannedErrorMessage(): String? {
        return getString(R.string.error_product_name_banned)
    }

    // product price string properties

    fun getEmptyProductPriceErrorMessage(): String? {
        return getString(R.string.error_empty_product_price)
    }

    fun getMinLimitProductPriceErrorMessage(): String? {
        return getString(R.string.error_product_price_less_than_min_limit)
    }

    // product whole sale quantity string properties

    fun getEmptyWholeSaleQuantityErrorMessage(): String? {
        return getString(R.string.error_empty_wholesale_quantity)
    }

    fun getZeroWholeSaleQuantityErrorMessage(): String? {
        return getString(R.string.error_zero_wholesale_quantity)
    }

    fun getMinLimitWholeSaleQuantityErrorMessage(): String? {
        return getString(R.string.error_wholesale_quantity_less_min_order)
    }

    fun getPrevInputWholeSaleQuantityErrorMessage(): String? {
        return getString(R.string.error_wholesale_quantity_must_bigger_than_previous_input)
    }

    // product whole sale price string properties

    fun getEmptyWholeSalePriceErrorMessage(): String? {
        return getString(R.string.error_empty_wholesale_price)
    }

    fun getZeroWholeSalePriceErrorMessage(): String? {
        return getString(R.string.error_zero_wholesale_price)
    }

    fun getWholeSalePriceTooExpensiveErrorMessage(): String? {
        return getString(R.string.error_wholesale_price_too_expensive)
    }

    fun getPrevInputWholeSalePriceErrorMessage(): String? {
        return getString(R.string.error_wholesale_price_must_cheaper_than_previous_input)
    }

    // product stock string properties

    fun getEmptyProductStockErrorMessage(): String? {
        return getString(R.string.error_empty_product_stock)
    }

    fun getMinLimitProductStockErrorMessage(): String? {
        return getString(R.string.error_minimum_stock_quantity_is_one)
    }

    fun getMaxLimitProductStockErrorMessage(): String? {
        return getString(R.string.error_available_stock_quantity_exceeding_max_limit)
    }

    // product order quantity string properties

    fun getEmptyOrderQuantityErrorMessage(): String? {
        return getString(R.string.error_empty_minimum_order)
    }

    fun getMinLimitOrderQuantityErrorMessage(): String? {
        return getString(R.string.error_minimum_order_cant_be_zero)
    }

    fun getMaxLimitOrderQuantityErrorMessage(): String? {
        return getString(R.string.error_maximum_order_exceeding_max_limit)
    }

    fun getMinOrderExceedStockErrorMessage(): String? {
        return getString(R.string.error_minimum_order_cant_exceed_available_stock)
    }

    // pre order string properties

    fun getEmptyPreorderDurationErrorMessage(): String? {
        return getString(R.string.error_empty_preorder_duration)
    }

    fun getMinLimitPreorderDurationErrorMessage(): String? {
        return getString(R.string.error_preorder_duration_minimum_is_one)
    }

    fun getMaxDaysLimitPreorderDuratioErrorMessage(): String? {
        return getString(R.string.error_preorder_duration_cant_exceed_ninety_days)
    }

    fun getMaxWeeksLimitPreorderDuratioErrorMessage(): String? {
        return getString(R.string.error_preorder_duration_cant_exceed_thirteen_weeks)
    }

    // product video string properties

    fun getDuplicateProductVideoErrorMessage(): String? {
        return getString(R.string.error_video_is_exist)
    }

    // product variant string properties

    fun getVariantEmptyMessage(): String? {
        return getString(R.string.label_variant_subtitle)
    }

    fun getVariantAddedMessage(): String? {
        return getString(R.string.label_variant_subtitle_added) + "\n"
    }

    fun getVariantCountSuffix(): String? {
        return getString(R.string.label_variant_count_suffix)
    }

    // product add validation string properties

    fun getInvalidCategoryIdErrorMessage(): String? {
        return getString(R.string.error_invalid_category_id)
    }

    fun getInvalidPhotoCountErrorMessage(): String? {
        return getString(R.string.error_invalid_photo_count)
    }

    fun getInvalidPhotoReachErrorMessage(): String? {
        return getString(R.string.error_invalid_photo_reach_maximum)
    }

    // Network errors

    fun getGqlErrorMessage(): String? {
        return getString(R.string.error_gql_failed)
    }

}