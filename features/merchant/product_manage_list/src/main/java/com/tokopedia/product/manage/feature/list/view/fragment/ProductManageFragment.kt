package com.tokopedia.product.manage.feature.list.view.fragment

import android.accounts.NetworkErrorException
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.presentation.activity.ProductManageSetCashbackActivity
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_RESULT
import com.tokopedia.product.manage.feature.etalase.view.activity.EtalasePickerActivity
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.EXTRA_ETALASE_ID
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.EXTRA_ETALASE_NAME
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.REQUEST_CODE_PICK_ETALASE
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.product.manage.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.feature.list.constant.ProductManageAnalytics.MP_PRODUCT_MANAGE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_THRESHOLD
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.INSTAGRAM_SELECT_REQUEST_CODE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_ADD_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_EDIT_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_STOCK_REMINDER
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.SET_CASHBACK_REQUEST_CODE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.URL_TIPS_TRICK
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.view.adapter.ProductManageListAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductListItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactoryImpl
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductManageMoreMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.listener.ProductManageListListener
import com.tokopedia.product.manage.feature.list.view.model.*
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.ShowFilterTab
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductManageBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductManageMoreMenuBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.StockInformationBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.tab.ProductManageFilterTab
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.multiedit.ui.bottomsheet.ProductMultiEditBottomSheet
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getRetryMessage
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getSuccessMessage
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment.ProductManageQuickEditPriceFragment
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui.QuickEditVariantPriceBottomSheet
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui.QuickEditVariantStockBottomSheet
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.*
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.*
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.data.model.FreeDeposit.CREATOR.DEPOSIT_ACTIVE
import com.tokopedia.topads.freeclaim.data.constant.TOPADS_FREE_CLAIM_URL
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_product_manage.*
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException
import javax.inject.Inject

open class ProductManageFragment : BaseListFragment<ProductViewModel, ProductManageAdapterFactoryImpl>(),
    ProductViewHolder.ProductViewHolderView,
    ProductMenuViewHolder.ProductMenuListener,
    ProductMultiEditBottomSheet.MultiEditListener,
    ProductManageFilterFragment.OnFinishedListener,
    ProductManageQuickEditPriceFragment.OnFinishedListener,
    ProductManageQuickEditStockFragment.OnFinishedListener,
    ProductManageMoreMenuViewHolder.ProductManageMoreMenuListener,
    ProductManageListListener{

    @Inject
    lateinit var viewModel: ProductManageViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var shopDomain: String = ""
    private var goldMerchant: Boolean = false
    private var isOfficialStore: Boolean = false
    private var productListFeaturedOnlySize: Int = 0
    private var dialogFeaturedProduct: DialogUnify? = null
    private var productManageBottomSheet: ProductManageBottomSheet? = null
    private var filterProductBottomSheet: ProductManageFilterFragment? = null
    private var productManageMoreMenuBottomSheet: ProductManageMoreMenuBottomSheet? = null
    private var multiEditBottomSheet: ProductMultiEditBottomSheet? = null
    private val stockInfoBottomSheet by lazy { StockInformationBottomSheet(view, fragmentManager) }

    private val productManageListAdapter by lazy { adapter as ProductManageListAdapter }
    private var defaultFilterOptions: List<FilterOption> = emptyList()
    private var itemsChecked: MutableList<ProductViewModel> = mutableListOf()
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var filterTab: ProductManageFilterTab? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    open fun getLayoutRes(): Int = R.layout.fragment_product_manage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        loadInitialData()
    }

    private fun initView() {
        setupInterceptor()
        setupSearchBar()
        setupProductList()
        setupFiltersTab()
        setupBottomSheet()
        setupMultiSelect()
        setupSelectAll()
        renderCheckedView()

        observeShopInfo()
        observeDeleteProduct()
        observeProductListFeaturedOnly()
        observeProductList()
        observeFilterTabs()
        observeMultiSelect()

        observeEditPrice()
        observeEditStock()
        observeMultiEdit()
        observeGetFreeClaim()
        observeGetPopUpInfo()

        observeSetFeaturedProduct()
        observeViewState()
        observeFilter()

        observeEditVariantPrice()
        observeEditVariantStock()

        getFiltersTab()
        getProductListFeaturedOnlySize()
        getTopAdsFreeClaim()
        getGoldMerchantStatus()

        setupDialogFeaturedProduct()

        context?.let { UpdateShopActiveService.startService(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuViewId = if (GlobalConfig.isSellerApp()) {
            R.menu.menu_product_manage
        } else {
            R.menu.menu_product_manage_dark
        }
        inflater.inflate(menuViewId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_product_menu) {
            val subMenu = item.subMenu
            val addProductMenu = subMenu.findItem(R.id.label_view_add_image)
            val importFromInstagramMenu = subMenu.findItem(R.id.label_view_import_from_instagram)

            addProductMenu.setOnMenuItemClickListener {
                val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_ADD)
                startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
                true
            }

            importFromInstagramMenu.setOnMenuItemClickListener {
                val intent = AddProductImagePickerBuilder.createPickerIntentInstagramImport(context)
                startActivityForResult(intent, INSTAGRAM_SELECT_REQUEST_CODE)
                false
            }

            ProductManageTracking.eventAddProduct()
        }
        else if (item.itemId == R.id.action_more_menu) {
            showMoreMenuBottomSheet()
            ProductManageTracking.eventClickMoreMenuEllipses()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMoreMenuClicked(menu: ProductMoreMenuModel) {
        if(menu.title == getString(R.string.product_manage_shop_showcase_more_menu_text)) {
            // goto showcase list
            RouteManager.route(requireContext(), ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            productManageMoreMenuBottomSheet?.dismiss()
            ProductManageTracking.eventClickMoreMenuShopShowcase()
        }
    }

    private fun onClickMoreFilter() {
        showFilterBottomSheet()

        val tabName = getString(R.string.product_manage_filter)
        ProductManageTracking.eventInventory(tabName)
    }

    private fun onClickFilterTab(filter: FilterTabViewModel) {
        showLoadingProgress()

        clearAllData()
        resetMultiSelect()
        disableMultiSelect()
        renderCheckedView()

        getFiltersTab(withDelay = true)
        getProductList(withDelay = true)

        val tabName = getString(filter.titleId, filter.count)
        ProductManageTracking.eventInventory(tabName)
    }

    override fun editMultipleProductsEtalase() {
        goToEtalasePicker()
        ProductManageTracking.eventBulkSettingsMoveEtalase()
    }

    override fun editMultipleProductsInActive() {
        showEditProductsInActiveConfirmationDialog()
        ProductManageTracking.eventBulkSettingsDeactive()
    }

    override fun deleteMultipleProducts() {
        showDeleteProductsConfirmationDialog()
        ProductManageTracking.eventBulkSettingsDeleteBulk()
    }

    override fun onFinish(selectedData: FilterOptionWrapper) {
        viewModel.setFilterOptionWrapper(selectedData)
    }

    override fun onFinishEditPrice(product: ProductViewModel) {
        product.title?.let { product.minPrice?.price?.let { price -> viewModel.editPrice(product.id, price, it) } }
    }

    override fun onFinishEditStock(modifiedProduct: ProductViewModel) {
        if(modifiedProduct.stock != null && modifiedProduct.title != null && modifiedProduct.status != null) {
            viewModel.editStock(modifiedProduct.id, modifiedProduct.stock, modifiedProduct.title, modifiedProduct.status)
        }
    }

    override fun getEmptyDataViewModel(): EmptyModel {
        return EmptyModel().apply {
            if(showProductEmptyState()) {
                contentRes = R.string.product_manage_list_empty_product
                urlRes = ProductManageUrl.PRODUCT_MANAGE_LIST_EMPTY_STATE
            } else {
                contentRes = R.string.product_manage_list_empty_search
                urlRes = ProductManageUrl.PRODUCT_MANAGE_SEARCH_EMPTY_STATE
            }
        }
    }

    //set filter options if filterOptions is not null or empty
    private fun setDefaultFilterOption() {
        if (defaultFilterOptions.isNotEmpty()) {
            val filterOptionsWrapper = FilterOptionWrapper(
                    sortOption = null,
                    filterOptions = defaultFilterOptions,
                    filterShownState = listOf(true, true, false, true)
            )
            viewModel.setFilterOptionWrapper(filterOptionsWrapper)

            defaultFilterOptions = emptyList()
        }
    }

    fun setDefaultFilterOptions(filterOptions: List<FilterOption>) {
        defaultFilterOptions = filterOptions
    }

    private fun showProductEmptyState(): Boolean {
        val selectedFilters = viewModel.selectedFilterAndSort.value
        val searchKeyword = searchBar.searchTextView.text.toString()
        return searchKeyword.isEmpty() && selectedFilters == null && filterTab?.isFilterActive() == false
    }

    private fun setupInterceptor() {
        interceptor.setOnTouchListener { _, _ ->
            searchBar.clearFocus()
            searchBar.hideKeyboard()
            false
        }
    }

    private fun setupSearchBar() {
        searchBar.clearFocus()

        searchBar.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                clearAllData()
                showLoadingProgress()
                getProductList()
                searchBar.clearFocus()
            }

            override fun onSearchTextChanged(text: String?) {}
        })

        searchBar.closeImageButton.setOnClickListener {
            clearSearchBarInput()
            loadInitialData()
        }

        searchBar.setOnTouchListener { view, _ ->
            view.requestFocus()
        }

        searchBar.setSearchHint(getString(R.string.product_manage_search_hint))

        context?.let {
            searchBar.closeImageButton.setImageResource(android.R.color.transparent)
            searchBar.closeImageButton.setBackgroundResource(com.tokopedia.unifycomponents.R.drawable.unify_clear_ic)
        }
    }

    private fun setupBottomSheet() {
        productManageBottomSheet = ProductManageBottomSheet(view, this, fragmentManager)
        multiEditBottomSheet = ProductMultiEditBottomSheet(view, this, fragmentManager)
        productManageMoreMenuBottomSheet = ProductManageMoreMenuBottomSheet(context, this, fragmentManager)
    }

    private fun setupMultiSelect() {
        textMultipleSelect.setOnClickListener {
            viewModel.toggleMultiSelect()
            ProductManageTracking.eventMultipleSelect()
        }

        btnMultiEdit.setOnClickListener {
            multiEditBottomSheet?.show()
            ProductManageTracking.eventBulkSettings()
        }
    }

    private fun startPerformanceMonitoring() {
        performanceMonitoring = PerformanceMonitoring.start(MP_PRODUCT_MANAGE)
    }

    private fun setupDialogFeaturedProduct() {
        context?.let {
            dialogFeaturedProduct = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        }
    }

    private fun setupSelectAll() {
        checkBoxSelectAll.setOnClickListener {
            val isChecked = checkBoxSelectAll.isChecked
            adapter.data.forEachIndexed { index, _ ->
                onClickProductCheckBox(isChecked, index)
            }
            productManageListAdapter.notifyDataSetChanged()
        }
    }

    private fun showFilterBottomSheet() {
        filterProductBottomSheet = context?.let {
            ProductManageFilterFragment.createInstance(it, viewModel.selectedFilterAndSort.value,this)
        }
        this.childFragmentManager.let { filterProductBottomSheet?.show(it,"BottomSheetTag") }
    }

    private fun showMoreMenuBottomSheet() {
        productManageMoreMenuBottomSheet?.show()
    }

    private fun setActiveFilterCount(filter: FilterOptionWrapper) {
        filterTab?.setActiveFilterCount(filter.selectedFilterCount)
    }

    private fun renderCheckedView() {
        val multiSelectEnabled = viewModel.toggleMultiSelect.value == true

        if (multiSelectEnabled) {
            val textSelectedProduct = getString(R.string.product_manage_bulk_count,
                itemsChecked.size.toString())
            textProductCount.text = textSelectedProduct
            textProductCount.show()
        } else {
            btnMultiEdit.hide()
            renderProductCount()
        }

        btnMultiEdit.showWithCondition(itemsChecked.isNotEmpty())
    }

    private fun renderSelectAllCheckBox() {
        when {
            itemsChecked.isEmpty() -> {
                resetSelectAllCheckBox()
            }
            itemsChecked.size == adapter.data.size -> {
                checkBoxSelectAll.isChecked = true
                checkBoxSelectAll.setIndeterminate(false)
            }
            !checkBoxSelectAll.getIndeterminate() -> {
                checkBoxSelectAll.isChecked = true
                checkBoxSelectAll.setIndeterminate(true)
            }
        }
    }

    private fun setupProductList() {
        recycler_view.apply {
            itemAnimator = null
            clearItemDecoration()
            addItemDecoration(ProductListItemDecoration())
        }
    }

    private fun setupFiltersTab() {
        filterTab = ProductManageFilterTab(tabSortFilter, {
            onClickMoreFilter()
        }, {
            onClickFilterTab(it)
        })
    }

    private val addProductReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == TkpdState.ProductService.BROADCAST_ADD_PRODUCT &&
                intent.hasExtra(TkpdState.ProductService.STATUS_FLAG) &&
                intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0) == TkpdState.ProductService.STATUS_DONE) {
                activity?.run {
                    runOnUiThread {
                        val productId = intent.extras?.getString(TkpdState.ProductService.PRODUCT_ID)
                            ?: ""
                        viewModel.getPopupsInfo(productId)
                        getFiltersTab(withDelay = true)
                        getProductList(withDelay = true, isRefresh = true)
                    }
                }
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<ProductViewModel, ProductManageAdapterFactoryImpl> {
        return ProductManageListAdapter(adapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): ProductManageAdapterFactoryImpl {
        return ProductManageAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ProductManageListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        getProductList(page)
    }

    private fun renderProductList(list: List<ProductViewModel>, hasNextPage: Boolean) {
        renderList(list, hasNextPage)
        renderCheckedView()
    }

    private fun getProductList(page: Int = 1, isRefresh: Boolean = false, withDelay: Boolean = false) {
        val keyword = searchBar.searchTextView.text.toString()
        val selectedFilter = viewModel.selectedFilterAndSort.value
        val filterOptions = createFilterOptions(page, keyword)
        val sortOption = selectedFilter?.sortOption

        filterTab?.getSelectedFilter()?.let {
            filterOptions.add(FilterByStatus(it))
        }

        viewModel.getProductList(userSession.shopId, filterOptions, sortOption, isRefresh, withDelay)
    }

    private fun getProductListFeaturedOnlySize() {
        viewModel.getFeaturedProductCount(userSession.shopId)
    }

    private fun createFilterOptions(page: Int, keyword: String?): MutableList<FilterOption> {
        val selectedFilter = viewModel.selectedFilterAndSort.value
        val filterOptions = selectedFilter?.filterOptions.orEmpty().toMutableList()

        filterOptions.addKeywordFilter(keyword)
        filterOptions.add(FilterByPage(page))
        return filterOptions
    }

    private fun MutableList<FilterOption>.addKeywordFilter(keyword: String?) {
        if(!keyword.isNullOrEmpty()) add(FilterByKeyword(keyword))
    }

    private fun showProductList(productList: List<ProductViewModel>) {
        val hasNextPage = productList.isNotEmpty()
        renderProductList(productList, hasNextPage)
        renderSelectAllCheckBox()
        setDefaultFilterOption()
    }

    private fun renderMultiSelectProduct() {
        val shouldShow = adapter.data
            .filterIsInstance<ProductViewModel>()
            .isNotEmpty()

        multiSelectContainer.showWithCondition(shouldShow)
    }

    private fun onErrorEditPrice(editPriceResult: EditPriceResult) {
        val message = if(editPriceResult.error is NetworkErrorException) {
            getString(editPriceResult.error.message.toIntOrZero())
        } else {
            editPriceResult.error?.message
        }
        message?.let {
            val retryMessage = getString(R.string.product_manage_snack_bar_retry)
            showErrorToast(it, retryMessage) {
                viewModel.editPrice(editPriceResult.productId, editPriceResult.price, editPriceResult.productName)
            }
        }
    }

    private fun onErrorEditStock(editStockResult: EditStockResult) {
        val message = if(editStockResult.error is NetworkErrorException) {
            getString(editStockResult.error.message.toIntOrZero())
        } else {
            editStockResult.error?.message
        }
        message?.let {
            val retryMessage = getString(R.string.product_manage_snack_bar_retry)
            showErrorToast(it, retryMessage) {
                viewModel.editStock(editStockResult.productId, editStockResult.stock, editStockResult.productName, editStockResult.status)
            }
        }
    }

    private fun onSuccessEditPrice(productId: String, price: String, productName: String) {
        Toaster.make(coordinatorLayout, getString(
                R.string.product_manage_quick_edit_price_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        productManageListAdapter.updatePrice(productId, price)
    }

    private fun onSuccessEditStock(productId: String, stock: Int, productName: String, status: ProductStatus) {
        Toaster.make(coordinatorLayout, getString(
                R.string.product_manage_quick_edit_stock_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        productManageListAdapter.updateStock(productId, stock, status)

        filterTab?.getSelectedFilter()?.let {
            filterProductListByStatus(it)
            renderMultiSelectProduct()
        }

        getFiltersTab(withDelay = true)
    }

    private fun onSuccessSetCashback(setCashbackResult: SetCashbackResult) {
        Toaster.make(coordinatorLayout, getString(
                R.string.product_manage_set_cashback_success, setCashbackResult.productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        productManageListAdapter.updateCashBack(setCashbackResult.productId, setCashbackResult.cashback)
        val filterOptions = viewModel.selectedFilterAndSort.value?.filterOptions
        if(filterOptions == listOf(FilterByCondition.CashBackOnly)) {
            filterProductListByCashback()
        }
    }

    private fun filterProductListByCashback() {
        productManageListAdapter.filterProductList { it.cashBack != 0 }
    }

    private fun onSetCashbackLimitExceeded() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
                setTitle(it.resources.getString(R.string.product_manage_set_cashback_dialog_title))
                setDescription(it.resources.getString(R.string.product_manage_set_cashback_dialog_desc))
                setPrimaryCTAText(it.resources.getString(R.string.product_manage_set_cashback_dialog_upgrade_button))
                setSecondaryCTAText(it.resources.getString(R.string.product_manage_set_cashback_dialog_see_cashback_products_button))
                setPrimaryCTAClickListener {
                    dismiss()
                    RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                    ProductManageTracking.eventCashbackSettingsPopUp()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                    viewModel.getProductList(shopId = userSession.shopId, filterOptions = listOf(FilterByCondition.CashBackOnly), isRefresh = true)
                    viewModel.setSelectedFilter(selectedFilter = listOf(FilterByCondition.CashBackOnly))
                }
                setImageUrl(ProductManageUrl.ILLUSTRATION_SET_CASHBACK_LIMIT_REACHED)
            }.show()
        }
    }

    private fun onErrorDeleteProduct(deleteProductResult: DeleteProductResult) {
        val message = if(deleteProductResult.error is NetworkErrorException) {
            getString(deleteProductResult.error.message.toIntOrZero())
        } else {
            deleteProductResult.error?.message
        }
        message?.let {
            val retryMessage = getString(R.string.product_manage_snack_bar_retry)
            showErrorToast(it, retryMessage) {
                viewModel.deleteSingleProduct(deleteProductResult.productName, deleteProductResult.productId)
            }
        }
    }

    private fun onSuccessDeleteProduct(productName: String, productId: String) {
        Toaster.make(coordinatorLayout, getString(
                R.string.product_manage_delete_product_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        productManageListAdapter.deleteProduct(productId)
        renderMultiSelectProduct()
        getFiltersTab(withDelay = true)
    }

    private fun showMessageToast(message: String) {
        view?.let {
            val actionLabel = getString(com.tokopedia.design.R.string.close)
            Toaster.make(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionLabel)
        }
    }

    private fun showMessageToastWithoutAction(message: String) {
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        }
    }

    private fun showErrorToast(
        message: String = getString(R.string.product_manage_snack_bar_fail),
        actionLabel: String = getString(com.tokopedia.abstraction.R.string.close),
        listener: () -> Unit = {}
    ) {
        view?.let {
            val onClickActionLabel = View.OnClickListener { listener.invoke() }
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionLabel, onClickActionLabel)
        }
    }

    private fun showLoadingProgress() {
        progressBar.show()
    }

    private fun hideLoadingProgress() {
        progressBar.hide()
    }

    private fun onErrorGetFreeClaim() {
        topAdsWidgetFreeClaim.visibility = View.GONE
    }

    private fun onSuccessGetFreeClaim(dataDeposit: DataDeposit) {
        val freeDeposit = dataDeposit.freeDeposit

        if (freeDeposit.nominal > 0 && freeDeposit.status == DEPOSIT_ACTIVE) {
            topAdsWidgetFreeClaim.setContent(MethodChecker.fromHtml(getString(com.tokopedia.topads.freeclaim.R.string.free_claim_template, freeDeposit.nominalFmt,
                freeDeposit.remainingDays.toString() + "", TOPADS_FREE_CLAIM_URL)))
            topAdsWidgetFreeClaim.visibility = View.VISIBLE
        } else {
            topAdsWidgetFreeClaim.visibility = View.GONE
        }
    }

    private fun onSuccessGetPopUp(isShowPopup: Boolean, productId: String) {
        if (isShowPopup) {
            initPopUpDialog(productId).show()
        }
    }

    private fun initPopUpDialog(productId: String): Dialog {
        context?.let { context ->
            activity?.let { activity ->
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_product_add)

                val btnSubmit: Button = dialog.findViewById(R.id.filterSubmitButton)
                val btnGoToPdp: Button = dialog.findViewById(R.id.btn_product_list)
                val txtTipsTrick: TextView = dialog.findViewById(R.id.txt_tips_trick)

                btnSubmit.setOnClickListener {
                    RouteManager.route(context, ApplinkConst.SELLER_SHIPPING_EDITOR)
                    activity.finish()
                }

                btnGoToPdp.setOnClickListener {
                    goToPDP(productId)
                    dialog.dismiss()
                }
                val backgroundColor = MethodChecker.getColor(context, com.tokopedia.design.R.color.tkpd_main_green)

                val spanText = SpannableString(getString(com.tokopedia.product.manage.item.R.string.popup_tips_trick_clickable))
                spanText.setSpan(StyleSpan(Typeface.BOLD),
                    5, spanText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spanText.setSpan(ForegroundColorSpan(backgroundColor),
                    5, spanText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                val cs = object : ClickableSpan() {
                    override fun onClick(v: View) {
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TIPS_TRICK))
                        activity.finish()
                    }
                }
                spanText.setSpan(cs, 5, spanText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                txtTipsTrick.movementMethod = LinkMovementMethod.getInstance()
                txtTipsTrick.text = spanText
                return dialog
            }
        }
        return Dialog(context!!)
    }

    private fun onErrorGetPopUp() {
        onSuccessGetPopUp(false, "")
    }

    private fun onSuccessMultiEditProducts(result: MultiEditResult) {
        showMultiEditToast(result)
        updateEditProductList(result)
    }

    private fun showMultiEditToast(result: MultiEditResult) {
        context?.let { context ->
            if (result.failed.isNotEmpty()) {
                val retryLabel = getString(R.string.product_manage_snack_bar_retry)
                val retryMessage = getRetryMessage(context, result)

                showErrorToast(retryMessage, retryLabel) { retryMultiEditProducts(result) }
            } else {
                val message = getSuccessMessage(context, result)
                showMessageToast(message)

                clearSelectedProduct()
                renderCheckedView()
            }
        }
    }

    private fun retryMultiEditProducts(result: MultiEditResult) {
        val productIds = result.failed.map { it.productID }

        when(result) {
            is EditByStatus -> viewModel.editProductsByStatus(productIds, result.status)
            is EditByMenu -> viewModel.editProductsEtalase(productIds, result.menuId, result.menuName)
        }
    }

    private fun updateEditProductList(result: MultiEditResult) {
        val productIds = result.success.map { it.productID }

        when(result) {
            is EditByStatus -> {
                updateProductListStatus(productIds, result.status)

                filterTab?.getSelectedFilter()?.let {
                    filterProductListByStatus(it)
                    renderMultiSelectProduct()
                }

                getFiltersTab(withDelay = true)
            }
        }

        if(result.failed.isEmpty()) {
            viewModel.toggleMultiSelect()
        } else {
            unCheckMultipleProducts(productIds)
        }
    }

    private fun unCheckMultipleProducts(productIds: List<String>) {
        productIds.forEach { productId ->
            val index = adapter.data.indexOfFirst { it.id == productId }
            if(index >= 0) { onClickProductCheckBox(false, index) }
        }
        productManageListAdapter.notifyDataSetChanged()
    }

    private fun updateProductListStatus(productIds: List<String>, status: ProductStatus) {
        productIds.forEach { productId ->
            when(status) {
                DELETED -> productManageListAdapter.deleteProduct(productId)
                INACTIVE -> productManageListAdapter.setProductStatus(productId, status)
                else -> {}  // do nothing
            }
        }
    }

    private fun filterProductListByStatus(productStatus: ProductStatus?) {
        productManageListAdapter.filterProductList { it.status == productStatus }
    }

    private fun filterProductListByFeatured() {
        productManageListAdapter.filterProductList { it.isFeatured == true }
    }

    override fun onSwipeRefresh() {
        isLoadingInitialData = true
        swipeToRefresh.isRefreshing = true
        clearFilterAndKeywordIfEmpty()

        showPageLoading()
        hideSnackBarRetry()
        resetProductList()
        disableMultiSelect()

        getFiltersTab(withDelay = true)
        getProductList(withDelay = true)
    }

    private fun clearFilterAndKeywordIfEmpty() {
        val productList = adapter.data
            .filterIsInstance<ProductViewModel>()

        if(productList.isEmpty()) {
            resetSelectedFilter()
            clearSearchBarInput()
        }
    }

    private fun resetSelectedFilter() {
        removeObservers(viewModel.selectedFilterAndSort)
        viewModel.resetSelectedFilter()
        filterTab?.resetFilters()
        observeFilter()
    }

    private fun clearSearchBarInput() {
        searchBar.searchTextView.text.clear()
    }

    private fun onSuccessChangeFeaturedProduct(productId: String, status: Int) {
        //Default feature product action is to remove the product from featured products.
        //The value will change depends on the status code. 0 is remove, 1 is add
        var successMessage: String = getString(R.string.product_manage_success_remove_featured_product)
        var isFeaturedProduct = false
        //If the action is to add featured product, invert the attributes value also
        if (status == ProductManageListConstant.FEATURED_PRODUCT_ADD_STATUS) {
            successMessage = getString(R.string.product_manage_success_add_featured_product)
            isFeaturedProduct = true
        }
        productManageListAdapter.updateFeaturedProduct(productId, isFeaturedProduct)
        val filterOptions = viewModel.selectedFilterAndSort.value?.filterOptions
        if(filterOptions == listOf(FilterByCondition.FeaturedOnly)) {
            filterProductListByFeatured()
            renderMultiSelectProduct()
        }
        hideLoadingProgress()
        showMessageToastWithoutAction(successMessage)
    }

    private fun onFailedChangeFeaturedProduct(e: Throwable) {
        val toasterError = ""
        hideLoadingProgress()
        showErrorToast(getChangeFeaturedErrorMessage(e), toasterError) {}
    }

    private fun getChangeFeaturedErrorMessage(throwable: Throwable): String =
        when (throwable) {
            is UnknownHostException -> getString(R.string.product_manage_failed_no_internet)
            is TimeoutException -> getString(R.string.product_manage_failed_set_featured_product)
            is MessageErrorException -> throwable.message
                ?: getString(R.string.product_manage_failed_set_featured_product)
            else -> ErrorHandler.getErrorMessage(context, throwable)
        }

    override fun onClickProductCheckBox(isChecked: Boolean, position: Int) {
        val product = adapter.data[position]
        val checkedData = itemsChecked.firstOrNull { it.id.contains(product.id) }
        adapter.data[position] = product.copy(isChecked = isChecked)

        if (isChecked && checkedData == null) {
            itemsChecked.add(product)
        } else if(!isChecked){
            itemsChecked.remove(checkedData)
        }

        renderSelectAllCheckBox()
        renderCheckedView()
    }

    override fun onClickStockInformation() {
        stockInfoBottomSheet.show()
    }

    override fun onClickEditStockButton(product: ProductViewModel) {
        val editStockBottomSheet = context?.let { ProductManageQuickEditStockFragment.createInstance(it, product, this) }
        editStockBottomSheet?.show(childFragmentManager, "quick_edit_stock")
        ProductManageTracking.eventEditStock(product.id)
    }

    override fun onClickEditVariantPriceButton(product: ProductViewModel) {
        val editVariantBottomSheet = QuickEditVariantPriceBottomSheet.createInstance(product.id) { result ->
            viewModel.editVariantsPrice(result)
        }
        editVariantBottomSheet.show(childFragmentManager, QuickEditVariantPriceBottomSheet.TAG)
        ProductManageTracking.eventClickEditPriceVariant()
    }

    override fun onClickEditVariantStockButton(product: ProductViewModel) {
        val editVariantStockBottomSheet = QuickEditVariantStockBottomSheet.createInstance(product.id) { result ->
            viewModel.editVariantsStock(result)
        }
        editVariantStockBottomSheet.show(childFragmentManager, QuickEditVariantStockBottomSheet.TAG)
        ProductManageTracking.eventClickEditStockVariant()
    }

    override fun onClickContactCsButton(product: ProductViewModel) {
        goToProductViolationHelpPage()
        ProductManageTracking.eventContactCs(product.id)
    }

    override fun onClickMoreOptionsButton(product: ProductViewModel) {
        hideSoftKeyboard()

        if (product.status == MODERATED) {
            val errorMessage = getString(R.string.product_manage_desc_product_on_supervision, product.title)
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        } else {
            val isPowerMerchantOrOfficialStore = viewModel.isPowerMerchant() || isOfficialStore
            productManageBottomSheet?.show(product, isPowerMerchantOrOfficialStore)
        }
    }

    override fun onClickEditPriceButton(product: ProductViewModel) {
        val editPriceBottomSheet = context?.let { ProductManageQuickEditPriceFragment.createInstance(it, product, this) }
        editPriceBottomSheet?.show(childFragmentManager, "quick_edit_price")
        ProductManageTracking.eventEditPrice(product.id)
    }

    override fun onClickOptionMenu(menu: ProductMenuViewModel) {
        val product = menu.product
        val productId = product.id
        val productName = product.title ?: ""

        when(menu) {
            is Preview -> {
                goToPDP(productId)
                ProductManageTracking.eventSettingsPreview(productId)
            }
            is Duplicate -> {
                clickDuplicateProduct(productId)
                ProductManageTracking.eventSettingsDuplicate(productId)
            }
            is StockReminder -> {
                onSetStockReminderClicked(product)
                ProductManageTracking.eventSettingsReminder(productId)
            }
            is Delete -> {
                clickDeleteProductMenu(productName, productId)
                ProductManageTracking.eventSettingsDelete(productId)
            }
            is SetTopAds -> {
                onPromoTopAdsClicked(product)
                ProductManageTracking.eventSettingsTopads(productId)
            }
            is SetCashBack -> {
                onSetCashbackClicked(product)
                ProductManageTracking.eventSettingsCashback(productId)
            }
            is SetFeaturedProduct -> {
                onSetFeaturedProductClicked(product)
                ProductManageTracking.eventSettingsFeatured(productId)
            }
            is RemoveFeaturedProduct -> {
                onRemoveFeaturedProductClicked(product)
                ProductManageTracking.eventSettingsFeatured(productId)
            }
        }

        productManageBottomSheet?.dismiss()
    }

    private fun goToProductViolationHelpPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${ProductManageUrl.PRODUCT_VIOLATION_HELP_URL}")
    }

    private fun clickDuplicateProduct(productId: String) {
        goToDuplicateProduct(productId)
    }

    private fun clickDeleteProductMenu(productName: String, productId: String) {
        showDialogDeleteProduct(productName, productId)
    }

    private fun hideSoftKeyboard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    private fun onSetFeaturedProductClicked(productManageViewModel: ProductViewModel) {
        context?.let { context ->
            if(viewModel.isPowerMerchant() || isOfficialStore) {
                productManageViewModel.isFeatured?.let { isFeatured ->
                    if(!isFeatured) {
                        when (productListFeaturedOnlySize) {
                            MAX_FEATURED_PRODUCT -> {
                                setDialogFeaturedProduct(
                                    ProductManageUrl.ILLUSTRATION_MAX_FEATURED_PRODUCT_DOMAIN,
                                    getString(R.string.product_featured_max_dialog_title),
                                    getString(R.string.product_featured_max_dialog_desc),
                                    getString(R.string.product_featured_max_dialog_primary_cta),
                                    getString(R.string.product_featured_max_dialog_secondary_cta)
                                )
                                dialogFeaturedProduct?.setPrimaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                                dialogFeaturedProduct?.setSecondaryCTAClickListener {
                                    dialogFeaturedProduct?.dismiss()
                                    viewModel.setSelectedFilter(listOf(FilterByCondition.FeaturedOnly))
                                }
                                dialogFeaturedProduct?.show()
                            }
                            MIN_FEATURED_PRODUCT -> {
                                setDialogFeaturedProduct(
                                    ProductManageUrl.ILLUSTRATION_ADD_FEATURED_PRODUCT_DOMAIN,
                                    getString(R.string.product_featured_add_dialog_title),
                                    getString(R.string.product_featured_add_dialog_desc),
                                    getString(R.string.product_featured_add_dialog_primary_cta),
                                    getString(R.string.product_featured_add_dialog_secondary_cta)
                                )
                                dialogFeaturedProduct?.setPrimaryCTAClickListener {
                                    addFeaturedProduct(productManageViewModel.id)
                                    dialogFeaturedProduct?.dismiss()
                                    ProductManageTracking.eventFeaturedProductPopUpSave()
                                }
                                dialogFeaturedProduct?.setSecondaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                                dialogFeaturedProduct?.show()
                            }
                            else -> {
                                addFeaturedProduct(productManageViewModel.id)
                            }
                        }
                    }
                }
            } else {
                    setDialogFeaturedProduct(
                        ProductManageUrl.ILLUSTRATION_SPECIAL_FEATURED_PRODUCT_DOMAIN,
                        getString(R.string.product_featured_special_dialog_title),
                        getString(R.string.product_featured_special_dialog_desc),
                        getString(R.string.product_featured_special_dialog_primary_cta),
                        getString(R.string.product_featured_special_dialog_secondary_cta)
                )
                dialogFeaturedProduct?.setPrimaryCTAClickListener {
                    dialogFeaturedProduct?.dismiss()
                    RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                    ProductManageTracking.eventFeaturedProductPopUpMore()
                }
                dialogFeaturedProduct?.setSecondaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                dialogFeaturedProduct?.show()
            }
        }
    }

    private fun onRemoveFeaturedProductClicked(productManageViewModel: ProductViewModel) {
        productListFeaturedOnlySize -= 1
        showLoadingProgress()
        setFeaturedProduct(productManageViewModel.id, ProductManageListConstant.FEATURED_PRODUCT_REMOVE_STATUS)
    }

    private fun onSetStockReminderClicked(productManageViewModel: ProductViewModel) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.STOCK_REMINDER, productManageViewModel.id, productManageViewModel.title, productManageViewModel.stock.toString())
        startActivityForResult(intent, REQUEST_CODE_STOCK_REMINDER)
    }

    private fun onPromoTopAdsClicked(productManageViewModel: ProductViewModel) {
        context?.let {
            val uri = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, userSession.shopId)
                .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, productManageViewModel.id)
                .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                    if (GlobalConfig.isSellerApp())
                        TopAdsSourceOption.SA_MANAGE_LIST_PRODUCT
                    else
                        TopAdsSourceOption.MA_MANAGE_LIST_PRODUCT).build().toString()

            RouteManager.route(it, uri)
        }
    }

    private fun onSetCashbackClicked(productManageViewModel: ProductViewModel) {
        with(productManageViewModel) {
            context?.let {
                val intent = ProductManageSetCashbackActivity.createIntent(it, id, title, cashBack, minPrice?.price)
                startActivityForResult(intent, SET_CASHBACK_REQUEST_CODE)
            }
        }
    }

    private fun goToDuplicateProduct(productId: String) {
        activity?.let {
            val uri = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                    .buildUpon()
                    .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, productId)
                    .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_DUPLICATE_PRODUCT)
                    .build()
                    .toString()
            val intent = RouteManager.getIntent(requireContext(), uri)
            startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
        }
    }

    private fun goToEditProduct(productId: String) {
        context?.let {
            val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_EDIT, productId)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT)
        }
    }

    private fun addFeaturedProduct(productId: String) {
        productListFeaturedOnlySize += 1
        showLoadingProgress()
        setFeaturedProduct(productId, ProductManageListConstant.FEATURED_PRODUCT_ADD_STATUS)
    }

    private fun setFeaturedProduct(id: String, type: Int) {
        viewModel.setFeaturedProduct(id, type)
    }

    private fun showDialogDeleteProduct(productName: String, productId: String) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(it.resources.getString(R.string.product_manage_delete_product_title))
            dialog.setDescription(it.resources.getString(R.string.product_manage_delete_product_description))
            dialog.setPrimaryCTAText(it.resources.getString(R.string.product_manage_delete_product_delete_button))
            dialog.setSecondaryCTAText(it.resources.getString(R.string.product_manage_delete_product_cancel_button))
            dialog.setPrimaryCTAClickListener {
                viewModel.deleteSingleProduct(productName, productId)
                dialog.dismiss()
                ProductManageTracking.eventDeleteProduct(productId)
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun onClickProductItem(product: ProductViewModel) {
        goToEditProduct(product.id)
        ProductManageTracking.eventOnProduct(product.id)
    }

    override fun clearAndGetProductList() {
        clearAllData()
        resetMultiSelect()
        disableMultiSelect()
        getProductList()
    }
    /**
     * This function is temporary for testing to avoid router and applink
     * For Dynamic Feature Support
     */
    private fun goToPDP(productId: String?) {
        if (productId != null) {
            RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        }
    }

    private fun goToEtalasePicker() {
        val intent = Intent(activity, EtalasePickerActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_PICK_ETALASE)
    }

    override fun onItemClicked(t: ProductViewModel?) {
        // NO OP
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            activity?.unregisterReceiver(addProductReceiver)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(TkpdState.ProductService.BROADCAST_ADD_PRODUCT)
            it.registerReceiver(addProductReceiver, intentFilter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
        activity?.let {
            if (addProductReceiver.isOrderedBroadcast) {
                it.unregisterReceiver(addProductReceiver)
            }
        }
        removeObservers()
    }

    private fun removeObservers() {
        removeObservers(viewModel.viewState)
        removeObservers(viewModel.productListResult)
        removeObservers(viewModel.shopInfoResult)
        removeObservers(viewModel.multiEditProductResult)
        removeObservers(viewModel.deleteProductResult)
        removeObservers(viewModel.editPriceResult)
        removeObservers(viewModel.getFreeClaimResult)
        removeObservers(viewModel.getPopUpResult)
        removeObservers(viewModel.setFeaturedProductResult)
        removeObservers(viewModel.toggleMultiSelect)
        removeObservers(viewModel.productFiltersTab)
    }

    override fun callInitialLoadAutomatically(): Boolean = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        intent?.let {
            when (requestCode) {
                INSTAGRAM_SELECT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val imageUrls = it.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    val imageDescList = it.getStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST)
                    goToProductDraft(imageUrls, imageDescList)
                }
                REQUEST_CODE_PICK_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                    val productIds = itemsChecked.map{ product -> product.id }
                    val etalaseId = it.getStringExtra(EXTRA_ETALASE_ID)
                    val etalaseName = it.getStringExtra(EXTRA_ETALASE_NAME)

                    viewModel.editProductsEtalase(productIds, etalaseId, etalaseName)
                }
                REQUEST_CODE_STOCK_REMINDER -> if(resultCode == Activity.RESULT_OK) {
                    val productName = it.getStringExtra(EXTRA_PRODUCT_NAME)
                    val threshold = it.getIntExtra(EXTRA_THRESHOLD, 0)
                    if(threshold > 0) {
                        Toaster.make(coordinatorLayout, getString(R.string.product_stock_reminder_toaster_success_desc, productName), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                    }
                    else {
                        Toaster.make(coordinatorLayout, getString(R.string.product_stock_reminder_toaster_success_remove_desc, productName), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                    }
                }
                SET_CASHBACK_REQUEST_CODE -> if(resultCode == Activity.RESULT_OK) {
                    val cacheManagerId = it.getStringExtra(SET_CASHBACK_CACHE_MANAGER_KEY)
                    val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, cacheManagerId) }
                    val setCashbackResult: SetCashbackResult? = cacheManager?.get(SET_CASHBACK_RESULT, SetCashbackResult::class.java)
                    setCashbackResult?.let { cashbackResult ->
                        if(cashbackResult.limitExceeded) {
                            onSetCashbackLimitExceeded()
                        } else {
                            onSuccessSetCashback(cashbackResult)
                        }
                    }
                }
                else -> super.onActivityResult(requestCode, resultCode, it)
            }
        }
    }

    private fun getTopAdsFreeClaim() {
        val query = GraphqlHelper.loadRawString(resources, com.tokopedia.topads.common.R.raw.gql_get_deposit)
        viewModel.getFreeClaim(query, userSession.shopId)
    }

    private fun getGoldMerchantStatus() {
        viewModel.getGoldMerchantStatus()
    }

    private fun getFiltersTab(withDelay: Boolean = false) {
        viewModel.getFiltersTab(withDelay)
    }

    private fun setDialogFeaturedProduct(imageUrl: String, title: String, desc: String, primaryCta: String, secondaryCta: String) {
        dialogFeaturedProduct?.setImageUrl(imageUrl)
        dialogFeaturedProduct?.setTitle(title)
        dialogFeaturedProduct?.setDescription(desc)
        dialogFeaturedProduct?.setPrimaryCTAText(primaryCta)
        dialogFeaturedProduct?.setSecondaryCTAText(secondaryCta)
    }

    // region observers
    private fun observeSetFeaturedProduct() {
        observe(viewModel.setFeaturedProductResult) {
            when (it) {
                is Success -> onSuccessChangeFeaturedProduct(it.data.productId, it.data.status)
                is Fail -> onFailedChangeFeaturedProduct(it.throwable)
            }
        }
    }

    private fun observeGetPopUpInfo() {
        observe(viewModel.getPopUpResult) {
            when (it) {
                is Success -> onSuccessGetPopUp(it.data.isSuccess, it.data.productId)
                is Fail -> onErrorGetPopUp()
            }
        }
    }

    private fun observeGetFreeClaim() {
        observe(viewModel.getFreeClaimResult) {
            when (it) {
                is Success -> onSuccessGetFreeClaim(it.data)
                is Fail -> onErrorGetFreeClaim()
            }
        }
    }

    private fun observeEditPrice() {
        observe(viewModel.editPriceResult) {
            when (it) {
                is Success -> onSuccessEditPrice(it.data.productId, it.data.price, it.data.productName)
                is Fail -> {
                    onErrorEditPrice(it.throwable as EditPriceResult)
                }
            }
        }
    }

    private fun observeEditStock() {
        observe(viewModel.editStockResult) {
            when (it) {
                is Success -> onSuccessEditStock(it.data.productId, it.data.stock, it.data.productName, it.data.status)
                is Fail -> {
                    onErrorEditStock(it.throwable as EditStockResult)
                }
            }
        }
    }

    private fun observeMultiEdit() {
        observe(viewModel.multiEditProductResult) {
            when(it) {
                is Success -> onSuccessMultiEditProducts(it.data)
                is Fail -> showErrorToast()

            }
        }
    }

    private fun showDeleteProductsConfirmationDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.product_manage_dialog_delete_products_title, itemsChecked.count()))
                setDescription(getString(R.string.product_manage_delete_product_description))
                setPrimaryCTAText(getString(R.string.product_manage_delete_product_delete_button))
                setSecondaryCTAText(getString(R.string.product_manage_delete_product_cancel_button))
                setPrimaryCTAClickListener {
                    val productIds = itemsChecked.map { item -> item.id }
                    viewModel.editProductsByStatus(productIds, DELETED)
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
            }.show()
        }
    }

    private fun showEditProductsInActiveConfirmationDialog() {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.product_manage_dialog_edit_products_inactive_title, itemsChecked.count()))
                setDescription(getString(R.string.product_manage_edit_products_inactive_description))
                setPrimaryCTAText(getString(R.string.product_manage_edit_products_inactive_button))
                setSecondaryCTAText(getString(R.string.product_manage_delete_product_cancel_button))
                setPrimaryCTAClickListener {
                    val productIds = itemsChecked.map { item -> item.id }
                    viewModel.editProductsByStatus(productIds, INACTIVE)
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
            }.show()
        }
    }

    private fun observeProductList() {
        observe(viewModel.productListResult) {
            when (it) {
                is Success -> {
                    initHeaderView(it.data)
                    showProductList(it.data)
                    enableMultiSelect()
                    renderMultiSelectProduct()
                }
                is Fail -> showErrorToast()
            }
            hidePageLoading()
            stopPerformanceMonitoring()
        }
    }

    private fun stopPerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
    }

    private fun observeFilterTabs() {
        observe(viewModel.productFiltersTab) {
            when(it) {
                is Success -> {
                    val data = it.data

                    if(data is ShowFilterTab) {
                        filterTab?.show(data)
                    } else {
                        filterTab?.update(data, this)
                    }
                    renderCheckedView()
                }
            }
        }
    }

    private fun observeProductListFeaturedOnly() {
        observe(viewModel.productListFeaturedOnlyResult) {
            when (it) {
                is Success -> productListFeaturedOnlySize = it.data
            }
        }
    }

    private fun observeMultiSelect() {
        observe(viewModel.toggleMultiSelect) { multiSelectEnabled ->
            if(multiSelectEnabled) {
                showMultiSelectView()
            } else {
                hideMultiSelectView()
                resetMultiSelect()
            }

            renderCheckedView()
            showHideProductCheckBox(multiSelectEnabled)
        }
    }

    private fun showHideProductCheckBox(multiSelectEnabled: Boolean) {
        productManageListAdapter.setMultiSelectEnabled(multiSelectEnabled)
    }

    private fun showMultiSelectView() {
        val cancelMultiSelectText = getString(R.string.product_manage_cancel_multiple_select)
        textMultipleSelect.text = cancelMultiSelectText
        checkBoxSelectAll.show()
    }

    private fun hideMultiSelectView() {
        val multiSelectText = getString(R.string.product_manage_multiple_select)
        textMultipleSelect.text = multiSelectText
        checkBoxSelectAll.hide()
        btnMultiEdit.hide()
    }

    private fun resetProductList() {
        clearAllData()
        resetMultiSelect()
        renderCheckedView()
    }

    private fun resetMultiSelect() {
        resetSelectAllCheckBox()
        clearSelectedProduct()
    }

    private fun enableMultiSelect() {
        setupMultiSelect()
        checkBoxSelectAll.isEnabled = true
        textMultipleSelect.isEnabled = true
    }

    private fun disableMultiSelect() {
        checkBoxSelectAll.isEnabled = false
        textMultipleSelect.isEnabled = false
    }

    private fun observeDeleteProduct() {
        observe(viewModel.deleteProductResult) {
            when (it) {
                is Success -> onSuccessDeleteProduct(it.data.productName, it.data.productId)
                is Fail -> onErrorDeleteProduct(it.throwable as DeleteProductResult)
            }
        }
    }

    private fun observeFilter() {
        observe(viewModel.selectedFilterAndSort) {
            clearAllData()
            resetMultiSelect()
            getProductList()
            setActiveFilterCount(it)
        }
    }

    private fun observeShopInfo() {
        observe(viewModel.shopInfoResult) {
            if (it is Success) {
                goldMerchant = it.data.isGoldMerchant
                isOfficialStore = it.data.isOfficialStore
                shopDomain = it.data.shopDomain
            }
        }
    }

    private fun observeViewState() {
        observe(viewModel.viewState) {
            when(it) {
                is ShowProgressDialog -> showLoadingProgress()
                is HideProgressDialog -> hideLoadingProgress()
                is RefreshList -> resetProductList()
            }
        }
    }

    private fun observeEditVariantPrice() {
        observe(viewModel.editVariantPriceResult) {
            when (it) {
                is Success -> {
                    productManageListAdapter.updatePrice(it.data)
                    val message = context?.getString(
                        R.string.product_manage_quick_edit_price_success,
                        it.data.productName
                    ).orEmpty()
                    showMessageToast(message)
                }
                is Fail -> showErrorMessageToast(it)
            }
        }
    }

    private fun observeEditVariantStock() {
        observe(viewModel.editVariantStockResult) {
            when (it) {
                is Success -> {
                    val message = context?.getString(
                        R.string.product_manage_quick_edit_stock_success,
                        it.data.productName
                    ).orEmpty()
                    updateVariantStock(it.data)
                    showMessageToast(message)
                }
                is Fail -> showErrorMessageToast(it)
            }
        }
    }

    // endregion

    private fun updateVariantStock(data: EditVariantResult) {
        val stock = data.countVariantStock()
        val status = data.getVariantStatus()

        productManageListAdapter.updateStock(data.productId, stock, status)

        filterTab?.getSelectedFilter()?.let {
            filterProductListByStatus(it)
            renderMultiSelectProduct()
        }
    }

    private fun showErrorMessageToast(result: Fail) {
        val error = result.throwable
        if (error is MessageErrorException) {
            val message = error.message.orEmpty()
            showErrorToast(message)
        } else {
            showErrorToast()
        }
    }

    private fun showPageLoading() {
        mainContainer.hide()
        pageLoading.show()
    }

    private fun hidePageLoading() {
        mainContainer.show()
        pageLoading.hide()
    }

    private fun initHeaderView(productList: List<ProductViewModel>) {
        if(isLoadingInitialData && showProductEmptyState()) {
            searchBar.showWithCondition(productList.isNotEmpty())
            tabSortFilter.showWithCondition(productList.isNotEmpty())
        }
    }

    private fun clearSelectedProduct() {
        itemsChecked.clear()
    }

    private fun resetSelectAllCheckBox() {
        if(checkBoxSelectAll.isChecked) {
            checkBoxSelectAll.isChecked = false
            checkBoxSelectAll.setIndeterminate(false)
        }
    }

    private fun renderProductCount() {
        val productCount = if(filterTab?.isFilterActive() == true) {
            filterTab?.getProductCount()
        } else {
            viewModel.getTotalProductCount()
        }
        textProductCount.text = getString(R.string.product_manage_count_format, productCount)
    }

    private fun goToProductDraft(imageUrls: ArrayList<String>?, imageDescList: ArrayList<String>?) {
        if (imageUrls != null && imageUrls.size > 0) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.PRODUCT_DRAFT)
            intent.putStringArrayListExtra(LOCAL_PATH_IMAGE_LIST, imageUrls)
            intent.putStringArrayListExtra(DESC_IMAGE_LIST, imageDescList)
            startActivity(intent)
        }
    }

    companion object {
        private const val LOCAL_PATH_IMAGE_LIST = "loca_img_list"
        private const val DESC_IMAGE_LIST = "desc_img_list"

        private const val MIN_FEATURED_PRODUCT = 0
        private const val MAX_FEATURED_PRODUCT = 5

    }

}