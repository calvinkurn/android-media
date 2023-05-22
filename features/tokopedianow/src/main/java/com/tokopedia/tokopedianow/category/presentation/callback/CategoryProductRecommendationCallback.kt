package com.tokopedia.tokopedianow.category.presentation.callback

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
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.common.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.oldcategory.utils.RECOM_QUERY_PARAM_REF

class CategoryProductRecommendationCallback(
    private val productRecommendationViewModel: TokoNowProductRecommendationViewModel?,
    private val activity: FragmentActivity?,
    private val startActivityForResult: (Intent, Int) -> Unit,
): TokoNowProductRecommendationView.TokoNowProductRecommendationListener {
    companion object {
        private const val REQUEST_CODE_LOGIN = 69
    }

    override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel? = productRecommendationViewModel

    override fun hideProductRecommendationWidget() {

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
                trackerCdListName = String.format(CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC, "CategoryIdLv1"),
                startActivitResult = startActivityForResult,
            )
        }
    }

    override fun productCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) {
        RouteManager.route(activity, product.appLink)
    }

    override fun productCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        isLogin: Boolean,
        userId: String
    ) {
//        val recommendationItem =
//            ProductRecommendationMapper.mapProductItemToRecommendationItem(product)
//        SearchResultTracker.trackImpressionProduct(
//            position,
//            eventLabel,
//            eventActionImpressed,
//            eventCategory,
//            getListValue(recommendationItem),
//            userId,
//            recommendationItem
//        )
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

    private fun directToSeeMorePage(
        appLink: String
    ) {
//        CategoryTracking.sendRecommendationSeeAllClickEvent(categoryIdTracking)
        modifySeeMoreAppLink(appLink)
        RouteManager.route(activity, appLink)
    }

    private fun modifySeeMoreAppLink(
        originalAppLink: String
    ): String {
        val uri = Uri.parse(originalAppLink)
        val queryParamsMap = UrlParamUtils.getParamMap(uri.query ?: "")
        val ref = queryParamsMap[RECOM_QUERY_PARAM_REF] ?: ""

        return if (ref == RecomPageConstant.TOKONOW_CLP) {
            val recomCategoryId = queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] ?: ""

//            if (recomCategoryId.isEmpty()) {
//                queryParamsMap[RECOM_QUERY_PARAM_CATEGORY_ID] = categoryL1
//            }

            "${uri.scheme}://" +
                "${uri.host}/" +
                "${uri.path}?" +
                UrlParamUtils.generateUrlParamString(queryParamsMap)
        } else {
            originalAppLink
        }
    }
}
