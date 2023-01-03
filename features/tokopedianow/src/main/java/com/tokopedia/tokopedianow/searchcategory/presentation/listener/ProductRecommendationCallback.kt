package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.utils.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.category.utils.RECOM_QUERY_PARAM_REF
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper.mapProductItemToRecommendationItem
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.search.analytics.SearchTracking
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

data class ProductRecommendationCallback(
    private val productRecommendationViewModel: TokoNowProductRecommendationViewModel?,
    private val baseSearchCategoryViewModel: BaseSearchCategoryViewModel,
    private val activity: FragmentActivity?,
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
    private val getListValue: (RecommendationItem) -> String,
): TokoNowProductRecommendationView.TokoNowProductRecommendationListener {
    companion object {
        private const val REQUEST_CODE_LOGIN = 69
    }

    override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel? = productRecommendationViewModel

    override fun hideProductRecommendationWidget() {
        baseSearchCategoryViewModel.removeProductRecommendationWidget()
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
        val recommendationItem = mapProductItemToRecommendationItem(product)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            ProductRecommendationTracking.getClickProductTracking(
                recommendationItem = recommendationItem,
                eventCategory = eventCategory,
                headerTitle = product.headerName,
                position = position,
                isLoggedIn = isLogin,
                userId = userId,
                eventLabel = eventLabel,
                eventAction = eventActionClicked,
                listValue = getListValue(recommendationItem),
            )
        )
        RouteManager.route(activity, product.appLink)
    }

    override fun productCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) {
        val recommendationItem = mapProductItemToRecommendationItem(product)
        trackingQueue?.putEETracking(
            ProductRecommendationTracking.getImpressionProductTracking(
                recommendationItem = recommendationItem,
                eventCategory = eventCategory,
                headerTitle = product.headerName,
                position = position,
                isLoggedIn = isLogin,
                userId = userId,
                eventLabel = eventLabel,
                eventAction = eventActionImpressed,
                listValue = getListValue(recommendationItem),
            )
        )
    }

    override fun seeMoreClicked(
        seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
    ) {
        directToSeeMorePage(seeMoreUiModel.appLink)
    }

    override fun seeAllClicked(
        appLink: String
    ) {
        directToSeeMorePage(appLink)
    }

    private fun directToSeeMorePage(
        appLink: String
    ) {
        val newAppLink = if (eventCategory == TOKONOW_CATEGORY_PAGE) {
            CategoryTracking.sendRecommendationSeeAllClickEvent(categoryIdTracking)
            modifySeeMoreAppLink(appLink)
        } else {
            SearchTracking.sendRecommendationSeeAllClickEvent(query)
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
