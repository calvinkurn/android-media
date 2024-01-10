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
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.databinding.FragmentProductBinding
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter
import com.tokopedia.content.product.preview.view.adapter.product.ProductIndicatorAdapter
import com.tokopedia.content.product.preview.view.components.items.ProductIndicatorItemDecoration
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewVideoPlayerManager
import com.tokopedia.content.product.preview.view.listener.ProductIndicatorListener
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewUiAction
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductFragment @Inject constructor() : TkpdBaseV4Fragment() {

    private val mParentPage: ProductPreviewFragment
        get() = (requireParentFragment() as ProductPreviewFragment)

    private var _binding: FragmentProductBinding? = null
    private val binding: FragmentProductBinding
        get() = _binding!!

    private val viewModel by activityViewModels<ProductPreviewViewModel> {
        mParentPage.viewModelProvider
    }

    private var snapHelperContent: PagerSnapHelper = PagerSnapHelper()

    private val layoutManagerContent by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }
    private val layoutManagerIndicator by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }

    private val productContentAdapter by lazyThreadSafetyNone {
        ProductContentAdapter(
            listener = object : ProductPreviewListener {
                override fun getVideoPlayer(id: String): ProductPreviewExoPlayer {
                    return videoPlayerManager.occupy(id)
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
                    viewModel.submitAction(ProductPreviewUiAction.ProductSelected(position))
                }
            }
        )
    }

    private val videoPlayerManager by lazy { ProductPreviewVideoPlayerManager(requireContext()) }
    private val contentScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == ViewPager2.SCROLL_STATE_IDLE) {
                val position = getContentCurrentPosition()
                scrollTo(position)
                viewModel.submitAction(ProductPreviewUiAction.ProductSelected(position))
            }
        }
    }

    override fun getScreenName() = TAG

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

        viewModel.submitAction(ProductPreviewUiAction.InitializeProductMainData)
    }

    private fun setupViews() {
        setupProductContentViews()
        setupProductIndicatorViews()
    }

    private fun setupProductContentViews() = with(binding.rvContentProduct) {
        adapter = productContentAdapter
        layoutManager = layoutManagerContent
        itemAnimator = null
        removeOnScrollListener(contentScrollListener)
        addOnScrollListener(contentScrollListener)
        snapHelperContent.attachToRecyclerView(this)
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

        productContentAdapter.updateData(state)
    }

    private fun renderIndicator(
        prev: List<ProductIndicatorUiModel>?,
        state: List<ProductIndicatorUiModel>
    ) {
        if (prev == state) return

        try {
            val selectedData = state.firstOrNull { it.selected }
            val position = state.indexOf(selectedData)
            binding.tvIndicatorLabel.apply {
                visible()
                text = String.format(
                    getString(contentproductpreviewR.string.text_label_place_holder),
                    position.plus(1),
                    state.size,
                    state[position].variantName
                )
            }
        } catch (_: Exception) {
            binding.tvIndicatorLabel.gone()
        }
        productIndicatorAdapter.submitList(state)
    }

    private fun getContentCurrentPosition(): Int {
        val snappedView =
            snapHelperContent.findSnapView(layoutManagerContent) ?: return RecyclerView.NO_POSITION
        return binding.rvContentProduct.getChildAdapterPosition(snappedView)
    }

    private fun scrollTo(position: Int) {
        binding.rvContentProduct.scrollToPosition(position)
        binding.rvIndicatorProduct.scrollToPosition(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvContentProduct.removeOnScrollListener(contentScrollListener)
        videoPlayerManager.releaseAll()
        _binding = null
    }

    companion object {
        const val TAG = "ProductFragment"

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ProductFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ProductFragment
        }
    }
}
