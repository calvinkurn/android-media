package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.category.analytic.CategoryTracking
import com.tokopedia.tokopedianow.category.analytic.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.constant.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.category.constant.RECOM_QUERY_PARAM_REF
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper.mapProductItemToRecommendationItem
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker
import com.tokopedia.tokopedianow.search.presentation.viewmodel.TokoNowSearchViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue

data class ProductRecommendationCallback(
    private val productRecommendationViewModel: TokoNowProductRecommendationViewModel?,
    private val searchViewModel: TokoNowSearchViewModel,
    private val activity: FragmentActivity?,
    private val onAddToCartBlocked: () -> Unit,
    private val startActivityForResult: (Intent, Int) -> Unit,

    /**
     * Analytics Purpose
     */
    private val cdListName: String = "",
    private val categoryL1: String = "",
    private val categoryIdTracking: String = "",
    private val query: String = "",
    private val trackingQueue: TrackingQueue?,
    private val eventLabel: String,
    private val eventActionClicked: String,
    private val eventActionImpressed: String,
    private val eventCategory: String,
    private val getListValue: (RecommendationItem) -> String
) : TokoNowProductRecommendationView.TokoNowProductRecommendationListener {
    companion object {
        private const val REQUEST_CODE_LOGIN = 69
    }

    override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel? = productRecommendationViewModel

    override fun hideProductRecommendationWidget() {
        searchViewModel.removeProductRecommendationWidget()
    }

    override fun openLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        activity?.startActivityForResult(intent, REQUEST_CODE_LOGIN)
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
                startActivitResult = startActivityForResult
            )
        }
    }

    override fun productCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) {
        val recommendationItem = mapProductItemToRecommendationItem(product)
        val appLink = searchViewModel.createAffiliateLink(product.appLink)

        SearchResultTracker.trackClickProduct(
            position,
            eventLabel,
            eventActionClicked,
            eventCategory,
            getListValue(recommendationItem),
            userId,
            recommendationItem
        )

        RouteManager.route(activity, appLink)
    }

    override fun productCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) {
        val recommendationItem = mapProductItemToRecommendationItem(product)
        SearchResultTracker.trackImpressionProduct(
            position,
            eventLabel,
            eventActionImpressed,
            eventCategory,
            getListValue(recommendationItem),
            userId,
            recommendationItem
        )
    }

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
        val newAppLink = if (eventCategory == TOKONOW_CATEGORY_PAGE) {
            CategoryTracking.sendRecommendationSeeAllClickEvent(categoryIdTracking)
            modifySeeMoreAppLink(appLink)
        } else {
            SearchResultTracker.sendRecommendationSeeAllClickEvent(query)
            appLink
        }
        RouteManager.route(activity, newAppLink)
    }

    private fun modifySeeMoreAppLink(
        originalAppLink: String
    ): String {
        val uri = Uri.parse(originalAppLink)
        val queryParamsMap = UrlParamUtils.getParamMap(uri.query ?: "")
        val ref = queryParamsMap[RECOM_QUERY_PARAM_REF] ?: ""

        return if (ref == RecomPageConstant.TOKONOW_CLP) {
            val recomCategoryId = queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] ?: ""

            if (recomCategoryId.isEmpty()) {
                queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] = categoryL1
            }

            "${uri.scheme}://" +
                "${uri.host}/" +
                "${uri.path}?" +
                UrlParamUtils.generateUrlParamString(queryParamsMap)
        } else {
            originalAppLink
        }
    }
}
