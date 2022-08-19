package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
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

    protected fun GetRecipeBookmarksUseCase.mockGetRecipeBookmark(response: GetRecipeBookmarksResponse) {
        coEvery {
            this@mockGetRecipeBookmark.execute(
                limit = any(),
                page = any(),
                warehouseId = any()
            )
        } returns response
    }

    protected fun GetRecipeBookmarksUseCase.mockGetRecipeBookmark(throwable: Throwable) {
        coEvery {
            this@mockGetRecipeBookmark.execute(
                limit = any(),
                page = any(),
                warehouseId = any()
            )
        } throws throwable
    }

    protected fun AddRecipeBookmarkUseCase.mockAddRecipeBookmark(response: AddRecipeBookmarkResponse) {
        coEvery {
            this@mockAddRecipeBookmark.execute(
                recipeId = any()
            )
        } returns response.tokonowAddRecipeBookmark
    }

    protected fun AddRecipeBookmarkUseCase.mockAddRecipeBookmark(throwable: Throwable) {
        coEvery {
            this@mockAddRecipeBookmark.execute(
                recipeId = any()
            )
        } throws throwable
    }

    protected fun RemoveRecipeBookmarkUseCase.mockRemoveRecipeBookmark(response: RemoveRecipeBookmarkResponse) {
        coEvery {
            this@mockRemoveRecipeBookmark.execute(
                recipeId = any()
            )
        } returns response.tokonowRemoveRecipeBookmark
    }

    protected fun RemoveRecipeBookmarkUseCase.mockRemoveRecipeBookmark(throwable: Throwable) {
        coEvery {
            this@mockRemoveRecipeBookmark.execute(
                recipeId = any()
            )
        } throws throwable
    }

}