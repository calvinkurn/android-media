package com.tokopedia.product.detail.postatc.base

import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.postatc.view.PostAtcBottomSheet
import com.tokopedia.product.detail.postatc.view.component.addons.AddonsCallback
import com.tokopedia.product.detail.postatc.view.component.addons.AddonsCallbackImpl
import com.tokopedia.product.detail.postatc.view.component.error.ErrorCallback
import com.tokopedia.product.detail.postatc.view.component.error.ErrorCallbackImpl
import com.tokopedia.product.detail.postatc.view.component.productinfo.ProductInfoCallback
import com.tokopedia.product.detail.postatc.view.component.productinfo.ProductInfoCallbackImpl
import com.tokopedia.product.detail.postatc.view.component.recommendation.RecommendationCallback
import com.tokopedia.product.detail.postatc.view.component.recommendation.RecommendationCallbackImpl

class PostAtcCallback(
    fragment: PostAtcBottomSheet,
    addonsCallback: AddonsCallback = AddonsCallbackImpl(fragment),
    errorCallback: ErrorCallback = ErrorCallbackImpl(fragment),
    productInfoCallback: ProductInfoCallback = ProductInfoCallbackImpl(fragment),
    recommendationCallback: RecommendationCallback = RecommendationCallbackImpl(fragment)
) : BaseCallbackImpl(fragment),
    AddonsCallback by addonsCallback,
    ErrorCallback by errorCallback,
    ProductInfoCallback by productInfoCallback,
    RecommendationCallback by recommendationCallback {

    fun impressComponent(componentTrackData: ComponentTrackData) {
        /**
         * Currently No OP, will needed in future
         */
    }

    fun goToAppLink(appLink: String) {
        val fragment = fragment ?: return
        RouteManager.route(fragment.context, appLink)
    }

    fun removeComponent(uiModelId: Int) {
        fragment?.adapter?.removeComponent(uiModelId)
    }
}
