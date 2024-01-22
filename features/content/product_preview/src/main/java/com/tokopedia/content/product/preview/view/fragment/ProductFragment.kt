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
import com.tokopedia.content.product.preview.databinding.FragmentProductBinding
import com.tokopedia.content.product.preview.utils.PRODUCT_CONTENT_VIDEO_KEY_REF
import com.tokopedia.content.product.preview.utils.PRODUCT_FRAGMENT_TAG
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter
import com.tokopedia.content.product.preview.view.adapter.product.ProductIndicatorAdapter
import com.tokopedia.content.product.preview.view.components.items.ProductIndicatorItemDecoration
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewVideoPlayerManager
import com.tokopedia.content.product.preview.view.listener.ProductIndicatorListener
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction.ProductSelected
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductFragment @Inject constructor() : TkpdBaseV4Fragment() {

    private val viewModel by activityViewModels<ProductPreviewViewModel>()

    private var _binding: FragmentProductBinding? = null
    private val binding: FragmentProductBinding
        get() = _binding!!

    private var snapHelperContent = PagerSnapHelper()
    private var mVideoPlayer: ProductPreviewExoPlayer? = null

    private val layoutManagerContent by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }

    private val layoutManagerIndicator by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }

    private val videoPlayerManager by lazyThreadSafetyNone {
        ProductPreviewVideoPlayerManager(requireContext())
    }

    private val productContentAdapter by lazyThreadSafetyNone {
        ProductContentAdapter(
            listener = object : ProductPreviewListener {
                override fun getVideoPlayer(id: String): ProductPreviewExoPlayer {
                    return videoPlayerManager.occupy(id)
                }

                override fun pauseVideo(id: String) {
                    videoPlayerManager.pause(id)
                }

                override fun resumeVideo(id: String) {
                    videoPlayerManager.resume(id)
                }

                override fun onScrubbing() {
                    binding.tvIndicatorLabel.hide()
                    binding.rvIndicatorProduct.hide()
                }

                override fun onStopScrubbing() {
                    binding.tvIndicatorLabel.show()
                    binding.rvIndicatorProduct.show()
                }
            }
        )
    }

    private val productIndicatorAdapter by lazyThreadSafetyNone {
        ProductIndicatorAdapter(
            listener = object :
                ProductIndicatorListener {
                override fun onClickProductIndicator(position: Int) {
                    scrollTo(position)
                    viewModel.onAction(ProductSelected(position))
                }
            }
        )
    }

    private val contentScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState != RecyclerView.SCROLL_STATE_IDLE) return
            val position = getContentCurrentPosition()
            scrollTo(position)
            viewModel.onAction(ProductSelected(position))
        }
    }

    private var autoScrollFirstOpenContent = true
    private var autoScrollFirstOpenIndicator = true

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
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        setupProductContentViews()
        setupProductIndicatorViews()
    }

    private fun setupProductContentViews() = with(binding.rvContentProduct) {
        adapter = productContentAdapter
        layoutManager = layoutManagerContent
        snapHelperContent.attachToRecyclerView(this)
        removeOnScrollListener(contentScrollListener)
        addOnScrollListener(contentScrollListener)
        itemAnimator = null
    }

    private fun setupProductIndicatorViews() = with(binding.rvIndicatorProduct) {
        adapter = productIndicatorAdapter
        layoutManager = layoutManagerIndicator
        itemAnimator = null
        if (itemDecorationCount == 0) {
            addItemDecoration(ProductIndicatorItemDecoration(requireContext()))
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.productUiState.withCache().collectLatest { (prevState, currState) ->
                renderContent(prevState?.productContent, currState.productContent)
                renderIndicator(prevState?.productIndicator, currState.productIndicator)
            }
        }
    }

    private fun renderContent(
        prev: List<ContentUiModel>?,
        state: List<ContentUiModel>
    ) {
        if (prev == state) return

        prepareVideoPlayerIfNeeded(state)

        val position = state.indexOfFirst { it.selected }
        if (position < 0) return

        productContentAdapter.submitList(state)
        if (autoScrollFirstOpenContent) {
            val position = getSelectedItemPosition(state)
            binding.rvContentProduct.scrollToPosition(position)
            autoScrollFirstOpenContent = false
        }
    }

    private fun renderIndicator(
        prev: List<ProductIndicatorUiModel>?,
        state: List<ProductIndicatorUiModel>
    ) {
        if (prev == state) return

        val position = state.indexOfFirst { it.selected }
        if (position < 0) return

        productIndicatorAdapter.submitList(state)
        if (autoScrollFirstOpenIndicator) {
            binding.rvIndicatorProduct.scrollToPosition(position)
            autoScrollFirstOpenIndicator = false
        }

        if (state[position].variantName.isEmpty()) {
            binding.tvIndicatorLabel.apply {
                visible()
                text = String.format(
                    getString(contentproductpreviewR.string.text_label_place_holder_empty_variant),
                    position.plus(1),
                    state.size
                )
            }
        } else {
            binding.tvIndicatorLabel.apply {
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

    private fun prepareVideoPlayerIfNeeded(state: List<ContentUiModel>) {
        if (mVideoPlayer != null) return
        val videoPosition = state.indexOfFirst { it.type == MediaType.Video }

        if (videoPosition < 0) return
        val videoUrl = state[videoPosition].url

        val instance = videoPlayerManager.occupy(String.format(PRODUCT_CONTENT_VIDEO_KEY_REF, videoUrl))
        val videoPlayer = mVideoPlayer ?: instance
        mVideoPlayer = videoPlayer
        mVideoPlayer?.start(
            videoUrl = videoUrl,
            isMute = false,
            playWhenReady = false
        )

        if (viewModel.productVideoLastDuration == 0L) return
        mVideoPlayer?.seekDurationTo(viewModel.productVideoLastDuration)
    }

    private fun getSelectedItemPosition(state: List<ContentUiModel>): Int {
        val selectedData = state.firstOrNull { it.selected } ?: return 0
        return state.indexOf(selectedData)
    }

    private fun getContentCurrentPosition(): Int {
        val snappedView = snapHelperContent.findSnapView(layoutManagerContent)
            ?: return RecyclerView.NO_POSITION
        return binding.rvContentProduct.getChildAdapterPosition(snappedView)
    }

    private fun scrollTo(position: Int) {
        binding.rvContentProduct.smoothScrollToPosition(position)
        binding.rvIndicatorProduct.smoothScrollToPosition(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvContentProduct.removeOnScrollListener(contentScrollListener)
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
