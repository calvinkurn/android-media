package com.tokopedia.home_wishlist.view.fragment

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.analytics.WishlistTracking
import com.tokopedia.home_wishlist.common.ToolbarElevationOffsetListener
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.model.action.*
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.util.GravitySnapHelper
import com.tokopedia.home_wishlist.view.adapter.WishlistAdapter
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactoryImpl
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
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
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
@SuppressLint("SyntheticAccessor")
open class WishlistFragment: BaseDaggerFragment(), WishlistListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: SmartExecutors

    private lateinit var trackingQueue: TrackingQueue
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    internal val viewModel by lazy{ viewModelProvider.get(WishlistViewModel::class.java) }
    private val adapterFactory by lazy { WishlistTypeFactoryImpl(appExecutors) }
    private val adapter by lazy { WishlistAdapter(appExecutors, adapterFactory, this) }
    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.recycler_view) }
    private val swipeToRefresh by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val containerDelete by lazy { view?.findViewById<FrameLayout>(R.id.container_delete) }
    private val deleteButton by lazy { view?.findViewById<UnifyButton>(R.id.delete_button) }
    private val searchView by lazy { view?.findViewById<CustomSearchView>(R.id.wishlist_search_view) }
    private val toolbar by lazy { view?.findViewById<Toolbar>(R.id.toolbar)}
    private val appBarLayout by lazy { view?.findViewById<AppBarLayout>(R.id.app_bar_layout)}
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private val coachMark by lazy { CoachMarkBuilder().allowPreviousButton(false).build() }
    private val staggeredGridLayoutManager by lazy { StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) }
    private val itemDecorationBottom by lazy { SpaceBottomItemDecoration() }
    private lateinit var toolbarElevation: ToolbarElevationOffsetListener
    private val dialogUnify by lazy { DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
    internal var menu: Menu? = null

    companion object{
        private const val SPAN_COUNT = 2
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val COACH_MARK_TAG = "wishlist"
        private const val REQUEST_FROM_PDP = 394

        fun newInstance() = WishlistFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_home_wishlist, container, false)
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
        hideSearchView()
        observeData()
        viewModel.getWishlistData(shouldShowInitialPage = true)
    }

    override fun onPause() {
        super.onPause()
        if(this::trackingQueue.isInitialized) trackingQueue.sendAll()
    }

    override fun getScreenName(): String = getString(R.string.wishlist_global)

    override fun initInjector() {
        getComponent(WishlistComponent::class.java).inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.wishlist_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        showOnBoarding()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.manage -> manageDeleteWishlist()
            R.id.cancel -> cancelDeleteWishlist()
            android.R.id.home -> {
                activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        initToolbar()
        initSwipeRefresh()
        initSearchView()
        initDeleteBulkButton()
        initRecyclerView()
    }

    private fun initToolbar(){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout?.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.appbar_elevation)
        }
        toolbar?.let{
            toolbarElevation = ToolbarElevationOffsetListener(activity as AppCompatActivity, it)
        }
    }

    private fun initSwipeRefresh(){
        swipeToRefresh?.setOnRefreshListener{
            updateBottomMargin()
            endlessRecyclerViewScrollListener?.resetState()
            viewModel.getWishlistData(searchView?.searchText ?: "")
        }
    }

    private fun initSearchView(){
        searchView?.setDelayTextChanged(250)
        searchView?.setListener(object : SearchInputView.Listener{
            override fun onSearchSubmitted(text: String?) {
                searchView?.hideKeyboard()
            }

            override fun onSearchTextChanged(text: String) {
                updateScrollFlagForSearchView(text.isNotEmpty())
                viewModel.getWishlistData(text)
            }
        })
    }

    private fun initDeleteBulkButton(){
        val count = viewModel.bulkSelectCountActionData.value?.peekContent() ?: 0
        val title = if(count == 0) getString(R.string.wishlist_delete_zero_text) else String.format(getString(R.string.wishlist_delete_text), count.toString())
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

    private fun initRecyclerView(){
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
            if(state.isEmpty()) hideSearchView()
            else showSearchView()

            val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
            (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(!state.isEmpty())

            endlessRecyclerViewScrollListener?.let {
                recyclerView?.removeOnScrollListener(it)
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(recyclerView?.layoutManager) {
                override fun getCurrentPage(): Int = 1

                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if(state.isEmpty()){
                        updateBottomMargin()
                        viewModel.getRecommendationOnEmptyWishlist(page + 1)
                    }else{
                        viewModel.getNextPageWishlistData()
                    }
                }
            }
            recyclerView?.addOnScrollListener(endlessRecyclerViewScrollListener as EndlessRecyclerViewScrollListener)

            menu?.findItem(R.id.manage)?.isVisible = state.isSuccess()
        })


    }

    private fun observeWishlistData() {
        viewModel.wishlistLiveData.observe(viewLifecycleOwner, Observer { response ->
            if(response.isNotEmpty())
                renderList(response)
            swipeToRefresh?.isRefreshing = false
        })
        viewModel.loadMoreWishlistAction.observe(viewLifecycleOwner, Observer {
            loadMoreWishlistActionData -> updateScrollListenerState(
                loadMoreWishlistActionData.getContentIfNotHandled()?.hasNextPage?:false)

        })
    }

    private fun observeBulkModeState() {
        viewModel.isInBulkModeState.observe(viewLifecycleOwner, Observer { isInBulkMode ->
            if (isInBulkMode) {
                updateBottomMargin()
                menu?.findItem(R.id.cancel)?.isVisible = true
                menu?.findItem(R.id.manage)?.isVisible = false

                containerDelete?.show()

                hideSearchView()
                val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
                (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(false)
                swipeToRefresh?.isEnabled = false
            } else {
                menu?.findItem(R.id.cancel)?.isVisible = false
                menu?.findItem(R.id.manage)?.isVisible = true

                containerDelete?.hide()

                showSearchView()
                val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
                (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(true)
                swipeToRefresh?.isEnabled = true
            }
        })


    }

    private fun observeAction(){
        viewModel.addToCartActionData.observe(viewLifecycleOwner, Observer { handleAddToCartActionData(it.getContentIfNotHandled()) })
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

    private fun renderList(list: List<WishlistDataModel>){
        val recyclerViewState = recyclerView?.layoutManager?.onSaveInstanceState()
        adapter.submitList(list)
        recyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun updateScrollListenerState(hasNextPage: Boolean) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)
    }

    private fun updateScrollFlagForSearchView(isSearch: Boolean){
        if(isSearch){
            disableScrollFlagsSearch()
        }else {
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
                viewModel.onProductClick(
                        dataModel.recommendationItem.productId,
                        parentPosition,
                        position
                )
            }
            is RecommendationItemDataModel -> {
                WishlistTracking.clickRecommendation(dataModel.recommendationItem, position)
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
        viewModel.setWishlistOnMarkDelete(position, isChecked)
    }

    override fun onWishlistClick(parentPosition: Int, childPosition: Int, wishlistStatus: Boolean) {
        viewModel.setRecommendationItemWishlist(parentPosition, childPosition, wishlistStatus)
    }

    override fun onProductImpression(dataModel: WishlistDataModel, position: Int) {
        when (dataModel) {
            is WishlistItemDataModel -> WishlistTracking.impressionProduct(trackingQueue, dataModel.productItem, position.toString())
            is RecommendationItemDataModel -> WishlistTracking.impressionEmptyWishlistRecommendation(trackingQueue, dataModel.recommendationItem, position)
            is RecommendationCarouselItemDataModel -> WishlistTracking.impressionRecommendation(trackingQueue, dataModel.recommendationItem, position)
        }
    }

    private fun handleAddToCartActionData(addToCartActionData: AddToCartActionData?){
        addToCartActionData?.let {
            if(it.isSuccess){
                WishlistTracking.clickBuy(cartId = it.cartId.toString(), wishlistItem = it.item)
                showToaster(getString(R.string.wishlist_success_atc), getString(R.string.wishlist_see)){
                    WishlistTracking.clickSeeCart()
                    RouteManager.route(context, ApplinkConst.CART)
                }
            }else {
                showToaster(if(it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleAddWishlistRecommendationActionData(addWishlistRecommendationData: AddWishlistRecommendationData?){
        addWishlistRecommendationData?.let {
            if(it.isSuccess){
                showToaster(getString(R.string.wishlist_success_add))
            }else {
                showToaster(if(it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleBulkRemoveActionData(bulkRemoveWishlistActionData: BulkRemoveWishlistActionData?){
        bulkRemoveWishlistActionData?.let {
            if(it.isSuccess){
                WishlistTracking.clickConfirmBulkRemoveWishlist(trackingQueue, it.productIds)
                showToaster(getString(R.string.wishlist_success_remove), "Ok")
                viewModel.exitBulkMode()
            }else {
                showToaster(if(it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleRemoveWishlistActionData(removeWishlistActionData: RemoveWishlistActionData?){
        removeWishlistActionData?.let {
            if(it.isSuccess){
                WishlistTracking.clickConfirmRemoveWishlist(productId = it.productId.toString())
                showToaster(getString(R.string.wishlist_success_remove), "Ok")
            }else {
                showToaster(if(it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun handleRemoveWishlistRecommendationActionData(removeWishlistRecommendationData: RemoveWishlistRecommendationData?){
        removeWishlistRecommendationData?.let {
            if(it.isSuccess){
                showToaster(getString(R.string.wishlist_success_remove), "Ok")
            }else {
                showToaster(if(it.message.isNotEmpty()) it.message else getString(R.string.wishlist_default_error_message))
            }
        }
    }

    private fun showToaster(message: String, action: String = "", actionClick: (() -> Unit)? = null){
        this.view?.let {
            if(action.isNotEmpty()) Toaster.make(it, message, actionText = action, clickListener = View.OnClickListener { actionClick?.invoke() })
            else Toaster.make(it, message)
        }
    }

    override fun onTryAgainClick() {
        viewModel.getWishlistData(shouldShowInitialPage = true)
    }

    private fun onBulkDelete(){
        viewModel.bulkRemoveWishlist()
        showSearchView()
        val layoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
        (layoutParams.behavior as CustomAppBarLayoutBehavior).setScrollBehavior(true)
        swipeToRefresh?.isEnabled = true
    }

    private fun showOnBoarding(){
        if(!coachMark.hasShown(activity, COACH_MARK_TAG)){
            Handler().postDelayed({
                val manageMenu = view?.rootView?.findViewById<View>(R.id.manage)

                manageMenu?.post {
                    val coachMarkItems: ArrayList<CoachMarkItem> = ArrayList()
                    coachMarkItems.add(
                            CoachMarkItem(
                                    manageMenu,
                                    getString(R.string.wishlist_coach_mark_title),
                                    getString(R.string.wishlist_coach_mark_description)
                            )
                    )
                    coachMark.show(activity, "wishlist", coachMarkItems)
                }
            }, 100)
        }
    }

    /**
     * Void [goToPDP]
     * It handling routing to PDP
     * @param productId the productId
     * @param position the position of the item at adapter
     */
    private fun goToPDP(productId: String, position: Int){
        RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    private fun manageDeleteWishlist(): Boolean{
        viewModel.enterBulkMode()
        return true
    }

    private fun cancelDeleteWishlist(): Boolean{
        viewModel.exitBulkMode()
        return true
    }

    private fun enableScrollFlagsSearch(){
        val layoutParam = collapse?.layoutParams as AppBarLayout.LayoutParams
        layoutParam.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        collapse?.layoutParams = layoutParam
    }

    private fun disableScrollFlagsSearch(){
        val layoutParam = collapse?.layoutParams as AppBarLayout.LayoutParams
        layoutParam.scrollFlags = 0
        collapse?.layoutParams = layoutParam
    }

    private fun hideSearchView(){
        appBarLayout?.setExpanded(false, true)
    }

    private fun showSearchView(){
        appBarLayout?.setExpanded(true, true)
    }

    private fun updateSelectedDeleteItem(value: Int){
        deleteButton?.text = if(value == 0) getString(R.string.wishlist_delete_zero_text) else String.format(getString(R.string.wishlist_delete_text), value.toString())
        deleteButton?.isEnabled = value > 0
    }

    private fun updateBottomMargin(){
        recyclerView?.removeItemDecoration(itemDecorationBottom)
        recyclerView?.addItemDecoration(itemDecorationBottom)
    }
}