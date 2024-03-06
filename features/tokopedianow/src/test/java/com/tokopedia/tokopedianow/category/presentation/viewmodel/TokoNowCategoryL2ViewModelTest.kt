package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.model.CategoryOpenScreenTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2ShimmerUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.mapper.CategoryL2Mapper
import com.tokopedia.tokopedianow.category.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.category.analytic.CategoryTracking
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoNowCategoryL2ViewModelTest: TokoNowCategoryL2ViewModelTestFixture() {

    @Test
    fun `when call onViewCreated should update category tab and visitable list live data`() {
        onGetWarehousesData_thenReturn(warehouses)
        onGetCategoryLayout_thenReturn(getCategoryLayoutResponse)
        onGetCategoryDetail_thenReturn(getCategoryDetailResponse)

        viewModel.onViewCreated()

        val headerComponentResponse = getCategoryLayoutResponse.components
            .first { it.type == "headline-l1" }
        val categoryDetailResponse = getCategoryDetailResponse.categoryDetail.data

        val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(
            id = CategoryStaticLayoutId.CHOOSE_ADDRESS,
            eventLabelHostPage = CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
        )

        val header = CategoryL2HeaderUiModel(
            id = headerComponentResponse.id,
            title = categoryDetailResponse.name,
            appLink = categoryDetailResponse.applinks,
            state = TokoNowLayoutState.LOADED
        )

        val ticker = TokoNowTickerUiModel(
            id = "ticker_widget",
            tickers = listOf(
                TickerData(
                    description = "Notification content baru sedang berkendala, Mohon cek berkala.",
                    type = 3
                )
            ),
            backgroundLightColor = "",
            backgroundDarkColor = "",
            hasOutOfStockTicker = false
        )

        val expectedOpenScreenTracker = CategoryOpenScreenTrackerModel(
            id = categoryDetailResponse.id,
            name = categoryDetailResponse.name,
            url = categoryDetailResponse.url
        )

        val expectedCategoryTab = CategoryL2Mapper.mapToCategoryTab(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            tickerData = TickerMapper.mapTickerData(getTargetedTickerResponse),
            getCategoryLayoutResponse = getCategoryLayoutResponse,
            categoryDetailResponse = getCategoryDetailResponse
        )

        val expectedVisitableList = listOf(
            chooseAddressWidget,
            header,
            ticker
        )

        viewModel.categoryTabLiveData
            .verifyValueEquals(expectedCategoryTab)

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)

        viewModel.openScreenTracker
            .verifyValueEquals(expectedOpenScreenTracker)
    }

    @Test
    fun `when call showPageLoading should add choose address and shimmer visitable`() {
        viewModel.showPageLoading()

        val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(
            id = CategoryStaticLayoutId.CHOOSE_ADDRESS,
            eventLabelHostPage = CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
        )

        val expectedVisitableList = listOf(
            chooseAddressWidget,
            CategoryL2ShimmerUiModel
        )

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given showPageLoading when call removeChooseAddressWidget should remove choose from visitableList`() {
        viewModel.showPageLoading()
        viewModel.removeChooseAddressWidget()

        val expectedVisitableList = listOf(
            CategoryL2ShimmerUiModel
        )

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given address data when getWarehouseIds should return warehouseIsd string`() {
        val addressData = LocalCacheModel(
            warehouses = listOf(
                LocalWarehouseModel(warehouse_id = 151245L, service_type = "fc"),
                LocalWarehouseModel(warehouse_id = 151246L, service_type = "hub")
            )
        )

        onGetAddressData_thenReturn(addressData)

        val expectedWarehouseIds = "151245,151246"
        val actualWarehouseIds = viewModel.getWarehouseIds()

        assertEquals(expectedWarehouseIds, actualWarehouseIds)
    }

    @Test
    fun `when onTabSelected should update categoryIdL2 and selectedTabPosition value`() {
        onGetWarehousesData_thenReturn(warehouses)
        onGetCategoryLayout_thenReturn(getCategoryLayoutResponse)
        onGetCategoryDetail_thenReturn(getCategoryDetailResponse)

        viewModel.onViewCreated()
        viewModel.onTabSelected(1)

        val tickerData = TickerMapper.mapTickerData(getTargetedTickerResponse)
        val componentListResponse = getCategoryLayoutResponse.components
        val tabComponentResponse = componentListResponse.first { it.type == "tabs-horizontal-scroll" }

        val categoryDetailResponse = getCategoryDetailResponse.categoryDetail
        val categoryChildList = categoryDetailResponse.data.child
        val tabComponents = componentListResponse.filter { supportedLayoutTypes.contains(it.type) }

        val categoryL2TabList = categoryChildList.map {
            CategoryL2TabData(
                title = it.name,
                componentList = tabComponents,
                categoryIdL1 = categoryIdL1,
                categoryIdL2 = it.id,
                tickerData = tickerData,
                categoryDetail = categoryDetailResponse
            )
        }

        val expectedCategoryTab = CategoryL2TabUiModel(
            id = tabComponentResponse.id,
            tabList = categoryL2TabList,
            selectedTabPosition = 1,
            state = TokoNowLayoutState.LOADED
        )

        val expectedCategoryIdL2 = categoryL2TabList[1].categoryIdL2
        val actualCategoryIdL2 = viewModel.categoryIdL2

        viewModel.onTabSelectedLiveData
            .verifyValueEquals(expectedCategoryTab)

        assertEquals(expectedCategoryIdL2, actualCategoryIdL2)
    }
}
