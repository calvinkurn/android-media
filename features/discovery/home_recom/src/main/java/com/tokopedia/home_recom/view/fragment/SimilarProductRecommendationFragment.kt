package com.tokopedia.home_recom.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.analytics.SimilarProductRecommendationTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.SimilarProductRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.SimilarProductRecommendationItemDataModel
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
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
    private var trackingQueue: TrackingQueue? = null
    private var ref: String = ""
    private var source: String = ""
    private var productId: String = ""
    private var internalRef: String = ""

    companion object{
        private const val SPAN_COUNT = 2
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val SAVED_REF = "saved_ref"
        private const val SAVED_SOURCE = "saved_source"
        private const val REQUEST_FROM_PDP = 399

        @SuppressLint("SyntheticAccessor")
        fun newInstance(productId: String = "", ref: String = "", source: String = "", internalRef: String = "") = SimilarProductRecommendationFragment().apply {
            this.ref = ref
            this.source = source
            this.productId = productId
            this.internalRef = internalRef
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run{
            (this as AppCompatActivity).supportActionBar?.title = getString(R.string.recom_similar_recommendation)
        }
        savedInstanceState?.let{
            productId = it.getString(SAVED_PRODUCT_ID) ?: ""
            ref = it.getString(SAVED_REF) ?: ""
            source = it.getString(SAVED_SOURCE) ?: ""
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
        recommendationViewModel.recommendationItem.observe(viewLifecycleOwner, Observer {
            it?.let {
                when {
                    it.status.isLoading() || it.status.isLoadMore()  -> showLoading()
                    it.status.isEmpty() -> showEmpty()
                    it.status.isError() -> showGetListError(Throwable(it.message))
                    it.status.isSuccess() -> {
                        if(it.data?.isNotEmpty() == true){
                            it.data[0].let {
                                activity?.run{
                                    (this as AppCompatActivity).supportActionBar?.title = if(it.header.isNotEmpty()) it.header else getString(R.string.recom_similar_recommendation)
                                }
                            }
                        }
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
                if(position >= 0 && adapter.data.size > position) {
                    (adapter.data[position] as SimilarProductRecommendationItemDataModel).productItem.isWishlist = wishlistStatusFromPdp
                    adapter.notifyItemChanged(position, wishlistStatusFromPdp)
                }
            }
        }
        handleProductCardOptionsActivityResult(requestCode, resultCode, data,
                object : ProductCardOptionsWishlistCallback {
                    override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                        handleWishlistAction(productCardOptionsModel)
                    }
                })
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return
        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                if (wishlistResult.isAddWishlist) {
                    if(productId.isNotBlank() || productId.isNotEmpty()){
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(true, ref)
                    }else {
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(true, productCardOptionsModel.screenName, ref)
                    }
                    showMessageSuccessAddWishlist()
                } else {
                    if(productId.isNotBlank() || productId.isNotEmpty()){
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(false, ref)
                    }else {
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(false, productCardOptionsModel.screenName, ref)
                    }
                    showMessageSuccessRemoveWishlist()
                }
                updateWishlist(wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
            } else {
                showMessageFailedWishlistAction()
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        if(position > -1 && adapter.itemCount > 0 &&
                adapter.itemCount > position) {
            (adapter.data[position] as SimilarProductRecommendationItemDataModel).productItem.isWishlist = isWishlist
            adapter.notifyItemChanged(position, isWishlist)
        }
    }

    private fun showMessageSuccessAddWishlist() {
        if (activity == null) return
        val view = activity!!.findViewById<View>(android.R.id.content)
        val message = getString(R.string.recom_msg_success_add_wishlist)
        view?.let {
            Toaster.make(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.home_recom_go_to_wishlist),
                    View.OnClickListener { goToWishlist() })
        }
    }

    private fun goToWishlist() {
        if (activity == null) return
        RouteManager.route(activity, ApplinkConst.NEW_WISHLIST)
    }

    private fun showMessageSuccessRemoveWishlist() {
        if (activity == null) return
        val view = activity!!.findViewById<View>(android.R.id.content)
        val message = getString(R.string.recom_msg_success_remove_wishlist)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showMessageFailedWishlistAction() {
        if (activity == null) return
        val view = activity?.findViewById<View>(android.R.id.content)
        view?.let { Toaster.make(it, ErrorHandler.getErrorMessage(activity, null), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_PRODUCT_ID, productId)
        outState.putString(SAVED_REF, ref)
    }

    /**
     * [onPause] is override from [BaseListFragment]
     * this void override with added extra sendAllTracking
     */
    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
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
        recommendationViewModel.getSimilarProductRecommendation(page, source, productId)
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
        if(recommendationViewModel.isLoggedIn()) SimilarProductRecommendationTracking.eventClick(item, item.position.toString(), ref, internalRef)
        else SimilarProductRecommendationTracking.eventClickNonLogin(item, item.position.toString(), ref, internalRef)
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
        trackingQueue?.let { trackingQueue ->
            if(recommendationViewModel.isLoggedIn()) SimilarProductRecommendationTracking.eventImpression(trackingQueue, item, item.position.toString(), ref, internalRef)
            else SimilarProductRecommendationTracking.eventImpressionNonLogin(trackingQueue, item, item.position.toString(), ref, internalRef)
        }
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
            SimilarProductRecommendationTracking.eventClickWishlist(isAddWishlist)
            if(isAddWishlist){
                recommendationViewModel.addWishlist(item, callback)
            } else {
                recommendationViewModel.removeWishlist(item, callback)
            }
        }else{
            SimilarProductRecommendationTracking.eventClickWishlistNonLogin()
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        showProductCardOptions(
                this,
                createProductCardOptionsModel(item, position[0])
        )
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

    private fun createProductCardOptionsModel(recommendationItem: RecommendationItem, position: Int): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = recommendationItem.isWishlist
        productCardOptionsModel.productId = recommendationItem.productId.toString()
        productCardOptionsModel.isTopAds = recommendationItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl = recommendationItem.wishlistUrl
        productCardOptionsModel.productPosition = position
        productCardOptionsModel.screenName = recommendationItem.header
        return productCardOptionsModel
    }
}