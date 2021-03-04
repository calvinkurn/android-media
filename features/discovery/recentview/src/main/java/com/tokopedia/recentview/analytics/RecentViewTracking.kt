package com.tokopedia.recentview.analytics

import android.content.Context
import com.tokopedia.recentview.ext.convertRupiahToInt
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import java.util.*

/**
 * Created by Lukas on 1/12/21.
 */
object RecentViewTracking : BaseTrackerConst(){

    private const val LOGIN_SESSION = "LOGIN_SESSION"
    private const val LOGIN_ID = "LOGIN_ID"

    private const val EE_KEY_LOGIN_STATUS = "isLoggedInStatus"

    private const val EE_VALUE_CATEGORY_RECENT_VIEW = "recent view"
    private const val EE_VALUE_LIST = "/recent"
    private const val EE_VALUE_EVENT_ACTION_CLICK = "click on product"
    private const val EE_VALUE_EVENT_ACTION_IMPRESSION = "impression on product"
    private const val EE_VALUE_NONE_OTHER = "none/other"

    fun trackEventClickOnProductRecentView(context: Context,
                                           dataList: RecentViewDetailProductDataModel) {
        val tracker = BaseTrackerBuilder().constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = EE_VALUE_CATEGORY_RECENT_VIEW,
                eventAction = EE_VALUE_EVENT_ACTION_CLICK,
                eventLabel = Label.NONE,
                list = EE_VALUE_LIST,
                products = mapToProductTracking(listOf(dataList))
        )
                .appendUserId(getLoginID(context))
                .build()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracker)
    }

    fun trackEventImpressionOnProductRecentView(context: Context?,
                                                dataList: ArrayList<RecentViewDetailProductDataModel>) {

        val tracker = BaseTrackerBuilder().constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = EE_VALUE_CATEGORY_RECENT_VIEW,
                eventAction = EE_VALUE_EVENT_ACTION_IMPRESSION,
                eventLabel = Label.NONE,
                list = EE_VALUE_LIST,
                products = mapToProductTracking(dataList)
        )
                .appendUserId(getLoginID(context))
                .build()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracker)
    }

    fun trackEventOpenScreen(context: Context?) {
        val tracker = BaseTrackerBuilder().constructBasicGeneralClick(
                event = Event.OPEN_SCREEN,
                eventCategory = EE_VALUE_CATEGORY_RECENT_VIEW,
                eventAction = EE_VALUE_EVENT_ACTION_IMPRESSION,
                eventLabel = Label.NONE
        )
                .appendScreen(EE_VALUE_LIST)
                .appendUserId(getLoginID(context))
                .appendCustomKeyValue(EE_KEY_LOGIN_STATUS, if (getLoginID(context).isEmpty()) "false" else "true")
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracker)
    }

    private fun mapToProductTracking(dataList: List<RecentViewDetailProductDataModel>) = dataList.map {
        Product(
                id = it.productId,
                brand = EE_VALUE_NONE_OTHER,
                category = EE_VALUE_NONE_OTHER,
                name = it.name,
                productPrice = it.price.convertRupiahToInt().toString(),
                productPosition = it.positionForRecentViewTracking.toString(),
                variant = Label.NONE,
                isFreeOngkir = false
        )
    }

    private fun getLoginID(context: Context?): String {
        val sharedPrefs = context?.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE)
        return sharedPrefs?.getString(LOGIN_ID, "0") ?: "0"
    }
}