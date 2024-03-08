package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationPageSource
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationType
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel.BrandWidgetState
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.AddressMapper
import com.tokopedia.tokopedianow.common.util.BrandWidgetMapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class CategoryBrandWidgetTest : TokoNowCategoryViewModelTestFixture() {

    @Test
    fun `given get brand widget success when getFirstPage should add brand widget to visitableList`() {
        setupAddressAndUserData()

        val getBrandWidgetResponse = getBrandWidgetResponse.response
        val warehouses = AddressMapper.mapToWarehouses(addressData)

        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onGetAnnotationWidget_thenReturn(getBrandWidgetResponse)

        viewModel.onViewCreated()

        val brandWidgetUiModel = BrandWidgetMapper
            .mapBrandWidget(getBrandWidgetResponse)

        val expectedVisitableList = createVisitableList()
        expectedVisitableList.add(brandWidgetUiModel)

        verifyGetAnnotationWidgetUseCaseCalled(
            categoryId = categoryIdL1,
            warehouses = warehouses,
            annotationType = AnnotationType.BRAND,
            pageSource = AnnotationPageSource.CLP_L1
        )

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given get brand widget return status 0 when getFirstPage should remove brand widget from visitableList`() {
        val getBrandWidgetResponse = getBrandWidgetResponse
            .response.copy(status = 0)
        setupAddressAndUserData()

        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onGetAnnotationWidget_thenReturn(getBrandWidgetResponse)

        viewModel.onViewCreated()

        val expectedVisitableList = createVisitableList()

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given get brand widget error when getFirstPage should add brand widget with error state to visitableList`() {
        val error = NullPointerException()
        setupAddressAndUserData()

        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onGetAnnotationWidget_thenReturn(error)

        viewModel.onViewCreated()

        val expectedVisitableList = createVisitableList()
        expectedVisitableList.add(
            BrandWidgetUiModel(
                id = "brand_widget",
                header = TokoNowDynamicHeaderUiModel(),
                items = emptyList(),
                state = BrandWidgetState.ERROR
            )
        )

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }
}
