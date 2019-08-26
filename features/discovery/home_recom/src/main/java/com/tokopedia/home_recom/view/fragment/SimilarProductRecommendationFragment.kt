package com.tokopedia.home_recom.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import javax.inject.Inject

/**
 * Created by Lukas on 26/08/19
 */
class SimilarProductRecommendationFragment : BaseListFragment<HomeRecommendationDataModel, SimilarProductRecommendationTypeFactoryImpl>(), RecommendationListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapterFactory by lazy { SimilarProductRecommendationTypeFactoryImpl() }
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val recommendationViewModel by lazy { viewModelProvider.get(SimilarProductRecommendationViewModel::class.java) }
    private val adapter by lazy { SimilarProductRecommendationAdapter(adapterFactory) }

    companion object{
        private const val SPAN_COUNT = 2
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableLoadMore()
        getRecyclerView(view).layoutManager = recyclerViewLayoutManager
        recommendationViewModel.recommendationItem.observe(this, Observer {
            it?.let {
                when {
                    it.status.isLoading() -> showLoading()
                    it.status.isEmpty() -> showEmpty()
                    it.status.isError() -> showGetListError(Throwable(it.message))
                    it.status.isSuccess() -> {
                        renderList(mapDataModel(it.data ?: emptyList()))
                    }
                    it.status.isLoadMore() -> {

                    }
                }
            }
        })
    }

    override fun getAdapterTypeFactory(): SimilarProductRecommendationTypeFactoryImpl = adapterFactory

    override fun createAdapterInstance(): BaseListAdapter<HomeRecommendationDataModel, SimilarProductRecommendationTypeFactoryImpl> {
        return adapter
    }

    override fun onItemClicked(item: HomeRecommendationDataModel?) {

    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        recommendationViewModel.getSimilarProductRecommendation(page)
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }


    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(getRecyclerView(this.view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoadMore()
                loadData(page)
            }
        }
    }

    /**
     * =================================================================================
     * Listener from [RecommendationListener]
     * =================================================================================
     */

    /**
     * This void from Callback [RecommendationListener]
     * It handling item click
     * @param item the item clicked
     * @param layoutType the layoutType is type layout where item placed
     * @param position list of position of the item at Adapter, can be [1] or [1,2] for dynamic nested item
     */
    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {

    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling product impression item
     * @param item the item clicked
     */
    override fun onProductImpression(item: RecommendationItem) {

    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling wishlist click from item
     * @param item the item clicked
     * @param isAddWishlist the wishlist is selected or not
     * @param callback the callback for notify when success or not, there are have 2 params [Boolean] and [Throwable]
     */
    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {

    }

    /**
     * =================================================================================
     * Private function
     * =================================================================================
     */

    /**
     * Void [onErrorGetRecommendation]
     * It handling show error when failed fetch Recommendation List from viewModel
     */
    private fun onErrorGetRecommendation(errorMessage: String?) {
        showGetListError(Throwable(errorMessage))
    }

    /**
     * Void [showLoadMore]
     */
    private fun showLoadMore(){

    }

    /**
     * Function [mapDataModel]
     * It handling mapper pojo into dataModel
     * @param listRecommendationModel list pojo recommendationWidget from API
     * @return list of dataModel
     */
    private fun mapDataModel(listRecommendationModel: List<RecommendationItem>): List<HomeRecommendationDataModel>{
        return listRecommendationModel.map { RecommendationItemDataModel(it, this) }
    }
}