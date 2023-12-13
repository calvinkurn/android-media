package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.common.util.BrandWidgetMapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class CategoryBrandWidgetTest : TokoNowCategoryViewModelTestFixture() {

    @Test
    fun `given get brand widget success when getFirstPage should add brand widget to visitableList`() {
        val getBrandWidgetResponse = getBrandWidgetResponse.response
        setupAddressAndUserData()

        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onGetAnnotationWidget_thenReturn(getBrandWidgetResponse)

        viewModel.onViewCreated()

        val brandWidgetUiModel = BrandWidgetMapper
            .mapBrandWidget(getBrandWidgetResponse)

        val expectedVisitableList = createVisitableList()
        expectedVisitableList.add(brandWidgetUiModel)

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
    fun `given get brand widget error when getFirstPage should remove brand widget from visitableList`() {
        val error = NullPointerException()
        setupAddressAndUserData()

        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onGetAnnotationWidget_thenReturn(error)

        viewModel.onViewCreated()

        val expectedVisitableList = createVisitableList()

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }
}
