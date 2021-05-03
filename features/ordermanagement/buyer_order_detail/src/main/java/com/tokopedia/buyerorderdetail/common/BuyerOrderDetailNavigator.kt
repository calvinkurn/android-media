package com.tokopedia.buyerorderdetail.common

import android.content.Context
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
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object BuyerOrderDetailNavigator {

    private const val KEY_URL = "url"
    private const val TELEPHONY_URI = "tel:"

    fun goToPrintInvoicePage(context: Context, url: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalOrder.INVOICE)?.apply {
            putExtra(KEY_URL, url)
        }
        context.startActivity(intent)
    }

    fun goToTrackOrderPage(context: Context, orderId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalOrder.TRACK, "")
                .putExtra(ApplinkConstInternalOrder.EXTRA_ORDER_ID, orderId)
                .putExtra(ApplinkConstInternalOrder.EXTRA_USER_MODE, 1)
        context.startActivity(intent)
    }

    fun goToTrackShipmentPage(context: Context, orderId: String, trackingUrl: String) {
        val appLink = Uri.parse(ApplinkConst.ORDER_TRACKING).buildUpon()
                .appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, trackingUrl)
                .build()
                .toString()
        RouteManager.route(context, appLink, orderId)
    }

    fun goToShopPage(context: Context, shopId: String) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
    }

    fun goToProductSnapshotPage(context: Context, orderId: String, orderDetailId: String) {
        val appLinkSnapShot = "${ApplinkConst.SNAPSHOT_ORDER}/$orderId/$orderDetailId"
        val intent = RouteManager.getIntent(context, appLinkSnapShot)
        intent.putExtra(ApplinkConstInternalOrder.IS_SNAPSHOT_FROM_SOM, true)
        context.startActivity(intent)
    }

    fun goToCallingPage(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = composeCallIntentData(phoneNumber)
        }
        context.startActivity(intent)
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
                fragment.startActivityForResult(intent, BuyerOrderDetailFragment.REQUEST_CANCEL_ORDER)
            }
        }
    }

    fun goToAskSeller(context: Context, buyerOrderDetailData: BuyerOrderDetailUiModel) {
        val intent = RouteManager.getIntent(context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, buyerOrderDetailData.productListUiModel.productListHeaderUiModel.shopId)
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
        context.startActivity(intent)
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
}