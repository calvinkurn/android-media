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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.*
import com.tokopedia.shop_showcase.databinding.FragmentShopShowcaseProductAddBinding
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment.ShopShowcaseAddFragment
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.component.DaggerShowcaseProductAddComponent
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.component.ShowcaseProductAddComponent
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.module.ShowcaseProductAddModule
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.module.ShowcaseProductAddUseCaseModule
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.adapter.ShowcaseProductListAdapter
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.listener.ShopShowcaseProductAddListener
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewmodel.ShowcaseProductAddViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
        private const val SLIDE_UP_COUNTER_ANIMATION_DELAY_MILLIS = 1800L
        private const val SLIDE_DOWN_COUNTER_TRANSLATE_Y = 250f
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracking: ShopShowcaseTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    private var _binding: FragmentShopShowcaseProductAddBinding? = null
    private var productSelectedCounter: CardView? = null
    private var emptyState: EmptyStateUnify? = null
    private var searchBar: SearchBarUnify? = null
    private var productCounterChoosenImage: ImageUnify? = null
    private var productChoosenContainer: CardView? = null
    private var productChoosenCounterText: Typography? = null
    private var buttonBackToTop: FloatingButtonUnify? = null
    private var headerUnify: HeaderUnify? = null
    private var headerLayoutShowcaseProductAdd: CardView? = null
    private var loader: LoaderUnify? = null
    private var recyclerViewShowcaseAddProduct: RecyclerView? = null
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
                    headerLayoutShowcaseProductAdd?.cardElevation = CARD_HEADER_NO_ELEVATION
                }
                else {
                    buttonBackToTop?.show()
                    headerLayoutShowcaseProductAdd?.cardElevation = CARD_HEADER_ELEVATION
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Handler().postDelayed({
                        slideUpCounter()
                    }, SLIDE_UP_COUNTER_ANIMATION_DELAY_MILLIS)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isActionEdit = it.getBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT)
            if(isActionEdit) {
                showcaseId = activity?.intent?.getStringExtra(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentShopShowcaseProductAddBinding.inflate(inflater, container, false).apply {
            productChoosenCounterText = totalSelectedProductCounter
            productChoosenContainer = productChoosenCounter
            productCounterChoosenImage = productChoosenImage
            searchBar = searchbarAddProduct
            emptyState = emptyStateProductSearch
            productSelectedCounter = productChoosenCounter
            recyclerViewShowcaseAddProduct = rvShowcaseAddProduct
            loader = loaderUnify
            headerLayoutShowcaseProductAdd = headerLayout

            buttonBackToTop = btnBackToTop.apply {
                circleMainMenu.run {
                    setOnClickListener {
                        recyclerViewShowcaseAddProduct?.smoothScrollToPosition(0)
                        hide()
                    }
                }
            }

            headerUnify = addProductShowcaseToolbar.apply {
                setNavigationOnClickListener {
                    tracking.addShowcaseProductClickBackButton(shopId, shopType, isActionEdit)
                    activity?.onBackPressed()
                }
            }
        }

        _binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, this)
        initListener()
        tracking.sendScreenNameAddShowcaseProduct()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    override fun showProductCounter(totalSelectedProduct: Int, excludedProduct: ArrayList<ShowcaseProduct>, selectedProduct: ArrayList<ShowcaseProduct>) {
        if(totalSelectedProduct > 0) {
            var idx = 0
            val item = if(excludedProduct.size > 0) {
                // if in edit mode, find first appended product to show product image on counter
                for(i in 0 until selectedProduct.size) {
                    if(selectedProduct[i].isNewAppended) {
                        idx = i
                        break
                    }
                }
                selectedProduct[idx]
            } else {
                // if in create mode, just use first selected product image
                selectedProduct[idx]
            }

            productCounterChoosenImage?.loadImage(item.productImageUrl)
            productChoosenContainer?.visibility = View.VISIBLE
            productChoosenCounterText?.text = context?.resources?.getString(
                    R.string.chosen_product_counter_text,
                    totalSelectedProduct.toString()
            )
        } else {
            productChoosenContainer?.visibility = View.GONE
        }
    }

    private fun initView(view: View?, listener: ShopShowcaseProductAddListener) {
        emptyState?.visible()
        initRecyclerView(view, listener)
        getProductList(productListFilter)

        observeProductListData()
        observeLoadingState()
        observeFetchingState()

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
            recyclerViewShowcaseAddProduct?.apply {
                setHasFixedSize(true)
                layoutManager = linearLayoutManager
                showcaseProductListAdapter = ShowcaseProductListAdapter(listener)
                adapter = showcaseProductListAdapter
                addOnScrollListener(scrollListener)
            }
        }
    }

    private fun showEmptyViewProductSearch(state: Boolean) {
        emptyState?.setImageUrl(ImageAssets.SEARCH_SHOWCASE_NOT_FOUND)
        if(state) {
            recyclerViewShowcaseAddProduct?.gone()
            emptyState?.visible()
        }
        else {
            recyclerViewShowcaseAddProduct?.visible()
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

    private fun observeFetchingState() {
        observe(showcaseProductAddViewModel.fetchingState) {
            if(it) showFetchingProgress()
            else hideFetchingProgress()
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

    private fun showFetchingProgress() {
        emptyState?.invisible()
        loader?.visible()
        recyclerViewShowcaseAddProduct?.gone()
    }

    private fun hideFetchingProgress() {
        loader?.gone()
        recyclerViewShowcaseAddProduct?.visible()
    }

    private fun hideLoadingProgress() {
        showcaseProductListAdapter?.hideLoadingProgress()
    }

    private fun slideDownCounter() {
        productSelectedCounter?.animate()?.translationY(SLIDE_DOWN_COUNTER_TRANSLATE_Y)
        if(buttonBackToTop?.circleMainMenu?.visibility == View.VISIBLE)
            buttonBackToTop?.circleMainMenu?.hide()
    }

    private fun slideUpCounter() {
        productSelectedCounter?.animate()?.translationY(0f)
        if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0)
            buttonBackToTop?.circleMainMenu?.show()
    }

}