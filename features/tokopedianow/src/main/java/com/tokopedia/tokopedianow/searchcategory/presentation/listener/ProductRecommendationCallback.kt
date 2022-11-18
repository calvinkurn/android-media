package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.category.utils.RECOM_QUERY_PARAM_CATEGORY_ID
import com.tokopedia.tokopedianow.category.utils.RECOM_QUERY_PARAM_REF
import com.tokopedia.tokopedianow.common.constant.RequestCode
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel

class ProductRecommendationCallback(
    private val productRecommendationViewModel: TokoNowProductRecommendationViewModel?,
    private val baseSearchCategoryViewModel: BaseSearchCategoryViewModel,
    private val activity: FragmentActivity?,
    private val startActivityForResult: (Intent, Int) -> Unit,
    private val cdListName: String = "",
    private val categoryL1: String = ""
): TokoNowProductRecommendationView.TokoNowProductRecommendationListener {
    override fun getProductRecommendationViewModel(): TokoNowProductRecommendationViewModel? = productRecommendationViewModel

    override fun hideProductRecommendationWidget() {
        baseSearchCategoryViewModel.removeProductRecommendationWidget()
    }

    override fun openLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        activity?.startActivityForResult(intent, RequestCode.REQUEST_CODE_LOGIN)
    }

    override fun productCardAddVariantClicked(productId: String, shopId: String) {
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
        product: TokoNowProductCardCarouselItemUiModel
    ) {
        RouteManager.route(activity, product.appLink)
    }

    override fun productCardImpressed(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {

    }

    override fun productCardQuantityChanged(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    ) {

    }

    override fun seeMoreClicked(seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel) {
        RouteManager.route(activity, modifySeeMoreAppLink(seeMoreUiModel.appLink))
    }

    override fun seeAllClicked(appLink: String) {
        RouteManager.route(activity, modifySeeMoreAppLink(appLink))
    }

    private fun modifySeeMoreAppLink(originalAppLink: String): String {
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
