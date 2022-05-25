package com.tokopedia.review.feature.media.gallery.detailed.presentation.bottomsheet

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
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.BottomsheetReviewActionMenuBinding
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.adapter.typefactory.DetailedReviewActionAdapterTypeFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.ActionMenuBottomSheetUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ActionMenuBottomSheet: BottomSheetUnify(), CoroutineScope {

    companion object {
        const val TAG = "ActionMenuBottomSheet"
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
    private var uiState: ActionMenuBottomSheetUiState? = null

    private var binding by viewBinding(BottomsheetReviewActionMenuBinding::bind)

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + supervisorJob

    init {
        setOnDismissListener {
            sharedReviewMediaGalleryViewModel.dismissActionMenuBottomSheet()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        daggerHandler.initInjector()
        super.onCreate(savedInstanceState)
        initUiStateCollector()
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

    private fun initUiStateCollector() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.actionMenuBottomSheetUiState) {
            uiState = it
            updateUi(it)
        }
    }

    private fun updateUi(uiState: ActionMenuBottomSheetUiState) {
        when(uiState) {
            is ActionMenuBottomSheetUiState.Hidden -> dismiss()
            is ActionMenuBottomSheetUiState.Showing -> {
                adapter.setElements(uiState.items)
            }
        }
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
                .inject(this@ActionMenuBottomSheet)
        }
    }

    private inner class DetailedReviewActionMenuListener: DetailedReviewActionAdapterTypeFactory.Listener {
        override fun onReviewActionMenuClicked(id: Int) {
            when(id) {
                R.string.review_action_menu_report -> {
                    val currentUiState = uiState
                    if (currentUiState is ActionMenuBottomSheetUiState.Showing) {
                        goToReportReview(currentUiState.feedbackID, currentUiState.shopID)
                    }
                }
            }
        }
    }
}