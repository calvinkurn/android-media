package com.tokopedia.review.feature.media.gallery.base.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.review.utils.createSuccessResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.junit.Before

open class ReviewMediaGalleryViewModelTestFixture {
    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: ReviewMediaGalleryViewModel

    protected val getDetailedReviewMediaResult1stPage = createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_and_video.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

    protected val getDetailedReviewMediaResult2ndPage = createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_load_more.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

    protected val getDetailedReviewMediaResultWithInvalidImageAndVideo = createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_invalid_image_and_video.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

    @Before
    fun setUp() {
        viewModel = ReviewMediaGalleryViewModel(coroutineDispatchers)
    }
}