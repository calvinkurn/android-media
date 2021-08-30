package com.tokopedia.product_bundle.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product_bundle.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext val context: Context?) {

    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getPreOrderTimeUnitDay(): String? {
        return getString(R.string.preorder_time_unit_day)
    }

    fun getPreOrderTimeUnitMonth(): String? {
        return getString(R.string.preorder_time_unit_month)
    }
}