package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.assertProductFeedbackWidget
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
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
}
