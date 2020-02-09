package com.tokopedia.shop.search.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.DISCOVERY_SEARCH
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.ShopPageTrackingShopSearchProduct
import com.tokopedia.shop.common.config.ShopPageConfig
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.SHOP_LOCATION_PLACEHOLDER
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.SHOP_NAME_PLACEHOLDER
import com.tokopedia.shop.product.view.activity.ShopProductListActivity
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.shop.search.di.component.DaggerShopSearchProductComponent
import com.tokopedia.shop.search.di.module.ShopSearchProductModule
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_KEYWORD
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_SHOP_ATTRIBUTION
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_SHOP_INFO_CACHE_MANAGER_ID
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_SORT_ID
import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDataModel
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductFixedResultDataModel
import com.tokopedia.shop.search.view.viewmodel.ShopSearchProductViewModel
import com.tokopedia.shop.search.widget.ShopSearchProductDividerItemDecoration
import com.tokopedia.shop.search.widget.share.ShopShare
import com.tokopedia.shop.search.widget.share.ShopShareData
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_search_product.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.schedule

class ShopSearchProductFragment : BaseSearchListFragment<ShopSearchProductDataModel, ShopSearchProductAdapterTypeFactory>() {

    companion object {
        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"
        private const val REQUEST_CODE_USER_LOGIN_CART = 102
        private const val REQUEST_CODE_SORT = 103
        private const val KEY_SHOP_INFO_CACHE_MANAGER_SAVED_INSTANCE_STATE_ID = "keyShopInfoCacheManagerSavedInstanceStateId"

        fun createInstance(
                keyword: String,
                shopInfoCacheManagerId: String,
                shopAttribution: String
        ): Fragment {
            return ShopSearchProductFragment().apply {
                val bundleData = Bundle()
                bundleData.putString(KEY_SHOP_INFO_CACHE_MANAGER_ID, shopInfoCacheManagerId)
                bundleData.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
                bundleData.putString(KEY_KEYWORD, keyword)
                arguments = bundleData
            }
        }

        fun createInstance(
                keyword: String,
                shopInfoCacheManagerId: String,
                shopAttribution: String,
                sortId: String
        ): Fragment {
            return ShopSearchProductFragment().apply {
                val bundleData = Bundle()
                bundleData.putString(KEY_SHOP_INFO_CACHE_MANAGER_ID, shopInfoCacheManagerId)
                bundleData.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
                bundleData.putString(KEY_KEYWORD, keyword)
                bundleData.putString(KEY_SORT_ID, sortId)
                arguments = bundleData
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var shopPageTrackingShopSearchProduct: ShopPageTrackingShopSearchProduct

    @Inject
    lateinit var userSession : UserSessionInterface

    private lateinit var viewModel: ShopSearchProductViewModel

    private val remoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    private val cartLocalCacheHandler by lazy {
        LocalCacheHandler(context, CART_LOCAL_CACHE_NAME)
    }

    private var shopInfoCacheManagerId: String = ""
    private var shopAttribution: String = ""

    private var shopInfo: ShopInfo? = null

    private var searchQuery = ""
    private var sortValue: String? = ""

    private var viewFragment: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        getArgumentsData(savedInstanceState)
        getShopInfoFromCacheManager()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_search_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFragment = view

        if (isNewShopPageEnabled()) {
            initViewNew(view)
        } else {
            initViewOld(view)
        }

        observeShopSearchProductResult()
    }

    override fun onPause() {
        viewFragment?.run {
            hideKeyboard(this)
        }
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.shopSearchProductResult.removeObservers(this)
        searchInputView.setListener(null)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        shopInfo?.run {
            outState.putString(
                    KEY_SHOP_INFO_CACHE_MANAGER_SAVED_INSTANCE_STATE_ID,
                    saveShopInfoModelToCacheManager(this)
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun loadData(page: Int) {}

    override fun getAdapterTypeFactory(): ShopSearchProductAdapterTypeFactory {
        return ShopSearchProductAdapterTypeFactory()
    }

    override fun onItemClicked(dataModel: ShopSearchProductDataModel) {
        when (dataModel.type) {
            ShopSearchProductDataModel.Type.TYPE_SEARCH_SRP -> {
                shopPageTrackingShopSearchProduct.clickAutocompleteExternalShopPage(
                        SCREEN_SHOP_PAGE,
                        searchQuery,
                        String.format(SRP_SHOPNAME, shopInfo?.shopCore?.name.orEmpty())
                )
                redirectToSearchResultPage()
            }
            ShopSearchProductDataModel.Type.TYPE_PDP -> {
                val model = dataModel as ShopSearchProductDynamicResultDataModel
                shopPageTrackingShopSearchProduct.clickAutocompleteProducts(
                        SCREEN_SHOP_PAGE,
                        searchQuery,
                        model.url
                )
                redirectToProductDetailPage(model.appLink)
            }
            ShopSearchProductDataModel.Type.TYPE_SEARCH_STORE -> {
                shopPageTrackingShopSearchProduct.clickAutocompleteInternalShopPage(
                        SCREEN_SHOP_PAGE,
                        searchQuery,
                        String.format(ETALASE_SHOPNAME, shopInfo?.shopCore?.name.orEmpty())
                )
                redirectToShopProductListPage()
            }
        }
        activity?.finish()
    }

    private fun isNewShopPageEnabled(): Boolean{
        val shopPageConfig = ShopPageConfig(context)
        return shopPageConfig.isNewShopPageEnabled()
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

    override fun onSearchSubmitted(keyword: String) {
        searchQuery = keyword
        if (searchQuery.isNotEmpty()) {
            shopPageTrackingShopSearchProduct.clickManualSearch(
                    SCREEN_SHOP_PAGE,
                    searchQuery,
                    String.format(ETALASE_SHOPNAME, shopInfo?.shopCore?.name.orEmpty())
            )
            redirectToShopProductListPage()
            activity?.finish()
        }
    }

    override fun onSearchTextChanged(text: String) {
        searchQuery = text
        searchProduct()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_shop_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
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
        when (requestCode) {
            REQUEST_CODE_USER_LOGIN_CART -> if (resultCode == Activity.RESULT_OK) {
                goToCart()
            }
            REQUEST_CODE_SORT -> {
                val sortName = data?.getStringExtra(ShopProductSortActivity.SORT_NAME)
                sortName?.let {
                    sortValue = sortName
                    searchQuery = editTextSearchProduct.text.toString()
                    redirectToShopProductListPage()
                    activity?.finish()
                }
            }
        }
    }

    private fun searchProduct() {
        adapter.clearAllElements()
        if (searchQuery.isNotEmpty()) {
            populateFixedSearchResult()
            viewModel.getSearchShopProduct(shopInfo?.shopCore?.shopID.orEmpty(), searchQuery)
        }
    }

    private fun showToasterRequestError(throwable: Throwable, onClickListener: View.OnClickListener) {
        view?.run {
            Toaster.showErrorWithAction(
                    this,
                    ErrorHandler.getErrorMessage(this.context, throwable),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.retry),
                    onClickListener
            )
        }
    }

    private fun redirectToShopProductListPage() {
        shopInfo?.run {
            val intent = ShopProductListActivity.createIntent(
                    context,
                    shopCore.shopID,
                    searchQuery,
                    "",
                    shopAttribution,
                    sortValue
            )
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun redirectToProductDetailPage(appLink: String) {
        RouteManager.route(context, appLink)
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
                    showToasterRequestError(it.throwable, View.OnClickListener {
                        searchProduct()
                    })
                }
            }
        })
    }

    private fun populateFixedSearchResult() {
        if (context == null) {
            return
        }
        val listData = mutableListOf<ShopSearchProductDataModel>().apply {
            add(ShopSearchProductFixedResultDataModel(
                    searchQuery,
                    getString(R.string.shop_search_product_in_this_shop_etalase),
                    ShopSearchProductDataModel.Type.TYPE_SEARCH_STORE
            ))
            add(ShopSearchProductFixedResultDataModel(
                    searchQuery,
                    getString(R.string.shop_search_product_in_tokopedia),
                    ShopSearchProductDataModel.Type.TYPE_SEARCH_SRP
            ))
        }
        renderList(listData, false)
    }

    private fun populateDynamicSearchResult(universeSearchResponse: UniverseSearchResponse) {
        val listData: MutableList<ShopSearchProductDataModel> = arrayListOf()
        universeSearchResponse.universeSearch.data.firstOrNull()?.items?.forEach {
            listData.add(ShopSearchProductDynamicResultDataModel(
                    it.imageUri,
                    it.keyword,
                    it.affiliateUsername,
                    it.appLink,
                    it.url,
                    searchQuery,
                    ShopSearchProductDataModel.Type.TYPE_PDP
            ))
        }
        renderList(listData, false)
    }

    private fun initViewOld(view: View) {
        with(getRecyclerView(view) as VerticalRecyclerView) {
            clearItemDecoration()
            addItemDecoration(ShopSearchProductDividerItemDecoration(
                    view.context.resources.getDrawable(R.drawable.bg_line_separator_thin)
            ))
        }
        initToolbar()
        linearLayoutNewDesign.visibility = View.GONE
        textCancel.visibility = View.GONE
        with(searchInputView) {
            setSearchHint(getString(
                    R.string.shop_product_search_hint_2,
                    MethodChecker.fromHtml(shopInfo?.shopCore?.name.orEmpty()).toString()
            ))
            searchText = searchQuery
            searchTextView.requestFocus()
            showKeyboard()
        }
    }

    private fun initViewNew(view: View) {
        showSortButton()
        with(getRecyclerView(view) as VerticalRecyclerView) {
            clearItemDecoration()
            addItemDecoration(ShopSearchProductDividerItemDecoration(
                    view.context.resources.getDrawable(R.drawable.bg_line_separator_thin)
            ))
        }
        searchInputView.visibility = View.GONE
        textCancel.setOnClickListener {
            onClickCancel()
        }
        with(editTextSearchProduct) {
            hint = getString(
                    R.string.shop_product_search_hint_2,
                    MethodChecker.fromHtml(shopInfo?.shopCore?.name.orEmpty()).toString()
            )
            addTextChangedListener(getSearchTextWatcher())
            setOnEditorActionListener(getSearchEditorActionListener())
            requestFocus()
            showKeyboard()
            setText(searchQuery)
            setSelection(searchQuery.length)
        }
        shopPageMainSortIcon.setOnClickListener {
            onClickSort()
        }
        image_view_clear_button.setOnClickListener {
            editTextSearchProduct.text.clear()
        }
    }

    private fun getSearchTextWatcher(): TextWatcher {
        return object : TextWatcher {
            var timer: Timer? = Timer()

            override fun afterTextChanged(s: Editable) {
                runTimer(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                timer?.cancel()
                if (TextUtils.isEmpty(text.toString())) {
                    showSortButton()
                } else {
                    showClearButton()
                }
            }

            private fun runTimer(text: String) {
                timer = Timer()
                timer?.schedule(delayTextChanged) {
                    updateListener(text)
                }
            }

            private fun updateListener(text: String) {
                val myRunnable = Runnable { onSearchTextChanged(text) }
                Handler(context?.mainLooper).post(myRunnable)
            }
        }
    }

    private fun showClearButton() {
        shopPageMainSortIcon.hide()
        image_view_clear_button.show()
    }

    private fun showSortButton() {
        shopPageMainSortIcon.show()
        image_view_clear_button.hide()
    }

    private fun getSearchEditorActionListener(): TextView.OnEditorActionListener {
        return object : TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchSubmitted(textView?.text.toString())
                    return true
                }
                return false
            }

        }
    }

    private fun showKeyboard() {
        context?.run {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

    private fun hideKeyboard(view: View) {
        context?.run {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun getShopInfoFromCacheManager() {
        shopInfo = context?.run {
            SaveInstanceCacheManager(this, shopInfoCacheManagerId).run {
                get(ShopInfo.TAG, ShopInfo::class.java)
            }
        }
    }

    private fun saveShopInfoModelToCacheManager(shopInfo: ShopInfo): String? {
        return context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true)
            cacheManager.put(ShopInfo.TAG, shopInfo, TimeUnit.DAYS.toMillis(7))
            cacheManager.id
        }
    }

    private fun getArgumentsData(savedInstanceState: Bundle?) {
        arguments?.run {
            shopInfoCacheManagerId = savedInstanceState?.run {
                getString(KEY_SHOP_INFO_CACHE_MANAGER_SAVED_INSTANCE_STATE_ID).orEmpty()
            } ?: getString(KEY_SHOP_INFO_CACHE_MANAGER_ID).orEmpty()
            shopAttribution = getString(KEY_SHOP_ATTRIBUTION).orEmpty()
            searchQuery = getString(KEY_KEYWORD).orEmpty()
            sortValue = getString(KEY_SORT_ID).orEmpty()
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
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun onClickSort() {
        val intent = ShopProductSortActivity.createIntent(activity, sortValue)
        startActivityForResult(intent, REQUEST_CODE_SORT)
    }

    private fun onClickCancel() {
        activity?.finish()
    }

    private fun onShareShop() {
        shopInfo?.run {
            shopPageTrackingShopSearchProduct.clickShareButton(SCREEN_SEARCH_BAR, searchQuery)
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
            shopPageTrackingShopSearchProduct.clickCartButton(SCREEN_SEARCH_BAR, searchQuery)
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