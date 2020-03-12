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
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gm.common.constant.IMG_URL_POWER_MERCHANT_IDLE_POPUP
import com.tokopedia.gm.common.constant.IMG_URL_REGULAR_MERCHANT_POPUP
import com.tokopedia.gm.common.constant.URL_POWER_MERCHANT_SCORE_TIPS
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.presentation.activity.ProductManageSetCashbackActivity
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_PRODUCT
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.view.adapter.ProductManageListAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductListItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToTabFilters
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.Default
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.feature.list.view.ui.ManageProductBottomSheet
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment.ProductManageQuickEditPriceFragment
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment.Companion.EDIT_STOCK_PRODUCT
import com.tokopedia.product.manage.item.common.util.ViewUtils
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_STOCK
import com.tokopedia.product.manage.item.main.duplicate.activity.ProductDuplicateActivity
import com.tokopedia.product.manage.item.main.edit.view.activity.ProductEditActivity
import com.tokopedia.product.manage.item.stock.view.activity.ProductBulkEditStockActivity
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.ETALASE_PICKER_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_FILTER_SELECTED
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_SORT_SELECTED
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.INSTAGRAM_SELECT_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_FILTER
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_SORT
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_STOCK_REMINDER
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.SET_CASHBACK_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.STOCK_EDIT_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.URL_TIPS_TRICK
import com.tokopedia.product.manage.oldlist.data.ConfirmationProductData
import com.tokopedia.product.manage.oldlist.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.oldlist.data.model.ProductManageFilterModel
import com.tokopedia.product.manage.oldlist.data.model.ProductManageSortModel
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3SuccessFailedResponse
import com.tokopedia.product.manage.oldlist.utils.ProductManageTracking
import com.tokopedia.product.manage.oldlist.view.bottomsheets.ConfirmationUpdateProductBottomSheet
import com.tokopedia.product.manage.oldlist.view.bottomsheets.EditProductBottomSheet
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByKeyword
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByPage
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

open class ProductManageFragment : BaseSearchListFragment<ProductViewModel, ProductManageAdapterFactory>(),
    BaseListCheckableAdapter.OnCheckableAdapterListener<ProductViewModel>,
    MerchantCommonBottomSheet.BottomSheetListener,
    BaseCheckableViewHolder.CheckableInteractionListener,
    ProductViewHolder.ProductViewHolderView,
    EditProductBottomSheet.EditProductInterface,
    FilterViewHolder.ProductFilterListener,
    ProductMenuViewHolder.ProductMenuListener {

    @Inject
    lateinit var viewModel: ProductManageViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var shopDomain: String = ""
    private var goldMerchant: Boolean = false
    private var isOfficialStore: Boolean = false
    private var productListFeaturedOnlySize: Int = 0
    private var dialogFeaturedProduct: DialogUnify? = null
    private var manageProductBottomSheet: ManageProductBottomSheet? = null
    private var filterProductBottomSheet: ProductManageFilterFragment? = null
    private var productManageFilterModel: ProductManageFilterModel = ProductManageFilterModel()
    private val productManageListAdapter by lazy { adapter as ProductManageListAdapter }

    private var productList: MutableList<ProductViewModel> = mutableListOf()
    private var etalaseType = BulkBottomSheetType.EtalaseType("", 0)
    private var stockType = BulkBottomSheetType.StockType()
    private var confirmationProductDataList: ArrayList<ConfirmationProductData> = arrayListOf()
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

        productManageFilterModel = ProductManageFilterModel()
        productManageFilterModel.reset()

        initView()
        loadInitialData()
    }

    private fun initView() {
        setupSearchBar()
        setupProductList()
        setupTabFilters()
        setupBottomSheet()
        renderCheckedView()

        observeShopInfo()
        observeUpdateProduct()
        observeDeleteProduct()
        observeProductListFeaturedOnly()
        observeProductList()

        observeEditPrice()
        observeEditStock()
        observeSetCashback()
        observeGetFreeClaim()
        observeGetPopUpInfo()

        observeSetFeaturedProduct()
        observeViewState()

        getProductListFeaturedOnlySize()
        getTopAdsFreeClaim()
        getGoldMerchantStatus()
        context?.let { dialogFeaturedProduct = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION) }
    }

    private fun setupSearchBar() {
        searchInputView.clearFocus()
        searchInputView.closeImageButton.setOnClickListener {
            searchInputView.searchText = ""
            loadInitialData()
        }
        searchInputView.setSearchHint(getString(R.string.product_manage_search_hint))
    }

    private fun setupBottomSheet() {
        manageProductBottomSheet = ManageProductBottomSheet(view, this, fragmentManager)
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

            addProductMenu.setOnMenuItemClickListener { menuItem ->
                startActivity(ProductAddNameCategoryActivity.createInstance(activity))
                ProductManageTracking.eventProductManageTopNav(menuItem.title.toString())
                true
            }

            importFromInstagramMenu.setOnMenuItemClickListener { menuItem ->
                val intent = AddProductImagePickerBuilder.createPickerIntentInstagramImport(context)
                startActivityForResult(intent, INSTAGRAM_SELECT_REQUEST_CODE)
                ProductManageTracking.eventProductManageTopNav(menuItem.title.toString())
                false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickProductFilter(filter: FilterViewModel, viewHolder: FilterViewHolder) {
        when(filter) {
            is Default -> {
                showFilterBottomSheet()
            }
            else -> {
                val selectedFilter = filter.status
                val currentFilter = tabFilters.selectedFilter?.status

                if(selectedFilter == currentFilter) {
                    renderList(productList)
                    tabFilters.resetSelectedFilter()
                } else {
                    tabFilters.resetAllFilter(viewHolder)
                    tabFilters.setSelectedFilter(filter)
                    filterProductByStatus(selectedFilter)
                }
            }
        }
    }

    private fun showFilterBottomSheet() {
        val savedInstanceManager = this.context?.let { SaveInstanceCacheManager(it, true) }
        savedInstanceManager?.let { cacheManager ->
            filterProductBottomSheet = context?.let { cacheManager.id?.let { id -> ProductManageFilterFragment.createInstance(it, id) } }
            setupFilterProductBottomSheet()
            this.childFragmentManager.let { filterProductBottomSheet?.show(it,"BottomSheetTag") }
        }
    }

    private fun setupFilterProductBottomSheet() {
//        filterProductBottomSheet?.let { bottomSheet ->
//            bottomSheet.setOnDismissListener {
//                if(bottomSheet.isResultReady) {
//                    bottomSheet.isResultReady = false
//                    val cacheManager = context?.let { SaveInstanceCacheManager(it, bottomSheet.resultCacheManagerId) }
//                    val filterOptionWrapper: FilterOptionWrapper? = cacheManager?.get(ProductManageFilterFragment.SELECTED_FILTER, FilterOptionWrapper::class.java)
//                    filterOptionWrapper?.let {
//                        viewModel.getProductList(userSession.shopId, filterOptionWrapper.filterOptions, filterOptionWrapper.sortOption)
//                    }
//                }
//            }
//        }
        filterProductBottomSheet?.let { bottomSheet ->
            bottomSheet.setOnDismissListener {
                if(bottomSheet.isResultReady) {
                    bottomSheet.isResultReady = false
                    viewModel.getProductList(userSession.shopId,
                            bottomSheet.selectedFilterOptions?.filterOptions,
                            bottomSheet.selectedFilterOptions?.sortOption, true)
                }
            }
        }
    }

    private fun filterProductByStatus(status: ProductStatus?) {
        val productList = productList.filter {
            it.status == status
        }
        renderList(productList)
    }

    private fun clearEtalaseAndStockData() {
        stockType = BulkBottomSheetType.StockType()
        etalaseType = BulkBottomSheetType.EtalaseType()
    }

    private fun renderCheckedView() {
        if (itemsChecked.size > 0) {
            textProductCount.visibility = View.VISIBLE
            textProductCount.text = getString(R.string.product_manage_bulk_count, itemsChecked.size.toString())
        } else {
            textProductCount.text = getString(R.string.product_manage_count_format, productList.count())
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

    override fun renderList(list: List<ProductViewModel>, hasNextPage: Boolean) {
        super.renderList(list, hasNextPage)
        renderCheckedView()
    }

    override fun onSearchSubmitted(text: String) {
        getProductList(keyword = text, isRefresh = true)
        ProductManageTracking.eventProductManageSearch()
    }

    override fun onSearchTextChanged(text: String?) {
        // NO OP
    }

    private fun getProductList(page: Int = 1, keyword: String? = null, isRefresh: Boolean = false) {
        val selectedFilter = filterProductBottomSheet?.selectedFilterOptions
        val filterOptions = createFilterOptions(page, keyword)
        val sortOption = selectedFilter?.sortOption

        viewModel.getProductList(userSession.shopId, filterOptions, sortOption, isRefresh)
    }

    private fun getProductListFeaturedOnlySize() {
        viewModel.getFeaturedProductCount(userSession.shopId)
    }

    private fun createFilterOptions(page: Int, keyword: String?): MutableList<FilterOption> {
        val selectedFilter = filterProductBottomSheet?.selectedFilterOptions
        val filterOptions = selectedFilter?.filterOptions.orEmpty()
            .toMutableList()

        filterOptions.addKeywordFilter(keyword)
        filterOptions.add(FilterByPage(page))
        return filterOptions
    }

    private fun MutableList<FilterOption>.addKeywordFilter(keyword: String?) {
        if(!keyword.isNullOrEmpty()) add(FilterByKeyword(keyword))
    }

    private fun loadEmptyList() {
        renderList(ArrayList())
    }

    private fun showProductList(productList: List<ProductViewModel>) {
        if(tabFilters.isActive()) {
            val selectedFilter = tabFilters.selectedFilter?.status
            filterProductByStatus(selectedFilter)
        } else {
            val hasNextPage = productList.isNotEmpty()
            renderList(productList, hasNextPage)
        }
    }

    private fun showTabFilters() {
        val tabFilterList = mapToTabFilters(productList)
        tabFilters.setData(tabFilterList)
    }

    private fun addProductList(products: List<ProductViewModel>) {
        productList.addAll(products)
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

    private fun onErrorSetCashback(setCashbackResult: SetCashbackResult) {
        if(setCashbackResult.limitExceeded) {
            context?.let {
                DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
                    setTitle(it.resources.getString(R.string.product_manage_set_cashback_dialog_title))
                    setDescription(it.resources.getString(R.string.product_manage_set_cashback_dialog_desc))
                    setPrimaryCTAText(it.resources.getString(R.string.product_manage_set_cashback_dialog_upgrade_button))
                    setSecondaryCTAText(it.resources.getString(R.string.product_manage_set_cashback_dialog_see_cashback_products_button))
                    setPrimaryCTAClickListener {
                        dismiss()
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                    }
                    setSecondaryCTAClickListener {
                        dismiss()
                        viewModel.getProductList(userSession.shopId, listOf(FilterOption.FilterByCondition.CashBackOnly))
                    }
                    setImageUrl(ProductManageUrl.ILLUSTRATION_SET_CASHBACK_LIMIT_REACHED)
                }.show()
            }
            return
        }
        Toaster.make(coordinatorLayout, getString(R.string.product_manage_snack_bar_fail),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.product_manage_snack_bar_retry),
                View.OnClickListener {
                     viewModel.setCashback(productId = setCashbackResult.productId,
                             productName = setCashbackResult.productName, cashback = setCashbackResult.cashback)
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

    private fun showRegularMerchantBottomSheet(featureName: String) {
        val title = getString(com.tokopedia.gm.common.R.string.bottom_sheet_regular_title, featureName)
        val description = getString(com.tokopedia.gm.common.R.string.bottom_sheet_regular_desc, featureName)
        val buttonName = getString(com.tokopedia.gm.common.R.string.bottom_sheet_regular_btn)
        showBottomSheet(title, IMG_URL_REGULAR_MERCHANT_POPUP, description, buttonName)
    }

    private fun showIdlePowerMerchantBottomSheet(featureName: String) {
        val title = getString(com.tokopedia.gm.common.R.string.bottom_sheet_idle_title, featureName)
        val description = getString(com.tokopedia.gm.common.R.string.bottom_sheet_idle_desc, featureName)
        val buttonName = getString(com.tokopedia.gm.common.R.string.bottom_sheet_idle_btn)
        showBottomSheet(title, IMG_URL_POWER_MERCHANT_IDLE_POPUP, description, buttonName)
    }

    private fun showBottomSheet(title: String, imageUrl: String, description: String, buttonName: String) {
        val model = MerchantCommonBottomSheet.BottomSheetModel(
            title,
            description,
            imageUrl,
            buttonName,
            ""
        )
        val bottomSheet = MerchantCommonBottomSheet.newInstance(model)
        bottomSheet.setListener(this)
        bottomSheet.show(childFragmentManager, "merchant_warning_bottom_sheet")
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

    private fun showErrorToast(message: String, actionLabel: String, listener: () -> Unit = {}) {
        view?.let {
            val onClickActionLabel = View.OnClickListener { listener.invoke() }
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionLabel, onClickActionLabel)
        }
    }

    private fun showSnackBarWithAction(message: String, listener: () -> Unit) {
        NetworkErrorHelper.createSnackbarWithAction(activity, message) { listener.invoke() }
            .showRetrySnackbar()

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
                    ProductManageTracking.trackerManageCourierButton()
                    RouteManager.route(context, ApplinkConst.SELLER_SHIPPING_EDITOR)
                    activity.finish()
                }

                btnGoToPdp.setOnClickListener {
                    ProductManageTracking.trackerSeeProduct()
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
                        ProductManageTracking.trackerLinkClick()
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

    private fun onSuccessBulkUpdateProduct(listOfResponse: ProductUpdateV3SuccessFailedResponse) {
        hideLoadingProgress()

        /**
         * When bulk update products, there's possibility one of the product failed to update from server so
         * this logic use for catch failed update products
         */
        if (listOfResponse.failedResponse.isNotEmpty()) {
            showErrorToast(getString(R.string.product_manage_bulk_snackbar, listOfResponse.successResponse.size.toString(), listOfResponse.failedResponse.size.toString()), getString(com.tokopedia.abstraction.R.string.retry_label)) {
                viewModel.updateMultipleProducts(viewModel.failedBulkDataMapper(listOfResponse.failedResponse, confirmationProductDataList))
            }
        } else {
            confirmationProductDataList.clear()
            clearEtalaseAndStockData()
            showMessageToast(getString(R.string.product_manage_bulk_snackbar_sucess, listOfResponse.successResponse.size.toString()))
        }

        productManageListAdapter.resetCheckedItemSet()
        itemsChecked.clear()
        renderCheckedView()
        loadInitialData()
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        searchInputView.searchTextView.text.clear()
        productManageListAdapter.resetCheckedItemSet()
        itemsChecked.clear()
        productList.clear()
        renderCheckedView()
    }

    private fun showMultipleUpdateErrorToast(e: Throwable) {
        activity?.let {
            val message = ViewUtils.getErrorMessage(it, e)
            showErrorToast(message, getString(com.tokopedia.design.R.string.close))
        }
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

    override fun isChecked(position: Int): Boolean =productManageListAdapter.isChecked(position)

    override fun updateListByCheck(isChecked: Boolean, position: Int) =productManageListAdapter.updateListByCheck(isChecked, position)

    override fun onClickStockInformation() {
    }

    override fun onClickEditStockButton(product: ProductViewModel) {
        val cacheManager = context?.let { SaveInstanceCacheManager(it,true) }
        cacheManager?.put(EDIT_STOCK_PRODUCT, product)
        val editStockBottomSheet = context?.let { cacheManager?.id?.let {
            cacheId -> ProductManageQuickEditStockFragment.createInstance(it, cacheId) } }
        editStockBottomSheet?.setOnDismissListener {
            if(editStockBottomSheet.editStockSuccess) {
                editStockBottomSheet.editStockSuccess = false
                val editStockCacheManager = context?.let { SaveInstanceCacheManager(it, editStockBottomSheet.cacheManagerId) }
                val modifiedProduct: ProductViewModel? = editStockCacheManager?.get(
                        EDIT_STOCK_PRODUCT, ProductViewModel::class.java)
                modifiedProduct?.let { product ->
                    product.stock?.let { stock ->
                        product.title?.let { title ->
                            product.status?.let { status ->
                                viewModel.editStock( product.id, stock, title, status) } } }
                }
            }
        }
        editStockBottomSheet?.show(childFragmentManager, "quick_edit_stock")
    }

    override fun onClickMoreOptionsButton(product: ProductViewModel) {
        hideSoftKeyboard()

        if (product.status == ProductStatus.MODERATED) {
            val errorMessage = getString(R.string.product_manage_desc_product_on_supervision, product.title)
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        } else {
            manageProductBottomSheet?.show(product)
        }
    }

    override fun onClickEditPriceButton(product: ProductViewModel) {
        val editPriceBottomSheet = context?.let { ProductManageQuickEditPriceFragment.createInstance(it, product.price ?: "") }
        editPriceBottomSheet?.setOnDismissListener {
            if(editPriceBottomSheet.editPriceSuccess) {
                editPriceBottomSheet.editPriceSuccess = false
                product.title?.let { viewModel.editPrice(product.id, editPriceBottomSheet.price, it) }
            }
        }
        editPriceBottomSheet?.show(childFragmentManager, "quick_edit_price")
    }

    override fun onClickOptionMenu(menu: ProductMenuViewModel) {
        val product = menu.product
        val productId = product.id
        val productName = product.title ?: ""
        val menuTitle = getString(menu.title)

        when(menu) {
            is Preview -> goToPDP(productId)
            is Duplicate -> clickDuplicateProduct(productId, menuTitle)
            is StockReminder -> { onSetStockReminderClicked(product)}
            is Delete -> clickDeleteProductMenu(productName, productId)
            is SetTopAds -> onPromoTopAdsClicked(product)
            is SetCashBack -> onSetCashbackClicked(product)
            is SetFeaturedProduct -> onSetFeaturedProductClicked(product)
            is RemoveFeaturedProduct -> onRemoveFeaturedProductClicked(product)
        }

        manageProductBottomSheet?.dismiss()
    }

    private fun clickDuplicateProduct(productId: String, menuTitle: String) {
        goToDuplicateProduct(productId)
        ProductManageTracking.eventProductManageOverflowMenu(menuTitle)
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
                productManageViewModel.isFeatured?.let {
                    if(productListFeaturedOnlySize == 5 && !it) {
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
                            RouteManager.route(context, ApplinkConstInternalMarketplace.GOLD_MERCHANT_FEATURED_PRODUCT)
                        }
                        dialogFeaturedProduct?.show()
                    }
                    else {
                        setDialogFeaturedProduct(
                                ProductManageUrl.ILLUSTRATION_ADD_FEATURED_PRODUCT_DOMAIN,
                                getString(R.string.product_featured_add_dialog_title),
                                getString(R.string.product_featured_add_dialog_desc),
                                getString(R.string.product_featured_add_dialog_primary_cta),
                                getString(R.string.product_featured_add_dialog_secondary_cta)
                        )
                        dialogFeaturedProduct?.setPrimaryCTAClickListener {
                            productListFeaturedOnlySize += 1
                            showLoadingProgress()
                            setFeaturedProduct(productManageViewModel.id, ProductManageListConstant.FEATURED_PRODUCT_ADD_STATUS)
                            dialogFeaturedProduct?.dismiss()
                        }
                        dialogFeaturedProduct?.setSecondaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                        dialogFeaturedProduct?.show()
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
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.let {
            val intent = Intent(this.activity,ProductManageSetCashbackActivity::class.java)
            it.put(SET_CASHBACK_PRODUCT, productManageViewModel)
            intent.putExtra(SET_CASHBACK_CACHE_MANAGER_KEY, it.id)
            startActivityForResult(intent, SET_CASHBACK_REQUEST_CODE)
        }
    }

    private fun downloadBitmap(product: ProductViewModel) {
        activity?.let {
            val price = product.price
            val productShare = ProductShare(it, ProductShare.MODE_IMAGE)

            val cashBackText = if (product.cashBack > 0) {
                getString(R.string.pml_sticker_cashback, product.cashBack)
            } else {
                ""
            }

            val data = ProductData().apply {
                priceText = "Rp $price"
                cashbacktext = cashBackText
                productId = product.id
                productName = product.title
                productUrl = product.url
                productImageUrl = product.imageUrl
                shopUrl = getString(R.string.pml_sticker_shop_link, shopDomain)
            }
            
            productShare.share(data, { showLoadingProgress() }, { hideLoadingProgress() })
        }
    }

    private fun showDialogVariantPriceLocked() {
        activity?.let {
            val alertDialogBuilder = AlertDialog.Builder(it, com.tokopedia.design.R.style.AppCompatAlertDialogStyle)
                .setTitle(getString(com.tokopedia.product.manage.item.R.string.product_price_locked))
                .setMessage(getString(com.tokopedia.product.manage.item.R.string.product_price_locked_manage_desc))
                .setPositiveButton(getString(com.tokopedia.design.R.string.close)) { _, _ ->
                    // no op, just dismiss
                }
            val dialog = alertDialogBuilder.create()
            dialog.show()
        }
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
            }
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun onClickProductItem(product: ProductViewModel) {
        goToPDP(product.id)
        ProductManageTracking.eventProductManageClickDetail()
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

    override fun goToEtalasePicker(etalaseId: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_ETALASE_PICKER,
            etalaseId.toString())
        startActivityForResult(intent, ETALASE_PICKER_REQUEST_CODE)
    }

    override fun goToEditStock() {
        activity?.let {
            startActivityForResult(ProductBulkEditStockActivity.createIntent(it), STOCK_EDIT_REQUEST_CODE)
        }
    }

    override fun goToConfirmationBottomSheet(isActionDelete: Boolean) {
        confirmationProductDataList = viewModel.mapToProductConfirmationData(isActionDelete, stockType, etalaseType, itemsChecked)
        val confirmationUpdateProductBottomSheet = ConfirmationUpdateProductBottomSheet.newInstance(confirmationProductDataList)
        confirmationUpdateProductBottomSheet.setListener(this)
        fragmentManager?.let {
            confirmationUpdateProductBottomSheet.show(it, "bs_update_product")
        }
    }

    override fun updateProduct() {
        viewModel.updateMultipleProducts(confirmationProductDataList)
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
                    productManageFilterModel = it.getParcelableExtra(EXTRA_FILTER_SELECTED)
                    loadInitialData()
                    ProductManageTracking.trackingFilter(productManageFilterModel)
                }
                ETALASE_PICKER_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val etalaseId = it.getIntExtra(ProductExtraConstant.EXTRA_ETALASE_ID, -1)
                    val etalaseNameString = it.getStringExtra(ProductExtraConstant.EXTRA_ETALASE_NAME)
                    etalaseType.etalaseId = etalaseId
                    etalaseType.etalaseValue = etalaseNameString
                }
                STOCK_EDIT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val isActive = it.getBooleanExtra(EXTRA_STOCK, false)
                    val productStock: Int
                    productStock = if (isActive) 1 else 0
                    stockType.stockStatus = productStock
                }
                REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK) {
                    val productManageSortModel: ProductManageSortModel = it.getParcelableExtra(EXTRA_SORT_SELECTED)
                    loadInitialData()
                    ProductManageTracking.eventProductManageSortProduct(productManageSortModel.titleSort)
                }
                REQUEST_CODE_STOCK_REMINDER -> if(resultCode == Activity.RESULT_OK) {
                    val productName = it.getStringExtra(EXTRA_PRODUCT_NAME)
                    Toaster.make(coordinatorLayout, getString(R.string.product_stock_reminder_toaster_success_desc, productName), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                }
                SET_CASHBACK_REQUEST_CODE -> if(resultCode == Activity.RESULT_OK) {
                    val cacheManagerId = it.getStringExtra(SET_CASHBACK_CACHE_MANAGER_KEY)
                    val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, cacheManagerId) }
                    val product: ProductViewModel? = cacheManager?.get(SET_CASHBACK_PRODUCT, ProductViewModel::class.java)
                    product?.let { modifiedProduct ->
                        modifiedProduct.title?.let { title ->
                            viewModel.setCashback(modifiedProduct.id, title, modifiedProduct.cashBack)
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

    private fun observeSetCashback() {
        observe(viewModel.setCashbackResult) {
            when (it) {
                is Success -> {
                    onSuccessSetCashback(it.data)
                }
                is Fail -> {
                    onErrorSetCashback(it.throwable as SetCashbackResult)
                }
            }
        }
    }

    private fun observeProductList() {
        observe(viewModel.productListResult) {
            when (it) {
                is Success -> {
                    addProductList(it.data)
                    showProductList(it.data)
                    showTabFilters()
                }
                is Fail -> loadEmptyList()
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

    private fun observeUpdateProduct() {
        observe(viewModel.updateProductResult) {
            when (it) {
                is Success -> onSuccessBulkUpdateProduct(it.data)
                is Fail -> showMultipleUpdateErrorToast(it.throwable)
            }
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

    private fun clearProductList() {
        renderList(emptyList())
        productList.clear()
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