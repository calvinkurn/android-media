package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.feature.gallery.domain.usecase.GetProductRatingUseCase
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewGalleryViewModelTestFixture {

    @RelaxedMockK
    lateinit var getProductRatingUseCase: GetProductRatingUseCase

    @RelaxedMockK
    lateinit var getDetailedReviewMediaUseCase: GetDetailedReviewMediaUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewGalleryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewGalleryViewModel(
            getProductRatingUseCase,
            getDetailedReviewMediaUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.reviewMedia.observeForever { }
        viewModel.rating.observeForever { }
    }
}