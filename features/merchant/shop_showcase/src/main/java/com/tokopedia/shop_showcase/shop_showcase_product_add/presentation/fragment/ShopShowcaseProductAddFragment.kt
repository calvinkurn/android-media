package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.ImageAssets
import com.tokopedia.shop_showcase.common.ShopShowcaseEditParam
import com.tokopedia.shop_showcase.common.ShopShowcaseTracking
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment.ShopShowcaseAddFragment
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.component.DaggerShowcaseProductAddComponent
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.component.ShowcaseProductAddComponent
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.module.ShowcaseProductAddModule
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.module.ShowcaseProductAddUseCaseModule
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.adapter.ShowcaseProductListAdapter
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewmodel.ShowcaseProductAddViewModel
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_shop_showcase_product_add.view.*
import javax.inject.Inject

/**
 * @author by Rafli Syam on 2020-03-09
 */
class ShopShowcaseProductAddFragment : BaseDaggerFragment(),
        HasComponent<ShowcaseProductAddComponent>, ShopShowcaseProductAddListener {

    companion object {
        @JvmStatic
        fun createInstance(extraActionEdit: Boolean): ShopShowcaseProductAddFragment {
            val fragment = ShopShowcaseProductAddFragment()
            val extraData = Bundle()
            extraData.putBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT, extraActionEdit)
            fragment.arguments = extraData
            return fragment
        }

        const val SHOWCASE_PRODUCT_LIST = "product_list"
        const val SHOWCASE_DELETED_LIST = "deleted_list"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracking: ShopShowcaseTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    private var productListFilter = GetProductListFilter()
    private var isLoadNextPage: Boolean = false
    private var showcaseId: String? = ""
    private var showcaseProductListAdapter: ShowcaseProductListAdapter? = null
    private var isActionEdit: Boolean = false

    private val shopId: String by lazy {
        userSession.shopId
    }

    private val shopType: String by lazy {
        tracking.getShopType(userSession)
    }

    override fun onCLickProductCardTracking() {
        tracking.addShowcaseProductCardClick(shopId, shopType, isActionEdit)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private var productListFirstItem: Int = linearLayoutManager.findFirstCompletelyVisibleItemPosition()

    private val buttonBackToTop: FloatingButtonUnify? by lazy {
        view?.findViewById<FloatingButtonUnify>(R.id.btn_back_to_top)?.apply {
            circleMainMenu.run {
                setOnClickListener {
                    recyclerViewProductList?.smoothScrollToPosition(0)
                    hide()
                }
            }
        }
    }

    private val recyclerViewProductList: RecyclerView? by lazy {
        view?.findViewById<RecyclerView>(R.id.rv_showcase_add_product)
    }

    private val headerUnify: HeaderUnify? by lazy {
        view?.findViewById<HeaderUnify>(R.id.add_product_showcase_toolbar)?.apply {
            setNavigationOnClickListener {
                tracking.addShowcaseProductClickBackButton(shopId, shopType, isActionEdit)
                activity?.onBackPressed()
            }
        }
    }

    private val headerLayout: CardView? by lazy {
        view?.findViewById<CardView>(R.id.header_layout)
    }

    /**
     * Setup Scroll Listener for endless recycler view load.
     */
    private val scrollListener by lazy {
        object : EndlessRecyclerViewScrollListener(linearLayoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                productListFilter.page = (page+1)
                loadMoreProduct(productListFilter)
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    buttonBackToTop?.hide()
                    headerLayout?.cardElevation = 0f
                }
                else {
                    buttonBackToTop?.show()
                    headerLayout?.cardElevation = 16.0f
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Handler().postDelayed({
                        slideUpCounter()
                    }, 1800)
                }
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val currentFirstVisible = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if(currentFirstVisible > productListFirstItem)
                        slideDownCounter()
                    else
                        slideDownCounter()

                    productListFirstItem = currentFirstVisible
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

        }
    }

    private val showcaseProductAddViewModel: ShowcaseProductAddViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShowcaseProductAddViewModel::class.java)
    }

    private val productSelectedCounter: CardView? by lazy {
        view?.findViewById<CardView>(R.id.product_choosen_counter)
    }

    private val emptyState: EmptyStateUnify? by lazy {
        view?.findViewById<EmptyStateUnify>(R.id.empty_state_product_search)
    }

    private val searchBar: SearchBarUnify? by lazy {
        view?.findViewById<SearchBarUnify>(R.id.searchbar_add_product)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isActionEdit = it.getBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT)
            if(isActionEdit) {
                showcaseId = activity?.intent?.getStringExtra(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_showcase_product_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, this)
        initListener()
        tracking.sendScreenNameAddShowcaseProduct()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): ShowcaseProductAddComponent? {
        return activity?.run {
            DaggerShowcaseProductAddComponent
                    .builder()
                    .showcaseProductAddModule(ShowcaseProductAddModule())
                    .showcaseProductAddUseCaseModule(ShowcaseProductAddUseCaseModule())
                    .shopShowcaseComponent(ShopShowcaseInstance.getComponent(application))
                    .build()
        }
    }

    private fun initView(view: View?, listener: ShopShowcaseProductAddListener) {
        emptyState?.visible()
        initRecyclerView(view, listener)
        getProductList(productListFilter)

        observeProductListData()
        observeLoadingState()
        observeFetchingState(view)

        setSearchListener(searchBar)
    }

    private fun initListener() {
        headerUnify?.actionTextView?.setOnClickListener {
            tracking.addShowcaseProductClickSaveButton(shopId, shopType, isActionEdit)
            goBackToPreviewShowcase()
        }
    }

    private fun goBackToPreviewShowcase() {
        val previewShowcaseIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_ADD)
        previewShowcaseIntent.putParcelableArrayListExtra(SHOWCASE_PRODUCT_LIST, showcaseProductListAdapter?.getSelectedProduct())
        previewShowcaseIntent.putParcelableArrayListExtra(SHOWCASE_DELETED_LIST, showcaseProductListAdapter?.getDeletedProduct())
        activity?.setResult(Activity.RESULT_OK, previewShowcaseIntent)
        activity?.finish()
    }

    private fun setSearchListener(searchBar: SearchBarUnify?) {

        val searchTextField = searchBar?.searchBarTextField
        val searchClearButton = searchBar?.searchBarIcon

        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, even: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    productListFilter.setDefaultFilter()
                    productListFilter.fkeyword = searchTextField.text.toString()
                    getProductList(productListFilter)
                    return true
                }
                return false
            }
        })

        searchTextField?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus) tracking.addShowcaseProductClickSearchbar(shopId, shopType, isActionEdit)
        }

        searchClearButton?.setOnClickListener {
            productListFilter.setDefaultFilter()
            scrollListener.resetState()
            searchTextField?.text?.clear()
            getProductList(productListFilter)
        }
    }

    private fun initRecyclerView(view: View?, listener: ShopShowcaseProductAddListener) {
        view?.run {
            recyclerViewProductList?.apply {
                setHasFixedSize(true)
                layoutManager = linearLayoutManager
                showcaseProductListAdapter = ShowcaseProductListAdapter(context, view, listener)
                adapter = showcaseProductListAdapter
                addOnScrollListener(scrollListener)
            }
        }
    }

    private fun showEmptyViewProductSearch(state: Boolean) {
        emptyState?.setImageUrl(ImageAssets.SEARCH_SHOWCASE_NOT_FOUND)
        if(state) {
            recyclerViewProductList?.gone()
            emptyState?.visible()
        }
        else {
            recyclerViewProductList?.visible()
            emptyState?.gone()
        }
    }

    private fun observeProductListData() {
        observe(showcaseProductAddViewModel.productList) {
            when(it) {
                is Success -> {

                    val productList: MutableList<ShowcaseProduct> = it.data.toMutableList()
                    val tempSelectedProductFromAdapter = showcaseProductListAdapter?.getSelectedProduct()
                    val selectedProductList = activity?.intent?.getParcelableArrayListExtra<BaseShowcaseProduct>(ShopShowcaseAddFragment.SELECTED_SHOWCASE_PRODUCT)?.filterIsInstance<ShowcaseProduct>()
                    val excludedProduct = activity?.intent?.getParcelableArrayListExtra<ShowcaseProduct>(ShopShowcaseAddFragment.EXCLUDED_SHOWCASE_PRODUCT)

                    if(productList.size == 0 && !isLoadNextPage) {
                        showEmptyViewProductSearch(true)
                        showcaseProductListAdapter?.updateShopProductList(isLoadNextPage, productList, excludedProduct!!, selectedProductList!!)
                    } else {
                        showEmptyViewProductSearch(false)
                        productList.forEach { showcaseProduct->

                            // check selected product that already saved before
                            selectedProductList?.forEach { selectedProduct ->
                                if(selectedProduct.productId == showcaseProduct.productId)
                                    showcaseProduct.ishighlighted = true
                            }

                            // check unsaved selected product
                            tempSelectedProductFromAdapter?.forEach { tempSelectedProduct ->
                                if(tempSelectedProduct.productId == showcaseProduct.productId)
                                    showcaseProduct.ishighlighted = true
                            }
                        }
                        showcaseProductListAdapter?.updateShopProductList(isLoadNextPage, productList, excludedProduct!!, selectedProductList!!)
                        if (isLoadNextPage)
                            scrollListener.updateStateAfterGetData()
                    }
                }
                is Fail -> {
                    println(it)
                    // Do error handling here
                }
            }
        }
    }

    private fun observeLoadingState() {
        observe(showcaseProductAddViewModel.loadingState) {
            if(it) showLoadingProgress()
            else hideLoadingProgress()
        }
    }

    private fun observeFetchingState(view: View?) {
        observe(showcaseProductAddViewModel.fetchingState) {
            if(it) showFetchingProgress(view)
            else hideFetchingProgress(view)
        }
    }

    private fun getProductList(filter: GetProductListFilter) {
        isLoadNextPage = false
        searchBar?.clearFocus()
        productListFilter.fmenuExclude = showcaseId
        showcaseProductAddViewModel.getProductList(filter, false)
    }

    private fun loadMoreProduct(filter: GetProductListFilter) {
        isLoadNextPage = true
        showcaseProductAddViewModel.getProductList(filter, true)
    }

    private fun showLoadingProgress() {
        showcaseProductListAdapter?.showLoadingProgress()
    }

    private fun showFetchingProgress(view: View?) {
        emptyState?.invisible()
        view?.loaderUnify?.visible()
        view?.rv_showcase_add_product?.gone()
    }

    private fun hideFetchingProgress(view: View?) {
        view?.loaderUnify?.gone()
        view?.rv_showcase_add_product?.visible()
    }

    private fun hideLoadingProgress() {
        showcaseProductListAdapter?.hideLoadingProgress()
    }

    private fun slideDownCounter() {
        productSelectedCounter?.animate()?.translationY(250f)
        if(buttonBackToTop?.circleMainMenu?.visibility == View.VISIBLE)
            buttonBackToTop?.circleMainMenu?.hide()
    }

    private fun slideUpCounter() {
        productSelectedCounter?.animate()?.translationY(0f)
        if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0)
            buttonBackToTop?.circleMainMenu?.show()
    }

}

interface ShopShowcaseProductAddListener {
    fun onCLickProductCardTracking()
}