package com.tokopedia.reviewcommon.feature.media.player.controller.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.reviewcommon.TestHelper
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductRevGetDetailedReviewMediaResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.junit.Before

open class ReviewMediaPlayerControllerViewModelTestFixture {
    private val coroutineDispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: ReviewMediaPlayerControllerViewModel

    protected val getDetailedReviewMediaResult1stPage = TestHelper.createSuccessResponse<ProductRevGetDetailedReviewMediaResponse>(
        "json/get_detailed_review_media_use_case_result_with_image_and_video.json"
    ).getSuccessData<ProductRevGetDetailedReviewMediaResponse>()

    @Before
    fun setUp() {
        viewModel = ReviewMediaPlayerControllerViewModel(coroutineDispatchers)
    }
}