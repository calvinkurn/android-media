package com.tokopedia.review.feature.media.detail.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.FragmentReviewDetailCommonBinding
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTracker
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTrackerConstant
import com.tokopedia.review.feature.media.detail.di.ReviewDetailComponentInstance
import com.tokopedia.review.feature.media.detail.di.qualifier.ReviewDetailViewModelFactory
import com.tokopedia.review.feature.media.detail.presentation.bottomsheet.ExpandedReviewDetailBottomSheet
import com.tokopedia.review.feature.media.detail.presentation.uistate.ExpandedReviewDetailBottomSheetUiState
import com.tokopedia.review.feature.media.detail.presentation.uistate.ReviewDetailFragmentUiState
import com.tokopedia.review.feature.media.detail.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.review.feature.media.detail.presentation.widget.ReviewDetailBasicInfo
import com.tokopedia.review.feature.media.detail.presentation.widget.ReviewDetailSupplementaryInfo
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewDetailFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
        private const val PARAM_PRODUCT_ID = "productId"

        const val TAG = "ReviewDetailFragment"
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    @ReviewDetailViewModelFactory
    lateinit var reviewDetailViewModelFactory: ViewModelProvider.Factory

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(FragmentReviewDetailCommonBinding::bind)

    private val bottomSheetHandler by lazy(LazyThreadSafetyMode.NONE) { BottomSheetHandler() }
    private val reviewDetailBasicInfoListener = ReviewDetailBasicInfoListener()
    private val reviewDetailSupplementaryInfoListener = ReviewDetailSupplementaryInfoListener()

    private val reviewDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            requireActivity(),
            reviewDetailViewModelFactory
        ).get(ReviewDetailViewModel::class.java)
    }
    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), detailedReviewMediaGalleryViewModelFactory).get(
            SharedReviewMediaGalleryViewModel::class.java
        )
    }

    private val supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + supervisorJob

    override fun getScreenName(): String {
        return ReviewDetailFragment::class.simpleName.orEmpty()
    }

    override fun initInjector() {
        ReviewDetailComponentInstance.getInstance(requireContext()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUiState(savedInstanceState)
        return FragmentReviewDetailCommonBinding.inflate(
            inflater,
            container,
            false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        initUiStateCollectors()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        reviewDetailViewModel.saveState(outState)
    }

    private fun initUiState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            reviewDetailViewModel.restoreSavedState(savedInstanceState)
        }
    }

    private fun setupListeners() {
        reviewDetailBasicInfoListener.attachListener()
        reviewDetailSupplementaryInfoListener.attachListener()
    }

    private fun initUiStateCollectors() {
        collectReviewDetailFragmentUiStateUpdate()
        collectExpandedReviewDetailBottomSheetUiStateUpdate()
        collectDetailedReviewMediaResultUpdate()
        collectCurrentMediaItemUpdate()
        collectOrientationUiStateUpdate()
        collectOverlayVisibilityUpdate()
        collectCurrentReviewDetailUpdate()
    }

    private fun collectReviewDetailFragmentUiStateUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewDetailViewModel.reviewDetailFragmentUiState, ::updateUi)
    }

    private fun collectExpandedReviewDetailBottomSheetUiStateUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewDetailViewModel.expandedReviewDetailBottomSheetUiState) {
            if (it is ExpandedReviewDetailBottomSheetUiState.Showing) {
                bottomSheetHandler.showExpandedReviewDetailBottomSheet()
            }
        }
    }

    private fun collectDetailedReviewMediaResultUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.detailedReviewMediaResult) {
            reviewDetailViewModel.updateGetDetailedReviewMediaResult(it)
        }
    }

    private fun collectCurrentMediaItemUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.currentMediaItem) {
            reviewDetailViewModel.updateCurrentMediaItem(it)
        }
    }

    private fun collectOrientationUiStateUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.orientationUiState) {
            reviewDetailViewModel.updateCurrentOrientation(it)
        }
    }

    private fun collectOverlayVisibilityUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.overlayVisibility) {
            reviewDetailViewModel.updateCurrentOverlayVisibility(it)
        }
    }

    private fun collectCurrentReviewDetailUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewDetailViewModel.currentReviewDetail) {
            sharedReviewMediaGalleryViewModel.updateReviewDetailItem(it)
        }
    }

    private fun updateUi(uiState: ReviewDetailFragmentUiState) {
        when (uiState) {
            is ReviewDetailFragmentUiState.Hidden -> {
                binding?.layoutReviewMediaGalleryReviewDetail?.gone()
            }
            is ReviewDetailFragmentUiState.Showing -> {
                binding?.reviewDetailBasicInfo?.apply {
                    hideReviewerInfo()
                    updateUi(
                        uiState.basicInfoUiState,
                        ReviewDetailBasicInfo.Source.REVIEW_DETAIL_FRAGMENT
                    )
                }
                binding?.reviewDetailSupplementaryInfo?.updateUi(
                    uiState.supplementaryUiState,
                    ReviewDetailSupplementaryInfo.Source.REVIEW_DETAIL_FRAGMENT
                )
                binding?.layoutReviewMediaGalleryReviewDetail?.show()
            }
        }
    }

    private fun getCredibilitySource(): String {
        return if (sharedReviewMediaGalleryViewModel.isFromGallery()) {
            ReviewApplinkConst.REVIEW_CREDIBILITY_SOURCE_REVIEW_GALLERY
        } else {
            ReviewApplinkConst.REVIEW_CREDIBILITY_SOURCE_REVIEW_READING_IMAGE_PREVIEW
        }
    }

    private inner class BottomSheetHandler {
        private var expandedReviewDetailBottomSheet: ExpandedReviewDetailBottomSheet? = null

        private fun createExpandedReviewDetailBottomSheet(): ExpandedReviewDetailBottomSheet {
            return ExpandedReviewDetailBottomSheet()
        }

        private fun getAddedExpandedReviewDetailBottomSheet(): ExpandedReviewDetailBottomSheet? {
            return childFragmentManager.findFragmentByTag(
                ExpandedReviewDetailBottomSheet.TAG
            ) as? ExpandedReviewDetailBottomSheet
        }

        private fun getExpandedReviewDetailBottomSheet(): ExpandedReviewDetailBottomSheet {
            return expandedReviewDetailBottomSheet ?: getAddedExpandedReviewDetailBottomSheet()
            ?: createExpandedReviewDetailBottomSheet()
        }

        fun showExpandedReviewDetailBottomSheet() {
            getExpandedReviewDetailBottomSheet().run {
                setListener(reviewDetailBasicInfoListener, reviewDetailSupplementaryInfoListener)
                if (!isAdded) {
                    show(this@ReviewDetailFragment.childFragmentManager, ExpandedReviewDetailBottomSheet.TAG)
                }
            }
        }
    }

    private inner class ReviewDetailBasicInfoListener: ReviewDetailBasicInfo.Listener {
        override fun onToggleLikeClicked() {
            val feedbackID = reviewDetailViewModel.getFeedbackID()
            val invertedLikeStatus = reviewDetailViewModel.getInvertedLikeStatus()
            if (feedbackID != null && invertedLikeStatus != null) {
                if (sharedReviewMediaGalleryViewModel.isProductReview()) {
                    ReviewDetailTracker.trackOnLikeReviewClicked(
                        feedbackId = feedbackID,
                        isLiked = invertedLikeStatus == ToggleLikeReviewUseCase.LIKED,
                        productId = sharedReviewMediaGalleryViewModel.getProductId(),
                        isFromGallery = sharedReviewMediaGalleryViewModel.isFromGallery()
                    )
                } else {
                    ReviewDetailTracker.trackOnShopReviewLikeReviewClicked(
                        feedbackId = feedbackID,
                        isLiked = invertedLikeStatus == ToggleLikeReviewUseCase.LIKED,
                        shopId = sharedReviewMediaGalleryViewModel.getShopId()
                    )
                }
                sharedReviewMediaGalleryViewModel.requestToggleLike(feedbackID, invertedLikeStatus)
            }
        }

        override fun onGoToCredibilityClicked(
            userId: String,
            reviewerStatsSummary: String,
            reviewerLabel: String
        ) {
            val routed = RouteManager.route(
                context, Uri.parse(
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                        userId,
                        getCredibilitySource()
                    )
                ).buildUpon().appendQueryParameter(
                    PARAM_PRODUCT_ID, sharedReviewMediaGalleryViewModel.getProductId()
                ).build().toString()
            )
            if (routed) {
                ReviewDetailTracker.trackClickReviewerName(
                    sharedReviewMediaGalleryViewModel.isFromGallery(),
                    reviewDetailViewModel.getFeedbackID().orEmpty(),
                    userId,
                    reviewerStatsSummary,
                    sharedReviewMediaGalleryViewModel.getProductId(),
                    sharedReviewMediaGalleryViewModel.getUserID(),
                    reviewerLabel,
                    ReviewDetailTrackerConstant.TRACKER_ID_CLICK_REVIEWER_NAME_FROM_EXPANDED_REVIEW_DETAIL
                )
            }
        }

        fun attachListener() {
            binding?.reviewDetailBasicInfo?.setListener(this)
        }
    }

    private inner class ReviewDetailSupplementaryInfoListener: ReviewDetailSupplementaryInfo.Listener {
        override fun onDescriptionSeeMoreClicked() {
            if (sharedReviewMediaGalleryViewModel.isProductReview()) {
                ReviewDetailTracker.trackOnSeeAllClicked(
                    reviewDetailViewModel.getFeedbackID().orEmpty(),
                    sharedReviewMediaGalleryViewModel.getProductId(),
                    sharedReviewMediaGalleryViewModel.isFromGallery()
                )
            } else {
                ReviewDetailTracker.trackOnShopReviewSeeAllClicked(
                    reviewDetailViewModel.getFeedbackID().orEmpty(),
                    sharedReviewMediaGalleryViewModel.getShopId()
                )
            }
            reviewDetailViewModel.showExpandedReviewDetailBottomSheet()
        }

        fun attachListener() {
            binding?.reviewDetailSupplementaryInfo?.setListener(this)
        }
    }
}