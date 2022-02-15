package com.tokopedia.home_wishlist.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.analytics.WishlistTracking
import com.tokopedia.home_wishlist.common.EndlessRecyclerViewScrollListener
import com.tokopedia.home_wishlist.common.ToolbarElevationOffsetListener
import com.tokopedia.home_wishlist.di.DaggerWishlistComponent
import com.tokopedia.home_wishlist.model.action.*
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.util.GravitySnapHelper
import com.tokopedia.home_wishlist.view.adapter.WishlistAdapter
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactoryImpl
import com.tokopedia.home_wishlist.view.custom.CountDrawable
import com.tokopedia.home_wishlist.view.custom.CustomAppBarLayoutBehavior
import com.tokopedia.home_wishlist.view.custom.CustomSearchView
import com.tokopedia.home_wishlist.view.custom.SpaceBottomItemDecoration
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.PDP_EXTRA_PRODUCT_ID
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.PDP_EXTRA_UPDATED_POSITION
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.REQUEST_FROM_PDP
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.SAVED_PRODUCT_ID
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.SHARE_PRODUCT_TITLE
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.SPAN_COUNT
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.WIHSLIST_STATUS_IS_WISHLIST
import com.tokopedia.home_wishlist.view.listener.TopAdsListener
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.wishlist.common.request.WishlistAdditionalParamRequest
import com.tokopedia.wishlist.common.toEmptyStringIfZero
import kotlinx.android.synthetic.main.fragment_new_home_wishlist.*
import javax.inject.Inject


/**
 * A Class of Recommendation Fragment.
 *
 * This class for handling Recommendation Page, it will shown List of ProductDetail, Recommendation Items, Recommendation Carousel
 *
 * @property viewModelFactory the factory for ViewModel provide by Dagger.
 * @property trackingQueue the queue util for handle tracking.
 * @property viewModelProvider the viewModelProvider by Dagger
 * @property adapterFactory the factory for handling type factory Visitor Pattern
 * @property adapter the adapter for recyclerView
 * @property SPAN_COUNT the span count for list.
 * @property SHARE_PRODUCT_TITLE the const value for sharing title.
 * @property SAVED_PRODUCT_ID the const value for handling save productId at SaveInstance.
 * @property WIHSLIST_STATUS_IS_WISHLIST the const value for get extras `isWhislist` from ActivityFromResult ProductDetailActivity.
 * @property PDP_EXTRA_PRODUCT_ID the const value for get extras `product_id` from ActivityFromResult ProductDetailActivity.
 * @property PDP_EXTRA_UPDATED_POSITION the const value for get extras index item from ActivityFromResult ProductDetailActivity.
 * @property REQUEST_FROM_PDP the const value for set request calling startActivityForResult ProductDetailActivity.
 * @constructor Creates an empty recommendation.
 */
@SuppressLint("SyntheticAccessor")
open class WishlistFragment : BaseDaggerFragment(), WishlistListener, TopAdsListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: SmartExecutors

    private lateinit var cartLocalCacheHandler: LocalCacheHandler
    private lateinit var trackingQueue: TrackingQueue
    private lateinit var remoteConfigInstance: RemoteConfigInstance
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfigImpl

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    internal val viewModel by lazy { viewModelProvider.get(WishlistViewModel::class.java) }
    private val adapterFactory by lazy { WishlistTypeFactoryImpl(appExecutors) }
    private val adapter by lazy { WishlistAdapter(appExecutors, adapterFactory, this) }
    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.recycler_view) }
    private val swipeToRefresh by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val containerDelete by lazy { view?.findViewById<FrameLayout>(R.id.container_delete) }
    private val deleteButton by lazy { view?.findViewById<UnifyButton>(R.id.delete_button) }
    private val navToolbar by lazy { view?.findViewById<NavToolbar>(R.id.navToolbar) }
    private val appBarLayout by lazy { view?.findViewById<AppBarLayout>(R.id.app_bar_layout) }
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var coachMark: CoachMark2? = null
    private val staggeredGridLayoutManager by lazy { StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) }
    private val itemDecorationBottom by lazy { SpaceBottomItemDecoration() }
    private lateinit var toolbarElevation: ToolbarElevationOffsetListener
    private val dialogUnify by lazy { DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
    private val searchView by lazy { view?.findViewById<CustomSearchView>(R.id.wishlist_search_view) }

    private var additionalParamRequest: WishlistAdditionalParamRequest? = null
    private var launchSourceWishlist: String = ""
    private var scrollAfterSubmit = false

    companion object {
        private const val SPAN_COUNT = 2
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        const val COACH_MARK_TAG = "wishlist"
        private const val REQUEST_FROM_PDP = 394
        private const val className = "com.tokopedia.home_wishlist.view.fragment.WishlistFragment"
        private const val CACHE_CART = "CART"
        private const val CACHE_KEY_IS_HAS_CART = "IS_HAS_CART"
        private const val CACHE_KEY_TOTAL_CART = "CACHE_TOTAL_CART"
        private const val DELAY_TEXT_CHANGED = 250L
        private const val DELAY_MILIS_100 = 100L
        private const val COACHMARK_SAFE_DELAY = 100L
        const val PARAM_LAUNCH_WISHLIST = "launch_source_wishlist"
        const val PARAM_HOME = "home"
        @JvmStatic
        fun newInstance(bundle: Bundle = Bundle()): WishlistFragment {
            return WishlistFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_home_wishlist, container, false)
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfigImpl? {
        if (!::firebaseRemoteConfig.isInitialized) {
            context?.let {
                firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
                return firebaseRemoteConfig
            }
            return null
        } else {
            return firebaseRemoteConfig
        }
    }

    private fun isAutoRefreshEnabled(): Boolean {
        return try {
            return getFirebaseRemoteConfig()?.getBoolean(RemoteConfigKey.HOME_ENABLE_AUTO_REFRESH_UOH)?:false
        } catch (e: Exception) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID) ?: ""
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                viewModel.onPDPActivityResultForWishlist(
                        id.toLongOrZero(),
                        wishlistStatusFromPdp
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchSourceWishlist = arguments?.getString(PARAM_LAUNCH_WISHLIST, "") ?: ""

        initView()
        initCartLocalCacheHandler()
        hideSearchView()
        observeData()
        viewModel.getWishlistData(additionalParams = generateWishlistAdditionalParamRequest(), shouldShowInitialPage = true)
        WishlistTracking.openWishlistPage(viewModel.getUserId())
        showOnBoarding()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        launchAutoRefresh(isVisibleToUser)
    }

    private fun launchAutoRefresh(isVisibleToUser: Boolean = true) {
        if (isVisibleToUser && isAutoRefreshEnabled()) {
            viewModel.getWishlistData(
                navToolbar?.getCurrentSearchbarText() ?: "",
                generateWishlistAdditionalParamRequest()
            )
            scrollAfterSubmit = true
        } else if (coachmarkIsAvailable()) {
            coachMark?.dismissCoachMark()
        }
    }

    private fun initCartLocalCacheHandler() {
        activity?.let {
            cartLocalCacheHandler = LocalCacheHandler(it, CACHE_CART)
        }
    }

    private fun coachmarkIsAvailable(): Boolean {
        return coachMark != null
    }

    override fun onPause() {
        super.onPause()
        if (this::trackingQueue.isInitialized) trackingQueue.sendAll()
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        DaggerWishlistComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.wishlist_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        context?.let {
            showCartBadge(menu)
        }
    }

    private fun showCartBadge(menu: Menu?) {
        context?.let {
            if (::cartLocalCacheHandler.isInitialized) {
                val drawable = ContextCompat.getDrawable(it, R.drawable.ic_wishlist_cart_menu)
                if (drawable is LayerDrawable) {
                    val cartCount = cartLocalCacheHandler.getInt(CACHE_KEY_TOTAL_CART, 0)
                    val countDrawable = CountDrawable(it)
                    countDrawable.setCount(cartCount.toString())
                    drawable.mutate()
                    drawable.setDrawableByLayerId(R.id.ic_cart_count, countDrawable)
                    menu?.findItem(R.id.action_cart)?.icon = drawable
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        activity?.let {
            return when (item.itemId) {
                R.id.action_cart -> {
                    WishlistTracking.clickCartIcon(userId = viewModel.getUserId())
                    RouteManager.route(it, ApplinkConst.CART)
                    it.finish()
                    true
                }
                android.R.id.home -> {
                    it.finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        initSwipeRefresh()
        initSearchView()
        initDeleteBulkButton()
        initRecyclerView()

        //add divider if necessary
//        appBarLayout?.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.appbar_elevation)

        activity?.let {
            navToolbar?.setupToolbarWithStatusBar(it)
            appBarLayout?.setMargin(0, NavToolbarExt.getFullToolbarHeight(it), 0 , 0)
        }
        navToolbar?.let {
            viewLifecycleOwner.lifecycle.addObserver(it)
            it.setupSearchbar(
                searchbarType = NavToolbar.Companion.SearchBarType.TYPE_EDITABLE,
                hints = listOf(HintData(getString(R.string.hint_wishlist))),
                editorActionCallback = { query ->
                    when {
                        query.isBlank() -> {
                            viewModel.getWishlistData(query ?: "", generateWishlistAdditionalParamRequest())
                        }
                        else -> {
                            viewModel.getWishlistData(query ?: "", generateWishlistAdditionalParamRequest())
                        }
                    }
                    view?.let { context?.let { it1 -> navToolbar?.hideKeyboard() } }
                }
            )
            var pageSource = ""
            if(launchSourceWishlist != PARAM_HOME)
            {
                it.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST
            }
            val icons = IconBuilder(
                IconBuilderFlag(pageSource = pageSource)
            )
            icons.apply {
                addIcon(IconList.ID_MESSAGE) {}
                addIcon(IconList.ID_NOTIFICATION) {}
                addIcon(IconList.ID_CART) {}
                addIcon(IconList.ID_NAV_GLOBAL) {}
            }
            it.setIcon(icons)
        }
    }

    private fun initSwipeRefresh() {
        swipeToRefresh?.setOnRefreshListener {
            updateBottomMargin()
            endlessRecyclerViewScrollListener?.resetState()
            viewModel.getWishlistData(navToolbar?.getCurrentSearchbarText() ?: "", generateWishlistAdditionalParamRequest())
        }
    }

    private fun initSearchView() {
        searchView?.setListener(object : CustomSearchView.Listener {
            override fun onManageDeleteWishlistClicked() {
                manageDeleteWishlist()
            }

            override fun onCancelDeleteWishlistClicked() {
                cancelDeleteWishlist()
            }
        })
    }

    private fun initDeleteBulkButton() {
        val count = viewModel.bulkSelectCountActionData.value?.peekContent() ?: 0
        val title = if (count == 0) getString(R.string.wishlist_delete_zero_text) else String.format(getString(R.string.wishlist_delete_text), count.toString())
        deleteButton?.setOnClickListener {
            dialogUnify.apply {
                setTitle(title)
                setDescription(getString(R.string.wishlist_message_delete_alert))
                setPrimaryCTAText(getString(R.string.wishlist_cancel))
                setSecondaryCTAText(getString(R.string.wishlist_delete))
                setPrimaryCTAClickListener { WishlistTracking.clickCancelDeleteWishlist();dismiss() }
                setSecondaryCTAClickListener {
                    onBulkDelete()
                    dismiss()
                }
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }.show()
        }
    }

    private fun initRecyclerView() {
        recyclerView?.layoutManager = object: StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
            override fun supportsPredictiveItemAnimations() = false

            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        recyclerView?.adapter = adapter
        GravitySnapHelper(Gravity.TOP, true).attachToRecyclerView(recyclerView)
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                navToolbar?.hideKeyboard()
                if (coachmarkIsAvailable()) {
                    coachMark?.dismissCoachMark()
                }
            }
        })
    }

    private fun observeData() {
        observeWishlistData()
        observeBulkModeState()
        observeWishlistState()
        observeAction()
        updateBottomMargin()
    }

    private fun observeWishlistState() {
        viewModel.isWishlistState.observe(viewLifecycleOwner, Observer { state ->
            if (state.isEmpty()) hideSearchView()
            else showSearchView()

            val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
            (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(!state.isEmpty())

            endlessRecyclerViewScrollListener?.let {
                recyclerView?.removeOnScrollListener(it)
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(recyclerView?.layoutManager, 1) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (state.isEmpty()) {
                        updateBottomMargin()
                        viewModel.getRecommendationOnEmptyWishlist(page + 1)
                    } else {
                        if (!state.isDone()) {
                            viewModel.getNextPageWishlistData()
                        }
                    }
                }
            }
            recyclerView?.addOnScrollListener(endlessRecyclerViewScrollListener as EndlessRecyclerViewScrollListener)
        })


    }

    private fun observeWishlistData() {
        viewModel.wishlistLiveData.observe(viewLifecycleOwner, Observer { response ->
            if (response.isNotEmpty())
                renderList(response)
            swipeToRefresh?.isRefreshing = false
        })
        viewModel.wishlistCountLiveData.observe(viewLifecycleOwner, {
            searchView?.setWishlistCount(it)
        })
        viewModel.loadMoreWishlistAction.observe(viewLifecycleOwner, Observer { loadMoreWishlistActionData ->
            updateScrollListenerState(
                    loadMoreWishlistActionData.getContentIfNotHandled()?.hasNextPage ?: false)

        })
    }

    private fun observeBulkModeState() {
        viewModel.isInBulkModeState.observe(viewLifecycleOwner, Observer { isInBulkMode ->
            if (isInBulkMode) {
                updateBottomMargin()

                containerDelete?.show()

                showSearchView()
                val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
                (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(false)
                swipeToRefresh?.isEnabled = false
            } else {
                containerDelete?.hide()
                searchView?.resetLabelState()
                showSearchView()
                val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
                (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(true)
                swipeToRefresh?.isEnabled = true
            }
        })

    }

    private fun observeAction() {
        viewModel.addToCartActionData.observe(viewLifecycleOwner, Observer { handleAddToCartActionData(it.getContentIfNotHandled()) })
        viewModel.updateCartCounterActionData.observe(viewLifecycleOwner, Observer { handleUpdateCartCounterActionData(it.getContentIfNotHandled()) })
        viewModel.removeWishlistActionData.observe(viewLifecycleOwner, Observer { handleRemoveWishlistActionData(it.getContentIfNotHandled()) })
        viewModel.bulkRemoveWishlistActionData.observe(viewLifecycleOwner, Observer { handleBulkRemoveActionData(it.getContentIfNotHandled()) })
        viewModel.bulkSelectCountActionData.observe(viewLifecycleOwner, Observer { updateSelectedDeleteItem(it.peekContent()) })
        viewModel.addWishlistRecommendationActionData.observe(viewLifecycleOwner, Observer { handleAddWishlistRecommendationActionData(it.getContentIfNotHandled()) })
        viewModel.removeWishlistRecommendationActionData.observe(viewLifecycleOwner, Observer { handleRemoveWishlistRecommendationActionData(it.getContentIfNotHandled()) })
        viewModel.productClickActionData.observe(viewLifecycleOwner, Observer { handleProductClick(it.getContentIfNotHandled()) })
    }

    private fun handleProductClick(productClickActionData: ProductClickActionData?) {
        productClickActionData?.let {
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, it.productId.toString()).run {
                putExtra(PDP_EXTRA_UPDATED_POSITION, it.position)
                startActivityForResult(this, REQUEST_FROM_PDP)
            }
        }
    }

    private fun renderList(list: List<WishlistDataModel>) {
        val recyclerViewState = recyclerView?.layoutManager?.onSaveInstanceState()
        adapter.submitList(list)
        if (scrollAfterSubmit) {
            recyclerView?.addOneTimeGlobalLayoutListener {
                recyclerView?.scrollToPosition(0)
            }
            scrollAfterSubmit = false
        }
        recyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun updateScrollListenerState(hasNextPage: Boolean) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun updateScrollFlagForSearchView(isSearch: Boolean) {
        if (isSearch) {
            disableScrollFlagsSearch()
        } else {
            enableScrollFlagsSearch()
        }
    }

    override fun onProductClick(dataModel: WishlistDataModel, parentPosition: Int, position: Int) {
        when (dataModel) {
            is WishlistItemDataModel -> {
                WishlistTracking.productClick(dataModel.productItem, position.toString())
                goToPDP(dataModel.productItem.id, position)
            }
            is RecommendationCarouselItemDataModel -> {
                WishlistTracking.clickRecommendation(dataModel.recommendationItem, position)
                if(dataModel.recommendationItem.isTopAds) {
                    TopAdsUrlHitter(context).hitClickUrl(
                            className,
                            dataModel.recommendationItem.clickUrl,
                            dataModel.recommendationItem.productId.toString(),
                            dataModel.recommendationItem.name,
                            dataModel.recommendationItem.imageUrl
                    )
                }
                viewModel.onProductClick(
                        dataModel.recommendationItem.productId,
                        parentPosition,
                        position
                )
            }
            is RecommendationItemDataModel -> {
                WishlistTracking.clickRecommendation(dataModel.recommendationItem, position)
                if(dataModel.recommendationItem.isTopAds) {
                    TopAdsUrlHitter(context).hitClickUrl(
                            className,
                            dataModel.recommendationItem.clickUrl,
                            dataModel.recommendationItem.productId.toString(),
                            dataModel.recommendationItem.name,
                            dataModel.recommendationItem.imageUrl
                    )
                }
                viewModel.onProductClick(
                        dataModel.recommendationItem.productId,
                        parentPosition,
                        position
                )
            }
        }
    }

    override fun onDeleteClick(dataModel: WishlistDataModel, adapterPosition: Int) {
        dialogUnify.apply {
            setTitle(getString(R.string.wishlist_delete_title))
            setDescription(getString(R.string.wishlist_message_delete_alert))
            setPrimaryCTAText(getString(R.string.wishlist_cancel))
            setSecondaryCTAText(getString(R.string.wishlist_delete))
            setPrimaryCTAClickListener {
                WishlistTracking.clickCancelDeleteWishlist()
                dismiss()
            }
            setSecondaryCTAClickListener {
                viewModel.removeWishlistedProduct(adapterPosition)
                dismiss()
            }
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }.show()
    }

    override fun onAddToCartClick(dataModel: WishlistDataModel, adapterPosition: Int) {
        viewModel.addToCartProduct(adapterPosition)
    }

    override fun onClickCheckboxDeleteWishlist(position: Int, isChecked: Boolean) {
        if (position != -1) viewModel.setWishlistOnMarkDelete(position, isChecked)
    }

    override fun onWishlistClick(parentPosition: Int, childPosition: Int, wishlistStatus: Boolean) {
        viewModel.setRecommendationItemWishlist(parentPosition, childPosition, wishlistStatus)
    }

    override fun onProductImpression(dataModel: WishlistDataModel, position: Int) {
        when (dataModel) {
            is WishlistItemDataModel -> WishlistTracking.impressionProduct(trackingQueue, dataModel.productItem, position.toString())
            is RecommendationItemDataModel -> {
                if(dataModel.recommendationItem.isTopAds) {
                    TopAdsUrlHitter(context).hitImpressionUrl(
                            className,
                            dataModel.recommendationItem.trackerImageUrl,
                            dataModel.recommendationItem.productId.toString(),
                            dataModel.recommendationItem.name,
                            dataModel.recommendationItem.imageUrl
                    )
                }
                WishlistTracking.impressionEmptyWishlistRecommendation(trackingQueue, dataModel.recommendationItem, position)
            }
            is RecommendationCarouselItemDataModel -> {
                if(dataModel.recommendationItem.isTopAds) {
                    TopAdsUrlHitter(context).hitImpressionUrl(
                            className,
                            dataModel.recommendationItem.trackerImageUrl,
                            dataModel.recommendationItem.productId.toString(),
                            dataModel.recommendationItem.name,
                            dataModel.recommendationItem.imageUrl
                    )
                }
                WishlistTracking.impressionRecommendation(trackingQueue, dataModel.recommendationItem, position)
            }
        }
    }

    override fun onBannerTopAdsClick(item: BannerTopAdsDataModel, position: Int) {
        TopAdsUrlHitter(context).hitClickUrl(
                this::class.java.simpleName,
                item.topAdsDataModel.adClickUrl,
                "",
                "",
                item.topAdsDataModel.imageUrl
        )
        WishlistTracking.clickTopAdsBanner(item, viewModel.getUserId(), position)
        RouteManager.route(context, item.topAdsDataModel.applink)
    }

    override fun onBannerTopAdsImpress(item: BannerTopAdsDataModel, position: Int) {
        TopAdsUrlHitter(context).hitImpressionUrl(
                this::class.java.simpleName,
                item.topAdsDataModel.adViewUrl,
                "",
                "",
                item.topAdsDataModel.imageUrl
        )
        WishlistTracking.impressionTopAdsBanner(item, viewModel.getUserId(), position)
    }

    private fun handleAddToCartActionData(addToCartActionData: AddToCartActionData?) {
        addToCartActionData?.let {
            if (it.isSuccess) {
                WishlistTracking.clickBuy(cartId = it.cartId.toString(), wishlistItem = it.item)
                showToaster(getString(R.string.wishlist_success_atc), getString(R.string.wishlist_see)) {
                    WishlistTracking.clickSeeCart()
                    RouteManager.route(context, ApplinkConst.CART)
                }
            } else {
                showToaster(if (it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleUpdateCartCounterActionData(count: Int?) {
        activity?.let { activity ->
            count?.let {
                val cache = LocalCacheHandler(activity, "CART")
                cache.putInt(CACHE_KEY_IS_HAS_CART, if (count > 0) 1 else 0)
                cache.putInt(CACHE_KEY_TOTAL_CART, count)
                cache.applyEditor()

                activity.invalidateOptionsMenu()
            }
        }
    }

    private fun handleAddWishlistRecommendationActionData(addWishlistRecommendationData: AddWishlistRecommendationData?) {
        addWishlistRecommendationData?.let {
            if (it.isSuccess) {
                showToaster(getString(R.string.wishlist_success_add))
            } else {
                showToaster(if (it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleBulkRemoveActionData(bulkRemoveWishlistActionData: BulkRemoveWishlistActionData?) {
        bulkRemoveWishlistActionData?.let {
            if (it.isSuccess) {
                WishlistTracking.clickConfirmBulkRemoveWishlist(trackingQueue, it.productIds)
                showToaster(getString(R.string.wishlist_success_remove), "Ok")
                viewModel.exitBulkMode()
            } else {
                showToaster(if (it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleRemoveWishlistActionData(removeWishlistActionData: RemoveWishlistActionData?) {
        removeWishlistActionData?.let {
            if (it.isSuccess) {
                WishlistTracking.clickConfirmRemoveWishlist(productId = it.productId.toString())
                showToaster(getString(R.string.wishlist_success_remove), "Ok")
            } else {
                showToaster(if (it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleRemoveWishlistRecommendationActionData(removeWishlistRecommendationData: RemoveWishlistRecommendationData?) {
        removeWishlistRecommendationData?.let {
            if (it.isSuccess) {
                showToaster(getString(R.string.wishlist_success_remove), "Ok")
            } else {
                showToaster(if (it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun showToaster(message: String, action: String = "", actionClick: (() -> Unit)? = null) {
        this.view?.let {
            if (action.isNotEmpty()) Toaster.build(it, message, actionText = action, clickListener = View.OnClickListener { actionClick?.invoke() }).show()
            else Toaster.build(it, message).show()
        }
    }

    override fun onTryAgainClick() {
        viewModel.getWishlistData(additionalParams = generateWishlistAdditionalParamRequest(), shouldShowInitialPage = true)
    }

    private fun onBulkDelete() {
        viewModel.bulkRemoveWishlist()
        showSearchView()
        val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
        (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(true)
        swipeToRefresh?.isEnabled = true
    }

    private fun showOnBoarding() {
        context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, COACH_MARK_TAG)) {
                Handler().postDelayed({
                    val manageMenu = view?.rootView?.findViewById<View>(R.id.text_manage)
                    this.coachMark = CoachMark2(context)
                    manageMenu?.post {
                        val coachMarkItems: ArrayList<CoachMark2Item> = ArrayList()
                        coachMarkItems.add(
                                CoachMark2Item(
                                        manageMenu,
                                        context.getString(R.string.wishlist_coach_mark_title),
                                        context.getString(R.string.wishlist_coach_mark_description)
                                )
                        )
                        coachMark?.showCoachMark(step = coachMarkItems)
                        CoachMarkPreference.setShown(context, COACH_MARK_TAG, true)
                    }
                }, COACHMARK_SAFE_DELAY)
        }
        }
    }

    /**
     * Void [goToPDP]
     * It handling routing to PDP
     * @param productId the productId
     * @param position the position of the item at adapter
     */
    private fun goToPDP(productId: String, position: Int) {
        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    private fun manageDeleteWishlist(): Boolean {
        viewModel.enterBulkMode()
        return true
    }

    private fun cancelDeleteWishlist(): Boolean {
        viewModel.exitBulkMode()
        return true
    }

    private fun enableScrollFlagsSearch() {
        val layoutParam = collapse?.layoutParams as? AppBarLayout.LayoutParams
        layoutParam?.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        collapse?.layoutParams = layoutParam
    }

    private fun disableScrollFlagsSearch() {
        val layoutParam = collapse?.layoutParams as? AppBarLayout.LayoutParams
        layoutParam?.scrollFlags = 0
        collapse?.layoutParams = layoutParam
    }

    private fun hideSearchView() {
        appBarLayout?.setExpanded(false, true)
    }

    private fun showSearchView() {
        appBarLayout?.setExpanded(true, true)
    }

    private fun updateSelectedDeleteItem(value: Int) {
        deleteButton?.text = if (value == 0) getString(R.string.wishlist_delete_zero_text) else String.format(getString(R.string.wishlist_delete_text), value.toString())
        deleteButton?.isEnabled = value > 0
    }

    private fun updateBottomMargin() {
        recyclerView?.removeItemDecoration(itemDecorationBottom)
        recyclerView?.addItemDecoration(itemDecorationBottom)
    }

    private fun generateWishlistAdditionalParamRequest(): WishlistAdditionalParamRequest {
        if (additionalParamRequest != null) {
            return additionalParamRequest!!
        }
        context?.also {
            ChooseAddressUtils.getLocalizingAddressData(it.applicationContext)?.run {
                val wishlistAdditionalParamRequest = WishlistAdditionalParamRequest(district_id.toEmptyStringIfZero(), city_id.toEmptyStringIfZero(),
                        lat.toEmptyStringIfZero(), long.toEmptyStringIfZero(),
                        postal_code.toEmptyStringIfZero(), address_id.toEmptyStringIfZero())
                additionalParamRequest = wishlistAdditionalParamRequest
                return wishlistAdditionalParamRequest
            }
        }
        return WishlistAdditionalParamRequest()
    }
}
