package com.tokopedia.review.feature.media.gallery.detailed.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.common.extension.collectWhenResumed
import com.tokopedia.review.databinding.ActivityDetailedReviewMediaGalleryBinding
import com.tokopedia.review.feature.media.detail.presentation.fragment.ReviewDetailFragment
import com.tokopedia.review.feature.media.gallery.base.presentation.fragment.ReviewMediaGalleryFragment
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.bottomsheet.ActionMenuBottomSheet
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.ActionMenuBottomSheetUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.player.controller.presentation.fragment.ReviewMediaPlayerControllerFragment
import com.tokopedia.reviewcommon.extension.hideSystemUI
import com.tokopedia.reviewcommon.extension.intersectWith
import com.tokopedia.reviewcommon.extension.showSystemUI
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
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
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

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
    private val bottomSheetHandler = BottomSheetHandler()
    private val connectivityStatusListener = ConnectivityStatusListener()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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

    override fun dispatchTouchEvent(e: MotionEvent?): Boolean {
        return if (
            e != null && !e.isAboveCloseButton() && !e.isAboveKebabButton() &&
            !e.isAboveController() && !e.isAboveReviewDetail() &&
            gestureDetector?.onTouchEvent(e) == true
        ) {
            true
        } else super.dispatchTouchEvent(e)
    }

    override fun onBackPressed() {
        if (sharedReviewMediaGalleryViewModel.orientationUiState.value.isLandscape()) {
            sharedReviewMediaGalleryViewModel.requestPortraitMode()
        } else {
            finishActivity()
        }
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
            ViewCompat.setOnApplyWindowInsetsListener(it.icReviewMediaGalleryClose) { v, insets ->
                v.setMargin(
                    left = 12.toPx(),
                    top = 12.toPx() + insets.systemWindowInsetTop,
                    right = Int.ZERO,
                    bottom = insets.systemWindowInsetBottom
                )
                insets
            }
            ViewCompat.setOnApplyWindowInsetsListener(it.icReviewMediaGalleryKebab) { v, insets ->
                if (sharedReviewMediaGalleryViewModel.orientationUiState.value.isPortrait()) {
                    v.setMargin(
                        left = Int.ZERO,
                        top = 12.toPx() + insets.systemWindowInsetTop,
                        right = 12.toPx(),
                        bottom = insets.systemWindowInsetBottom
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
    }

    private fun collectToolbarUiStateUpdate() {
        collectLatestWhenResumed(
            combine(
                sharedReviewMediaGalleryViewModel.orientationUiState,
                sharedReviewMediaGalleryViewModel.overlayVisibility,
                sharedReviewMediaGalleryViewModel.currentReviewDetail,
            ) { orientationUiState, overlayVisibility, currentReviewDetail ->
                Triple(orientationUiState, overlayVisibility, currentReviewDetail)
            }
        ) {
            val showOverlay = it.second
            val isInPortrait = it.first.isPortrait()
            val isReportable = it.third?.isReportable.orFalse()
            binding?.icReviewMediaGalleryClose?.showWithCondition(showOverlay)
            binding?.icReviewMediaGalleryKebab?.showWithCondition(
                showOverlay && isInPortrait && isReportable
            )
        }
    }

    private fun collectOrientationUiStateUpdate() {
        collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.orientationUiState) {
            requestedOrientation = when(it.orientation) {
                OrientationUiState.Orientation.LANDSCAPE -> {
                    enableFullscreen()
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
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

    private fun initUiStateCollectors() {
        collectToolbarUiStateUpdate()
        collectOrientationUiStateUpdate()
        collectActionMenuBottomSheetUiStateUpdate()
        collectToasterQueue()
        collectToasterActionClickEvent()
    }

    private fun MotionEvent.isAboveCloseButton(): Boolean {
        return binding?.icReviewMediaGalleryClose?.let { closeButton ->
            intersectWith(closeButton)
        } ?: false
    }

    private fun MotionEvent.isAboveKebabButton(): Boolean {
        return binding?.icReviewMediaGalleryKebab?.let { kebabButton ->
            intersectWith(kebabButton)
        } ?: false
    }

    private fun MotionEvent.isAboveController(): Boolean {
        return binding?.fragmentReviewGalleryController?.let { controller ->
            intersectWith(controller)
        } ?: false
    }

    private fun MotionEvent.isAboveReviewDetail(): Boolean {
        return binding?.fragmentReviewDetail?.let { reviewDetail ->
            intersectWith(reviewDetail)
        } ?: false
    }

    private inner class DetailedReviewMediaGalleryGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            sharedReviewMediaGalleryViewModel.toggleOverlayVisibility()
            return true
        }
    }

    private fun finishActivity() {
        if (sharedReviewMediaGalleryViewModel.hasSuccessToggleLikeStatus()) {
            setResult(Activity.RESULT_OK)
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
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(networkRequest, callback)
        }

        fun detachListener() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(callback)
        }

        private inner class Callback: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                sharedReviewMediaGalleryViewModel.updateWifiConnectivityStatus(connected = true)
            }

            override fun onLost(network: Network) {
                sharedReviewMediaGalleryViewModel.updateWifiConnectivityStatus(connected = false)
            }
        }
    }
}
