package com.tokopedia.review.feature.bulk_write_review.presentation.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getVisiblePercent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.common.extension.collectWhenResumed
import com.tokopedia.review.databinding.FragmentBulkReviewBinding
import com.tokopedia.review.feature.bulk_write_review.di.component.DaggerBulkReviewComponent
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.BulkReviewAdapter
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewItemViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.bottomsheet.BulkReviewBadRatingCategoryBottomSheet
import com.tokopedia.review.feature.bulk_write_review.presentation.bottomsheet.BulkReviewExpandedTextAreaBottomSheet
import com.tokopedia.review.feature.bulk_write_review.presentation.dialog.BulkReviewCancelReviewSubmissionDialog
import com.tokopedia.review.feature.bulk_write_review.presentation.dialog.BulkReviewRemoveReviewItemDialog
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewCancelReviewSubmissionDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewExpandedTextAreaBottomSheetUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewPageUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRemoveReviewItemDialogUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewStickyButtonUiState
import com.tokopedia.review.feature.bulk_write_review.presentation.viewmodel.BulkReviewViewModel
import com.tokopedia.review.feature.bulk_write_review.presentation.widget.WidgetBulkReviewStickyButton
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView
import com.tokopedia.review.feature.inbox.presentation.ReviewInboxActivity
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class BulkReviewFragment : BaseDaggerFragment(), BulkReviewItemViewHolder.Listener {

    companion object {
        private const val MAX_VIDEO_COUNT = 1
        private const val MAX_IMAGE_COUNT = 4
        private const val MAX_VIDEO_SIZE_BYTE = 250L * 1024L * 1024L
        private const val REQUEST_CODE_IMAGE = 111
        private const val BULK_REVIEW_COACH_MARK_TAG = "BULK_REVIEW_COACH_MARK_TAG"
        private const val BULK_REVIEW_KEY_CACHE_MANAGER_ID = "bulkReviewCacheManagerId"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var gestureDetector: GestureDetectorCompat? = null

    private var binding by autoClearedNullable<FragmentBulkReviewBinding>()

    private val adapter = BulkReviewAdapter(this)
    private val gestureListener = BulkReviewGestureListener()
    private val stickyButtonListener = BulkReviewStickyButtonListener()

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), viewModelFactory).get(BulkReviewViewModel::class.java)
    }
    private val activityResultHandler by lazy(LazyThreadSafetyMode.NONE) { ActivityResultHandler() }
    private val bottomSheetHandler by lazy(LazyThreadSafetyMode.NONE) { BottomSheetHandler() }
    private val coachMarkHandler by lazy(LazyThreadSafetyMode.NONE) { CoachMarkHandler() }
    private val dialogHandler by lazy(LazyThreadSafetyMode.NONE) { DialogHandler() }

    override fun getScreenName(): String {
        return BulkReviewFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            DaggerBulkReviewComponent.builder()
                .reviewComponent(ReviewInstance.getComponent(it.application))
                .build()
                .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gestureDetector = context?.let { GestureDetectorCompat(it, gestureListener) }
        activity?.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
        initUiState(savedInstanceState)
        collectBulkReviewPageUiState()
        collectToasterQueue()
        collectBulkReviewRemoveReviewItemDialogUiState()
        collectBulkReviewCancelReviewSubmissionDialogUiState()
        collectBulkReviewBadRatingCategoryBottomSheetUiState()
        collectBulkReviewExpandedTextAreaBottomSheetUiState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBulkReviewBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupToolbar()
        setupInsetListener()
    }

    override fun onPause() {
        super.onPause()
        viewModel.sendAllTrackers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityResultHandler.handleResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        context?.let { context ->
            val cacheManager = SaveInstanceCacheManager(context = context, generateObjectId = true)
            viewModel.onSaveInstanceState(cacheManager)
            outState.putString(BULK_REVIEW_KEY_CACHE_MANAGER_ID, cacheManager.id)
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        return viewModel.onBackPressed()
    }

    override fun onClickRemoveReviewItem(inboxID: String) {
        clearViewFocus()
        viewModel.onRemoveReviewItem(inboxID)
    }

    override fun onRatingChanged(inboxID: String, rating: Int) {
        viewModel.onRatingChanged(inboxID, rating)
    }

    override fun onRatingSet(inboxID: String) {
        viewModel.onRatingSet(inboxID)
    }

    override fun onClickChangeBadRatingCategory(inboxID: String) {
        clearViewFocus()
        viewModel.onChangeBadRatingCategory(inboxID)
    }

    override fun onClickTestimonyMiniAction(inboxID: String) {
        clearViewFocus()
        viewModel.onClickTestimonyMiniAction(inboxID)
    }

    override fun onClickAddAttachmentMiniAction(inboxID: String) {
        clearViewFocus()
        goToMediaPicker()
        viewModel.getAndUpdateActiveMediaPickerInboxID(inboxID)
        viewModel.onClickAddAttachmentMiniAction(inboxID)
    }

    override fun onTextAreaGainFocus(inboxID: String, view: View) {
        showSoftKeyboard(view)
        viewModel.onReviewItemTextAreaGainFocus(inboxID)
    }

    override fun onTextAreaLostFocus(inboxID: String, view: View, text: String) {
        hideSoftKeyboard(view)
        viewModel.onReviewItemTextAreaLostFocus(inboxID, text)
    }

    override fun onExpandTextArea(inboxID: String, text: String) {
        viewModel.onExpandTextArea(inboxID, text)
    }

    override fun onSingleTapToDismissKeyboard() {
        clearViewFocus()
    }

    override fun onAddMediaClicked(inboxID: String, enabled: Boolean) {
        if (enabled) {
            val pickedMedia = viewModel.getReviewItemMedia(inboxID)
            viewModel.getAndUpdateActiveMediaPickerInboxID(inboxID)
            goToMediaPicker(pickedMedia)
        } else {
            viewModel.enqueueToasterDisabledAddMoreMedia()
        }
    }

    override fun onRemoveMediaClicked(inboxID: String, media: CreateReviewMediaUiModel) {
        viewModel.onRemoveMedia(inboxID, media)
    }

    override fun onRetryUploadClicked(inboxID: String) {
        viewModel.onRetryUploadClicked(inboxID)
    }

    override fun onReviewItemImpressed(inboxID: String) {
        viewModel.onReviewItemImpressed(inboxID)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews() {
        binding?.rvBulkReviewItems?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.rvBulkReviewItems?.adapter = adapter
        binding?.rvBulkReviewItems?.setOnTouchListener { _, e ->
            gestureDetector?.onTouchEvent(e)
            false
        }
        binding?.globalErrorBulkReview?.setActionClickListener {
            viewModel.getData()
        }
    }

    private fun setupToolbar() {
        binding?.headerBulkReview?.setTitle(R.string.title_activity_bulk_review)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
            setSupportActionBar(binding?.headerBulkReview)
        }
    }

    private fun setupInsetListener() {
        binding?.root?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
                val stickyButtonHeight = binding?.widgetBulkReviewStickyButton?.height.orZero()
                val bottom = imeInsets.bottom - stickyButtonHeight - systemBarsInsets.bottom
                binding?.root?.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    systemBarsInsets.bottom
                )
                binding?.rvBulkReviewItems?.setPadding(
                    Int.ZERO,
                    Int.ZERO,
                    Int.ZERO,
                    bottom.coerceAtLeast(Int.ZERO)
                )
                viewModel.findFocusedReviewItemVisitable()?.let { (index, _) ->
                    binding?.rvBulkReviewItems?.smoothSnapToPosition(index, SNAP_TO_START)
                }
                if (imeInsets.bottom.isZero()) clearViewFocus()
                insets
            }
        }
    }

    private fun initUiState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.getData()
        } else {
            val cacheManagerId = savedInstanceState.getString(BULK_REVIEW_KEY_CACHE_MANAGER_ID).orEmpty()
            val cacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)
            viewModel.onRestoreInstanceState(cacheManager)
        }
    }

    private fun collectBulkReviewPageUiState() {
        collectLatestWhenResumed(viewModel.bulkReviewPageUiState) {
            when (it) {
                is BulkReviewPageUiState.Error -> onBulkReviewPageError(it.throwable)
                is BulkReviewPageUiState.Loading -> onBulkReviewPageLoading()
                is BulkReviewPageUiState.Showing -> onBulkReviewPageShowing(
                    it.items,
                    it.stickyButtonUiState
                )
                is BulkReviewPageUiState.Submitting -> onBulkReviewPageSubmitting()
                is BulkReviewPageUiState.Cancelled -> onBulkReviewPageCancelled()
                is BulkReviewPageUiState.Submitted -> onBulkReviewPageSubmitted(it.userName)
            }
        }
    }

    private fun collectToasterQueue() {
        collectWhenResumed(viewModel.bulkReviewPageToasterQueue) {
            suspendCoroutine { cont ->
                binding?.root?.let { view ->
                    Toaster.build(
                        view,
                        it.message.getStringValueWithDefaultParam(view.context),
                        it.duration,
                        it.type,
                        it.actionText.getStringValue(view.context)
                    ) { _ ->
                        viewModel.onToasterCtaClicked(it)
                    }.run {
                        anchorView = binding?.widgetBulkReviewStickyButton
                        addCallback(object : Snackbar.Callback() {
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

    private fun collectBulkReviewRemoveReviewItemDialogUiState() {
        collectLatestWhenResumed(viewModel.removeReviewItemDialogUiState) {
            dialogHandler.updateRemoveReviewItemDialog(it)
        }
    }

    private fun collectBulkReviewCancelReviewSubmissionDialogUiState() {
        collectLatestWhenResumed(viewModel.cancelReviewSubmissionDialogUiState) {
            dialogHandler.updateCancelReviewSubmissionDialog(it)
        }
    }

    private fun collectBulkReviewBadRatingCategoryBottomSheetUiState() {
        collectLatestWhenResumed(viewModel.badRatingCategoryBottomSheetUiState) {
            when (it) {
                is BulkReviewBadRatingCategoryBottomSheetUiState.Dismissed -> {
                    bottomSheetHandler.dismissBulkReviewBadRatingCategoryBottomSheet()
                }
                is BulkReviewBadRatingCategoryBottomSheetUiState.Showing -> {
                    bottomSheetHandler.showBulkReviewBadRatingCategoryBottomSheet(
                        it.badRatingCategories
                    )
                }
            }
        }
    }

    private fun collectBulkReviewExpandedTextAreaBottomSheetUiState() {
        collectLatestWhenResumed(viewModel.expandedTextAreaBottomSheetUiState) {
            when (it) {
                is BulkReviewExpandedTextAreaBottomSheetUiState.Dismissed -> {
                    bottomSheetHandler.dismissBulkReviewExpandedTextAreaBottomSheet()
                }
                is BulkReviewExpandedTextAreaBottomSheetUiState.Showing -> {
                    bottomSheetHandler.showBulkReviewExpandedTextAreaBottomSheet(
                        hint = it.hint,
                        text = it.text,
                        title = context?.let { context ->
                            it.title.getStringValueWithDefaultParam(context)
                        }.orEmpty()
                    )
                }
            }
        }
    }

    private suspend fun onBulkReviewPageError(throwable: Throwable?) {
        suspendCoroutine<Unit> { continuation ->
            binding?.run {
                setupGlobalErrorUI(throwable)
                globalErrorBulkReview.show()
                animatePageUiStateChange(
                    rvBulkReviewItemsAlphaTarget = 0F,
                    loaderBulkReviewAlphaTarget = 0F,
                    widgetBulkReviewStickyButtonAlphaTarget = 0F,
                    widgetBulkReviewSubmitLoaderAlphaTarget = 0F,
                    globalErrorBulkReviewAlphaTarget = 1F
                ) {
                    rvBulkReviewItems.gone()
                    loaderBulkReview.gone()
                    widgetBulkReviewStickyButton.gone()
                    widgetBulkReviewSubmitLoader.gone()
                    continuation.resume(Unit)
                }
            }
        }
    }

    private suspend fun onBulkReviewPageLoading() {
        suspendCoroutine<Unit> { continuation ->
            binding?.run {
                loaderBulkReview.show()
                animatePageUiStateChange(
                    rvBulkReviewItemsAlphaTarget = 0F,
                    loaderBulkReviewAlphaTarget = 1F,
                    widgetBulkReviewStickyButtonAlphaTarget = 0F,
                    widgetBulkReviewSubmitLoaderAlphaTarget = 0F,
                    globalErrorBulkReviewAlphaTarget = 0F
                ) {
                    rvBulkReviewItems.gone()
                    widgetBulkReviewStickyButton.gone()
                    globalErrorBulkReview.gone()
                    widgetBulkReviewSubmitLoader.gone()
                    continuation.resume(Unit)
                }
            }
        }
    }

    private suspend fun onBulkReviewPageShowing(
        bulkReviewVisitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
        stickyButtonUiState: BulkReviewStickyButtonUiState
    ) {
        suspendCancellableCoroutine<Unit> { continuation ->
            binding?.run {
                rvBulkReviewItems.post {
                    if (continuation.isActive) {
                        adapter.updateItems(bulkReviewVisitableList)
                        widgetBulkReviewStickyButton.show()
                        rvBulkReviewItems.show()
                        widgetBulkReviewStickyButton.updateUiState(stickyButtonUiState)
                        widgetBulkReviewStickyButton.setListener(stickyButtonListener)
                        animatePageUiStateChange(
                            rvBulkReviewItemsAlphaTarget = 1F,
                            loaderBulkReviewAlphaTarget = 0F,
                            widgetBulkReviewStickyButtonAlphaTarget = 1F,
                            widgetBulkReviewSubmitLoaderAlphaTarget = 0F,
                            globalErrorBulkReviewAlphaTarget = 0F
                        ) {
                            loaderBulkReview.gone()
                            globalErrorBulkReview.gone()
                            widgetBulkReviewSubmitLoader.gone()
                            coachMarkHandler.tryShowCoachMark()
                            continuation.resume(Unit)
                        }
                    }
                }
            }
        }
    }

    private suspend fun onBulkReviewPageSubmitting() {
        suspendCoroutine<Unit> { continuation ->
            binding?.run {
                widgetBulkReviewSubmitLoader.show()
                coachMarkHandler.tryDismissCoachMark()
                animatePageUiStateChange(
                    rvBulkReviewItemsAlphaTarget = 0F,
                    loaderBulkReviewAlphaTarget = 0F,
                    widgetBulkReviewStickyButtonAlphaTarget = 0F,
                    widgetBulkReviewSubmitLoaderAlphaTarget = 1F,
                    globalErrorBulkReviewAlphaTarget = 0F
                ) {
                    rvBulkReviewItems.gone()
                    widgetBulkReviewStickyButton.gone()
                    globalErrorBulkReview.gone()
                    loaderBulkReview.gone()
                    continuation.resume(Unit)
                }
            }
        }
    }

    private fun onBulkReviewPageCancelled() {
        activity?.run {
            if (isTaskRoot) {
                backToReviewPendingPage(String.EMPTY)
            } else {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    private fun onBulkReviewPageSubmitted(userName: String) {
        activity?.run {
            if (isTaskRoot) {
                backToReviewPendingPage(
                    getString(R.string.bulk_review_submission_success_message, userName)
                )
            } else {
                val intent = Intent()
                intent.putExtra(
                    ReviewInboxConstants.BULK_CREATE_REVIEW_MESSAGE,
                    getString(R.string.bulk_review_submission_success_message, userName)
                )
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun setupGlobalErrorUI(throwable: Throwable?) {
        binding?.run {
            when (throwable) {
                is UnknownHostException, is SocketTimeoutException -> {
                    globalErrorBulkReview.setType(GlobalError.NO_CONNECTION)
                    globalErrorBulkReview.errorSecondaryAction.show()
                }
                else -> {
                    globalErrorBulkReview.setType(GlobalError.SERVER_ERROR)
                    globalErrorBulkReview.errorSecondaryAction.gone()
                }
            }
            globalErrorBulkReview.errorDescription.text = ErrorHandler.getErrorMessage(
                context,
                throwable
            )
        }
    }

    private fun getInputMethodManager(): InputMethodManager? {
        return activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    }

    private fun showSoftKeyboard(view: View) {
        getInputMethodManager()?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftKeyboard(view: View) {
        view.windowToken?.let { windowToken ->
            getInputMethodManager()?.hideSoftInputFromWindow(windowToken, Int.ZERO)
        }
    }

    private fun clearViewFocus() {
        binding?.root?.findFocus()?.clearFocus()
    }

    private fun goToMediaPicker(pickedMedia: List<String> = emptyList()) {
        context?.let {
            val intent = MediaPicker.intent(it) {
                pageSource(PageSource.Review)
                modeType(ModeType.COMMON)
                maxMediaItem(MAX_IMAGE_COUNT)
                maxVideoItem(MAX_VIDEO_COUNT)
                maxVideoFileSize(MAX_VIDEO_SIZE_BYTE)
                includeMedias(pickedMedia)
            }
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun animatePageUiStateChange(
        rvBulkReviewItemsAlphaTarget: Float,
        loaderBulkReviewAlphaTarget: Float,
        widgetBulkReviewStickyButtonAlphaTarget: Float,
        widgetBulkReviewSubmitLoaderAlphaTarget: Float,
        globalErrorBulkReviewAlphaTarget: Float,
        onAnimationEnd: () -> Unit
    ) {
        binding?.run {
            val currentRvBulkReviewItemsAlpha = rvBulkReviewItems.alpha
            val currentLoaderBulkReviewAlpha = loaderBulkReview.alpha
            val currentWidgetBulkReviewStickyButtonAlpha = widgetBulkReviewStickyButton.alpha
            val currentWidgetBulkReviewSubmitLoaderAlpha = widgetBulkReviewSubmitLoader.alpha
            val currentGlobalErrorBulkReviewAlpha = globalErrorBulkReview.alpha
            val needToAnimate = currentRvBulkReviewItemsAlpha != rvBulkReviewItemsAlphaTarget ||
                currentLoaderBulkReviewAlpha != loaderBulkReviewAlphaTarget ||
                currentWidgetBulkReviewStickyButtonAlpha != widgetBulkReviewStickyButtonAlphaTarget ||
                currentWidgetBulkReviewSubmitLoaderAlpha != widgetBulkReviewSubmitLoaderAlphaTarget ||
                currentGlobalErrorBulkReviewAlpha != globalErrorBulkReviewAlphaTarget
            if (needToAnimate) {
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(
                    ObjectAnimator.ofFloat(
                        rvBulkReviewItems,
                        View.ALPHA,
                        currentRvBulkReviewItemsAlpha,
                        rvBulkReviewItemsAlphaTarget
                    ),
                    ObjectAnimator.ofFloat(
                        loaderBulkReview,
                        View.ALPHA,
                        currentLoaderBulkReviewAlpha,
                        loaderBulkReviewAlphaTarget
                    ),
                    ObjectAnimator.ofFloat(
                        widgetBulkReviewStickyButton,
                        View.ALPHA,
                        currentWidgetBulkReviewStickyButtonAlpha,
                        widgetBulkReviewStickyButtonAlphaTarget
                    ),
                    ObjectAnimator.ofFloat(
                        widgetBulkReviewSubmitLoader,
                        View.ALPHA,
                        currentWidgetBulkReviewSubmitLoaderAlpha,
                        widgetBulkReviewSubmitLoaderAlphaTarget
                    ),
                    ObjectAnimator.ofFloat(
                        globalErrorBulkReview,
                        View.ALPHA,
                        currentGlobalErrorBulkReviewAlpha,
                        globalErrorBulkReviewAlphaTarget
                    )
                )
                animatorSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        onAnimationEnd()
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                animatorSet.duration = BaseReviewCustomView.ANIMATION_DURATION
                animatorSet.interpolator = PathInterpolatorCompat.create(
                    BaseReviewCustomView.CUBIC_BEZIER_X1,
                    BaseReviewCustomView.CUBIC_BEZIER_Y1,
                    BaseReviewCustomView.CUBIC_BEZIER_X2,
                    BaseReviewCustomView.CUBIC_BEZIER_Y2
                )
                animatorSet.start()
            } else {
                onAnimationEnd()
            }
        } ?: onAnimationEnd()
    }

    private fun backToReviewPendingPage(message: String) {
        activity?.run {
            TaskStackBuilder
                .create(this)
                .addNextIntent(RouteManager.getIntent(this, ApplinkConst.HOME))
                .addNextIntent(RouteManager.getIntent(this, ApplinkConst.INBOX))
                .addNextIntent(
                    ReviewInboxActivity.createNewInstance(
                        context = this,
                        tab = null,
                        source = ReviewInboxConstants.SOURCE_REVIEW_INBOX
                    ).apply {
                        if (message.isNotBlank()) {
                            putExtra(ReviewInboxConstants.BULK_CREATE_REVIEW_MESSAGE, message)
                        }
                    }
                )
                .startActivities()
        }
    }

    private inner class ActivityResultHandler {
        private fun handleMediaPickerResult(data: Intent) {
            val result = MediaPicker.result(data)
            viewModel.onReceiveMediaPickerResult(result.originalPaths)
        }

        fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {
            when (requestCode) {
                CreateReviewFragment.REQUEST_CODE_IMAGE -> {
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        handleMediaPickerResult(data)
                    }
                }
                else -> super@BulkReviewFragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private inner class BottomSheetHandler {

        private val badRatingCategoryBottomSheetListener = BadRatingCategoryBottomSheetListener()
        private val expandedTextAreaBottomSheetListener = ExpandedTextAreaBottomSheetListener()

        private fun getBulkReviewBadRatingCategoryBottomSheet(): BulkReviewBadRatingCategoryBottomSheet? {
            return (
                childFragmentManager.findFragmentByTag(
                    BulkReviewBadRatingCategoryBottomSheet.TAG
                ) as? BulkReviewBadRatingCategoryBottomSheet
                )
        }

        fun showBulkReviewBadRatingCategoryBottomSheet(
            badRatingCategories: List<BulkReviewBadRatingCategoryUiModel>
        ) {
            getBulkReviewBadRatingCategoryBottomSheet()?.setData(
                badRatingCategories
            ) ?: BulkReviewBadRatingCategoryBottomSheet().also {
                it.show(childFragmentManager, BulkReviewBadRatingCategoryBottomSheet.TAG) {
                    it.setData(badRatingCategories)
                    it.setListener(badRatingCategoryBottomSheetListener)
                }
            }
        }

        fun dismissBulkReviewBadRatingCategoryBottomSheet() {
            getBulkReviewBadRatingCategoryBottomSheet()?.dismiss()
        }

        private fun getBulkReviewExpandedTextAreaBottomSheet(): BulkReviewExpandedTextAreaBottomSheet? {
            return (
                childFragmentManager.findFragmentByTag(
                    BulkReviewExpandedTextAreaBottomSheet.TAG
                ) as? BulkReviewExpandedTextAreaBottomSheet
                )
        }

        fun showBulkReviewExpandedTextAreaBottomSheet(
            hint: StringRes,
            text: String,
            title: String
        ) {
            getBulkReviewExpandedTextAreaBottomSheet()?.apply {
                setTitle(title)
                setData(hint, text)
                setToasterQueue(viewModel.expandedTextAreaToasterQueue)
            } ?: BulkReviewExpandedTextAreaBottomSheet().let {
                it.show(childFragmentManager, BulkReviewExpandedTextAreaBottomSheet.TAG) {
                    it.setTitle(title)
                    it.setData(hint, text)
                    it.setToasterQueue(viewModel.expandedTextAreaToasterQueue)
                    it.setListener(expandedTextAreaBottomSheetListener)
                }
            }
        }

        fun dismissBulkReviewExpandedTextAreaBottomSheet() {
            getBulkReviewExpandedTextAreaBottomSheet()?.dismiss()
        }
    }

    private inner class CoachMarkHandler {

        private var coachMark: CoachMark2? = null

        fun tryShowCoachMark() {
            context?.let { context ->
                if (!CoachMarkPreference.hasShown(context, BULK_REVIEW_COACH_MARK_TAG)) {
                    CoachMarkPreference.setShown(context, BULK_REVIEW_COACH_MARK_TAG, true)
                    coachMark = CoachMark2(context)
                    val anchorView = getAnchorView()
                    if (anchorView != null) {
                        val scrollListener = object : OnScrollListener() {
                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                if (getVisiblePercent(anchorView) == -1) {
                                    coachMark?.dismissCoachMark()
                                    binding?.rvBulkReviewItems?.removeOnScrollListener(this)
                                }
                            }
                        }
                        coachMark?.showCoachMark(
                            arrayListOf(
                                CoachMark2Item(
                                    anchorView = anchorView,
                                    title = String.EMPTY,
                                    description = context.getString(R.string.bulk_review_coach_mark_description)
                                )
                            )
                        )
                        binding?.rvBulkReviewItems?.addOnScrollListener(scrollListener)
                        coachMark?.setOnDismissListener {
                            binding?.rvBulkReviewItems?.removeOnScrollListener(scrollListener)
                        }
                    }
                }
            }
        }

        fun tryDismissCoachMark() {
            coachMark?.dismissCoachMark()
        }

        private fun getAnchorView(): View? {
            val layoutManager = binding?.rvBulkReviewItems?.layoutManager as? LinearLayoutManager
            val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition().orZero().coerceAtLeast(Int.ZERO)
            val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPosition().orZero().coerceAtLeast(Int.ZERO)
            for (i in firstVisibleItemPosition..lastVisibleItemPosition) {
                layoutManager
                    ?.findViewByPosition(i)
                    ?.findViewById<View>(R.id.divider_bulk_write_review_form_actions)
                    ?.let { return it }
            }
            return null
        }
    }

    private inner class DialogHandler {
        private val removeReviewItemDialog by lazy(LazyThreadSafetyMode.NONE) {
            BulkReviewRemoveReviewItemDialog(requireContext(), RemoveReviewItemDialogListener())
        }
        private val cancelReviewSubmissionDialog by lazy(LazyThreadSafetyMode.NONE) {
            BulkReviewCancelReviewSubmissionDialog(requireContext(), ReviewCancelReviewSubmissionDialogListener())
        }

        fun updateRemoveReviewItemDialog(uiState: BulkReviewRemoveReviewItemDialogUiState) {
            removeReviewItemDialog.updateUiState(uiState)
        }

        fun updateCancelReviewSubmissionDialog(uiState: BulkReviewCancelReviewSubmissionDialogUiState) {
            cancelReviewSubmissionDialog.updateUiState(uiState)
        }
    }

    private inner class BadRatingCategoryBottomSheetListener : BulkReviewBadRatingCategoryBottomSheet.Listener {

        override fun onApplyBadRatingCategory() {
            viewModel.onApplyBadRatingCategory()
        }

        override fun onBadRatingCategorySelected(position: Int, badRatingCategoryID: String, reason: String) {
            viewModel.onBadRatingCategorySelectionChanged(position, badRatingCategoryID, reason, true)
        }

        override fun onBadRatingCategoryUnselected(position: Int, badRatingCategoryID: String, reason: String) {
            viewModel.onBadRatingCategorySelectionChanged(position, badRatingCategoryID, reason, false)
        }

        override fun onBadRatingCategoryImpressed(position: Int, reason: String) {
            viewModel.onBadRatingCategoryImpressed(position, reason)
        }
    }

    private inner class ExpandedTextAreaBottomSheetListener : BulkReviewExpandedTextAreaBottomSheet.Listener {
        override fun onDismiss(text: String) {
            viewModel.onDismissExpandedTextAreaBottomSheet(text)
        }
    }

    private inner class RemoveReviewItemDialogListener : BulkReviewRemoveReviewItemDialog.Listener {
        override fun onConfirmRemoveReviewItem(
            inboxID: String,
            title: String,
            subtitle: String
        ) {
            viewModel.onConfirmRemoveReviewItem(inboxID, title, subtitle)
        }

        override fun onCancelRemoveReviewItem(
            title: String,
            subtitle: String
        ) {
            viewModel.onCancelRemoveReviewItem(title, subtitle)
        }

        override fun onRemoveReviewItemDialogImpressed(
            inboxID: String,
            title: String,
            subtitle: String
        ) {
            viewModel.onRemoveReviewItemDialogImpressed(inboxID, title, subtitle)
        }
    }

    private inner class ReviewCancelReviewSubmissionDialogListener : BulkReviewCancelReviewSubmissionDialog.Listener {
        override fun onConfirmCancelReviewSubmission() {
            viewModel.onConfirmCancelReviewSubmission()
        }

        override fun onCancelReviewSubmissionCancellation() {
            viewModel.onCancelReviewSubmissionCancellation()
        }
    }

    private inner class BulkReviewGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            clearViewFocus()
            return false
        }
    }

    private inner class BulkReviewStickyButtonListener : WidgetBulkReviewStickyButton.Listener {
        override fun onAnonymousCheckChanged(checked: Boolean) {
            viewModel.onAnonymousCheckChanged(checked)
        }

        override fun onClickSubmitReview() {
            viewModel.onSubmitReviews()
        }
    }
}
