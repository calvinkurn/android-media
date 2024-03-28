package com.tokopedia.product.detail.postatc.view.component.productinfo

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EnterMethod
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.postatc.base.ComponentTrackData
import com.tokopedia.product.detail.postatc.base.PostAtcBottomSheetDelegate
import com.tokopedia.product.detail.postatc.tracker.PostAtcTracking
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet

interface ProductInfoCallback {
    fun onClickLihatKeranjang(cartId: String, componentTrackData: ComponentTrackData)
    fun goToCart(cartId: String)
}

class ProductInfoCallbackImpl(
    fragment: PostAtcBottomSheet
) : ProductInfoCallback, PostAtcBottomSheetDelegate by fragment {
    override fun onClickLihatKeranjang(
        cartId: String,
        componentTrackData: ComponentTrackData
    ) {
        PostAtcTracking.sendClickLihatKeranjang(
            userSession.userId,
            viewModel.postAtcInfo,
            componentTrackData
        )
        AppLogAnalytics.putPreviousPageData(
            AppLogParam.ENTER_METHOD,
            EnterMethod.CLICK_ATC_TOASTER_PDP.str
        )

        goToCart(cartId)
    }

    override fun goToCart(cartId: String) {
        val context = getContext() ?: return
        val intent = RouteManager.getIntent(context, ApplinkConst.CART)
        intent.putExtra("cart_id", cartId)
        AppLogAnalytics.putPreviousPageData(
            AppLogParam.ENTER_METHOD,
            EnterMethod.CLICK_ATC_TOASTER_PDP.str
        )
        startActivity(intent)
        dismissAllowingStateLoss()
    }
}
