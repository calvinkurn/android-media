package com.tokopedia.home_recom.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.model.datamodel.TitleDataModel
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.recommendation_widget_common.TYPE_CAROUSEL
import com.tokopedia.recommendation_widget_common.TYPE_SCROLL
import com.tokopedia.recommendation_widget_common.TYPE_CUSTOM_HORIZONTAL
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class RecommendationFragment: BaseListFragment<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(), RecommendationCardView.TrackingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var trackingQueue: TrackingQueue
    private lateinit var productId: String
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl() }
    private val adapter by lazy { HomeRecommendationAdapter(adapterTypeFactory) }
    private val recommendationWidgetViewModel by lazy { viewModelProvider.get(RecommendationPageViewModel::class.java) }
    private var menu: Menu? = null
    private val SPAN_COUNT = 2
    private val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
    private val SAVED_PRODUCT_ID = "saved_product_id"

    companion object{
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

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    private fun clearProductInfoView(){
        if(childFragmentManager.fragments.size > 0 && childFragmentManager.fragments[0] is ProductInfoFragment){
            childFragmentManager.beginTransaction().remove(childFragmentManager.fragments[0]).commit()
        }
    }

    private fun loadData(){
        activity?.let{
            if(productId.isNotBlank()) {
                recommendationWidgetViewModel.getPrimaryProduct(productId, it)
                recommendationWidgetViewModel.getRecommendationList(arrayListOf(productId),
                        onErrorGetRecommendation = this::onErrorGetRecommendation)
            } else {
                recommendationWidgetViewModel.getRecommendationList(arrayListOf(), onErrorGetRecommendation = this::onErrorGetRecommendation)
            }
        }
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
        if(page == defaultInitialPage){
            //load initial data when press retry
            loadData()
        }
    }

    override fun disableLoadMore() {
        super.disableLoadMore()
        getRecyclerView(view).isNestedScrollingEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.recommendation_page_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
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
                TYPE_CUSTOM_HORIZONTAL -> list.add(RecommendationCarouselDataModel(recommendationWidget.title, recommendationWidget.recommendationItemList, this))
            }
        }
        return list
    }

    override fun onImpressionTopAds(item: RecommendationItem) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderNameLogin(trackingQueue, getHeaderName(item), item, item.position.toString())
        } else {
            RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderName(trackingQueue, getHeaderName(item), item, item.position.toString())
        }
    }

    override fun onImpressionOrganic(item: RecommendationItem) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderNameLogin(trackingQueue, getHeaderName(item), item, item.position.toString())
        } else {
            RecommendationPageTracking.eventImpressionProductRecommendationOnHeaderName(trackingQueue, getHeaderName(item), item, item.position.toString())
        }
    }

    override fun onClickTopAds(item: RecommendationItem) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventUserClickOnHeaderNameProduct(getHeaderName(item), item, item.position.toString())
        }else{
            RecommendationPageTracking.eventUserClickOnHeaderNameProductNonLogin(getHeaderName(item), item, item.position.toString())
        }
    }

    override fun onClickOrganic(item: RecommendationItem) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventUserClickOnHeaderNameProduct(getHeaderName(item), item, item.position.toString())
        }else{
            RecommendationPageTracking.eventUserClickOnHeaderNameProductNonLogin(getHeaderName(item), item, item.position.toString())
        }
    }

    fun onClickAddWishlist(item: RecommendationItem) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventUserClickProductToWishlistForUserLogin(true)
        }else{
            RecommendationPageTracking.eventUserClickProductToWishlistForNonLogin()
        }
    }

    fun onClickRemoveWishlist(item: RecommendationItem) {
        if(recommendationWidgetViewModel.isLoggedIn()){
            RecommendationPageTracking.eventUserClickProductToWishlistForUserLogin(false)
        }else{
            RecommendationPageTracking.eventUserClickProductToWishlistForNonLogin()
        }
    }

    private fun getHeaderName(item: RecommendationItem): String{
        var header = ""
        val data = adapter.data
        val itemFoundAtStaggeredGridLayoutManager = data.any { it is RecommendationItemDataModel && it.productItem.productId == item.productId }
        if(itemFoundAtStaggeredGridLayoutManager){
            data.find { it is TitleDataModel }?.let {
                header = (it as TitleDataModel).title
            }
        }else{
            data.find { it is RecommendationCarouselDataModel
                    && it.products.any { product -> product.productId == item.productId }}?.let {
                header = (it as RecommendationCarouselDataModel).title
            }
        }
        return header
    }

    private fun shareProduct(productDetailData: ProductDetailData){
        context?.let{ context ->
            RecommendationPageTracking.eventClickIconShare()
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                    productDataToLinkerDataMapper(productDetailData), object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult) {
                    openIntentShare(productDetailData.name, context.getString(R.string.home_recommendation), linkerShareData.url)
                }

                override fun onError(linkerError: LinkerError) {
                    openIntentShare(productDetailData.name, context.getString(R.string.home_recommendation), "https://tokopedia.com/rekomendasi/${productDetailData.id}")
                }
            }))
        }
    }

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

    private fun openIntentShare(title: String, shareContent: String, shareUri: String){

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_REFERRER, shareUri)
            putExtra(Intent.EXTRA_HTML_TEXT, "$shareContent $shareUri")
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, "$shareContent $shareUri")
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        activity?.startActivity(Intent.createChooser(shareIntent, SHARE_PRODUCT_TITLE))
    }

}