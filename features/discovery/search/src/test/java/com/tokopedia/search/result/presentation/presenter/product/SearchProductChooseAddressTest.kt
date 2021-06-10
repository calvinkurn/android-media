package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_PARAMS
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_EXP_TOP_NAV
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_OLD
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldBeInstanceOf
import com.tokopedia.search.shouldNotContain
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber

internal class SearchProductChooseAddressTest: ProductListPresenterTestFixtures() {

    private val searchParameter = mapOf(SearchApiConst.Q to "samsung")
    private val requestParamsSlot = slot<RequestParams>()
    private val requestParams by lazy { requestParamsSlot.captured }
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }
    private val dummyChooseAddressData = LocalCacheModel(
            address_id = "123",
            city_id = "45",
            district_id = "123",
            lat = "10.2131",
            long = "12.01324",
            postal_code = "12345",
    )

    @Test
    fun `Test Show choose address widget in first page`() {
        `Setup choose address`(dummyChooseAddressData)
        setUp()

        `Given search product API will return data`()
        `Given visitable list will be captured`()

        `When load data`()

        `Then verify parameters contains choose address data`(
                requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, String>,
                dummyChooseAddressData,
        )
        `Then verify top of visitable list is choose address widget`()
    }

    private fun `Setup choose address`(chooseAddressModel: LocalCacheModel?) {
        `Given choose address is enabled`()
        `Given chosen address data`(chooseAddressModel)
    }

    private fun `Given choose address is enabled`() {
        every { productListView.isChooseAddressWidgetEnabled } returns true
    }

    private fun `Given chosen address data`(chooseAddressModel: LocalCacheModel?) {
        every { productListView.chooseAddressData } returns chooseAddressModel
    }

    private fun `Given search product API will return data`() {
        every {
            searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductFirstPageJSON.jsonToObject())
        }
    }

    private fun `Given visitable list will be captured`() {
        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs
    }

    private fun `When load data`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify parameters contains choose address data`(
            parameters: Map<String, String>, expectedChooseAddressData: LocalCacheModel
    ) {
        parameters[SearchApiConst.USER_LAT] shouldBe expectedChooseAddressData.lat
        parameters[SearchApiConst.USER_LONG] shouldBe expectedChooseAddressData.long
        parameters[SearchApiConst.USER_ADDRESS_ID] shouldBe expectedChooseAddressData.address_id
        parameters[SearchApiConst.USER_CITY_ID] shouldBe expectedChooseAddressData.city_id
        parameters[SearchApiConst.USER_DISTRICT_ID] shouldBe expectedChooseAddressData.district_id
        parameters[SearchApiConst.USER_POST_CODE] shouldBe expectedChooseAddressData.postal_code
    }

    private fun `Then verify top of visitable list is choose address widget`() {
        visitableList.first().shouldBeInstanceOf<ChooseAddressDataView>()
    }

    @Test
    fun `Test choose address data is null`() {
        `Setup choose address`(null)
        setUp()

        `Given search product API will return data`()
        `Given visitable list will be captured`()

        `When load data`()

        `Then verify choose address data is not sent`(requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, String>)
    }

    private fun `Then verify choose address data is not sent`(parameters: Map<String, String>) {
        parameters.shouldNotContain(SearchApiConst.USER_LAT)
        parameters.shouldNotContain(SearchApiConst.USER_LONG)
        parameters.shouldNotContain(SearchApiConst.USER_ADDRESS_ID)
        parameters.shouldNotContain(SearchApiConst.USER_CITY_ID)
        parameters.shouldNotContain(SearchApiConst.USER_DISTRICT_ID)
        parameters.shouldNotContain(SearchApiConst.USER_POST_CODE)
    }

    @Test
    fun `Test choose address data with empty values`() {
        `Setup choose address`(LocalCacheModel())
        setUp()

        `Given search product API will return data`()

        `When load data`()

        `Then verify choose address data is not sent`(requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, String>)
    }

    @Test
    fun `Test choose address data with navigation revamp enabled`() {
        `Setup choose address`(dummyChooseAddressData)
        `Given navigation revamp is enabled`()
        setUp()

        `Given search product API will return data`()
        `Given visitable list will be captured`()

        `When load data`()

        `Then verify top of visitable list is choose address widget`()
        `Then verify visitable list does not contain search product count`()
    }

    private fun `Then verify visitable list does not contain search product count`() {
        visitableList.any { it is SearchProductCountDataView } shouldBe false
    }

    private fun `Given navigation revamp is enabled`() {
        every {
            productListView.abTestRemoteConfig?.getString(NAVIGATION_EXP_TOP_NAV, NAVIGATION_VARIANT_OLD)
        } answers { AbTestPlatform.NAVIGATION_VARIANT_REVAMP }
    }

    @Test
    fun `Test choose address data in filter`() {
        `Setup choose address`(dummyChooseAddressData)

        setUp()

        `Given search product API will return data`()
        `Given dynamic filter API request params will be captured`()
        `Given view already load first page`()

        `When open filter page`()

        `Then verify parameters contains choose address data`(
                requestParams.parameters as Map<String, String>,
                dummyChooseAddressData,
        )
    }

    private fun `Given dynamic filter API request params will be captured`() {
        every {
            getDynamicFilterUseCase.execute(capture(requestParamsSlot), any())
        } just runs
    }

    private fun `Given view already load first page`() {
        productListPresenter.loadData(searchParameter)
    }

    private fun `When open filter page`() {
        productListPresenter.openFilterPage(searchParameter)
    }

    @Test
    fun `Test reload search page after choosing new address`() {
        val newChooseAddressData = LocalCacheModel(
                address_id = "125",
                city_id = "11",
                district_id = "999",
                lat = "19.2167",
                long = "17.01374",
                postal_code = "53241",
        )

        `Setup choose address`(dummyChooseAddressData)

        setUp()

        `Given search product API will return data`()
        `Given view already load first page`()
        `Setup choose address`(newChooseAddressData)
        `Given view reload data behavior`()

        `When chosen address data changes`()

        `Then verify view will fetch new chosen address and reload data`()
        `Then verify parameters contains choose address data`(
                requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, String>,
                newChooseAddressData
        )
    }

    private fun `Given view reload data behavior`() {
        every { productListView.reloadData() } answers {
            productListPresenter.loadData(searchParameter)
        }
    }

    private fun `When chosen address data changes`() {
        productListPresenter.onLocalizingAddressSelected()
    }

    private fun `Then verify view will fetch new chosen address and reload data`() {
        verify {
            productListView.chooseAddressData
            productListView.reloadData()
        }
    }

    @Test
    fun `Test re-fetch filter from API after choosing new address`() {
        val newChooseAddressData = LocalCacheModel(
                address_id = "125",
                city_id = "11",
                district_id = "999",
                lat = "19.2167",
                long = "17.01374",
                postal_code = "53241",
        )

        `Setup choose address`(dummyChooseAddressData)

        setUp()

        `Given search product API will return data`()
        `Given dynamic filter API request params will be captured`()
        `Given view already load first page`()
        `Given filter page already opened and closed`()
        `Given chosen address data has changed`(newChooseAddressData)

        `When open filter page`()

        `Then verify get dynamic filter API is called twice`()
        `Then verify parameters contains choose address data`(
                requestParams.parameters as Map<String, String>, newChooseAddressData
        )
    }

    private fun `Given filter page already opened and closed`() {
        productListPresenter.openFilterPage(searchParameter)
        productListPresenter.onBottomSheetFilterDismissed()
    }

    private fun `Given chosen address data has changed`(newChooseAddressData: LocalCacheModel) {
        `Setup choose address`(newChooseAddressData)
        `Given view reload data behavior`()

        productListPresenter.onLocalizingAddressSelected()
    }

    private fun `Then verify get dynamic filter API is called twice`() {
        verify(exactly = 2) { getDynamicFilterUseCase.execute(any(), any()) }
    }

    @Test
    fun `Test on view resumed when choose address data has changes`() {
        val newChooseAddressData = LocalCacheModel(
                address_id = "125",
                city_id = "11",
                district_id = "999",
                lat = "19.2167",
                long = "17.01374",
                postal_code = "53241",
        )

        `Setup choose address`(dummyChooseAddressData)

        setUp()

        `Given search product API will return data`()
        `Given view already load first page`()
        `Setup choose address`(newChooseAddressData)
        `Given view reload data behavior`()
        `Given choose address data has updated`()

        `When view is resumed`()

        `Then verify check choose address data updated is called`()
        `Then verify view will fetch new chosen address and reload data`()
        `Then verify parameters contains choose address data`(
                requestParams.parameters[SEARCH_PRODUCT_PARAMS] as Map<String, String>,
                newChooseAddressData
        )
    }

    private fun `Given choose address data has updated`() {
        every { productListView.getIsLocalizingAddressHasUpdated(dummyChooseAddressData) } returns true
    }

    private fun `When view is resumed`() {
        productListPresenter.onViewResumed()
    }

    private fun `Then verify check choose address data updated is called`() {
        verify { productListView.getIsLocalizingAddressHasUpdated(dummyChooseAddressData) }
    }

    @Test
    fun `Test on view resumed when choose address data does not have changes`() {
        `Setup choose address`(dummyChooseAddressData)

        setUp()

        `Given search product API will return data`()
        `Given view already load first page`()

        `When view is resumed`()

        `Then verify check choose address data updated is called`()
        `Then verify view does not reload`()
    }

    private fun `Then verify view does not reload`() {
        verify(exactly = 0) {
            productListView.reloadData()
        }
    }
}