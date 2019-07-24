package com.tokopedia.home_recom.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.recommendation_widget_common.TYPE_CAROUSEL
import com.tokopedia.recommendation_widget_common.TYPE_CUSTOM_HORIZONTAL
import com.tokopedia.recommendation_widget_common.TYPE_SCROLL
import com.tokopedia.home_recom.view.listener.TrackingListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * A Class of Recommendation Fragment.
 *
 * This class for handling Recommendation Page, it will shown List of ProductDetail, Recommendation Items, Recommendation Carousel
 *
 * @property viewModelFactory the factory for ViewModel provide by Dagger.
 * @property trackingQueue the queue util for handle tracking.
 * @property productId the productId of ProductDetail.
 * @property lastClickLayoutType for handling last click product layout type.
 * @property lastParentPosition for handling last click product and get know parent position for nested recyclerView.
 * @property viewModelProvider the viewModelProvider by Dagger
 * @property adapterFactory the factory for handling type factory Visitor Pattern
 * @property adapter the adapter for recyclerView
 * @property recommendationWidgetViewModel the viewModel for recommendation
 * @property menu the menu of this activity.
 * @property SPAN_COUNT the span count for list.
 * @property SHARE_PRODUCT_TITLE the const value for sharing title.
 * @property SAVED_PRODUCT_ID the const value for handling save productId at SaveInstance.
 * @property WIHSLIST_STATUS_IS_WISHLIST the const value for get extras `isWhislist` from ActivityFromResult ProductDetailActivity.
 * @property PDP_EXTRA_PRODUCT_ID the const value for get extras `product_id` from ActivityFromResult ProductDetailActivity.
 * @property PDP_EXTRA_UPDATED_POSITION the const value for get extras index item from ActivityFromResult ProductDetailActivity.
 * @property REQUEST_FROM_PDP the const value for set request calling startActivityForResult ProductDetailActivity.
 * @constructor Creates an empty recommendation.
 */
class RecommendationFragment: BaseListFragment<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(), TrackingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var productId: String
    private var lastClickLayoutType: String? = null
    private var lastParentPosition: Int? = null
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl() }
    private val adapter by lazy { HomeRecommendationAdapter(adapterTypeFactory) }
    private val recommendationWidgetViewModel by lazy { viewModelProvider.get(RecommendationPageViewModel::class.java) }
    private var menu: Menu? = null

    companion object{
        private const val RECOMMENDATION_APP_LINK = "https://tokopedia.com/rekomendasi/%s"
        private const val SPAN_COUNT = 2
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394
        fun newInstance(productId: String = "") = RecommendationFragment().apply {
            this.productId = productId
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendation, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clearProductInfoView()
        savedInstanceState?.let{
            productId = it.getString(SAVED_PRODUCT_ID) ?: ""
        }
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_PRODUCT_ID, productId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        disableLoadMore()
        getRecyclerView(view).layoutManager = recyclerViewLayoutManager
        recommendationWidgetViewModel.productInfoDataModel.observe(this, Observer {
            it?.let {
                primaryProduct ->
                displayProductInfo(primaryProduct)

                menu?.findItem(R.id.action_share)?.isVisible = true
                menu?.findItem(R.id.action_share)?.setOnMenuItemClickListener {
                    shareProduct(primaryProduct.productDetailData)
                    true
                }
            }
        })

        recommendationWidgetViewModel.recommendationListModel.observe(this, Observer {
            it?.let { recommendationList ->
                clearAllData()
                renderList(mapDataModel(recommendationList))
            }
        })
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(id.toInt(), wishlistStatusFromPdp, position)
            }
            lastClickLayoutType = null
            lastParentPosition = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.recommendation_page_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
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

    override fun onItemClicked(dataModel: HomeRecommendationDataModel) {
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        if(page == defaultInitialPage){
            //load initial data when press retry
            loadData()
        }
    }

    override fun disableLoadMore() {
        super.disableLoadMore()
        getRecyclerView(view).isNestedScrollingEnabled = false
    }


    /**
     * This void from Callback [TrackingListener]
     * It handling wishlist click from item
     * @param item the item clicked
     * @param isAddWishlist the wishlist is selected or not
     * @param callback the callback for notify when success or not, there are have 2 params [Boolean] and [Throwable]
     */
    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            if(isAddWishlist){
                recommendationWidgetViewModel.addWishlist(item, callback)
                RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(true, getHeaderName(item))
            } else {
                recommendationWidgetViewModel.removeWishlist(item, callback)
                RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(false, getHeaderName(item))
            }
        }else{
            RecommendationPageTracking.eventUserClickRecommendationWishlistForNonLogin(getHeaderName(item))
        }
    }

    /**
     * This void from Callback [TrackingListener]
     * It handling product impression item
     * @param item the item clicked
     */
    override fun onProductImpression(item: RecommendationItem) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderNameLogin(trackingQueue, getHeaderName(item), item, item.position.toString())
        } else {
            RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderName(trackingQueue, getHeaderName(item), item, item.position.toString())
        }
    }


    /**
     * This void from Callback [TrackingListener]
     * It handling item click
     * @param item the item clicked
     * @param layoutType the layoutType is type layout where item placed
     * @param position list of position of the item at Adapter, can be [1] or [1,2] for dynamic nested item
     */
    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        eventTrackerClickListener(item)
        lastClickLayoutType = layoutType
        if(position.size > 1){
            lastParentPosition = position[0]
            goToPDP(item, position[1])
        }else {
            goToPDP(item, position[0])
        }
    }

    /**
     * Void [clearProductInfoView]
     * It handling clear productInfo fragment when productInfoFragment is added at view
     */
    private fun clearProductInfoView(){
        if(childFragmentManager.fragments.size > 0 && childFragmentManager.fragments[0] is ProductInfoFragment){
            childFragmentManager.beginTransaction().remove(childFragmentManager.fragments[0]).commit()
        }
    }

    /**
     * Void [loadData]
     * It handling trigger load primaryProduct and recommendationList from viewModel
     */
    private fun loadData(){
        activity?.let{
            if(productId.isNotBlank()) {
                recommendationWidgetViewModel.getPrimaryProduct(productId)
                recommendationWidgetViewModel.getRecommendationList(arrayListOf(productId),
                        onErrorGetRecommendation = this::onErrorGetRecommendation)
            } else {
                recommendationWidgetViewModel.getRecommendationList(arrayListOf(), onErrorGetRecommendation = this::onErrorGetRecommendation)
            }
        }
    }

    /**
     * Void [onErrorGetRecommendation]
     * It handling show error when failed fetch Recommendation List from viewModel
     */
    private fun onErrorGetRecommendation(errorMessage: String?) {
        showGetListError(Throwable(errorMessage))
    }

    /**
     * Void [displayProductInfo]
     * It handling show productInfo fragment into container layout
     */
    private fun displayProductInfo(dataModel: ProductInfoDataModel){
        childFragmentManager.beginTransaction()
                .replace(R.id.product_info_container, ProductInfoFragment.newInstance(dataModel))
                .commit()
    }

    /**
     * Function [mapDataModel]
     * It handling mapper pojo into dataModel
     * @param listRecommendationModel list pojo recommendationWidget from API
     * @return list of dataModel
     */
    private fun mapDataModel(listRecommendationModel: List<RecommendationWidget>): List<HomeRecommendationDataModel>{
        val list = ArrayList<HomeRecommendationDataModel>()
        listRecommendationModel.forEach { recommendationWidget ->
            when(recommendationWidget.layoutType){
                TYPE_SCROLL -> {
                    list.add(TitleDataModel(recommendationWidget.title))
                    recommendationWidget.recommendationItemList.forEach {
                        list.add(RecommendationItemDataModel(it, this))
                    }
                }
                TYPE_CAROUSEL, TYPE_CUSTOM_HORIZONTAL -> list.add(
                        RecommendationCarouselDataModel(
                                recommendationWidget.title,
                                recommendationWidget.recommendationItemList.asSequence().map { RecommendationCarouselItemDataModel(it, list.size, this) }.toList(),
                                this
                        )
                )
            }
        }
        return list
    }

    /**
     * Void [eventTrackerClickListener]
     * It handling tracker event from click Product
     * @param item the recommendation item product
     */
    private fun eventTrackerClickListener(item: RecommendationItem){
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventUserClickOnHeaderNameProduct(getHeaderName(item), item, item.position.toString())
        }else{
            RecommendationPageTracking.eventUserClickOnHeaderNameProductNonLogin(getHeaderName(item), item, item.position.toString())
        }
    }

    /**
     * Void [goToPDP]
     * It handling routing to PDP
     * @param item the recommendation item
     * @param position the position of the item at adapter
     */
    private fun goToPDP(item: RecommendationItem, position: Int){
        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    /**
     * Function [getHeaderName]
     * It handling routing to PDP
     * @param item the recommendation item
     * @return header name
     */
    private fun getHeaderName(item: RecommendationItem): String{
        if(item.header.isNotBlank()) return item.header
        return ""
    }

    /**
     * Void [shareProduct]
     * It handling show share intent
     * @param productDetailData the primary product pojo
     */
    private fun shareProduct(productDetailData: ProductDetailData){
        context?.let{ context ->
            RecommendationPageTracking.eventClickIconShare()
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                    productDataToLinkerDataMapper(productDetailData), object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult) {
                    openIntentShare(productDetailData.name, context.getString(R.string.recom_home_recommendation), linkerShareData.url)
                }

                override fun onError(linkerError: LinkerError) {
                    openIntentShare(productDetailData.name, context.getString(R.string.recom_home_recommendation), String.format(RECOMMENDATION_APP_LINK, "${productDetailData.id}"))
                }
            }))
        }
    }

    /**
     * Function [productDataToLinkerDataMapper]
     * It handling routing to PDP
     * @param productDetailData the primary product pojo
     * @return LinkerShareData for the requirement share intent
     */
    private fun productDataToLinkerDataMapper(productDetailData: ProductDetailData): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = productDetailData.id.toString()
        linkerData.name = productDetailData.name
        linkerData.description = productDetailData.name
        linkerData.imgUri = productDetailData.imageUrl
        linkerData.ogUrl = null
        linkerData.type = "Recommendation"
        linkerData.uri =  "https://m.tokopedia.com/rekomendasi/${productDetailData.id}"
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    /**
     * Void [openIntentShare]
     * It handling show intent share
     * @param title the title of intent share
     * @param shareContent the content of intent share
     * @param shareUri the uri of intent share
     */
    private fun openIntentShare(title: String, shareContent: String, shareUri: String){
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                putExtra(Intent.EXTRA_REFERRER, shareUri)
            }
            putExtra(Intent.EXTRA_HTML_TEXT, "$shareContent $shareUri")
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, "$shareContent $shareUri")
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        activity?.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
    }

    /**
     * Void [updateWishlist]
     * It handling show intent share
     * @param id the product id
     * @param isWishlist the state wishlist or not wishlist
     * @param position the position of item at adapter
     */
    private fun updateWishlist(id: Int, isWishlist: Boolean, position: Int){
        if(position > -1 && adapter.data != null && adapter.dataSize > position) {
            if(lastClickLayoutType != null){
                when(lastClickLayoutType){
                    TYPE_SCROLL -> {
                        if(adapter.data[position] is RecommendationItemDataModel){
                            (adapter.data[position] as RecommendationItemDataModel).productItem.isWishlist = isWishlist
                            adapter.notifyItemChanged(position)
                        }
                    }
                    TYPE_CAROUSEL, TYPE_CUSTOM_HORIZONTAL -> {
                        if(lastParentPosition != null && adapter.data[lastParentPosition!!] is RecommendationCarouselDataModel){
                            (getRecyclerView(view).findViewHolderForAdapterPosition(lastParentPosition!!) as RecommendationCarouselViewHolder)
                                    .updateWishlist(position, isWishlist)
                        }else {
                            adapter.data.withIndex().find{(_, item) ->
                                item is RecommendationCarouselDataModel && item.contains(id)}?.let { (index, _) ->
                                (getRecyclerView(view).findViewHolderForAdapterPosition(index) as RecommendationCarouselViewHolder)
                                        .updateWishlist(position, isWishlist)
                            }
                        }
                    }
                }
            }
        }
    }

}