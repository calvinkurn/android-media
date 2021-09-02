package com.tokopedia.tokopedianow.recentpurchase.domain.mapper

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseProductMapper.mapToProductListUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLoadingUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductGridUiModel

object RepurchaseLayoutMapper {

    fun MutableList<Visitable<*>>.addLayoutList() {
        add(RepurchaseProductGridUiModel(emptyList()))
    }

    fun MutableList<Visitable<*>>.addProductGrid(response: List<RepurchaseProduct>) {
        val productList = response.mapToProductListUiModel()
        add(RepurchaseProductGridUiModel(productList))
    }

    fun MutableList<Visitable<*>>.addLoading() {
        add(RepurchaseLoadingUiModel)
    }

    fun MutableList<Visitable<*>>.addEmptyStateNoHistory(@StringRes description: Int) {
        removeFirstLayout(RepurchaseProductGridUiModel::class.java)
        add(RepurchaseEmptyStateNoHistoryUiModel(description))
    }

    fun MutableList<Visitable<*>>.addCategoryGrid(response: List<CategoryResponse>?, context: Context) {
        val categoryList = RepurchaseCategoryMapper.mapToCategoryList(response)
        add(TokoNowCategoryGridUiModel(
                id = "",
                title = context.getString(R.string.tokopedianow_repurchase_title_page),
                categoryList = categoryList,
                state = TokoNowLayoutState.SHOW
            )
        )
    }

    fun MutableList<Visitable<*>>.addProductRecom(pageName: String, recommendationWidget: RecommendationWidget) {
        add(TokoNowRecommendationCarouselUiModel(
                pageName = pageName,
                carouselData = RecommendationCarouselData(
                    recommendationData = recommendationWidget,
                    state = STATE_READY
                )
            )
        )
    }

    fun MutableList<Visitable<*>>.addChooseAddress() {
        add(TokoNowChooseAddressWidgetUiModel())
    }

    fun MutableList<Visitable<*>>.addEmptyStateOoc() {
        add(TokoNowEmptyStateOocUiModel())
    }

    fun MutableList<Visitable<*>>.addEmptyStateNoResult() {
        add(TokoNowEmptyStateNoResultUiModel())
    }

    fun MutableList<Visitable<*>>.removeLoading() {
        removeFirstLayout(RepurchaseLoadingUiModel::class.java)
    }

    fun MutableList<Visitable<*>>.removeChooseAddress() {
        removeFirstLayout(TokoNowChooseAddressWidgetUiModel::class.java)
    }

    private fun <T> MutableList<Visitable<*>>.removeFirstLayout(model: Class<T>) {
        firstOrNull { it.javaClass == model }?.let {
            val index = indexOf(it)
            removeAt(index)
        }
    }
}
