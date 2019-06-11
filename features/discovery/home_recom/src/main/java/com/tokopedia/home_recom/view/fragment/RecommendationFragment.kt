package com.tokopedia.home_recom.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.TYPE_CAROUSEL
import com.tokopedia.recommendation_widget_common.TYPE_SCROLL
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import javax.inject.Inject

class RecommendationFragment: BaseListFragment<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(), RecommendationCardView.TrackingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var productId: String
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl() }
    private val adapter by lazy { HomeRecommendationAdapter(adapterTypeFactory) }
    private val recommendationWidgetViewModel by lazy { viewModelProvider.get(RecommendationPageViewModel::class.java) }

    companion object{
        private const val SPAN_COUNT = 2

        fun newInstance(productId: String) = RecommendationFragment().apply {
            this.productId = productId
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disableLoadMore()
        getRecyclerView(view).layoutManager = recyclerViewLayoutManager
        recommendationWidgetViewModel.productInfoDataModel.observe(this, Observer {
            it?.let {
                primaryProduct ->
                displayProductInfo(primaryProduct)
            }
        })

        recommendationWidgetViewModel.recommendationListModel.observe(this, Observer {
            it?.let { recommendationList ->
                renderList(mapDataModel(recommendationList))
            }
        })

        recommendationWidgetViewModel.getPrimaryProduct(productId, activity!!)

        recommendationWidgetViewModel.getRecommendationList(arrayListOf(productId),
                onErrorGetRecommendation = this::onErrorGetRecommendation)

    }

    private fun onErrorGetRecommendation(errorMessage: String?) {
        showGetListError(Throwable(errorMessage))
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getAdapterTypeFactory(): HomeRecommendationTypeFactoryImpl {
        return adapterFactory
    }

    override fun createAdapterInstance(): BaseListAdapter<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl> {
        return adapter
    }

    override fun onItemClicked(t: HomeRecommendationDataModel) {
        //Do nothing
    }

    override fun getScreenName(): String = ""


    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        //Do nothing
    }

    override fun disableLoadMore() {
        super.disableLoadMore()
        getRecyclerView(view).isNestedScrollingEnabled = false
    }

    private fun displayProductInfo(dataModel: ProductInfoDataModel){
        childFragmentManager.beginTransaction()
                .replace(R.id.product_info_container, ProductInfoFragment.newInstance(dataModel))
                .commit()
    }

    private fun mapDataModel(listRecommendationModelDummy: List<RecommendationWidget>): List<HomeRecommendationDataModel>{
        val list = ArrayList<HomeRecommendationDataModel>()
        listRecommendationModelDummy.forEach { recommendationWidget ->
            when(recommendationWidget.layoutType){
                TYPE_SCROLL -> {
                    list.add(TitleDataModel(recommendationWidget.title))
                    recommendationWidget.recommendationItemList.forEach {
                        list.add(RecommendationItemDataModel(it, this))
                    }
                }
                TYPE_CAROUSEL -> list.add(RecommendationCarouselDataModel(recommendationWidget.title, recommendationWidget.recommendationItemList, this))
            }
        }
        return list
    }

    override fun onImpressionTopAds(item: RecommendationItem) {}

    override fun onImpressionOrganic(item: RecommendationItem) {}

    override fun onClickTopAds(item: RecommendationItem) {}

    override fun onClickOrganic(item: RecommendationItem) {}

}