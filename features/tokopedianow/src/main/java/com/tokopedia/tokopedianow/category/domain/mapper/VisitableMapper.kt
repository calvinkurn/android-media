package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.RECOM_WIDGET
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.TOKONOW_CLP
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryPageMapper.mapToShowcaseProductCard
import com.tokopedia.tokopedianow.category.domain.response.CategoryHeaderResponse
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryShowcaseUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel

internal object VisitableMapper {
    fun MutableList<Visitable<*>>.addHeaderSpace(
        space: Int,
        headerResponse: CategoryHeaderResponse
    ) {
        add(
            headerResponse.mapToHeaderSpace(
                space = space
            )
        )
    }

    fun MutableList<Visitable<*>>.addChooseAddress(
        headerResponse: CategoryHeaderResponse
    )  {
        add(headerResponse.mapToChooseAddress())
    }

    fun MutableList<Visitable<*>>.addCategoryTitle(
        headerResponse: CategoryHeaderResponse
    ) {
        add(headerResponse.mapToCategoryTitle())
    }

    fun MutableList<Visitable<*>>.addCategoryNavigation(
        categoryNavigationUiModel: CategoryNavigationUiModel
    ) {
        add(categoryNavigationUiModel)
    }

    fun MutableList<Visitable<*>>.addProductRecommendation(categoryId: List<String>) {
        add(
            TokoNowProductRecommendationUiModel(
                requestParam = GetRecommendationRequestParam(
                    queryParam = TOKONOW_CLP,
                    pageName = TOKONOW_CLP,
                    categoryIds = categoryId,
                    xSource = RECOM_WIDGET,
                    isTokonow = true,
                    pageNumber = PAGE_NUMBER_RECOM_WIDGET,
                    xDevice = DEFAULT_VALUE_OF_PARAMETER_DEVICE,
                )
            )
        )
    }
    fun MutableList<Visitable<*>>.addCategoryShowcase(
        model: AceSearchProductModel = AceSearchProductModel(),
        categoryIdL2: String = String.EMPTY,
        title: String = String.EMPTY,
        seeAllAppLink: String = String.EMPTY,
        @TokoNowLayoutState state: Int
    ) {
        add(
            model.mapToShowcaseProductCard(
                categoryIdL2 = categoryIdL2,
                title = title,
                state = state,
                seeAllAppLink = seeAllAppLink
            )
        )
    }

    fun MutableList<Visitable<*>>.addRecipeProgressBar() {
        add(TokoNowProgressBarUiModel())
    }

    fun MutableList<Visitable<*>>.addCategoryMenu(
        categoryMenuUiModel: TokoNowCategoryMenuUiModel
    ) {
        add(categoryMenuUiModel)
    }

    fun MutableList<Visitable<*>>.mapCategoryShowcase(
        model: AceSearchProductModel,
        categoryIdL2: String,
        title: String,
        seeAllAppLink: String
    ) {
        updateItemById(
            id = categoryIdL2,
            block = {
                model.mapToShowcaseProductCard(
                    categoryIdL2 = categoryIdL2,
                    title = title,
                    state = TokoNowLayoutState.SHOW,
                    seeAllAppLink = seeAllAppLink
                )
            }
        )
    }

    fun MutableList<Visitable<*>>.removeRecipeProgressBar() {
        removeFirst { it is TokoNowProgressBarUiModel }
    }

    fun MutableList<Visitable<*>>.updateItemById(id: String?, block: () -> Visitable<*>?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    fun MutableList<Visitable<*>>.removeItem(id: String?) {
        getItemIndex(id)?.let { removeAt(it) }
    }

    fun MutableList<Visitable<*>>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is CategoryShowcaseUiModel -> id
            else -> null
        }
    }
}
