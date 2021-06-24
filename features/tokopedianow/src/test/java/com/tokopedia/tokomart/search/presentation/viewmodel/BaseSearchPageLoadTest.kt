package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

open class BaseSearchPageLoadTest: SearchTestFixtures() {

    protected val requestParamsSlot = slot<RequestParams>()
    protected val requestParams by lazy { requestParamsSlot.captured }

    protected fun createExpectedMandatoryTokonowQueryParams(page: Int) =
            createPaginationQueryParams(page) + createMandatoryTokonowQueryParams()

    protected fun createPaginationQueryParams(page: Int) = mapOf(
        SearchApiConst.PAGE to page.toString(),
        SearchApiConst.USE_PAGE to true.toString(),
    )

    protected fun `Then assert request params map`(
            mandatoryParams: Map<String, String>
    ) {
        val queryParams = requestParams.parameters[TOKONOW_QUERY_PARAMS] as Map<String, Any>
        val actualRequestParamsMap = queryParams.map { it.key to it.value.toString() }.toMap()

        `Then assert request params map contains query param map`(actualRequestParamsMap)
        `Then assert request params map contains mandatory params`(mandatoryParams, actualRequestParamsMap)
    }

    private fun `Then assert request params map contains query param map`(
            actualRequestParamsMap: Map<String, String>
    ) {
        val expectedQueryParamMap = defaultQueryParamMap

        expectedQueryParamMap.forEach { (key, value) ->
            assertThat(actualRequestParamsMap[key], shouldBe(value))
        }
    }

    private fun `Then assert request params map contains mandatory params`(
            mandatoryParams: Map<String, String>,
            actualRequestParamsMap: Map<String, String>
    ) {
        mandatoryParams.forEach { (key, value) ->
            assertThat(actualRequestParamsMap[key], shouldBe(value))
        }
    }

    protected fun `Then assert visitable list does not end with loading more model`(
            visitableList: List<Visitable<*>>
    ) {
        assertThat(visitableList.last(), not(instanceOf(LoadingMoreModel::class.java)))
    }

    protected fun `Then assert has next page value`(expectedHasNextPage: Boolean) {
        assertThat(searchViewModel.hasNextPageLiveData.value!!, shouldBe(expectedHasNextPage))
    }

    protected fun `Then assert visitable list end with loading more model`(visitableList: List<Visitable<*>>) {
        assertThat(visitableList.last(), instanceOf(LoadingMoreModel::class.java))
    }
}