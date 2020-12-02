package com.tokopedia.product.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.activity.ProductDetailActivityInterface
import com.tokopedia.product.detail.view.adapter.ProductVideoDetailAdapter
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel
import kotlinx.android.synthetic.main.fragment_product_video_viewer.*

class ProductVideoDetailFragment : Fragment() {

    companion object {
        const val PRODUCT_DETAIL_VIDEO_BUNDLE_KEY = "product_detail_video_bundle_key"
        fun getFragment(cacheId: String): ProductVideoDetailFragment = ProductVideoDetailFragment().also {
            it.arguments = Bundle().apply {
                putString(PRODUCT_DETAIL_VIDEO_BUNDLE_KEY, cacheId)
            }
        }
    }

    private var sharedViewModel: ProductDetailSharedViewModel? = null
    private var videoCoordinator: ProductVideoCoordinator? = null
    private var videoDetailAdapter: ProductVideoDetailAdapter? = null
    private var videoDetailPager: ViewPager2? = null
    private var closeBtn: IconUnify? = null
    private var mediaList: List<ProductVideoDataModel> = listOf()
    private var activityListener: ProductDetailActivityInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getArgumentsBundle()
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(ProductDetailSharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_video_viewer, container, false)
    }

    fun onBackButtonClicked() {
        videoCoordinator?.onStopAndSaveLastPosition()
        sharedViewModel?.updateVideoData(videoCoordinator?.getVideoDataModel() ?: listOf())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityListener = activity as ProductDetailActivityInterface
        initView(view)

        closeBtn?.setOnClickListener {
            onBackButtonClicked()
            activityListener?.onBackPressed()
        }

        videoCoordinator = ProductVideoCoordinator(viewLifecycleOwner)
        videoCoordinator?.fillInitialData(mediaList)

        videoDetailAdapter = ProductVideoDetailAdapter(videoCoordinator, mediaList)
        videoDetailPager?.adapter = videoDetailAdapter

        setupViewPagerCallback(videoCoordinator)

        videoDetailPager?.addOneTimeGlobalLayoutListener {
            videoDetailPager?.let {
                videoCoordinator?.onScrollChangedListener(it, 0)
            }
        }
    }

    private fun initView(view: View) {
        videoDetailPager = view.findViewById(R.id.pdp_video_detail_pager)
        closeBtn = view.findViewById(R.id.pdp_video_detail_close)
    }

    private fun setupViewPagerCallback(productVideoCoordinator: ProductVideoCoordinator?) {
        pdp_video_detail_pager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var lastPosition = 0
            override fun onPageSelected(position: Int) {
                lastPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    videoDetailPager?.let {
                        productVideoCoordinator?.onScrollChangedListener(it, lastPosition)
                    }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
    }

    private fun getArgumentsBundle() {
        if (context == null) return

        arguments?.let {
            val cacheId = it.getString(PRODUCT_DETAIL_VIDEO_BUNDLE_KEY)
            val cacheManager = SaveInstanceCacheManager(requireContext(), cacheId)
            val parcelData: ProductVideoDetailDataModel = cacheManager.get(
                    this::class.java.simpleName,
                    ProductVideoDetailDataModel::class.java
            ) ?: ProductVideoDetailDataModel()

            mediaList = parcelData.data
        }
    }
}

data class ProductVideoDetailDataModel(
        val data: MutableList<ProductVideoDataModel> = mutableListOf()
)