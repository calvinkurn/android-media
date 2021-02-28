package com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel

import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetHelpfulUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetRatingUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import java.lang.RuntimeException

class ReviewProductViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSession

    @RelaxedMockK
    lateinit var reviewProductListMapper: ReviewProductListMapper

    @RelaxedMockK
    lateinit var reviewProductGetHelpfulUseCase: ReviewProductGetHelpfulUseCase

    @RelaxedMockK
    lateinit var reviewProductGetListUseCase: ReviewProductGetListUseCase

    @RelaxedMockK
    lateinit var deleteReviewResponseUseCase: DeleteReviewResponseUseCase

    @RelaxedMockK
    lateinit var likeDislikeReviewUseCase: LikeDislikeReviewUseCase

    @RelaxedMockK
    lateinit var reviewProductGetRatingUseCase: ReviewProductGetRatingUseCase

    lateinit var viewModel: ReviewProductViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewProductViewModel(
                CoroutineTestDispatchersProvider,
                userSession,
                reviewProductListMapper,
                reviewProductGetHelpfulUseCase,
                reviewProductGetListUseCase,
                deleteReviewResponseUseCase,
                likeDislikeReviewUseCase,
                reviewProductGetRatingUseCase
        )
    }

    @Test
    fun `when getRatingReview success should execute expected usecase`() {
        val productId = anyString()
        val expectedNetworkResponse = DataResponseReviewStarCount()
        coEvery { reviewProductGetRatingUseCase.executeOnBackground() } returns expectedNetworkResponse
        viewModel.getRatingReview(productId)
        coVerify { reviewProductGetRatingUseCase.executeOnBackground() }
        viewModel.getRatingReview().verifyValueEquals(expectedNetworkResponse)
    }

    @Test
    fun `when getHelpfulReview success should execute expected usecase`() {
        val productId = anyString()
        val expectedNetworkResponse = DataResponseReviewHelpful()
        val expectedResult = anyList<ReviewProductModel>()
        coEvery { reviewProductGetHelpfulUseCase.executeOnBackground() } returns expectedNetworkResponse
        coEvery { reviewProductListMapper.map(expectedNetworkResponse, anyString(), anyString()) } returns expectedResult
        viewModel.getHelpfulReview(productId)
        coVerify { reviewProductGetHelpfulUseCase.executeOnBackground() }
        viewModel.getHelpfulReviewList().verifyValueEquals(expectedResult)
    }

    @Test
    fun `when getProductReview should success execute expected usecase`() {
        val productId = anyString()
        val page = anyInt()
        val rating = anyString()
        val isWithImage = anyBoolean()
        val expectedNetworkResponse = DataResponseReviewProduct()
        val userId = anyString()
        val reviewProductList = anyList<ReviewProductModel>()
        coEvery { reviewProductGetListUseCase.executeOnBackground() } returns expectedNetworkResponse
        coEvery { userSession.userId } returns userId
        coEvery { reviewProductListMapper.map(expectedNetworkResponse, userId) } returns reviewProductList
        viewModel.getProductReview(productId, page, rating, isWithImage)
        coVerify { reviewProductGetListUseCase.executeOnBackground() }
        viewModel.getReviewProductList().verifySuccessEquals(Success(reviewProductList to true))
    }

    @Test
    fun `when getProductReview fail expected error`() {
        val productId = anyString()
        val page = anyInt()
        val rating = anyString()
        val isWithImage = anyBoolean()
        val expectedError = Throwable()
        coEvery { reviewProductGetListUseCase.executeOnBackground() } throws expectedError
        viewModel.getProductReview(productId, page, rating, isWithImage)
        coVerify { reviewProductGetListUseCase.executeOnBackground() }
        viewModel.getReviewProductList().verifyErrorEquals(Fail(expectedError))
    }

    @Test
    fun `when deleteReview success should execute expected usecase`() {
        val reviewId = anyString()
        val reputationId = anyString()
        val productId = anyString()
        // 1 to assign isSuccess
        val expectedNetworkResponse = DeleteReviewResponseDomain(1)
        coEvery { deleteReviewResponseUseCase.executeOnBackground() } returns expectedNetworkResponse
        viewModel.deleteReview(reviewId, reputationId, productId)
        coEvery { deleteReviewResponseUseCase.executeOnBackground() }
        viewModel.getDeleteReview().verifySuccessEquals(Success(reviewId))
    }

    @Test
    fun `when deleteReview sucess but with isSuccess 0 should execute expected usecase and expected runtime Error`() {
        val reviewId = anyString()
        val reputationId = anyString()
        val productId = anyString()
        // 0 to assign isSuccess false
        val expectedNetworkResponse = DeleteReviewResponseDomain(0)
        coEvery { deleteReviewResponseUseCase.executeOnBackground() } returns expectedNetworkResponse
        viewModel.deleteReview(reviewId, reputationId, productId)
        coEvery { deleteReviewResponseUseCase.executeOnBackground() }
        viewModel.getDeleteReview().verifyErrorEquals(Fail(RuntimeException()))
    }

    @Test
    fun `when deleteReview fail expected error`() {
        val reviewId = anyString()
        val reputationId = anyString()
        val productId = anyString()
        val expectedNetworkResponse = Throwable()
        coEvery { deleteReviewResponseUseCase.executeOnBackground() } throws expectedNetworkResponse
        viewModel.deleteReview(reviewId, reputationId, productId)
        coEvery { deleteReviewResponseUseCase.executeOnBackground() }
        viewModel.getDeleteReview().verifyErrorEquals(Fail(expectedNetworkResponse))
    }

    @Test
    fun `when postLikeDislikeReview success should execute expected usecase`() {
        val reviewId = anyString()
        val likeStatus = anyInt()
        val productId = anyString()
        val expectedNetworkResponse = LikeDislikeDomain(
                anyInt(), anyInt(), anyInt()
        )
        coEvery { likeDislikeReviewUseCase.executeOnBackground() } returns expectedNetworkResponse
        viewModel.postLikeDislikeReview(reviewId, likeStatus, productId)
        coVerify { likeDislikeReviewUseCase.executeOnBackground() }
        viewModel.getPostLikeDislike().verifyValueEquals(expectedNetworkResponse to reviewId)
    }

    @Test
    fun `when postLikeDislikeReview fail expected error`() {
        val reviewId = anyString()
        val likeStatus = anyInt()
        val productId = anyString()
        val expectedNetworkResponse = Throwable()
        coEvery { likeDislikeReviewUseCase.executeOnBackground() } throws expectedNetworkResponse
        viewModel.postLikeDislikeReview(reviewId, likeStatus, productId)
        coVerify { likeDislikeReviewUseCase.executeOnBackground() }
        viewModel.getErrorPostLikeDislike().verifyValueEquals(Triple(expectedNetworkResponse, reviewId, likeStatus))
    }

}

