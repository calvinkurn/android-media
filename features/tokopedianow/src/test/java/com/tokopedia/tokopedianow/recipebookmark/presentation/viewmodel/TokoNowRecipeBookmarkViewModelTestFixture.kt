package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.persentation.viewmodel.TokoNowRecipeBookmarkViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TokoNowRecipeBookmarkViewModelTestFixture {

    @RelaxedMockK
    protected lateinit var chooseAddressData: LocalCacheModel

    @RelaxedMockK
    protected lateinit var getRecipeBookmarksUseCase: GetRecipeBookmarksUseCase

    @RelaxedMockK
    protected lateinit var removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase

    @RelaxedMockK
    protected lateinit var addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase

    @get: Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TokoNowRecipeBookmarkViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowRecipeBookmarkViewModel(
            chooseAddressData,
            getRecipeBookmarksUseCase,
            removeRecipeBookmarkUseCase,
            addRecipeBookmarkUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun GetRecipeBookmarksUseCase.mockRecipeBookmark(recipeBookmarkResponse: GetRecipeBookmarksResponse) {
        coEvery {
            this@mockRecipeBookmark.execute(
                limit = any(),
                page = any(),
                warehouseId = any()
            )
        } returns recipeBookmarkResponse
    }

    protected fun GetRecipeBookmarksUseCase.mockRecipeBookmark(throwable: Throwable) {
        coEvery {
            this@mockRecipeBookmark.execute(
                limit = any(),
                page = any(),
                warehouseId = any()
            )
        } throws throwable
    }
}