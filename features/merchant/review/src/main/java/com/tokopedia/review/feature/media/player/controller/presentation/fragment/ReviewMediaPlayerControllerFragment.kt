package com.tokopedia.review.feature.media.player.controller.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.util.Util
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.FragmentReviewMediaPlayerControllerBinding
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.player.controller.di.ReviewMediaPlayerControllerComponentInstance
import com.tokopedia.review.feature.media.player.controller.di.qualifier.ReviewMediaPlayerControllerViewModelFactory
import com.tokopedia.review.feature.media.player.controller.presentation.uistate.ReviewMediaPlayerControllerUiState
import com.tokopedia.review.feature.media.player.controller.presentation.viewmodel.ReviewMediaPlayerControllerViewModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.Formatter
import java.util.Locale
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
    private val stringBuilder = StringBuilder()
    private val formatter = Formatter(stringBuilder, Locale.getDefault())

    private var tvDurationView: Typography? = null
    private var icMaximizeControl: IconUnify? = null
    private var icMinimizeControl: IconUnify? = null
    private var icVolumeMutedControl: IconUnify? = null
    private var icVolumeUnMutedControl: IconUnify? = null

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
        bindViews(view)
        setupLayout()
        initUiStateCollectors()
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

    private fun bindViews(view: View) {
        tvDurationView = view.findViewById(com.google.android.exoplayer2.ui.R.id.exo_duration)
        icMaximizeControl = view.findViewById(R.id.review_media_gallery_maximize_control)
        icMinimizeControl = view.findViewById(R.id.review_media_gallery_minimize_control)
        icVolumeMutedControl = view.findViewById(R.id.review_media_gallery_volume_muted_control)
        icVolumeUnMutedControl = view.findViewById(R.id.review_media_gallery_volume_unmuted_control)
    }

    private fun setupLayout() {
        binding?.run {
            setupVideoPlayer()
            setupVideoPlayerController()
        }
    }

    private fun initUiStateCollectors() {
        collectUiStateUpdate()
        collectCurrentMediaItemUpdate()
        collectDetailedReviewMediaGalleryResultUpdate()
        collectOrientationUiStateUpdate()
        collectOverlayVisibilityUpdate()
        collectVideoPlayingDurationUpdate()
    }

    private fun collectUiStateUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewMediaPlayerControllerViewModel.uiState) {
            binding?.updateUi(it)
        }
    }

    private fun collectCurrentMediaItemUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.currentMediaItem) {
            reviewMediaPlayerControllerViewModel.updateCurrentMediaItem(it)
        }
    }

    private fun collectDetailedReviewMediaGalleryResultUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.detailedReviewMediaResult) {
            reviewMediaPlayerControllerViewModel.updateGetDetailedReviewMediaResult(it)
        }
    }

    private fun collectOrientationUiStateUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.orientationUiState) {
            reviewMediaPlayerControllerViewModel.updateOrientationUiState(it)
        }
    }

    private fun collectOverlayVisibilityUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.overlayVisibility) {
            reviewMediaPlayerControllerViewModel.updateOverlayVisibility(it)
        }
    }

    private fun collectVideoPlayingDurationUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.videoDurationMillis) {
            reviewMediaPlayerControllerViewModel.updateVideoDurationMillis(it)
        }
    }

    private fun FragmentReviewMediaPlayerControllerBinding.setupVideoPlayerController() {
        icMaximizeControl?.setOnClickListener {
            sharedReviewMediaGalleryViewModel.requestLandscapeMode()
        }
        icMinimizeControl?.setOnClickListener {
            sharedReviewMediaGalleryViewModel.requestPortraitMode()
        }
        icVolumeMutedControl?.setOnClickListener { reviewMediaPlayerControllerViewModel.unmute() }
        icVolumeUnMutedControl?.setOnClickListener { reviewMediaPlayerControllerViewModel.mute() }
        playerControlViewReviewMediaGallery.hide()
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
        tvDurationView?.text = Util.getStringForTime(
            stringBuilder, formatter, uiState.videoDurationMillis
        )
        icMaximizeControl?.showWithCondition(uiState.orientationUiState.isPortrait())
        icMinimizeControl?.showWithCondition(uiState.orientationUiState.isLandscape())
        icVolumeMutedControl?.showWithCondition(uiState.muted)
        icVolumeUnMutedControl?.showWithCondition(!uiState.muted)
        if (uiState.muted) {
            videoPlayer.mute()
        } else {
            videoPlayer.unmute()
        }
        binding?.root?.showWithCondition(uiState.overlayVisibility)
    }
}