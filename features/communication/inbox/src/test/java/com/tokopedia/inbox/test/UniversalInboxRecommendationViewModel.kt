// package com.tokopedia.inbox.test
//
// import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
// import com.tokopedia.inbox.universalinbox.view.UniversalInboxAction
// import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
// import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
// import io.mockk.coEvery
// import kotlinx.coroutines.ExperimentalCoroutinesApi
// import kotlinx.coroutines.flow.flow
// import kotlinx.coroutines.test.runTest
// import org.junit.Assert
// import org.junit.Test
// import com.tokopedia.inbox.universalinbox.util.Result
// import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxProductRecommendationUiState
// import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
// import com.tokopedia.inbox.util.test
//
// @ExperimentalCoroutinesApi
// class UniversalInboxRecommendationViewModel : UniversalInboxViewModelTestFixture() {
//
//    private val dummyProductRecommendation = RecommendationWidget(
//        recommendationItemList = listOf(
//            RecommendationItem()
//        )
//    )
//
//    private val dummyProductRecommendationEmpty = listOf<RecommendationWidget>()
//
//    @Test
//    fun `refresh product recommendation, give product recommendation`() {
//        runTest {
//            // Given
//            val expectedResult = dummyProductRecommendation.recommendationItemList.map {
//                UniversalInboxRecommendationUiModel(it)
//            }
//
//            coEvery {
//                getRecommendationUseCase.observe()
//            } returns flow {
//                emit(Result.Success(dummyProductRecommendation))
//            }
//
//            // When
//            viewModel.setupViewModelObserver()
//            viewModel.processAction(UniversalInboxAction.RefreshRecommendation)
//
//            // Then
//            testProductRecommendation {
//                Assert.assertEquals(expectedResult, it.productRecommendation)
//            }
//            Assert.assertEquals(
//                2,
//                viewModel.getRecommendationPage()
//            )
//        }
//    }
//
//    private suspend fun testProductRecommendation(
//        verify: (UniversalInboxProductRecommendationUiState) -> Unit
//    ) {
//        viewModel.productRecommendationUiState.test {
//            verify(this.expectMostRecentItem())
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
// //    @Test
// //    fun `should give nothing when success get first page recommendation but null`() {
// //        runBlocking {
// //            // Given
// //            coEvery {
// //                getRecommendationUseCase.getData(any())
// //            } returns dummyProductRecommendationEmpty
// //
// //            // When
// //            viewModel.loadFirstPageRecommendation()
// //
// //            // Then
// //            Assert.assertTrue(
// //                viewModel.firstPageRecommendation.value is Fail
// //            )
// //        }
// //    }
// //
// //    @Test
// //    fun `should give error when error get first page recommendation`() {
// //        runBlocking {
// //            // Given
// //            coEvery {
// //                getRecommendationUseCase.getData(any())
// //            } throws dummyThrowable
// //
// //            // When
// //            viewModel.loadFirstPageRecommendation()
// //
// //            // Then
// //            Assert.assertEquals(
// //                dummyThrowable.message,
// //                (viewModel.firstPageRecommendation.value as Fail).throwable.message
// //            )
// //        }
// //    }
// //
// //    @Test
// //    fun `should give products recommendation when success get more recommendation`() {
// //        runBlocking {
// //            // Given
// //            coEvery {
// //                getRecommendationUseCase.getData(any())
// //            } returns dummyProductRecommendation
// //
// //            // When
// //            viewModel.loadMoreRecommendation(2)
// //
// //            // Then
// //            Assert.assertEquals(
// //                dummyProductRecommendation.firstOrNull()?.recommendationItemList,
// //                (viewModel.morePageRecommendation.value as Success).data
// //            )
// //        }
// //    }
// //
// //    @Test
// //    fun `should give error when success get more recommendation but null`() {
// //        runBlocking {
// //            // Given
// //            coEvery {
// //                getRecommendationUseCase.getData(any())
// //            } returns dummyProductRecommendationEmpty
// //
// //            // When
// //            viewModel.loadMoreRecommendation(2)
// //
// //            // Then
// //            Assert.assertTrue(
// //                viewModel.morePageRecommendation.value is Fail
// //            )
// //        }
// //    }
// //
// //    @Test
// //    fun `should give error when error get more recommendation`() {
// //        runBlocking {
// //            runBlocking {
// //                // Given
// //                coEvery {
// //                    getRecommendationUseCase.getData(any())
// //                } throws dummyThrowable
// //
// //                // When
// //                viewModel.loadMoreRecommendation(2)
// //
// //                // Then
// //                Assert.assertEquals(
// //                    dummyThrowable.message,
// //                    (viewModel.morePageRecommendation.value as Fail).throwable.message
// //                )
// //            }
// //        }
// //    }
// }
