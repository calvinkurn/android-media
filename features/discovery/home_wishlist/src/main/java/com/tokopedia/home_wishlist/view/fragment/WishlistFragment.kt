package com.tokopedia.home_wishlist.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.home_wishlist.di.WishlistComponent
import com.tokopedia.home_wishlist.model.action.*
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistItemDataModel
import com.tokopedia.home_wishlist.util.Status
import com.tokopedia.home_wishlist.view.adapter.WishlistAdapter
import com.tokopedia.home_wishlist.view.adapter.WishlistTypeFactoryImpl
import com.tokopedia.home_wishlist.view.custom.CustomSearchView
import com.tokopedia.home_wishlist.view.ext.isEmptyWishlist
import com.tokopedia.home_wishlist.view.ext.isErrorWishlist
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
    internal val searchView by lazy { view?.findViewById<CustomSearchView>(R.id.search_view) }
    private val swipeToRefresh by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val containerDelete by lazy { view?.findViewById<FrameLayout>(R.id.container_delete) }
    private val deleteButton by lazy { view?.findViewById<UnifyButton>(R.id.delete_button) }
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    internal var menu: Menu? = null
    private var modeBulkDelete = false

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
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
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
        initView()
        loadData()
        observeAction()
    }

    override fun getScreenName(): String = getString(R.string.home_recom_screen_name)

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

    private fun initView(){
        swipeToRefresh?.setOnRefreshListener{
            if(!modeBulkDelete) {
                endlessRecyclerViewScrollListener?.resetState()
                viewModel.getWishlistData()
            }
        }
        searchView?.setDelayTextChanged(250)
        searchView?.setListener(object : SearchInputView.Listener{
            override fun onSearchSubmitted(text: String?) {
                searchView?.hideKeyboard()
            }

            override fun onSearchTextChanged(text: String?) {
                viewModel.getWishlistData(text ?: "")
            }
        })
        deleteButton?.setOnClickListener {
            AlertDialog.Builder(it.context)
                    .setTitle(getString(R.string.wishlist_delete_title))
                    .setMessage(getString(R.string.wishlist_message_delete_alert))
                    .setPositiveButton(getString(R.string.wishlist_delete)) { _, _ ->
                        onBulkDelete()
                    }
                    .setNegativeButton(getString(R.string.wishlist_cancel)) { _, _ -> }
                    .show()
        }
        recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.adapter = adapter
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(recyclerView?.layoutManager) {
            override fun getCurrentPage(): Int = 1

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(viewModel.wishlistState.value == Status.EMPTY){
                    viewModel.getRecommendationOnEmptyWishlist(page + 1)
                }else{
                    if(menu?.findItem(R.id.cancel)?.isVisible == false) viewModel.getNextPageWishlistData()
                }
            }
        }
        recyclerView?.addOnScrollListener(endlessRecyclerViewScrollListener as EndlessRecyclerViewScrollListener)
    }

    /**
     * Void [loadData]
     * It handling trigger loadWishlist primaryProduct and recommendationList from viewModel
     */
    private fun loadData(){
        viewModel.wishlistData.observe(viewLifecycleOwner, Observer { response ->
            if(response.isNotEmpty()) renderList(response)
        })
        viewModel.wishlistState.observe(viewLifecycleOwner, Observer { state ->
            if(state.isEmpty() || state.isLoading() || state.isError()){
                searchView?.hide()
                swipeToRefresh?.isRefreshing = false
                menu?.findItem(R.id.cancel)?.isVisible = false
                menu?.findItem(R.id.manage)?.isVisible = false
            } else {
                // success state
                swipeToRefresh?.isRefreshing = false
                searchView?.show()
                menu?.findItem(R.id.manage)?.isVisible = true
            }
        })
        viewModel.getWishlistData()
    }

    private fun observeAction(){
        viewModel.addToCartActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.removeWishlistActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.bulkRemoveWishlistActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.addWishlistRecommendationActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
        viewModel.removeWishlistRecommendationActionData.observe(viewLifecycleOwner, Observer { handleMessageAction(it) })
    }

    private fun renderList(list: List<WishlistDataModel>){
        val recyclerViewState = recyclerView?.layoutManager?.onSaveInstanceState()
        adapter.submitList(list)
        recyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    override fun onProductClick(dataModel: WishlistDataModel, position: Int) {
        if(dataModel is WishlistItemDataModel){
            goToPDP(dataModel.productItem.id, position)
        } else if(dataModel is RecommendationCarouselItemDataModel){
            goToPDP(dataModel.recommendationItem.productId.toString(), position)
        }
    }

    override fun onDeleteClick(dataModel: WishlistDataModel, adapterPosition: Int) {
        context?.let { context ->
            AlertDialog.Builder(context)
                    .setTitle(getString(R.string.wishlist_delete_title))
                    .setMessage(getString(R.string.wishlist_message_delete_alert))
                    .setPositiveButton(getString(R.string.wishlist_delete)) { _, _ ->
                        viewModel.removeWishlistedProduct(adapterPosition)
                    }
                    .setNegativeButton(getString(R.string.wishlist_cancel)) { _, _ -> }
                    .show()
        }
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

    override fun onProductImpression(dataModel: WishlistDataModel) {
        // Add tracker
    }

    private fun handleMessageAction(action: Event<BaseActionData>){
        if(action.peekContent().isSuccess){
            when(action.peekContent()){
                is AddToCartActionData -> showToaster(getString(R.string.wishlist_success_atc))
                is AddWishlistRecommendationData -> showToaster(getString(R.string.wishlist_success_add))
                is BulkRemoveWishlistActionData -> showToaster(getString(R.string.wishlist_success_remove))
                is RemoveWishlistActionData -> showToaster(getString(R.string.wishlist_success_remove))
                is RemoveWishlistRecommendationData -> showToaster(getString(R.string.wishlist_success_remove))
            }
        }else {
            showToaster(if(action.peekContent().message.isNotEmpty()) action.peekContent().message else getString(R.string.wishlist_default_error_message))
        }
    }

    private fun showToaster(message: String){
        this.view?.let { Toaster.make(it, message) }
    }

    override fun onTryAgainClick() {
        viewModel.getWishlistData()
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
                coachMark.show(activity, tag, coachMarkItems)
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
        searchView?.hide()

        viewModel.updateBulkMode(true)

        swipeToRefresh?.isEnabled = false

        return true
    }

    private fun cancelDeleteWishlist(): Boolean{
        modeBulkDelete = false

        menu?.findItem(R.id.cancel)?.isVisible = false
        menu?.findItem(R.id.manage)?.isVisible = true

        containerDelete?.hide()
        searchView?.show()

        swipeToRefresh?.isEnabled = true
        viewModel.updateBulkMode(false)
        return true
    }
}