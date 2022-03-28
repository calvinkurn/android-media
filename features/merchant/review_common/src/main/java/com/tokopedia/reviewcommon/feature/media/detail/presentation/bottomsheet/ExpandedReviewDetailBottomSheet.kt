package com.tokopedia.reviewcommon.feature.media.detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.reviewcommon.databinding.BottomsheetExpandedReviewDetailCommonBinding
import com.tokopedia.reviewcommon.feature.media.detail.di.ReviewDetailComponentInstance
import com.tokopedia.reviewcommon.feature.media.detail.di.qualifier.ReviewDetailViewModelFactory
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ExpandedReviewDetailBottomSheetUiState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.viewmodel.ReviewDetailViewModel
import com.tokopedia.reviewcommon.feature.media.detail.presentation.widget.ReviewDetailBasicInfo
import com.tokopedia.reviewcommon.feature.media.detail.presentation.widget.ReviewDetailSupplementaryInfo
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ExpandedReviewDetailBottomSheet: BottomSheetUnify(), CoroutineScope {

    companion object {
        const val TAG = "ExpandedReviewDetailBottomSheet"
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    @ReviewDetailViewModelFactory
    lateinit var reviewDetailViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(BottomsheetExpandedReviewDetailCommonBinding::bind)

    private var basicInfoListener: ReviewDetailBasicInfo.Listener? = null
    private var supplementaryInfoListener: ReviewDetailSupplementaryInfo.Listener? = null

    private var supervisorJob = SupervisorJob()
    private var uiStateCollectorJob: Job? = null

    private val daggerHandler = DaggerHandler()

    private val reviewDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            requireActivity(),
            reviewDetailViewModelFactory
        ).get(ReviewDetailViewModel::class.java)
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

    override fun onResume() {
        super.onResume()
        collectUiState()
    }

    override fun onPause() {
        super.onPause()
        cancelUiStateCollector()
    }

    private fun collectUiState() {
        uiStateCollectorJob = uiStateCollectorJob?.takeIf { !it.isCompleted } ?: launch {
            reviewDetailViewModel.expandedReviewDetailBottomSheetUiState.collectLatest {
                updateUi(it)
            }
        }
    }

    private fun cancelUiStateCollector() {
        uiStateCollectorJob?.cancel()
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