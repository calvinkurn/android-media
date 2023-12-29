package com.tokopedia.review.feature.media.detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.BottomsheetExpandedReviewDetailCommonBinding
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTracker
import com.tokopedia.review.feature.media.detail.di.ReviewDetailComponentInstance
import com.tokopedia.review.feature.media.detail.di.qualifier.ReviewDetailViewModelFactory
import com.tokopedia.review.feature.media.detail.presentation.uistate.ExpandedReviewDetailBottomSheetUiState
import com.tokopedia.review.feature.media.detail.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.review.feature.media.detail.presentation.widget.ReviewDetailBasicInfo
import com.tokopedia.review.feature.media.detail.presentation.widget.ReviewDetailSupplementaryInfo
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ExpandedReviewDetailBottomSheet: BottomSheetUnify(), CoroutineScope {

    companion object {
        const val TAG = "ExpandedReviewDetailBottomSheet"
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var reviewDetailTracker: ReviewDetailTracker

    @Inject
    @ReviewDetailViewModelFactory
    lateinit var reviewDetailViewModelFactory: ViewModelProvider.Factory

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(BottomsheetExpandedReviewDetailCommonBinding::bind)

    private var basicInfoListener: ReviewDetailBasicInfo.Listener? = null
    private var supplementaryInfoListener: ReviewDetailSupplementaryInfo.Listener? = null

    private var supervisorJob = SupervisorJob()

    private val daggerHandler = DaggerHandler()

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

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + supervisorJob

    init {
        isFullpage = true
        clearContentPadding = true
        isDragable = true
        showCloseIcon = false
        showKnob = true
        showHeader = false
        isHideable = true
        setOnDismissListener { reviewDetailViewModel.dismissExpandedReviewDetailBottomSheet() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        daggerHandler.initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetExpandedReviewDetailCommonBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiStateCollectors()

        reviewDetailTracker.trackImpressOnSeeMoreBottomSheet(
            loggedInUserId = sharedReviewMediaGalleryViewModel.getUserID(),
            feedbackId = reviewDetailViewModel.getFeedbackID().orEmpty(),
            productId = sharedReviewMediaGalleryViewModel.getProductId(),
            reviewUserId = sharedReviewMediaGalleryViewModel.getReviewUserID(),
            isReviewOwner = sharedReviewMediaGalleryViewModel.isReviewOwner,
        )
    }

    private fun initUiStateCollectors() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewDetailViewModel.expandedReviewDetailBottomSheetUiState, ::updateUi)
    }

    private fun updateUi(uiState: ExpandedReviewDetailBottomSheetUiState) {
        binding?.run {
            when(uiState) {
                is ExpandedReviewDetailBottomSheetUiState.Hidden -> {
                    dismiss()
                }
                is ExpandedReviewDetailBottomSheetUiState.Showing -> {
                    reviewDetailBasicInfo.updateUi(
                        uiState.basicInfoUiState,
                        ReviewDetailBasicInfo.Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET
                    )
                    reviewDetailSupplementaryInfo.updateUi(
                        uiState.supplementaryUiState,
                        ReviewDetailSupplementaryInfo.Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET
                    )
                    basicInfoListener?.let { reviewDetailBasicInfo.setListener(it) }
                    supplementaryInfoListener?.let { reviewDetailSupplementaryInfo.setListener(it) }
                }
            }
        }
    }

    fun setListener(
        basicInfoListener: ReviewDetailBasicInfo.Listener,
        supplementaryInfoListener: ReviewDetailSupplementaryInfo.Listener
    ) {
        this.basicInfoListener = basicInfoListener
        this.supplementaryInfoListener = supplementaryInfoListener
    }

    private inner class DaggerHandler {
        fun initInjector() {
            ReviewDetailComponentInstance
                .getInstance(requireContext())
                .inject(this@ExpandedReviewDetailBottomSheet)
        }
    }
}
