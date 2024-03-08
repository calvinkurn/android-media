 
package com.tokopedia.inbox.test

import app.cash.turbine.test
import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.inbox.universalinbox.util.Result
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.INBOX_ADS_REFRESH_KEY
import com.tokopedia.inbox.universalinbox.view.UniversalInboxAction
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxProductRecommendationUiState
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class UniversalInboxRecommendationViewModel : UniversalInboxViewModelTestFixture() {

    private val dummyProductRecommendation = RecommendationWidget(
        title = "dummyTitle",
        recommendationItemList = listOf(
            RecommendationItem()
        )
    )

    private val dummyProductRecommendationEmpty = RecommendationWidget(
        title = "dummyEmptyTitle"
    )

    @Test
    fun `observe product recommendation, get product recommendation`() {
        runTest {
            // Given
            val expectedResult = dummyProductRecommendation.recommendationItemList.map {
                UniversalInboxRecommendationUiModel(it)
            }
            mockRecommendationFlow(Result.Success(dummyProductRecommendation))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then initial state
                assertInitialState(awaitItem())

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshRecommendation)
                // Then loading state
                assertLoadingState(awaitItem())

                // Then updated state
                assertUpdatedState(
                    awaitItem(),
                    dummyProductRecommendation.title,
                    expectedResult
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe product recommendation, get empty product recommendation`() {
        runTest {
            // Given
            mockRecommendationFlow(Result.Success(dummyProductRecommendationEmpty))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then initial state
                assertInitialState(awaitItem())

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshRecommendation)
                // Then loading state
                assertLoadingState(awaitItem())

                // Then updated state
                assertUpdatedState(
                    awaitItem(),
                    dummyProductRecommendationEmpty.title,
                    listOf(),
                    isEmpty = true
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe product recommendation, get error recommendation`() {
        runTest {
            // Given
            mockRecommendationFlow(Result.Error(dummyThrowable))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then initial state
                assertInitialState(awaitItem())

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshRecommendation)
                // Then loading state
                assertLoadingState(awaitItem())

                // Then updated state
                assertErrorState(awaitItem())

                cancelAndConsumeRemainingEvents()
            }

            viewModel.errorUiState.test {
                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshRecommendation)
                // Then show error
                val errorState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable.message,
                    errorState.error?.first?.message
                )
                Assert.assertEquals(
                    "loadProductRecommendation",
                    errorState.error?.second
                )
            }
        }
    }

    @Test
    fun `observe and get more product recommendation, get more product recommendation`() {
        runTest {
            // Given
            val expectedResult = dummyProductRecommendation.recommendationItemList.map {
                UniversalInboxRecommendationUiModel(it)
            }
            mockRecommendationFlow(Result.Success(dummyProductRecommendation))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.LoadNextPage(1))

                // Then updated state
                assertUpdatedState(
                    awaitItem(),
                    dummyProductRecommendation.title,
                    expectedResult
                )

                println(cancelAndConsumeRemainingEvents())
            }
        }
    }

    @Test
    fun `observe and get more product recommendation, get empty product recommendation`() {
        runTest {
            // Given
            mockRecommendationFlow(Result.Success(dummyProductRecommendationEmpty))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.LoadNextPage(1))

                // Then updated state
                assertUpdatedState(
                    awaitItem(),
                    dummyProductRecommendationEmpty.title,
                    listOf(),
                    isEmpty = true
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe and get more product recommendation, get error`() {
        runTest {
            // Given
            mockRecommendationFlow(Result.Error(dummyThrowable))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When update state
                viewModel.processAction(UniversalInboxAction.LoadNextPage(1))

                // Then updated state
                assertErrorState(awaitItem())

                println(cancelAndConsumeRemainingEvents())
            }

            viewModel.errorUiState.test {
                // When update state 2
                viewModel.processAction(UniversalInboxAction.LoadNextPage(2))
                // Then show error
                val errorState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable.message,
                    errorState.error?.first?.message
                )
                Assert.assertEquals(
                    "loadProductRecommendation",
                    errorState.error?.second
                )
            }
        }
    }

    @Test
    fun `observe and get more product recommendation, get loading`() {
        runTest {
            // Given
            mockRecommendationFlow(Result.Loading)

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.LoadNextPage(2))
                // Then update state
                assertLoadingState(awaitItem())

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `auto scroll recommendation, update state to true `() {
        runTest {
            // Given
            every {
                abTestPlatform.getString(any(), any())
            } returns INBOX_ADS_REFRESH_KEY

            viewModel.autoScrollUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(
                    UniversalInboxAction.AutoScrollRecommendation
                )

                skipItems(1) // initial state value

                // Then
                val updatedValue = awaitItem()
                assertEquals(true, updatedValue.shouldScroll)
            }
        }
    }

    @Test
    fun `auto scroll recommendation with rollence off, reset state`() {
        runTest {
            // Given
            every {
                abTestPlatform.getString(any(), any())
            } returns ""

            viewModel.autoScrollUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(
                    UniversalInboxAction.AutoScrollRecommendation
                )

                // Then
                val value = awaitItem()
                assertEquals(false, value.shouldScroll)
            }
        }
    }

    @Test
    fun `reset user scroll state, auto scroll ui state reset`() {
        runTest {
            // Given
            viewModel.autoScrollUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(UniversalInboxAction.ResetUserScrollState)

                // Then
                val initialValue = awaitItem()
                assertEquals(false, initialValue.shouldScroll)
            }
        }
    }

    private fun mockRecommendationFlow(
        expectedResult: Result<RecommendationWidget>
    ) {
        coEvery {
            getRecommendationUseCase.fetchProductRecommendation(any())
        } returns flow {
            emit(expectedResult)
        }
    }

    private fun assertInitialState(initialState: UniversalInboxProductRecommendationUiState) {
        assert(!initialState.isLoading)
        assert(initialState.productRecommendation.isEmpty())
        assert(initialState.title.isBlank())
    }
    private fun assertLoadingState(loadingState: UniversalInboxProductRecommendationUiState) {
        assert(loadingState.isLoading)
        assert(loadingState.productRecommendation.isEmpty())
        assert(loadingState.title.isBlank())
    }

    private fun assertUpdatedState(
        updatedState: UniversalInboxProductRecommendationUiState,
        expectedTitle: String,
        expectedListResult: List<UniversalInboxRecommendationUiModel>,
        isEmpty: Boolean = false
    ) {
        assert(updatedState.productRecommendation.isEmpty() == isEmpty)
        Assert.assertEquals(expectedListResult, updatedState.productRecommendation)
        Assert.assertEquals(
            expectedTitle,
            updatedState.title
        )
    }

    private fun assertErrorState(errorState: UniversalInboxProductRecommendationUiState) {
        assert(!errorState.isLoading)
        assert(errorState.productRecommendation.isEmpty())
        assert(errorState.title.isBlank())
    }
}
