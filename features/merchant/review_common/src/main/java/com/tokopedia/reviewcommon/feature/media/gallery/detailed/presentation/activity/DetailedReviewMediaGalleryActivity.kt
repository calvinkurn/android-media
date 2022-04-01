package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.ActivityDetailedReviewMediaGalleryBinding
import com.tokopedia.reviewcommon.extension.hideSystemUI
import com.tokopedia.reviewcommon.extension.intersectWith
import com.tokopedia.reviewcommon.extension.showSystemUI
import com.tokopedia.reviewcommon.feature.media.detail.presentation.fragment.ReviewDetailFragment
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.fragment.ReviewMediaGalleryFragment
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.bottomsheet.ActionMenuBottomSheet
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.ActionMenuBottomSheetUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.reviewcommon.feature.media.player.controller.presentation.fragment.ReviewMediaPlayerControllerFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DetailedReviewMediaGalleryActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val EXTRAS_CACHE_MANAGER_ID = "extrasCacheManagerId"
        const val TOASTER_KEY_ERROR_GET_REVIEW_MEDIA = "ERROR_GET_REVIEW_MEDIA"
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(ActivityDetailedReviewMediaGalleryBinding::bind)

    private var toolbarUiStateCollectorJob: Job? = null
    private var orientationUiStateCollectorJob: Job? = null
    private var actionMenuBottomSheetUiStateCollectorJob: Job? = null
    private var toasterQueueCollectorJob: Job? = null
    private var toasterActionCLickEventCollectorJob: Job? = null
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

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        initUiState(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_review_media_gallery)
        gestureDetector = GestureDetectorCompat(this, gestureListener)
        setupInsetListener()
        setupMainLayout()
        setupFragments()
    }

    override fun onResume() {
        super.onResume()
        collectUiState()
    }

    override fun onPause() {
        super.onPause()
        cancelUiStateCollector()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        sharedReviewMediaGalleryViewModel.saveState(outState)
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
            super.onBackPressed()
        }
    }

    private fun initInjector() {
        DetailedReviewMediaGalleryComponentInstance.getInstance(this).inject(this)
    }

    private fun initUiState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val cacheManagerId = intent.extras?.getString(EXTRAS_CACHE_MANAGER_ID).orEmpty()
            val cacheManager = SaveInstanceCacheManager(this, cacheManagerId)
            sharedReviewMediaGalleryViewModel.tryGetPreloadedData(cacheManager)
        } else {
            sharedReviewMediaGalleryViewModel.restoreState(savedInstanceState)
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
            MethodChecker.getColor(
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
        icReviewMediaGalleryClose.setOnClickListener { finish() }
        icReviewMediaGalleryKebab.setOnClickListener {
            sharedReviewMediaGalleryViewModel.showActionMenuBottomSheet()
        }
    }

    private fun collectUiState() {
        toolbarUiStateCollectorJob = toolbarUiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            combine(
                sharedReviewMediaGalleryViewModel.orientationUiState,
                sharedReviewMediaGalleryViewModel.overlayVisibility,
                sharedReviewMediaGalleryViewModel.currentReviewDetail,
            ) { orientationUiState, overlayVisibility, currentReviewDetail ->
                Triple(orientationUiState, overlayVisibility, currentReviewDetail)
            }.collectLatest {
                val showOverlay = it.second
                val isInPortrait = it.first.isPortrait()
                val isReportable = it.third?.isReportable.orFalse()
                binding?.icReviewMediaGalleryClose?.showWithCondition(showOverlay)
                binding?.icReviewMediaGalleryKebab?.showWithCondition(
                    showOverlay && isInPortrait && isReportable
                )
            }
        }
        orientationUiStateCollectorJob = orientationUiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.orientationUiState.collectLatest {
                requestedOrientation = when(it) {
                    OrientationUiState.Landscape -> {
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
        actionMenuBottomSheetUiStateCollectorJob = actionMenuBottomSheetUiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.actionMenuBottomSheetUiState.collectLatest {
                if (it is ActionMenuBottomSheetUiState.Showing) {
                    bottomSheetHandler.showActionMenuBottomSheet()
                }
            }
        }
        toasterQueueCollectorJob = toasterQueueCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.toasterQueue.collectLatest {
                binding?.root?.let { view ->
                    Toaster.build(
                        view,
                        it.message.getStringValue(view.context),
                        it.duration,
                        it.type,
                        it.actionText.getStringValue(view.context)
                    ) { _ ->
                        sharedReviewMediaGalleryViewModel.toasterEventActionClicked(it.key)
                    }.show()
                }
            }
        }
        toasterActionCLickEventCollectorJob = toasterActionCLickEventCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.toasterEventActionClickQueue.collectLatest {
                if (it == TOASTER_KEY_ERROR_GET_REVIEW_MEDIA) {
                    sharedReviewMediaGalleryViewModel.retryGetReviewMedia()
                }
            }
        }
    }

    private fun cancelUiStateCollector() {
        toolbarUiStateCollectorJob?.cancel()
        orientationUiStateCollectorJob?.cancel()
        actionMenuBottomSheetUiStateCollectorJob?.cancel()
        toasterQueueCollectorJob?.cancel()
        toasterActionCLickEventCollectorJob?.cancel()
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
}
