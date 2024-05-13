package com.tokopedia.tokopedianow.common.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R

object TokoNowServiceTypeUtil {
    const val EDU_BOTTOMSHEET_DURATION_RESOURCE_ID = "edu_bottomsheet_duration_resource_id"
    const val EDU_BOTTOMSHEET_STOCK_RESOURCE_ID = "edu_bottomsheet_stock_resource_id"
    const val EDU_BOTTOMSHEET_FAQ_RESOURCE_ID = "edu_bottomsheet_faq_resource_id"
    const val EDU_BOTTOMSHEET_SK_RESOURCE_ID = "edu_bottomsheet_sk_resource_id"
    const val EDU_WIDGET_DURATION_RESOURCE_ID = "edu_widget_duration_resource_id"
    const val EDU_WIDGET_SELECTED_PRODUCT_FREE_SHIPPING_RESOURCE_ID = "edu_widget_selected_product_free_shipping_resource_id"
    const val SHARING_WIDGET_RESOURCE_ID = "sharing_widget_resource_id"
    const val REPURCHASE_EMPTY_RESOURCE_ID = "repurchase_empty_resource_id"
    const val SEARCH_CATEGORY_SUBTITLE_RESOURCE_ID = "search_category_subtitle_resource_id"
    const val CATEGORY_AISLE_HEADER_ID = "category_aisle_header"
    const val OUT_OF_COVERAGE_TITLE_ID = "out_of_coverage_title"
    const val OUT_OF_COVERAGE_DESCRIPTION_ID = "out_of_coverage_description"
    const val OUT_OF_COVERAGE_PRIMARY_BUTTON_ID = "out_of_coverage_primary_button"
    const val OUT_OF_COVERAGE_SECONDARY_BUTTON_ID = "out_of_coverage_secondary_button"

    /*
    * Create the key to access and wrap resourceId into TokoNowStringResource data class
    * Properties explanation :
    * 1. formattedResourceId -> Put resourceId here if there is something (variable or color) we want to put into the string resource.
    * 2. resourceId2h -> Put here if we want to get the resourceId of string.
    */

    private val resourceIdsMap = mutableMapOf(
        EDU_BOTTOMSHEET_DURATION_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_home_educational_information_2h_duration_bottomsheet
        ),
        EDU_BOTTOMSHEET_STOCK_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_home_educational_information_2h_stock_available_bottomsheet
        ),
        EDU_BOTTOMSHEET_FAQ_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_home_educational_information_2h_twenty_four_hours_bottomsheet
        ),
        EDU_BOTTOMSHEET_SK_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_home_educational_information_2h_terms_and_conditions_bottomsheet
        ),
        EDU_WIDGET_DURATION_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_home_educational_information_2h_duration
        ),
        EDU_WIDGET_SELECTED_PRODUCT_FREE_SHIPPING_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_home_educational_information_free_shipping
        ),
        SHARING_WIDGET_RESOURCE_ID to TokoNowStringResource(
            formattedResourceId = R.string.tokopedianow_home_sharing_education_title
        ),
        REPURCHASE_EMPTY_RESOURCE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_repurchase_no_result_description_two_hours
        ),
        SEARCH_CATEGORY_SUBTITLE_RESOURCE_ID to TokoNowStringResource(
            formattedResourceId = R.string.tokopedianow_search_category_subtitle
        ),
        CATEGORY_AISLE_HEADER_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_2h_category_aisle_header
        ),
        OUT_OF_COVERAGE_TITLE_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_common_empty_state_title
        ),
        OUT_OF_COVERAGE_DESCRIPTION_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_common_empty_state_desc
        ),
        OUT_OF_COVERAGE_PRIMARY_BUTTON_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_common_empty_state_button_change_address
        ),
        OUT_OF_COVERAGE_SECONDARY_BUTTON_ID to TokoNowStringResource(
            resourceId2h = R.string.tokopedianow_common_empty_state_button_return
        )
    )

    fun getServiceTypeRes(key: String): Int? {
        return resourceIdsMap[key]?.resourceId2h
    }

    /*
    * There are two options of formatted copy :
    * 1. Get string resource with params : resourceId and delivery duration copy.
    * 2. Get string resource with params : resourceId, delivery duration copy and color (need color resourceId).
    *
    * Current formatted on a string:
    * %1$s -> Delivery duration copy.
    * %2$s -> Color
    *
    * If need more params, need to adjust the function or make another function.
    */
    fun getServiceTypeFormattedCopy(context: Context, key: String, @ColorRes colorRes: Int? = null): String {
        val deliveryDurationCopy = context.getString(R.string.tokopedianow_2h_copy)
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
        @StringRes val resourceId2h: Int? = null,
        @StringRes val formattedResourceId: Int? = null
    )
}
