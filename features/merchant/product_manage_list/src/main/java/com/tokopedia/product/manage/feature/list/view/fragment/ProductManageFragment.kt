package com.tokopedia.product.manage.feature.list.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.*
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.KMNumbers
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
import com.tokopedia.gm.common.constant.IMG_URL_POWER_MERCHANT_IDLE_POPUP
import com.tokopedia.gm.common.constant.IMG_URL_REGULAR_MERCHANT_POPUP
import com.tokopedia.gm.common.constant.URL_POWER_MERCHANT_SCORE_TIPS
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.view.adapter.ProductFilterAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.ProductManageListAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductFilterItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductListItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.model.*
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ViewState.HideProgressDialog
import com.tokopedia.product.manage.feature.list.view.model.ViewState.ShowProgressDialog
import com.tokopedia.product.manage.feature.list.view.ui.ManageProductBottomSheet
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment.ProductManageQuickEditPriceFragment
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
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.ERROR_CODE_LIMIT_CASHBACK
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.ETALASE_PICKER_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_FILTER_SELECTED
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_SORT_SELECTED
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.INSTAGRAM_SELECT_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_FILTER
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_SORT
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.REQUEST_CODE_STOCK_REMINDER
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.STOCK_EDIT_REQUEST_CODE
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.URL_TIPS_TRICK
import com.tokopedia.product.manage.oldlist.constant.option.CashbackOption
import com.tokopedia.product.manage.oldlist.data.ConfirmationProductData
import com.tokopedia.product.manage.oldlist.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.oldlist.data.model.ProductManageFilterModel
import com.tokopedia.product.manage.oldlist.data.model.ProductManageSortModel
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3SuccessFailedResponse
import com.tokopedia.product.manage.oldlist.utils.ProductManageTracking
import com.tokopedia.product.manage.oldlist.view.bottomsheets.ConfirmationUpdateProductBottomSheet
import com.tokopedia.product.manage.oldlist.view.bottomsheets.EditProductBottomSheet
import com.tokopedia.product.manage.oldlist.view.fragment.ProductManageEditPriceDialogFragment
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
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
    private var manageProductBottomSheet: ManageProductBottomSheet? = null
    private var filterProductBottomSheet: ProductManageFilterFragment? = null

    private var productManageFilterModel: ProductManageFilterModel = ProductManageFilterModel()
    private val productManageListAdapter by lazy { adapter as ProductManageListAdapter }
    private val productFilterAdapter by lazy { ProductFilterAdapter(this) }

    private var productManageViewModels: MutableList<ProductViewModel> = mutableListOf()
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
        setupProductFilters()
        setupBottomSheet()
        renderCheckedView()

        observeShopInfo()
        observeUpdateProduct()
        observeDeleteProduct()

        observeProductFilters()
        observeProductList()

        observeEditPrice()
        observeGetFreeClaim()
        observeGetPopUpInfo()

        observeSetCashBack()
        observeSetFeaturedProduct()
        observeViewState()

        getTopAdsFreeClaim()
        getGoldMerchantStatus()
    }

    private fun setupSearchBar() {
        searchInputView.clearFocus()
        searchInputView.closeImageButton.setOnClickListener {
            searchInputView.searchText = ""
            loadInitialData()
        }
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
        filterProductList(filter)
        resetProductFilters(viewHolder)
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
                            bottomSheet.selectedFilterOptions?.sortOption)
                }
            }
        }
    }

    private fun filterProductList(filter: FilterViewModel) {
        // TODO: Handle filter product here
        when(filter) {
            is Default -> {}
            is Active -> {}
            is InActive -> {}
            is Banned -> {}
        }
    }

    private fun resetProductFilters(selectedFilter: FilterViewHolder) {
        for(i in 0..productFilterAdapter.itemCount) {
            val viewHolder = productFilterList.findViewHolderForAdapterPosition(i)
            if(viewHolder is FilterViewHolder && viewHolder != selectedFilter) {
                viewHolder.resetFilter()
            }
        }
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
            textProductCount.text = getString(R.string.product_manage_count_format, productManageViewModels.count())
        }
    }

    private fun setupProductList() {
        recycler_view.clearItemDecoration()
        recycler_view.addItemDecoration(ProductListItemDecoration())
    }

    private fun setupProductFilters() {
        with(productFilterList) {
            adapter = productFilterAdapter
            layoutManager = LinearLayoutManager(this@ProductManageFragment.context, HORIZONTAL, false)
            addItemDecoration(ProductFilterItemDecoration())
            isNestedScrollingEnabled = false
        }
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
        val filters = listOf(FilterByPage(page))
        viewModel.getProductList(userSession.shopId, filters)
    }

    override fun renderList(list: List<ProductViewModel>, hasNextPage: Boolean) {
        super.renderList(list, hasNextPage)
        renderCheckedView()
    }

    override fun onSearchSubmitted(text: String) {
        ProductManageTracking.eventProductManageSearch()
        loadInitialData()
    }

    override fun onSearchTextChanged(text: String?) {
        // NO OP
    }

    private fun loadEmptyList() {
        renderList(ArrayList())
    }

    private fun showProductList(productList: List<ProductViewModel>) {
        productManageViewModels = productList.toMutableList()
        val hasNextPage = productList.isNotEmpty()
        renderList(productList, hasNextPage)
    }

    private fun onErrorEditPrice(productId: String, price: String, t: Throwable?) {
        context?.let {
            showSnackBarWithAction(ViewUtils.getErrorMessage(it, t)) {
                viewModel.editPrice(productId, price)
            }
        }
    }

    private fun onSuccessEditPrice(productId: String, price: String) {
       productManageListAdapter.updatePrice(productId, price)
    }

    private fun onErrorSetCashback(t: Throwable?, productId: String?, cashback: Int) {
        context?.let {
            if (t is MessageErrorException && t.errorCode == ERROR_CODE_LIMIT_CASHBACK) {
                if (viewModel.isIdlePowerMerchant()) {
                    showIdlePowerMerchantBottomSheet(getString(R.string.product_manage_feature_name_cashback))
                } else if (!viewModel.isPowerMerchant()) {
                    showRegularMerchantBottomSheet(getString(R.string.product_manage_feature_name_cashback))
                } else {
                    showSnackBarWithAction(ViewUtils.getErrorMessage(it, t)) {
                        viewModel.setCashback(productId ?: "", cashback)
                    }
                }
            } else {
                showSnackBarWithAction(ViewUtils.getErrorMessage(it, t)) {
                    viewModel.setCashback(productId ?: "", cashback)
                }
            }
        }
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

    private fun onErrorMultipleDeleteProduct(e: Throwable?) {
        activity?.let {
            showErrorToast(ViewUtils.getErrorMessage(it, e), getString(com.tokopedia.design.R.string.close))
        }
    }

    private fun onSuccessMultipleDeleteProduct() {
        showMessageToast(getString(R.string.product_manage_bulk_snackbar_sucess_delete))
        loadInitialData()
    }

    private fun showMessageToast(message: String) {
        view?.let {
            val actionLabel = getString(com.tokopedia.design.R.string.close)
            Toaster.make(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionLabel)
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

                val btnSubmit: Button = dialog.findViewById(R.id.btn_submit)
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
        productManageListAdapter.resetCheckedItemSet()
        itemsChecked.clear()
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
        hideLoadingProgress()
        showMessageToast(successMessage)
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
                Toaster.make(coordinatorLayout, getString(
                        R.string.quick_edit_stock_success, product.title),
                        Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                //Call GQL
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
                Toaster.make(coordinatorLayout, getString(
                        R.string.quick_edit_price_success, product.title),
                        Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                viewModel.editPrice(product.id, editPriceBottomSheet.price)
            }
        }
        editPriceBottomSheet?.show(childFragmentManager, "quick_edit_price")
    }

    override fun onClickOptionMenu(menu: ProductMenuViewModel) {
        val product = menu.product
        val productId = product.id
        val menuTitle = getString(menu.title)

        when(menu) {
            is Preview -> goToPDP(productId)
            is Duplicate -> clickDuplicateProduct(productId, menuTitle)
            is StockReminder -> { onSetStockReminderClicked(product)}
            is Delete -> clickDeleteProductMenu(productId, menuTitle)
            is SetTopAds -> onPromoTopAdsClicked(product)
            is SetCashBack -> onSetCashbackClicked(product)
            is SetFeaturedProduct -> {
                //handle SetFeaturedProduct here
            }
            is RemoveFeaturedProduct -> {
                //handle RemoveFeaturedProduct here
            }
        }

        manageProductBottomSheet?.dismiss()
    }

    private fun clickDuplicateProduct(productId: String, menuTitle: String) {
        goToDuplicateProduct(productId)
        ProductManageTracking.eventProductManageOverflowMenu(menuTitle)
    }

    private fun clickDeleteProductMenu(productId: String, menuTitle: String) {
        showDialogActionDeleteProduct(DialogInterface.OnClickListener { _, _ ->
            val label = menuTitle + " - " + getString(com.tokopedia.product.manage.item.R.string.label_delete)
            ProductManageTracking.eventProductManageOverflowMenu(label)
            viewModel.deleteSingleProduct(productId)
        }, DialogInterface.OnClickListener { dialog, _ ->
            val label = menuTitle + " - " + getString(com.tokopedia.product.manage.item.R.string.label_cancel)
            ProductManageTracking.eventProductManageOverflowMenu(label)
            dialog.dismiss()
        })
    }

    private fun hideSoftKeyboard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    private fun showDialogChangeProductPrice(productId: String, productPrice: String) {
        if (!isAdded) {
            return
        }

        val productManageEditPriceDialogFragment = ProductManageEditPriceDialogFragment.createInstance(productId, productPrice, 0, goldMerchant, isOfficialStore)
        productManageEditPriceDialogFragment.setListenerDialogEditPrice(object : ProductManageEditPriceDialogFragment.ListenerDialogEditPrice {
            override fun onSubmitEditPrice(productId: String, price: String, currencyId: String, currencyText: String) {
                viewModel.editPrice(productId, price)
            }
        })
        activity?.let {
            fragmentManager?.let {
                productManageEditPriceDialogFragment.show(it, "")
            }
        }
    }

    private fun onSetFeaturedProductClicked(productManageViewModel: ProductViewModel, setFeaturedType: Int) {
        viewModel.setFeaturedProduct(productManageViewModel.id, setFeaturedType)
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
        activity?.let {
            if (!GlobalConfig.isSellerApp()) {
                RouteManager.route(context, ApplinkConstInternalMarketplace.GOLD_MERCHANT_REDIRECT)
                return
            }
            showOptionCashback(productManageViewModel.id, productManageViewModel.price, productManageViewModel.price.toIntOrZero())
        }
    }


    private fun showOptionCashback(productId: String, productPrice: String?, productCashback: Int) {
        val bottomSheetBuilder = CheckedBottomSheetBuilder(activity)
            .setMode(BottomSheetBuilder.MODE_LIST)
            .addTitleItem(getString(R.string.product_manage_cashback_title))

        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productCashback, CashbackOption.CASHBACK_OPTION_3)
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productCashback, CashbackOption.CASHBACK_OPTION_4)
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productCashback, CashbackOption.CASHBACK_OPTION_5)
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productCashback, CashbackOption.CASHBACK_OPTION_NONE)

        val bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
            .setItemClickListener(onOptionCashbackClicked(productId))
            .createDialog()
        bottomSheetDialog.show()
    }

    private fun addCashbackBottomSheetItemMenu(
        bottomSheetBuilder: BottomSheetBuilder,
        productPrice: String?,
        productCashback: Int,
        @CashbackOption cashbackOption: Int
    ) {
        if (bottomSheetBuilder is CheckedBottomSheetBuilder && productPrice != null) {
            val productPricePlain = java.lang.Double.parseDouble(productPrice)
            val cashbackMenuText = getCashbackMenuText(cashbackOption, productPricePlain)

            bottomSheetBuilder.addItem(
                cashbackOption,
                cashbackMenuText,
                null,
                productCashback == cashbackOption
            )
        }
    }

    private fun onOptionCashbackClicked(productId: String): BottomSheetItemClickListener {
        return BottomSheetItemClickListener {
            when (it.itemId) {
                CashbackOption.CASHBACK_OPTION_3 -> viewModel.setCashback(productId, CashbackOption.CASHBACK_OPTION_3)
                CashbackOption.CASHBACK_OPTION_4 -> viewModel.setCashback(productId, CashbackOption.CASHBACK_OPTION_4)
                CashbackOption.CASHBACK_OPTION_5 -> viewModel.setCashback(productId, CashbackOption.CASHBACK_OPTION_5)
                CashbackOption.CASHBACK_OPTION_NONE -> viewModel.setCashback(productId, CashbackOption.CASHBACK_OPTION_NONE)
                else -> {
                }
            }
            ProductManageTracking.eventProductManageOverflowMenu(getString(R.string.product_manage_cashback_title) + " - " + it.title)
        }
    }

    private fun getCashbackMenuText(cashBackOption: Int, productPricePlain: Double): String {
        var cashbackText = getString(R.string.product_manage_cashback_option_none)
        val productPriceSymbol = "Rp"
        if (cashBackOption > 0) {
            cashbackText = getString(R.string.product_manage_cashback_option, cashBackOption.toString(),
                productPriceSymbol,
                KMNumbers.formatDouble2PCheckRound(cashBackOption.toDouble() * productPricePlain / 100f, productPriceSymbol != "Rp"))
        }
        return cashbackText
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

    private fun showDialogActionDeleteProduct(onClickListener: DialogInterface.OnClickListener, onCancelListener: DialogInterface.OnClickListener) {
        activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setTitle(com.tokopedia.product.manage.item.R.string.label_delete)
            alertDialog.setMessage(R.string.product_manage_dialog_delete_product)
            alertDialog.setPositiveButton(com.tokopedia.product.manage.item.R.string.label_delete, onClickListener)
            alertDialog.setNegativeButton(com.tokopedia.product.manage.item.R.string.label_cancel, onCancelListener)
            alertDialog.show()
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

    private fun observeSetCashBack() {
        observe(viewModel.setCashBackResult) {
            when (it) {
                is Fail -> {
                    val result = it.throwable as SetCashBackResult
                    onErrorSetCashback(result.error, result.productId, result.cashback)
                }
            }
        }
    }

    private fun observeEditPrice() {
        observe(viewModel.editPriceResult) {
            when (it) {
                is Success -> onSuccessEditPrice(it.data.productId, it.data.price)
                is Fail -> {
                    val result = it.throwable as EditPriceResult
                    onErrorEditPrice(result.productId, result.price, result.error)
                }
            }
        }
    }

    private fun observeProductList() {
        observe(viewModel.productListResult) {
            when (it) {
                is Success -> showProductList(it.data)
                is Fail -> loadEmptyList()
            }
        }
    }

    private fun observeProductFilters() {
        observe(viewModel.productFilters) {
            productFilterAdapter.clearAllElements()
            productFilterAdapter.addElement(it)
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
                is Success -> onSuccessMultipleDeleteProduct()
                is Fail -> onErrorMultipleDeleteProduct(it.throwable)
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
            }
        }
    }
    // endregion

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