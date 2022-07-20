package com.tokopedia.review.feature.gallery.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.review.feature.gallery.domain.usecase.GetProductRatingUseCase
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.GetDetailedReviewMediaUseCase
import com.tokopedia.review.utils.createSuccessResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewGalleryViewModelTestFixture {

    companion object {
        const val DEFAULT_FIRST_PAGE = 1
    }

    @RelaxedMockK
    lateinit var getProductRatingUseCase: GetProductRatingUseCase

    @RelaxedMockK
    lateinit var getDetailedReviewMediaUseCase: GetDetailedReviewMediaUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected val getDetailedReviewMediaResult1stPage = createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_and_video.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

    protected val getDetailedReviewMediaResult2ndPage = createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_load_more.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

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