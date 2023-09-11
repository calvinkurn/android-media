package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.ProductBundleRecomResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.data.BundleWidgetDataFactory
import com.tokopedia.tokopedianow.data.createCategoryGridDataModel
import com.tokopedia.tokopedianow.data.createDynamicLegoBannerDataModel
import com.tokopedia.tokopedianow.data.createHomeLayoutList
import com.tokopedia.tokopedianow.data.createSliderBannerDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.BundleMapper
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestBundle : TokoNowHomeViewModelTestFixture() {
    @Test
    fun `when fetching bundle then receiving success result, expected result should return homeLayout with bundle widget`() {
        // create dummy data
        val bundleWidgetId = "222332"
        val headerName = "Product Bundling"
        val productBundle = ProductBundleRecomResponse(
            ProductBundleRecomResponse.TokonowBundleWidget(
                ProductBundleRecomResponse.TokonowBundleWidget.Data(
                    widgetData = listOf(
                        ProductBundleRecomResponse.TokonowBundleWidget.Data.WidgetData(
                            bundleDetails = listOf(
                                ProductBundleRecomResponse.TokonowBundleWidget.Data.WidgetData.BundleDetail(
                                    bundleID = "12231212"
                                ),
                                ProductBundleRecomResponse.TokonowBundleWidget.Data.WidgetData.BundleDetail(
                                    bundleID = "12231233"
                                )
                            )
                        )
                    )
                )
            )
        )
        onGetHomeLayoutData_thenReturn(
            layoutResponse = BundleWidgetDataFactory.createBundleResponseList(
                id = bundleWidgetId,
                headerName = headerName
            )
        )
        onGetProductBundleRecom_thenReturn(productBundle)

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = LocalCacheModel()
        )

        // prepare model for expectedResult
        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                BundleWidgetDataFactory.createBundleUiModel(
                    id = bundleWidgetId,
                    bundleIds = BundleMapper.mapToProductBundleListItemUiModel(
                        widgetData = productBundle.tokonowBundleWidget.data.widgetData
                    ),
                    title = headerName
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetTickerUseCaseCalled()
        verifyGetProductBundleRecomUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when fetching bundle then receiving success result with empty bundle id list, expected result should return homeLayout without bundle widget`() {
        // create dummy data
        val productBundle = ProductBundleRecomResponse(ProductBundleRecomResponse.TokonowBundleWidget())
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetProductBundleRecom_thenReturn(productBundle)

        // fetch homeLayout
        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf()
        )
        viewModel.getLayoutComponentData(
            localCacheModel = LocalCacheModel()
        )

        // prepare model for expectedResult
        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetTickerUseCaseCalled()
        verifyGetProductBundleRecomUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }

    @Test
    fun `when fetching bundle but failed then expected result should return homeLayout without bundle widget`() {
        // create dummy data
        onGetHomeLayoutData_thenReturn(createHomeLayoutList())
        onGetProductBundleRecom_thenReturn(Exception())

        // fetch homeLayout
        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

        // prepare model for expectedResult
        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                createDynamicLegoBannerDataModel(
                    "34923",
                    "",
                    "Lego Banner"
                ),
                createCategoryGridDataModel(
                    "11111",
                    "Category Tokonow",
                    null,
                    TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    "2222",
                    "",
                    "Banner Tokonow"
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetTickerUseCaseCalled()
        verifyGetProductBundleRecomUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)
    }
}
