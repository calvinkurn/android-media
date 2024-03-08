package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
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
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.category.analytic.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.unifycomponents.ticker.TickerData

object CategoryL2Mapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        HEADLINE_L1
    )

    fun MutableList<Visitable<*>>.mapToCategoryUiModel(
        categoryModularResponse: CategoryGetDetailModular?,
        categoryDetailResponse: CategoryDetailResponse?,
        tickerList: List<TickerData>
    ) {
        val componentsListResponse = categoryModularResponse?.components

        componentsListResponse?.filter { SUPPORTED_LAYOUT_TYPES.contains(it.type) }
            ?.forEach { componentResponse ->
                val type = componentResponse.type

                when (type) {
                    HEADLINE_L1 -> addHeader(
                        componentResponse = componentResponse,
                        categoryDetailResponse = categoryDetailResponse
                    )
                }

                if(type == HEADLINE_L1) {
                    addTicker(tickerList)
                }
            }
    }

    fun MutableList<Visitable<*>>.addChooseAddress(
        localCacheModel: LocalCacheModel
    ) {
        add(TokoNowChooseAddressWidgetUiModel(
            id = CategoryStaticLayoutId.CHOOSE_ADDRESS,
            eventLabelHostPage = TOKONOW_CATEGORY_PAGE,
            localCacheModel = localCacheModel
        ))
    }

    fun MutableList<Visitable<*>>.filterNotLoadedLayout(): MutableList<Visitable<*>> {
        return filter { it.getLayoutState() == TokoNowLayoutState.LOADING }.toMutableList()
    }

    fun mapToCategoryTab(
        categoryIdL1: String,
        categoryIdL2: String,
        tickerData: GetTickerData,
        getCategoryLayoutResponse: CategoryGetDetailModular,
        categoryDetailResponse: CategoryDetailResponse,
        queryParamMap: HashMap<String, String>,
        deepLink: String
    ): CategoryL2TabUiModel {
        val componentListResponse = getCategoryLayoutResponse.components
        val id = componentListResponse
            .firstOrNull { it.type == TABS_HORIZONTAL_SCROLL }?.id.orEmpty()
        val categoryDetail = categoryDetailResponse.categoryDetail
        val categoryChildList = categoryDetail.data.child
        val tabComponents = componentListResponse.filterTabComponents()

        val categoryL2TabList = categoryChildList.map {
            CategoryL2TabData(
                title = it.name,
                componentList = tabComponents,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = it.id,
                tickerData = tickerData,
                categoryDetail = categoryDetail,
                queryParamMap = if(deepLink.contains(it.id)) {
                    queryParamMap
                } else {
                    hashMapOf()
                }
            )
        }

        val selectedTabPosition = categoryL2TabList
            .indexOfFirst { it.categoryIdL2 == categoryIdL2 }

        return CategoryL2TabUiModel(
            id = id,
            tabList = categoryL2TabList,
            selectedTabPosition = selectedTabPosition,
            state = TokoNowLayoutState.LOADED
        )
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

    private fun MutableList<Visitable<*>>.addTicker(tickerList: List<TickerData>) {
        if(tickerList.isNotEmpty()) {
            add(TokoNowTickerUiModel(
                id = CategoryStaticLayoutId.TICKER_WIDGET_ID,
                tickers = tickerList
            ))
        }
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
            else -> null
        }
    }

    private fun Visitable<*>.getLayoutState(): Int? {
        return when (this) {
            is CategoryL2HeaderUiModel -> state
            else -> null
        }
    }
}
