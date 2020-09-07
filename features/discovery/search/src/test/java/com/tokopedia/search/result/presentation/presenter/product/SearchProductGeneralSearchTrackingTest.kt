package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val commonResponse = "searchproduct/common-response.json"
private const val generalSearchTrackingDirectory = "searchproduct/generalsearchtracking/"
private const val multipleCategories = "${generalSearchTrackingDirectory}multiple-categories.json"
private const val noResult = "${generalSearchTrackingDirectory}no-result.json"
private const val responseCode3RelatedSearch = "${generalSearchTrackingDirectory}response-code-3-related-search.json"
private const val responseCode4RelatedSearch = "${generalSearchTrackingDirectory}response-code-4-related-search.json"
private const val responseCode4NoRelatedKeyword = "${generalSearchTrackingDirectory}response-code-4-no-related-keyword.json"
private const val responseCode5RelatedSearch = "${generalSearchTrackingDirectory}response-code-5-related-search.json"
private const val responseCode6RelatedSearch = "${generalSearchTrackingDirectory}response-code-6-related-search.json"
private const val responseCode7SuggestedSearch = "${generalSearchTrackingDirectory}response-code-7-suggested-search.json"
private const val responseCode9 = "${generalSearchTrackingDirectory}response-code-9.json"

internal class SearchProductGeneralSearchTrackingTest: ProductListPresenterTestFixtures() {

    private val keyword = "samsung"

    private fun `Test General Search Tracking`(searchProductModel: SearchProductModel, previousKeyword: String, expectedGeneralSearchTrackingModel: GeneralSearchTrackingModel) {
        `Given Search Product Setup`(searchProductModel, previousKeyword)

        `When View is created`()

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    private fun `Given Search Product Setup`(searchProductModel: SearchProductModel, previousKeyword: String) {
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given View with previous keyword`(previousKeyword)
        `Given View reload data immediately calls load data`()
        `Given View getQueryKey will return the keyword`()
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `Given View is first active tab`() {
        every { productListView.isFirstActiveTab }.returns(true)
    }

    private fun `Given View with previous keyword`(previousKeyword: String) {
        every { productListView.previousKeyword }.returns(previousKeyword)
    }

    private fun `Given View reload data immediately calls load data`() {
        every { productListView.reloadData() }.answers {
            val searchParameter : Map<String, Any> = mutableMapOf<String, Any>().also {
                it[SearchApiConst.Q] = keyword
                it[SearchApiConst.START] = "0"
                it[SearchApiConst.UNIQUE_ID] = "unique_id"
                it[SearchApiConst.USER_ID] = productListPresenter.userId
            }

            productListPresenter.loadData(searchParameter)
        }
    }

    private fun `Given View getQueryKey will return the keyword`() {
        every { productListView.queryKey } returns keyword
    }

    private fun `When View is created`() {
        productListPresenter.onViewCreated()
    }

    private fun `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel: GeneralSearchTrackingModel) {
        val generalSearchTrackingModelSlot = slot<GeneralSearchTrackingModel>()

        verify {
            productListView.sendTrackingGTMEventSearchAttempt(capture(generalSearchTrackingModelSlot))
        }

        val actualGeneralSearchTrackingModel = generalSearchTrackingModelSlot.captured

        actualGeneralSearchTrackingModel shouldBe expectedGeneralSearchTrackingModel
    }

    @Test
    fun `General search tracking with result found`() {
        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val previousKeyword = ""
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "65",
                categoryNameMapping = "Handphone & Tablet",
                relatedKeyword = "none - none"
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with result found and multiple categories`() {
        val searchProductModel = multipleCategories.jsonToObject<SearchProductModel>()
        val previousKeyword = ""
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758,65",
                categoryNameMapping = "Fashion Pria,Handphone & Tablet,Fashion Wanita",
                relatedKeyword = "none - none"
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with no result found`() {
        val searchProductModel = noResult.jsonToObject<SearchProductModel>()
        val previousKeyword = ""
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = false,
                categoryIdMapping = "",
                categoryNameMapping = "",
                relatedKeyword = "none - none"
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword`() {
        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel =  GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "65",
                categoryNameMapping = "Handphone & Tablet",
                relatedKeyword = "$previousKeyword - none"
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword and has related search with response code 3`() {
        val searchProductModel = responseCode3RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.data.related.relatedKeyword}"
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword, has related search and other related with response code 4`() {
        val searchProductModel = responseCode4RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - " +
                        "${searchProductModel.searchProduct.data.related.relatedKeyword}," +
                        searchProductModel.searchProduct.data.related.otherRelatedList.joinToString(",") { it.keyword }
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword, response code 4, without related keyword`() {
        val searchProductModel = responseCode4NoRelatedKeyword.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - " +
                        searchProductModel.searchProduct.data.related.otherRelatedList.joinToString(",") { it.keyword }
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword, has related search and other related with response code 5`() {
        val searchProductModel = responseCode5RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - " +
                        "${searchProductModel.searchProduct.data.related.relatedKeyword}," +
                        searchProductModel.searchProduct.data.related.otherRelatedList.joinToString(",") { it.keyword }
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword and has related search with response code 6`() {
        val searchProductModel = responseCode6RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.data.related.relatedKeyword}"
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }

    @Test
    fun `General search tracking with previous keyword and has suggestion with response code 7`() {
        val searchProductModel = responseCode7SuggestedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
                eventLabel = String.format(
                        SearchEventTracking.Label.KEYWORD_TREATMENT_RESPONSE,
                        keyword,
                        searchProductModel.searchProduct.header.keywordProcess,
                        searchProductModel.searchProduct.header.responseCode
                ),
                isResultFound = true,
                categoryIdMapping = "1759,1758",
                categoryNameMapping = "Fashion Pria,Fashion Wanita",
                relatedKeyword = "$previousKeyword - ${searchProductModel.searchProduct.data.suggestion.suggestion}"
        )

        `Test General Search Tracking`(searchProductModel, previousKeyword, expectedGeneralSearchTrackingModel)
    }
}