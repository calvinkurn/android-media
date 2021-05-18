package com.tokopedia.tokomart.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.mockk
import org.junit.Rule

open class CategoryTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val getCategoryFirstPageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getCategoryLoadMorePageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val chooseAddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)

    protected val categoryViewModel = CategoryViewModel(
            CoroutineTestDispatchersProvider,
            getCategoryFirstPageUseCase,
            getCategoryLoadMorePageUseCase,
            getFilterUseCase,
            chooseAddressWrapper,
    )
}