package com.tokopedia.buyerorderdetail.common.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailCommonIntentParamKey
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailIntentCode
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerRequestCancellationIntentParamKey
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet.MaskingPhoneNumberBottomSheet

class BuyerOrderDetailNavigator(
    private val activity: Activity,
    private val fragment: Fragment
) {

    companion object {
        private const val KEY_URL = "url"
        private const val TELEPHONY_URI = "tel:"
        private const val PREFIX_HTTP = "http"
        private const val PREFIX_HTTPS = "https://"
        private const val INVOICE_REF_NUM = "invoice_ref_num"
        private const val KEY_ORDER_CATEGORY = "KEY_ORDER_CATEGORY"
        private const val BUYER_MODE = 1
    }

    private fun applyTransition() {
        activity.overridePendingTransition(
            com.tokopedia.resources.common.R.anim.slide_right_in_medium,
            com.tokopedia.resources.common.R.anim.slide_left_out_medium
        )
    }

    fun goToBomDetailPage(orderId: String) {
        val appLink =
            Uri.parse(ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_DETAIL).buildUpon()
                .appendQueryParameter(DeeplinkMapperUoh.PATH_ORDER_ID, orderId)
                .build()
                .toString()
        val intent = RouteManager.getIntent(activity, appLink).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToPrintInvoicePage(url: String, invoiceNum: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalOrder.INVOICE)?.apply {
            putExtra(KEY_URL, url)
            putExtra(INVOICE_REF_NUM, invoiceNum)
            putExtra(KEY_ORDER_CATEGORY, BuyerOrderDetailMiscConstant.CATEGORY_MARKETPLACE)
        }
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToTrackOrderPage(orderId: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalOrder.TRACK, "")
            .putExtra(ApplinkConstInternalOrder.EXTRA_ORDER_ID, orderId)
            .putExtra(ApplinkConstInternalOrder.EXTRA_USER_MODE, BUYER_MODE)
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToTrackShipmentPage(appLink: String) {
        val intent = RouteManager.getIntent(activity, appLink)
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToShopPage(shopId: String) {
        val intent =
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.SHOP_PAGE, shopId)
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToProductSnapshotPage(orderId: String, orderDetailId: String) {
        val appLinkSnapShot = "${ApplinkConst.SNAPSHOT_ORDER}/$orderId/$orderDetailId"
        val intent = RouteManager.getIntent(activity, appLinkSnapShot)
        intent.putExtra(ApplinkConstInternalOrder.IS_SNAPSHOT_FROM_SOM, false)
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToCallingPage(phoneNumber: String) {
        val bottomSheetMaskingPhoneNumber =
            MaskingPhoneNumberBottomSheet.newInstance(phoneNumber)
        bottomSheetMaskingPhoneNumber.show(fragment.childFragmentManager)
    }

    fun goToRequestCancellationPage(
        uiState: BuyerOrderDetailUiState,
        button: ActionButtonsUiModel.ActionButton,
        cacheManager: SaveInstanceCacheManager
    ) {
        if (uiState is BuyerOrderDetailUiState.HasData) {
            val orderStatusUiModel = uiState.orderStatusUiState.data
            val productListUiModel = uiState.productListUiState.data
            val shipmentInfoUiModel = uiState.shipmentInfoUiState.data
            val owocInfoUiModel = shipmentInfoUiModel.owocInfoUiModel

            val intent = RouteManager.getIntent(activity, ApplinkConstInternalOrder.INTERNAL_ORDER_BUYER_CANCELLATION_REQUEST_PAGE)
            val payload: Map<String, Any?> = mapOf(
                BuyerRequestCancellationIntentParamKey.SHOP_NAME to productListUiModel.productListHeaderUiModel.shopName,
                BuyerRequestCancellationIntentParamKey.INVOICE to orderStatusUiModel.orderStatusInfoUiModel.invoice.invoice,
                BuyerOrderDetailCommonIntentParamKey.ORDER_ID to orderStatusUiModel.orderStatusHeaderUiModel.orderId,
                BuyerRequestCancellationIntentParamKey.IS_CANCEL_ALREADY_REQUESTED to false,
                BuyerRequestCancellationIntentParamKey.TITLE_CANCEL_REQUESTED to button.popUp.title,
                BuyerRequestCancellationIntentParamKey.BODY_CANCEL_REQUESTED to button.popUp.body,
                BuyerRequestCancellationIntentParamKey.SHOP_ID to productListUiModel.productListHeaderUiModel.shopId,
                BuyerRequestCancellationIntentParamKey.PARAM_TX_ID to owocInfoUiModel?.txId.orEmpty()
            )
            val cacheId = cacheManager.generateUniqueRandomNumber()
            cacheManager.put(cacheId, payload)
            intent.putExtra(BuyerOrderDetailCommonIntentParamKey.CACHE_ID, cacheId)
            intent.putExtra(BuyerOrderDetailCommonIntentParamKey.CACHE_MANAGER_ID, cacheManager.id)
            fragment.startActivityForResult(
                intent,
                BuyerOrderDetailIntentCode.REQUEST_CODE_REQUEST_CANCEL_ORDER
            )
            applyTransition()
        }
    }

    fun goToAskSeller(
        orderStatusUiModel: OrderStatusUiModel,
        productListUiModel: ProductListUiModel,
        paymentInfoUiModel: PaymentInfoUiModel
    ) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, productListUiModel.productListHeaderUiModel.shopId)
        intent.putExtra(ApplinkConst.Chat.INVOICE_ID, orderStatusUiModel.orderStatusHeaderUiModel.orderId)
        intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, orderStatusUiModel.orderStatusInfoUiModel.invoice.invoice)
        val productName =
            productListUiModel.productList.firstOrNull()?.productName
                ?: productListUiModel.productBundlingList.firstOrNull()?.bundleItemList?.firstOrNull()?.productName
        intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, productName.orEmpty())
        intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, orderStatusUiModel.orderStatusInfoUiModel.purchaseDate)
        val productThumbnail =
            productListUiModel.productList.firstOrNull()?.productThumbnailUrl
                ?: productListUiModel.productBundlingList.firstOrNull()?.bundleItemList?.firstOrNull()?.productThumbnailUrl
        intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, productThumbnail.orEmpty())
        intent.putExtra(ApplinkConst.Chat.INVOICE_URL, orderStatusUiModel.orderStatusInfoUiModel.invoice.url)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, orderStatusUiModel.orderStatusHeaderUiModel.orderStatus)
        intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, paymentInfoUiModel.paymentGrandTotal.value)
        intent.putExtra(ApplinkConst.Chat.SOURCE, ApplinkConst.Chat.SOURCE_ASK_SELLER)
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED)
        applyTransition()
    }

    fun goToCreateResolution(url: String) {
        val intent: Intent = RouteManager.getIntent(
            fragment.context,
            String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
        )
        fragment.startActivityForResult(
            intent,
            BuyerOrderDetailIntentCode.REQUEST_CODE_CREATE_RESOLUTION
        )
        applyTransition()
    }

    fun goToOrderExtension(orderId: String) {
        val params = mapOf(
            ApplinkConstInternalOrder.PARAM_ORDER_ID to orderId
        )
        val appLink = UriUtil.buildUriAppendParams(
            ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_EXTENSION,
            params
        )
        val intent = RouteManager.getIntent(fragment.context, appLink).apply {
            putExtra(ApplinkConstInternalOrder.OrderExtensionKey.IS_FROM_UOH, false)
        }
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_ORDER_EXTENSION)
        applyTransition()
    }

    fun goToPartialOrderFulfillment(orderId: String) {
        val params = mapOf(
            ApplinkConstInternalOrder.PARAM_ORDER_ID to orderId
        )
        val appLink = UriUtil.buildUriAppendParams(
            ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_PARTIAL_ORDER_FULFILLMENT,
            params
        )
        val intent = RouteManager.getIntent(fragment.context, appLink)
        fragment.startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_PARTIAL_ORDER_FULFILLMENT)
    }

    fun openAppLink(appLink: String, shouldRefreshWhenBack: Boolean): Boolean {
        val intent: Intent? = RouteManager.getIntentNoFallback(activity, appLink)
        return if (intent == null) {
            if (appLink.startsWith(PREFIX_HTTP)) {
                openWebView(appLink, shouldRefreshWhenBack)
                true
            } else {
                false
            }
        } else {
            val requestCode = if (shouldRefreshWhenBack) {
                BuyerOrderDetailIntentCode.REQUEST_CODE_REFRESH_ONLY
            } else {
                BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED
            }
            fragment.startActivityForResult(intent, requestCode)
            applyTransition()
            true
        }
    }

    fun openWebView(url: String, shouldRefreshWhenBack: Boolean) {
        val intent: Intent =
            RouteManager.getIntent(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        val requestCode =
            if (shouldRefreshWhenBack) {
                BuyerOrderDetailIntentCode.REQUEST_CODE_REFRESH_ONLY
            } else {
                BuyerOrderDetailIntentCode.REQUEST_CODE_IGNORED
            }
        fragment.startActivityForResult(intent, requestCode)
        applyTransition()
    }
}
