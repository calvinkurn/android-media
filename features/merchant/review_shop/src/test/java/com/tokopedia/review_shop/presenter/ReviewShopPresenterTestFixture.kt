package com.tokopedia.review_shop.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review_shop.product.view.ReviewProductListMapper
import com.tokopedia.review_shop.shop.domain.DeleteReviewResponseUseCase
import com.tokopedia.review_shop.shop.domain.LikeDislikeReviewUseCase
import com.tokopedia.review_shop.shop.domain.ReviewShopUseCase
import com.tokopedia.review_shop.shop.view.presenter.ReviewShopContract
import com.tokopedia.review_shop.shop.view.presenter.ReviewShopPresenter
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