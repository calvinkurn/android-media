package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.review.R
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.common.extension.collectWhenResumed
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.databinding.BottomsheetCreateReviewBinding
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTracking
import com.tokopedia.review.feature.createreputation.analytics.CreateReviewTrackingConstants
import com.tokopedia.review.feature.createreputation.di.CreateReviewDaggerInstance
import com.tokopedia.review.feature.createreputation.model.ProductrevGetPostSubmitBottomSheetResponse
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet.ArgumentHandler.getProductIdFromArgument
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet.ArgumentHandler.getRatingFromArgument
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet.ArgumentHandler.getReputationIdFromArgument
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet.ArgumentHandler.getUtmSourceFromArgument
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet.ArgumentHandler.putArguments
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewDialogType
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewTextAreaTextUiModel
import com.tokopedia.review.feature.createreputation.presentation.uimodel.PostSubmitUiState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewAnonymousInfoBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewIncentiveBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewBadRatingCategoryViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.SubmitReviewRequestErrorState
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewAnonymous
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewMediaPicker
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewSubmitButton
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTextArea
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTicker
import com.tokopedia.review.feature.ovoincentive.data.ThankYouBottomSheetTrackerData
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet.IncentiveOvoBottomSheet
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoBottomSheetUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CreateReviewBottomSheet : BottomSheetUnify() {
    companion object {
        private const val TEXT_AREA_MAX_MIN_LINE = 4
        private const val MAX_VIDEO_COUNT = 1
        private const val MAX_IMAGE_COUNT = 4
        private const val MAX_VIDEO_SIZE_BYTE = 250L * 1024L * 1024L
        private const val SHOW_TEXT_AREA_AUTO_SCROLL_DELAY = 500L

        const val TEMPLATES_ROW_COUNT = 2
        const val BAD_RATING_OTHER_ID = "6"

        fun createInstance(
            rating: Int,
            productId: String,
            reputationId: String,
            utmSource: String
        ): CreateReviewBottomSheet {
            return CreateReviewBottomSheet().apply {
                putArguments(rating, productId, reputationId, utmSource)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var trackingQueue: TrackingQueue

    private var binding by viewBinding(BottomsheetCreateReviewBinding::bind)
    private var isUserInitiateDismiss: Boolean = false

    private val dialogHandler by lazy(LazyThreadSafetyMode.NONE) { DialogHandler() }
    private val activityResultHandler by lazy(LazyThreadSafetyMode.NONE) { ActivityResultHandler() }
    private val bottomSheetHandler by lazy(LazyThreadSafetyMode.NONE) { BottomSheetHandler() }
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), viewModelFactory).get(CreateReviewViewModel::class.java)
    }

    private val baseCreateReviewCustomViewListener = BaseCreateReviewCustomViewListener()
    private val anonymousListener = AnonymousListener()
    private val badRatingCategoryListener = BadRatingCategoryListener()
    private val createReviewAnonymousInfoBottomSheetListener = CreateReviewAnonymousInfoBottomSheetListener()
    private val incentiveOvoBottomSheetListener = IncentiveOvoBottomSheetListener()
    private val mediaPickerListener = MediaPickerListener()
    private val submitButtonListener = SubmitButtonListener()
    private val textAreaListener = TextAreaListener()
    private val templateListener = TemplateListener()
    private val tickerListener = TickerListener()
    private val uiStateHandler = UiStateHandler()
    private val daggerHandler = DaggerHandler()
    private val trackingHandler = TrackingHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        daggerHandler.initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        binding = BottomsheetCreateReviewBinding.inflate(inflater).also { setChild(it.root) }
        uiStateHandler.initUiState(savedInstanceState)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListeners()
        setupDismissBehavior()
    }

    override fun onPause() {
        super.onPause()
        bottomSheetHandler.dismissOvoIncentiveBottomSheet()
        bottomSheetHandler.dismissCreateReviewTextAreaBottomSheet()
        bottomSheetHandler.dismissCreateReviewPostSubmitBottomSheet()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityResultHandler.handleResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        uiStateHandler.saveUiState(outState)
    }

    private fun setupLayout() {
        setupCreateReviewTextArea()
        setupCreateReviewTemplate()
    }

    private fun setupListeners() {
        setShowListener {
            trackingHandler.trackOpenScreen()
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            isCancelable = false
            dialog?.setCanceledOnTouchOutside(false)
            this.dialog?.window?.decorView?.findViewById<View>(
                com.google.android.material.R.id.touch_outside
            )?.setOnClickListener { handleDismiss() }
        }
        setCloseClickListener {
            handleDismiss()
        }
        setupInsetListener()
        baseCreateReviewCustomViewListener.attachListener()
        tickerListener.attachListener()
        badRatingCategoryListener.attachListener()
        textAreaListener.attachListener()
        templateListener.attachListener()
        mediaPickerListener.attachListener()
        templateListener.attachListener()
        anonymousListener.attachListener()
        submitButtonListener.attachListener()
    }

    private fun setupInsetListener() {
        dialog?.window?.decorView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                viewModel.setBottomSheetBottomInset(insets.systemWindowInsetBottom)
                insets
            }
        }
    }

    private suspend fun shouldScheduleScrollToTextArea(bottomInset: Int) {
        if (bottomInset.isMoreThanZero()) {
            delay(SHOW_TEXT_AREA_AUTO_SCROLL_DELAY)
            scrollToTextArea()
        }
    }

    private fun scrollToTextArea() {
        binding?.let {
            val mediaPickerBottom = it.reviewFormTextArea.bottom + CreateReviewTextArea.PADDING_BOTTOM.toPx()
            val scrollViewHeight = it.reviewFormScrollView.height
            val scrollX = Int.ZERO
            val scrollY = mediaPickerBottom - scrollViewHeight
            it.reviewFormScrollView.smoothScrollTo(scrollX, scrollY)
        }
    }

    private fun handleDismiss() {
        if (viewModel.hasIncentive()) {
            dialogHandler.showIncentivesExitWarningDialog()
            return
        }
        isUserInitiateDismiss = true
        dismiss()
    }

    private fun finishIfRoot(
        success: Boolean,
        message: String,
        feedbackId: String
    ) {
        activity?.run {
            if (isTaskRoot) {
                val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                if (success) {
                    setResult(Activity.RESULT_OK, intent)
                }
                startActivity(intent)
            } else {
                val intent = Intent()
                intent.putExtra(ReviewInboxConstants.CREATE_REVIEW_MESSAGE, message)
                if (success) {
                    intent.putExtra(ReputationCommonConstants.ARGS_FEEDBACK_ID, feedbackId)
                    intent.putExtra(ReputationCommonConstants.ARGS_RATING, viewModel.getRating())
                    intent.putExtra(ReputationCommonConstants.ARGS_PRODUCT_ID, viewModel.getProductId())
                    intent.putExtra(ReputationCommonConstants.ARGS_REPUTATION_ID, viewModel.getReputationId())
                    intent.putExtra(
                        ReputationCommonConstants.ARGS_REVIEW_STATE,
                        ReputationCommonConstants.REVIEWED
                    )
                    setResult(Activity.RESULT_OK, intent)
                } else {
                    intent.putExtra(
                        ReputationCommonConstants.ARGS_REVIEW_STATE,
                        ReputationCommonConstants.INVALID_TO_REVIEW
                    )
                    setResult(Activity.RESULT_FIRST_USER, intent)
                }
            }
            dismiss()
            finish()
        }
    }

    private fun setupDismissBehavior() {
        setOnDismissListener {
            if (isUserInitiateDismiss) {
                trackingHandler.trackDismissForm()
                if (activity?.isTaskRoot == true) {
                    activity?.finish()
                    RouteManager.route(context, ApplinkConst.HOME)
                    return@setOnDismissListener
                }
                activity?.finish()
            }
        }
    }

    private fun showThankYouToaster(data: ProductrevGetPostSubmitBottomSheetResponse?) {
        finishIfRoot(
            success = true,
            message = data?.getToasterText(viewModel.getUserName()) ?: getString(
                R.string.review_create_success_toaster,
                viewModel.getUserName()
            ),
            feedbackId = viewModel.getFeedbackId()
        )
    }

    private fun getThankYouBottomSheetTrackerData(): ThankYouBottomSheetTrackerData {
        return ThankYouBottomSheetTrackerData(
            viewModel.getReputationId(),
            viewModel.getOrderId(),
            viewModel.getProductId(),
            viewModel.getUserId(),
            viewModel.getFeedbackId()
        )
    }

    private fun setupCreateReviewTextArea() {
        binding?.reviewFormTextArea?.setMinLine(TEXT_AREA_MAX_MIN_LINE)
        binding?.reviewFormTextArea?.setMaxLine(TEXT_AREA_MAX_MIN_LINE)
    }

    private fun setupCreateReviewTemplate() {
        binding?.reviewFormTemplates?.setMargins(top = 6.toPx())
    }

    private object ArgumentHandler {

        private const val ARG_RATING = "argRating"
        private const val ARG_PRODUCT_ID = "argProductId"
        private const val ARG_REPUTATION_ID = "argReputationId"
        private const val ARG_UTM_SOURCE = "argUtmSource"

        fun CreateReviewBottomSheet.putArguments(
            rating: Int,
            productId: String,
            reputationId: String,
            utmSource: String
        ) {
            val fragmentArguments = Bundle()
            fragmentArguments.putInt(ARG_RATING, rating)
            fragmentArguments.putString(ARG_PRODUCT_ID, productId)
            fragmentArguments.putString(ARG_REPUTATION_ID, reputationId)
            fragmentArguments.putString(ARG_UTM_SOURCE, utmSource)
            arguments = fragmentArguments
        }

        fun CreateReviewBottomSheet.getRatingFromArgument(): Int {
            return arguments?.getInt(ARG_RATING).orZero()
        }

        fun CreateReviewBottomSheet.getProductIdFromArgument(): String {
            return arguments?.getString(ARG_PRODUCT_ID).orEmpty()
        }

        fun CreateReviewBottomSheet.getReputationIdFromArgument(): String {
            return arguments?.getString(ARG_REPUTATION_ID).orEmpty()
        }

        fun CreateReviewBottomSheet.getUtmSourceFromArgument(): String {
            return arguments?.getString(ARG_UTM_SOURCE).orEmpty()
        }
    }

    private inner class ActivityResultHandler {
        private fun handleMediaPickerResult(data: Intent) {
            val result = MediaPicker.result(data)
            viewModel.updateMediaPicker(result.originalPaths)
            val mediaCount = result.originalPaths.size
            trackingHandler.trackOnReceiveMediaFromMediaPicker(mediaCount)
        }

        fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {
            when (requestCode) {
                CreateReviewFragment.REQUEST_CODE_IMAGE -> {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        handleMediaPickerResult(data)
                    }
                }
                else -> super@CreateReviewBottomSheet.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private inner class BottomSheetHandler {
        private fun getOvoIncentiveBottomSheetFromFragmentManager(): IncentiveOvoBottomSheet? {
            return (childFragmentManager.findFragmentByTag(
                IncentiveOvoBottomSheet.TAG
            ) as? IncentiveOvoBottomSheet)
        }

        private fun getCreateReviewTextAreaBottomSheetFromFragmentManager(): CreateReviewTextAreaBottomSheet? {
            return (childFragmentManager.findFragmentByTag(
                CreateReviewTextAreaBottomSheet.TAG
            ) as? CreateReviewTextAreaBottomSheet)
        }

        private fun getCreateReviewPostSubmitBottomSheetFromFragmentManager(): CreateReviewPostSubmitBottomSheet? {
            return (childFragmentManager.findFragmentByTag(
                CreateReviewPostSubmitBottomSheet.TAG
            ) as? CreateReviewPostSubmitBottomSheet)
        }

        private fun getCreateReviewAnonymousInfoBottomSheetFromFragmentManager(): CreateReviewAnonymousInfoBottomSheet? {
            return (childFragmentManager.findFragmentByTag(
                CreateReviewAnonymousInfoBottomSheet.TAG
            ) as? CreateReviewAnonymousInfoBottomSheet)
        }

        fun showOvoIncentiveBottomSheet(data: IncentiveOvoBottomSheetUiModel) {
            getOvoIncentiveBottomSheetFromFragmentManager()?.init(
                data, incentiveOvoBottomSheetListener
            ) ?: IncentiveOvoBottomSheet().also {
                it.init(data, incentiveOvoBottomSheetListener)
                it.show(childFragmentManager, IncentiveOvoBottomSheet.TAG)
                if (viewModel.hasIncentive()) {
                    CreateReviewTracking.eventClickIncentivesTicker(
                        data.productRevIncentiveOvoDomain.productrevIncentiveOvo?.subtitle.orEmpty(),
                        viewModel.getReputationId(),
                        viewModel.getOrderId(),
                        viewModel.getProductId(),
                        viewModel.getUserId()
                    )
                } else if (viewModel.hasOngoingChallenge()) {
                    CreateReviewTracking.eventClickOngoingChallengeTicker(
                        viewModel.getReputationId(),
                        viewModel.getOrderId(),
                        viewModel.getProductId(),
                        viewModel.getUserId()
                    )
                }
            }
        }

        fun showCreateReviewTextAreaBottomSheet() {
            getCreateReviewTextAreaBottomSheetFromFragmentManager()
                ?: CreateReviewTextAreaBottomSheet().also {
                    it.show(childFragmentManager, CreateReviewTextAreaBottomSheet.TAG)
                }
        }

        fun showCreateReviewPostSubmitBottomSheet(data: ProductrevGetPostSubmitBottomSheetResponse) {
            getCreateReviewPostSubmitBottomSheetFromFragmentManager()
                ?: CreateReviewPostSubmitBottomSheet().also {
                    it.init(
                        data,
                        viewModel.hasIncentive(),
                        viewModel.hasOngoingChallenge(),
                        incentiveOvoBottomSheetListener,
                        getThankYouBottomSheetTrackerData()
                    )
                    it.show(childFragmentManager, CreateReviewPostSubmitBottomSheet.TAG)
                }
        }

        fun showCreateReviewAnonymousInfoBottomSheet() {
            getCreateReviewAnonymousInfoBottomSheetFromFragmentManager()
                ?: CreateReviewAnonymousInfoBottomSheet().also {
                    it.setListener(createReviewAnonymousInfoBottomSheetListener)
                    it.show(childFragmentManager, CreateReviewAnonymousInfoBottomSheet.TAG)
                }
        }

        fun dismissOvoIncentiveBottomSheet() {
            getOvoIncentiveBottomSheetFromFragmentManager()?.dismiss()
        }

        fun dismissCreateReviewTextAreaBottomSheet() {
            getCreateReviewTextAreaBottomSheetFromFragmentManager()?.dismiss()
        }

        fun dismissCreateReviewPostSubmitBottomSheet() {
            getCreateReviewPostSubmitBottomSheetFromFragmentManager()?.dismiss()
        }

        fun dismissCreateReviewAnonymousInfoBottomSheet() {
            getCreateReviewAnonymousInfoBottomSheetFromFragmentManager()?.dismiss()
        }
    }

    private inner class DaggerHandler {
        fun initInjector() {
            context?.let {
                CreateReviewDaggerInstance.getInstance(it).inject(this@CreateReviewBottomSheet)
            }
        }
    }

    private inner class DialogHandler {
        private fun showDialog(
            title: String,
            description: String,
            primaryCtaText: String,
            primaryCtaAction: () -> Unit,
            secondaryCtaText: String,
            secondaryCtaAction: () -> Unit
        ) {
            context?.let {
                DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(title)
                    setDescription(description)
                    setPrimaryCTAText(primaryCtaText)
                    setPrimaryCTAClickListener {
                        this.dismiss()
                        primaryCtaAction.invoke()
                    }
                    setSecondaryCTAText(secondaryCtaText)
                    setSecondaryCTAClickListener {
                        this.dismiss()
                        secondaryCtaAction.invoke()
                    }
                    show()
                }
            }
        }

        fun showIncentivesExitWarningDialog() {
            showDialog(
                getString(R.string.review_form_incentives_exit_dialog_title),
                getString(R.string.review_form_incentives_exit_dialog_body),
                getString(R.string.review_form_dismiss_form_dialog_stay),
                {
                    isUserInitiateDismiss = false
                },
                getString(R.string.review_edit_dialog_exit),
                {
                    isUserInitiateDismiss = true
                    dismiss()
                }
            )
        }

        fun showReviewIncompleteDialog() {
            val title = getString(R.string.review_create_incomplete_title)
            showDialog(
                title,
                getString(R.string.review_form_incentives_incomplete_dialog_body),
                getString(R.string.review_create_incomplete_cancel),
                { trackingHandler.trackClickCompleteReviewFirst(title) },
                getString(R.string.review_create_incomplete_send_anyways),
                {
                    viewModel.submitReview()
                    trackingHandler.trackClickSendNow(title)
                })
            trackingHandler.trackViewDialog(title)
        }
    }

    private inner class TrackingHandler {
        fun trackOpenScreen() {
            CreateReviewTracking.openScreenWithCustomDimens(
                CreateReviewTrackingConstants.SCREEN_NAME_BOTTOM_SHEET,
                viewModel.getProductId(),
                viewModel.getReputationId(),
                viewModel.getUtmSource()
            )
        }

        fun trackClickDialogOption(title: String) {
            CreateReviewTracking.eventClickDialogOption(
                CreateReviewDialogType.CreateReviewSendRatingOnlyDialog,
                title,
                viewModel.getReputationId(),
                viewModel.getOrderId(),
                viewModel.getProductId(),
                viewModel.getUserId()
            )
        }

        fun trackViewDialog(title: String) {
            CreateReviewTracking.eventViewDialog(
                CreateReviewDialogType.CreateReviewSendRatingOnlyDialog,
                title,
                viewModel.getReputationId(),
                viewModel.getOrderId(),
                viewModel.getProductId(),
                viewModel.getUserId()
            )
        }

        fun trackClickSubmitForm() {
            CreateReviewTracking.eventClickSubmitForm(
                viewModel.getRating(),
                viewModel.getReviewMessageLength(),
                viewModel.getNumberOfMedia(),
                viewModel.isAnonymous(),
                viewModel.hasIncentive(),
                viewModel.isTemplateAvailable(),
                viewModel.getSelectedTemplateCount(),
                viewModel.getOrderId(),
                viewModel.getProductId(),
                viewModel.getUserId()
            )
        }

        fun trackClickCompleteReviewFirst(title: String) {
            CreateReviewTracking.eventClickCompleteReviewFirst(title)
        }

        fun trackClickSendNow(title: String) {
            CreateReviewTracking.eventClickSendNow(title)
        }

        fun trackViewBadRatingReason(title: String) {
            CreateReviewTracking.eventViewBadRatingReason(
                trackingQueue,
                viewModel.getOrderId(),
                viewModel.getProductId(),
                title,
                viewModel.getUserId()
            )
        }

        fun trackClickBadRatingReason(title: String, selected: Boolean) {
            CreateReviewTracking.eventClickBadRatingReason(
                viewModel.getOrderId(),
                viewModel.getProductId(),
                viewModel.getUserId(),
                title,
                selected
            )
        }

        fun trackDismissForm() {
            CreateReviewTracking.eventDismissForm(
                viewModel.getRating(),
                viewModel.getReviewMessageLength(),
                viewModel.getNumberOfMedia(),
                viewModel.isAnonymous(),
                viewModel.hasIncentive(),
                viewModel.isTemplateAvailable(),
                viewModel.getSelectedTemplateCount(),
                viewModel.getOrderId(),
                viewModel.getProductId(),
                viewModel.getUserId()
            )
        }

        fun trackOnReceiveMediaFromMediaPicker(mediaCount: Int) {
            CreateReviewTracking.reviewOnImageUploadTracker(
                orderId = viewModel.getOrderId(),
                productId = viewModel.getProductId(),
                isSuccessful = true,
                imageNum = mediaCount.toString(),
                isEditReview = false,
                feedbackId = viewModel.getFeedbackId()
            )
        }

        fun trackOpenUniversalMediaPicker() {
            CreateReviewTracking.trackOpenUniversalMediaPicker(
                viewModel.getUserId(),
                viewModel.getShopId()
            )
        }
    }

    private inner class UiStateHandler {

        private fun initUiStateCollector() {
            collectCreateReviewBottomSheet()
            collectProductCardUiState()
            collectRatingUiState()
            collectTickerUiState()
            collectTextAreaTitleUiState()
            collectBadRatingCategoriesUiState()
            collectTopicsUiState()
            collectTextAreaUiState()
            collectTemplateUiState()
            collectMediaPickerUiState()
            collectAnonymousUiState()
            collectProgressBarUiState()
            collectSubmitButtonUiState()
            collectIncentiveBottomSheetUiState()
            collectTextAreaBottomSheetUiState()
            collectPostSubmitBottomSheetUiState()
            collectAnonymousInfoBottomSheetUiState()
            collectToasterQueue()
            collectSubmitReviewResult()
        }

        fun initUiState(savedInstanceState: Bundle?) {
            if (savedInstanceState == null) {
                viewModel.setRating(getRatingFromArgument())
                viewModel.setProductId(getProductIdFromArgument())
                viewModel.setReputationId(getReputationIdFromArgument())
                viewModel.setUtmSource(getUtmSourceFromArgument())
            } else {
                viewModel.restoreUiState(savedInstanceState)
            }
            initUiStateCollector()
        }

        fun saveUiState(outState: Bundle) {
            viewModel.saveUiState(outState)
        }

        private fun collectCreateReviewBottomSheet() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.createReviewBottomSheetUiState) {
                when (it) {
                    is CreateReviewBottomSheetUiState.Showing -> {
                        shouldScheduleScrollToTextArea(it.bottomInset)
                    }
                    is CreateReviewBottomSheetUiState.ShouldDismiss -> {
                        context?.let { context ->
                            finishIfRoot(
                                it.success,
                                it.message.getStringValue(context),
                                it.feedbackId
                            )
                        }
                    }
                }
            }
        }

        private fun collectProductCardUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.productCardUiState) {
                binding?.reviewFormProductCard?.updateUi(it)
            }
        }

        private fun collectRatingUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.ratingUiState) {
                binding?.reviewFormRating?.updateUi(it)
            }
        }

        private fun collectTickerUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.tickerUiState) {
                binding?.reviewFormTicker?.updateUi(it)
            }
        }

        private fun collectTextAreaTitleUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.textAreaTitleUiState) {
                binding?.reviewFormTextAreaTitle?.updateUi(it)
            }
        }

        private fun collectBadRatingCategoriesUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.badRatingCategoriesUiState) {
                binding?.reviewFormBadRatingCategories?.updateUi(it)
            }
        }

        private fun collectTopicsUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.topicsUiState) {
                binding?.reviewFormTopics?.updateUI(it)
            }
        }

        private fun collectTextAreaUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.textAreaUiState) {
                binding?.reviewFormTextArea?.updateUi(
                    uiState = it,
                    source = CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA
                )
            }
        }

        private fun collectTemplateUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.templateUiState) {
                binding?.reviewFormTemplates?.updateUi(it)
            }
        }

        private fun collectMediaPickerUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.mediaPickerUiState) {
                binding?.reviewFormMediaPicker?.updateUi(it)
            }
        }

        private fun collectAnonymousUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.anonymousUiState) {
                binding?.reviewFormAnonymous?.updateUi(it)
            }
        }

        private fun collectProgressBarUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.progressBarUiState) {
                binding?.reviewFormProgressBarWidget?.updateUi(it)
            }
        }

        private fun collectSubmitButtonUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.submitButtonUiState) {
                binding?.reviewFormSubmitButton?.updateUi(it)
            }
        }

        private fun collectIncentiveBottomSheetUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.incentiveBottomSheetUiState) {
                if (it is CreateReviewIncentiveBottomSheetUiState.Showing) {
                    bottomSheetHandler.showOvoIncentiveBottomSheet(it.data)
                } else {
                    bottomSheetHandler.dismissOvoIncentiveBottomSheet()
                }
            }
        }

        private fun collectTextAreaBottomSheetUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.textAreaBottomSheetUiState) {
                if (it is CreateReviewTextAreaBottomSheetUiState.Showing) {
                    bottomSheetHandler.showCreateReviewTextAreaBottomSheet()
                } else {
                    bottomSheetHandler.dismissCreateReviewTextAreaBottomSheet()
                }
            }
        }

        private fun collectPostSubmitBottomSheetUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.postSubmitBottomSheetUiState) {
                when (it) {
                    is PostSubmitUiState.ShowThankYouBottomSheet -> {
                        bottomSheetHandler.showCreateReviewPostSubmitBottomSheet(it.data)
                    }
                    is PostSubmitUiState.Hidden -> {
                        bottomSheetHandler.dismissCreateReviewPostSubmitBottomSheet()
                    }
                    is PostSubmitUiState.ShowThankYouToaster -> {
                        showThankYouToaster(it.data)
                    }
                }
            }
        }

        private fun collectAnonymousInfoBottomSheetUiState() {
            viewLifecycleOwner.collectLatestWhenResumed(viewModel.anonymousInfoBottomSheetUiState) {
                if (it is CreateReviewAnonymousInfoBottomSheetUiState.Showing) {
                    bottomSheetHandler.showCreateReviewAnonymousInfoBottomSheet()
                } else {
                    bottomSheetHandler.dismissCreateReviewAnonymousInfoBottomSheet()
                }
            }
        }

        private fun collectToasterQueue() {
            viewLifecycleOwner.collectWhenResumed(viewModel.toasterQueue) {
                suspendCoroutine { cont ->
                    binding?.root?.let { view ->
                        Toaster.build(
                            view,
                            it.message.getStringValueWithDefaultParam(view.context),
                            it.duration,
                            it.type,
                            it.actionText.getStringValue(view.context)
                        ) {}.run {
                            anchorView = binding?.reviewFormProgressBarDivider
                            addCallback(object: Snackbar.Callback() {
                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    removeCallback(this)
                                    cont.resume(Unit)
                                }
                            })
                            show()
                        }
                    }
                }
            }
        }

        private fun collectSubmitReviewResult() {
            viewLifecycleOwner.collectWhenResumed(viewModel.submitReviewResult) {
                if (it is SubmitReviewRequestErrorState) {
                    CreateReviewTracking.trackErrorSubmitReview(
                        userId = viewModel.getUserId(),
                        errorMessage = getString(
                            R.string.review_create_fail_toaster,
                            ErrorHandler.getErrorMessagePair(
                                null,
                                it.throwable,
                                ErrorHandler.Builder()
                            ).second
                        ),
                        orderId = viewModel.getOrderId(),
                        productId = viewModel.getProductId(),
                        rating = viewModel.getRating(),
                        hasReviewText = viewModel.isReviewTextEmpty().not(),
                        reviewTextLength = viewModel.getReviewMessageLength(),
                        mediaCount = viewModel.getNumberOfMedia(),
                        anonymous = viewModel.isAnonymous(),
                        hasIncentive = viewModel.hasIncentive(),
                        hasTemplate = viewModel.hasTemplate(),
                        templateUsedCount = viewModel.templateUsedCount()
                    )
                }
            }
        }
    }

    private inner class BaseCreateReviewCustomViewListener: BaseReviewCustomView.Listener {
        override fun onRequestClearTextAreaFocus() {
            viewModel.updateTextAreaHasFocus(hasFocus = false)
        }

        override fun onRequestTextAreaFocus() {
            viewModel.updateTextAreaHasFocus(hasFocus = true)
        }

        fun attachListener() {
            binding?.run {
                reviewFormProductCard.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormRating.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormTicker.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormTextAreaTitle.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormTextArea.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormTemplates.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormMediaPicker.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormAnonymous.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormProgressBarWidget.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
                reviewFormSubmitButton.setBaseCustomViewListener(this@BaseCreateReviewCustomViewListener)
            }
        }
    }

    private inner class AnonymousListener : CreateReviewAnonymous.Listener {
        override fun onIsAnonymousChanged(anonymous: Boolean) {
            viewModel.setAnonymous(anonymous)
        }

        override fun onClickSeeAnonymousInfo() {
            viewModel.showAnonymousInfoBottomSheet()
        }

        fun attachListener() {
            binding?.reviewFormAnonymous?.setListener(this)
        }
    }

    private inner class BadRatingCategoryListener : CreateReviewBadRatingCategoryViewHolder.Listener {
        override fun onImpressBadRatingCategory(description: String) {
            trackingHandler.trackViewBadRatingReason(description)
        }

        override fun onBadRatingCategoryClicked(
            description: String,
            checked: Boolean,
            id: String,
            shouldRequestFocus: Boolean
        ) {
            trackingHandler.trackClickBadRatingReason(description, checked)
            viewModel.updateBadRatingSelectedStatus(id, checked)
            if (shouldRequestFocus && checked) {
                viewModel.updateTextAreaHasFocus(hasFocus = true)
            } else {
                viewModel.updateTextAreaHasFocus(hasFocus = false)
            }
        }

        fun attachListener() {
            binding?.reviewFormBadRatingCategories?.setListener(this)
        }
    }

    private inner class MediaPickerListener: CreateReviewMediaPicker.Listener {
        private fun goToMediaPicker() {
            context?.let {
                trackingHandler.trackOpenUniversalMediaPicker()
                val intent = MediaPicker.intent(it) {
                    pageSource(PageSource.Review)
                    modeType(ModeType.COMMON)
                    maxMediaItem(MAX_IMAGE_COUNT)
                    maxVideoItem(MAX_VIDEO_COUNT)
                    maxVideoFileSize(MAX_VIDEO_SIZE_BYTE)
                    includeMedias(viewModel.getSelectedMediasUrl())
                }
                startActivityForResult(intent, CreateReviewFragment.REQUEST_CODE_IMAGE)
            }
        }

        override fun onAddMediaClicked(enabled: Boolean) {
            if (enabled) {
                goToMediaPicker()
            } else {
                viewModel.enqueueDisabledAddMoreMediaToaster()
            }
        }

        override fun onRemoveMediaClicked(media: CreateReviewMediaUiModel) {
            viewModel.removeMedia(media)
        }

        override fun onRetryUploadClicked() {
            viewModel.retryUploadMedia()
        }

        fun attachListener() {
            binding?.reviewFormMediaPicker?.setListener(this)
        }
    }

    private inner class SubmitButtonListener : CreateReviewSubmitButton.Listener {
        override fun onSubmitButtonClicked() {
            trackingHandler.trackClickSubmitForm()
            if (!viewModel.isReviewComplete() && (viewModel.hasIncentive() || viewModel.hasOngoingChallenge())) {
                dialogHandler.showReviewIncompleteDialog()
            } else {
                viewModel.submitReview()
            }
        }

        fun attachListener() {
            binding?.reviewFormSubmitButton?.setListener(this)
        }
    }

    private inner class TextAreaListener : CreateReviewTextArea.Listener {

        override fun onTextChanged(text: String, source: CreateReviewTextAreaTextUiModel.Source) {
            viewModel.setReviewText(text, source)
        }

        override fun onFocusChanged(hasFocus: Boolean) {
            viewModel.updateTextAreaHasFocus(hasFocus = hasFocus)
        }

        override fun onExpandButtonClicked() {
            viewModel.showTextAreaBottomSheet()
        }

        fun attachListener() {
            binding?.reviewFormTextArea?.setListener(this)
        }
    }

    private inner class TemplateListener: CreateReviewTemplate.Listener {
        override fun onTemplateSelected(template: com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate) {
            viewModel.selectTemplate(template)
        }

        fun attachListener() {
            binding?.reviewFormTemplates?.setListener(this)
        }
    }

    private inner class TickerListener : CreateReviewTicker.Listener {
        override fun onTickerDescriptionClicked() { viewModel.showIncentiveBottomSheet() }

        fun attachListener() {
            binding?.reviewFormTicker?.setListener(this)
        }
    }

    private inner class IncentiveOvoBottomSheetListener: IncentiveOvoListener {
        override fun onUrlClicked(url: String): Boolean {
            context?.let {
                viewModel.dismissIncentiveBottomSheet()
                return ReviewUtil.routeToWebview(context = it, bottomSheet = null, url = url)
            }
            return false
        }

        override fun onDismissIncentiveBottomSheet() {
            viewModel.dismissIncentiveBottomSheet()
        }

        override fun onClickCloseThankYouBottomSheet() {
            finishIfRoot(
                success = true,
                message = getString(
                    R.string.review_create_success_toaster,
                    viewModel.getUserName()
                ),
                feedbackId = viewModel.getFeedbackId()
            )
        }
    }

    private inner class CreateReviewAnonymousInfoBottomSheetListener: CreateReviewAnonymousInfoBottomSheet.Listener {
        override fun onDismissCreateReviewAnonymousInfoBottomSheet() {
            viewModel.dismissAnonymousInfoBottomSheet()
        }
    }
}
