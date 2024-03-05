package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.assertNoProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.assertNotProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.assertProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class SearchProductFeedbackTest : SearchTestFixtures() {
    companion object {
        private const val PRODUCT_FEEDBACK_DATA_VIEW_POSITION = 12
    }

    @Test
    fun `test show product feedback widget`() {
        val searchModel = "search/productfeedback/first-page-less-than-6-products.json".jsonToObject<SearchModel>()

        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        visitableList[PRODUCT_FEEDBACK_DATA_VIEW_POSITION].assertProductFeedbackWidget()
    }

    @Test
    fun `test don't show product feedback widget when out of coverage`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

        `Given choose address data`(ChooseAddressConstant.emptyAddress)
        `Given get warehouse API will be successful`(GetStateChosenAddressResponse())
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        assertNoProductFeedbackWidget(visitableList)
    }

    @Test
    fun `test dont show product feedback widget when total products greater than equal to 8`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        searchModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive = true
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        visitableList.assertNotProductFeedbackWidget()
        Assert.assertEquals(tokoNowSearchViewModel.isProductFeedbackLoopVisible(), false)
    }

    @Test
    fun `test dont show product feedback widget when is active response false`() {
        val searchModel = "search/productfeedback/first-page-less-than-6-products.json".jsonToObject<SearchModel>()
        searchModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive = false
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!
        visitableList.assertNotProductFeedbackWidget()
    }

    private fun `Given get warehouse API will be successful`(warehouseResponse: GetStateChosenAddressResponse) {
        coEvery {
            getWarehouseUseCase(SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH)
        } returns warehouseResponse
    }
}
