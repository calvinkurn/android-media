package com.tokopedia.discovery.find.view.fragment

import android.content.Context
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
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.core.share.DefaultShare
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.ProductNavListAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.QuickFilterAdapter
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseCategorySectionFragment
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.QuickFilterListener
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.find.data.model.RelatedLinkData
import com.tokopedia.discovery.find.di.component.DaggerFindNavComponent
import com.tokopedia.discovery.find.di.component.FindNavComponent
import com.tokopedia.discovery.find.view.adapters.FindPriceListAdapter
import com.tokopedia.discovery.find.view.adapters.FindRelatedLinkAdapter
import com.tokopedia.discovery.find.viewmodel.FindNavViewModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.android.synthetic.main.find_nav_fragment.*
import kotlinx.android.synthetic.main.fragment_hotlist_nav.layout_no_data
import kotlinx.android.synthetic.main.fragment_hotlist_nav.product_recyclerview
import kotlinx.android.synthetic.main.fragment_hotlist_nav.quickfilter_recyclerview
import kotlinx.android.synthetic.main.layout_find_related.*
import kotlinx.android.synthetic.main.layout_nav_no_product.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val REQUEST_ACTIVITY_SORT_PRODUCT = 102
private const val REQUEST_ACTIVITY_FILTER_PRODUCT = 103
private const val REQUEST_PRODUCT_ITEM_CLICK = 1002
private const val FIND_SHARE_URI = "https://www.tokopedia.com/find/"

class FindNavFragment : BaseCategorySectionFragment(), ProductCardListener,
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener, WishListActionListener {

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
    private var rows = 10
    private var isPagingAllowed: Boolean = true
    private var bannedProductFoundListener: OnBannedProductFoundListener? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.find_nav_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        arguments?.let {
            findSearchParam = it.getString(EXTRA_FIND_PARAM, "")
            getFindNavScreenName()
        }
        setUpVisibleFragmentListener()
        setUpNavigation()
        initViewModel()
        init()
        observeData()
        setUpAdapter()
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
        product_recyclerview.adapter = productNavListAdapter
        product_recyclerview.layoutManager = getStaggeredGridLayoutManager()
        productNavListAdapter?.addShimmer()
    }

    private fun observeData() {
        findNavViewModel.mProductList.observe(this, Observer {
            when (it) {
                is Success -> {
                    if (productNavListAdapter?.isShimmerRunning() == true) {
                        productNavListAdapter?.removeShimmer()
                    }

                    if (it.data.isNotEmpty()) {
                        showNoDataScreen(false)
                        productList.addAll(it.data as ArrayList<Visitable<ProductTypeFactory>>)
                        productNavListAdapter?.removeLoading()
                        product_recyclerview.adapter?.notifyDataSetChanged()
                        isPagingAllowed = true
                    } else {
                        if (productList.isEmpty()) {
                            if (findNavViewModel.mBannedData.isEmpty()) {
                                showNoDataScreen(true)
                            } else {
                                showBannedDataScreen()
                            }
                        }
                    }
                    showProductPriceSection(it.data as ArrayList<ProductsItem>)
                    hideRefreshLayout()
                    reloadFilter()
                }

                is Fail -> {
                    productNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                    if (productList.isEmpty()) {
                        showNoDataScreen(true)
                    }
                    isPagingAllowed = true
                }

            }
        })

        findNavViewModel.mProductCount.observe(this, Observer {
            it?.let {
                setTotalSearchResultCount(it)
                if (!TextUtils.isEmpty(it)) {
                    setQuickFilterAdapter(getString(R.string.result_count_template_text, it))
                } else {
                    setQuickFilterAdapter("")
                }
            }
        })

        findNavViewModel.mQuickFilterModel.observe(this, Observer {
            when (it) {
                is Success -> {
                    loadQuickFilters(it.data as ArrayList)
                }
            }
        })

        findNavViewModel.mDynamicFilterModel.observe(this, Observer {

            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }
            }
        })

        findNavViewModel.mRelatedLinkList.observe(this, Observer {

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

    private fun showProductPriceSection(productList: ArrayList<ProductsItem>) {
        if (productList.isNotEmpty()) {
            showPriceHeader()
            price_list_recyclerview.adapter = FindPriceListAdapter(productList, this)
            price_list_recyclerview.layoutManager = LinearLayoutManager(activity)
        } else {
            hidePriceHeader()
        }
        btn_read_more.setOnClickListener {
            val layoutParams = layout_related.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            layout_related.layoutParams = layoutParams
            btn_read_more.hide()
        }
    }

    private fun hidePriceHeader() {
        price_list_header.hide()
        data_updated_header.hide()
    }

    private fun showPriceHeader() {
        price_list_header.show()
        data_updated_header.show()
        val calendar = Calendar.getInstance()
        val currentMonthYear = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("in", "ID")) + " ${calendar?.get(Calendar.YEAR)}"
        val dateUpdatedText = "Data diperbaharui pada " + calendar.time.toFormattedString("DD/MM/YYYY")
        val priceHeaderText = "Daftar harga $findNavScreenName terbaru $currentMonthYear"
        price_list_header.text = priceHeaderText
        data_updated_header.text = dateUpdatedText
    }

    private fun renderRelatedLink(relatedLinkList: ArrayList<RelatedLinkData>) {
        if (relatedLinkList.isNotEmpty()) {
            related_header.show()
            related_recyclerview.adapter = context?.let { FindRelatedLinkAdapter(it, relatedLinkList) }
            val layoutManager = ChipsLayoutManager.newBuilder(activity)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            related_recyclerview.layoutManager = layoutManager
        } else {
            related_header.hide()
        }
    }

    private fun loadQuickFilters(list: ArrayList<Filter>) {
        quickFilterList.clear()
        quickFilterList.addAll(list)
        quickfilter_recyclerview.adapter?.notifyDataSetChanged()

    }

    private fun init() {
        setUpBreadCrumb()
        fetchProducts(0)
        attachLoadMoreButton()
        setQuickFilterAdapter("")
    }

    private fun setUpBreadCrumb() {
        val breadCrumb = "Tokopedia > $findNavScreenName"
        find_bread_crumb.text = breadCrumb
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
        btn_load_more.setOnClickListener {
            if (isPagingAllowed) {
                incrementPage()
                fetchProducts(getPage())
                productNavListAdapter?.addLoading()
                isPagingAllowed = false
            }
        }
    }

    private fun fetchProducts(start: Int) {
        findNavViewModel.fetchProductList(start, getDepartMentId(), rows, getUniqueId(), getSelectedSort(), getSelectedFilter())
        findNavViewModel.fetchRelatedLinkList(findSearchParam)
    }

    private fun getPage(): Int {
        return pageCount
    }

    private fun incrementPage() {
        pageCount += 1
    }

    private fun setQuickFilterAdapter(productCount: String) {
        quickFilterAdapter = QuickFilterAdapter(quickFilterList, this, productCount)
        quickfilter_recyclerview.adapter = quickFilterAdapter
        quickfilter_recyclerview.layoutManager = LinearLayoutManager(activity,
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
        fetchProducts(0)
    }

    override fun getScreenName(): String {
        return findNavScreenName
    }

    override fun initInjector() {
        component = DaggerFindNavComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
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
        findNavViewModel.fetchQuickFilterList(findSearchParam)
    }

    override fun getDepartMentId(): String {
        return findSearchParam
    }

    override fun onShareButtonClicked() {
        val remoteConfig = FirebaseRemoteConfigImpl(activity)
        // to be verified on what to use in place of hotlist_share_msg
        val hotlistShareMsg = remoteConfig.getString(RemoteConfigKey.HOTLIST_SHARE_MSG)
        val shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.DISCOVERY_TYPE)
                .setName(getString(R.string.message_share_catalog))
                .setTextContent(hotlistShareMsg)
                .setCustMsg(hotlistShareMsg)
                .setUri(FIND_SHARE_URI + findSearchParam)
                .setId(findSearchParam)
                .build()

        shareData.type = LinkerData.HOTLIST_TYPE
        DefaultShare(activity, shareData).show()
    }

    override fun getFilterRequestCode(): Int {
        return REQUEST_ACTIVITY_FILTER_PRODUCT
    }

    override fun getSortRequestCode(): Int {
        return REQUEST_ACTIVITY_SORT_PRODUCT
    }

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        // to add analytics
    }

    override fun onItemClicked(item: ProductsItem, adapterPosition: Int) {
        val intent = getProductIntent(item.id.toString(), item.categoryID.toString())
        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, REQUEST_PRODUCT_ITEM_CLICK)
        }
        if (item.isTopAds) {
            ImpresionTask().execute(item.productClickTrackingUrl)
        }
    }

    override fun onLongClick(item: ProductsItem, adapterPosition: Int) {
    }

    override fun onWishlistButtonClicked(productItem: ProductsItem, position: Int) {
        if (userSession.isLoggedIn) {
            disableWishListButton(productItem.id.toString())
            if (productItem.wishlist) {
                removeWishList(productItem.id.toString(), userSession.userId)
            } else {
                addWishList(productItem.id.toString(), userSession.userId)
            }
        } else {
            launchLoginActivity()
        }
    }

    override fun onChangeList() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeSingleGrid() {
        product_recyclerview.requestLayout()
    }

    override fun onProductImpressed(item: ProductsItem, adapterPosition: Int) {
        // To add analytics
    }

    override fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>, viewedTopAdsList: List<Visitable<Any>>) {
        // To add analytics
    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
        // To be implemented if the data tracker team will provide what values needed to send
    }

    override fun topAdsTrackerUrlTrigger(url: String) {
        // TO be implemented when topAds will be applied for find products, currently they are not applied for find products
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
            layout_no_data.show()
            layout_related.hide()
            btn_load_more.hide()
            txt_no_data_header.text = resources.getText(R.string.category_nav_product_no_data_title)
            txt_no_data_description.text = resources.getText(R.string.category_nav_product_no_data_description)
        } else {
            layout_no_data.hide()
            layout_related.show()
            btn_load_more.show()
        }
    }

    private fun showBannedDataScreen() {
        val bannedProduct = Data()
        bannedProduct.bannedMsgHeader = getString(R.string.banned_product)
        bannedProduct.bannedMessage = findNavViewModel.mBannedData[0]
        bannedProduct.appRedirection = findNavViewModel.mBannedData[1]
        bannedProduct.displayButton = findNavViewModel.mBannedData[1].isNotEmpty()
        bannedProductFoundListener?.onBannedProductFound(bannedProduct)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBannedProductFoundListener) {
            bannedProductFoundListener = context
        } else {
            throw RuntimeException("$context must implement OnBannedProductFoundListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        bannedProductFoundListener = null
    }
}
