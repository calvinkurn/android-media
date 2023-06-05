package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase.Companion.CATEGORY_PAGE
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import io.mockk.coEvery
import org.junit.Test

class CategoryDetailTest: TokoNowCategoryMainViewModelTestFixture() {

    @Test
    fun a() {
        val categoryDetailResponse = "category/category-detail.json".jsonToObject<CategoryDetailResponse>()
        val targetedTickerResponse = "category/targeted-ticker.json".jsonToObject<GetTargetedTickerResponse>()

        val categoryProductResponse1 = "category/ace-search-product-1-aneka-sayuran.json".jsonToObject<AceSearchProductModel>()
        val categoryProductResponse2 = "category/ace-search-product-2-bawang.json".jsonToObject<AceSearchProductModel>()
        val categoryProductResponse3 = "category/ace-search-product-3-buah-buahan.json".jsonToObject<AceSearchProductModel>()
        val categoryProductResponse4 = "category/ace-search-product-4-jamur.json".jsonToObject<AceSearchProductModel>()
        val categoryProductResponse5 = "category/ace-search-product-5-paket-sayur.json".jsonToObject<AceSearchProductModel>()

        val categoryMaps = mapOf(
            "4859" to categoryProductResponse1,
            "4826" to categoryProductResponse2,
            "4860" to categoryProductResponse3,
            "4863" to categoryProductResponse4,
            "4865" to categoryProductResponse5
        )

        coEvery {
            getCategoryDetailUseCase.execute(
                categoryIdL1 = categoryIdL1,
                warehouseId = warehouseId
            )
        } returns categoryDetailResponse

        coEvery {
            getTargetedTickerUseCase.execute(
                warehouseId = warehouseId,
                page = CATEGORY_PAGE
            )
        } returns targetedTickerResponse

        categoryMaps.forEach { (categoryIdL2, categoryProductResponse) ->
            coEvery {
                getCategoryProductUseCase.execute(
                    any(),
                    any(),
                    categoryIdL2
                )
            } returns categoryProductResponse
        }

        viewModel.getCategoryHeader(navToolbarHeight = navToolbarHeight)

        viewModel.getFirstPage()



    }
}
