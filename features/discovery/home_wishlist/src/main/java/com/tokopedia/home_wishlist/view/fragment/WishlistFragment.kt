package com.tokopedia.home_wishlist.view.fragment

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.analytics.WishlistTracking
import com.tokopedia.home_wishlist.common.EndlessRecyclerViewScrollListener
import com.tokopedia.home_wishlist.common.ToolbarElevationOffsetListener
import com.tokopedia.home_wishlist.component.HasComponent
import com.tokopedia.home_wishlist.di.WishlistComponent
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
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
open class WishlistFragment : Fragment(), WishlistListener, TopAdsListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: SmartExecutors

    private lateinit var cartLocalCacheHandler: LocalCacheHandler
    private lateinit var trackingQueue: TrackingQueue
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    internal val viewModel by lazy { viewModelProvider.get(WishlistViewModel::class.java) }
    private val adapterFactory by lazy { WishlistTypeFactoryImpl(appExecutors) }
    private val adapter by lazy { WishlistAdapter(appExecutors, adapterFactory, this) }
    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.recycler_view) }
    private val swipeToRefresh by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val containerDelete by lazy { view?.findViewById<FrameLayout>(R.id.container_delete) }
    private val deleteButton by lazy { view?.findViewById<UnifyButton>(R.id.delete_button) }
    private val searchView by lazy { view?.findViewById<CustomSearchView>(R.id.wishlist_search_view) }
    private val toolbar by lazy { view?.findViewById<Toolbar>(R.id.toolbar) }
    private val appBarLayout by lazy { view?.findViewById<AppBarLayout>(R.id.app_bar_layout) }
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private val coachMark by lazy { CoachMarkBuilder().allowPreviousButton(false).build() }
    private val staggeredGridLayoutManager by lazy { StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) }
    private val itemDecorationBottom by lazy { SpaceBottomItemDecoration() }
    private lateinit var toolbarElevation: ToolbarElevationOffsetListener
    private val dialogUnify by lazy { DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }

    private var additionalParamRequest: WishlistAdditionalParamRequest? = null

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
        fun newInstance() = WishlistFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_home_wishlist, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
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
                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                viewModel.onPDPActivityResultForWishlist(
                        id.toInt(),
                        wishlistStatusFromPdp
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initCartLocalCacheHandler()
        hideSearchView()
        observeData()
        viewModel.getWishlistData(additionalParams = generateWishlistAdditionalParamRequest(), shouldShowInitialPage = true)
        WishlistTracking.openWishlistPage(viewModel.getUserId())
        showOnBoarding()
    }

    private fun initCartLocalCacheHandler() {
        activity?.let {
            cartLocalCacheHandler = LocalCacheHandler(it, CACHE_CART)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::trackingQueue.isInitialized) trackingQueue.sendAll()
    }


    private fun initInjector() {
        getComponent(WishlistComponent::class.java)?.inject(this)
    }

    private fun <C> getComponent(componentType: Class<C>): C? {
        return componentType.cast((activity as HasComponent<C>?)?.getComponent())
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
                val drawable = ContextCompat.getDrawable(it, R.drawable.ic_cart_menu)
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
        initToolbar()
        initSwipeRefresh()
        initSearchView()
        initDeleteBulkButton()
        initRecyclerView()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout?.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.appbar_elevation)
        }
        toolbar?.let {
            toolbarElevation = ToolbarElevationOffsetListener(activity as AppCompatActivity, it)
        }
    }

    private fun initSwipeRefresh() {
        swipeToRefresh?.setOnRefreshListener {
            updateBottomMargin()
            endlessRecyclerViewScrollListener?.resetState()
            viewModel.getWishlistData(searchView?.getSearchText() ?: "", generateWishlistAdditionalParamRequest())
        }
    }

    private fun initSearchView() {
        searchView?.setDelayTextChanged(250)
        searchView?.setListener(object : CustomSearchView.Listener {
            override fun onSearchSubmitted(text: String?) {
                searchView?.hideKeyboard()
            }

            override fun onSearchTextChanged(text: String?) {
                updateScrollFlagForSearchView(text?.isNotEmpty() ?: false)
                viewModel.getWishlistData(text ?: "", generateWishlistAdditionalParamRequest())
            }

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
        recyclerView?.layoutManager = staggeredGridLayoutManager
        recyclerView?.adapter = adapter
        GravitySnapHelper(Gravity.TOP, true).attachToRecyclerView(recyclerView)
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                searchView?.hideKeyboard()
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
            if (!coachMark.hasShown(activity, COACH_MARK_TAG)) {
                Handler().postDelayed({
                    val manageMenu = view?.rootView?.findViewById<View>(R.id.text_manage)

                    manageMenu?.post {
                        val coachMarkItems: ArrayList<CoachMarkItem> = ArrayList()
                        coachMarkItems.add(
                                CoachMarkItem(
                                        manageMenu,
                                        context.getString(R.string.wishlist_coach_mark_title),
                                        context.getString(R.string.wishlist_coach_mark_description)
                                )
                        )
                        coachMark.show(activity, "wishlist", coachMarkItems)
                    }
                }, 100)
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
