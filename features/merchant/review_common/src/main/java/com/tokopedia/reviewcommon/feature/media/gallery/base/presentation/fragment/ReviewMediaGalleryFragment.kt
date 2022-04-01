package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewcommon.databinding.FragmentReviewMediaGalleryBinding
import com.tokopedia.reviewcommon.feature.media.gallery.base.analytic.ReviewMediaGalleryTracker
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.ReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.adapter.ReviewMediaGalleryAdapter
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uistate.AdapterUiState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uistate.ViewPagerUiState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.viewmodel.ReviewMediaGalleryViewModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.fragment.ReviewImagePlayerFragment
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewMediaGalleryFragment : BaseDaggerFragment(), CoroutineScope,
    ReviewImagePlayerFragment.Listener {

    companion object {
        const val TAG = "ReviewMediaGalleryFragment"
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var videoPlayer: ReviewVideoPlayer

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    @ReviewMediaGalleryViewModelFactory
    lateinit var reviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(FragmentReviewMediaGalleryBinding::bind)
    private var uiStateCollectorJob: Job? = null
    private var currentMediaItemCollectorJob: Job? = null
    private var detailedReviewMediaResultCollectorJob: Job? = null
    private var orientationUiStateCollectorJob: Job? = null

    private val reviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), reviewMediaGalleryViewModelFactory)
            .get(ReviewMediaGalleryViewModel::class.java)
    }
    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), detailedReviewMediaGalleryViewModelFactory)
            .get(SharedReviewMediaGalleryViewModel::class.java)
    }
    private val galleryAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ReviewMediaGalleryAdapter(this, this)
    }
    private val pageChangeListener = PagerListener()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + SupervisorJob()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUiState(savedInstanceState)
        return FragmentReviewMediaGalleryBinding.inflate(
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
        releaseVideoPlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        reviewMediaGalleryViewModel.saveUiState(outState)
    }

    override fun getScreenName(): String = ReviewMediaGalleryFragment::class.simpleName.orEmpty()

    override fun initInjector() {
        ReviewMediaGalleryComponentInstance.getInstance(requireContext()).inject(this)
    }

    override fun onSeeMoreClicked() {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY,
            sharedReviewMediaGalleryViewModel.getProductId()
        )
    }

    override fun onImageImpressed(imageUri: String) {
        galleryAdapter.getItemByUri(imageUri)?.let { (index, media) ->
            ReviewMediaGalleryTracker.trackImpressImage(
                sharedReviewMediaGalleryViewModel.getTotalMediaCount(),
                sharedReviewMediaGalleryViewModel.getProductId(),
                media.id,
                index,
                userSession.userId
            )
        }
    }

    private fun setupLayout() {
        binding?.setupViewPager()
    }

    private fun FragmentReviewMediaGalleryBinding.setupViewPager() {
        viewPagerReviewMediaGallery.run {
            offscreenPageLimit = 1
            adapter = galleryAdapter
        }
    }

    private fun collectUiState() {
        uiStateCollectorJob = uiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            reviewMediaGalleryViewModel.uiState.collectLatest {
                updateAdapter(it.adapterUiState)
                // only update when there's any media item, since updating viewpager while there's no
                // media item is unnecessary
                val needUpdate = it.adapterUiState.mediaItemUiModels.any {
                    it !is LoadingStateItemUiModel
                }
                if (needUpdate) binding?.updateViewPager(it.viewPagerUiState)
            }
        }
        currentMediaItemCollectorJob = currentMediaItemCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            reviewMediaGalleryViewModel.currentMediaItem.collectLatest {
                if (it is ImageMediaItemUiModel || it is VideoMediaItemUiModel) {
                    if (sharedReviewMediaGalleryViewModel.isProductReview()) {
                        ReviewMediaGalleryTracker.trackSwipeImage(
                            it.feedbackId,
                            reviewMediaGalleryViewModel.viewPagerUiState.value.previousPagerPosition,
                            reviewMediaGalleryViewModel.viewPagerUiState.value.currentPagerPosition,
                            sharedReviewMediaGalleryViewModel.getTotalMediaCount().toInt(),
                            userSession.userId
                        )
                    } else {
                        ReviewMediaGalleryTracker.trackShopReviewSwipeImage(
                            it.feedbackId,
                            reviewMediaGalleryViewModel.viewPagerUiState.value.previousPagerPosition,
                            reviewMediaGalleryViewModel.viewPagerUiState.value.currentPagerPosition,
                            sharedReviewMediaGalleryViewModel.getTotalMediaCount().toInt(),
                            sharedReviewMediaGalleryViewModel.getShopId()
                        )
                    }
                }
                sharedReviewMediaGalleryViewModel.updateCurrentMediaItem(it)
            }
        }
        detailedReviewMediaResultCollectorJob = detailedReviewMediaResultCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            combine(
                sharedReviewMediaGalleryViewModel.detailedReviewMediaResult,
                sharedReviewMediaGalleryViewModel.mediaNumberToLoad,
                sharedReviewMediaGalleryViewModel.showSeeMore,
            ) { detailedReviewMediaResult, mediaNumberToLoad, showSeeMore ->
                Triple(detailedReviewMediaResult, mediaNumberToLoad, showSeeMore)
            }.collectLatest {
                reviewMediaGalleryViewModel.updateDetailedReviewMediaResult(
                    it.first,
                    it.second,
                    it.third,
                    it.first?.detail?.mediaCount.orZero().toInt()
                )
            }
        }
        orientationUiStateCollectorJob = orientationUiStateCollectorJob?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.orientationUiState.collectLatest {
                reviewMediaGalleryViewModel.updateOrientationUiState(it)
                binding?.viewPagerReviewMediaGallery?.isUserInputEnabled = it.isPortrait()
            }
        }
    }

    private fun stopUiStateCollector() {
        uiStateCollectorJob?.cancel()
        currentMediaItemCollectorJob?.cancel()
        detailedReviewMediaResultCollectorJob?.cancel()
        orientationUiStateCollectorJob?.cancel()
    }

    private fun releaseVideoPlayer() {
        if (activity?.isChangingConfigurations != true) {
            videoPlayer.cleanupVideoPlayer()
        }
    }

    private fun updateAdapter(uiState: AdapterUiState) {
        if (uiState.mediaItemUiModels.isNotEmpty()) {
            galleryAdapter.updateItems(uiState.mediaItemUiModels)
        }
    }

    private fun FragmentReviewMediaGalleryBinding.updateViewPager(
        uiState: ViewPagerUiState
    ) {
        val pointToValidPosition = uiState.currentPagerPosition != RecyclerView.NO_POSITION && galleryAdapter.itemCount > uiState.currentPagerPosition
        val pointToDifferentPosition = viewPagerReviewMediaGallery.currentItem != uiState.currentPagerPosition
        if (pointToValidPosition && pointToDifferentPosition) {
            viewPagerReviewMediaGallery.setCurrentItem(uiState.currentPagerPosition, false)
        }
        pageChangeListener.attachListener()
        binding?.viewPagerReviewMediaGallery?.isUserInputEnabled = uiState.enableUserInput
    }

    private fun initUiState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            restoreUiState(savedInstanceState)
            reviewMediaGalleryViewModel.restoreUiState(savedInstanceState)
        }
    }

    private fun restoreUiState(savedInstanceState: Bundle) {
        savedInstanceState.getParcelable<AdapterUiState>(
            ReviewMediaGalleryViewModel.SAVED_STATE_MEDIA_GALLERY_ADAPTER_UI_STATE
        )?.let { savedReviewMediaGalleryAdapterUiState ->
            galleryAdapter.restoreUiState(savedReviewMediaGalleryAdapterUiState.mediaItemUiModels)
        }
    }

    inner class PagerListener : ViewPager2.OnPageChangeCallback() {
        private var attached: Boolean = false
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (galleryAdapter.itemCount > position) {
                reviewMediaGalleryViewModel.onPageSelected(position)
            }
        }

        fun attachListener() {
            attached = true
            binding?.viewPagerReviewMediaGallery?.registerOnPageChangeCallback(this)
        }
    }
}