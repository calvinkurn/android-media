package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
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

private const val commonResponse = "searchproduct/common-response-reimagine.json"
private const val generalSearchTrackingDirectory = "searchproduct/generalsearchtracking/reimagine/"
private const val multipleCategories = "${generalSearchTrackingDirectory}multiple-categories.json"
private const val noResult = "${generalSearchTrackingDirectory}no-result.json"
private const val responseCode3RelatedSearch =
    "${generalSearchTrackingDirectory}response-code-3-related-search.json"
private const val responseCode4RelatedSearch =
    "${generalSearchTrackingDirectory}response-code-4-related-search.json"
private const val responseCode4NoRelatedKeyword =
    "${generalSearchTrackingDirectory}response-code-4-no-related-keyword.json"
private const val responseCode5RelatedSearch =
    "${generalSearchTrackingDirectory}response-code-5-related-search.json"
private const val responseCode6RelatedSearch =
    "${generalSearchTrackingDirectory}response-code-6-related-search.json"
private const val responseCode7SuggestedSearch =
    "${generalSearchTrackingDirectory}response-code-7-suggested-search.json"
private const val responseCode8BannedProducts =
    "${generalSearchTrackingDirectory}response-code-8-banned-products.json"
private const val withRedirection = "${generalSearchTrackingDirectory}with-redirection.json"
private const val withGlobalNav = "${generalSearchTrackingDirectory}with-global-nav.json"
private const val withGlobalNavEmptySource =
    "${generalSearchTrackingDirectory}with-global-nav-empty-source.json"
private const val responseCode15RequestTimeoutProducts =
    "${generalSearchTrackingDirectory}response-code-15-timeout-product.json"
private const val REQUEST_TIMEOUT_RESPONSE_CODE = "15"

internal class SearchProductGeneralSearchTrackingReimagineTest : ProductListPresenterTestFixtures() {

    private val keyword = "samsung"
    private val userId = "12345"
    private val pageSource = SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
    private var searchParameter: Map<String, Any> = mutableMapOf<String, Any>().also {
        it[SearchApiConst.Q] = keyword
        it[SearchApiConst.START] = "0"
        it[SearchApiConst.UNIQUE_ID] = "unique_id"
        it[SearchApiConst.USER_ID] = userId
    }

    private fun `Test General Search Tracking`(
        searchProductModel: SearchProductModel,
        expectedGeneralSearchTrackingModel: GeneralSearchTrackingModel,
        previousKeyword: String = "",
    ) {
        `Given Search Product Setup`(searchProductModel, previousKeyword)

        `When View is created`()

        `Then verify general search tracking model is correct`(expectedGeneralSearchTrackingModel)
    }

    private fun `Given Search Product Setup`(
        searchProductModel: SearchProductModel,
        previousKeyword: String = "",
    ) {
        `Given search reimagine rollence product card will return non control variant`()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given View is first active tab`()
        `Given View with previous keyword`(previousKeyword)
        `Given View reload data immediately calls load data`()
        `Given View getQueryKey will return the keyword`()
        `Given user is logged in`()
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
            productListPresenter.loadData(searchParameter)
        }
    }

    private fun `Given View getQueryKey will return the keyword`() {
        every { productListView.queryKey } returns searchParameter[SearchApiConst.Q].toString()
    }

    private fun `Given user is logged in`() {
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns userId
    }

    private fun `When View is created`() {
        productListPresenter.onViewCreated()
    }

    private fun `Then verify general search tracking model is correct`(
        expectedGeneralSearchTrackingModel: GeneralSearchTrackingModel
    ) {
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
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General search tracking with result found and multiple categories`() {
        val searchProductModel = multipleCategories.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "62,63,65,1759",
            categoryNameMapping = "Olahraga,Otomotif,Handphone & Tablet,Fashion Pria",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General search tracking with no result found`() {
        val searchProductModel = noResult.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = false.toString(),
            categoryIdMapping = "",
            categoryNameMapping = "",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General search tracking with previous keyword`() {
        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "$previousKeyword - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
            previousKeyword
        )
    }

    @Test
    fun `General search tracking with previous keyword and has related search with response code 3`() {
        val searchProductModel = responseCode3RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "1758,1759",
            categoryNameMapping = "Fashion Wanita,Fashion Pria",
            relatedKeyword = "$previousKeyword - ${searchProductModel.searchProductV5.data.related.relatedKeyword}",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
            previousKeyword
        )
    }

    @Test
    fun `General search tracking with previous keyword, has related search and other related with response code 4`() {
        val searchProductModel = responseCode4RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "1758,1759",
            categoryNameMapping = "Fashion Wanita,Fashion Pria",
            relatedKeyword = "$previousKeyword - " +
                "${searchProductModel.searchProductV5.data.related.relatedKeyword}," +
                searchProductModel.searchProductV5.data.related.otherRelatedList.joinToString(",") { it.keyword },
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
            previousKeyword
        )
    }

    @Test
    fun `General search tracking with previous keyword, response code 4, without related keyword`() {
        val searchProductModel = responseCode4NoRelatedKeyword.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "1758,1759",
            categoryNameMapping = "Fashion Wanita,Fashion Pria",
            relatedKeyword = "$previousKeyword - " +
                searchProductModel.searchProductV5.data.related.otherRelatedList.joinToString(",") { it.keyword },
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
            previousKeyword
        )
    }

    @Test
    fun `General search tracking with previous keyword, has related search and other related with response code 5`() {
        val searchProductModel = responseCode5RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "1758,1759",
            categoryNameMapping = "Fashion Wanita,Fashion Pria",
            relatedKeyword = "$previousKeyword - " +
                "${searchProductModel.searchProductV5.data.related.relatedKeyword}," +
                searchProductModel.searchProductV5.data.related.otherRelatedList.joinToString(",") { it.keyword },
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
            previousKeyword
        )
    }

    @Test
    fun `General search tracking with previous keyword and has related search with response code 6`() {
        val searchProductModel = responseCode6RelatedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "1758,1759",
            categoryNameMapping = "Fashion Wanita,Fashion Pria",
            relatedKeyword = "$previousKeyword - ${searchProductModel.searchProductV5.data.related.relatedKeyword}",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
            previousKeyword
        )
    }

    @Test
    fun `General search tracking with previous keyword and has suggestion with response code 7`() {
        val searchProductModel = responseCode7SuggestedSearch.jsonToObject<SearchProductModel>()
        val previousKeyword = "xiaomi"
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "1758,1759",
            categoryNameMapping = "Fashion Wanita,Fashion Pria",
            relatedKeyword = "$previousKeyword - ${searchProductModel.searchProductV5.data.suggestion.suggestion}",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
            previousKeyword
        )
    }

    @Test
    fun `General search tracking with response code 8`() {
        val searchProductModel = responseCode8BannedProducts.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                "0",
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "1758,1759",
            categoryNameMapping = "Fashion Wanita,Fashion Pria",
            relatedKeyword = "${SearchEventTracking.NONE} - ${SearchEventTracking.NONE}",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General search tracking with response code 15`() {
        val searchProductModel = responseCode15RequestTimeoutProducts.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                REQUEST_TIMEOUT_RESPONSE_CODE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = false.toString(),
            categoryIdMapping = "",
            categoryNameMapping = "",
            relatedKeyword = "${SearchEventTracking.NONE} - ${SearchEventTracking.NONE}",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking With Redirection`() {
        val searchProductModel = withRedirection.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking With Global Nav`() {
        val searchProductModel = withGlobalNav.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                searchProductModel.globalSearchNavigation.data.source,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking With Global Nav Empty Source`() {
        val searchProductModel = withGlobalNavEmptySource.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.OTHER,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking with navsource`() {
        val navSource = "campaign"

        searchParameter = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = keyword
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
            it[SearchApiConst.NAVSOURCE] = navSource
        }

        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                navSource,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking with page title`() {
        val pageTitle = "Waktu Indonesia Belanja"

        searchParameter = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = keyword
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
            it[SearchApiConst.SRP_PAGE_TITLE] = pageTitle
        }

        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = "${SearchEventTracking.Category.EVENT_TOP_NAV} - $pageTitle",
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                pageTitle,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking Local Search`() {
        val pageTitle = "Waktu Indonesia Belanja"
        val navSource = "campaign"

        searchParameter = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = keyword
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
            it[SearchApiConst.SRP_PAGE_TITLE] = pageTitle
            it[SearchApiConst.NAVSOURCE] = navSource
            it[SearchApiConst.SRP_PAGE_ID] = "1234"
        }

        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = "${SearchEventTracking.Category.EVENT_TOP_NAV} - $pageTitle",
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                navSource,
                pageTitle,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = "${searchParameter[SearchApiConst.SRP_PAGE_TITLE]}." +
                "${searchParameter[SearchApiConst.NAVSOURCE]}." +
                "local_search." +
                "${searchParameter[SearchApiConst.SRP_PAGE_ID]}",
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking Search Ref`() {
        val searchRef = "search ref example"

        searchParameter = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = keyword
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
            it[SearchApiConst.SEARCH_REF] = searchRef
        }

        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = searchRef,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = "",
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking External Reference`() {
        val externalReference = "1234567"

        searchParameter = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = keyword
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
            it[SearchApiConst.SRP_EXT_REF] = externalReference
        }

        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = externalReference,
        )

        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }

    @Test
    fun `General Search Tracking Reimagine`() {
        val externalReference = "1234567"

        searchParameter = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = keyword
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
            it[SearchApiConst.SRP_EXT_REF] = externalReference
        }

        val searchProductModel = commonResponse.jsonToObject<SearchProductModel>()
        val expectedGeneralSearchTrackingModel = GeneralSearchTrackingModel(
            eventCategory = SearchEventTracking.Category.EVENT_TOP_NAV,
            eventLabel = String.format(
                SearchEventTracking.Label.GENERAL_SEARCH_EVENT_LABEL,
                keyword,
                searchProductModel.searchProductV5.header.keywordProcess,
                searchProductModel.searchProductV5.header.responseCode,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                SearchEventTracking.NONE,
                searchProductModel.searchProductV5.header.totalData,
            ),
            userId = userId,
            isResultFound = true.toString(),
            categoryIdMapping = "65",
            categoryNameMapping = "Handphone & Tablet",
            relatedKeyword = "none - none",
            pageSource = pageSource,
            searchFilter = searchProductModel.searchProductV5.header.backendFilters,
            componentId = searchProductModel.searchProductV5.header.componentID,
            externalReference = externalReference,
        )

        `Given search reimagine rollence product card will return non control variant`()
        `Test General Search Tracking`(
            searchProductModel,
            expectedGeneralSearchTrackingModel,
        )
    }
}
