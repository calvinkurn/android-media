package com.tokopedia.tokomart.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule

open class CategoryTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val defaultCategoryId = 123
    protected val defaultQueryParamMap = mapOf<String, String>()
    protected val getCategoryFirstPageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getCategoryLoadMorePageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val chooseAddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)

    protected lateinit var categoryViewModel: CategoryViewModel

    @Before
    open fun setUp() {
        `Given category view model`()
    }

    protected open fun `Given category view model`(
            categoryId: Int = defaultCategoryId,
            queryParamMap: Map<String, String> = defaultQueryParamMap,
    ) {
        categoryViewModel = CategoryViewModel(
                CoroutineTestDispatchersProvider,
                categoryId,
                queryParamMap,
                getCategoryFirstPageUseCase,
                getCategoryLoadMorePageUseCase,
                getFilterUseCase,
                chooseAddressWrapper,
        )
    }

    protected fun `Given get category first page use case will be successful`(
            categoryModel: CategoryModel,
            requestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        every {
            getCategoryFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            firstArg<(CategoryModel) -> Unit>().invoke(categoryModel)
        }
    }

    protected fun `Given view already created`() {
        categoryViewModel.onViewCreated()
    }

    protected fun `When view created`() {
        categoryViewModel.onViewCreated()
    }
}