package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.filterTabComponents
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.CategoryGetDetailModular
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.HEADLINE_L1
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentType.Companion.TABS_HORIZONTAL_SCROLL
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE

object CategoryL2Mapper {

    private const val DEFAULT_INDEX = 0

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        HEADLINE_L1
    )

    fun MutableList<Visitable<*>>.mapToCategoryUiModel(
        categoryModularResponse: CategoryGetDetailModular?,
        categoryDetailResponse: CategoryDetailResponse?
    ) {
        val componentsListResponse = categoryModularResponse?.components

        componentsListResponse?.filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }
            ?.forEach { componentResponse ->
                when (componentResponse.type) {
                    HEADLINE_L1 -> addHeader(
                        componentResponse = componentResponse,
                        categoryDetailResponse = categoryDetailResponse
                    )
                }
            }
    }

    fun MutableList<Visitable<*>>.addChooseAddress() {
        add(TokoNowChooseAddressWidgetUiModel(
            trackingSource = CategoryStaticLayoutId.CHOOSE_ADDRESS,
            eventLabelHostPage = TOKONOW_CATEGORY_PAGE
        ))
    }

    fun MutableList<Visitable<*>>.filterNotLoadedLayout(): MutableList<Visitable<*>> {
        return filter { it.getLayoutState() == TokoNowLayoutState.LOADING }.toMutableList()
    }

    private fun MutableList<Visitable<*>>.addHeader(
        componentResponse: Component,
        categoryDetailResponse: CategoryDetailResponse?
    ) {
        categoryDetailResponse?.let {
            val data = it.categoryDetail.data
            add(
                CategoryL2HeaderUiModel(
                    id = componentResponse.id,
                    title = data.name,
                    appLink = data.applinks,
                    state = TokoNowLayoutState.LOADED
                )
            )
        }
    }

    fun mapToCategoryTab(
        categoryIdL1: String,
        categoryIdL2: String,
        tickerData: GetTickerData,
        getCategoryLayoutResponse: CategoryGetDetailModular?,
        categoryDetailResponse: CategoryDetailResponse?
    ): CategoryL2TabUiModel {
        val componentListResponse = getCategoryLayoutResponse?.components.orEmpty()
        val id = componentListResponse
            .firstOrNull { it.type == TABS_HORIZONTAL_SCROLL }?.id.orEmpty()
        val categoryDetail = categoryDetailResponse?.categoryDetail
        val categoryChildList = categoryDetail?.data?.child.orEmpty()
        val categoryNameList = categoryChildList.map { it.name }
        val categoryL2Ids = categoryChildList.map { it.id }

        val tabComponents = componentListResponse.filterTabComponents()
        val selectedTabPosition = if (categoryL2Ids.contains(categoryIdL2)) {
            categoryL2Ids.indexOf(categoryIdL2)
        } else {
            DEFAULT_INDEX
        }

        val categoryL2TabList = categoryL2Ids.map {
            CategoryL2TabData(
                componentList = tabComponents,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = it,
                tickerData = tickerData
            )
        }

        return CategoryL2TabUiModel(
            id = id,
            titleList = categoryNameList,
            tabList = categoryL2TabList,
            selectedTabPosition = selectedTabPosition,
            state = TokoNowLayoutState.LOADED
        )
    }

    private fun MutableList<Visitable<*>>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    private fun MutableList<Visitable<*>>.updateItemById(id: String?, block: () -> Visitable<*>?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is CategoryL2HeaderUiModel -> id
            is CategoryL2TabUiModel -> id
            else -> null
        }
    }

    private fun Visitable<*>.getLayoutState(): Int? {
        return when (this) {
            is CategoryL2HeaderUiModel -> state
            is CategoryL2TabUiModel -> state
            else -> null
        }
    }
}
