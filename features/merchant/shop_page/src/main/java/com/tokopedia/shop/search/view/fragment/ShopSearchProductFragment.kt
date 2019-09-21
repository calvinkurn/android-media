package com.tokopedia.shop.search.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.DISCOVERY_SEARCH
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.page.view.activity.ShopPageActivity.Companion.SHOP_LOCATION_PLACEHOLDER
import com.tokopedia.shop.page.view.activity.ShopPageActivity.Companion.SHOP_NAME_PLACEHOLDER
import com.tokopedia.shop.product.view.activity.ShopProductListActivity
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.shop.search.di.component.DaggerShopSearchProductComponent
import com.tokopedia.shop.search.di.module.ShopSearchProductModule
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.SHOP_INFO_CACHE_MANAGER_ID_KEY
import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDataModel
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductFixedResultDataModel
import com.tokopedia.shop.search.view.viewmodel.ShopSearchProductViewModel
import com.tokopedia.shop.search.widget.ShopSearchProductDividerItemDecoration
import com.tokopedia.shop.search.widget.share.ShopShare
import com.tokopedia.shop.search.widget.share.ShopShareData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_shop_search_product.*
import javax.inject.Inject

class ShopSearchProductFragment : BaseSearchListFragment<ShopSearchProductDataModel, ShopSearchProductAdapterTypeFactory>() {

    companion object {
        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"
        private const val REQUEST_CODE_USER_LOGIN_CART = 102

        fun createInstance(shopInfoCacheManagerId: String): Fragment {
            return ShopSearchProductFragment().apply {
                val bundleData = Bundle()
                bundleData.putString(SHOP_INFO_CACHE_MANAGER_ID_KEY, shopInfoCacheManagerId)
                arguments = bundleData
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShopSearchProductViewModel

    private val remoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    private val cartLocalCacheHandler by lazy {
        LocalCacheHandler(context, CART_LOCAL_CACHE_NAME)
    }

    private var shopInfoCacheManagerId: String = ""

    private var shopInfo: ShopInfo? = null

    private var searchQuery = ""

    override fun getAdapterTypeFactory(): ShopSearchProductAdapterTypeFactory {
        return ShopSearchProductAdapterTypeFactory()
    }

    override fun onItemClicked(dataModel: ShopSearchProductDataModel) {
        when(dataModel.type){
            ShopSearchProductDataModel.Type.TYPE_SEARCH_SRP ->{
                redirectToSearchResultPage()
            }
            ShopSearchProductDataModel.Type.TYPE_PDP -> {
                redirectToProductDetailPage(
                        (dataModel as ShopSearchProductDynamicResultDataModel).appLink
                )
            }
            ShopSearchProductDataModel.Type.TYPE_SEARCH_STORE -> {
                redirectToShopProductListPage()
            }
        }
    }

    override fun getScreenName(): String {
        return ShopSearchProductFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerShopSearchProductComponent
                .builder()
                .shopSearchProductModule(ShopSearchProductModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun onSearchSubmitted(text: String) {
        shopInfo?.run {
            startActivity(ShopProductListActivity.createIntent(context,
                    shopCore.shopID, searchQuery, "", ""))
        }
    }

    override fun loadData(page: Int) {
        adapter.clearAllElements()
    }

    override fun onSearchTextChanged(text: String) {
        searchQuery = text
        adapter.clearAllElements()
        if (searchQuery.isNotEmpty()) {
            populateFixedSearchResult()
            viewModel.submitSearchQuery(text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        getArgumentsData()
        getShopInfoFromCacheManager()
        observeShopSearchProductResult()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_search_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun redirectToShopProductListPage() {
        shopInfo?.run {
            startActivity(ShopProductListActivity.createIntent(context,
                    shopCore.shopID, searchQuery, "", ""))
        }
    }

    private fun redirectToProductDetailPage(appLink : String) {
        RouteManager.route(context,appLink)
    }

    private fun redirectToSearchResultPage() {
        RouteManager.route(
                context,
                "$DISCOVERY_SEARCH?q=$searchQuery"
        )
    }

    private fun observeShopSearchProductResult() {
        viewModel.shopSearchProductResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    populateDynamicSearchResult(it.data)
                }
                is Fail -> {
                }
            }
        })
    }

    private fun populateFixedSearchResult() {
        with(adapter) {
            addElement(ShopSearchProductFixedResultDataModel(
                    searchQuery,
                    "di Etalase Toko ini",
                    ShopSearchProductDataModel.Type.TYPE_SEARCH_STORE
            ))
            addElement(ShopSearchProductFixedResultDataModel(
                    searchQuery,
                    "di Tokopedia",
                    ShopSearchProductDataModel.Type.TYPE_SEARCH_SRP
            ))
        }
    }

    private fun populateDynamicSearchResult(universeSearchResponse: UniverseSearchResponse) {
        universeSearchResponse.universeSearch.data[0].items.forEach {
            with(adapter) {
                addElement(ShopSearchProductDynamicResultDataModel(
                        it.imageUri,
                        it.keyword,
                        it.affiliateUsername,
                        it.appLink,
                        searchQuery,
                        ShopSearchProductDataModel.Type.TYPE_PDP
                ))
            }
        }
    }

    private fun initView(view: View) {
        with(getRecyclerView(view) as VerticalRecyclerView) {
            clearItemDecoration()
            addItemDecoration(ShopSearchProductDividerItemDecoration(
                    view.context.resources.getDrawable(R.drawable.bg_line_separator_thin)
            ))
        }
        initToolbar()
        shopInfo?.run {
            searchInputView.setSearchHint(getString(
                    R.string.shop_product_search_hint_2,
                    MethodChecker.fromHtml(shopCore.name).toString()
            ))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_shop_page, menu)
        super.onCreateOptionsMenu(menu, inflater)

//        this.menu = menu
//        initToolBarMethod()

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val userSession = UserSession(context)
        if (GlobalConfig.isSellerApp() || !remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CART_ICON_IN_SHOP, true)) {
            menu.removeItem(R.id.action_cart)
        } else if (userSession.isLoggedIn) {
            showCartBadge(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            onShareShop()
        } else if (item.itemId == R.id.action_cart) {
            onClickCart()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
            if (resultCode == Activity.RESULT_OK) {
                goToCart()
            }
        }
    }

    private fun getShopInfoFromCacheManager() {
        shopInfo = context?.run {
            SaveInstanceCacheManager(this, shopInfoCacheManagerId).run {
                get(ShopInfo.TAG, ShopInfo::class.java)
            }
        }
    }

    private fun getArgumentsData() {
        arguments?.run {
            shopInfoCacheManagerId = getString(SHOP_INFO_CACHE_MANAGER_ID_KEY).orEmpty()
        }
    }

    private fun showCartBadge(menu: Menu) {
        context?.run {
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_cart_menu)
            if (drawable is LayerDrawable) {
                val countDrawable = CountDrawable(this)
                val cartCount = cartLocalCacheHandler.getInt(TOTAL_CART_CACHE_KEY, 0)
                countDrawable.setCount(cartCount.toString())
                drawable.mutate()
                drawable.setDrawableByLayerId(R.id.ic_cart_count, countDrawable)
                menu.findItem(R.id.action_cart)?.icon = drawable
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ShopSearchProductViewModel::class.java)
    }

    private fun initToolbar() {
        activity?.run {
            (this as AppCompatActivity).setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun onShareShop() {
        shopInfo?.run {
            //            shopPageTracking.clickShareButton(shopViewModel.isMyShop(shopCore.shopID),
//                    CustomDimensionShopPage.create(shopCore.shopID,
//                            goldOS.isOfficial == 1,
//                            goldOS.isGold == 1))
            var shopShareMsg: String = remoteConfig.getString(RemoteConfigKey.SHOP_SHARE_MSG)
            shopShareMsg = if (shopShareMsg.isNotEmpty()) {
                FindAndReplaceHelper.findAndReplacePlaceHolders(
                        shopShareMsg,
                        SHOP_NAME_PLACEHOLDER,
                        MethodChecker.fromHtml(shopCore.name).toString(),
                        SHOP_LOCATION_PLACEHOLDER, location
                )
            } else {
                getString(
                        R.string.shop_label_share_formatted,
                        MethodChecker.fromHtml(shopCore.name).toString(),
                        location
                )
            }
            activity?.run {
                ShopShare(this).share(ShopShareData().apply {
                    shopId = shopCore.shopID
                    shopUrl = shopCore.url
                    shopShareLabel = shopShareMsg
                })
            }
        }

    }

    private fun onClickCart() {
        shopInfo?.run {
            //            shopPageTracking.clickCartButton(shopViewModel.isMyShop(shopCore.shopID),
//                    CustomDimensionShopPage.create(shopCore.shopID,
//                            goldOS.isOfficial == 1,
//                            goldOS.isGold == 1))
            goToCart()
        }
    }

    private fun goToCart() {
        if (viewModel.isLoggedIn()) {
            startActivity(RouteManager.getIntent(context, ApplinkConst.CART))
        } else {
            startActivityForResult(
                    RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    REQUEST_CODE_USER_LOGIN_CART
            )
        }
    }

}