package com.tokopedia.travel_slice.ui.provider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.travel_slice.analytics.TravelSliceAnalytics
import com.tokopedia.travel_slice.di.DaggerTravelSliceComponent
import com.tokopedia.travel_slice.utils.TravelActionConst
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by jessica on 01/12/20
 */

class TravelSliceActivity : Activity() {

    @Inject
    lateinit var analytics: TravelSliceAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    companion object {
        fun createHotelDashboardIntent(context: Context, applink: String): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalTravel.TRAVEL_ACTION).apply {
                putExtra(TravelActionConst.PARAM_FEATURE_NAME, TravelActionConst.Feature.HOTEL_DASHBOARD)
                putExtra(TravelActionConst.PARAM_FEATURE_APPLINK, applink)
                action = System.currentTimeMillis().toString()
            }
        }

        fun createHotelDetailIntent(context: Context, applink: String, city: String): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalTravel.TRAVEL_ACTION).apply {
                putExtra(TravelActionConst.PARAM_FEATURE_NAME, TravelActionConst.Feature.HOTEL_DETAIL)
                putExtra(TravelActionConst.PARAM_FEATURE_APPLINK, applink)
                putExtra(TravelActionConst.PARAM_CITY_NAME, city)
                action = System.currentTimeMillis().toString()
            }
        }

        fun createHotelOrderDetailIntent(context: Context, applink: String, city: String): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalTravel.TRAVEL_ACTION).apply {
                putExtra(TravelActionConst.PARAM_FEATURE_NAME, TravelActionConst.Feature.HOTEL_ORDER)
                putExtra(TravelActionConst.PARAM_FEATURE_APPLINK, applink)
                putExtra(TravelActionConst.PARAM_CITY_NAME, city)
                action = System.currentTimeMillis().toString()
            }
        }

        fun createHotelOrderListIntent(context: Context): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalTravel.TRAVEL_ACTION).apply {
                putExtra(TravelActionConst.PARAM_FEATURE_NAME, TravelActionConst.Feature.HOTEL_ORDER_LIST)
                putExtra(TravelActionConst.PARAM_FEATURE_APPLINK, ApplinkConst.HOTEL_ORDER)
                action = System.currentTimeMillis().toString()
            }
        }

        fun createFlightOrderDetailIntent(context: Context, applink: String): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalTravel.TRAVEL_ACTION).apply {
                putExtra(TravelActionConst.PARAM_FEATURE_NAME, TravelActionConst.Feature.FLIGHT_ORDER)
                putExtra(TravelActionConst.PARAM_FEATURE_APPLINK, applink)
                action = System.currentTimeMillis().toString()
            }
        }

        fun createFlightOrderListIntent(context: Context, applink: String): Intent {
            return RouteManager.getIntent(context, ApplinkConstInternalTravel.TRAVEL_ACTION).apply {
                putExtra(TravelActionConst.PARAM_FEATURE_NAME, TravelActionConst.Feature.FLIGHT_ORDERLIST)
                putExtra(TravelActionConst.PARAM_FEATURE_APPLINK, applink)
                action = System.currentTimeMillis().toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependency()
        super.onCreate(savedInstanceState)
        intent?.getStringExtra(TravelActionConst.PARAM_FEATURE_NAME)?.let { feature ->
            redirectToMainApp(feature)
        }
    }

    private fun injectDependency() {
        DaggerTravelSliceComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun redirectToMainApp(featureName: String) {
        when (featureName) {
            TravelActionConst.Feature.HOTEL_DETAIL -> {
                val city = intent?.getStringExtra(TravelActionConst.PARAM_CITY_NAME) ?: ""
                analytics.clickOnHotelBooking(this, city, userSessionInterface.userId)
            }
            TravelActionConst.Feature.HOTEL_ORDER -> {
                val city = intent?.getStringExtra(TravelActionConst.PARAM_CITY_NAME) ?: ""
                analytics.clickOnHotelReservation(this, city, userSessionInterface.userId)
            }
            TravelActionConst.Feature.FLIGHT_ORDER -> {
                analytics.clickOnFlightReservation(this, userSessionInterface.userId)
            }
        }
        intent?.getStringExtra(TravelActionConst.PARAM_FEATURE_APPLINK)?.let { applink ->
            RouteManager.route(this, applink)
        }
        finish()
    }
}