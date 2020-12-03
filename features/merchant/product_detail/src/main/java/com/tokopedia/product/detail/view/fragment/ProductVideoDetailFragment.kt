package com.tokopedia.product.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.activity.ProductDetailActivityInterface
import com.tokopedia.product.detail.view.adapter.ProductVideoDetailAdapter
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator

class ProductVideoDetailFragment : Fragment() {

    private var sharedViewModel: ProductDetailSharedViewModel? = null
    private var videoCoordinator: ProductVideoCoordinator? = null
    private var videoDetailAdapter: ProductVideoDetailAdapter? = null
    private var activityListener: ProductDetailActivityInterface? = null

    private var videoDetailPager: ViewPager2? = null
    private var closeBtn: IconUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(ProductDetailSharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_video_viewer, container, false)
    }

    fun onBackButtonClicked() {
        videoCoordinator?.onStopAndSaveLastPosition()
        sharedViewModel?.updateVideoDataInPreviousFragment(videoCoordinator?.getVideoDataModel()
                ?: listOf())
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

        videoDetailAdapter = ProductVideoDetailAdapter(videoCoordinator)
        videoDetailPager?.adapter = videoDetailAdapter

        setupViewPagerCallback(videoCoordinator)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun observeData() {
        sharedViewModel?.productVideoDetailData?.observeOnce(requireActivity(), {
            if (it.isNotEmpty()) {
                val newInstanceData = it.map { oldData ->
                    oldData.copy()
                }

                videoDetailPager?.isUserInputEnabled = newInstanceData.size != 1

                videoDetailAdapter?.mediaData = newInstanceData
                videoDetailAdapter?.notifyDataSetChanged()

                videoDetailPager?.addOneTimeGlobalLayoutListener {
                    videoDetailPager?.let { vp ->
                        videoCoordinator?.onScrollChangedListener(vp, 0)
                    }
                }
            }
        })
    }

    private fun initView(view: View) {
        videoDetailPager = view.findViewById(R.id.pdp_video_detail_pager)
        closeBtn = view.findViewById(R.id.pdp_video_detail_close)
    }

    private fun setupViewPagerCallback(productVideoCoordinator: ProductVideoCoordinator?) {
        videoDetailPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
}