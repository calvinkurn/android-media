package com.tokopedia.home_wishlist.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
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
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.model.action.*
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.util.Status
import com.tokopedia.home_wishlist.view.adapter.WishlistAdapter
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactoryImpl
import com.tokopedia.home_wishlist.view.custom.CustomSearchView
import com.tokopedia.home_wishlist.view.custom.SpaceBottomItemDecoration
import com.tokopedia.home_wishlist.view.ext.isScrollable
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
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
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
    private lateinit var searchView: CustomSearchView
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private val itemDecorationBottom by lazy { SpaceBottomItemDecoration() }
    private val dialogUnify by lazy { DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE) }
    internal var menu: Menu? = null
    private var modeBulkDelete = false
    private var isInitialPage: Boolean = true

    companion object{
        private const val SPAN_COUNT = 2
        private const val SHARE_PRODUCT_TITLE = "Bagikan Produk Ini"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_PRODUCT_ID = "product_id"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394

        fun newInstance() = WishlistFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_wishlist, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            trackingQueue = TrackingQueue(it)
            (it as AppCompatActivity).supportActionBar?.elevation = 0f
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
//                val id = data.getStringExtra(PDP_EXTRA_PRODUCT_ID)
//                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
//                        false)
//                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
//                viewModel.updateWishlist(id.toInt(), position, wishlistStatusFromPdp)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        loadData()
        observeAction()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView(view: View){
        swipeToRefresh?.setOnRefreshListener{
            if(!modeBulkDelete) {
                endlessRecyclerViewScrollListener?.resetState()
                viewModel.getWishlistData()
            }
        }
        searchView = view.findViewById(R.id.wishlist_search_view)
        searchView.setDelayTextChanged(250)
        searchView.setListener(object : SearchInputView.Listener{
            override fun onSearchSubmitted(text: String?) {
                searchView.hideKeyboard()
            }

            override fun onSearchTextChanged(text: String) {
                updateScrollFlagForSearchView(text.isNotEmpty())
                viewModel.getWishlistData(text)
            }
        })
        deleteButton?.setOnClickListener {
            dialogUnify.apply {
                setTitle(getString(R.string.wishlist_delete_title))
                setDescription(getString(R.string.wishlist_message_delete_alert))
                setPrimaryCTAText(getString(R.string.wishlist_delete))
                setSecondaryCTAText(getString(R.string.wishlist_cancel))
                setPrimaryCTAClickListener { onBulkDelete();dismiss() }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }.show()
        }
        recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.adapter = adapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(recyclerView?.layoutManager) {
            override fun getCurrentPage(): Int = 1

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(viewModel.wishlistState.value == Status.EMPTY){
                    viewModel.getRecommendationOnEmptyWishlist(page + 1)
                }else{
                    viewModel.getNextPageWishlistData()
                }
            }
        }
        recyclerView?.addOnScrollListener(endlessRecyclerViewScrollListener as EndlessRecyclerViewScrollListener)
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                searchView.hideKeyboard()
            }
        })
    }

    /**
     * Void [loadData]
     * It handling trigger loadWishlist primaryProduct and recommendationList from viewModel
     */
    private fun loadData(){
        viewModel.wishlistLiveData.observe(viewLifecycleOwner, Observer { response ->
            if(response.isNotEmpty())
                renderList(response)
        })
        viewModel.loadMoreWishlistAction.observe(viewLifecycleOwner, Observer {
            loadMoreWishlistActionData -> updateScrollListenerState(
                loadMoreWishlistActionData.getContentIfNotHandled()?.hasNextPage?:false)

        })
        searchView.hide()
        viewModel.wishlistState.observe(viewLifecycleOwner, Observer { state ->
            if(state.isEmpty() || state.isLoading() || state.isError()){
                if(state.isLoading()) endlessRecyclerViewScrollListener?.resetState()
                if(state.isEmpty() || state.isError()){
                    searchView.hide()
                    menu?.findItem(R.id.cancel)?.isVisible = false
                    menu?.findItem(R.id.manage)?.isVisible = false
                }
                swipeToRefresh?.isRefreshing = false
            } else {
                // success state
                swipeToRefresh?.isRefreshing = false
                searchView.show()
                menu?.findItem(R.id.manage)?.isVisible = true

                if(isInitialPage) {
                    isInitialPage = false
                    Handler().postDelayed({
                        if(recyclerView?.isScrollable() == true){
                            updateScrollFlagForSearchView(false)
                        }
                    }, 1500)
                }
            }
        })
        viewModel.getWishlistData(shouldShowInitialPage = true)
    }

    private fun observeAction(){
        viewModel.addToCartActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.removeWishlistActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.bulkRemoveWishlistActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.bulkSelectActionData.observe(viewLifecycleOwner, Observer { updateSelectedDeleteItem(it.peekContent()) })
        viewModel.addWishlistRecommendationActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.removeWishlistRecommendationActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
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
        val layoutParam = searchView.layoutParams as AppBarLayout.LayoutParams
        if(isSearch){
            layoutParam.scrollFlags = 0
        }else {
            layoutParam.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        }
        searchView.layoutParams = layoutParam
    }

    override fun onProductClick(dataModel: WishlistDataModel, position: Int) {
        when (dataModel) {
            is WishlistItemDataModel -> {
                WishlistTracking.productClick(dataModel.productItem, position.toString())
                goToPDP(dataModel.productItem.id, position)
            }
            is RecommendationCarouselItemDataModel -> {
                WishlistTracking.clickRecommendation(dataModel.recommendationItem, position)
                goToPDP(dataModel.recommendationItem.productId.toString(), position)
            }
            is RecommendationItemDataModel -> {
                WishlistTracking.clickRecommendation(dataModel.recommendationItem, position)
                goToPDP(dataModel.recommendationItem.productId.toString(), position)
            }
        }
    }

    override fun onDeleteClick(dataModel: WishlistDataModel, position: Int) {
        dialogUnify.apply {
            setTitle(getString(R.string.wishlist_delete_title))
            setDescription(getString(R.string.wishlist_message_delete_alert))
            setPrimaryCTAText(getString(R.string.wishlist_delete))
            setSecondaryCTAText(getString(R.string.wishlist_cancel))
            setPrimaryCTAClickListener {
                viewModel.removeWishlistedProduct(position)
                dismiss()
            }
            setSecondaryCTAClickListener {
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
            is RecommendationItemDataModel -> WishlistTracking.impressionRecommendation(trackingQueue, dataModel.recommendationItem, position)
            is RecommendationCarouselItemDataModel -> WishlistTracking.impressionRecommendation(trackingQueue, dataModel.recommendationItem, position)
        }
    }

    private fun handleMessageAction(action: Event<BaseActionData>){
        if(action.peekContent().isSuccess){
            when(action.peekContent()){
                is AddToCartActionData -> {
                    showToaster(getString(R.string.wishlist_success_atc), getString(R.string.wishlist_see)){
                        RouteManager.route(context, ApplinkConst.CART)
                    }
//                    WishlistTracking.clickBuy()
                }
                is AddWishlistRecommendationData -> showToaster(getString(R.string.wishlist_success_add))
                is BulkRemoveWishlistActionData -> {
                    showToaster(getString(R.string.wishlist_success_remove), "Ok")
                }
                is RemoveWishlistActionData -> showToaster(getString(R.string.wishlist_success_remove), "Ok")
                is RemoveWishlistRecommendationData -> showToaster(getString(R.string.wishlist_success_remove), "Ok")
            }
        }else {
            showToaster(if(action.peekContent().message.isNotEmpty()) action.peekContent().message else getString(R.string.wishlist_default_error_message))
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
    }

    private fun showOnBoarding(){
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
                val coachMark = CoachMarkBuilder().allowPreviousButton(false).build()
                coachMark.show(activity, "wishlist", coachMarkItems)
            }
        }, 100)
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
        modeBulkDelete = true

        menu?.findItem(R.id.cancel)?.isVisible = true
        menu?.findItem(R.id.manage)?.isVisible = false

        containerDelete?.show()
        searchView.hide()
        recyclerView?.addItemDecoration(itemDecorationBottom)

        viewModel.updateBulkMode(true)

        swipeToRefresh?.isEnabled = false

        return true
    }

    private fun cancelDeleteWishlist(): Boolean{
        modeBulkDelete = false

        menu?.findItem(R.id.cancel)?.isVisible = false
        menu?.findItem(R.id.manage)?.isVisible = true

        containerDelete?.hide()
        recyclerView?.removeItemDecoration(itemDecorationBottom)
        searchView.show()

        swipeToRefresh?.isEnabled = true
        viewModel.updateBulkMode(false)
        return true
    }

    private fun updateSelectedDeleteItem(value: Int){
        deleteButton?.text = "Hapus $value produk"
        deleteButton?.isEnabled = value > 0
    }
}