package com.tokopedia.shop.search.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst.DISCOVERY_SEARCH
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.OldShopPageTrackingConstant.SCREEN_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingShopSearchProduct
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG
//import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_PRODUCT_SEARCH_BUYER_FLOW_TAG
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListViewModel
import com.tokopedia.shop.search.data.model.UniverseSearchResponse
import com.tokopedia.shop.search.di.component.DaggerShopSearchProductComponent
import com.tokopedia.shop.search.di.module.ShopSearchProductModule
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_KEYWORD
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_SHOP_ATTRIBUTION
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_SHOP_REF
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.KEY_SORT_ID
import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDataModel
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDynamicResultDataModel
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductFixedResultDataModel
import com.tokopedia.shop.search.view.viewmodel.ShopSearchProductViewModel
import com.tokopedia.shop.search.widget.ShopSearchProductDividerItemDecoration
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_search_product.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.schedule

class ShopSearchProductFragment : BaseListFragment<ShopSearchProductDataModel, ShopSearchProductAdapterTypeFactory>() {

    companion object {
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_SHOP_NAME = "SHOP_NAME"
        private const val KEY_IS_OFFICIAL = "IS_OFFICIAL"
        private const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        val SEARCH_SUBMIT_RESULT_REDIRECTION = "SEARCH_SUBMIT_RESULT_REDIRECTION"
        val ETALASE_CLICK_RESULT_REDIRECTION = "ETALASE_CLICK_RESULT_REDIRECTION"
        private val DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300)

        fun createInstance(
                shopId: String,
                shopName: String,
                isOfficial: Boolean,
                isGoldMerchant: Boolean,
                keyword: String,
                shopAttribution: String,
                shopRef: String
        ): Fragment {
            return ShopSearchProductFragment().apply {
                val bundleData = Bundle()
                bundleData.putString(KEY_SHOP_ID, shopId)
                bundleData.putString(KEY_SHOP_NAME, shopName)
                bundleData.putBoolean(KEY_IS_OFFICIAL, isOfficial)
                bundleData.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
                bundleData.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
                bundleData.putString(KEY_KEYWORD, keyword)
                bundleData.putString(KEY_SHOP_REF, shopRef)
                arguments = bundleData
            }
        }

        fun createInstance(
                shopId: String,
                shopName: String,
                isOfficial: Boolean,
                isGoldMerchant: Boolean,
                keyword: String,
                shopAttribution: String,
                sortId: String,
                shopRef: String
        ): Fragment {
            return createInstance(shopId, shopName, isOfficial, isGoldMerchant, keyword, shopAttribution, shopRef).apply {
                val bundleData = arguments
                bundleData?.apply {
                    putString(KEY_SORT_ID, sortId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var shopPageTrackingShopSearchProduct: ShopPageTrackingShopSearchProduct

    @Inject
    lateinit var userSession: UserSessionInterface

    private var shopId: String = ""
    private var shopName: String = ""
    private var isOfficial: Boolean = false
    private var isGold: Boolean = false
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficial, isGold)
    }

    private lateinit var viewModel: ShopSearchProductViewModel

    private val isMyShop: Boolean
        get() = if (::viewModel.isInitialized) {
            viewModel.isMyShop(shopId)
        } else false

    private val userId: String
        get() = if (::viewModel.isInitialized) {
            viewModel.userId
        } else {
            ""
        }
    private val remoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }
    private var shopAttribution: String = ""
    private var searchQuery = ""
    private var shopRef: String = ""
    private var viewFragment: View? = null

    private var productListData: MutableList<ShopSearchProductDataModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        getArgumentsData()
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficial, isGold)
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
        initViewNew(view)
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
        super.onDestroy()
    }

    override fun loadData(page: Int) {}

    override fun getAdapterTypeFactory(): ShopSearchProductAdapterTypeFactory {
        return ShopSearchProductAdapterTypeFactory()
    }

    override fun onItemClicked(dataModel: ShopSearchProductDataModel) {
        when (dataModel.type) {
            ShopSearchProductDataModel.Type.TYPE_SEARCH_SRP -> {
                shopPageTrackingShopSearchProduct.clickAutocompleteExternalShopPage(isMyShop, searchQuery, customDimensionShopPage)
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
                redirectToShopProductListPage(ETALASE_CLICK_RESULT_REDIRECTION)
            }
        }
        activity?.finish()
    }

    override fun getScreenName(): String {
        return ShopSearchProductFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            DaggerShopSearchProductComponent
                    .builder()
                    .shopSearchProductModule(ShopSearchProductModule())
                    .shopComponent(getComponent(ShopComponent::class.java))
                    .build()
                    .inject(this)
        }
    }

    fun onSearchSubmitted(keyword: String) {
        searchQuery = keyword
        if (searchQuery.isNotEmpty()) {
            redirectToShopProductListPage(SEARCH_SUBMIT_RESULT_REDIRECTION)
            activity?.finish()
        }
    }

    fun onSearchTextChanged(text: String) {
        searchQuery = text
        searchProduct()
    }

    private fun searchProduct() {
        adapter.clearAllElements()
        if (searchQuery.isNotEmpty()) {
            populateFixedSearchResult()
            viewModel.getSearchShopProduct(shopId, searchQuery)
        }
    }

    private fun showToasterRequestError(throwable: Throwable, onClickListener: View.OnClickListener) {
        view?.run {
            Toaster.showErrorWithAction(
                    this,
                    ErrorHandler.getErrorMessage(this.context, throwable),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(com.tokopedia.merchantvoucher.R.string.retry),
                    onClickListener
            )
        }
    }

    private fun redirectToShopProductListPage(sourceOfRedirection: String) {
        val intent = ShopProductListResultActivity.createIntentWithSourceRedirection(
                context,
                shopId,
                searchQuery,
                "",
                shopAttribution,
                shopRef,
                sourceOfRedirection
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
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
        viewModel.shopSearchProductResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    populateDynamicSearchResult(it.data)
                }
                is Fail -> {
                    val throwable = it.throwable
                    if (!ShopUtil.isExceptionIgnored(throwable)) {
                        ShopUtil.logShopPageP1BuyerFlowAlerting(
                                SHOP_PAGE_BUYER_FLOW_TAG,
                                this::observeShopSearchProductResult.name,
                                ShopSearchProductViewModel::shopSearchProductResult.name,
                                userId,
                                shopId,
                                shopName,
                                ErrorHandler.getErrorMessage(context, throwable),
                                Log.getStackTraceString(throwable)
                        )
                    }
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
                    getString(R.string.shop_search_product_in_shop_name, shopName),
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
        productListData.clear()
        universeSearchResponse.universeSearch.data.firstOrNull()?.items?.forEach {
            productListData.add(ShopSearchProductDynamicResultDataModel(
                    it.imageUri,
                    it.keyword,
                    it.affiliateUsername,
                    it.appLink,
                    it.url,
                    searchQuery,
                    ShopSearchProductDataModel.Type.TYPE_PDP
            ))
        }
        renderList(productListData, false)
    }

    private fun initViewNew(view: View) {
        hideClearButton()
        (getRecyclerView(view) as? VerticalRecyclerView)?.run {
            clearItemDecoration()
            addItemDecoration(ShopSearchProductDividerItemDecoration(
                    view.context.resources.getDrawable(R.drawable.shop_page_bg_line_separator_thin)
            ))
        }
        textCancel.setOnClickListener {
            onClickCancel()
        }
        with(editTextSearchProduct) {
            hint = getString(
                    R.string.shop_product_search_hint_2,
                    MethodChecker.fromHtml(shopName).toString()
            )
            addTextChangedListener(getSearchTextWatcher())
            setOnEditorActionListener(getSearchEditorActionListener())
            requestFocus()
            showKeyboard()
            setText(searchQuery)
            setSelection(searchQuery.length)
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
                    hideClearButton()
                } else {
                    showClearButton()
                }
            }

            private fun runTimer(text: String) {
                timer = Timer()
                timer?.schedule(DEFAULT_DELAY_TEXT_CHANGED) {
                    updateListener(text)
                }
            }

            private fun updateListener(text: String) {
                val myRunnable = Runnable { onSearchTextChanged(text) }
                context?.mainLooper?.let {
                    Handler(it).post(myRunnable)
                }
            }
        }
    }

    private fun showClearButton() {
        image_view_clear_button.show()
    }

    private fun hideClearButton() {
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

    private fun getArgumentsData() {
        arguments?.run {
            shopId = getString(KEY_SHOP_ID).orEmpty()
            shopName = getString(KEY_SHOP_NAME).orEmpty()
            isOfficial = getBoolean(KEY_IS_OFFICIAL, false)
            isGold = getBoolean(KEY_IS_GOLD_MERCHANT, false)
            shopAttribution = getString(KEY_SHOP_ATTRIBUTION).orEmpty()
            searchQuery = getString(KEY_KEYWORD).orEmpty()
            shopRef = getString(KEY_SHOP_REF).orEmpty()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ShopSearchProductViewModel::class.java)
    }

    private fun onClickCancel() {
        activity?.finish()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }
}