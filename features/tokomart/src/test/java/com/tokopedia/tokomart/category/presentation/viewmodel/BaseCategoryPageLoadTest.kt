package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

open class BaseCategoryPageLoadTest: CategoryTestFixtures() {

    protected val requestParamsSlot = slot<RequestParams>()
    protected val requestParams by lazy { requestParamsSlot.captured }

    protected fun `Then assert visitable list footer`(visitableList: List<Visitable<*>>) {
        assertThat(visitableList.last(), instanceOf(CategoryAisleDataView::class.java))
    }

    protected fun `Then assert visitable list end with loading more model`(visitableList: List<Visitable<*>>) {
        assertThat(visitableList.last(), instanceOf(LoadingMoreModel::class.java))
    }

    protected fun `Then assert has next page value`(expectedHasNextPage: Boolean) {
        assertThat(categoryViewModel.hasNextPageLiveData.value!!, shouldBe(expectedHasNextPage))
    }
}