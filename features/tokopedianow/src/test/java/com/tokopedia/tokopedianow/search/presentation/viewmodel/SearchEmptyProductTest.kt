package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.EmptyProductTestHelper
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.usecase.RequestParams
import io.mockk.verify
import org.junit.Test

class SearchEmptyProductTest: SearchTestFixtures(), EmptyProductTestHelper.Callback {

    private lateinit var emptyProductTestHelper: EmptyProductTestHelper

    override fun setUp() {
        super.setUp()
        emptyProductTestHelper = EmptyProductTestHelper(tokoNowSearchViewModel, this)
    }

    override fun `Given first page product list is empty`(feedbackFieldToggle:Boolean) {
        val emptyProductModel = "search/emptyproduct/empty-product.json".jsonToObject<SearchModel>()
        if(feedbackFieldToggle)
            emptyProductModel.feedbackFieldToggle.tokonowFeedbackFieldToggle.data.isActive = true
        `Given get search first page use case will be successful`(emptyProductModel)
    }

    override fun `Given first page product list will be successful`() {
        val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()
        `Given get search first page use case will be successful`(searchModel)
    }

    override fun `Then verify first page API is called`(
            count: Int,
            requestParamsSlot: MutableList<RequestParams>,
    ) {
        verify (exactly = count) {
            getSearchFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        }
    }

    override fun `Then assert empty result visitable list`(visitableList: List<Visitable<*>>) {

    }

    @Test
    fun `empty product list should show empty product view`() {
        emptyProductTestHelper.`empty product list should show empty product view`()
    }

    @Test
    fun `empty product list because of filter should show filter list`() {
        emptyProductTestHelper.`empty product list because of filter should show filter list`()
    }

    @Test
    fun `empty state remove filter`() {
        emptyProductTestHelper.`empty state remove filter`()
    }

    @Test
    fun `empty state remove filter with exclude_ prefix`() {
        emptyProductTestHelper.`empty state remove filter with exclude_ prefix`()
    }

    @Test
    fun `empty product list with feedback widget active should show feedback view`(){
        emptyProductTestHelper.`empty product list with feedback widget active should show feedback view`()
    }
}
