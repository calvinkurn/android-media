package com.tokopedia.tokofood.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokofood.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext val context: Context?) {

    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getDistanceTitle(): String? {
        return getString(R.string.text_distance_title)
    }

    fun getEstimationTitle(): String? {
        return getString(R.string.text_estimation_title)
    }

    fun getOpsHoursTitle(): String? {
        return getString(R.string.text_ops_hour_title)
    }

    fun getOutOfStockWording(): String {
        return getString(R.string.text_out_of_stock).orEmpty()
    }
}
