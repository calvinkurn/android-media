 
package com.tokopedia.inbox.test

import app.cash.turbine.test
import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.inbox.universalinbox.util.Result
import com.tokopedia.inbox.universalinbox.view.UniversalInboxAction
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxProductRecommendationUiState
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

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
    fun `refresh product recommendation, get product recommendation`() {
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

            // Then
            Assert.assertEquals(
                2,
                viewModel.getRecommendationPage()
            )
        }
    }

    @Test
    fun `refresh product recommendation, get empty product recommendation`() {
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

            // Then
            Assert.assertEquals(
                2,
                viewModel.getRecommendationPage()
            )
        }
    }

    @Test
    fun `refresh product recommendation, get error recommendation`() {
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
                    "handleResultProductRecommendation",
                    errorState.error?.second
                )
            }

            // Then
            Assert.assertEquals(
                1,
                viewModel.getRecommendationPage()
            )
        }
    }

    @Test
    fun `refresh and get more product recommendation, get more product recommendation`() {
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
                viewModel.processAction(UniversalInboxAction.LoadNextPage)

                // Then updated state
                assertUpdatedState(
                    awaitItem(),
                    dummyProductRecommendation.title,
                    expectedResult
                )

                println(cancelAndConsumeRemainingEvents())
            }

            // Then
            Assert.assertEquals(
                viewModel.getRecommendationPage(),
                2 // init 1 + load more 1
            )
        }
    }

    @Test
    fun `refresh and get more product recommendation, get empty product recommendation`() {
        runTest {
            // Given
            mockRecommendationFlow(Result.Success(dummyProductRecommendationEmpty))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.LoadNextPage)

                // Then updated state
                assertUpdatedState(
                    awaitItem(),
                    dummyProductRecommendationEmpty.title,
                    listOf(),
                    isEmpty = true
                )

                cancelAndConsumeRemainingEvents()
            }

            // Then
            Assert.assertEquals(
                2, // init 1 + load more 1
                viewModel.getRecommendationPage()
            )
        }
    }

    @Test
    fun `should give error when error get more recommendation`() {
        runTest {
            // Given
            mockRecommendationFlow(Result.Error(dummyThrowable))

            viewModel.productRecommendationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When update state
                viewModel.processAction(UniversalInboxAction.LoadNextPage)

                // Then updated state
                assertErrorState(awaitItem())

                println(cancelAndConsumeRemainingEvents())
            }

            viewModel.errorUiState.test {
                // When update state 2
                viewModel.processAction(UniversalInboxAction.LoadNextPage)
                // Then show error
                val errorState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable.message,
                    errorState.error?.first?.message
                )
                Assert.assertEquals(
                    "handleResultProductRecommendation",
                    errorState.error?.second
                )
            }

            // Then
            Assert.assertEquals(
                1,
                viewModel.getRecommendationPage()
            )
        }
    }

    private fun mockRecommendationFlow(
        expectedResult: Result<RecommendationWidget>
    ) {
        val recommendationFlow = MutableSharedFlow<Result<RecommendationWidget>>()

        coEvery {
            getRecommendationUseCase.observe()
        } returns recommendationFlow

        coEvery {
            getRecommendationUseCase.fetchProductRecommendation(any())
        } coAnswers {
            recommendationFlow.emit(expectedResult)
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
