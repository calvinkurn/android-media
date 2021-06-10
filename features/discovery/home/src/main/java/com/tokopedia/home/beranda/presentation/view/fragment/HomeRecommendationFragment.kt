package com.tokopedia.home.beranda.presentation.view.fragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationAddWishlistLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationAddWishlistNonLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickNonLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductClickNonLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewNonLogin
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationProductViewNonLoginTopAds
import com.tokopedia.home.analytics.v2.HomeRecommendationTracking.getRecommendationRemoveWishlistLogin
import com.tokopedia.home.beranda.di.BerandaComponent
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.helper.HomeFeedEndlessScrollListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeEggListener
import com.tokopedia.home.beranda.listener.HomeTabFeedListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeFeedItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationItemViewHolder.Companion.LAYOUT
import com.tokopedia.home.beranda.presentation.viewModel.HomeRecommendationViewModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

@SuppressLint("SyntheticAccessor")
@SuppressWarnings("unused")
open class HomeRecommendationFragment : Fragment(), HomeRecommendationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: SmartExecutors
    @Inject
    lateinit var trackingQueue: TrackingQueue
    @Inject
    lateinit var userSessionInterface: UserSessionInterface


    private lateinit var viewModel: HomeRecommendationViewModel
    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl() }
    private val adapter by lazy { HomeRecommendationAdapter(appExecutors, adapterFactory, this) }
    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.home_feed_fragment_recycler_view) }

    private val staggeredGridLayoutManager by lazy { StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL) }
    private var endlessRecyclerViewScrollListener: HomeFeedEndlessScrollListener? = null

    private var totalScrollY = 0
    private var tabIndex = 0
    private var recomId = 0
    private var tabName: String = ""
    private var hasLoadData = false
    private var homeEggListener: HomeEggListener? = null
    private var homeTabFeedListener: HomeTabFeedListener? = null
    private var parentPool: RecyclerView.RecycledViewPool? = null
    private var homeCategoryListener: HomeCategoryListener? = null
    private var component: BerandaComponent? = null
    private var pmProCoachmarkIsShowing = false
    private var coachmarkLocalCache: CoachMarkLocalCache? = null
    private var pmProCoachmark: CoachMark2? = null
    private var pmProCoachmarkItem: ArrayList<CoachMark2Item> = arrayListOf()
    private var pmProProductPosition: Int = -1
    private var remoteConfigInstance: RemoteConfigInstance? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_home_feed_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initViewModel()
        viewModel.topAdsBannerNextPageToken = homeCategoryListener?.getTopAdsBannerNextPageToken()?:""
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.coachmarkLocalCache = CoachMarkLocalCache(context = context)
        activity?.let {
            this.remoteConfigInstance = RemoteConfigInstance(it.application)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP && data != null && data.hasExtra(WIHSLIST_STATUS_IS_WISHLIST)) {
            val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
            val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST, false)
            val position = data.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1)
            updateWishlist(id, wishlistStatusFromPdp, position)
        }
        handleProductCardOptionsActivityResult(requestCode, resultCode, data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabIndex = arguments?.getInt(ARG_TAB_INDEX) ?: -1
        recomId = arguments?.getInt(ARG_RECOM_ID) ?: -1
        tabName = arguments?.getString(ARG_TAB_NAME) ?: ""
        setupRecyclerView()
        loadFirstPageData()
        initListeners()
        observeLiveData()
    }

    override fun onPause() {
        TopAdsGtmTracker.getInstance().eventRecomendationProductView(trackingQueue,
                tabName.toLowerCase(Locale.ROOT), userSessionInterface.isLoggedIn)
        trackingQueue.sendAll()
        super.onPause()
    }

    private fun initInjector() {
        if (activity != null) {
            if (component == null) {
                component = initBuilderComponent().build()
            }
            component?.inject(this)
        }
    }

    open fun initBuilderComponent(): DaggerBerandaComponent.Builder {
        return DaggerBerandaComponent.builder().baseAppComponent((requireActivity().application as BaseMainApplication).baseAppComponent)
    }

    @VisibleForTesting
    open fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeRecommendationViewModel::class.java)
    }

    private fun observeLiveData(){
        viewModel.homeRecommendationLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data ->
            adapter.submitList(data.homeRecommendations)
        })

        viewModel.homeRecommendationNetworkLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {result ->
            if(result.isFailure){
                view?.let {
                    if(adapter.itemCount > 1){
                        Toaster.make(it, getString(R.string.home_error_connection), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.title_try_again), View.OnClickListener {
                            endlessRecyclerViewScrollListener?.loadMoreNextPage()
                        })
                    }
                }
            } else {
                updateScrollEndlessListener(result.getOrNull()?.isHasNextPage ?: false)
            }
        })
    }

    private fun updateScrollEndlessListener(hasNextPage: Boolean){
        // load next page data if adapter data less than minimum scrollable data
        // when the list has next page and auto load next page is enabled
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun setupRecyclerView() {
        recyclerView?.layoutManager = staggeredGridLayoutManager
        (recyclerView?.layoutManager as StaggeredGridLayoutManager?)?.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView?.addItemDecoration(HomeFeedItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_4)))
        recyclerView?.adapter = adapter
        parentPool?.setMaxRecycledViews(
                LAYOUT,
                20
        )
        recyclerView?.setRecycledViewPool(parentPool)
        createEndlessRecyclerViewListener()
        endlessRecyclerViewScrollListener?.let { recyclerView?.addOnScrollListener(it) }
    }

    fun setListener(homeCategoryListener: HomeCategoryListener?,
                    homeEggListener: HomeEggListener?,
                    homeTabFeedListener: HomeTabFeedListener?) {
        this.homeCategoryListener = homeCategoryListener
        this.homeEggListener = homeEggListener
        this.homeTabFeedListener = homeTabFeedListener
    }

    fun setParentPool(parentPool: RecyclerView.RecycledViewPool?) {
        this.parentPool = parentPool
    }

    private fun createEndlessRecyclerViewListener(){
        endlessRecyclerViewScrollListener = object : HomeFeedEndlessScrollListener(recyclerView?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.loadNextData(tabName, recomId, DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE, page, getLocationParamString())
            }
        }
    }

    override fun onProductImpression(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int) {
        if (homeRecommendationItemDataModel.product.isTopads) {
            TopAdsUrlHitter(className).hitImpressionUrl(context, homeRecommendationItemDataModel.product.trackerImageUrl,
                    homeRecommendationItemDataModel.product.id,
                    homeRecommendationItemDataModel.product.name,
                    homeRecommendationItemDataModel.product.imageUrl,
                    HOME_RECOMMENDATION_FRAGMENT)
            if (userSessionInterface.isLoggedIn) {
                trackingQueue.putEETracking(getRecommendationProductViewLoginTopAds(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ) as HashMap<String, Any>)
            } else {
                trackingQueue.putEETracking(getRecommendationProductViewNonLoginTopAds(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ) as HashMap<String, Any>)
            }
        } else {
            if (userSessionInterface.isLoggedIn) {
                trackingQueue.putEETracking(getRecommendationProductViewLogin(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ) as HashMap<String, Any>)
            } else {
                trackingQueue.putEETracking(getRecommendationProductViewNonLogin(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ) as HashMap<String, Any>)
            }
        }
    }

    override fun onProductClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int) {
        if (homeRecommendationItemDataModel.product.isTopads){
            TopAdsUrlHitter(className).hitClickUrl(context,
                    homeRecommendationItemDataModel.product.clickUrl,
                    homeRecommendationItemDataModel.product.id,
                    homeRecommendationItemDataModel.product.name,
                    homeRecommendationItemDataModel.product.imageUrl,
                    HOME_RECOMMENDATION_FRAGMENT)
            if (userSessionInterface.isLoggedIn) {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(getRecommendationProductClickLoginTopAds(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ))
            } else {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(getRecommendationProductClickNonLoginTopAds(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ))
            }
        }else {
            if (userSessionInterface.isLoggedIn) {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(getRecommendationProductClickLogin(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ))
            } else {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(getRecommendationProductClickNonLogin(
                        tabName.toLowerCase(Locale.ROOT),
                        homeRecommendationItemDataModel
                ))
            }
        }
        goToProductDetail(homeRecommendationItemDataModel.product.id, position)
    }

    override fun onProductThreeDotsClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int) {
        showProductCardOptions(
                this,
                createProductCardOptionsModel(homeRecommendationItemDataModel, position)
        )
    }

    override fun onBannerImpression(bannerRecommendationDataModel: BannerRecommendationDataModel) {
        trackingQueue.putEETracking(HomeRecommendationTracking.getBannerRecommendation(bannerRecommendationDataModel) as HashMap<String, Any>)
    }

    override fun onBannerTopAdsClick(homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(HomeRecommendationTracking.getClickBannerTopAds(homeTopAdsRecommendationBannerDataModelDataModel, tabIndex, position))
        RouteManager.route(context, homeTopAdsRecommendationBannerDataModelDataModel.topAdsImageViewModel?.applink)
    }

    override fun onBannerTopAdsImpress(homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel, position: Int) {
        trackingQueue.putEETracking(HomeRecommendationTracking.getImpressionBannerTopAds(homeTopAdsRecommendationBannerDataModelDataModel, tabIndex, position) as HashMap<String, Any>)
    }

    override fun onRetryGetProductRecommendationData() {
        viewModel.loadInitialPage(tabName, recomId, DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE, getLocationParamString())
    }

    override fun onProductWithPmProImpressed(pmProView: View?, position: Int) {
        if (shouldShowPmProCoachmark()) {
            pmProView?.let {
                setupPMProCoachmark(pmProView)
                this.pmProProductPosition = position
            }
        }
    }

    private fun shouldShowPmProCoachmark(): Boolean {
        val pmProAbTestValue =
                try {
                    remoteConfigInstance?.abTestPlatform?.getString(
                            AbTestPlatform.POWER_MERCHANT_PRO_POP_UP)
                } catch (e: Exception) {
                    false
                }

        val isPmProRollenceActive =
                pmProAbTestValue == AbTestPlatform.POWER_MERCHANT_PRO_POP_UP

        return if (pmProCoachmark == null && isPmProRollenceActive) {
            coachmarkLocalCache?.shouldShowHomePMProCoachMark()?: false
        } else {
            false
        }
    }

    private fun initListeners() {
        if (view == null) return
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                totalScrollY += dy
                if (!userVisibleHint) {
                    return
                }
                homeEggListener?.hideEggOnScroll()
                if (homeTabFeedListener != null) {
                    homeTabFeedListener?.onFeedContentScrolled(dy, totalScrollY)
                }

                val viewsIds: IntArray = staggeredGridLayoutManager.findFirstVisibleItemPositions(null)
                if (pmProProductPosition == viewsIds.getOrNull(0)?:-1 || pmProProductPosition == viewsIds.getOrNull(1)?:-1) {
                    showPmProCoachmark()
                } else {
                    pmProCoachmark?.dismissCoachMark()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!userVisibleHint) {
                    return
                }
                if (homeTabFeedListener != null) {
                    homeTabFeedListener?.onFeedContentScrollStateChanged(newState)
                }
            }
        })
    }

    @VisibleForTesting
    open fun goToProductDetail(productId: String, position: Int) {
        if (activity != null) {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
            intent.putExtra(WISHLIST_STATUS_UPDATED_POSITION, position)
            try {
                startActivityForResult(intent, REQUEST_FROM_PDP)
            } catch (exception: ActivityNotFoundException) {
                exception.printStackTrace()
            }
        }
    }

    private fun updateWishlist(id: String, isWishlist: Boolean, position: Int) {
        if(position > -1 && adapter.itemCount > 0 &&
                adapter.itemCount > position) {
            viewModel.updateWishlist(id, position, isWishlist)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        loadFirstPageData()
    }

    private fun loadFirstPageData() {
        if (userVisibleHint && isAdded && activity != null && !hasLoadData) {
            hasLoadData = true
            viewModel.loadInitialPage(tabName, recomId, DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE, getLocationParamString())
        }
    }

    private fun isValidToShowCoachMark(): Boolean {
        activity?.let {
            return !it.isFinishing
        }
        return false
    }

    private fun setupPMProCoachmark(pmProBadgeView: View) {
        context?.let {
            pmProCoachmarkItem = arrayListOf(
                    CoachMark2Item(
                            title = getString(R.string.home_pmpro_coachmark_title),
                            description = getString(R.string.home_pmpro_coachmark_description),
                            anchorView = pmProBadgeView
                    )
            )
            pmProCoachmark = CoachMark2(requireContext())
            showPmProCoachmark()
        }
    }

    private fun showPmProCoachmark() {
        pmProCoachmark.let {
            //error comes from unify library, hence for quick fix we just catch the error since its not blocking any feature
            //will be removed along the coachmark removal in the future
            try {
                if (pmProCoachmarkItem.isNotEmpty() && isValidToShowCoachMark()) {
                    if (!pmProCoachmarkIsShowing) {
                        pmProCoachmark?.showCoachMark(step = pmProCoachmarkItem)
                        pmProCoachmarkIsShowing = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hidePmProCoachmark() {
        pmProCoachmark?.hideCoachMark()
        pmProCoachmarkIsShowing = false
    }

    fun scrollToTop() {
        if (view == null) {
            return
        }
        val staggeredGridLayoutManager = recyclerView?.layoutManager as StaggeredGridLayoutManager?
        if (staggeredGridLayoutManager != null && staggeredGridLayoutManager.findFirstVisibleItemPositions(null)[0] > 10) {
            recyclerView?.scrollToPosition(10)
        }
        recyclerView?.smoothScrollToPosition(0)
    }


    private fun createProductCardOptionsModel(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = homeRecommendationItemDataModel.product.isWishlist
        productCardOptionsModel.productId = homeRecommendationItemDataModel.product.id
        productCardOptionsModel.isTopAds = homeRecommendationItemDataModel.product.isTopads
        productCardOptionsModel.topAdsWishlistUrl = homeRecommendationItemDataModel.product.wishlistUrl
        productCardOptionsModel.productPosition = position
        return productCardOptionsModel
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return
        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                if (wishlistResult.isAddWishlist) {
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(getRecommendationAddWishlistLogin(productCardOptionsModel.productId, tabName))
                    showMessageSuccessAddWishlist()
                } else {
                    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(getRecommendationRemoveWishlistLogin(productCardOptionsModel.productId, tabName))
                    showMessageSuccessRemoveWishlist()
                }
                updateWishlist(productCardOptionsModel.productId, wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
            } else {
                showMessageFailedWishlistAction()
            }
        } else {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(getRecommendationAddWishlistNonLogin(productCardOptionsModel.productId, tabName))
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    private fun showMessageSuccessAddWishlist() {
        if (activity == null) return
        val view = requireActivity().findViewById<View>(android.R.id.content)
        val message = getString(R.string.msg_success_add_wishlist)
        view?.let {
            Toaster.make(
                    it,
                    message,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.go_to_wishlist),
                    View.OnClickListener { goToWishlist() })
        }
    }

    private fun goToWishlist() {
        if (activity == null) return
        RouteManager.route(activity, ApplinkConst.NEW_WISHLIST)
    }

    private fun showMessageSuccessRemoveWishlist() {
        if (activity == null) return
        val view = requireActivity().findViewById<View>(android.R.id.content)
        val message = getString(R.string.msg_success_remove_wishlist)
        Toaster.make(view, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL)
    }

    private fun showMessageFailedWishlistAction() {
        if (activity == null) return
        val view = activity?.findViewById<View>(android.R.id.content)
        view?.let { Toaster.make(it, ErrorHandler.getErrorMessage(activity, null), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR) }
    }

    fun smoothScrollRecyclerViewByVelocity(distance: Int) {
        recyclerView?.smoothScrollBy(0, distance)
    }

    companion object {
        private const val className = "com.tokopedia.home.beranda.presentation.view.fragment.HomeRecommendationFragment"
        private const val HOME_RECOMMENDATION_FRAGMENT = "home_recommendation_fragment"
        const val ARG_TAB_INDEX = "ARG_TAB_INDEX"
        const val ARG_RECOM_ID = "ARG_RECOM_ID"
        const val ARG_TAB_NAME = "ARG_TAB_NAME"
        const val DEFAULT_TOTAL_ITEM_HOME_RECOM_PER_PAGE = 12

        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val HOME_JANKY_FRAMES_MONITORING_PAGE_NAME = "home"
        private const val HOME_JANKY_FRAMES_MONITORING_SUB_PAGE_NAME = "feed"
        private const val REQUEST_FROM_PDP = 349
        private const val DEFAULT_SPAN_COUNT = 2

        fun newInstance(tabIndex: Int, recomId: Int, tabName: String): HomeRecommendationFragment {
            val homeFeedFragment = HomeRecommendationFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_TAB_INDEX, tabIndex)
            bundle.putInt(ARG_RECOM_ID, recomId)
            bundle.putString(ARG_TAB_NAME, tabName)
            homeFeedFragment.arguments = bundle
            return homeFeedFragment
        }
    }

    private fun getLocationParamString() : String {
        return ChooseAddressUtils.getLocalizingAddressData(requireContext())?.convertToLocationParams() ?: ""
    }
}
