package com.tokopedia.home_account.analytics

import android.content.Context
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.Analytics.ACCOUNT
import com.tokopedia.home_account.AccountConstants.Analytics.ACTION_FIELD
import com.tokopedia.home_account.AccountConstants.Analytics.CLICK
import com.tokopedia.home_account.AccountConstants.Analytics.CLICK_ACCOUNT
import com.tokopedia.home_account.AccountConstants.Analytics.CURRENCY_CODE
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_ATTRIBUTION
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_BRAND
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_CATEGORY
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_DIMENSION_83
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_ID
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_NAME
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_POSITION
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_PRICE
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_VARIAN
import com.tokopedia.home_account.AccountConstants.Analytics.ECOMMERCE
import com.tokopedia.home_account.AccountConstants.Analytics.EMPTY
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_ACTION
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_CATEGORY
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_CATEGORY_ACCOUNT_PAGE_BUYER
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_LABEL
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_PRODUCT_CLICK
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_PRODUCT_VIEW
import com.tokopedia.home_account.AccountConstants.Analytics.IDR
import com.tokopedia.home_account.AccountConstants.Analytics.IMPRESSIONS
import com.tokopedia.home_account.AccountConstants.Analytics.LIST
import com.tokopedia.home_account.AccountConstants.Analytics.NONE_OTHER
import com.tokopedia.home_account.AccountConstants.Analytics.PRODUCTS
import com.tokopedia.home_account.AccountConstants.Analytics.SETTING
import com.tokopedia.home_account.AccountConstants.Analytics.SHOP
import com.tokopedia.home_account.AccountConstants.Analytics.USER
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_BEBAS_ONGKIR
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_PRODUCT_RECOMMENDATION_LIST
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_PRODUCT_TOPADS
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_WISHLIST_PRODUCT
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

/**
 * Created by Yoris Prayogo on 27/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountAnalytics(val context: Context, val userSessionInterface: UserSessionInterface) {

    fun eventClickSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE, String.format("%s %s", USER, SETTING), String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ))
    }

    fun eventClickAccountSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE, String.format("%s %s", ACCOUNT, SETTING), String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ))
    }


    fun eventClickPaymentSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                AccountConstants.Analytics.CLICK_HOME_PAGE, String.format("%s %s", SHOP, SETTING), String.format("%s %s", AccountConstants.Analytics.CLICK, item),
                ""
        ))
    }

    fun eventClickToggleOnGeolocation(context: Context?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics?.sendGeneralEvent(
                "clickHomePage",
                "homepage",
                "click toggle on geolocation",
                ""
        )
    }

    fun eventClickToggleOffGeolocation(context: Context?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics?.sendGeneralEvent(
                "clickHomePage",
                "homepage",
                "click toggle off geolocation",
                ""
        )
    }

    fun eventAccountProductView(trackingQueue: TrackingQueue, recommendationItem: RecommendationItem, position: Int) {
        val map: Map<String, Any> = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_ACCOUNT_PAGE_BUYER,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, IDR,
                IMPRESSIONS, DataLayer.listOf(
                addAccountProductViewImpressions(recommendationItem, position)
        )
        ))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    private fun addAccountProductViewImpressions(recommendationItem: RecommendationItem, position: Int): Any? {
        var list: String? = kotlin.String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.recommendationType)
        if (recommendationItem.isTopAds) {
            list += VALUE_PRODUCT_TOPADS
        }
        return DataLayer.mapOf(DATA_NAME, recommendationItem.name,
                DATA_ID, recommendationItem.productId,
                DATA_PRICE, recommendationItem.price.replace("[^0-9]".toRegex(), ""),
                DATA_BRAND, NONE_OTHER,
                DATA_CATEGORY, recommendationItem.categoryBreadcrumbs,
                DATA_VARIAN, NONE_OTHER,
                LIST, list,
                DATA_POSITION, position.toString(),
                DATA_DIMENSION_83, if (recommendationItem.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else NONE_OTHER)
    }

    fun eventAccountProductClick(recommendationItem: RecommendationItem, position: Int, widgetTitle: String?) {
        var list = String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.recommendationType)
        if (recommendationItem.isTopAds) {
            list += VALUE_PRODUCT_TOPADS
        }
        val tracker: Analytics = TrackApp.getInstance().gtm
        val map = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_ACCOUNT_PAGE_BUYER,
                EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION,
                EVENT_LABEL, EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD,
                        DataLayer.mapOf(LIST, list),
                        PRODUCTS,
                        DataLayer.listOf(
                                DataLayer.mapOf(
                                    DATA_NAME, recommendationItem.name,
                                    DATA_ID, recommendationItem.productId,
                                    DATA_PRICE, recommendationItem.price.replace("[^0-9]".toRegex(), ""),
                                    DATA_BRAND, NONE_OTHER,
                                    DATA_CATEGORY, recommendationItem.categoryBreadcrumbs,
                                    DATA_VARIAN, NONE_OTHER,
                                    LIST, widgetTitle,
                                    DATA_POSITION, position.toString(),
                                    DATA_ATTRIBUTION, NONE_OTHER,
                                    DATA_DIMENSION_83, if (recommendationItem.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else NONE_OTHER
                                )
                        )
                    )
                )
            )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickWishlistButton(wishlistStatus: Boolean) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        var status: String = if (wishlistStatus) {
            "add"
        } else {
            "remove"
        }
        analytics.sendGeneralEvent(
                CLICK_ACCOUNT,
                EVENT_CATEGORY_ACCOUNT_PAGE_BUYER, String.format(VALUE_WISHLIST_PRODUCT, status),
                ""
        )
    }

}
