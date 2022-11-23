package com.tokopedia.tokopedianow.repurchase.presentation.listener

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.tokopedianow.common.constant.RequestCode
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.repurchase.presentation.viewmodel.TokoNowRepurchaseViewModel

class ProductRecommendationCallback(
    private val productRecommendationViewModel: TokoNowProductRecommendationViewModel?,
    private val tokoNowRepurchaseViewModel: TokoNowRepurchaseViewModel,
    private val activity: FragmentActivity?,
    private val startActivityForResult: (Intent, Int) -> Unit,
    private val cdListName: String = "",
): TokoNowProductRecommendationView.TokoNowProductRecommendationListener {
    override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel? = productRecommendationViewModel

    override fun hideProductRecommendationWidget() {
        tokoNowRepurchaseViewModel.removeProductRecommendationWidget()
    }

    override fun openLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        activity?.startActivityForResult(intent, RequestCode.REQUEST_CODE_LOGIN)
    }

    override fun productCardAddVariantClicked(
        productId: String,
        shopId: String
    ) {
        activity?.apply {
            AtcVariantHelper.goToAtcVariant(
                context = this,
                productId = productId,
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = shopId,
                trackerCdListName = cdListName,
                startActivitResult = startActivityForResult,
            )
        }
    }

    override fun productCardClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) {
        RouteManager.route(activity, product.appLink)
    }

    override fun seeMoreClicked(
        seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
    ) {
        RouteManager.route(activity, seeMoreUiModel.appLink)
    }

    override fun seeAllClicked(
        appLink: String
    ) {
        RouteManager.route(activity, appLink)
    }

    override fun productCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) { /* nothing to do */ }

}
