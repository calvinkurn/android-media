package com.tokopedia.tokopedianow.recipebookmark.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.recipebookmark.domain.mapper.RecipeBookmarksMapper.mapResponseToUiModelList
import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.GetRecipeBookmarksResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.GetRecipeBookmarksUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkAction
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkState
import com.tokopedia.tokopedianow.recipebookmark.presentation.uimodel.ToasterUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class TokoNowRecipeBookmarkViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private lateinit var chooseAddressData: LocalCacheModel

    protected lateinit var getRecipeBookmarksUseCase: GetRecipeBookmarksUseCase

    protected lateinit var removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase

    protected lateinit var addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase

    protected lateinit var viewModel: TokoNowRecipeBookmarkViewModel

    protected val recipeList = "recipebookmark/recipebookmarksuccessequalsto10hasnext.json"
        .jsonToObject<GetRecipeBookmarksResponse>()
        .tokonowGetRecipeBookmarks
        .data
        .recipes
        .mapResponseToUiModelList()

    private val uiActionValues = arrayListOf<RecipeBookmarkAction>()

    @Before
    fun setup() {
        chooseAddressData = mockk(relaxed = true)
        getRecipeBookmarksUseCase = mockk(relaxed = true)
        removeRecipeBookmarkUseCase = mockk(relaxed = true)
        addRecipeBookmarkUseCase = mockk(relaxed = true)

        viewModel = TokoNowRecipeBookmarkViewModel(
            chooseAddressData,
            getRecipeBookmarksUseCase,
            removeRecipeBookmarkUseCase,
            addRecipeBookmarkUseCase,
            coroutineTestRule.dispatchers
        )

        mockRecipeBookmark()
    }

    @After
    fun tearDown() {
        uiActionValues.clear()
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

    private fun AddRecipeBookmarkUseCase.mockAddRecipeBookmark(response: AddRecipeBookmarkResponse) {
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

    private fun RemoveRecipeBookmarkUseCase.mockRemoveRecipeBookmark(throwable: Throwable) {
        coEvery {
            this@mockRemoveRecipeBookmark.execute(
                recipeId = any()
            )
        } throws throwable
    }

    protected fun mockRecipeBookmark(): GetRecipeBookmarksResponse {
        val recipeBookmarkResponse = "recipebookmark/recipebookmarksuccessequalsto10hasnext.json"
            .jsonToObject<GetRecipeBookmarksResponse>()

        getRecipeBookmarksUseCase
            .mockGetRecipeBookmark(
                response = recipeBookmarkResponse
            )

        return recipeBookmarkResponse
    }

    protected fun mockAddRecipeBookmark(addResponse: AddRecipeBookmarkResponse) {
        addRecipeBookmarkUseCase.mockAddRecipeBookmark(
            response = addResponse
        )
    }

    protected fun mockRemoveRecipeBookmark(removeResponse: RemoveRecipeBookmarkResponse) {
        removeRecipeBookmarkUseCase
            .mockRemoveRecipeBookmark(
                response = removeResponse
            )
    }

    protected fun mockRemoveRecipeBookmark(throwable: Throwable) {
        removeRecipeBookmarkUseCase
            .mockRemoveRecipeBookmark(
                throwable = throwable
            )
    }

    protected fun verifyList(expectedItemList: List<Visitable<*>>? = recipeList, isAdding: Boolean? = null) {
        val expectedItems = expectedItemList?.toMutableList()

        if (isAdding == false) {
            expectedItems?.removeAt(0)
        }

        val actualState = viewModel.uiState.value as? RecipeBookmarkState.Show

        val expectedScrollToTop = if (actualState == null) {
            null
        } else {
            false
        }

        val actualScrollToTop = actualState?.scrollToTop
        val actualItems = actualState?.items?.toMutableList()

        assertEquals(expectedScrollToTop, actualScrollToTop)
        assertEquals(expectedItems, actualItems)
    }

    protected fun verifyError(expectedStatusCode: String? = null, expectedThrowable: Throwable? = null) {
        val actualState = viewModel.uiState.value as RecipeBookmarkState.Error
        val actualStatus = actualState.code
        val actualThrowable = actualState.throwable

        assertEquals(expectedStatusCode, actualStatus)
        assertEquals(expectedThrowable, actualThrowable)
    }

    protected fun TestScope.observeUiAction() {
        backgroundScope.launch(coroutineTestRule.coroutineDispatcher) {
            viewModel.uiAction.toList(uiActionValues)
        }
    }

    protected fun verifyToaster(expectedToaster: ToasterUiModel) {
        val expectedModel = expectedToaster.model
        val actualModel = (uiActionValues.last() as RecipeBookmarkAction.ShowToaster).model
        assertEquals(expectedModel, actualModel)
    }

    protected fun verifyUiAction(expectedUiAction: RecipeBookmarkAction) {
        val actualUiAction = uiActionValues.last()
        assertEquals(expectedUiAction, actualUiAction)
    }

    protected fun verifyGetRecipeBookmarkUseCaseCalled(times: Int = 1) {
        coVerify(exactly = times) { getRecipeBookmarksUseCase.execute(any(), any(), any()) }
    }
}
