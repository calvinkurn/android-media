package com.tokopedia.product.edit.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.adapter.ProductAddVideoRecommendationAdapterTypeFactory
import com.tokopedia.product.edit.view.listener.ProductAddVideoRecommendationView
import com.tokopedia.product.edit.domain.mapper.VideoRecommendationMapper
import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.view.presenter.ProductAddVideoRecommendationPresenter
import com.tokopedia.product.edit.view.viewmodel.ProductAddVideoRecommendationBaseViewModel
import com.tokopedia.product.edit.view.viewmodel.TitleVideoRecommendationViewModel
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel
import java.util.ArrayList

class ProductAddVideoRecommendationFragment : BaseListFragment<ProductAddVideoRecommendationBaseViewModel, ProductAddVideoRecommendationAdapterTypeFactory>(), ProductAddVideoRecommendationView {

    override val contextView: Context get() = activity

    var youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel> = ArrayList()
    var videoIDs: ArrayList<String> = ArrayList()

    private lateinit var productAddVideoRecommendationPresenter: ProductAddVideoRecommendationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        GraphqlClient.init(activity.applicationContext)
        productAddVideoRecommendationPresenter = ProductAddVideoRecommendationPresenter()
        productAddVideoRecommendationPresenter.attachView(this)

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_default);
        if(activity.intent != null){
            youtubeVideoModelArrayList = activity.intent.getParcelableArrayListExtra<YoutubeVideoModel>(ProductAddVideoFragment.EXTRA_VIDEO_RECOMMENDATION)
            videoIDs = activity.intent.getStringArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEOS_LINKS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_product_add_video_recommendation, container, false)

        val btnSimpan: Button = view.findViewById(R.id.button_simpan)

        btnSimpan.setOnClickListener({

        })

        return view
    }

    override fun getAdapterTypeFactory(): ProductAddVideoRecommendationAdapterTypeFactory {
        return ProductAddVideoRecommendationAdapterTypeFactory()
    }

    override fun onItemClicked(t: ProductAddVideoRecommendationBaseViewModel?) {

    }

    override fun getScreenName(): String {
        return getString(R.string.title_activity_video_recommendation)
    }

    override fun initInjector() {}

    override fun loadData(page: Int) {
        val productAddVideoRecommendationBaseViewModel : ArrayList<ProductAddVideoRecommendationBaseViewModel> = ArrayList()
        val mapper = VideoRecommendationMapper()

        val videoRecommendationViewModelList: List<VideoRecommendationViewModel> = mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList)
        for(videoID in videoIDs){
            for(videoRecommendationViewModel in videoRecommendationViewModelList){
                if(videoRecommendationViewModel.videoID == videoID){
                    videoRecommendationViewModel.chosen = true
                }
            }
        }

        productAddVideoRecommendationBaseViewModel.addAll(videoRecommendationViewModelList)

        val titleVideoRecommendationViewModel = TitleVideoRecommendationViewModel()
        productAddVideoRecommendationBaseViewModel.add(0, titleVideoRecommendationViewModel)

        renderList(productAddVideoRecommendationBaseViewModel)
    }

    companion object {

        fun createInstance(): android.support.v4.app.Fragment {
            return ProductAddVideoRecommendationFragment()
        }
    }
}
