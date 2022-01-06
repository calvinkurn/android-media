package com.tokopedia.tokopedianow.common.util

import androidx.annotation.StringRes
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M

object TokoNowServiceTypeUtil {
    const val DELIVERY_DURATION_COPY_RESOURCE_ID = "delivery_duration_copy_resource_id"
    const val EDU_BOTTOM_SHEET_RESOURCE_ID = "edu_bottomsheet_resource_id"
    const val EDU_WIDGET_RESOURCE_ID = "edu_widget_resource_id"
    const val SHARING_WIDGET_RESOURCE_ID = "sharing_widget_resource_id"
    const val REPURCHASE_EMPTY_RESOURCE_ID = "repurchase_empty_resource_id"

    private val resourceIdsMap = mutableMapOf(
        DELIVERY_DURATION_COPY_RESOURCE_ID to TokoNowStringResource(
            resourceId15m = R.string.tokopedianow_15m_copy,
            resourceId2h = R.string.tokopedianow_2h_copy
        ),
        EDU_BOTTOM_SHEET_RESOURCE_ID to TokoNowStringResource(
            formattedResourceId = R.string.tokopedianow_home_educational_information_duration_bottomsheet
        ),
        EDU_WIDGET_RESOURCE_ID to TokoNowStringResource(
            formattedResourceId = R.string.tokopedianow_home_educational_information_duration
        ),
        SHARING_WIDGET_RESOURCE_ID to TokoNowStringResource(
            formattedResourceId = R.string.tokopedianow_home_sharing_education_title
        ),
        REPURCHASE_EMPTY_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_repurchase_no_result_description_two_hours,
            resourceId15m = R.string.tokopedianow_repurchase_no_result_description_fifteen_minutes
        )
    )

    fun getServiceTypeRes(key: String, serviceType: String): Int? {
        return when {
            resourceIdsMap[key]?.formattedResourceId != null -> {
                resourceIdsMap[key]?.formattedResourceId
            }
            serviceType == NOW_15M -> {
                resourceIdsMap[key]?.resourceId15m
            }
            else -> {
                resourceIdsMap[key]?.resourceId2h
            }
        }
    }

    data class TokoNowStringResource(
        @StringRes val resourceId15m: Int? = null,
        @StringRes val resourceId2h: Int? = null,
        @StringRes val formattedResourceId: Int? = null
    )
}