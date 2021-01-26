package com.tokopedia.shop.review.shop.view.presenter

import com.tokopedia.shop.review.product.data.model.reviewlist.DataResponseReviewShop
import com.tokopedia.shop.review.shop.domain.model.DeleteReviewResponseDomain
import com.tokopedia.shop.review.shop.domain.model.LikeDislikeDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.observers.TestSubscriber

class ReviewShopPresenterTest : ReviewShopPresenterTestFixture() {

    @Test
    fun `when deleteReview success should execute expected usecase and perform expected view action`() {
        val expectedReturn = mockk<DeleteReviewResponseDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<DeleteReviewResponseDomain?> = TestSubscriber()

        every {
            deleteReviewResponseUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        reviewShopPresenter.deleteReview(anyString(), anyString(), anyString())

        verifyDeleteReviewResponseUseCaseCalled()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when deleteReview error should execute expected usecase and perform expected error handling`() {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<DeleteReviewResponseDomain?> = TestSubscriber()

        every {
            deleteReviewResponseUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        reviewShopPresenter.deleteReview(anyString(), anyString(), anyString())

        verifyDeleteReviewResponseUseCaseCalled()
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when postLikeDislikeReview success should execute expected usecase and perform expected view action`() {
        val expectedReturn = mockk<LikeDislikeDomain>(relaxed = true)
        val testSubscriber: TestSubscriber<LikeDislikeDomain?> = TestSubscriber()

        every {
            likeDislikeReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        reviewShopPresenter.postLikeDislikeReview(anyString(), anyInt(), anyString())

        verifyLikeDislikeReviewUseCaseCalled()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when postLikeDislikeReview error should execute expected usecase and perform expected error handling`() {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<LikeDislikeDomain?> = TestSubscriber()

        every {
            likeDislikeReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        reviewShopPresenter.postLikeDislikeReview(anyString(), anyInt(), anyString())

        verifyLikeDislikeReviewUseCaseCalled()
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when getShopReview success should execute expected usecase and perform expected view action`() {
        val expectedReturn = mockk<DataResponseReviewShop>(relaxed = true)
        val testSubscriber: TestSubscriber<DataResponseReviewShop?> = TestSubscriber()

        every {
            shopReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        reviewShopPresenter.getShopReview(anyString(), anyString(), anyInt())

        verifyShopReviewUseCaseCalled()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when getShopReview error should execute expected usecase and perform expected error handling`() {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<DataResponseReviewShop?> = TestSubscriber()

        every {
            shopReviewUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        reviewShopPresenter.getShopReview(anyString(), anyString(), anyInt())

        verifyShopReviewUseCaseCalled()
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when isMyShop should return true if equals to userSession's shopId and vice versa`() {
        val shopId = "someShopId"

        every {
            userSession.shopId
        } answers {
            shopId
        }

        Assert.assertTrue(reviewShopPresenter.isMyShop(shopId))

        every {
            userSession.shopId
        } answers {
            ""
        }

        Assert.assertFalse(reviewShopPresenter.isMyShop(shopId))
    }

    @Test
    fun `check whether isLogin return same value as mocked userSession isLoggedIn value`() {
        every {
            userSession.isLoggedIn
        } answers {
            true
        }
        Assert.assertTrue(reviewShopPresenter.isLogin)
        every {
            userSession.isLoggedIn
        } answers {
            false
        }
        Assert.assertFalse(reviewShopPresenter.isLogin)
    }

    @Test
    fun `check whether required function is called when onDestroy`() {
        reviewShopPresenter.onDestroy()
        verify {
            shopReviewUseCase.unsubscribe()
            likeDislikeReviewUseCase.unsubscribe()
            deleteReviewResponseUseCase.unsubscribe()
        }
    }


    private fun verifyDeleteReviewResponseUseCaseCalled() {
        verify { deleteReviewResponseUseCase.execute(any(), any()) }
    }

    private fun verifyLikeDislikeReviewUseCaseCalled() {
        verify { likeDislikeReviewUseCase.execute(any(), any()) }
    }

    private fun verifyShopReviewUseCaseCalled() {
        verify { shopReviewUseCase.execute(any(), any()) }
    }
}