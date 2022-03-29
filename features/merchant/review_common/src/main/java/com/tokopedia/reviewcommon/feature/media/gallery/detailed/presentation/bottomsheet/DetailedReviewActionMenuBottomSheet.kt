package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.BottomsheetReviewActionMenuBinding
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.adapter.typefactory.DetailedReviewActionAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.DetailedReviewActionMenuBottomSheetUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DetailedReviewActionMenuBottomSheet: BottomSheetUnify(), CoroutineScope {

    companion object {
        const val TAG = "DetailedReviewActionMenuBottomSheet"
        const val REPORT_REVIEW_ACTIVITY_CODE = 200
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), detailedReviewMediaGalleryViewModelFactory).get(
            SharedReviewMediaGalleryViewModel::class.java
        )
    }
    private val daggerHandler = DaggerHandler()
    private val detailedReviewActionMenuListener = DetailedReviewActionMenuListener()
    private val adapter = BaseAdapter(DetailedReviewActionAdapterTypeFactory(detailedReviewActionMenuListener))

    private var supervisorJob = SupervisorJob()
    private var uiStateCollectorJob: Job? = null
    private var uiState: DetailedReviewActionMenuBottomSheetUiState? = null

    private var binding by viewBinding(BottomsheetReviewActionMenuBinding::bind)

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + supervisorJob

    init {
        setOnDismissListener {
            sharedReviewMediaGalleryViewModel.dismissDetailedReviewActionMenuBottomSheet()
        }
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
        binding = BottomsheetReviewActionMenuBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
    }

    override fun onResume() {
        super.onResume()
        collectUiState()
    }

    override fun onPause() {
        super.onPause()
        cancelUiStateCollector()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REPORT_REVIEW_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            binding?.root?.let { view ->
                Toaster.build(
                    view,
                    getString(R.string.review_action_success_report),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL
                ).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupLayout() {
        binding?.rvDetailedReviewActionMenu?.adapter = adapter
    }

    private fun collectUiState() {
        uiStateCollectorJob = uiStateCollectorJob?.takeIf { !it.isCompleted } ?: launch {
            sharedReviewMediaGalleryViewModel.detailedReviewActionMenuBottomSheetUiState.collectLatest {
                uiState = it
                updateUi(it)
            }
        }
    }

    private fun updateUi(uiState: DetailedReviewActionMenuBottomSheetUiState) {
        when(uiState) {
            is DetailedReviewActionMenuBottomSheetUiState.Hidden -> dismiss()
            is DetailedReviewActionMenuBottomSheetUiState.Showing -> {
                adapter.setElements(uiState.items)
            }
        }
    }

    private fun cancelUiStateCollector() {
        uiStateCollectorJob?.cancel()
    }

    private fun goToReportReview(reviewId: String, shopId: String) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT
        )
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId)
        startActivityForResult(intent, REPORT_REVIEW_ACTIVITY_CODE)
    }

    private inner class DaggerHandler {
        fun initInjector() {
            DetailedReviewMediaGalleryComponentInstance
                .getInstance(requireContext())
                .inject(this@DetailedReviewActionMenuBottomSheet)
        }
    }

    private inner class DetailedReviewActionMenuListener: DetailedReviewActionAdapterTypeFactory.Listener {
        override fun onReviewActionMenuClicked(id: Int) {
            when(id) {
                R.string.review_action_menu_report -> {
                    val currentUiState = uiState
                    if (currentUiState is DetailedReviewActionMenuBottomSheetUiState.Showing) {
                        goToReportReview(currentUiState.feedbackID, currentUiState.shopID)
                    }
                }
            }
        }
    }
}