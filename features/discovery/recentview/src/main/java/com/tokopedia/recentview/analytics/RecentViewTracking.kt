package com.tokopedia.recentview.analytics

import android.content.Context
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recentview.ext.convertRupiahToInt
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.track.TrackApp
import timber.log.Timber
import java.util.*

/**
 * Created by Lukas on 1/12/21.
 */
object RecentViewTracking {

    private const val LOGIN_SESSION = "LOGIN_SESSION"
    private const val LOGIN_ID = "LOGIN_ID"

    private const val EE_PARAM_ITEM_ID = "item_id"
    private const val EE_PARAM_ITEM_NAME = "item_name"
    private const val EE_PARAM_ITEM_BRAND = "item_brand"
    private const val EE_PARAM_ITEM_CATEGORY = "item_category"
    private const val EE_PARAM_PRICE = "price"
    private const val EE_PARAM_INDEX = "index"
    private const val EE_USER_ID = "userId"
    private const val EE_PARAM_DIMENSION_40 = "dimension40"

    private const val EE_VALUE_LIST = "/recent"
    private const val EE_VALUE_EVENT_NAME_IMPRESSION = "view_item_list"
    private const val EE_VALUE_EVENT_CATEGORY = "recent view"
    private const val EE_VALUE_EVENT_ACTION_IMPRESSION = "impression on product"

    private const val EE_VALUE_ITEMS = "items"
    private const val EE_VALUE_NONE_OTHER = "none/other"

    fun trackEventClickOnProductRecentView(context: Context,
                                           dataItem: Any?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "recent view",
                        "eventAction", "click on product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click",
                        DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/recent"),
                                "products", DataLayer.listOf(dataItem)
                        )
                ),
                        "userId", getLoginID(context)
                )
        )
    }

    fun trackEventImpressionOnProductRecentView(context: Context?,
                                                dataList: ArrayList<RecentViewDetailProductDataModel>) {

        Timber.tag("lukas").d("Send impression on product review")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "recent view",
                        "eventAction", "impression on product",
                        "eventLabel", "",
                        "ecommerce",
                        DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                    dataList.map { data ->
                                        DataLayer.mapOf(
                                                "name", data.name,
                                                "id", data.productId,
                                                "price", data.price.convertRupiahToInt().toString(),
                                                "brand", "none / other",
                                                "category", "",
                                                "position", data.positionForRecentViewTracking.toString()
                                        )
                                    }.toTypedArray()
                                )
                        ),
                        "userId", getLoginID(context)
                ) as HashMap<String, Any>?
        )
    }

    fun trackEventOpenScreen(context: Context?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "openScreen",
                        "screenName", "/recent",
                        "eventAction", "impression on product",
                        "isLoggedInStatus", if (getLoginID(context).isEmpty()) "false" else "true",
                        "businessUnit", "home & browse",
                        "currentSite", "tokopediamarketplace",
                        "userId", getLoginID(context)
                )
        )
    }

    private fun getLoginID(context: Context?): String {
        val sharedPrefs = context?.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE)
        return sharedPrefs?.getString(LOGIN_ID, "0") ?: "0"
    }
}