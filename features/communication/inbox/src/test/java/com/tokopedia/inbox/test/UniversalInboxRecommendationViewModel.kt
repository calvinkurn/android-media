package com.tokopedia.inbox.test

import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UniversalInboxRecommendationViewModel : UniversalInboxViewModelTestFixture() {

    private val dummyProductRecommendation = listOf(
        RecommendationWidget(
            recommendationItemList = listOf(
                RecommendationItem()
            )
        )
    )

    private val dummyProductRecommendationEmpty = listOf<RecommendationWidget>()

    @Test
    fun `should give products recommendation when success get first page recommendation`() {
        runBlocking {
            // Given
            coEvery {
                getRecommendationUseCase.getData(any())
            } returns dummyProductRecommendation

            // When
            viewModel.loadFirstPageRecommendation()

            // Then
            Assert.assertEquals(
                dummyProductRecommendation.firstOrNull(),
                (viewModel.firstPageRecommendation.value as Success).data
            )
        }
    }

    @Test
    fun `should give nothing when success get first page recommendation but null`() {
        runBlocking {
            // Given
            coEvery {
                getRecommendationUseCase.getData(any())
            } returns dummyProductRecommendationEmpty

            // When
            viewModel.loadFirstPageRecommendation()

            // Then
            Assert.assertTrue(
                viewModel.firstPageRecommendation.value is Fail
            )
        }
    }

    @Test
    fun `should give error when error get first page recommendation`() {
        runBlocking {
            // Given
            coEvery {
                getRecommendationUseCase.getData(any())
            } throws dummyThrowable

            // When
            viewModel.loadFirstPageRecommendation()

            // Then
            Assert.assertEquals(
                dummyThrowable.message,
                (viewModel.firstPageRecommendation.value as Fail).throwable.message
            )
        }
    }

    @Test
    fun `should give products recommendation when success get more recommendation`() {
        runBlocking {
            // Given
            coEvery {
                getRecommendationUseCase.getData(any())
            } returns dummyProductRecommendation

            // When
            viewModel.loadMoreRecommendation(2)

            // Then
            Assert.assertEquals(
                dummyProductRecommendation.firstOrNull()?.recommendationItemList,
                (viewModel.morePageRecommendation.value as Success).data
            )
        }
    }

    @Test
    fun `should give error when success get more recommendation but null`() {
        runBlocking {
            // Given
            coEvery {
                getRecommendationUseCase.getData(any())
            } returns dummyProductRecommendationEmpty

            // When
            viewModel.loadMoreRecommendation(2)

            // Then
            Assert.assertTrue(
                viewModel.morePageRecommendation.value is Fail
            )
        }
    }

    @Test
    fun `should give error when error get more recommendation`() {
        runBlocking {
            runBlocking {
                // Given
                coEvery {
                    getRecommendationUseCase.getData(any())
                } throws dummyThrowable

                // When
                viewModel.loadMoreRecommendation(2)

                // Then
                Assert.assertEquals(
                    dummyThrowable.message,
                    (viewModel.morePageRecommendation.value as Fail).throwable.message
                )
            }
        }
    }
}
