package com.tokopedia.review.feature.media.detail.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailBasicInfoUiModel
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailSupplementaryInfoUiModel
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailUiModel
import com.tokopedia.review.feature.media.detail.presentation.uistate.ExpandedReviewDetailBottomSheetUiState
import com.tokopedia.review.feature.media.detail.presentation.uistate.ReviewDetailBasicInfoUiState
import com.tokopedia.review.feature.media.detail.presentation.uistate.ReviewDetailFragmentUiState
import com.tokopedia.review.feature.media.detail.presentation.uistate.ReviewDetailSupplementaryUiState
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.review.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import com.tokopedia.reviewcommon.extension.getSavedState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class ReviewDetailViewModelTest : ReviewDetailViewModelTestFixture() {
    @Test
    fun `currentReviewDetail should null when _currentMediaItem is null`() = runBlockingTest {
        viewModel.updateCurrentMediaItem(null)
        viewModel.updateGetDetailedReviewMediaResult(mockk())
        val currentReviewDetail = viewModel.currentReviewDetail.first()
        Assert.assertNull(currentReviewDetail)
    }

    @Test
    fun `currentReviewDetail should null when _getDetailedReviewMediaResult is null`() = runBlockingTest {
        viewModel.updateGetDetailedReviewMediaResult(null)
        val currentReviewDetail = viewModel.currentReviewDetail.first()
        Assert.assertNull(currentReviewDetail)
    }

    @Test
    fun `currentReviewDetail should not null when currentMediaItem has review detail`() = runBlockingTest {
        val firstMedia = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first()
        val reviewDetailData = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewDetail.first {
            it.feedbackId == firstMedia.feedbackId
        }
        val mediaItemUiModel = VideoMediaItemUiModel(
            id = firstMedia.videoId,
            uri = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewGalleryVideos.first().url,
            mediaNumber = firstMedia.mediaNumber,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResultWithImageAndVideo.detail.mediaCount.toInt(),
            feedbackId = firstMedia.feedbackId
        )
        val expected = ReviewDetailUiModel(
            basicInfoUiModel = ReviewDetailBasicInfoUiModel(
                feedbackId = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first().feedbackId,
                rating = reviewDetailData.rating,
                createTimeStr = reviewDetailData.createTimestamp,
                likeCount = reviewDetailData.totalLike,
                isLiked = reviewDetailData.isLiked,
                userId = reviewDetailData.user.userId,
                anonymous = reviewDetailData.isAnonymous,
                profilePicture = reviewDetailData.user.image,
                reviewerName = reviewDetailData.user.fullName,
                reviewerStatsSummary = reviewDetailData.userStats.joinToString(separator = " • ") {
                    it.formatted
                }
            ),
            supplementaryInfoUiModel = ReviewDetailSupplementaryInfoUiModel(
                review = reviewDetailData.review,
                complaint = reviewDetailData.badRatingReasonFmt
            ),
            isReportable = reviewDetailData.isReportable,
            feedbackID = reviewDetailData.feedbackId,
            shopID = reviewDetailData.shopId
        )
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResultWithImageAndVideo)
        viewModel.updateCurrentMediaItem(mediaItemUiModel)
        val currentReviewDetail = viewModel.currentReviewDetail.first()
        Assert.assertEquals(expected, currentReviewDetail)
    }

    @Test
    fun `currentReviewDetail should null when currentMediaItem don't have review detail`() = runBlockingTest {
        val firstMedia = preloadedDetailedReviewMediaResult.reviewMedia.first()
        val mediaItemUiModel = VideoMediaItemUiModel(
            id = firstMedia.videoId,
            uri = preloadedDetailedReviewMediaResult.detail.reviewGalleryVideos.first().url,
            mediaNumber = firstMedia.mediaNumber,
            showSeeMore = false,
            totalMediaCount = preloadedDetailedReviewMediaResult.detail.mediaCount.toInt(),
            feedbackId = firstMedia.feedbackId
        )
        viewModel.updateGetDetailedReviewMediaResult(preloadedDetailedReviewMediaResult)
        viewModel.updateCurrentMediaItem(mediaItemUiModel)
        val currentReviewDetail = viewModel.currentReviewDetail.first()
        Assert.assertNull(currentReviewDetail)
    }

    @Test
    fun `reviewDetailFragmentUiState should equal to Hidden when _basicInfoUiState is Hidden and _supplementaryInfoUiState is Hidden`() = runBlockingTest {
        val firstMedia = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first()
        val mediaItemUiModel = VideoMediaItemUiModel(
            id = firstMedia.videoId,
            uri = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewGalleryVideos.first().url,
            mediaNumber = firstMedia.mediaNumber,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResultWithImageAndVideo.detail.mediaCount.toInt(),
            feedbackId = firstMedia.feedbackId
        )
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResultWithImageAndVideo)
        viewModel.updateCurrentMediaItem(mediaItemUiModel)
        viewModel.updateCurrentOrientation(OrientationUiState(OrientationUiState.Orientation.LANDSCAPE))
        viewModel.updateCurrentOverlayVisibility(false)
        val reviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        Assert.assertEquals(ReviewDetailFragmentUiState.Hidden, reviewDetailFragmentUiState)
    }

    @Test
    fun `reviewDetailFragmentUiState should equal to Showing when _basicInfoUiState is Loading and _supplementaryInfoUiState is Loading`() = runBlockingTest {
        viewModel.updateCurrentMediaItem(LoadingStateItemUiModel(mediaNumber = 1))
        val reviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        Assert.assertTrue(
            reviewDetailFragmentUiState is ReviewDetailFragmentUiState.Showing &&
            reviewDetailFragmentUiState.basicInfoUiState is ReviewDetailBasicInfoUiState.Loading &&
            reviewDetailFragmentUiState.supplementaryUiState is ReviewDetailSupplementaryUiState.Loading
        )
    }

    @Test
    fun `reviewDetailFragmentUiState should equal to Showing when _basicInfoUiState is Showing and _supplementaryInfoUiState is Showing`() = runBlockingTest {
        val firstMedia = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first()
        val mediaItemUiModel = VideoMediaItemUiModel(
            id = firstMedia.videoId,
            uri = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewGalleryVideos.first().url,
            mediaNumber = firstMedia.mediaNumber,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResultWithImageAndVideo.detail.mediaCount.toInt(),
            feedbackId = firstMedia.feedbackId
        )
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResultWithImageAndVideo)
        viewModel.updateCurrentMediaItem(mediaItemUiModel)
        val reviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        Assert.assertTrue(reviewDetailFragmentUiState is ReviewDetailFragmentUiState.Showing && reviewDetailFragmentUiState.basicInfoUiState is ReviewDetailBasicInfoUiState.Showing)
        Assert.assertTrue(reviewDetailFragmentUiState is ReviewDetailFragmentUiState.Showing && reviewDetailFragmentUiState.supplementaryUiState is ReviewDetailSupplementaryUiState.Showing)
    }

    @Test
    fun `reviewDetailFragmentUiState should equal to Showing when _basicInfoUiState is Showing, _supplementaryInfoUiState is Hidden`() = runBlockingTest {
        val firstMedia = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first()
        val mediaItemUiModel = VideoMediaItemUiModel(
            id = firstMedia.videoId,
            uri = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewGalleryVideos.first().url,
            mediaNumber = firstMedia.mediaNumber,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResultWithImageAndVideo.detail.mediaCount.toInt(),
            feedbackId = firstMedia.feedbackId
        )
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResultWithImageAndVideo)
        viewModel.updateCurrentMediaItem(mediaItemUiModel)
        val reviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        Assert.assertTrue(
            reviewDetailFragmentUiState is ReviewDetailFragmentUiState.Showing &&
            reviewDetailFragmentUiState.basicInfoUiState is ReviewDetailBasicInfoUiState.Showing &&
            reviewDetailFragmentUiState.supplementaryUiState is ReviewDetailSupplementaryUiState.Showing
        )
    }

    @Test
    fun `expandedReviewDetailBottomSheetUiState should equal to Hidden when _showExpandedReviewDetailBottomSheet is false`() = runBlockingTest {
        viewModel.dismissExpandedReviewDetailBottomSheet()
        val expandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()
        Assert.assertTrue(expandedReviewDetailBottomSheetUiState is ExpandedReviewDetailBottomSheetUiState.Hidden)
    }

    @Test
    fun `expandedReviewDetailBottomSheetUiState should equal to Showing when _showExpandedReviewDetailBottomSheet is true`() = runBlockingTest {
        viewModel.showExpandedReviewDetailBottomSheet()
        val expandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()
        Assert.assertTrue(expandedReviewDetailBottomSheetUiState is ExpandedReviewDetailBottomSheetUiState.Showing)
    }

    @Test
    fun `saveState should put _showExpandedReviewDetailBottomSheet value to given Bundle object`() {
        val outState = mockk<Bundle>(relaxed = true)
        viewModel.saveState(outState)
        verify { outState.putBoolean(ReviewDetailViewModel.SAVED_STATE_SHOW_EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET, any()) }
    }

    @Test
    fun `restoreSavedState should restore _showExpandedReviewDetailBottomSheet latest state saved in given Bundle object`() = runBlockingTest {
        val firstMedia = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first()
        val mediaItemUiModel = VideoMediaItemUiModel(
            id = firstMedia.videoId,
            uri = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewGalleryVideos.first().url,
            mediaNumber = firstMedia.mediaNumber,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResultWithImageAndVideo.detail.mediaCount.toInt(),
            feedbackId = firstMedia.feedbackId
        )
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResultWithImageAndVideo)
        viewModel.updateCurrentMediaItem(mediaItemUiModel)

        viewModel.showExpandedReviewDetailBottomSheet()
        val latestReviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        val latestExpandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()

        // reset state
        viewModel.dismissExpandedReviewDetailBottomSheet()

        // verify state changed
        val resetExpandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()
        Assert.assertNotEquals(latestExpandedReviewDetailBottomSheetUiState, resetExpandedReviewDetailBottomSheetUiState)

        val savedState = mockk<Bundle>(relaxed = true) {
            every {
                getSavedState(ReviewDetailViewModel.SAVED_STATE_SHOW_EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET, false)
            } returns true
        }
        viewModel.restoreSavedState(savedState)
        val currentReviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        val currentExpandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()
        Assert.assertEquals(latestReviewDetailFragmentUiState, currentReviewDetailFragmentUiState)
        Assert.assertEquals(latestExpandedReviewDetailBottomSheetUiState, currentExpandedReviewDetailBottomSheetUiState)
    }

    @Test
    fun `restoreSavedState should not change _showExpandedReviewDetailBottomSheet if no state is saved`() = runBlockingTest {
        val firstMedia = getDetailedReviewMediaResultWithImageAndVideo.reviewMedia.first()
        val mediaItemUiModel = VideoMediaItemUiModel(
            id = firstMedia.videoId,
            uri = getDetailedReviewMediaResultWithImageAndVideo.detail.reviewGalleryVideos.first().url,
            mediaNumber = firstMedia.mediaNumber,
            showSeeMore = false,
            totalMediaCount = getDetailedReviewMediaResultWithImageAndVideo.detail.mediaCount.toInt(),
            feedbackId = firstMedia.feedbackId
        )
        viewModel.updateGetDetailedReviewMediaResult(getDetailedReviewMediaResultWithImageAndVideo)
        viewModel.updateCurrentMediaItem(mediaItemUiModel)

        viewModel.showExpandedReviewDetailBottomSheet()
        val latestReviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        val latestExpandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()

        // reset state
        viewModel.dismissExpandedReviewDetailBottomSheet()

        // verify state changed
        val resetReviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        val resetExpandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()
        Assert.assertNotEquals(latestExpandedReviewDetailBottomSheetUiState, resetExpandedReviewDetailBottomSheetUiState)

        val savedState = mockk<Bundle> {
            every {
                getSavedState(ReviewDetailViewModel.SAVED_STATE_SHOW_EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET, false)
            } returns null
        }
        viewModel.restoreSavedState(savedState)
        val currentReviewDetailFragmentUiState = viewModel.reviewDetailFragmentUiState.first()
        val currentExpandedReviewDetailBottomSheetUiState = viewModel.expandedReviewDetailBottomSheetUiState.first()
        Assert.assertNotEquals(latestExpandedReviewDetailBottomSheetUiState, currentExpandedReviewDetailBottomSheetUiState)
        Assert.assertEquals(latestReviewDetailFragmentUiState, currentReviewDetailFragmentUiState)
        Assert.assertEquals(resetReviewDetailFragmentUiState, currentReviewDetailFragmentUiState)
        Assert.assertEquals(resetExpandedReviewDetailBottomSheetUiState, currentExpandedReviewDetailBottomSheetUiState)
    }

    @Test
    fun `getFeedbackID should return feedback id from current review detail`() = runBlockingTest {
        `currentReviewDetail should not null when currentMediaItem has review detail`()
        val feedbackID = viewModel.getFeedbackID()
        Assert.assertEquals(viewModel.currentReviewDetail.first()!!.feedbackID, feedbackID)
    }

    @Test
    fun `getInvertedLikeStatus should return inverted isLiked status from current review detail`() = runBlockingTest {
        `currentReviewDetail should not null when currentMediaItem has review detail`()
        val currentLikeStatus = if (viewModel.currentReviewDetail.first()!!.basicInfoUiModel.isLiked) ToggleLikeReviewUseCase.LIKED else ToggleLikeReviewUseCase.NEUTRAL
        val invertedLikeStatus = viewModel.getInvertedLikeStatus()
        Assert.assertNotEquals(currentLikeStatus, invertedLikeStatus)
    }
}