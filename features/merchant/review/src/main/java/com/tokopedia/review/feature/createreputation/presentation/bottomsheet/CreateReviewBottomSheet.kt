package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reputation.common.constant.ReputationCommonConstants
import com.tokopedia.review.R
import com.tokopedia.review.common.ReviewInboxConstants
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
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewIncentiveBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaBottomSheetUiState
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewBadRatingCategoryViewHolder
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseCreateReviewCustomView
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewAnonymous
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewMediaPicker
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewRating
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
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class CreateReviewBottomSheet : BottomSheetUnify(), CoroutineScope {
    companion object {
        private const val TEXT_AREA_MAX_MIN_LINE = 4

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

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

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
    private val incentiveOvoBottomSheetListener = IncentiveOvoBottomSheetListener()
    private val mediaPickerListener = MediaPickerListener()
    private val ratingListener = RatingListener()
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
        uiStateHandler.collectUiStates()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListeners()
        setupDismissBehavior()
    }

    override fun onResume() {
        super.onResume()
        uiStateHandler.collectUiStates()
    }

    override fun onPause() {
        super.onPause()
        uiStateHandler.cancelUiStateCollectors()
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
        baseCreateReviewCustomViewListener.attachListener()
        ratingListener.attachListener()
        tickerListener.attachListener()
        badRatingCategoryListener.attachListener()
        textAreaListener.attachListener()
        templateListener.attachListener()
        mediaPickerListener.attachListener()
        templateListener.attachListener()
        anonymousListener.attachListener()
        submitButtonListener.attachListener()
    }

    private fun handleDismiss() {
        if (viewModel.hasIncentive()) {
            dialogHandler.showIncentivesExitWarningDialog()
            return
        }
        if (viewModel.isGoodRating() && viewModel.isReviewTextEmpty() && viewModel.isMediaEmpty()) {
            dialogHandler.showSendRatingOnlyDialog()
            return
        }
        if (!viewModel.isReviewTextEmpty() || !viewModel.isMediaEmpty()) {
            dialogHandler.showReviewUnsavedWarningDialog()
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
            arguments = Bundle().apply {
                putInt(ARG_RATING, rating)
                putString(ARG_PRODUCT_ID, productId)
                putString(ARG_REPUTATION_ID, reputationId)
                putString(ARG_UTM_SOURCE, utmSource)
            }
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
            val result = ImagePickerResultExtractor.extract(data)
            val selectedImage = result.imageUrlOrPathList
            val imagesFedIntoPicker = result.imagesFedIntoPicker
            viewModel.updateMediaPicker(selectedImage, imagesFedIntoPicker)
            trackingHandler.trackOnReceiveMediaFromMediaPicker(selectedImage.size)
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

        fun dismissOvoIncentiveBottomSheet() {
            getOvoIncentiveBottomSheetFromFragmentManager()?.dismiss()
        }

        fun dismissCreateReviewTextAreaBottomSheet() {
            getCreateReviewTextAreaBottomSheetFromFragmentManager()?.dismiss()
        }

        fun dismissCreateReviewPostSubmitBottomSheet() {
            getCreateReviewPostSubmitBottomSheetFromFragmentManager()?.dismiss()
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
                DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
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

        fun showSendRatingOnlyDialog() {
            val title = getString(R.string.review_form_send_rating_only_dialog_title)
            showDialog(
                title,
                getString(R.string.review_form_send_rating_only_body),
                getString(R.string.review_form_send_rating_only_exit),
                {
                    isUserInitiateDismiss = true
                    dismiss()
                },
                getString(R.string.review_form_send_rating_only),
                {
                    viewModel.submitReview()
                    trackingHandler.trackClickDialogOption(title)
                }
            )
            trackingHandler.trackViewDialog(title)
        }

        fun showReviewUnsavedWarningDialog() {
            val title = getString(R.string.review_form_dismiss_form_dialog_title)
            showDialog(
                title,
                getString(R.string.review_form_dismiss_form_dialog_body),
                getString(R.string.review_edit_dialog_exit),
                {
                    isUserInitiateDismiss = true
                    dismiss()
                },
                getString(R.string.review_form_dismiss_form_dialog_stay),
                { trackingHandler.trackClickDialogOption(title) }
            )
            trackingHandler.trackViewDialog(title)
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
                viewModel.getNumberOfPictures(),
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

        fun trackClickBadRatingReason(title: String, selected: Boolean, ) {
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
                viewModel.getNumberOfPictures(),
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
                viewModel.getOrderId(),
                viewModel.getProductId(),
                true,
                mediaCount.toString(),
                false,
                viewModel.getFeedbackId()
            )
        }
    }

    private inner class UiStateHandler {

        private var productCardUiStateCollectorJob: Job? = null
        private var ratingUiStateCollectorJob: Job? = null
        private var tickerUiStateCollectorJob: Job? = null
        private var textAreaTitleUiStateCollectorJob: Job? = null
        private var badRatingCategoriesUiStateCollectorJob: Job? = null
        private var textAreaUiStateCollectorJob: Job? = null
        private var templateUiStateCollectorJob: Job? = null
        private var mediaPickerUiStateCollectorJob: Job? = null
        private var anonymousUiStateCollectorJob: Job? = null
        private var progressBarUiStateCollectorJob: Job? = null
        private var submitButtonUiStateCollectorJob: Job? = null
        private var incentiveBottomSheetUiStateCollectorJob: Job? = null
        private var textAreaBottomSheetUiStateCollectorJob: Job? = null
        private var postSubmitBottomSheetUiStateCollectorJob: Job? = null

        fun initUiState(savedInstanceState: Bundle?) {
            if (savedInstanceState == null) {
                viewModel.setRating(getRatingFromArgument())
                viewModel.setProductId(getProductIdFromArgument())
                viewModel.setReputationId(getReputationIdFromArgument())
                viewModel.setUtmSource(getUtmSourceFromArgument())
            } else {
                viewModel.restoreUiState(savedInstanceState)
            }
        }

        fun saveUiState(outState: Bundle) {
            viewModel.saveUiState(outState)
        }

        fun collectUiStates() {
            collectProductCardUiState()
            collectRatingUiState()
            collectTickerUiState()
            collectTextAreaTitleUiState()
            collectBadRatingCategoriesUiState()
            collectTextAreaUiState()
            collectTemplateUiState()
            collectMediaPickerUiState()
            collectAnonymousUiState()
            collectProgressBarUiState()
            collectSubmitButtonUiState()
            collectIncentiveBottomSheetUiState()
            collectTextAreaBottomSheetUiState()
            collectPostSubmitBottomSheetUiState()
        }

        fun cancelUiStateCollectors() {
            productCardUiStateCollectorJob?.cancel()
            ratingUiStateCollectorJob?.cancel()
            tickerUiStateCollectorJob?.cancel()
            textAreaTitleUiStateCollectorJob?.cancel()
            badRatingCategoriesUiStateCollectorJob?.cancel()
            textAreaUiStateCollectorJob?.cancel()
            templateUiStateCollectorJob?.cancel()
            mediaPickerUiStateCollectorJob?.cancel()
            anonymousUiStateCollectorJob?.cancel()
            progressBarUiStateCollectorJob?.cancel()
            submitButtonUiStateCollectorJob?.cancel()
            incentiveBottomSheetUiStateCollectorJob?.cancel()
            textAreaBottomSheetUiStateCollectorJob?.cancel()
            postSubmitBottomSheetUiStateCollectorJob?.cancel()
        }

        private fun collectProductCardUiState() {
            productCardUiStateCollectorJob = productCardUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.productCardUiState.collectLatest {
                    binding?.reviewFormProductCard?.updateUi(it)
                }
            }
        }

        private fun collectRatingUiState() {
            ratingUiStateCollectorJob = ratingUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.ratingUiState.collectLatest {
                    binding?.reviewFormRating?.updateUi(it)
                }
            }
        }

        private fun collectTickerUiState() {
            tickerUiStateCollectorJob = tickerUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.tickerUiState.collectLatest {
                    binding?.reviewFormTicker?.updateUi(it)
                }
            }
        }

        private fun collectTextAreaTitleUiState() {
            textAreaTitleUiStateCollectorJob = textAreaTitleUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.textAreaTitleUiState.collectLatest {
                    binding?.reviewFormTextAreaTitle?.updateUi(it)
                }
            }
        }

        private fun collectBadRatingCategoriesUiState() {
            badRatingCategoriesUiStateCollectorJob = badRatingCategoriesUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.badRatingCategoriesUiState.collectLatest {
                    binding?.reviewFormBadRatingCategories?.updateUi(it)
                }
            }
        }

        private fun collectTextAreaUiState() {
            textAreaUiStateCollectorJob = textAreaUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.textAreaUiState.collectLatest {
                    binding?.reviewFormTextArea?.updateUi(it, CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA)
                }
            }
        }

        private fun collectTemplateUiState() {
            templateUiStateCollectorJob = templateUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.templateUiState.collectLatest {
                    binding?.reviewFormTemplates?.updateUi(it)
                }
            }
        }

        private fun collectMediaPickerUiState() {
            mediaPickerUiStateCollectorJob = mediaPickerUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.mediaPickerUiState.collectLatest {
                    binding?.reviewFormMediaPicker?.updateUi(it)
                }
            }
        }

        private fun collectAnonymousUiState() {
            anonymousUiStateCollectorJob = anonymousUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.anonymousUiState.collectLatest {
                    binding?.reviewFormAnonymous?.updateUi(it)
                }
            }
        }

        private fun collectProgressBarUiState() {
            progressBarUiStateCollectorJob = progressBarUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.progressBarUiState.collectLatest {
                    binding?.reviewFormProgressBarWidget?.updateUi(it)
                }
            }
        }

        private fun collectSubmitButtonUiState() {
            submitButtonUiStateCollectorJob = submitButtonUiStateCollectorJob?.takeIf {
                !it.isCompleted
            } ?: launch {
                viewModel.submitButtonUiState.collectLatest {
                    binding?.reviewFormSubmitButton?.updateUi(it)
                }
            }
        }

        private fun collectIncentiveBottomSheetUiState() {
            incentiveBottomSheetUiStateCollectorJob =
                incentiveBottomSheetUiStateCollectorJob?.takeIf {
                    !it.isCompleted
                } ?: launch {
                    viewModel.incentiveBottomSheetUiState.collectLatest {
                        if (it is CreateReviewIncentiveBottomSheetUiState.Showing) {
                            bottomSheetHandler.showOvoIncentiveBottomSheet(it.data)
                        } else {
                            bottomSheetHandler.dismissOvoIncentiveBottomSheet()
                        }
                    }
                }
        }

        private fun collectTextAreaBottomSheetUiState() {
            textAreaBottomSheetUiStateCollectorJob =
                textAreaBottomSheetUiStateCollectorJob?.takeIf {
                    !it.isCompleted
                } ?: launch {
                    viewModel.textAreaBottomSheetUiState.collectLatest {
                        if (it is CreateReviewTextAreaBottomSheetUiState.Showing) {
                            bottomSheetHandler.showCreateReviewTextAreaBottomSheet()
                        } else {
                            bottomSheetHandler.dismissCreateReviewTextAreaBottomSheet()
                        }
                    }
                }
        }

        private fun collectPostSubmitBottomSheetUiState() {
            postSubmitBottomSheetUiStateCollectorJob =
                postSubmitBottomSheetUiStateCollectorJob?.takeIf {
                    !it.isCompleted
                } ?: launch {
                    viewModel.postSubmitBottomSheetUiState.collectLatest {
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
        }
    }

    private inner class BaseCreateReviewCustomViewListener: BaseCreateReviewCustomView.Listener {
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
        private fun goToImagePicker() {
            context?.let {
                val builder = ImagePickerBuilder.getSquareImageBuilder(it)
                    .withSimpleEditor()
                    .withSimpleMultipleSelection(viewModel.getSelectedImagesUrl())
                    .apply { title = getString(R.string.image_picker_title) }
                val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
                intent.putImagePickerBuilder(builder)
                intent.putParamPageSource(ImagePickerPageSource.REVIEW_PAGE)
                startActivityForResult(intent, CreateReviewFragment.REQUEST_CODE_IMAGE)
            }
        }

        override fun onAddMediaClicked() {
            goToImagePicker()
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

    private inner class RatingListener : CreateReviewRating.Listener {
        override fun onRatingChanged(rating: Int) {
            viewModel.setRating(rating)
        }

        fun attachListener() {
            binding?.reviewFormRating?.setListener(this)
        }
    }

    private inner class SubmitButtonListener : CreateReviewSubmitButton.Listener {
        override fun onSubmitButtonClicked() {
            trackingHandler.trackClickSubmitForm()
            if (!viewModel.isReviewComplete() && viewModel.hasIncentive()) {
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

        override fun onStartAnimatingTemplates() {
            viewModel.setReviewTemplatesAnimating(true)
        }

        override fun onFinishAnimatingTemplates() {
            viewModel.setReviewTemplatesAnimating(false)
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
}