package com.tokopedia.reviewcommon.feature.media.player.controller.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.FragmentReviewMediaPlayerControllerBinding
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.reviewcommon.feature.media.player.controller.di.ReviewMediaPlayerControllerComponentInstance
import com.tokopedia.reviewcommon.feature.media.player.controller.di.qualifier.ReviewMediaPlayerControllerViewModelFactory
import com.tokopedia.reviewcommon.feature.media.player.controller.presentation.uistate.ReviewMediaPlayerControllerUiState
import com.tokopedia.reviewcommon.feature.media.player.controller.presentation.viewmodel.ReviewMediaPlayerControllerViewModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewMediaPlayerControllerFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
        const val TAG = "ReviewMediaPlayerControllerFragment"
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    @ReviewMediaPlayerControllerViewModelFactory
    lateinit var reviewMediaPlayerControllerViewModelFactory: ViewModelProvider.Factory

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var videoPlayer: ReviewVideoPlayer

    private var binding by viewBinding(FragmentReviewMediaPlayerControllerBinding::bind)
    private var uiStateCollectorJob: Job? = null
    private var currentMediaItemCollectorJob: Job? = null
    private var detailedReviewMediaGalleryResultCollectorJob: Job? = null
    private var orientationUiStateCollectorJob: Job? = null
    private var overlayVisibilityCollectorJob: Job? = null

    private val reviewMediaPlayerControllerViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            requireActivity(),
            reviewMediaPlayerControllerViewModelFactory
        ).get(ReviewMediaPlayerControllerViewModel::class.java)
    }
    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            requireActivity(),
            detailedReviewMediaGalleryViewModelFactory
        ).get(SharedReviewMediaGalleryViewModel::class.java)
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { reviewMediaPlayerControllerViewModel.restoreState(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentReviewMediaPlayerControllerBinding.inflate(
            inflater,
            container,
            false
        ).root
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
        stopUiStateCollector()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        reviewMediaPlayerControllerViewModel.saveState(outState)
    }

    override fun getScreenName(): String {
        return ReviewMediaPlayerControllerFragment::class.simpleName.orEmpty()
    }

    override fun initInjector() {
        ReviewMediaPlayerControllerComponentInstance.getInstance(requireContext()).inject(this)
    }

    private fun setupLayout() {
        binding?.run {
            setupCounter()
            setupVideoPlayer()
            setupVideoPlayerController()
        }
    }

    private fun collectUiState() {
        uiStateCollectorJob = uiStateCollectorJob?.takeIf { !it.isCompleted } ?: launch {
            reviewMediaPlayerControllerViewModel.uiState.collectLatest {
                binding?.updateUi(it)
            }
        }
        currentMediaItemCollectorJob = currentMediaItemCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.currentMediaItem.collectLatest {
                reviewMediaPlayerControllerViewModel.updateCurrentMediaItem(it)
            }
        }
        detailedReviewMediaGalleryResultCollectorJob = detailedReviewMediaGalleryResultCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.detailedReviewMediaResult.collectLatest {
                reviewMediaPlayerControllerViewModel.updateGetDetailedReviewMediaResult(it)
            }
        }
        orientationUiStateCollectorJob = orientationUiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.orientationUiState.collectLatest {
                reviewMediaPlayerControllerViewModel.updateOrientationUiState(it)
            }
        }
        overlayVisibilityCollectorJob = overlayVisibilityCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.overlayVisibility.collectLatest {
                reviewMediaPlayerControllerViewModel.updateOverlayVisibility(it)
            }
        }
    }

    private fun stopUiStateCollector() {
        uiStateCollectorJob?.cancel()
        currentMediaItemCollectorJob?.cancel()
        detailedReviewMediaGalleryResultCollectorJob?.cancel()
        orientationUiStateCollectorJob?.cancel()
        overlayVisibilityCollectorJob?.cancel()
    }

    private fun setupVideoPlayerController() {
        view?.findViewById<IconUnify>(R.id.review_media_gallery_maximize_control)?.setOnClickListener {
            sharedReviewMediaGalleryViewModel.requestLandscapeMode()
        }
        view?.findViewById<IconUnify>(R.id.review_media_gallery_minimize_control)?.setOnClickListener {
            sharedReviewMediaGalleryViewModel.requestPortraitMode()
        }
        view?.findViewById<IconUnify>(R.id.review_media_gallery_volume_muted_control)?.setOnClickListener {
            reviewMediaPlayerControllerViewModel.unmute()
        }
        view?.findViewById<IconUnify>(R.id.review_media_gallery_volume_unmuted_control)?.setOnClickListener {
            reviewMediaPlayerControllerViewModel.mute()
        }
    }

    private fun FragmentReviewMediaPlayerControllerBinding.setupCounter() {
        layoutReviewMediaGalleryItemCounter.setBackgroundResource(R.drawable.bg_review_media_gallery_item_counter)
    }

    private fun FragmentReviewMediaPlayerControllerBinding.setupVideoPlayer() {
        videoPlayer.setPlayerController(playerControlViewReviewMediaGallery)
    }

    private fun FragmentReviewMediaPlayerControllerBinding.updateUi(uiState: ReviewMediaPlayerControllerUiState) {
        if (uiState.shouldShowVideoPlayerController) {
            dividerReviewMediaGalleryBottomController.showWithCondition(uiState.orientationUiState.isPortrait())
            playerControlViewReviewMediaGallery.show()
        } else {
            dividerReviewMediaGalleryBottomController.gone()
            playerControlViewReviewMediaGallery.hide()
        }
        tvReviewMediaGalleryItemCounter.run {
            text = buildString {
                append(uiState.currentGalleryPosition)
                append("/")
                append(uiState.totalMedia)
            }
            if (uiState.shouldShowMediaCounter) show() else hide()
        }
        loaderReviewMediaGalleryItemCounter.showWithCondition(uiState.shouldShowMediaCounterLoader)
        layoutReviewMediaGalleryItemCounter.showWithCondition(
            uiState.shouldShowMediaCounter || uiState.shouldShowMediaCounterLoader
        )
        view?.findViewById<IconUnify>(R.id.review_media_gallery_maximize_control)?.showWithCondition(uiState.orientationUiState.isPortrait())
        view?.findViewById<IconUnify>(R.id.review_media_gallery_minimize_control)?.showWithCondition(uiState.orientationUiState.isLandscape())
        view?.findViewById<IconUnify>(R.id.review_media_gallery_volume_muted_control)?.showWithCondition(uiState.muted)
        view?.findViewById<IconUnify>(R.id.review_media_gallery_volume_unmuted_control)?.showWithCondition(!uiState.muted)
        if (uiState.muted) {
            videoPlayer.mute()
        } else {
            videoPlayer.unmute()
        }
        binding?.root?.showWithCondition(uiState.overlayVisibility)
    }
}