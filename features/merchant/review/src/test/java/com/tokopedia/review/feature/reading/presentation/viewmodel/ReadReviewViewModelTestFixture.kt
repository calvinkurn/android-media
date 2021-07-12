package com.tokopedia.review.feature.reading.presentation.viewmodel

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ReadReviewViewModelTestFixture {

    @RelaxedMockK
    lateinit var getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase

    @RelaxedMockK
    lateinit var getProductReviewListUseCase: GetProductReviewListUseCase

    @RelaxedMockK
    lateinit var toggleLikeReviewUseCase: ToggleLikeReviewUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReadReviewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReadReviewViewModel(getProductRatingAndTopicsUseCase, getProductReviewListUseCase, toggleLikeReviewUseCase, userSessionInterface, CoroutineTestDispatchersProvider)
        viewModel.productReviews.observeForever { }
        viewModel.ratingAndTopic.observeForever { }
        mockkStatic(Resources::class)
        val resources = mockk<Resources>()
        val displayMetrics = mockk<DisplayMetrics>()
        every { Resources.getSystem() } returns resources
        every { Resources.getSystem().displayMetrics } returns displayMetrics
    }

    @After
    fun finish() {
        unmockkStatic(Resources::class)
    }
}