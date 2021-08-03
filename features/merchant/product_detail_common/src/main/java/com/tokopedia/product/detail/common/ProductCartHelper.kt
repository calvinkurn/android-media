package com.tokopedia.product.detail.common

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.product.detail.common.bottomsheet.OvoFlashDealsBottomSheet
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant

/**
 * Created by Yehezkiel on 17/05/21
 */
object ProductCartHelper {

    fun getBoTrackerString(boType: Int): String {
        return when (boType) {
            ProductDetailCommonConstant.BEBAS_ONGKIR_EXTRA -> {
                ProductDetailCommonConstant.VALUE_BEBAS_ONGKIR_EXTRA
            }
            ProductDetailCommonConstant.BEBAS_ONGKIR_NORMAL -> {
                ProductDetailCommonConstant.VALUE_BEBAS_ONGKIR
            }
            ProductDetailCommonConstant.BO_TOKONOW -> {
                ProductDetailCommonConstant.VALUE_TOKONOW
            }
            else -> "none/other"
        }
    }

    fun validateOvo(activity: FragmentActivity?,
                    result: AddToCartDataModel,
                    parentProductId: String,
                    userId: String,
                    refreshPage: () -> Unit,
                    onError: () -> Unit
    ) {
        if (result.data.refreshPrerequisitePage) {
            refreshPage.invoke()
        } else {
            activity?.let {
                when (result.data.ovoValidationDataModel.status) {
                    ProductDetailCommonConstant.OVO_INACTIVE_STATUS -> {
                        val applink = "${result.data.ovoValidationDataModel.applink}&product_id=${
                            parentProductId
                        }"
                        ProductTrackingCommon.eventActivationOvo(
                                parentProductId,
                                userId)
                        RouteManager.route(it, applink)
                    }
                    ProductDetailCommonConstant.OVO_INSUFFICIENT_BALANCE_STATUS -> {
                        val bottomSheetOvoDeals = OvoFlashDealsBottomSheet(
                                parentProductId,
                                userId,
                                result.data.ovoValidationDataModel)
                        bottomSheetOvoDeals.show(it.supportFragmentManager, "Ovo Deals")
                    }
                    else -> onError.invoke()
                }
            }
        }
    }

    fun generateButtonAction(it: String, atcButton: Boolean): Int {
        return when {
            atcButton -> ProductDetailCommonConstant.ATC_BUTTON
            it == ProductDetailCommonConstant.KEY_NORMAL_BUTTON -> {
                ProductDetailCommonConstant.BUY_BUTTON
            }
            it == ProductDetailCommonConstant.KEY_OCS_BUTTON -> {
                ProductDetailCommonConstant.OCS_BUTTON
            }
            it == ProductDetailCommonConstant.KEY_OCC_BUTTON -> {
                ProductDetailCommonConstant.OCC_BUTTON
            }
            it == ProductDetailCommonConstant.KEY_REMIND_ME -> {
                ProductDetailCommonConstant.REMIND_ME_BUTTON
            }
            it == ProductDetailCommonConstant.KEY_CHECK_WISHLIST -> {
                ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON
            }
            else -> ProductDetailCommonConstant.BUY_BUTTON
        }
    }

    fun goToCheckout(activity: Activity, shipmentFormRequest: Bundle) {
        val intent = RouteManager.getIntent(activity.applicationContext, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, true)
        intent.putExtras(shipmentFormRequest)
        activity.startActivityForResult(intent, ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
    }

    fun goToOneClickCheckout(activity: Activity) {
        val intent = RouteManager.getIntent(activity.applicationContext, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
        activity.startActivityForResult(intent, ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
    }

    fun goToCartCheckout(activity: Activity, cartId: String) {
        val intent = RouteManager.getIntent(activity.applicationContext, ApplinkConst.CART)
        intent?.run {
            putExtra(ApplinkConst.Transaction.EXTRA_CART_ID, cartId)
            activity.startActivityForResult(intent, ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
        }
    }

}