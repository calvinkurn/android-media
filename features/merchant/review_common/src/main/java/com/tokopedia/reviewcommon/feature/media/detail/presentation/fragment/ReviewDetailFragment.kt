package com.tokopedia.reviewcommon.feature.media.detail.presentation.fragment

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
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewcommon.databinding.FragmentReviewDetailCommonBinding
import com.tokopedia.reviewcommon.feature.media.detail.di.ReviewDetailComponentInstance
import com.tokopedia.reviewcommon.feature.media.detail.di.qualifier.ReviewDetailViewModelFactory
import com.tokopedia.reviewcommon.feature.media.detail.presentation.bottomsheet.ExpandedReviewDetailBottomSheet
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ExpandedReviewDetailBottomSheetUiState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ReviewDetailFragmentUiState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.reviewcommon.feature.media.detail.presentation.widget.ReviewDetailBasicInfo
import com.tokopedia.reviewcommon.feature.media.detail.presentation.widget.ReviewDetailSupplementaryInfo
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.activity.DetailedReviewMediaGalleryActivity.Companion.GALLERY_SOURCE_CREDIBILITY_SOURCE
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.activity.DetailedReviewMediaGalleryActivity.Companion.READING_IMAGE_PREVIEW_CREDIBILITY_SOURCE
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewDetailFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
        private const val PARAM_PRODUCT_ID = "productId"
        private const val ARG_IS_FROM_GALLERY = "argIsFromGallery"
        const val TAG = "ReviewDetailFragment"

        fun createInstance(isFromGallery: Boolean): ReviewDetailFragment {
            return ReviewDetailFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_FROM_GALLERY, isFromGallery)
                }
            }
        }
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
    private var reviewDetailFragmentUiStateCollectorJob: Job? = null
    private var expandedReviewDetailBottomSheetUiStateCollectorJob: Job? = null
    private var getDetailedReviewMediaResultCollectorJob: Job? = null
    private var currentMediaItemCollectorJob: Job? = null
    private var orientationCollectorJob: Job? = null
    private var overlayVisibilityCollectorJob: Job? = null
    private var currentReviewDetailCollectorJob: Job? = null

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
    }

    override fun onResume() {
        super.onResume()
        collectUIState()
    }

    override fun onPause() {
        super.onPause()
        cancelUIStateCollector()
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

    private fun collectUIState() {
        reviewDetailFragmentUiStateCollectorJob = reviewDetailFragmentUiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            reviewDetailViewModel.reviewDetailFragmentUiState.collectLatest {
                updateUi(it)
            }
        }
        expandedReviewDetailBottomSheetUiStateCollectorJob = expandedReviewDetailBottomSheetUiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            reviewDetailViewModel.expandedReviewDetailBottomSheetUiState.collectLatest {
                if (it is ExpandedReviewDetailBottomSheetUiState.Showing) {
                    bottomSheetHandler.showExpandedReviewDetailBottomSheet()
                }
            }
        }
        getDetailedReviewMediaResultCollectorJob = getDetailedReviewMediaResultCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.detailedReviewMediaResult.collectLatest {
                reviewDetailViewModel.updateGetDetailedReviewMediaResult(it)
            }
        }
        currentMediaItemCollectorJob = currentMediaItemCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.currentMediaItem.collectLatest {
                reviewDetailViewModel.updateCurrentMediaItem(it)
            }
        }
        orientationCollectorJob = orientationCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.orientationUiState.collectLatest {
                reviewDetailViewModel.updateCurrentOrientation(it)
            }
        }
        overlayVisibilityCollectorJob = overlayVisibilityCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.overlayVisibility.collectLatest {
                reviewDetailViewModel.updateCurrentOverlayVisibility(it)
            }
        }
        currentReviewDetailCollectorJob = currentReviewDetailCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            reviewDetailViewModel.currentReviewDetail.collectLatest {
                sharedReviewMediaGalleryViewModel.updateReviewDetailItem(it)
            }
        }
    }

    private fun cancelUIStateCollector() {
        reviewDetailFragmentUiStateCollectorJob?.cancel()
        expandedReviewDetailBottomSheetUiStateCollectorJob?.cancel()
        getDetailedReviewMediaResultCollectorJob?.cancel()
        currentMediaItemCollectorJob?.cancel()
        orientationCollectorJob?.cancel()
        overlayVisibilityCollectorJob?.cancel()
    }

    private fun updateUi(uiState: ReviewDetailFragmentUiState) {
        when (uiState) {
            is ReviewDetailFragmentUiState.Hidden -> {
                binding?.layoutReviewMediaGalleryReviewDetail?.gone()
            }
            is ReviewDetailFragmentUiState.Showing -> {
                binding?.reviewDetailBasicInfo?.updateUi(
                    uiState.basicInfoUiState,
                    ReviewDetailBasicInfo.Source.REVIEW_DETAIL_FRAGMENT
                )
                binding?.reviewDetailSupplementaryInfo?.updateUi(
                    uiState.supplementaryUiState,
                    ReviewDetailSupplementaryInfo.Source.REVIEW_DETAIL_FRAGMENT
                )
                binding?.layoutReviewMediaGalleryReviewDetail?.show()
            }
        }
    }

    private fun getCredibilitySource(): String {
        return if (isFromGallery()) GALLERY_SOURCE_CREDIBILITY_SOURCE
        else READING_IMAGE_PREVIEW_CREDIBILITY_SOURCE
    }

    private fun isFromGallery(): Boolean {
        return arguments?.getBoolean(ARG_IS_FROM_GALLERY).orFalse()
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
        override fun onToggleExpandClicked() {
            reviewDetailViewModel.toggleExpand()
        }

        override fun onToggleLikeClicked() {
            val feedbackID = reviewDetailViewModel.getFeedbackID()
            val invertedLikeStatus = reviewDetailViewModel.getInvertedLikeStatus()
            if (feedbackID != null && invertedLikeStatus != null) {
                sharedReviewMediaGalleryViewModel.requestToggleLike(feedbackID, invertedLikeStatus)
            }
        }

        override fun onGoToCredibilityClicked(userId: String) {
            RouteManager.route(
                context,
                Uri.parse(
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                        userId,
                        getCredibilitySource()
                    )
                ).buildUpon()
                    .appendQueryParameter(
                        PARAM_PRODUCT_ID, sharedReviewMediaGalleryViewModel.getProductId()
                    ).build()
                    .toString()
            )
        }

        fun attachListener() {
            binding?.reviewDetailBasicInfo?.setListener(this)
        }
    }

    private inner class ReviewDetailSupplementaryInfoListener: ReviewDetailSupplementaryInfo.Listener {
        override fun onDescriptionSeeMoreClicked() {
            reviewDetailViewModel.showExpandedReviewDetailBottomSheet()
        }

        fun attachListener() {
            binding?.reviewDetailSupplementaryInfo?.setListener(this)
        }
    }
}