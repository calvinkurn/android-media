package com.tokopedia.product.edit.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.adapter.ProductAddVideoRecommendationAdapterTypeFactory
import com.tokopedia.product.edit.listener.ProductAddVideoRecommendationView
import com.tokopedia.product.edit.mapper.VideoRecommendationMapper
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.presenter.ProductAddVideoRecommendationPresenter
import com.tokopedia.product.edit.viewmodel.ProductAddVideoRecommendationBaseViewModel
import com.tokopedia.product.edit.viewmodel.TitleVideoRecommendationViewModel
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel
import java.util.ArrayList

class ProductAddVideoRecommendationFragment : BaseListFragment<ProductAddVideoRecommendationBaseViewModel, ProductAddVideoRecommendationAdapterTypeFactory>(), ProductAddVideoRecommendationView {

    override val contextView: Context get() = activity

    private lateinit var productAddVideoRecommendationPresenter: ProductAddVideoRecommendationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        GraphqlClient.init(activity.applicationContext)
        productAddVideoRecommendationPresenter = ProductAddVideoRecommendationPresenter()
        productAddVideoRecommendationPresenter.attachView(this)

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_default);
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_product_add_video_recommendation, container, false)
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
        productAddVideoRecommendationPresenter.getYoutubeDataVideoRecommendation("iphone", MAX_VIDEO_RECOMMENDATION)
    }

    override fun onErrorGetVideoRecommendation(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        val productAddVideoRecommendationBaseViewModel : ArrayList<ProductAddVideoRecommendationBaseViewModel> = ArrayList()
        val mapper = VideoRecommendationMapper()

        val videoRecommendationViewModelList: List<VideoRecommendationViewModel> = mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList)
        productAddVideoRecommendationBaseViewModel.addAll(videoRecommendationViewModelList)

        val titleVideoRecommendationViewModel = TitleVideoRecommendationViewModel()
        productAddVideoRecommendationBaseViewModel.add(0, titleVideoRecommendationViewModel)

        renderList(videoRecommendationViewModelList)
    }

    companion object {
        const val MAX_VIDEO_RECOMMENDATION = 20

        fun createInstance(): android.support.v4.app.Fragment {
            return ProductAddVideoRecommendationFragment()
        }
    }
}
