package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokomart.search.domain.model.SearchModel
import io.mockk.every
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat

open class BaseSearchPageLoadTest: SearchTestFixtures() {

    protected fun `Given get search first page use case will be successful`(searchModel: SearchModel) {
        every {
            getSearchFirstPageUseCase.execute(any(), any(), any())
        } answers {
            firstArg<(SearchModel) -> Unit>().invoke(searchModel)
        }
    }

    protected fun `Then assert visitable list does not end with loading more model`(
            visitableList: List<Visitable<*>>
    ) {
        assertThat(visitableList.last(), CoreMatchers.not(instanceOf(LoadingMoreModel::class.java)))
    }

    protected fun `Then assert visitable list end with loading more model`(visitableList: List<Visitable<*>>) {
        assertThat(visitableList.last(), instanceOf(LoadingMoreModel::class.java))
    }
}