package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.model.CategorySharingModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test
import java.util.*

class TokoNowCategoryL2TabViewModelTestShare: TokoNowCategoryL2TabViewModelTestFixture() {

    @Test
    fun `given apply L3 filter when onViewCreated should set category sharing model with categoryIdL3`() {
        onGetProductList(thenReturn = getProductResponse)
        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)

        viewModel.onViewCreated(data)

        val filter = getQuickFilterResponse.data.filter.first()
        val option = filter.options[1]
        val categoryIdL3 = option.value

        val getProductFilterParams = createGetProductQueryParams(categoryIdL1, categoryIdL3)
        val getQuickFilterQueryParams = createRequestQueryParams(
            source = "quick_filter_tokonow_directory",
            srpPageId = categoryIdL1,
            sc = categoryIdL3
        )
        val getCategoryFilterQueryParams = createGetCategoryFilterQueryParams(
            srpPageId = categoryIdL1,
            sc = categoryIdL3
        )

        onGetQuickFilter(
            withQueryParams = getQuickFilterQueryParams,
            thenReturn = getQuickFilterResponse
        )

        onGetCategoryFilter(
            withQueryParams = getCategoryFilterQueryParams,
            thenReturn = getCategoryFilterResponse
        )

        onGetProductList(
            withQueryParams = getProductFilterParams,
            thenReturn = getProductResponse
        )

        viewModel.applyQuickFilter(filter, option)

        val expectedSharingModel = CategorySharingModel(
            categoryIdLvl2 = categoryIdL2,
            categoryIdLvl3 = categoryIdL3,
            title = "Buah",
            deeplinkParam = "category/l2/$categoryIdL1/$categoryIdL2?sc=$categoryIdL3",
            url = "${data.categoryDetail.data.url}?exclude_sc=$categoryIdL2&sc=$categoryIdL3",
            utmCampaignList = listOf(String.format(Locale.getDefault(), "cat3", 3), categoryIdL3)
        )

        viewModel.shareLiveData
            .verifyValueEquals(expectedSharingModel)
    }
}
