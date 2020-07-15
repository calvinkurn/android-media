package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.presenter.product.testinstance.*
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class SearchProductGeneralSearchTrackingTest: ProductListPresenterTestFixtures() {

    private val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

    @Test
    fun `General search tracking with result found`() {
        val searchProductModel = searchProductModelCommon

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given Previous keyword from view is empty`()
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify view send general search tracking`()

        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        searchProductModel.searchProduct.query,
                        searchProductModel.searchProduct.keywordProcess,
                        searchProductModel.searchProduct.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "65",
                categoryNameMapping = "Handphone & Tablet",
                relatedKeyword = "none - none"
        )

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given View is first active tab`() {
        every { productListView.isFirstActiveTab }.returns(true)
    }

    private fun `Given Previous keyword from view is empty`() {
        every { productListView.previousKeyword }.returns("")
    }

    private fun `Given View reload data immediately calls load data`() {
        every { productListView.reloadData() }.answers {
            val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                it[SearchApiConst.Q] = "samsung"
                it[SearchApiConst.START] = "0"
                it[SearchApiConst.UNIQUE_ID] = "unique_id"
                it[SearchApiConst.USER_ID] = productListPresenter.userId
            }

            productListPresenter.loadData(searchParameter)
        }
    }

    private fun `When View is created`() {
        productListPresenter.onViewCreated()
    }

    private fun `Then verify view send general search tracking`() {
        verify {
            productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
        }
    }

    private fun `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel: GeneralSearchTrackingModel) {
        val actualGeneralSearchTrackingModel = generalSearchTrackingModelSlot.captured

        actualGeneralSearchTrackingModel shouldBe expectedGeneralSearchTrackingModel
    }

    @Test
    fun `General search tracking with result found and multiple categories`() {
        val searchProductModel = searchProductModelMultipleCategories

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given Previous keyword from view is empty`()
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify view send general search tracking`()

        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        searchProductModel.searchProduct.query,
                        searchProductModel.searchProduct.keywordProcess,
                        searchProductModel.searchProduct.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758,65",
                categoryNameMapping = "Fashion Pria,Handphone & Tablet,Fashion Wanita",
                relatedKeyword = "none - none"
        )

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with no result found`() {
        val searchProductModel = searchProductModelNoResult

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given Previous keyword from view is empty`()
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify view send general search tracking`()

        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        searchProductModel.searchProduct.query,
                        searchProductModel.searchProduct.keywordProcess,
                        searchProductModel.searchProduct.responseCode
                ),
                isResultFound = false,
                categoryIdMapping = "",
                categoryNameMapping = "",
                relatedKeyword = "none - none"
        )

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword`() {
        val searchProductModel = searchProductModelCommon
        val previousKeyword = "xiaomi"

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given View has previous keyword`(previousKeyword)
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify view send general search tracking`()

        val expectedGeneralSearchTrackingModel =  GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        searchProductModel.searchProduct.query,
                        searchProductModel.searchProduct.keywordProcess,
                        searchProductModel.searchProduct.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "65",
                categoryNameMapping = "Handphone & Tablet",
                relatedKeyword = "$previousKeyword - none"
        )

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    private fun `Given View has previous keyword`(previousKeyword: String) {
        every { productListView.previousKeyword }.returns(previousKeyword)
    }

    @Test
    fun `General search tracking with previous keyword and has related search with response code 3`() {
        val searchProductModel = searchProductModelRelatedSearch_3
        val previousKeyword = "xiaomi"

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given View has previous keyword`(previousKeyword)
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify view send general search tracking`()

        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        searchProductModel.searchProduct.query,
                        searchProductModel.searchProduct.keywordProcess,
                        searchProductModel.searchProduct.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.related.relatedKeyword}"
        )

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword and has related search with response code 6`() {
        val searchProductModel = searchProductModelRelatedSearch_6
        val previousKeyword = "xiaomi"

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given View has previous keyword`(previousKeyword)
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify view send general search tracking`()

        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        searchProductModel.searchProduct.query,
                        searchProductModel.searchProduct.keywordProcess,
                        searchProductModel.searchProduct.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.related.relatedKeyword}"
        )

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword and has suggestion with response code 7`() {
        val searchProductModel = searchProductModelSuggestedSearch
        val previousKeyword = "xiaomi"

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given View has previous keyword`(previousKeyword)
        `Given View reload data immediately calls load data`()

        `When View is created`()

        `Then verify view send general search tracking`()

        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        searchProductModel.searchProduct.query,
                        searchProductModel.searchProduct.keywordProcess,
                        searchProductModel.searchProduct.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.suggestion.suggestion}"
        )

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }
}