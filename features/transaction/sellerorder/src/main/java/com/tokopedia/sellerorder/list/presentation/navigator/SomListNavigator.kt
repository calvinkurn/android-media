package com.tokopedia.sellerorder.list.presentation.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.requestpickup.presentation.activity.SomConfirmReqPickupActivity
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object SomListNavigator {

    const val REQUEST_DETAIL = 999
    const val REQUEST_FILTER = 998
    const val REQUEST_CONFIRM_SHIPPING = 997
    const val REQUEST_CONFIRM_REQUEST_PICKUP = 996

    fun goToSomOrderDetail(activity: Activity?, item: SomListOrderUiModel, userRolesResult: Result<SomGetUserRoleUiModel>?) {
        activity?.run {
            val userRoles = if (userRolesResult != null && userRolesResult is Success) userRolesResult.data else null
            Intent(this, SomDetailActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, item.orderId)
                userRoles?.let {
                    putExtra(SomConsts.PARAM_USER_ROLES, it)
                }
                startActivityForResult(this, REQUEST_DETAIL)
            }
        }
    }

    fun goToTrackingPage(context: Context?, orderId: String, url: String) {
        context?.run {
            var routingAppLink: String = ApplinkConst.ORDER_TRACKING.replace("{order_id}", orderId)
            val uriBuilder = Uri.Builder()
            uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, url)
            uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_CALLER, SomConsts.PARAM_SELLER)
            routingAppLink += uriBuilder.toString()
            RouteManager.route(this, routingAppLink)
        }
    }

    fun goToConfirmShippingPage(activity: Activity?, orderId: String) {
        activity?.run {
            Intent(activity, SomConfirmShippingActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                putExtra(SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING, false)
                startActivityForResult(this, REQUEST_CONFIRM_SHIPPING)
            }
        }
    }

    fun goToRequestPickupPage(activity: Activity?, orderId: String) {
        activity?.run {
            Intent(activity, SomConfirmReqPickupActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                startActivityForResult(this, REQUEST_CONFIRM_REQUEST_PICKUP)
            }
        }
    }
}