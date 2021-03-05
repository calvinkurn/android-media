package com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetHelpfulUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.usecase.ReviewProductGetRatingUseCase
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSession
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewProductViewModelTestFixture {
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
}