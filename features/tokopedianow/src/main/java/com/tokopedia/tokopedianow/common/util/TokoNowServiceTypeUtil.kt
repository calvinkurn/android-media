package com.tokopedia.tokopedianow.common.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M

object TokoNowServiceTypeUtil {
    const val EDU_BOTTOM_SHEET_RESOURCE_ID = "edu_bottomsheet_resource_id"
    const val EDU_WIDGET_RESOURCE_ID = "edu_widget_resource_id"
    const val SHARING_WIDGET_RESOURCE_ID = "sharing_widget_resource_id"
    const val REPURCHASE_EMPTY_RESOURCE_ID = "repurchase_empty_resource_id"
    const val SEARCH_CATEGORY_SUBTITLE_RESOURCE_ID = "search_category_subtitle_resource_id"
    const val CATEGORY_AISLE_HEADER_ID = "category_aisle_header"

    /*
    * Create the key to access and wrap resourceId into TokoNowStringResource data class
    * Properties explanation :
    * 1. formattedResourceId -> Put resourceId here if there is something (variable or color) we want to put into the string resource.
    * 2. resourceId2h and resourceId15m -> Put here only if we want to get the resourceId of string in either 15 min or 2 hr delivery duration.
    */
    private val resourceIdsMap = mutableMapOf(
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
        ),
        SEARCH_CATEGORY_SUBTITLE_RESOURCE_ID to TokoNowStringResource(
            formattedResourceId = R.string.tokopedianow_search_category_subtitle
        ),
        CATEGORY_AISLE_HEADER_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_2h_category_aisle_header,
            resourceId15m = R.string.tokopedianow_15m_category_aisle_header
        )
    )

    fun getServiceTypeRes(key: String, serviceType: String): Int? {
        return if (serviceType == NOW_15M) {
            resourceIdsMap[key]?.resourceId15m
        } else {
            resourceIdsMap[key]?.resourceId2h
        }
    }

    /*
    * There are two options of formatted copy :
    * 1. Get string resource with params : resourceId and delivery duration copy (15 min or 2 hr).
    * 2. Get string resource with params : resourceId, delivery duration copy (15 min or 2 hr) and color (need color resourceId).
    *
    * Current formatted on a string:
    * %1$s -> Delivery duration copy.
    * %2$s -> Color
    *
    * If need more params, need to adjust the function or make another function.
    */
    fun getServiceTypeFormattedCopy(context: Context, key: String, serviceType: String, @ColorRes colorRes: Int? = null): String {
        val deliveryDurationCopy = if(serviceType == NOW_15M) context.getString(R.string.tokopedianow_15m_copy) else context.getString(R.string.tokopedianow_2h_copy)
        return if (colorRes == null) {
            context.getString(
                resourceIdsMap[key]?.formattedResourceId.orZero(),
                deliveryDurationCopy
            )
        } else {
            context.getString(
                resourceIdsMap[key]?.formattedResourceId.orZero(),
                deliveryDurationCopy,
                ContextCompat.getColor(context, colorRes)
            )
        }
    }

    data class TokoNowStringResource(
        @StringRes val resourceId15m: Int? = null,
        @StringRes val resourceId2h: Int? = null,
        @StringRes val formattedResourceId: Int? = null
    )
}