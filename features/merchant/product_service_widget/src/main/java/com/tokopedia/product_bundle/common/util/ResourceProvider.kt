package com.tokopedia.product_bundle.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product_service_widget.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext val context: Context?) {

    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getPreOrderTimeUnitDay(): String {
        return getString(R.string.preorder_time_unit_day).orEmpty()
    }

    fun getPreOrderTimeUnitWeek(): String {
        return getString(R.string.preorder_time_unit_week).orEmpty()
    }

    fun getPreOrderTimeUnitMonth(): String {
        return getString(R.string.preorder_time_unit_month).orEmpty()
    }

    fun getProductVariantNotSelected(): String {
        return getString(R.string.error_bundle_variant_not_selected) ?: ""
    }

    fun getErrorMessage(throwable: Throwable): String {
        return ErrorHandler.getErrorMessage(context, throwable)
    }
}