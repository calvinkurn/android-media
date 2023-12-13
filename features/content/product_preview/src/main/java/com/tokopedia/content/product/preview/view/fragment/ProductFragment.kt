package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.product.preview.data.ContentUiModel
import com.tokopedia.content.product.preview.data.product.ProductContentUiModel
import com.tokopedia.content.product.preview.data.product.ProductIndicatorUiModel
import com.tokopedia.content.product.preview.databinding.FragmentProductBinding
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter
import com.tokopedia.content.product.preview.view.adapter.product.ProductIndicatorAdapter
import com.tokopedia.content.product.preview.view.components.items.ProductPreviewIndicatorItemDecoration
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewVideoPlayerManager
import com.tokopedia.content.product.preview.view.listener.ProductPreviewIndicatorListener
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductFragment @Inject constructor() : TkpdBaseV4Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding: FragmentProductBinding
        get() = _binding!!

    private var productContentAdapter: ProductContentAdapter? = null
    private var productIndicatorAdapter: ProductIndicatorAdapter? = null

    private var snapHelperContent: PagerSnapHelper = PagerSnapHelper()

    private val layoutManagerContent by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }
    private val layoutManagerIndicator by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }

    private val videoPlayerManager by lazy { ProductPreviewVideoPlayerManager(requireContext()) }
    private val contentScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == ViewPager2.SCROLL_STATE_IDLE) {
                val position = getContentCurrentPosition()
                setupTextLabelIndicatorViews(position = position)
                productIndicatorAdapter?.updateSelected(position)
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
    }

    private fun setupViews() {
        setupProductContentViews()
        setupTextLabelIndicatorViews(position = 0)
        setupProductIndicatorViews()
    }

    private fun setupProductContentViews() = with(binding.rvContentProduct) {
        productContentAdapter = ProductContentAdapter(listener = object : ProductPreviewListener {
            override fun getVideoPlayer(id: String): ProductPreviewExoPlayer {
                return videoPlayerManager.occupy(id)
            }
        })
        adapter = productContentAdapter
        layoutManager = layoutManagerContent
        hasFixedSize()
        removeOnScrollListener(contentScrollListener)
        addOnScrollListener(contentScrollListener)
        snapHelperContent.attachToRecyclerView(this)

        productContentAdapter?.insertData(mockData().content)
    }

    private fun setupTextLabelIndicatorViews(
        position: Int,
    ) = with(binding.tvIndicatorLabel) {
        text = String.format(
            getString(contentproductpreviewR.string.text_label_place_holder),
            position.plus(1), mockData().labels.size, mockData().labels[position]
        )
    }

    private fun setupProductIndicatorViews() = with(binding.rvIndicatorProduct) {
        productIndicatorAdapter = ProductIndicatorAdapter(listener = object :
            ProductPreviewIndicatorListener {
            override fun onClickProductIndicator(position: Int) {
                scrollTo(position)
                setupTextLabelIndicatorViews(position = position)
                productIndicatorAdapter?.updateSelected(position)
            }
        })
        adapter = productIndicatorAdapter
        layoutManager = layoutManagerIndicator
        itemAnimator = null
        if (itemDecorationCount == 0) {
            addItemDecoration(ProductPreviewIndicatorItemDecoration(requireContext()))
        }

        productIndicatorAdapter?.insertData(mockData().indicators)
    }

    private fun getContentCurrentPosition(): Int {
        val snappedView = snapHelperContent.findSnapView(layoutManagerContent) ?: return RecyclerView.NO_POSITION
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

fun mockData() = ProductContentUiModel(
    id = "productID_123",
    content = listOf(
        ContentUiModel(
            type = ContentUiModel.MediaType.Video,
            url = "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=f01396ff94ae71eeae0987c7371d0102",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Video,
            url = "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=f01396ff94ae71eeae0987c7371d0102",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png",
        ),
        ContentUiModel(
            type = ContentUiModel.MediaType.Image,
            url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png",
        ),
    ),
    labels = listOf(
        "Variant 1",
        "Variant 2",
        "Variant 3",
        "Variant 4",
        "Variant 5",
        "Variant 6",
        "Variant 7",
        "Variant 8",
        "Variant 9",
        "Variant 10",
    ),
    indicators = listOf(
        ProductIndicatorUiModel(
            id = "1",
            selected = true,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Video,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png",
            ),
        ),
        ProductIndicatorUiModel(
            id = "2",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png",
            ),
        ),
        ProductIndicatorUiModel(
            id = "3",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg",
            ),
        ),
        ProductIndicatorUiModel(
            id = "4",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png",
            ),
        ),
        ProductIndicatorUiModel(
            id = "5",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png",
            ),
        ),
        ProductIndicatorUiModel(
            id = "6",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/hDjmkQ/2023/2/4/6a3db555-a5e9-4bc1-9c17-1753ad105b92.jpg",
            ),
        ),
        ProductIndicatorUiModel(
            id = "7",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/9/14/9d770fbf-2bbd-4206-8511-56df29a6f4be.png",
            ),
        ),
        ProductIndicatorUiModel(
            id = "8",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/8/25/1f559a48-03f3-4656-b77f-3caf0fcc94d2.png",
            ),
        ),
        ProductIndicatorUiModel(
            id = "9",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Video,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png",
            ),
        ),
        ProductIndicatorUiModel(
            id = "10",
            selected = false,
            content = ContentUiModel(
                type = ContentUiModel.MediaType.Image,
                url = "https://images.tokopedia.net/img/cache/700/VqbcmM/2022/12/12/ca158fc4-699a-495e-aaac-229b4f8ed1aa.png",
            ),
        ),
    )
)
