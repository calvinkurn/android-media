package com.tokopedia.home_recom.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.analytics.SimilarProductRecommendationTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.SimilarProductRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.SimilarProductRecommendationItemDataModel
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created by Lukas on 26/08/19
 */
open class SimilarProductRecommendationFragment : BaseListFragment<SimilarProductRecommendationDataModel, SimilarProductRecommendationTypeFactoryImpl>(), RecommendationListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapterFactory by lazy { SimilarProductRecommendationTypeFactoryImpl() }
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val recommendationViewModel by lazy { viewModelProvider.get(SimilarProductRecommendationViewModel::class.java) }
    private val adapter by lazy { SimilarProductRecommendationAdapter(adapterFactory) }
    private val staggeredGrid by lazy { StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL) }
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var ref: String
    private lateinit var productId: String

    companion object{
        private const val SPAN_COUNT = 2
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 399

        fun newInstance(productId: String = "", ref: String = "") = SimilarProductRecommendationFragment().apply {
            this.ref = ref
            this.productId = productId
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { trackingQueue = TrackingQueue(it) }
        RecommendationPageTracking.sendScreenSimilarProductRecommendationPage("/rekomendasi/d", ref, productId)
        getRecyclerView(view)?.apply {
            if(this is VerticalRecyclerView) clearItemDecoration()
            layoutManager = recyclerViewLayoutManager
        }
        enableLoadMore()
        recommendationViewModel.recommendationItem.observe(this, Observer {
            it?.let {
                when {
                    it.status.isLoading() || it.status.isLoadMore()  -> showLoading()
                    it.status.isEmpty() -> showEmpty()
                    it.status.isError() -> showGetListError(Throwable(it.message))
                    it.status.isSuccess() -> {
                        renderList(mapDataModel(it.data ?: emptyList()), true)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                if(position >= 0) {
                    (adapter.data[position] as SimilarProductRecommendationItemDataModel).productItem.isWishlist = wishlistStatusFromPdp
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun getAdapterTypeFactory(): SimilarProductRecommendationTypeFactoryImpl = adapterFactory

    override fun createAdapterInstance(): BaseListAdapter<SimilarProductRecommendationDataModel, SimilarProductRecommendationTypeFactoryImpl> {
        return adapter
    }

    override fun onItemClicked(item: SimilarProductRecommendationDataModel?) {

    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        recommendationViewModel.getSimilarProductRecommendation(page, ref, productId)
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return staggeredGrid
    }

    override fun getEndlessLayoutManagerListener(): EndlessLayoutManagerListener? {
        return EndlessLayoutManagerListener { recyclerViewLayoutManager }
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
        if(recommendationViewModel.isLoggedIn()) SimilarProductRecommendationTracking.eventClick(trackingQueue, item, item.position.toString(), ref)
        else SimilarProductRecommendationTracking.eventClickNonLogin(trackingQueue, item, item.position.toString(), ref)
        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position.first())
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling product impression item
     * @param item the item clicked
     */
    override fun onProductImpression(item: RecommendationItem) {
        if(recommendationViewModel.isLoggedIn()) SimilarProductRecommendationTracking.eventImpression(trackingQueue, item, item.position.toString(), ref)
        else SimilarProductRecommendationTracking.eventImpressionNonLogin(trackingQueue, item, item.position.toString(), ref)
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling wishlist click from item
     * @param item the item clicked
     * @param isAddWishlist the wishlist is selected or not
     * @param callback the callback for notify when success or not, there are have 2 params [Boolean] and [Throwable]
     */
    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if(recommendationViewModel.isLoggedIn()){
            SimilarProductRecommendationTracking.eventClickWishlist(trackingQueue, isAddWishlist)
            if(isAddWishlist){
                recommendationViewModel.addWishlist(item, callback)
            } else {
                recommendationViewModel.removeWishlist(item, callback)
            }
        }else{
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    /**
     * =================================================================================
     * Private function
     * =================================================================================
     */

    /**
     * Function [mapDataModel]
     * It handling mapper pojo into dataModel
     * @param listRecommendationModel list pojo recommendationWidget from API
     * @return list of dataModel
     */
    private fun mapDataModel(listRecommendationModel: List<RecommendationItem>): List<SimilarProductRecommendationDataModel>{
        return listRecommendationModel.map { SimilarProductRecommendationItemDataModel(it, this) }
    }
}