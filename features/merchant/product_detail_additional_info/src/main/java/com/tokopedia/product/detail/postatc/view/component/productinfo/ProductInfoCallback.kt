package com.tokopedia.product.detail.postatc.view.component.productinfo

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.postatc.base.BaseCallbackImpl
import com.tokopedia.product.detail.postatc.base.ComponentTrackData
import com.tokopedia.product.detail.postatc.tracker.PostAtcTracking
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet

interface ProductInfoCallback {
    fun onClickLihatKeranjang(cartId: String, componentTrackData: ComponentTrackData)
    fun goToCart(cartId: String)
}

class ProductInfoCallbackImpl(
    fragment: PostAtcBottomSheet
) : BaseCallbackImpl(fragment), ProductInfoCallback {
    override fun onClickLihatKeranjang(
        cartId: String,
        componentTrackData: ComponentTrackData
    ) {
        val fragment = fragment ?: return
        PostAtcTracking.sendClickLihatKeranjang(
            fragment.userSession.userId,
            fragment.viewModel.postAtcInfo,
            componentTrackData
        )

        goToCart(cartId)
    }

    override fun goToCart(cartId: String) {
        val fragment = fragment ?: return
        val intent = RouteManager.getIntent(fragment.context, ApplinkConst.CART)
        intent.putExtra("cart_id", cartId)
        fragment.startActivity(intent)
        fragment.dismissAllowingStateLoss()
    }
}
