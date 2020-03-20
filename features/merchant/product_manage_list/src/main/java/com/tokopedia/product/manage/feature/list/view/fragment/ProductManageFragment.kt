package com.tokopedia.product.manage.feature.list.view.fragment

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
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gm.common.constant.URL_POWER_MERCHANT_SCORE_TIPS
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_VALUE
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_RESULT
import com.tokopedia.product.manage.feature.etalase.view.activity.EtalasePickerActivity
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.EXTRA_ETALASE_ID
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.EXTRA_ETALASE_NAME
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.REQUEST_CODE_PICK_ETALASE
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.utils.ProductManageTracking
import com.tokopedia.product.manage.feature.list.view.adapter.ProductManageListAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductListItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.TabFilterViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.model.SearchEmptyModel
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.ProductEmptyModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductManageBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.StockInformationBottomSheet
import com.tokopedia.product.manage.feature.multiedit.ui.bottomsheet.ProductMultiEditBottomSheet
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getRetryMessage
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getSuccessMessage
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment.ProductManageQuickEditPriceFragment
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_STOCK
import com.tokopedia.product.manage.item.main.duplicate.activity.ProductDuplicateActivity
import com.tokopedia.product.manage.item.main.edit.view.activity.ProductEditActivity
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_THRESHOLD
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.INSTAGRAM_SELECT_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_FILTER
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_SORT
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_STOCK_REMINDER
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.SET_CASHBACK_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.STOCK_EDIT_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.URL_TIPS_TRICK
import com.tokopedia.product.manage.oldlist.data.model.BulkBottomSheetType
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

open class ProductManageFragment : BaseListFragment<ProductViewModel, ProductManageAdapterFactory>(),
    BaseListCheckableAdapter.OnCheckableAdapterListener<ProductViewModel>,
    MerchantCommonBottomSheet.BottomSheetListener,
    BaseCheckableViewHolder.CheckableInteractionListener,
    ProductViewHolder.ProductViewHolderView,
    TabFilterViewHolder.ProductFilterListener,
    ProductMenuViewHolder.ProductMenuListener,
    ProductMultiEditBottomSheet.MultiEditListener,
    ProductManageFilterFragment.OnFinishedListener,
    ProductManageQuickEditPriceFragment.OnFinishedListener,
    ProductManageQuickEditStockFragment.OnFinishedListener {

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
    private var multiEditBottomSheet: ProductMultiEditBottomSheet? = null
    private val stockInfoBottomSheet by lazy { StockInformationBottomSheet(view, fragmentManager) }

    private val productManageListAdapter by lazy { adapter as ProductManageListAdapter }
    private val checkedPositionList: HashSet<Int> = hashSetOf()

    private var stockType = BulkBottomSheetType.StockType()
    private var itemsChecked: MutableList<ProductViewModel> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
        setupSearchBar()
        setupProductList()
        setupTabFilters()
        setupBottomSheet()
        setupMultiSelect()
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

        getFiltersTab()
        getProductListFeaturedOnlySize()
        getTopAdsFreeClaim()
        getGoldMerchantStatus()
        context?.let { dialogFeaturedProduct = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION) }
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
                startActivity(ProductAddNameCategoryActivity.createInstance(activity))
                true
            }

            importFromInstagramMenu.setOnMenuItemClickListener {
                val intent = AddProductImagePickerBuilder.createPickerIntentInstagramImport(context)
                startActivityForResult(intent, INSTAGRAM_SELECT_REQUEST_CODE)
                false
            }

            ProductManageTracking.eventAddProduct()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickMoreFilter(filter: FilterViewModel, tabName: String) {
        showFilterBottomSheet()
        ProductManageTracking.eventInventory(tabName)
    }

    override fun onClickProductFilter(filter: FilterViewModel, viewHolder: TabFilterViewHolder, tabName: String) {
        clickStatusFilterTab(filter, viewHolder)
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
        viewModel.setSelectedFilterAndSort(selectedData)
    }

    override fun onFinishEditPrice(product: ProductViewModel) {
        product.title?.let { product.price?.let { price -> viewModel.editPrice(product.id, price, it) } }
    }

    override fun onFinishEditStock(modifiedProduct: ProductViewModel) {
        if(modifiedProduct.stock != null && modifiedProduct.title != null && modifiedProduct.status != null) {
            viewModel.editStock(modifiedProduct.id, modifiedProduct.stock, modifiedProduct.title, modifiedProduct.status)
        }
    }

    override fun getEmptyDataViewModel(): EmptyModel {
        return if(showProductEmptyState()) {
            ProductEmptyModel
        } else {
            SearchEmptyModel
        }
    }

    private fun showProductEmptyState(): Boolean {
        val selectedFilters = viewModel.selectedFilterAndSort.value
        val searchKeyword = searchBar.searchBarTextField.text.toString()
        return searchKeyword.isEmpty() && selectedFilters == null && !tabFilters.isActive()
    }

    private fun setupSearchBar() {
        searchBar.clearFocus()

        searchBar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBar.clearFocus()
                getProductList(isRefresh = true)
                return@setOnEditorActionListener true
            }
            false
        }

        searchBar.searchBarIcon.setOnClickListener {
            clearSearchBarInput()
            clearProductList()
            loadInitialData()
        }

        searchBar.searchBarPlaceholder = getString(R.string.product_manage_search_hint)
    }

    private fun setupBottomSheet() {
        productManageBottomSheet = ProductManageBottomSheet(view, this, fragmentManager)
        multiEditBottomSheet = ProductMultiEditBottomSheet(view, this, fragmentManager)
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

    private fun showFilterBottomSheet() {
        filterProductBottomSheet = context?.let {
            ProductManageFilterFragment.createInstance(it, viewModel.selectedFilterAndSort.value,this)
        }
        this.childFragmentManager.let { filterProductBottomSheet?.show(it,"BottomSheetTag") }
    }

    private fun setTabFilterCount(filter: FilterOptionWrapper) {
        val sortCount =  if(filter.sortOption == null) 0 else 1
        val filterCount = filter.filterOptions.count() + sortCount
        tabFilters.setFilterCount(filterCount)
    }

    private fun clickStatusFilterTab(filter: FilterViewModel, viewHolder: TabFilterViewHolder) {
        val selectedFilter = filter.status
        val currentFilter = tabFilters.selectedFilter?.status

        if (selectedFilter == currentFilter) {
            tabFilters.resetSelectedFilter()
        } else {
            tabFilters.resetAllFilter(viewHolder)
            tabFilters.setSelectedFilter(filter)
        }

        getProductList(isRefresh = true)
    }

    private fun renderCheckedView() {
        if (itemsChecked.size > 0) {
            btnMultiEdit.show()
            textProductCount.show()
            textProductCount.text = getString(R.string.product_manage_bulk_count, itemsChecked.size.toString())
        } else {
            btnMultiEdit.hide()
            textProductCount.text = getString(R.string.product_manage_count_format, adapter.data.count())
        }
    }

    private fun setupProductList() {
        recycler_view.clearItemDecoration()
        recycler_view.addItemDecoration(ProductListItemDecoration())
    }

    private fun setupTabFilters() {
        tabFilters.init(this)
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
                        clearProductList()
                        loadInitialData()
                    }
                }
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<ProductViewModel, ProductManageAdapterFactory> {
        return ProductManageListAdapter(adapterTypeFactory, this)
    }

    override fun getAdapterTypeFactory(): ProductManageAdapterFactory = ProductManageAdapterFactory(this, this)

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

    private fun getProductList(page: Int = 1, isRefresh: Boolean = false) {
        val keyword = searchBar.searchBarTextField.text.toString()
        val selectedFilter = viewModel.selectedFilterAndSort.value
        val filterOptions = createFilterOptions(page, keyword)
        val sortOption = selectedFilter?.sortOption

        tabFilters.selectedFilter?.status?.let {
            filterOptions.add(FilterByStatus(it))
        }

        viewModel.getProductList(userSession.shopId, filterOptions, sortOption, isRefresh)
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
        renderMultiSelectProduct()
    }

    private fun renderMultiSelectProduct() {
        val shouldShow = adapter.data
            .filterIsInstance<ProductViewModel>()
            .isNotEmpty()

        multiSelectContainer.showWithCondition(shouldShow)
    }

    private fun onErrorEditPrice(editPriceResult: EditPriceResult) {
        Toaster.make(coordinatorLayout, getString(R.string.product_manage_snack_bar_fail),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.product_manage_snack_bar_retry),
                View.OnClickListener {
                    viewModel.editPrice(editPriceResult.productId, editPriceResult.price, editPriceResult.productName)
                })
    }

    private fun onErrorEditStock(editStockResult: EditStockResult) {
        Toaster.make(coordinatorLayout, getString(R.string.product_manage_snack_bar_fail),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.product_manage_snack_bar_retry),
                View.OnClickListener {
                    viewModel.editStock(editStockResult.productId, editStockResult.stock, editStockResult.productName, editStockResult.status)
                })
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
    }

    private fun onSuccessSetCashback(setCashbackResult: SetCashbackResult) {
        Toaster.make(coordinatorLayout, getString(
                R.string.product_manage_set_cashback_success, setCashbackResult.productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        productManageListAdapter.updateCashback(setCashbackResult.productId, setCashbackResult.cashback)
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
                }
                setImageUrl(ProductManageUrl.ILLUSTRATION_SET_CASHBACK_LIMIT_REACHED)
            }.show()
        }
    }

    private fun onErrorDeleteProduct(deleteProductResult: DeleteProductResult) {
        Toaster.make(coordinatorLayout, getString(R.string.product_manage_delete_product_fail),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.product_manage_snack_bar_retry),
                View.OnClickListener {
                    viewModel.deleteSingleProduct(deleteProductResult.productName, deleteProductResult.productId)
                })
    }

    private fun onSuccessDeleteProduct(productName: String, productId: String) {
        Toaster.make(coordinatorLayout, getString(
                R.string.product_manage_delete_product_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        productManageListAdapter.deleteProduct(productId)
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
        clearSelectedProduct()
        renderCheckedView()
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
            is EditByStatus -> updateProductListStatus(productIds, result.status)
            is EditByMenu -> viewModel.toggleMultiSelect()
        }
    }

    private fun updateProductListStatus(productIds: List<String>, status: ProductStatus) {
        productIds.forEach { productId ->
            if(status == INACTIVE) {
                val index = adapter.data.indexOfFirst { it.id == productId }

                if(index >= 0) {
                    adapter.data[index] = adapter.data[index].copy(status = status)
                    productManageListAdapter.updateInactiveProducts(productId)
                }
            }
            if(status == DELETED) {
                adapter.data.removeFirst { it.id == productId }
                productManageListAdapter.deleteProduct(productId)
            }
        }
    }

    override fun onSwipeRefresh() {
        clearProductList()
        clearSelectedProduct()
        renderCheckedView()
        getFiltersTab()
        super.onSwipeRefresh()
    }

    private fun clearSearchBarInput() {
        searchBar.searchBarTextField.text.clear()
    }

    override fun onItemChecked(data: ProductViewModel, isChecked: Boolean) {
        if (isChecked) {
            itemsChecked.add(data)
        } else {
            /**
             * When refresh the data , it will keept the check
             * but the id *ex:ProductManageViewModel@12xxx will also update
             * then we cant remove it from itemsChecked because the id is different.
             */
            val checkedData = itemsChecked.find {
                it.id.contains(data.id)
            }
            itemsChecked.remove(checkedData)
        }
        renderCheckedView()
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

    override fun onBottomSheetButtonClicked() {
        if (viewModel.isIdlePowerMerchant()) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_POWER_MERCHANT_SCORE_TIPS)
        } else if (!viewModel.isPowerMerchant()) {
            RouteManager.route(context, ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE)
        }
    }

    override fun isChecked(position: Int): Boolean {
        val selectedItem = adapter.data[position]
        return itemsChecked.contains(selectedItem)
    }

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        if (isChecked) {
            checkedPositionList.add(position)
        } else {
            checkedPositionList.remove(position)
        }

        val selectedItem = adapter.data[position]
        onItemChecked(selectedItem, isChecked)

        productManageListAdapter.setCheckedPositionList(checkedPositionList)
    }

    override fun onClickStockInformation() {
        stockInfoBottomSheet.show()
    }

    override fun onClickEditStockButton(product: ProductViewModel) {
        val editStockBottomSheet = context?.let { ProductManageQuickEditStockFragment.createInstance(it, product, this) }
        editStockBottomSheet?.show(childFragmentManager, "quick_edit_stock")
        ProductManageTracking.eventEditStock(product.id)
    }

    override fun onClickEditVariantButton(product: ProductViewModel) {
        goToEditProduct(product.id)
        ProductManageTracking.eventEditVariants(product.id)
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
            productManageBottomSheet?.show(product)
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
        RouteManager.route(activity, ProductManageUrl.PRODUCT_VIOLATION_HELP_URL)
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
                        if(productListFeaturedOnlySize == 5) {
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
                                val filterOptionWrapper = FilterOptionWrapper(null, listOf(FilterByCondition.FeaturedOnly))
                                viewModel.setSelectedFilterAndSort(filterOptionWrapper)
                            }
                            dialogFeaturedProduct?.show()
                        } else if(productListFeaturedOnlySize == 0) {
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
                            }
                            dialogFeaturedProduct?.setSecondaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                            dialogFeaturedProduct?.show()
                        } else {
                           addFeaturedProduct(productManageViewModel.id)
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
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.STOCK_REMINDER, productManageViewModel.id, productManageViewModel.title)
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
        val newUri = UriUtil.buildUri(ApplinkConstInternalMarketplace.SET_CASHBACK, productManageViewModel.id, productManageViewModel.title)
        val uri = Uri.parse(newUri)
                .buildUpon()
                .appendQueryParameter(PARAM_SET_CASHBACK_VALUE, productManageViewModel.cashBack.toString())
                .appendQueryParameter(PARAM_SET_CASHBACK_PRODUCT_PRICE, productManageViewModel.price)
                .build()
                .toString()
        val intent = RouteManager.getIntent(context, uri)
        startActivityForResult(intent, SET_CASHBACK_REQUEST_CODE)
    }

    private fun goToDuplicateProduct(productId: String) {
        activity?.let {
            val intent = ProductDuplicateActivity.createInstance(it, productId)
            startActivity(intent)
        }
    }

    private fun goToEditProduct(productId: String) {
        val intent = ProductEditActivity.createInstance(activity, productId)
        startActivity(intent)
    }

    private fun addFeaturedProduct(productId: String) {
        productListFeaturedOnlySize += 1
        showLoadingProgress()
        setFeaturedProduct(productId, ProductManageListConstant.FEATURED_PRODUCT_ADD_STATUS)
        ProductManageTracking.eventFeaturedProductPopUpSave()
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
                REQUEST_CODE_FILTER -> if (resultCode == Activity.RESULT_OK) {
                    loadInitialData()
                }
                REQUEST_CODE_PICK_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                    val productIds = itemsChecked.map{ product -> product.id }
                    val etalaseId = it.getStringExtra(EXTRA_ETALASE_ID)
                    val etalaseName = it.getStringExtra(EXTRA_ETALASE_NAME)

                    viewModel.editProductsEtalase(productIds, etalaseId, etalaseName)
                }
                STOCK_EDIT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val isActive = it.getBooleanExtra(EXTRA_STOCK, false)
                    val productStock: Int
                    productStock = if (isActive) 1 else 0
                    stockType.stockStatus = productStock
                }
                REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK) {
                    loadInitialData()
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

    private fun getFiltersTab() {
        viewModel.getFiltersTab(userSession.shopId)
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
        hideLoadingProgress()
        observe(viewModel.setFeaturedProductResult) {
            if(viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                when (it) {
                    is Success -> onSuccessChangeFeaturedProduct(it.data.productId, it.data.status)
                    is Fail -> onFailedChangeFeaturedProduct(it.throwable)
                }
                hideLoadingProgress()
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
                    showProductList(it.data)
                    initHeaderView(it.data)
                }
                is Fail -> showErrorToast()
            }
        }
    }

    private fun observeFilterTabs() {
        observe(viewModel.productFiltersTab) {
            tabFilters.setData(it)
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
        val multiSelectText = getString(R.string.product_manage_multiple_select)
        val cancelMultiSelectText = getString(R.string.product_manage_cancel_multiple_select)

        observe(viewModel.toggleMultiSelect) { multiSelectEnabled ->
            val productList = adapter.data.map {
                it.copy(multiSelectActive = multiSelectEnabled)
            }

            if(multiSelectEnabled) {
                textMultipleSelect.text = cancelMultiSelectText
            } else {
                btnMultiEdit.hide()
                textMultipleSelect.text = multiSelectText
            }

            clearProductList()
            clearSelectedProduct()
            renderCheckedView()

            showProductList(productList)
        }
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
            setTabFilterCount(it)
            getProductList(isRefresh = true)
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
                is RefreshList -> clearProductList()
            }
        }
    }
    // endregion

    private fun initHeaderView(productList: List<ProductViewModel>) {
        if(isLoadingInitialData && showProductEmptyState()) {
            searchBar.showWithCondition(productList.isNotEmpty())
            tabFilters.showWithCondition(productList.isNotEmpty())
        }
    }

    private fun clearSelectedProduct() {
        itemsChecked.clear()
        checkedPositionList.clear()
        productManageListAdapter.resetCheckedItemSet()
    }

    private fun clearProductList() {
        adapter.clearAllElements()
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
    }
}