package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import io.mockk.every
import org.hamcrest.CoreMatchers
import org.junit.Assert

open class BaseCategoryPageLoadTest: CategoryTestFixtures() {

    protected fun `Given get category first page use case will be successful`(categoryModel: CategoryModel) {
        every {
            getCategoryFirstPageUseCase.execute(any(), any(), any())
        } answers {
            firstArg<(CategoryModel) -> Unit>().invoke(categoryModel)
        }
    }

    protected fun `Then assert visitable list footer`(visitableList: List<Visitable<*>>) {
        Assert.assertThat(visitableList.last(), CoreMatchers.instanceOf(CategoryIsleDataView::class.java))
    }

    protected fun `Then assert visitable list end with loading more model`(visitableList: List<Visitable<*>>) {
        Assert.assertThat(visitableList.last(), CoreMatchers.instanceOf(LoadingMoreModel::class.java))
    }
}