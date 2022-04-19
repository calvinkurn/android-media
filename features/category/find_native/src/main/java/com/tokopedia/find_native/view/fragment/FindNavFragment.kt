package com.tokopedia.find_native.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.common_category.adapter.BaseCategoryAdapter
import com.tokopedia.common_category.adapter.ProductNavListAdapter
import com.tokopedia.common_category.adapter.QuickFilterAdapter
import com.tokopedia.common_category.factory.ProductTypeFactory
import com.tokopedia.common_category.factory.product.ProductTypeFactoryImpl
import com.tokopedia.common_category.fragment.BaseBannedProductFragment
import com.tokopedia.common_category.interfaces.ProductCardListener
import com.tokopedia.common_category.interfaces.QuickFilterListener
import com.tokopedia.common_category.model.bannedCategory.BannedData
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.find_native.R
import com.tokopedia.find_native.analytics.FindPageAnalytics.Companion.findPageAnalytics
import com.tokopedia.find_native.data.model.RelatedLinkData
import com.tokopedia.find_native.di.component.DaggerFindNavComponent
import com.tokopedia.find_native.di.component.FindNavComponent
import com.tokopedia.find_native.view.adapters.FindPriceListAdapter
import com.tokopedia.find_native.view.adapters.FindRelatedLinkAdapter
import com.tokopedia.find_native.viewmodel.FindNavViewModel
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.android.synthetic.main.find_nav_fragment.*
import kotlinx.android.synthetic.main.layout_find_related.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val REQUEST_ACTIVITY_SORT_PRODUCT = 102
private const val REQUEST_ACTIVITY_FILTER_PRODUCT = 103
private const val REQUEST_PRODUCT_ITEM_CLICK = 1002

class FindNavFragment : BaseBannedProductFragment(), ProductCardListener,
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener, WishListActionListener,
        FindRelatedLinkAdapter.RelatedLinkClickListener, FindPriceListAdapter.PriceListClickListener {

    private var findNavScreenName: String = "Find"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var gcmHandler: GCMHandler

    @Inject
    lateinit var removeWishlistActionUseCase: RemoveWishListUseCase

    @Inject
    lateinit var addWishListActionUseCase: AddWishListUseCase
    private lateinit var component: FindNavComponent
    private var quickFilterAdapter: QuickFilterAdapter? = null
    private var productNavListAdapter: ProductNavListAdapter? = null
    private var productList: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()
    private lateinit var productTypeFactory: ProductTypeFactory
    private var quickFilterList = ArrayList<Filter>()
    private lateinit var findNavViewModel: FindNavViewModel
    private var pageCount = 0
    private var rows = 60
    private var productCount = 0
    private var isPagingAllowed: Boolean = true

    private lateinit var findSearchParam: String

    companion object {
        private const val EXTRA_FIND_PARAM = "findParam"
        fun newInstance(searchParam: String): Fragment {
            val fragment = FindNavFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_FIND_PARAM, searchParam)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.find_nav_fragment, container, false)
    }

    override fun addFragmentView() {
        view?.findViewById<View>(R.id.swipe_refresh_layout)?.show()
        view?.findViewById<View>(R.id.layout_banned_screen)?.hide()
    }

    override fun initFragmentView() {
        getFindNavScreenName()
        setUpVisibleFragmentListener()
        setUpNavigation()
        initViewModel()
        init()
        observeData()
        setUpAdapter()
    }

    override fun getDataFromArguments() {
        arguments?.let {
            findSearchParam = it.getString(EXTRA_FIND_PARAM, "")
        }
    }

    override fun hideFragmentView() {
        view?.findViewById<View>(R.id.swipe_refresh_layout)?.hide()
    }

    override fun onPause() {
        super.onPause()
        productNavListAdapter?.onPause()
    }

    private fun getFindNavScreenName() {
        findNavScreenName = findSearchParam.replace("-", " ")
        val splits = findNavScreenName.split(" ")
        findNavScreenName = ""
        for (i in splits) {
            findNavScreenName = findNavScreenName.plus(i.capitalize() + " ")
        }
        findNavScreenName.trim()
    }

    private fun setUpAdapter() {
        productTypeFactory = ProductTypeFactoryImpl(this)
        productNavListAdapter = ProductNavListAdapter(productTypeFactory, productList, this)
        productRecyclerView.adapter = productNavListAdapter
        productRecyclerView.layoutManager = getStaggeredGridLayoutManager()
        productNavListAdapter?.addShimmer()
    }

    private fun observeData() {
        findNavViewModel.getProductListLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (!findNavViewModel.checkForAdultData()) {
                        AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_FIND_PAGE, findSearchParam)
                    }
                    removeShimmerIfRunning()
                    handleForProductsData(it.data)
                    showProductPriceSection(it.data as ArrayList<ProductsItem>)
                    hideRefreshLayout()
                    reloadFilter()
                }

                is Fail -> {
                    removeLoadingFromAdapter()
                    hideRefreshLayout()
                    if (productList.isEmpty()) {
                        showNoDataScreen(true)
                    }
                    isPagingAllowed = true
                }

            }
        })

        findNavViewModel.getBannedLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    addBannedProductScreen()
                    val bannedData = BannedData()
                    bannedData.bannedMsgHeader = getString(R.string.find_nav_banned_product)
                    bannedData.bannedMessage = it.data[0]
                    bannedData.displayButton = it.data[1].isNotEmpty()
                    bannedData.appRedirection = it.data[1]
                    bannedData.name = findNavScreenName
                    showBannedProductScreen(bannedData)
                }
            }
        })

        findNavViewModel.getProductCountLiveData().observe(viewLifecycleOwner, Observer {
            it?.let {
                it[0].let { countText ->
                    setTotalSearchResultCount(countText)
                    setQuickFilterAdapter(countText)
                }
                it[1].let { count ->
                    productCount = count.toInt()
                    setPagingAllowed()
                }
            }
        })

        findNavViewModel.getQuickFilterListListLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    loadQuickFilters(it.data as ArrayList)
                }
            }
        })

        findNavViewModel.getDynamicFilterListLiveData().observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }
            }
        })

        findNavViewModel.getRelatedLinkListLiveData().observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    renderRelatedLink(it.data as ArrayList<RelatedLinkData>)
                }
                is Fail -> {
                    renderRelatedLink(ArrayList())
                }
            }
        })
    }

    private fun hideBtnLoadMoreIfRequired() {
        if (isPagingAllowed) {
            btnLoadMore.show()
        } else {
            btnLoadMore.hide()
        }
    }

    private fun removeLoadingFromAdapter() {
        productNavListAdapter?.removeLoading()
    }

    private fun handleForProductsData(list: List<ProductsItem>) {
        if (list.isNotEmpty()) {
            showNoDataScreen(false)
            productList.addAll(list as ArrayList<Visitable<ProductTypeFactory>>)
            removeLoadingFromAdapter()
            productRecyclerView.adapter?.notifyDataSetChanged()
            setPagingAllowed()
        } else {
            showNoDataScreen(true)
        }
    }

    private fun setPagingAllowed() {
        isPagingAllowed = !productList.isNullOrEmpty() && productList.size < productCount
        hideBtnLoadMoreIfRequired()
    }

    private fun removeShimmerIfRunning() {
        if (productNavListAdapter?.isShimmerRunning() == true) {
            productNavListAdapter?.removeShimmer()
        }
    }

    private fun showProductPriceSection(productList: ArrayList<ProductsItem>) {
        if (productList.isNotEmpty()) {
            showPriceHeader()
            priceListRecyclerView.adapter = FindPriceListAdapter(productList, this)
            priceListRecyclerView.layoutManager = LinearLayoutManager(activity)
        } else {
            hidePriceHeader()
        }
        btnReadMore.setOnClickListener {
            val layoutParams = layoutRelated.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutRelated.layoutParams = layoutParams
            btnReadMore.hide()
        }
    }

    private fun hidePriceHeader() {
        priceListHeader.hide()
        dataUpdatedHeader.hide()
    }

    private fun showPriceHeader() {
        priceListHeader.show()
        dataUpdatedHeader.show()
        val calendar = Calendar.getInstance()
        val currentMonthYear = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("in", "ID")) + " ${calendar?.get(Calendar.YEAR)}"
        val dateUpdatedText = getString(R.string.find_data_updated_text, calendar.time.toFormattedString("dd/MM/yyyy"))
        val priceHeaderText = getString(R.string.find_price_header_text, findNavScreenName, currentMonthYear)
        priceListHeader.text = priceHeaderText
        dataUpdatedHeader.text = dateUpdatedText
    }

    private fun renderRelatedLink(relatedLinkList: ArrayList<RelatedLinkData>) {
        if (relatedLinkList.isNotEmpty()) {
            relatedHeader.show()
            relatedRecyclerView.adapter = context?.let { FindRelatedLinkAdapter(relatedLinkList, this) }
            val layoutManager = ChipsLayoutManager.newBuilder(activity)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            relatedRecyclerView.layoutManager = layoutManager
        } else {
            relatedHeader.hide()
        }
    }

    private fun loadQuickFilters(list: ArrayList<Filter>) {
        quickFilterList.clear()
        quickFilterList.addAll(list)
        quickFilterRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun init() {
        setUpBreadCrumb()
        fetchProducts(0)
        fetchRelatedLink()
        attachLoadMoreButton()
        setQuickFilterAdapter("")
    }

    private fun fetchRelatedLink() {
        findNavViewModel.fetchRelatedLinkList(findSearchParam)
    }

    private fun setUpBreadCrumb() {
        homeBreadCrumb.setOnClickListener {
            findPageAnalytics.eventClickBreadCrumb(ApplinkConst.HOME)
            RouteManager.route(activity, ApplinkConst.HOME)
        }
        val breadCrumb = ">  $findNavScreenName"
        findBreadCrumb.text = breadCrumb
    }

    private fun reloadFilter() {
        findNavViewModel.fetchQuickFilterList(findSearchParam)
        findNavViewModel.fetchDynamicFilterList(findSearchParam)
    }

    private fun initViewModel() {
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            findNavViewModel = viewModelProvider.get(FindNavViewModel::class.java)
        }
    }

    private fun attachLoadMoreButton() {
        btnLoadMore.setOnClickListener {
            if (isPagingAllowed) {
                findPageAnalytics.eventClickViewAll(findNavScreenName)
                incrementPage()
                fetchProducts(getPage())
                productNavListAdapter?.addLoading()
                isPagingAllowed = false
            }
        }
    }

    private fun fetchProducts(start: Int) {
        findNavViewModel.fetchProductList(start, getDepartMentId(), rows, getUniqueId(), getSelectedSort(), getSelectedFilter())
    }

    private fun getPage(): Int {
        return pageCount
    }

    private fun incrementPage() {
        pageCount += 1
    }

    private fun setQuickFilterAdapter(productCount: String) {
        val count = if (!TextUtils.isEmpty(productCount)) {
            getString(R.string.result_count_template_text, productCount)
        } else {
            ""
        }
        quickFilterAdapter = QuickFilterAdapter(quickFilterList, this, count)
        quickFilterRecyclerView.adapter = quickFilterAdapter
        quickFilterRecyclerView.layoutManager = LinearLayoutManager(activity,
                RecyclerView.HORIZONTAL, false)
    }

    private fun resetPage() {
        isPagingAllowed = true
        pageCount = 0
    }

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthHelper.getMD5Hash(userSession.userId)
        else
            AuthHelper.getMD5Hash(gcmHandler.registrationId)
    }

    override fun onSwipeToRefresh() {
        reloadData()
    }

    override fun getScreenName(): String {
        return findNavScreenName
    }

    override fun initInjector() {
        component = DaggerFindNavComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
        component.inject(this)
    }

    override fun getAdapter(): BaseCategoryAdapter? {
        return productNavListAdapter
    }

    override fun reloadData() {
        if (productNavListAdapter == null) {
            return
        }
        showRefreshLayout()
        productNavListAdapter?.clearData()
        productNavListAdapter?.addShimmer()
        resetPage()
        fetchProducts(getPage())
        fetchRelatedLink()
    }

    override fun getDepartMentId(): String {
        return findSearchParam
    }

    override fun onShareButtonClicked() {
    }

    override fun getFilterRequestCode(): Int {
        return REQUEST_ACTIVITY_FILTER_PRODUCT
    }

    override fun getSortRequestCode(): Int {
        return REQUEST_ACTIVITY_SORT_PRODUCT
    }

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        findPageAnalytics.eventClickSort(sortValue = sortValue.toString())
    }

    override fun onItemClicked(item: ProductsItem, adapterPosition: Int) {
        findPageAnalytics.eventProductClick(item, findNavScreenName)
        openProductDetailPage(item, adapterPosition)
    }

    private fun openProductDetailPage(item: ProductsItem, adapterPosition: Int) {
        val intent = getProductIntent(item.id.toString(), item.categoryID.toString())
        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, REQUEST_PRODUCT_ITEM_CLICK)
        }
        if (item.isTopAds) {
            activity?.let {
                ImpresionTask(it::class.qualifiedName).execute(item.productClickTrackingUrl)
            }
        }
    }

    override fun onLongClick(item: ProductsItem, adapterPosition: Int) {
    }

    override fun onWishlistButtonClicked(productItem: ProductsItem, position: Int) {
        if (userSession.isLoggedIn) {
            disableWishListButton(productItem.id.toString())
            if (productItem.wishlist) {
                findPageAnalytics.eventClickWishList(productItem.id.toString(), false, productItem.isTopAds)
                removeWishList(productItem.id.toString(), userSession.userId)
            } else {
                findPageAnalytics.eventClickWishList(productItem.id.toString(), true, productItem.isTopAds)
                addWishList(productItem.id.toString(), userSession.userId)
            }
        } else {
            launchLoginActivity()
        }
    }

    override fun onChangeList() {
        productRecyclerView.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        productRecyclerView.requestLayout()
    }

    override fun onChangeSingleGrid() {
        productRecyclerView.requestLayout()
    }

    override fun onProductImpressed(item: ProductsItem, adapterPosition: Int) {
    }

    override fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>, viewedTopAdsList: List<Visitable<Any>>) {
        findPageAnalytics.eventProductListViewImpression(viewedProductList, viewedTopAdsList, findNavScreenName)
    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
    }

    override fun topAdsTrackerUrlTrigger(url: String, id: String, name: String, imageURL: String) {
    }

    override fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
        }
    }

    override fun isQuickFilterSelected(option: Option): Boolean {
        return getSelectedFilter().containsKey(option.key)
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String) {
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessAddWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), true)
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_add_wishlist))
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String) {
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), false)
        enableWishListButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_remove_wishlist))
    }


    private fun enableWishListButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId = if (productId.toIntOrNull() != null) {
            productId.toInt()
        } else {
            0
        }, isEnabled = true)
    }

    private fun disableWishListButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId = if (productId.toIntOrNull() != null) {
            productId.toInt()
        } else {
            0
        }, isEnabled = false)
    }

    private fun removeWishList(productId: String, userId: String) {
        removeWishlistActionUseCase.createObservable(productId, userId, this)
    }

    private fun addWishList(productId: String, userId: String) {
        addWishListActionUseCase.createObservable(productId, userId, this)
    }

    private fun launchLoginActivity() {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    private fun getProductIntent(productId: String, warehouseId: String): Intent? {
        if (context == null) {
            return null
        }

        return if (!TextUtils.isEmpty(warehouseId)) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID, productId, warehouseId)
        } else {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        }
    }

    private fun showNoDataScreen(toShow: Boolean) {
        if (toShow) {
            layoutRelated.hide()
            btnLoadMore.hide()
            layoutNoData.show()
        } else {
            layoutNoData.hide()
            layoutRelated.show()
            btnLoadMore.show()
        }
    }

    override fun onRelatedLinkClick(relatedLink: RelatedLinkData) {
        findPageAnalytics.eventClickRelatedSearch()
        RouteManager.route(context, relatedLink.url)
    }

    override fun onPriceListClick(product: ProductsItem, adapterPosition: Int) {
        findPageAnalytics.eventClickPriceList()
        openProductDetailPage(product, adapterPosition)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AdultManager.handleActivityResult(activity, requestCode, resultCode, data)
    }

    override fun addBannedProductScreen() {
        super.addBannedProductScreen()
        view?.findViewById<View>(R.id.layout_banned_screen)?.show()
    }

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout? {
        return view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)
    }
}
