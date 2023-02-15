package com.tokopedia.sellerorder.common.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.activities.SomPrintAwbActivity
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity
import com.tokopedia.sellerorder.detail.presentation.activity.SomDetailActivity
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment
import com.tokopedia.sellerorder.requestpickup.presentation.activity.SomConfirmReqPickupActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import java.net.URLDecoder

object SomNavigator {

    const val REQUEST_DETAIL = 999
    const val REQUEST_CONFIRM_SHIPPING = 997
    const val REQUEST_CONFIRM_REQUEST_PICKUP = 996
    const val REQUEST_CHANGE_COURIER = 995
    const val REQUEST_RESCHEDULE_PICKUP = 994
    const val REQUEST_FIND_NEW_DRIVER = 993

    fun goToSomOrderDetail(fragment: SomListFragment, orderId: String) {
        fragment.run {
            Intent(context, SomDetailActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                startActivityForResult(this, REQUEST_DETAIL)
            }
        }
    }

    fun goToTrackingPage(context: Context?, orderId: String, url: String) {
        context?.run {
            var routingAppLink: String = ApplinkConst.ORDER_TRACKING.replace("{order_id}", orderId)
            val uriBuilder = Uri.Builder()
            val decodedUrl = if (url.startsWith(SomConsts.PREFIX_HTTPS)) {
                url
            } else {
                URLDecoder.decode(url, SomConsts.ENCODING_UTF_8)
            }
            uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, decodedUrl)
            uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_CALLER, SomConsts.PARAM_SELLER)
            routingAppLink += uriBuilder.toString()
            RouteManager.route(this, routingAppLink)
        }
    }

    fun goToConfirmShippingPage(fragment: Fragment, orderId: String) {
        fragment.run {
            Intent(context, SomConfirmShippingActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                putExtra(SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING, false)
                startActivityForResult(this, REQUEST_CONFIRM_SHIPPING)
            }
        }
    }

    fun goToRequestPickupPage(fragment: Fragment, orderId: String) {
        fragment.run {
            Intent(context, SomConfirmReqPickupActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                startActivityForResult(this, REQUEST_CONFIRM_REQUEST_PICKUP)
            }
        }
    }

    fun goToChangeCourierPage(fragment: Fragment, orderId: String) {
        fragment.run {
            Intent(activity, SomConfirmShippingActivity::class.java).apply {
                putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                putExtra(SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING, true)
                startActivityForResult(this, REQUEST_CHANGE_COURIER)
            }
        }
    }

    fun goToReschedulePickupPage(fragment: Fragment, orderId: String) {
        fragment.run {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.RESCHEDULE_PICKUP.replace("{order_id}", orderId))
            startActivityForResult(intent, REQUEST_RESCHEDULE_PICKUP)
        }
    }

    fun goToFindNewDriver(fragment: Fragment, orderId: String, invoice: String?) {
        fragment.run {
            startActivityForResult(RouteManager.getIntent(
                activity,
                ApplinkConstInternalLogistic.FIND_NEW_DRIVER
            ).apply {
                    putExtra(SomConsts.PARAM_ORDER_ID, orderId)
                    putExtra(SomConsts.PARAM_INVOICE, invoice.orEmpty())
                }, REQUEST_FIND_NEW_DRIVER
            )
        }
    }

    fun goToPrintAwb(activity: FragmentActivity?, view: View?, orderIds: List<String>, markAsPrinted: Boolean) {
        activity?.run {
            if (GlobalConfig.isSellerApp()) {
                val url = Uri.parse("${TokopediaUrl.getInstance().MOBILEWEB}${SomConsts.PATH_PRINT_AWB}")
                        .buildUpon()
                        .appendQueryParameter(SomConsts.PRINT_AWB_ORDER_ID_QUERY_PARAM, orderIds.joinToString(","))
                        .appendQueryParameter(SomConsts.PRINT_AWB_MARK_AS_PRINTED_QUERY_PARAM, if (markAsPrinted) "1" else "0")
                        .build()
                        .toString()
                Intent(this, SomPrintAwbActivity::class.java).apply {
                    putExtra(KEY_URL, url)
                    putExtra(KEY_TITLE, SomConsts.PRINT_AWB_WEBVIEW_TITLE)
                    startActivity(this)
                }
            } else {
                view?.let {
                    Toaster.build(it, getString(R.string.som_detail_som_print_not_available), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
                }
            }
        }
    }

    fun openAppLink(context: Context?, appLink: String): Boolean {
        val intent: Intent? = RouteManager.getIntentNoFallback(context, appLink)
        return if (intent == null) {
            if (appLink.startsWith(SomConsts.PREFIX_HTTP)) {
                openWebView(context, appLink)
                true
            } else false
        } else {
            context?.startActivity(intent)
            true
        }
    }

    fun openWebView(context: Context?, url: String) {
        val intent: Intent = RouteManager.getIntent(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        context?.startActivity(intent)
    }
}
