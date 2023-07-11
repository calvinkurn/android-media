package com.tokopedia.review.feature.media.gallery.detailed.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.common.extension.collectWhenResumed
import com.tokopedia.review.databinding.ActivityDetailedReviewMediaGalleryBinding
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTracker
import com.tokopedia.review.feature.media.detail.analytic.ReviewDetailTrackerConstant
import com.tokopedia.review.feature.media.detail.presentation.fragment.ReviewDetailFragment
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailUiModel
import com.tokopedia.review.feature.media.gallery.base.presentation.fragment.ReviewMediaGalleryFragment
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.bottomsheet.ActionMenuBottomSheet
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.ActionMenuBottomSheetUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.MediaCounterUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.util.DetailedReviewMediaGalleryStorage
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.player.controller.presentation.fragment.ReviewMediaPlayerControllerFragment
import com.tokopedia.reviewcommon.extension.hideSystemUI
import com.tokopedia.reviewcommon.extension.intersectWith
import com.tokopedia.reviewcommon.extension.showSystemUI
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DetailedReviewMediaGalleryActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val TOASTER_KEY_ERROR_GET_REVIEW_MEDIA = "ERROR_GET_REVIEW_MEDIA"
        const val KEY_CACHE_MANAGER_ID = "cacheManagerId"
        const val AUTO_HIDE_OVERLAY_DURATION = 5000L
        const val AUTO_HIDE_TOUCH_CLICKABLE_MARGIN = 16
        const val HEADER_HEIGHT_IN_PORTRAIT = 100
        const val HEADER_HEIGHT_IN_LANDSCAPE = 48
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var reviewDetailTracker: ReviewDetailTracker

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(ActivityDetailedReviewMediaGalleryBinding::bind)

    private var gestureDetector: GestureDetectorCompat? = null

    private var galleryFragment: ReviewMediaGalleryFragment? = null
        get() {
            return if (field == null) {
                supportFragmentManager.findFragmentByTag(
                    ReviewMediaGalleryFragment.TAG
                ) as? ReviewMediaGalleryFragment ?: ReviewMediaGalleryFragment().also { field = it }
            } else {
                field
            }
        }
    private var galleryControllerFragment: ReviewMediaPlayerControllerFragment? = null
        get() {
            return if (field == null) {
                supportFragmentManager.findFragmentByTag(
                    ReviewMediaPlayerControllerFragment.TAG
                ) as? ReviewMediaPlayerControllerFragment
                    ?: ReviewMediaPlayerControllerFragment().also { field = it }
            } else {
                field
            }
        }
    private var reviewDetailFragment: ReviewDetailFragment? = null
        get() {
            return if (field == null) {
                supportFragmentManager.findFragmentByTag(
                    ReviewDetailFragment.TAG
                ) as? ReviewDetailFragment ?: ReviewDetailFragment().also { field = it }
            } else {
                field
            }
        }

    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, detailedReviewMediaGalleryViewModelFactory).get(
            SharedReviewMediaGalleryViewModel::class.java
        )
    }

    private val gestureListener = DetailedReviewMediaGalleryGestureListener()
    private val autoHideOverlayHandler = AutoHideOverlayHandler()
    private val bottomSheetHandler = BottomSheetHandler()
    private val connectivityStatusListener = ConnectivityStatusListener()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initDetailReviewMediaGalleryStorage()
        initInjector()
        initUiState(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_review_media_gallery)
        gestureDetector = GestureDetectorCompat(this, gestureListener)
        setupInsetListener()
        setupMainLayout()
        setupFragments()
        initUiStateCollectors()
    }

    override fun onResume() {
        super.onResume()
        connectivityStatusListener.attachListener()
    }

    override fun onPause() {
        super.onPause()
        connectivityStatusListener.detachListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val cacheManager = SaveInstanceCacheManager(context = this, generateObjectId = true)
        sharedReviewMediaGalleryViewModel.saveState(cacheManager)
        outState.putString(KEY_CACHE_MANAGER_ID, cacheManager.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        DetailedReviewMediaGalleryStorage.clear()
    }

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        autoHideOverlayHandler.restartTimerIfAlreadyStarted()
        return if (
            e != null && !e.isAboveCloseButton() && !e.isAboveReviewerBasicInfo() &&
            !e.isAboveKebabButton() && !e.isAboveController() && !e.isAboveReviewDetail() &&
            !e.isAboveCounter() && gestureDetector?.onTouchEvent(e) == true
        ) {
            true
        } else {
            super.dispatchTouchEvent(e)
        }
    }

    override fun onBackPressed() {
        if (sharedReviewMediaGalleryViewModel.orientationUiState.value.isLandscape()) {
            sharedReviewMediaGalleryViewModel.requestPortraitMode()
        } else {
            finishActivity()
        }
    }

    private fun initDetailReviewMediaGalleryStorage() {
        val pageSource = intent.extras?.getInt(ReviewMediaGalleryRouter.EXTRAS_PAGE_SOURCE) ?: ReviewMediaGalleryRouter.PageSource.REVIEW
        DetailedReviewMediaGalleryStorage.pageSource = pageSource
    }

    private fun initInjector() {
        DetailedReviewMediaGalleryComponentInstance.getInstance(this).inject(this)
    }

    private fun initUiState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val cacheManagerId = intent.extras?.getString(
                ReviewMediaGalleryRouter.EXTRAS_CACHE_MANAGER_ID
            ).orEmpty()
            val cacheManager = SaveInstanceCacheManager(this, cacheManagerId)
            sharedReviewMediaGalleryViewModel.tryGetPreloadedData(cacheManager)
        } else {
            val cacheManagerId = savedInstanceState.getString(
                KEY_CACHE_MANAGER_ID
            ).orEmpty()
            val cacheManager = SaveInstanceCacheManager(this, cacheManagerId)
            sharedReviewMediaGalleryViewModel.restoreState(cacheManager)
        }
    }

    private fun setupFragments() {
        attachReviewDetailFragment()
        attachGalleryControllerFragment()
        attachGalleryFragment()
    }

    private fun attachGalleryFragment() {
        galleryFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentReviewGallery, it, ReviewMediaGalleryFragment.TAG)
                .commitAllowingStateLoss()
        }
    }

    private fun attachGalleryControllerFragment() {
        galleryControllerFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentReviewGalleryController,
                    it,
                    ReviewMediaPlayerControllerFragment.TAG
                )
                .commitAllowingStateLoss()
        }
    }

    private fun attachReviewDetailFragment() {
        reviewDetailFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentReviewDetail, it, ReviewDetailFragment.TAG)
                .commitAllowingStateLoss()
        }
    }

    private fun setupMainLayout() {
        setupBackground()
        binding?.setupToolbar()
        binding?.setupCounter()
    }

    private fun enableFullscreen() {
        hideSystemUI()
    }

    private fun disableFullscreen() {
        showSystemUI()
    }

    private fun setupBackground() {
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
            )
        )
    }

    private fun setupInsetListener() {
        binding?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it.root) { v, insets ->
                if (sharedReviewMediaGalleryViewModel.orientationUiState.value.isPortrait()) {
                    v.setMargin(
                        left = Int.ZERO,
                        top = Int.ZERO,
                        right = Int.ZERO,
                        bottom = insets.systemWindowInsetBottom
                    )
                } else {
                    v.setMargin(
                        left = Int.ZERO,
                        top = Int.ZERO,
                        right = Int.ZERO,
                        bottom = Int.ZERO
                    )
                }
                insets
            }
        }
    }

    private fun ActivityDetailedReviewMediaGalleryBinding.setupToolbar() {
        icReviewMediaGalleryClose.setOnClickListener { finishActivity() }
        icReviewMediaGalleryKebab.setOnClickListener {
            sharedReviewMediaGalleryViewModel.showActionMenuBottomSheet()
        }
        ivBgHeaderReviewMediaGallery.setImageResource(R.drawable.bg_header)
    }

    private fun ActivityDetailedReviewMediaGalleryBinding.setupCounter() {
        layoutReviewMediaGalleryItemCounter.setBackgroundResource(R.drawable.bg_review_media_gallery_item_counter)
    }

    private fun ActivityDetailedReviewMediaGalleryBinding.setupReviewerBasicInfo(
        reviewDetailUiModel: ReviewDetailUiModel?,
        orientationUiState: OrientationUiState
    ) {
        if (reviewDetailUiModel == null || orientationUiState.isLandscape()) {
            basicInfoReviewMediaGallery.gone()
        } else {
            basicInfoReviewMediaGallery.invertColors()
            basicInfoReviewMediaGallery.hideThreeDots()
            basicInfoReviewMediaGallery.hideRating()
            basicInfoReviewMediaGallery.hideCreateTime()
            basicInfoReviewMediaGallery.hideVariant()
            basicInfoReviewMediaGallery.setCredibilityData(
                isProductReview = true,
                isAnonymous = reviewDetailUiModel.basicInfoUiModel.anonymous,
                userId = reviewDetailUiModel.basicInfoUiModel.userId,
                feedbackId = reviewDetailUiModel.feedbackID
            )
            basicInfoReviewMediaGallery.setReviewerImage(reviewDetailUiModel.basicInfoUiModel.profilePicture)
            basicInfoReviewMediaGallery.setReviewerName(reviewDetailUiModel.basicInfoUiModel.reviewerName)
            basicInfoReviewMediaGallery.setReviewerLabel(reviewDetailUiModel.basicInfoUiModel.reviewerLabel)
            basicInfoReviewMediaGallery.setStatsString(reviewDetailUiModel.basicInfoUiModel.reviewerStatsSummary)
            basicInfoReviewMediaGallery.setListeners(ReviewerBasicInfoListener(), null)
            basicInfoReviewMediaGallery.show()
        }
    }

    private fun collectToolbarUiStateUpdate() {
        collectLatestWhenResumed(
            combine(
                sharedReviewMediaGalleryViewModel.orientationUiState,
                sharedReviewMediaGalleryViewModel.overlayVisibility,
                sharedReviewMediaGalleryViewModel.currentReviewDetail
            ) { orientationUiState, overlayVisibility, currentReviewDetail ->
                Triple(orientationUiState, overlayVisibility, currentReviewDetail)
            }
        ) { (orientationUiState, showOverlay, currentReviewDetail) ->
            val isInPortrait = orientationUiState.isPortrait()
            val isReportable = currentReviewDetail?.isReportable.orFalse()
            binding?.headerReviewMediaGallery?.apply {
                showWithCondition(showOverlay)
                val layoutParamsCopy = layoutParams
                layoutParamsCopy.height = if (isInPortrait) {
                    HEADER_HEIGHT_IN_PORTRAIT.toPx()
                } else {
                    HEADER_HEIGHT_IN_LANDSCAPE.toPx()
                }
                layoutParams = layoutParamsCopy
            }
            if (isInPortrait && currentReviewDetail != null) {
                binding?.ivBgHeaderReviewMediaGallery?.show()
            } else {
                binding?.ivBgHeaderReviewMediaGallery?.gone()
            }
            binding?.icReviewMediaGalleryClose?.showWithCondition(showOverlay)
            if (showOverlay && isInPortrait && isReportable) {
                binding?.icReviewMediaGalleryKebab?.show()
            } else {
                binding?.icReviewMediaGalleryKebab?.invisible()
            }
        }
    }

    private fun collectOrientationUiStateUpdate() {
        collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.orientationUiState) {
            requestedOrientation = when (it.orientation) {
                OrientationUiState.Orientation.LANDSCAPE -> {
                    enableFullscreen()
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                else -> {
                    disableFullscreen()
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
        }
    }

    private fun collectActionMenuBottomSheetUiStateUpdate() {
        collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.actionMenuBottomSheetUiState) {
            if (it is ActionMenuBottomSheetUiState.Showing) {
                bottomSheetHandler.showActionMenuBottomSheet()
            }
        }
    }

    private fun collectToasterQueue() {
        collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.toasterQueue) {
            binding?.root?.let { view ->
                Toaster.build(
                    view,
                    it.message.getStringValueWithDefaultParam(view.context),
                    it.duration,
                    it.type,
                    it.actionText.getStringValue(view.context)
                ) { _ ->
                    sharedReviewMediaGalleryViewModel.toasterEventActionClicked(it.key)
                }.show()
            }
        }
    }

    private fun collectToasterActionClickEvent() {
        collectWhenResumed(sharedReviewMediaGalleryViewModel.toasterEventActionClickQueue) {
            if (it == TOASTER_KEY_ERROR_GET_REVIEW_MEDIA) {
                sharedReviewMediaGalleryViewModel.retryGetReviewMedia()
            }
        }
    }

    private fun collectOverlayVisibilityUpdate() {
        collectWhenResumed(
            combine(
                sharedReviewMediaGalleryViewModel.overlayVisibility,
                sharedReviewMediaGalleryViewModel.orientationUiState,
                sharedReviewMediaGalleryViewModel.isPlayingVideo
            ) { overlayVisibility, orientationUiState, isPlayingVideo ->
                overlayVisibility && orientationUiState.isLandscape() && isPlayingVideo
            }
        ) { startAutoHideTimer ->
            if (startAutoHideTimer) {
                autoHideOverlayHandler.restartTimer()
            } else {
                autoHideOverlayHandler.stopTimer()
            }
        }
    }

    private fun collectMediaCounterUpdate() {
        collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.mediaCounterUiState) {
            when (it) {
                is MediaCounterUiState.Hidden -> {
                    binding?.layoutReviewMediaGalleryItemCounter?.gone()
                }
                is MediaCounterUiState.Loading -> {
                    binding?.layoutReviewMediaGalleryItemCounter?.show()
                    binding?.loaderReviewMediaGalleryItemCounter?.show()
                    binding?.tvReviewMediaGalleryItemCounter?.gone()
                }
                is MediaCounterUiState.Showing -> {
                    binding?.layoutReviewMediaGalleryItemCounter?.show()
                    binding?.loaderReviewMediaGalleryItemCounter?.gone()
                    binding?.tvReviewMediaGalleryItemCounter?.run {
                        text = buildString {
                            append(it.count)
                            append("/")
                            append(it.total)
                        }
                        show()
                    }
                }
            }
        }
    }

    private fun collectReviewerBasicInfo() {
        collectLatestWhenResumed(
            combine(
                sharedReviewMediaGalleryViewModel.currentReviewDetail,
                sharedReviewMediaGalleryViewModel.orientationUiState
            ) { currentReviewDetail, orientationUiState ->
                currentReviewDetail to orientationUiState
            }
        ) { (currentReviewDetail, orientationUiState) ->
            binding?.setupReviewerBasicInfo(currentReviewDetail, orientationUiState)
        }
    }

    private fun initUiStateCollectors() {
        collectToolbarUiStateUpdate()
        collectOrientationUiStateUpdate()
        collectActionMenuBottomSheetUiStateUpdate()
        collectToasterQueue()
        collectToasterActionClickEvent()
        collectOverlayVisibilityUpdate()
        collectMediaCounterUpdate()
        collectReviewerBasicInfo()
    }

    private fun MotionEvent.isAboveCloseButton(): Boolean {
        return binding?.icReviewMediaGalleryClose?.let { closeButton ->
            intersectWith(closeButton, AUTO_HIDE_TOUCH_CLICKABLE_MARGIN.toPx().toLong())
        } ?: false
    }

    private fun MotionEvent.isAboveReviewerBasicInfo(): Boolean {
        return binding?.basicInfoReviewMediaGallery?.let { closeButton ->
            intersectWith(closeButton, AUTO_HIDE_TOUCH_CLICKABLE_MARGIN.toPx().toLong())
        } ?: false
    }

    private fun MotionEvent.isAboveKebabButton(): Boolean {
        return binding?.icReviewMediaGalleryKebab?.let { kebabButton ->
            intersectWith(kebabButton, AUTO_HIDE_TOUCH_CLICKABLE_MARGIN.toPx().toLong())
        } ?: false
    }

    private fun MotionEvent.isAboveController(): Boolean {
        return binding?.fragmentReviewGalleryController?.let { controller ->
            intersectWith(controller, AUTO_HIDE_TOUCH_CLICKABLE_MARGIN.toPx().toLong())
        } ?: false
    }

    private fun MotionEvent.isAboveReviewDetail(): Boolean {
        return binding?.fragmentReviewDetail?.let { reviewDetail ->
            intersectWith(reviewDetail, AUTO_HIDE_TOUCH_CLICKABLE_MARGIN.toPx().toLong())
        } ?: false
    }

    private fun MotionEvent.isAboveCounter(): Boolean {
        return binding?.layoutReviewMediaGalleryItemCounter?.let { reviewDetail ->
            intersectWith(reviewDetail, AUTO_HIDE_TOUCH_CLICKABLE_MARGIN.toPx().toLong())
        } ?: false
    }

    private inner class DetailedReviewMediaGalleryGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            sharedReviewMediaGalleryViewModel.toggleOverlayVisibility()
            return true
        }
    }

    private fun finishActivity() {
        if (sharedReviewMediaGalleryViewModel.hasSuccessToggleLikeStatus()) {
            setResult(
                Activity.RESULT_OK,
                ReviewMediaGalleryRouter.setResultData(
                    sharedReviewMediaGalleryViewModel.getFeedbackId(),
                    sharedReviewMediaGalleryViewModel.getLikeStatus(),
                )
            )
        }
        finish()
    }

    private inner class BottomSheetHandler {
        private var actionMenuBottomSheet: ActionMenuBottomSheet? = null

        private fun createActionMenuBottomSheet(): ActionMenuBottomSheet {
            return ActionMenuBottomSheet()
        }

        private fun getAddedActionMenuBottomSheet(): ActionMenuBottomSheet? {
            return supportFragmentManager.findFragmentByTag(
                ActionMenuBottomSheet.TAG
            ) as? ActionMenuBottomSheet
        }

        private fun getActionMenuBottomSheet(): ActionMenuBottomSheet {
            return actionMenuBottomSheet ?: getAddedActionMenuBottomSheet()
                ?: createActionMenuBottomSheet()
        }

        fun showActionMenuBottomSheet() {
            getActionMenuBottomSheet().run {
                if (!isAdded) {
                    show(supportFragmentManager, ActionMenuBottomSheet.TAG)
                }
            }
        }
    }

    private inner class ConnectivityStatusListener {
        private val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        private val callback = Callback()

        fun attachListener() {
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(networkRequest, callback)
        }

        fun detachListener() {
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(callback)
        }

        private inner class Callback : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                sharedReviewMediaGalleryViewModel.updateWifiConnectivityStatus(connected = true)
            }

            override fun onLost(network: Network) {
                sharedReviewMediaGalleryViewModel.updateWifiConnectivityStatus(connected = false)
            }
        }
    }

    private inner class AutoHideOverlayHandler {
        private val timer by lazy(LazyThreadSafetyMode.NONE) {
            object : CountDownTimer(AUTO_HIDE_OVERLAY_DURATION, AUTO_HIDE_OVERLAY_DURATION) {
                override fun onTick(millisUntilFinished: Long) {
                    // noop
                }

                override fun onFinish() {
                    sharedReviewMediaGalleryViewModel.hideOverlay()
                }
            }
        }

        private var started: Boolean = false

        fun startTimer() {
            timer.start()
            started = true
        }

        fun stopTimer() {
            timer.cancel()
            started = false
        }

        fun restartTimer() {
            stopTimer()
            startTimer()
        }

        fun restartTimerIfAlreadyStarted() {
            if (started) {
                stopTimer()
                startTimer()
            }
        }
    }

    private inner class ReviewerBasicInfoListener : ReviewBasicInfoListener {
        override fun onUserNameClicked(
            feedbackId: String,
            userId: String,
            statistics: String,
            label: String
        ) {
            val routed = RouteManager.route(
                this@DetailedReviewMediaGalleryActivity,
                Uri.parse(
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                        userId,
                        ReviewApplinkConst.REVIEW_CREDIBILITY_SOURCE_REVIEW_READING
                    )
                ).buildUpon()
                    .appendQueryParameter(
                        ReviewApplinkConst.PARAM_PRODUCT_ID,
                        sharedReviewMediaGalleryViewModel.getProductId()
                    ).build()
                    .toString()
            )
            if (routed) {
                reviewDetailTracker.trackClickReviewerName(
                    sharedReviewMediaGalleryViewModel.isFromGallery(),
                    sharedReviewMediaGalleryViewModel.currentReviewDetail.value?.feedbackID.orEmpty(),
                    userId,
                    statistics,
                    sharedReviewMediaGalleryViewModel.getProductId(),
                    sharedReviewMediaGalleryViewModel.getUserID(),
                    label,
                    ReviewDetailTrackerConstant.TRACKER_ID_CLICK_REVIEWER_NAME_FROM_DETAILED_REVIEW_MEDIA_GALLERY
                )
            }
        }
    }
}
