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
import com.tokopedia.content.product.preview.databinding.FragmentProductBinding
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter
import com.tokopedia.content.product.preview.view.adapter.product.ProductIndicatorAdapter
import com.tokopedia.content.product.preview.view.components.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.ProductPreviewVideoPlayerManager
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.model.ProductVideoModel
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import timber.log.Timber
import javax.inject.Inject

class ProductFragment @Inject constructor(): TkpdBaseV4Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding: FragmentProductBinding
        get() = _binding!!

    private var productContentAdapter: ProductContentAdapter? = null
    private var productIndicatorAdapter: ProductIndicatorAdapter? = null

    private var snapHelper: PagerSnapHelper = PagerSnapHelper()
    private val layoutManager by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), HORIZONTAL, false)
    }

    private val videoPlayerManager by lazy { ProductPreviewVideoPlayerManager(requireContext()) }
    private val contentScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == ViewPager2.SCROLL_STATE_IDLE) {
                val position = getCurrentPosition()
                Timber.d("position $position")
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
    }

    private fun setupProductContentViews() {
        productContentAdapter = ProductContentAdapter(listener = object : ProductPreviewListener {
            override fun getVideoPlayer(id: String): ProductPreviewExoPlayer {
                return videoPlayerManager.occupy(id)
            }
        })
        binding.rvContentProduct.adapter = productContentAdapter
        binding.rvContentProduct.layoutManager = layoutManager
        binding.rvContentProduct.hasFixedSize()
        binding.rvContentProduct.removeOnScrollListener(contentScrollListener)
        binding.rvContentProduct.addOnScrollListener(contentScrollListener)
        snapHelper.attachToRecyclerView(binding.rvContentProduct)

        productContentAdapter?.insertData(listOf(
            ProductVideoModel(
                "1",
                "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=f01396ff94ae71eeae0987c7371d0102"
            ),
            ProductVideoModel(
                "2",
                "https://vod.tokopedia.com/view/adaptive.m3u8?id=515438f0665971edad0287c7361c0102"
            ),
            ProductVideoModel(
                "3",
                "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=d0c8f02d94aa71eeb0805114c0db0102"
            ),
            ProductVideoModel(
                "4",
                "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=10795c6294ab71eeb0215114c1ca0102"
            ),
            ProductVideoModel(
                "5",
                "https://vod-stream.tokopedia.net/view/adaptive.m3u8?id=1009f0ef94ae71ee8e0597c6360c0102"
            ),
        ))
    }

    private fun getCurrentPosition(): Int {
        val snappedView = snapHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return binding.rvContentProduct.getChildAdapterPosition(snappedView)
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
