package com.tokopedia.buyerorderdetail.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

class BuyerOrderDetailNavigator(
        private val activity: Activity
) {

    companion object {
        private const val KEY_URL = "url"
        private const val TELEPHONY_URI = "tel:"
        private const val PREFIX_HTTP = "http"
    }

    private fun composeCallIntentData(phoneNumber: String): Uri {
        return Uri.parse("$TELEPHONY_URI$phoneNumber")
    }

    private fun createProductListPayload(productList: List<ProductListUiModel.ProductUiModel>): String {
        return Gson().toJson(JsonArray(productList.size).apply {
            productList.forEach {
                add(createProductPayload(it))
            }
        })
    }

    private fun createProductPayload(it: ProductListUiModel.ProductUiModel): JsonObject {
        return JsonObject().apply {
            addProperty(BuyerOrderDetailConst.PARAM_PRODUCT_LIST_TITLE, it.productName)
            addProperty(BuyerOrderDetailConst.PARAM_PRODUCT_LIST_PRICE, it.priceText)
            addProperty(BuyerOrderDetailConst.PARAM_PRODUCT_LIST_IMAGE_URL, it.productThumbnailUrl)
        }
    }

    private fun applyTransition() {
        activity.overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_left_in_medium, com.tokopedia.resources.common.R.anim.slide_right_out_medium)
    }

    fun goToPrintInvoicePage(url: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalOrder.INVOICE)?.apply {
            putExtra(KEY_URL, url)
        }
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToTrackOrderPage(orderId: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalOrder.TRACK, "")
                .putExtra(ApplinkConstInternalOrder.EXTRA_ORDER_ID, orderId)
                .putExtra(ApplinkConstInternalOrder.EXTRA_USER_MODE, 1)
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToTrackShipmentPage(orderId: String, trackingUrl: String) {
        val appLink = Uri.parse(ApplinkConst.ORDER_TRACKING).buildUpon()
                .appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, trackingUrl)
                .build()
                .toString()
        val intent = RouteManager.getIntent(activity, appLink, orderId)
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToShopPage(shopId: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToProductSnapshotPage(orderId: String, orderDetailId: String) {
        val appLinkSnapShot = "${ApplinkConst.SNAPSHOT_ORDER}/$orderId/$orderDetailId"
        val intent = RouteManager.getIntent(activity, appLinkSnapShot)
        intent.putExtra(ApplinkConstInternalOrder.IS_SNAPSHOT_FROM_SOM, true)
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToCallingPage(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = composeCallIntentData(phoneNumber)
        }
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToRequestCancellationPage(
            fragment: Fragment,
            buyerOrderDetailData: Result<BuyerOrderDetailUiModel>?,
            button: ActionButtonsUiModel.ActionButton,
            cacheManager: SaveInstanceCacheManager
    ) {
        fragment.context?.let { context ->
            if (buyerOrderDetailData is Success) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalOrder.MP_INTERNAL_REQUEST_CANCEL)
                val payload: Bundle = Bundle().apply {
                    putString(BuyerOrderDetailConst.PARAM_SHOP_NAME, buyerOrderDetailData.data.productListUiModel.productListHeaderUiModel.shopName)
                    putString(BuyerOrderDetailConst.PARAM_INVOICE, buyerOrderDetailData.data.orderStatusUiModel.orderStatusInfoUiModel.invoice.invoice)
                    putString(BuyerOrderDetailConst.PARAM_JSON_LIST_PRODUCT, createProductListPayload(buyerOrderDetailData.data.productListUiModel.productList))
                    putString(BuyerOrderDetailConst.PARAM_ORDER_ID, buyerOrderDetailData.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId)
                    putBoolean(BuyerOrderDetailConst.PARAM_IS_CANCEL_ALREADY_REQUESTED, false) //TODO: use backend response
                    putString(BuyerOrderDetailConst.PARAM_TITLE_CANCEL_REQUESTED, button.popUp.title)
                    putString(BuyerOrderDetailConst.PARAM_BODY_CANCEL_REQUESTED, button.popUp.body)
                    putString(BuyerOrderDetailConst.PARAM_SHOP_ID, buyerOrderDetailData.data.productListUiModel.productListHeaderUiModel.shopId)
                }
                val cacheId = cacheManager.generateUniqueRandomNumber()
                cacheManager.put(cacheId, payload)
                intent.putExtra(BuyerOrderDetailConst.PARAM_CACHE_ID, cacheId)
                fragment.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_REQUEST_CANCEL_ORDER)
                applyTransition()
            }
        }
    }

    fun goToAskSeller(buyerOrderDetailData: BuyerOrderDetailUiModel) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, buyerOrderDetailData.productListUiModel.productListHeaderUiModel.shopId)
        intent.putExtra(ApplinkConst.Chat.INVOICE_ID, buyerOrderDetailData.orderStatusUiModel.orderStatusHeaderUiModel.orderId)
        intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, buyerOrderDetailData.orderStatusUiModel.orderStatusInfoUiModel.invoice.invoice)
        intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, buyerOrderDetailData.productListUiModel.productList.firstOrNull()?.productName.orEmpty())
        intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, buyerOrderDetailData.orderStatusUiModel.orderStatusInfoUiModel.purchaseDate)
        intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, buyerOrderDetailData.productListUiModel.productList.firstOrNull()?.productThumbnailUrl.orEmpty())
        intent.putExtra(ApplinkConst.Chat.INVOICE_URL, buyerOrderDetailData.orderStatusUiModel.orderStatusInfoUiModel.invoice.url)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, buyerOrderDetailData.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, buyerOrderDetailData.orderStatusUiModel.orderStatusHeaderUiModel.orderStatus)
        intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, buyerOrderDetailData.paymentInfoUiModel.paymentGrandTotal.value)
        intent.putExtra(ApplinkConst.Chat.SOURCE, ApplinkConst.Chat.SOURCE_ASK_SELLER)
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToCreateResolution(fragment: Fragment, url: String) {
        val intent: Intent = RouteManager.getIntent(fragment.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        fragment.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_CREATE_RESOLUTION)
        applyTransition()
    }

    fun openAppLink(appLink: String) {
        if (appLink.startsWith(PREFIX_HTTP)) {
            openWebView(appLink)
        } else {
            val intent: Intent = RouteManager.getIntent(activity, appLink)
            activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
            applyTransition()
        }
    }

    fun openWebView(url: String) {
        val intent: Intent = RouteManager.getIntent(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        activity.startActivityForResult(intent, BuyerOrderDetailConst.REQUEST_CODE_IGNORED)
        applyTransition()
    }
}