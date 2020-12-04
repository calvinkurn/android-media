package com.tokopedia.shop.review.shop.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.review.product.view.ReviewProductListMapper
import com.tokopedia.shop.review.shop.domain.DeleteReviewResponseUseCase
import com.tokopedia.shop.review.shop.domain.LikeDislikeReviewUseCase
import com.tokopedia.shop.review.shop.domain.ReviewShopUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewShopPresenterTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var shopReviewUseCase: ReviewShopUseCase

    @RelaxedMockK
    lateinit var likeDislikeReviewUseCase: LikeDislikeReviewUseCase

    @RelaxedMockK
    lateinit var deleteReviewResponseUseCase: DeleteReviewResponseUseCase

    @RelaxedMockK
    lateinit var reviewProductListMapper: ReviewProductListMapper

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var view: ReviewShopContract.View

    protected lateinit var reviewShopPresenter: ReviewShopPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        reviewShopPresenter = ReviewShopPresenter(shopReviewUseCase, likeDislikeReviewUseCase,
                deleteReviewResponseUseCase, reviewProductListMapper, userSession)
        reviewShopPresenter.attachView(view)
    }

}