package com.tokopedia.product.edit.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.adapter.ProductAddVideoAdapterTypeFactory
import com.tokopedia.product.edit.mapper.VideoViewMapper
import com.tokopedia.product.edit.viewmodel.EmptyVideoViewModel
import com.tokopedia.product.edit.viewmodel.ProductAddVideoBaseViewModel
import com.tokopedia.product.edit.viewmodel.TitleVideoChoosenViewModel
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel
import java.util.ArrayList

class ProductAddVideoFragment : BaseListFragment<ProductAddVideoBaseViewModel, ProductAddVideoAdapterTypeFactory>() {

    private var videoIDs: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        if(activity.intent != null){
            videoIDs = activity.intent.getStringArrayListExtra(EXTRA_VIDEOS_LINKS)
        }
        (activity as AppCompatActivity).supportActionBar!!.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_product_add_video, container, false)
    }

    override fun getAdapterTypeFactory(): ProductAddVideoAdapterTypeFactory {
        return ProductAddVideoAdapterTypeFactory()
    }

    override fun onItemClicked(t: ProductAddVideoBaseViewModel?) {

    }

    override fun getScreenName(): String {
        return getString(R.string.title_activity_video)
    }

    override fun initInjector() {}

    override fun loadData(page: Int) {
        val mapper = VideoViewMapper()
        val productAddVideoBaseViewModels : ArrayList<ProductAddVideoBaseViewModel> = ArrayList()

        if(!videoIDs.isEmpty()){
            productAddVideoBaseViewModels.addAll(mapper.transform(videoIDs))
            val titleVideoChoosenViewModel = TitleVideoChoosenViewModel()
            productAddVideoBaseViewModels.add(0, titleVideoChoosenViewModel)
            (activity as AppCompatActivity).supportActionBar!!.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
        } else {
            val emptyVideoViewModel = EmptyVideoViewModel()
            productAddVideoBaseViewModels.add(0, emptyVideoViewModel)
        }
        val videoRecommendationViewModel = VideoRecommendationViewModel()
        productAddVideoBaseViewModels.add(0, videoRecommendationViewModel)
        renderList(productAddVideoBaseViewModels)
    }

    companion object {
        const val EXTRA_VIDEOS_LINKS = "KEY_VIDEOS_LINK"

        const val MAX_VIDEO = 3
        const val MAX_VIDEO_RECOMMENDATION = 3

        fun createInstance(): android.support.v4.app.Fragment {
            return ProductAddVideoFragment()
        }
    }
}
