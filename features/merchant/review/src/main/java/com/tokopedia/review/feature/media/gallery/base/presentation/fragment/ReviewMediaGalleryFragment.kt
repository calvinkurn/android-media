package com.tokopedia.review.feature.media.gallery.base.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.FragmentReviewMediaGalleryBinding
import com.tokopedia.review.feature.media.gallery.base.analytic.ReviewMediaGalleryTracker
import com.tokopedia.review.feature.media.gallery.base.di.ReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryGson
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.base.presentation.adapter.ReviewMediaGalleryAdapter
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.review.feature.media.gallery.base.presentation.uistate.AdapterUiState
import com.tokopedia.review.feature.media.gallery.base.presentation.uistate.ViewPagerUiState
import com.tokopedia.review.feature.media.gallery.base.presentation.viewmodel.ReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.player.image.presentation.fragment.ReviewImagePlayerFragment
import com.tokopedia.review.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.review.feature.media.player.video.presentation.fragment.ReviewVideoPlayerFragment
import com.tokopedia.review.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import com.tokopedia.reviewcommon.extension.get
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewMediaGalleryFragment : BaseDaggerFragment(), CoroutineScope,
    ReviewImagePlayerFragment.Listener, ReviewVideoPlayerFragment.Listener {

    companion object {
        const val TAG = "ReviewMediaGalleryFragment"
        const val KEY_CACHE_MANAGER_ID = "cacheManagerId"
    }

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    @ReviewMediaGalleryGson
    lateinit var gson: Gson

    @Inject
    lateinit var videoPlayer: ReviewVideoPlayer

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var reviewMediaGalleryTracker: ReviewMediaGalleryTracker

    @Inject
    @ReviewMediaGalleryViewModelFactory
    lateinit var reviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    private var binding by viewBinding(FragmentReviewMediaGalleryBinding::bind)

    private val reviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), reviewMediaGalleryViewModelFactory)
            .get(ReviewMediaGalleryViewModel::class.java)
    }
    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), detailedReviewMediaGalleryViewModelFactory)
            .get(SharedReviewMediaGalleryViewModel::class.java)
    }
    private val galleryAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ReviewMediaGalleryAdapter(this, this, this)
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
        initUiStateCollectors()
    }

    override fun onPause() {
        super.onPause()
        releaseVideoPlayer()
        reviewMediaGalleryTracker.sendQueuedTrackers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val cacheManager = SaveInstanceCacheManager(
            context = requireContext(),
            generateObjectId = true
        )
        reviewMediaGalleryViewModel.saveUiState(cacheManager)
        outState.putString(KEY_CACHE_MANAGER_ID, cacheManager.id)
    }

    override fun getScreenName(): String = ReviewMediaGalleryFragment::class.simpleName.orEmpty()

    override fun initInjector() {
        ReviewMediaGalleryComponentInstance.getInstance(requireContext()).inject(this)
    }

    override fun onSeeMoreClicked() {
        val routed = RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY,
            sharedReviewMediaGalleryViewModel.getProductId()
        )
        if (routed) {
            reviewMediaGalleryTracker.trackClickShowSeeMore(
                sharedReviewMediaGalleryViewModel.getProductId()
            )
        }
    }

    override fun onImageImpressed(imageUri: String) {
        galleryAdapter.getImageByUri(imageUri)?.let { (index, media) ->
            reviewMediaGalleryTracker.trackImpressImage(
                pageSource = sharedReviewMediaGalleryViewModel.getPageSource(),
                imageCount = sharedReviewMediaGalleryViewModel.getTotalMediaCount(),
                feedbackId = media.feedbackId,
                productId = sharedReviewMediaGalleryViewModel.getProductId(),
                attachmentId = media.getAttachmentID(),
                fileName = media.getFileName(),
                position = index,
                userId = userSession.userId,
                reviewUserId = sharedReviewMediaGalleryViewModel.getReviewUserID(),
                isReviewOwner = sharedReviewMediaGalleryViewModel.isReviewOwner,
            )
        }
    }

    override fun onVideoImpressed(videoUri: String, videoDurationSecond: Long) {
        galleryAdapter.getVideoByUri(videoUri)?.let { (index, media) ->
            reviewMediaGalleryTracker.trackImpressVideoV2(
                imageCount = sharedReviewMediaGalleryViewModel.getTotalMediaCount(),
                feedbackId = media.feedbackId,
                productId = sharedReviewMediaGalleryViewModel.getProductId(),
                attachmentId = media.getAttachmentID(),
                videoID = media.getVideoID(),
                position = index,
                userId = userSession.userId,
                reviewUserId = sharedReviewMediaGalleryViewModel.getReviewUserID(),
                isReviewOwner = sharedReviewMediaGalleryViewModel.isReviewOwner,
                videoDurationSecond = videoDurationSecond,
            )
        }
    }

    override fun onVideoPlaying(videoUri: String, videoDurationSecond: Long) {
        galleryAdapter.getVideoByUri(videoUri)?.let { (_, media) ->
            reviewMediaGalleryTracker.trackPlayVideo(
                media.feedbackId,
                sharedReviewMediaGalleryViewModel.getProductId(),
                media.getAttachmentID(),
                media.getVideoID(),
                videoDurationSecond
            )
        }
    }

    override fun onVideoStopped(
        videoUri: String,
        videoDurationSecond: Long,
        watchingDurationSecond: Long
    ) {
        galleryAdapter.getVideoByUri(videoUri)?.let { (_, media) ->
            reviewMediaGalleryTracker.trackStopVideo(
                media.feedbackId,
                sharedReviewMediaGalleryViewModel.getProductId(),
                media.getAttachmentID(),
                media.getVideoID(),
                videoDurationSecond,
                watchingDurationSecond
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

    private fun initUiStateCollectors() {
        collectReviewMediaGalleryUiStateUpdate()
        collectCurrentMediaItemUpdateForTracker()
        collectCurrentMediaItemUpdate()
        collectDetailedReviewMediaGalleryResultUpdate()
        collectOrientationUpdate()
    }

    private fun collectReviewMediaGalleryUiStateUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewMediaGalleryViewModel.uiState) {
            updateAdapter(it.adapterUiState)
            // only update when there's any media item, since updating viewpager while there's no
            // media item is unnecessary
            val needUpdate = it.adapterUiState.mediaItemUiModels.any {
                it !is LoadingStateItemUiModel
            }
            if (needUpdate) binding?.updateViewPager(it.viewPagerUiState)
        }
    }

    private fun collectCurrentMediaItemUpdateForTracker() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewMediaGalleryViewModel.currentMediaItem.distinctUntilChangedBy {
            it?.id
        }) {
            if (it is ImageMediaItemUiModel || it is VideoMediaItemUiModel) {
                val currentViewPagerState = reviewMediaGalleryViewModel.viewPagerUiState.value
                if (currentViewPagerState.previousPagerPosition != currentViewPagerState.currentPagerPosition) {
                    if (sharedReviewMediaGalleryViewModel.isProductReview()) {
                        reviewMediaGalleryTracker.trackSwipeImage(
                            it.feedbackId,
                            currentViewPagerState.previousPagerPosition,
                            currentViewPagerState.currentPagerPosition,
                            sharedReviewMediaGalleryViewModel.getTotalMediaCount().toInt(),
                            userSession.userId
                        )
                    } else {
                        reviewMediaGalleryTracker.trackShopReviewSwipeImage(
                            it.feedbackId,
                            currentViewPagerState.previousPagerPosition,
                            currentViewPagerState.currentPagerPosition,
                            sharedReviewMediaGalleryViewModel.getTotalMediaCount().toInt(),
                            sharedReviewMediaGalleryViewModel.getShopId()
                        )
                    }
                }
            }
        }
    }

    private fun collectCurrentMediaItemUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewMediaGalleryViewModel.currentMediaItem) {
            sharedReviewMediaGalleryViewModel.updateCurrentMediaItem(it)
        }
    }

    private fun collectDetailedReviewMediaGalleryResultUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(
            combine(
                sharedReviewMediaGalleryViewModel.detailedReviewMediaResult,
                sharedReviewMediaGalleryViewModel.mediaNumberToLoad,
                sharedReviewMediaGalleryViewModel.showSeeMore,
            ) { detailedReviewMediaResult, mediaNumberToLoad, showSeeMore ->
                Triple(detailedReviewMediaResult, mediaNumberToLoad, showSeeMore)
            }
        ) {
            reviewMediaGalleryViewModel.updateDetailedReviewMediaResult(
                it.first,
                it.second,
                it.third,
                it.first?.detail?.mediaCount.orZero().toInt()
            )
        }
    }

    private fun collectOrientationUpdate() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.orientationUiState) {
            reviewMediaGalleryViewModel.updateOrientationUiState(it)
            binding?.viewPagerReviewMediaGallery?.isUserInputEnabled = it.isPortrait()
        }
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
            val cacheManagerId = savedInstanceState.getString(KEY_CACHE_MANAGER_ID)
            val cacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)
            restoreUiState(cacheManager)
            reviewMediaGalleryViewModel.restoreUiState(cacheManager)
        }
    }

    private fun restoreUiState(cacheManager: CacheManager) {
        cacheManager.get<AdapterUiState>(
            customId = ReviewMediaGalleryViewModel.SAVED_STATE_MEDIA_GALLERY_ADAPTER_UI_STATE,
            type = AdapterUiState::class.java,
            defaultValue = null,
            gson = gson
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
