package com.tokopedia.sellerorder.list.presentation.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.requestpickup.presentation.activity.SomConfirmReqPickupActivity
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object SomListNavigator {

    const val REQUEST_DETAIL = 999
    const val REQUEST_FILTER = 998
    const val REQUEST_CONFIRM_SHIPPING = 997
    const val REQUEST_CONFIRM_REQUEST_PICKUP = 996
    const val REQUEST_CHANGE_COURIER = 995
    const val REQUEST_PRINT_AWB = 994

    fun goToSomOrderDetail(fragment: SomListFragment, item: SomListOrderUiModel, userRolesResult: Result<SomGetUserRoleUiModel>?) {
        fragment.run {
            val userRoles = if (userRolesResult != null && userRolesResult is Success) userRolesResult.data else null
            Intent(context, SomDetailActivity::class.java).apply {
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

    fun goToConfirmShippingPage(fragment: SomListFragment, orderId: String) {
        fragment.run {
            Intent(context, SomConfirmShippingActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                putExtra(SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING, false)
                startActivityForResult(this, REQUEST_CONFIRM_SHIPPING)
            }
        }
    }

    fun goToRequestPickupPage(fragment: SomListFragment, orderId: String) {
        fragment.run {
            Intent(context, SomConfirmReqPickupActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                startActivityForResult(this, REQUEST_CONFIRM_REQUEST_PICKUP)
            }
        }
    }

    fun goToChangeCourierPage(fragment: SomListFragment, orderId: String) {
        fragment.run {
            Intent(activity, SomConfirmShippingActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                putExtra(SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING, true)
                startActivityForResult(this, REQUEST_CHANGE_COURIER)
            }
        }
    }

    fun goToPrintAwb(fragment: SomListFragment?, orderIds: List<String>, markAsPrinted: Boolean) {
        fragment?.run {
            val url = Uri.parse(TokopediaUrl.getInstance().MOBILEWEB)
                    .buildUpon()
                    .appendPath(SomConsts.PATH_PRINT_AWB)
                    .appendQueryParameter(SomConsts.PRINT_AWB_ORDER_ID_QUERY_PARAM, orderIds.joinToString(","))
                    .appendQueryParameter(SomConsts.PRINT_AWB_MARK_AS_PRINTED_QUERY_PARAM, if (markAsPrinted) "1" else "0")
                    .build()
                    .toString()
            val appLink = Uri.parse(ApplinkConst.WEBVIEW)
                    .buildUpon()
                    .appendQueryParameter("url", url)
                    .build().toString()
            RouteManager.getIntent(context, appLink)?.run {
                startActivityForResult(this, REQUEST_PRINT_AWB)
            }
        }
    }
}