package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_REF

class CategoryProductRecommendationCallback(
    private val categoryIdL1: String,
    private val activity: FragmentActivity?,
    private val productRecommendationViewModel: TokoNowProductRecommendationViewModel?,
    private val onHideProductRecommendationWidget: () -> Unit,
    private val onClickProductCard: (appLink: String, headerName: String, index: Int, productId: String, productName: String, productPrice: String, isOos: Boolean) -> Unit,
    private val onImpressProductCard: (headerName: String, index: Int, productId: String, productName: String, productPrice: String, isOos: Boolean) -> Unit,
    private val onAddToCartBlocked: () -> Unit,
    private val onOpenLoginPage: () -> Unit,
    private val startActivityResult: (Intent, Int) -> Unit
): TokoNowProductRecommendationView.TokoNowProductRecommendationListener {
    override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel? = productRecommendationViewModel

    override fun hideProductRecommendationWidget() = onHideProductRecommendationWidget()

    override fun openLoginPage() = onOpenLoginPage()

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
                trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, categoryIdL1),
                startActivitResult = startActivityResult
            )
        }
    }

    override fun productCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) = onClickProductCard(
        product.appLink,
        product.headerName,
        position,
        product.productCardModel.productId,
        product.productCardModel.name,
        product.productCardModel.price,
        product.productCardModel.isOos()
    )

    override fun productCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) = onImpressProductCard(
        product.headerName,
        position,
        product.productCardModel.productId,
        product.productCardModel.name,
        product.productCardModel.price,
        product.productCardModel.isOos()
    )

    override fun seeMoreClicked(
        seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel
    ) {
        directToSeeMorePage(seeMoreUiModel.appLink)
    }

    override fun seeAllClicked(
        appLink: String
    ) {
        directToSeeMorePage(appLink)
    }

    override fun productCardAddToCartBlocked() = onAddToCartBlocked()

    private fun directToSeeMorePage(
        appLink: String
    ) {
        val modifiedAppLink = modifySeeMoreAppLink(appLink)
        RouteManager.route(activity, modifiedAppLink)
    }

    private fun modifySeeMoreAppLink(
        originalAppLink: String
    ): String {
        val uri = Uri.parse(originalAppLink)
        val queryParamsMap = UrlParamUtils.getParamMap(uri.query ?: String.EMPTY)
        val ref = queryParamsMap[RECOM_QUERY_PARAM_REF].orEmpty()

        return if (ref == RecomPageConstant.TOKONOW_CLP) {
            val categoryId = queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID].orEmpty()

            if (categoryId.isEmpty()) queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] = categoryIdL1

            "${uri.scheme}://${uri.host}/${uri.path}?" + UrlParamUtils.generateUrlParamString(queryParamsMap)
        } else {
            originalAppLink
        }
    }
}
