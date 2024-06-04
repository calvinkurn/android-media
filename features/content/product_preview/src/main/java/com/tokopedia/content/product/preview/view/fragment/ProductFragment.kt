package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalytics
import com.tokopedia.content.product.preview.databinding.FragmentProductBinding
import com.tokopedia.content.product.preview.utils.PRODUCT_CONTENT_VIDEO_KEY_REF
import com.tokopedia.content.product.preview.utils.PRODUCT_FRAGMENT_TAG
import com.tokopedia.content.product.preview.view.adapter.product.ProductMediaAdapter
import com.tokopedia.content.product.preview.view.adapter.product.ProductThumbnailAdapter
import com.tokopedia.content.product.preview.view.components.items.ProductThumbnailItemDecoration
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewVideoPlayerManager
import com.tokopedia.content.product.preview.view.listener.MediaImageListener
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.listener.ProductThumbnailListener
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.TAB_PRODUCT_NAME
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.InitializeProductMainData
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction.ProductMediaSelected
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductFragment @Inject constructor(
    private val analyticsFactory: ProductPreviewAnalytics.Factory
) : TkpdBaseV4Fragment(),
    ProductPreviewVideoListener,
    MediaImageListener {

    private val viewModel by activityViewModels<ProductPreviewViewModel>()

    private var _binding: FragmentProductBinding? = null
    private val binding: FragmentProductBinding
        get() = _binding!!

    private val analytics: ProductPreviewAnalytics by lazyThreadSafetyNone {
        analyticsFactory.create(viewModel.productPreviewSource.productId)
    }

    private var snapHelperMedia = PagerSnapHelper()
    private var mVideoPlayer: ProductPreviewExoPlayer? = null

    private val layoutManagerMedia by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }

    private val layoutManagerThumbnail by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }

    private val videoPlayerManager by lazyThreadSafetyNone {
        ProductPreviewVideoPlayerManager(requireContext())
    }

    private val productMediaAdapter by lazyThreadSafetyNone {
        ProductMediaAdapter(
            productPreviewVideoListener = this,
            mediaImageLister = this
        )
    }

    private val productThumbnailAdapter by lazyThreadSafetyNone {
        ProductThumbnailAdapter(
            productThumbnailListener = object :
                ProductThumbnailListener {
                override fun onClickProductThumbnail(position: Int) {
                    analytics.onClickThumbnailProduct()
                    scrollTo(position)
                    viewModel.onAction(ProductMediaSelected(position))
                }
            }
        )
    }

    private val mediaScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState != RecyclerView.SCROLL_STATE_IDLE) return
            analytics.onSwipeContentAndTab(
                tabName = TAB_PRODUCT_NAME,
                isTabChanged = false
            )
            val position = getMediaCurrentPosition()
            scrollTo(position)
            viewModel.onAction(ProductMediaSelected(position))
        }
    }

    private var autoScrollFirstOpenMedia = true
    private var autoScrollFirstOpenThumbnail = true

    override fun getScreenName() = PRODUCT_FRAGMENT_TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeProductMainData()

        setupViews()

        observeData()
    }

    private fun initializeProductMainData() {
        viewModel.onAction(InitializeProductMainData)
    }

    private fun setupViews() {
        setupProductMediaViews()
        setupProductThumbnailViews()
    }

    private fun setupProductMediaViews() = with(binding.rvMediaProduct) {
        adapter = productMediaAdapter
        layoutManager = layoutManagerMedia
        snapHelperMedia.attachToRecyclerView(this)
        removeOnScrollListener(mediaScrollListener)
        addOnScrollListener(mediaScrollListener)
        itemAnimator = null
    }

    private fun setupProductThumbnailViews() = with(binding.rvThumbnailProduct) {
        adapter = productThumbnailAdapter
        layoutManager = layoutManagerThumbnail
        itemAnimator = null
        if (itemDecorationCount == 0) {
            addItemDecoration(ProductThumbnailItemDecoration(requireContext()))
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.withCache().collectLatest { (prevState, currState) ->
                renderMedia(
                    prevState?.productUiModel?.productMedia,
                    currState.productUiModel.productMedia
                )
                renderThumbnail(
                    prevState?.productUiModel?.productMedia,
                    currState.productUiModel.productMedia
                )
            }
        }
    }

    private fun renderMedia(
        prev: List<ProductMediaUiModel>?,
        state: List<ProductMediaUiModel>
    ) {
        if (prev == state) return

        prepareVideoPlayerIfNeeded(state)

        productMediaAdapter.submitList(state)
        if (autoScrollFirstOpenMedia) {
            val autoScrollPosition = getSelectedItemPosition(state)
            binding.rvMediaProduct.scrollToPosition(autoScrollPosition)
            autoScrollFirstOpenMedia = false
        } else {
            val autoScrollPosition = state.indexOfFirst { it.selected }
            val exactPosition = autoScrollPosition.coerceAtLeast(0)
            binding.rvMediaProduct.smoothScrollToPosition(exactPosition)
        }
    }

    private fun renderThumbnail(
        prev: List<ProductMediaUiModel>?,
        state: List<ProductMediaUiModel>
    ) {
        if (prev == state) return

        val position = getSelectedItemPosition(state)

        productThumbnailAdapter.submitList(state)
        if (autoScrollFirstOpenThumbnail) {
            binding.rvThumbnailProduct.scrollToPosition(position)
            autoScrollFirstOpenThumbnail = false
        } else {
            val autoScrollPosition = state.indexOfFirst { it.selected }
            val exactPosition = autoScrollPosition.coerceAtLeast(0)
            binding.rvMediaProduct.smoothScrollToPosition(exactPosition)
        }

        if (state[position].variantName.isEmpty()) {
            binding.tvThumbnailLabel.apply {
                visible()
                text = String.format(
                    getString(contentproductpreviewR.string.text_label_place_holder_empty_variant),
                    position.plus(1),
                    state.size
                )
            }
        } else {
            binding.tvThumbnailLabel.apply {
                visible()
                text = String.format(
                    getString(contentproductpreviewR.string.text_label_place_holder),
                    position.plus(1),
                    state.size,
                    state[position].variantName
                )
            }
        }
    }

    private fun prepareVideoPlayerIfNeeded(state: List<ProductMediaUiModel>) {
        if (mVideoPlayer != null) return

        val videoPosition = state.indexOfFirst { it.type == MediaType.Video }
        if (videoPosition < 0) return

        val data = state[videoPosition]
        val videoUrl = data.url

        val instance = videoPlayerManager.occupy(
            String.format(
                PRODUCT_CONTENT_VIDEO_KEY_REF,
                videoUrl
            )
        )
        val videoPlayer = mVideoPlayer ?: instance
        mVideoPlayer = videoPlayer
        mVideoPlayer?.start(
            videoUrl = videoUrl,
            isMute = false,
            playWhenReady = false
        )

        mVideoPlayer?.seekDurationTo(data.videoLastDuration)
    }

    private fun getSelectedItemPosition(state: List<ProductMediaUiModel>): Int {
        val selectedData = state.firstOrNull { it.selected } ?: return 0
        return state.indexOf(selectedData)
    }

    private fun getMediaCurrentPosition(): Int {
        val snappedView = snapHelperMedia.findSnapView(layoutManagerMedia)
            ?: return RecyclerView.NO_POSITION
        val position = binding.rvMediaProduct.getChildAdapterPosition(snappedView)
        return position.coerceAtLeast(0)
    }

    private fun scrollTo(position: Int) {
        binding.rvMediaProduct.smoothScrollToPosition(position)
        binding.rvThumbnailProduct.smoothScrollToPosition(position)
    }

    override fun onImpressedImage() {
        analytics.onImpressImage(TAB_PRODUCT_NAME)
    }

    override fun onImageInteraction(isScalingMode: Boolean) {
        viewModel.onAction(ProductPreviewAction.ProductImageInteraction(isScalingMode))
    }

    override fun onImpressedVideo() {
        analytics.onImpressVideo(TAB_PRODUCT_NAME)
    }

    override fun getVideoPlayer(id: String): ProductPreviewExoPlayer {
        return videoPlayerManager.occupy(id)
    }

    override fun onPauseResumeVideo() {
        analytics.onClickPauseOrPlayVideo(TAB_PRODUCT_NAME)
    }

    override fun pauseVideo(id: String) {
        videoPlayerManager.pause(id)
    }

    override fun resumeVideo(id: String) {
        videoPlayerManager.resume(id)
    }

    override fun onScrubbing() {
        binding.tvThumbnailLabel.hide()
        binding.rvThumbnailProduct.hide()
    }

    override fun onStopScrubbing() {
        binding.tvThumbnailLabel.show()
        binding.rvThumbnailProduct.show()
    }

    override fun onVideoEnded() {
        viewModel.onAction(ProductPreviewAction.ProductMediaVideoEnded)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvMediaProduct.removeOnScrollListener(mediaScrollListener)
        videoPlayerManager.releaseAll()
        _binding = null
    }

    companion object {
        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ProductFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(PRODUCT_FRAGMENT_TAG) as? ProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ProductFragment
        }
    }
}
