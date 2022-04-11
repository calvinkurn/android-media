package com.tokopedia.reviewcommon.feature.media.detail.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.reviewcommon.TestHelper
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewGalleryVideo
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ReviewMedia
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.junit.Before

open class ReviewDetailViewModelTestFixture {

    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: ReviewDetailViewModel

    protected val getDetailedReviewMediaResultWithImageAndVideo = TestHelper.createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_and_video.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>().productrevGetReviewMedia

    protected val preloadedDetailedReviewMediaResult = ProductrevGetReviewMedia(
        reviewMedia = listOf(
            ReviewMedia(
                videoId = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first().videoId,
                feedbackId = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first().feedbackId,
                mediaNumber = 1
            )
        ),
        detail = Detail(
            reviewGalleryVideos = listOf(
                ReviewGalleryVideo(
                    attachmentId = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first().videoId,
                    url = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewGalleryVideos.first().url,
                    feedbackId = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first().feedbackId
                )
            )
        )
    )

    @Before
    fun setUp() {
        viewModel = ReviewDetailViewModel(coroutineDispatchers)
    }
}