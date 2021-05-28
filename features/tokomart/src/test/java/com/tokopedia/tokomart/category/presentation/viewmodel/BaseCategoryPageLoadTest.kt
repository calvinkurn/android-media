package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail
import com.tokopedia.tokomart.category.domain.model.TokonowCategoryDetail.NavigationItem
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleItemDataView
import com.tokopedia.tokomart.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokomart.searchcategory.utils.HARDCODED_WAREHOUSE_ID_PLEASE_DELETE
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.tokomart.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

open class BaseCategoryPageLoadTest: CategoryTestFixtures() {

    protected val requestParamsSlot = slot<RequestParams>()
    protected val requestParams by lazy { requestParamsSlot.captured }

    protected fun createExpectedMandatoryTokonowQueryParams(page: Int): Map<String, String> =
            createPaginationQueryParams(page) + createMandatoryTokonowQueryParams()

    protected fun createPaginationQueryParams(page: Int) = mapOf(
            SearchApiConst.PAGE to page.toString(),
            SearchApiConst.USE_PAGE to true.toString(),
    )

    protected fun `Then assert request params map`(
            mandatoryParams: Map<String, String>
    ) {
        val useCaseRequestParams = requestParams.parameters
        val queryParams = useCaseRequestParams[TOKONOW_QUERY_PARAMS] as Map<String, Any>
        val actualRequestParamsMap = queryParams.map { it.key to it.value.toString() }.toMap()

        `Then assert request params map contains query param map`(actualRequestParamsMap)
        `Then assert request params map contains mandatory params`(
                mandatoryParams,
                actualRequestParamsMap,
        )

        assertThat(useCaseRequestParams[CATEGORY_ID], shouldBe(defaultCategoryId.toString()))
        assertThat(useCaseRequestParams[WAREHOUSE_ID], shouldBe(HARDCODED_WAREHOUSE_ID_PLEASE_DELETE))
    }

    private fun `Then assert request params map contains query param map`(
            actualRequestParamsMap: Map<String, String>
    ) {
        val expectedQueryParamMap = defaultQueryParamMap

        expectedQueryParamMap.forEach { (key, value) ->
            val reason = constructAssertParamsFailReason(key)
            assertThat(reason, actualRequestParamsMap[key], shouldBe(value))
        }
    }

    private fun constructAssertParamsFailReason(key: String) =
            "Assert params failed with key \"$key\""

    private fun `Then assert request params map contains mandatory params`(
            mandatoryParams: Map<String, String>,
            actualRequestParamsMap: Map<String, String>
    ) {
        mandatoryParams.forEach { (key, value) ->
            val reason = constructAssertParamsFailReason(key)
            assertThat(reason, actualRequestParamsMap[key], shouldBe(value))
        }
    }

    protected fun `Then assert visitable list footer`(
            visitableList: List<Visitable<*>>,
            categoryNavigation: TokonowCategoryDetail.Navigation
    ) {
        val lastVisitable = visitableList.last()
        assertThat(lastVisitable, instanceOf(CategoryAisleDataView::class.java))

        val categoryAisleDataView = lastVisitable as CategoryAisleDataView
        val categoryAisleItemList = categoryAisleDataView.items

        assertThat(categoryAisleItemList.size, shouldBe(2))
        categoryAisleItemList[0].assertAisle(categoryNavigation.prev)
        categoryAisleItemList[1].assertAisle(categoryNavigation.next)
    }

    private fun CategoryAisleItemDataView.assertAisle(navigationItem: NavigationItem) {
        assertThat(this.name, shouldBe(navigationItem.name))
        assertThat(this.imgUrl, shouldBe(navigationItem.imageUrl))
    }

    protected fun `Then assert visitable list end with loading more model`(
            visitableList: List<Visitable<*>>
    ) {
        assertThat(visitableList.last(), instanceOf(LoadingMoreModel::class.java))
    }

    protected fun `Then assert has next page value`(expectedHasNextPage: Boolean) {
        assertThat(categoryViewModel.hasNextPageLiveData.value!!, shouldBe(expectedHasNextPage))
    }
}