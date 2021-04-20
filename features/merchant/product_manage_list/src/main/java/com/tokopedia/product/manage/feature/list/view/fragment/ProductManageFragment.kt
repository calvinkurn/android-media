package com.tokopedia.product.manage.feature.list.view.fragment

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_PRODUCT_ID
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATED_STATUS
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATED_STOCK
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATE_MESSAGE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.REQUEST_CODE_CAMPAIGN_STOCK
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.ui.QuickEditVariantStockBottomSheet
import com.tokopedia.product.manage.common.session.ProductManageSession
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.feature.campaignstock.ui.activity.CampaignStockActivity
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.presentation.activity.ProductManageSetCashbackActivity
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.EXTRA_CASHBACK_SHOP_ID
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_VALUE
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_PRODUCT_NAME
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.SET_CASHBACK_RESULT
import com.tokopedia.product.manage.feature.etalase.view.activity.EtalasePickerActivity
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.EXTRA_ETALASE_ID
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.EXTRA_ETALASE_NAME
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment.Companion.REQUEST_CODE_PICK_ETALASE
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.product.manage.feature.list.constant.ProductManageAnalytics.MP_PRODUCT_MANAGE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.BROADCAST_ADD_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.BROADCAST_CHAT_CREATE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_IS_NEED_TO_RELOAD_DATA_SHOP_PRODUCT_LIST
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_THRESHOLD
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.PRODUCT_ID
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_ADD_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_DRAFT_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_EDIT_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_ETALASE
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
import com.tokopedia.product.manage.feature.list.view.model.DeleteProductDialogType.*
import com.tokopedia.product.manage.feature.list.view.model.FilterTabUiModel
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.ShowFilterTab
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductMoreMenuModel
import com.tokopedia.product.manage.feature.list.view.model.TopAdsPage.*
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductManageAddEditMenuBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductManageBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductManageMoreMenuBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductManageStockLocationBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.StockInformationBottomSheet
import com.tokopedia.product.manage.feature.list.view.ui.tab.ProductManageFilterTab
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.multiedit.ui.bottomsheet.ProductMultiEditBottomSheet
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getRetryMessage
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getSuccessMessage
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment.ProductManageQuickEditPriceFragment
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui.QuickEditVariantPriceBottomSheet
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.*
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.*
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.DIRECTED_FROM_MANAGE_OR_PDP
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.data.model.FreeDeposit.Companion.DEPOSIT_ACTIVE
import com.tokopedia.topads.freeclaim.data.constant.TOPADS_FREE_CLAIM_URL
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_product_manage.*
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException
import javax.inject.Inject

open class ProductManageFragment : BaseListFragment<ProductUiModel, ProductManageAdapterFactoryImpl>(),
        ProductViewHolder.ProductViewHolderView,
        ProductMenuViewHolder.ProductMenuListener,
        ProductMultiEditBottomSheet.MultiEditListener,
        ProductManageFilterFragment.OnFinishedListener,
        ProductManageQuickEditPriceFragment.OnFinishedListener,
        ProductManageQuickEditStockFragment.OnFinishedListener,
        ProductManageMoreMenuViewHolder.ProductManageMoreMenuListener,
        ProductManageListListener, ProductManageAddEditMenuBottomSheet.AddEditMenuClickListener {

    @Inject
    lateinit var viewModel: ProductManageViewModel
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var productManageSession: ProductManageSession

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
    private val productManageAddEditMenuBottomSheet by lazy { ProductManageAddEditMenuBottomSheet(view, sellerFeatureCarouselClickListener, this, fragmentManager) }

    private val productManageListAdapter by lazy { adapter as ProductManageListAdapter }
    private var defaultFilterOptions: List<FilterOption> = emptyList()
    private var itemsChecked: MutableList<ProductUiModel> = mutableListOf()
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var filterTab: ProductManageFilterTab? = null

    private var shouldEnableMultiEdit: Boolean = false
    private var shouldAddAsFeatured: Boolean = false
    private var extraCacheManagerId: String = ""
    private var sellerMigrationFeatureName: String = ""
    private var sellerFeatureCarouselClickListener: SellerFeatureCarousel.SellerFeatureClickListener = object : SellerFeatureCarousel.SellerFeatureClickListener {
        override fun onSellerFeatureClicked(item: SellerFeatureUiModel) {
            when (item) {
                is SellerFeatureUiModel.MultiEditFeatureWithDataUiModel -> goToSellerAppProductManageMultiEdit()
                is SellerFeatureUiModel.TopAdsFeatureWithDataUiModel -> goToSellerAppTopAds()
                is SellerFeatureUiModel.SetCashbackFeatureWithDataUiModel -> goToSellerAppProductManageThenSetCashback(item.data as ProductUiModel)
                is SellerFeatureUiModel.FeaturedProductFeatureWithDataUiModel -> goToSellerAppProductManageThenAddAsFeatured(item.data as ProductUiModel)
                is SellerFeatureUiModel.StockReminderFeatureWithDataUiModel -> goToSellerAppSetStockReminder(item.data as ProductUiModel)
                is SellerFeatureUiModel.ProductManageSetVariantFeatureWithDataUiModel -> goToSellerAppAddProduct()
                is SellerFeatureUiModel.BroadcastChatProductManageUiModel -> {
                    val product = item.data as ProductUiModel
                    goToCreateBroadCastChat(product)
                    ProductManageTracking.eventClickBroadcastChat(userId = userSession.userId, productId = product.id, isCarousel = true)
                }
            }
        }
    }

    // these variables only use from seller migration (entry point broadcast chat)
    private var productId = ""
    private var productStock = 0
    private var isProductVariant = false
    private var isProductActive = false

    private var progressDialog: ProgressDialog? = null
    private var optionsMenu: Menu? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        activity?.intent?.data?.run {
            shouldEnableMultiEdit = this.getBooleanQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_ENABLE_MULTI_EDIT, false)
            shouldAddAsFeatured = this.getBooleanQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_ADD_AS_FEATURED, false)
            extraCacheManagerId = this.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID).orEmpty()
            sellerMigrationFeatureName = this.getQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).orEmpty()
            productId = this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ID).orEmpty()
            productStock = this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_STOCK).toIntOrZero()
            isProductVariant = this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_VARIANT).toBoolean()
            isProductActive = this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ACTIVE).toBoolean()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            val bottomSheet = childFragmentManager.findFragmentByTag(BOTTOM_SHEET_TAG)
            when (bottomSheet) {
                is ProductManageFilterFragment -> bottomSheet.setOnFinishedListener(this@ProductManageFragment)
                is ProductManageQuickEditStockFragment -> bottomSheet.setOnFinishedListener(this@ProductManageFragment)
                is ProductManageQuickEditPriceFragment -> bottomSheet.setOnFinishedListener(this@ProductManageFragment)
            }
        }
    }

    open fun getLayoutRes(): Int = R.layout.fragment_product_manage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun clearAllData() {
        isLoadingInitialData = true
        super.clearAllData()
    }

    private fun initView() {
        setupInterceptor()
        setupSearchBar()
        setupProductList()
        setupProgressDialogVariant()
        setupFiltersTab()
        setupMultiSelect()
        setupSelectAll()
        setupErrorPage()
        setupNoAccessPage()
        setupStockTicker()
        renderCheckedView()

        observeShopInfo()
        observeDeleteProduct()
        observeProductListFeaturedOnly()
        observeProductList()
        observeFilterTabs()
        observeMultiSelect()
        observeProductVariantBroadcast()

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
        observeClickTopAdsMenu()
        observeProductManageAccess()
        observeDeleteProductDialog()
        observeOptionsMenu()

        getProductManageAccess()
        setupDialogFeaturedProduct()

        context?.let { UpdateShopActiveService.startService(it) }
    }

    private fun setupProgressDialogVariant() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage(getString(R.string.message_loading_progress_dialog))
    }

    private fun showProgressDialogVariant() {
        progressDialog?.show()
    }

    private fun hideProgressDialogVariant() {
        progressDialog?.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        optionsMenu = menu

        val menuViewId = if (GlobalConfig.isSellerApp()) {
            R.menu.menu_product_manage
        } else {
            R.menu.menu_product_manage_dark
        }

        menu.clear()
        inflater.inflate(menuViewId, menu)
        showHideOptionsMenu()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_product_menu) {
            if (GlobalConfig.isSellerApp()) {
                val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_ADD)
                startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
                ProductManageTracking.eventAddProduct()
            } else {
                showAddEditMenuBottomSheet()
            }
        } else if (item.itemId == R.id.action_more_menu) {
            showMoreMenuBottomSheet()
            ProductManageTracking.eventClickMoreMenuEllipses()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMoreMenuClicked(menu: ProductMoreMenuModel) {
        if (menu.title == getString(R.string.product_manage_shop_showcase_more_menu_text)) {
            // goto showcase list
            val showcaseListIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            val showcaseListBundle = Bundle().apply {
                putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, userSession.shopId)
                putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
                putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
                putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            }
            showcaseListIntent.putExtra(EXTRA_BUNDLE, showcaseListBundle)
            startActivityForResult(showcaseListIntent, REQUEST_CODE_ETALASE)
            productManageMoreMenuBottomSheet?.dismiss()
            ProductManageTracking.eventClickMoreMenuShopShowcase()
        }
    }

    private fun onClickMoreFilter() {
        showFilterBottomSheet()

        val tabName = getString(R.string.product_manage_filter)
        ProductManageTracking.eventInventory(tabName)
    }

    private fun onClickFilterTab(filter: FilterTabUiModel) {
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
        viewModel.onDeleteMultipleProducts()
        ProductManageTracking.eventBulkSettingsDeleteBulk()
    }

    override fun onFinish(selectedData: FilterOptionWrapper) {
        viewModel.setFilterOptionWrapper(selectedData)
    }

    override fun onFinishEditPrice(product: ProductUiModel) {
        product.title?.let { product.minPrice?.price?.let { price -> viewModel.editPrice(product.id, price, it) } }
    }

    override fun onFinishEditStock(productId: String, productName: String, stock: Int?, status: ProductStatus?) {
        viewModel.editStock(productId, productName, stock, status)
    }

    override fun getEmptyDataViewModel(): EmptyModel {
        return EmptyModel().apply {
            if (showProductEmptyState()) {
                contentRes = R.string.product_manage_list_empty_product
                urlRes = ProductManageUrl.PRODUCT_MANAGE_LIST_EMPTY_STATE
            } else {
                contentRes = R.string.product_manage_list_empty_search
                urlRes = ProductManageUrl.PRODUCT_MANAGE_SEARCH_EMPTY_STATE
            }
        }
    }

    override fun onAddProductWithNoVariantClicked() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_ADD)
        startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
        productManageAddEditMenuBottomSheet.dismiss()
    }

    private fun observeProductVariantBroadcast() {
        observe(viewModel.getProductVariantsResult) {
            when (it) {
                is Success -> setVariantGoToBroadcastChat(it.data)
                is Fail -> {
                    val message = resources.getString(R.string.broadcast_chat_error_state_message_empty_variant)
                    val action = resources.getString(R.string.broadcast_chat_error_state_action_retry)
                    errorStateBroadcastChat(message, action, isRetry = true)
                }
            }
        }
    }

    private fun setVariantGoToBroadcastChat(data: GetVariantResult) {
        if (!data.isEmptyPrimaryVariant()) {
            val mainProduct = data.getMainVariant()
            redirectToBroadcastChat(mainProduct?.id.orEmpty())
        } else if (!data.isEmptyOtherVariant()) {
            val mainProduct = data.getOtherVariant()
            redirectToBroadcastChat(mainProduct?.id.orEmpty())
        } else {
            showErrorStateEmptyProductBroadcastChat()
        }
    }

    private fun goToCreateBroadCastChat(product: ProductUiModel?) {
        if (isSellerMigrationEnabled(context)) {
            goToSellerAppProductManageBroadcastChat(product)
        } else {
            if (product?.stock.isZero() || product?.isActive() != true) {
                showErrorStateEmptyProductBroadcastChat()
            } else {
                //request variant
                if (product.isVariant()) {
                    viewModel.getProductVariants(product.id)
                } else {
                    redirectToBroadcastChat(product.id)
                }
            }
        }
    }

    private fun goToCreateBroadcastFromSellerMigration(stock: Int, isActive: Boolean, isVariant: Boolean, productId: String) {
        if (stock.isZero() || !isActive) {
            showErrorStateEmptyProductBroadcastChat()
        } else {
            //request variant
            if (isVariant) {
                viewModel.getProductVariants(productId)
            } else {
                redirectToBroadcastChat(productId)
            }
        }
    }

    private fun showErrorStateEmptyProductBroadcastChat() {
        val message = resources.getString(R.string.broadcast_chat_error_state_message_empty_stock)
        val action = resources.getString(R.string.broadcast_chat_error_state_action_oke)
        errorStateBroadcastChat(message, action)
    }

    private fun redirectToBroadcastChat(productId: String) {
        val chatBlastUrl = BROADCAST_CHAT_CREATE
        val url = Uri.parse(chatBlastUrl).buildUpon().appendQueryParameter(PRODUCT_ID, productId).build().toString()
        RouteManager.route(requireContext(), ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun errorStateBroadcastChat(message: String, action: String, isRetry: Boolean = false, product: ProductUiModel? = null) {
        Toaster.build(coordinatorLayout, type = Toaster.TYPE_ERROR, text = message, actionText = action, duration = Toaster.LENGTH_LONG, clickListener = View.OnClickListener {
            if (isRetry) {
                goToCreateBroadCastChat(product)
            } else {
                return@OnClickListener
            }
        }).show()
    }

    private fun goToSellerAppProductManageBroadcastChat(product: ProductUiModel?) {
        val firstAppLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
                .buildUpon()
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ID, product?.id)
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_STOCK, product?.stock?.toString())
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ACTIVE, product?.isActive()?.toString())
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_VARIANT, product?.isVariant()?.toString())
                .build()
                .toString()

        val chatBlastUrl = Uri.parse(BROADCAST_CHAT_CREATE).buildUpon().appendQueryParameter(PRODUCT_ID, product?.id).build().toString()
        val secondAppLink = UriUtil.buildUri(ApplinkConstInternalGlobal.WEBVIEW, chatBlastUrl)

        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT, arrayListOf(firstAppLink, secondAppLink))
    }

    private fun goToSellerAppProductManageMultiEdit() {
        val appLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
                .buildUpon()
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_ENABLE_MULTI_EDIT, "true")
                .build()
                .toString()

        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_MULTI_EDIT, arrayListOf(appLink))
    }

    private fun goToSellerAppTopAds() {
        val firstAppLink = ApplinkConst.PRODUCT_MANAGE
        val secondAppLink = ApplinkConst.SellerApp.TOPADS_DASHBOARD

        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_TOPADS, arrayListOf(firstAppLink, secondAppLink))
    }

    private fun goToSellerAppProductManageThenSetCashback(product: ProductUiModel) {
        val cacheManagerId = UUID.randomUUID().toString()
        val firstAppLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID, cacheManagerId)
                .build()
                .toString()
        val secondAppLink = Uri.parse(UriUtil.buildUri(ApplinkConstInternalMarketplace.SET_CASHBACK, product.id))
                .buildUpon()
                .appendQueryParameter(SET_CASHBACK_PRODUCT_NAME, product.title)
                .appendQueryParameter(PARAM_SET_CASHBACK_VALUE, product.cashBack.toString())
                .appendQueryParameter(PARAM_SET_CASHBACK_PRODUCT_PRICE, product.minPrice?.price.toIntOrZero().toString())
                .appendQueryParameter(EXTRA_CASHBACK_SHOP_ID, userSession.shopId)
                .appendQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID, cacheManagerId)
                .build()
                .toString()
        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_SET_CASHBACK, arrayListOf(firstAppLink, secondAppLink))
    }

    private fun goToSellerAppProductManageThenAddAsFeatured(product: ProductUiModel) {
        val firstAppLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
                .buildUpon()
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_ADD_AS_FEATURED, "true")
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_IS_PRODUCT_FEATURED, product.isFeatured.toString())
                .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_SELECTED_PRODUCT_ID, product.id)
                .build()
                .toString()

        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT, arrayListOf(firstAppLink))
    }

    private fun goToSellerAppSetStockReminder(product: ProductUiModel) {
        val cacheManagerId = UUID.randomUUID().toString()
        val firstAppLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID, cacheManagerId)
                .build()
                .toString()
        val secondAppLink = UriUtil.buildUri(ApplinkConstInternalMarketplace.STOCK_REMINDER, product.id, product.title, product.stock.toString())
                .plus("?${ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID}=$cacheManagerId")
        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_STOCK_REMINDER, arrayListOf(firstAppLink, secondAppLink))
    }

    private fun goToSellerAppAddProduct() {
        val secondAppLink = ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_SET_VARIANT, arrayListOf(ApplinkConst.PRODUCT_MANAGE, secondAppLink))
    }

    private fun goToSellerMigrationPage(@SellerMigrationFeatureName featureName: String, appLinks: ArrayList<String>) {
        context?.run {
            val intent = SellerMigrationActivity.createIntent(this, featureName, screenName, appLinks)
            startActivity(intent)
        }
    }

    //set filter options if filterOptions is not null or empty
    private fun setDefaultFilterOption() {
        if (defaultFilterOptions.isNotEmpty()) {
            val filterOptionsWrapper = FilterOptionWrapper(
                    sortOption = null,
                    filterOptions = defaultFilterOptions,
                    filterShownState = listOf(true, true, false, true),
                    selectedFilterCount = ProductManageFilterMapper.countSelectedFilter(defaultFilterOptions)
            )
            viewModel.setFilterOptionWrapper(filterOptionsWrapper)

            defaultFilterOptions = emptyList()
        }
    }

    fun setDefaultFilterOptions(filterOptions: List<FilterOption>) {
        defaultFilterOptions = filterOptions
    }

    fun setSearchKeywordOptions(keyword: String) {
        isLoadingInitialData = true
        tabSortFilter?.show()
        searchBar?.show()
        searchBar?.searchTextView?.setText(keyword)
        showLoadingProgress()
        getProductList()
        searchBar.clearFocus()
    }

    private fun showProductEmptyState(): Boolean {
        val selectedFilters = viewModel.selectedFilterAndSort.value
        val searchKeyword = searchBar.searchTextView.text.toString()
        return searchKeyword.isEmpty() && selectedFilters?.selectedFilterCount.orZero() == 0 && filterTab?.isFilterActive() == false
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

    private fun setupBottomSheet(access: ProductManageAccess) {
        productManageBottomSheet = ProductManageBottomSheet.createInstance(access).apply {
            init(this@ProductManageFragment, sellerFeatureCarouselClickListener)
        }
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

    private fun setupErrorPage() {
        errorPage.apply {
            errorAction.text = context?.getString(R.string.product_manage_refresh_page)

            setActionClickListener {
                onSwipeRefresh()
            }
        }
    }

    private fun setupNoAccessPage() {
        noAccessPage.apply {
            ImageHandler.loadImageAndCache(errorIllustration, ProductManageUrl.ILLUSTRATION_NO_ACCESS)
            errorTitle.text = context?.getString(R.string.product_manage_no_access_title)
            errorDescription.text = context?.getString(R.string.product_manage_no_access_description)
            errorAction.text = context?.getString(R.string.product_manage_back_to_home)
            setButtonFull(true)

            setActionClickListener {
                RouteManager.route(context, ApplinkConst.SellerApp.SELLER_APP_HOME)
            }
        }
    }

    private fun setupStockTicker() {
        (stockTicker as? Ticker)?.setDescriptionClickEvent(object: TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
            }
            override fun onDismiss() {
                viewModel.hideStockTicker()
            }
        })
    }

    private fun showFilterBottomSheet() {
        filterProductBottomSheet = context?.let {
            ProductManageFilterFragment.createInstance(viewModel.selectedFilterAndSort.value, this)
        }
        this.childFragmentManager.let { filterProductBottomSheet?.show(it, BOTTOM_SHEET_TAG) }
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
            if (intent.action == BROADCAST_ADD_PRODUCT &&
                    intent.hasExtra(TkpdState.ProductService.STATUS_FLAG) &&
                    intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0) == TkpdState.ProductService.STATUS_DONE) {
                activity?.run {
                    runOnUiThread {
                        val productId = intent.extras?.getString(TkpdState.ProductService.PRODUCT_ID) ?: ""
                        viewModel.getPopupsInfo(productId)
                        getFiltersTab(withDelay = true)
                        getProductList(withDelay = true, isRefresh = true)
                    }
                }
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<ProductUiModel, ProductManageAdapterFactoryImpl> {
        return ProductManageListAdapter(adapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): ProductManageAdapterFactoryImpl {
        return ProductManageAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = "/product list page"

    override fun initInjector() {
        getComponent(ProductManageListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        getProductList(page)
    }

    private fun renderProductList(list: List<ProductUiModel>, hasNextPage: Boolean) {
        renderList(list, hasNextPage)
        renderCheckedView()
        showAddAsFeaturedProduct()
        if (!extraCacheManagerId.isBlank()) {
            val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, extraCacheManagerId) }
            val resultStatus = cacheManager?.get(ProductManageListConstant.EXTRA_RESULT_STATUS, Int::class.java) ?: 0
            if (resultStatus == Activity.RESULT_OK) {
                if (sellerMigrationFeatureName == SellerMigrationFeatureName.FEATURE_SET_CASHBACK) {
                    onSetCashbackResult(cacheManager)
                } else if (sellerMigrationFeatureName == SellerMigrationFeatureName.FEATURE_STOCK_REMINDER) {
                    val productName: String = cacheManager?.getString(EXTRA_PRODUCT_NAME).orEmpty()
                    val threshold = cacheManager?.get(EXTRA_THRESHOLD, Int::class.java) ?: 0
                    onSetStockReminderResult(threshold, productName)
                }
            }
        }
        if (sellerMigrationFeatureName == SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT) {
            goToCreateBroadcastFromSellerMigration(productStock, isProductActive, isProductVariant, productId)
        }
    }

    private fun showAddAsFeaturedProduct() {
        if (shouldAddAsFeatured) {
            shouldAddAsFeatured = false
            val isFeatured = activity?.intent?.data?.getBooleanQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_IS_PRODUCT_FEATURED, false) ?: false
            val productId = activity?.intent?.data?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_SELECTED_PRODUCT_ID).orEmpty()
            onSetFeaturedProductClicked(isFeatured, productId)
        }
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
        if (!keyword.isNullOrEmpty()) add(FilterByKeyword(keyword))
    }

    private fun showProductList(productList: List<ProductUiModel>) {
        val hasNextPage = productList.isNotEmpty()
        renderProductList(productList, hasNextPage)
        renderSelectAllCheckBox()
        setDefaultFilterOption()
    }

    private fun renderMultiSelectProduct() {
        val productNotEmpty = adapter.data
                .filterIsInstance<ProductUiModel>()
                .isNotEmpty()
        val productManageAccess = viewModel.productManageAccess.value as? Success<ProductManageAccess>
        val hasMultiSelectAccess = productManageAccess?.data?.multiSelect == true
        val shouldShow = productNotEmpty && GlobalConfig.isSellerApp() && hasMultiSelectAccess

        multiSelectContainer.showWithCondition(productNotEmpty)
        textMultipleSelect.showWithCondition(shouldShow)

        if (shouldEnableMultiEdit && hasMultiSelectAccess) {
            shouldEnableMultiEdit = false
            textMultipleSelect.performClick()
        }

        if(hasMultiSelectAccess) {
            enableMultiSelect()
        }
    }

    private fun onErrorEditPrice(editPriceResult: EditPriceResult) {
        with(editPriceResult) {
            val message = if (error is NetworkErrorException) {
                getString(error.message.toIntOrZero())
            } else {
                error?.message
            }
            message?.let {
                val retryMessage = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
                showErrorToast(it, retryMessage) {
                    viewModel.editPrice(productId, price, productName)
                }
            }
        }
    }

    private fun onErrorEditStock(editStockResult: EditStockResult) {
        with(editStockResult) {
            val message = if (error is NetworkErrorException) {
                getString(error?.message.toIntOrZero())
            } else {
                error?.message
            }
            message?.let {
                val retryMessage = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
                showErrorToast(it, retryMessage) {
                    viewModel.editStock(productId, productName, stock, status)
                }
            }
        }
    }

    private fun onSuccessEditPrice(productId: String, price: String, productName: String) {
        Toaster.build(coordinatorLayout, getString(
                R.string.product_manage_quick_edit_price_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        productManageListAdapter.updatePrice(productId, price)
    }

    private fun onSuccessEditStock(productId: String, productName: String, stock: Int?, status: ProductStatus?) {
        Toaster.build(coordinatorLayout, getString(
                com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_stock_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        productManageListAdapter.updateStock(productId, stock, status)

        filterTab?.getSelectedFilter()?.let {
            filterProductListByStatus(it)
            renderMultiSelectProduct()
        }

        getFiltersTab(withDelay = true)
    }

    private fun onSuccessSetCashback(setCashbackResult: SetCashbackResult) {
        Toaster.build(coordinatorLayout, getString(
                R.string.product_manage_set_cashback_success, setCashbackResult.productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        productManageListAdapter.updateCashBack(setCashbackResult.productId, setCashbackResult.cashback)
        val filterOptions = viewModel.selectedFilterAndSort.value?.filterOptions
        if (filterOptions == listOf(FilterByCondition.CashBackOnly)) {
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
        val message = if (deleteProductResult.error is NetworkErrorException) {
            getString(deleteProductResult.error.message.toIntOrZero())
        } else {
            deleteProductResult.error?.message
        }
        message?.let {
            val retryMessage = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
            showErrorToast(it, retryMessage) {
                viewModel.deleteSingleProduct(deleteProductResult.productName, deleteProductResult.productId)
            }
        }
    }

    private fun onSuccessDeleteProduct(productName: String, productId: String) {
        Toaster.build(coordinatorLayout, getString(
                R.string.product_manage_delete_product_success, productName),
                Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        productManageListAdapter.deleteProduct(productId)
        renderMultiSelectProduct()
        getFiltersTab(withDelay = true)
    }

    private fun showMessageToast(message: String) {
        view?.let {
            val actionLabel = getString(com.tokopedia.design.R.string.close)
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionLabel).show()
        }
    }

    private fun showMessageToastWithoutAction(message: String) {
        view?.let {
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showErrorToast(
        message: String = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_fail),
        actionLabel: String = getString(com.tokopedia.abstraction.R.string.close),
        listener: () -> Unit = {}
    ) {
        view?.let {
            val onClickActionLabel = View.OnClickListener { listener.invoke() }
            Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionLabel, onClickActionLabel).show()
        }
    }

    private fun showRetryToast() {
        view?.let {
            val onClickActionLabel = View.OnClickListener {
                if (isLoadingInitialData) {
                    onSwipeRefresh()
                } else {
                    endlessRecyclerViewScrollListener.loadMoreNextPage()
                }
            }
            Toaster.build(
                    it,
                    getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_fail),
                    Snackbar.LENGTH_INDEFINITE,
                    Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.retry_label),
                    onClickActionLabel
            ).show()
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

    @SuppressLint("DialogUnifyUsage")
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
                val backgroundColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)

                val spanText = SpannableString(getString(R.string.popup_tips_trick_clickable))
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
                val retryLabel = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
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

        when (result) {
            is EditByStatus -> viewModel.editProductsByStatus(productIds, result.status)
            is EditByMenu -> viewModel.editProductsEtalase(productIds, result.menuId, result.menuName)
        }
    }

    private fun updateEditProductList(result: MultiEditResult) {
        val productIds = result.success.map { it.productID }

        when (result) {
            is EditByStatus -> {
                updateProductListStatus(productIds, result.status)

                filterTab?.getSelectedFilter()?.let {
                    filterProductListByStatus(it)
                    renderMultiSelectProduct()
                }

                getFiltersTab(withDelay = true)
            }
        }

        if (result.failed.isEmpty()) {
            viewModel.toggleMultiSelect()
        } else {
            unCheckMultipleProducts(productIds)
        }
    }

    private fun unCheckMultipleProducts(productIds: List<String>) {
        productIds.forEach { productId ->
            val index = adapter.data.indexOfFirst { it.id == productId }
            if (index >= 0) {
                onClickProductCheckBox(false, index)
            }
        }
        productManageListAdapter.notifyDataSetChanged()
    }

    private fun updateProductListStatus(productIds: List<String>, status: ProductStatus) {
        productIds.forEach { productId ->
            when (status) {
                DELETED -> productManageListAdapter.deleteProduct(productId)
                INACTIVE -> productManageListAdapter.setProductStatus(productId, status)
                else -> {
                }  // do nothing
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

        hideNoAccessPage()
        hideErrorPage()
        hideStockTicker()

        getProductManageAccess()
    }

    private fun clearFilterAndKeywordIfEmpty() {
        val productList = adapter.data
                .filterIsInstance<ProductUiModel>()

        if (productList.isEmpty()) {
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
        if (filterOptions == listOf(FilterByCondition.FeaturedOnly)) {
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
        adapter.data.getOrNull(position)?.let { product ->
            val checkedData = itemsChecked.firstOrNull { it.id.contains(product.id) }
            adapter.data[position] = product.copy(isChecked = isChecked)

            if (isChecked && checkedData == null) {
                itemsChecked.add(product)
            } else if (!isChecked) {
                itemsChecked.remove(checkedData)
            }

            renderSelectAllCheckBox()
            renderCheckedView()
        }
    }

    override fun onClickStockInformation() {
        stockInfoBottomSheet.show()
    }

    override fun onClickEditStockButton(product: ProductUiModel) {
        if (product.hasStockReserved) {
            context?.run {
                startActivityForResult(
                        CampaignStockActivity.createIntent(this, userSession.shopId, arrayOf(product.id)),
                        REQUEST_CODE_CAMPAIGN_STOCK)
            }
        } else {
            val editStockBottomSheet = context?.let { ProductManageQuickEditStockFragment.createInstance(it, product, this) }
            editStockBottomSheet?.show(childFragmentManager, BOTTOM_SHEET_TAG)
        }
        ProductManageTracking.eventEditStock(product.id)
    }

    override fun onClickEditVariantPriceButton(product: ProductUiModel) {
        val editVariantBottomSheet = QuickEditVariantPriceBottomSheet.createInstance(product.id) { result ->
            viewModel.editVariantsPrice(result)
        }
        editVariantBottomSheet.show(childFragmentManager, QuickEditVariantPriceBottomSheet.TAG)
        ProductManageTracking.eventClickEditPriceVariant()
    }

    override fun onClickEditVariantStockButton(product: ProductUiModel) {
        if (product.hasStockReserved) {
            context?.run {
                startActivityForResult(
                        CampaignStockActivity.createIntent(this, userSession.shopId, arrayOf(product.id)),
                        REQUEST_CODE_CAMPAIGN_STOCK)
            }
        } else {
            val editVariantStockBottomSheet = QuickEditVariantStockBottomSheet.createInstance(product.id) { result ->
                viewModel.editVariantsStock(result)
            }
            editVariantStockBottomSheet.show(childFragmentManager, QuickEditVariantStockBottomSheet.TAG)
        }
        ProductManageTracking.eventClickEditStockVariant()
    }

    override fun onClickContactCsButton(product: ProductUiModel) {
        goToProductViolationHelpPage()
        ProductManageTracking.eventContactCs(product.id)
    }

    override fun onClickMoreOptionsButton(product: ProductUiModel) {
        hideSoftKeyboard()

        if (product.status == MODERATED) {
            val errorMessage = getString(R.string.product_manage_desc_product_on_supervision, product.title)
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        } else {
            val isPowerMerchantOrOfficialStore = viewModel.isPowerMerchant() || isOfficialStore
            productManageBottomSheet?.show(childFragmentManager, product, isPowerMerchantOrOfficialStore)
        }
    }

    override fun onClickEditPriceButton(product: ProductUiModel) {
        val editPriceBottomSheet = context?.let { ProductManageQuickEditPriceFragment.createInstance(product, this) }
        editPriceBottomSheet?.show(childFragmentManager, BOTTOM_SHEET_TAG)
        ProductManageTracking.eventEditPrice(product.id)
    }

    override fun onClickOptionMenu(menu: ProductMenuUiModel) {
        val product = menu.product
        val productId = product.id
        val productName = product.title ?: ""

        when (menu) {
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
                onPromoTopAdsClicked(product.id)
                ProductManageTracking.eventSettingsTopads(productId)
            }
            is SeeTopAds -> {
                onSeeTopAdsClicked(product.id)
                ProductManageTracking.eventSettingsTopadsDetail(productId)
            }
            is SetCashBack -> {
                onSetCashbackClicked(product)
                ProductManageTracking.eventSettingsCashback(productId)
            }
            is SetFeaturedProduct -> {
                onSetFeaturedProductClicked(product.isFeatured ?: false, product.id)
                ProductManageTracking.eventSettingsFeatured(productId)
            }
            is RemoveFeaturedProduct -> {
                onRemoveFeaturedProductClicked(product)
                ProductManageTracking.eventSettingsFeatured(productId)
            }
            is CreateBroadcastChat -> {
                goToCreateBroadCastChat(product)
                ProductManageTracking.eventClickBroadcastChat(userId = userSession.userId, productId = productId, isCarousel = false)
            }
        }
        productManageBottomSheet?.dismiss(childFragmentManager)
    }

    private fun goToProductViolationHelpPage() {
        RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${ProductManageUrl.PRODUCT_VIOLATION_HELP_URL}")
    }

    private fun clickDuplicateProduct(productId: String) {
        goToDuplicateProduct(productId)
    }

    private fun clickDeleteProductMenu(productName: String, productId: String) {
        viewModel.onDeleteSingleProduct(productName, productId)
    }

    private fun hideSoftKeyboard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    private fun onSetFeaturedProductClicked(isFeatured: Boolean, productId: String) {
        context?.let { context ->
            if ((viewModel.isPowerMerchant() || isOfficialStore) && !isFeatured) {
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
                            addFeaturedProduct(productId)
                            dialogFeaturedProduct?.dismiss()
                            ProductManageTracking.eventFeaturedProductPopUpSave()
                        }
                        dialogFeaturedProduct?.setSecondaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                        dialogFeaturedProduct?.show()
                    }
                    else -> {
                        addFeaturedProduct(productId)
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

    private fun onRemoveFeaturedProductClicked(productManageUiModel: ProductUiModel) {
        productListFeaturedOnlySize -= 1
        showLoadingProgress()
        setFeaturedProduct(productManageUiModel.id, ProductManageListConstant.FEATURED_PRODUCT_REMOVE_STATUS)
    }

    private fun onSetStockReminderClicked(productManageUiModel: ProductUiModel) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.STOCK_REMINDER, productManageUiModel.id, productManageUiModel.title, productManageUiModel.stock.toString())
        startActivityForResult(intent, REQUEST_CODE_STOCK_REMINDER)
    }

    private fun onPromoTopAdsClicked(productId: String) {
        viewModel.onPromoTopAdsClicked(productId)
    }

    private fun onSeeTopAdsClicked(productId: String) {
        goToPDP(productId = productId, showTopAdsSheet = true)
    }

    private fun onSetCashbackClicked(productManageUiModel: ProductUiModel) {
        with(productManageUiModel) {
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

    private fun showDialogDeleteProduct(data: SingleProduct) {
        context?.let {
           DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
               val title = if(data.isMultiLocationShop) {
                   getString(R.string.product_manage_multi_location_delete_product_title)
               } else {
                   getString(R.string.product_manage_delete_product_title)
               }

               val description = if(data.isMultiLocationShop) {
                   getString(R.string.product_manage_multi_location_delete_product_description)
               } else {
                   getString(R.string.product_manage_delete_product_description)
               }

               setTitle(title)
               setDescription(description)
               setPrimaryCTAText(it.resources.getString(R.string.product_manage_delete_product_delete_button))
               setSecondaryCTAText(it.resources.getString(R.string.product_manage_delete_product_cancel_button))
               setPrimaryCTAClickListener {
                   viewModel.deleteSingleProduct(data.productName, data.productId)
                   ProductManageTracking.eventDeleteProduct(data.productId)
                   dismiss()
               }
               setSecondaryCTAClickListener {
                   dismiss()
               }
           }.show()
        }
    }

    override fun onClickProductItem(product: ProductUiModel) {
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
    private fun goToPDP(productId: String?, showTopAdsSheet: Boolean = false) {
        if (productId != null) {
            val uri = Uri.parse(ApplinkConstInternalMarketplace.PRODUCT_DETAIL).buildUpon()

            if (showTopAdsSheet) {
                uri.appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME,
                        SellerMigrationFeatureName.FEATURE_ADS)
            }

            val uriString = uri.build().toString()
            val intent = RouteManager.getIntent(context, uriString, productId)

            startActivity(intent)
        }
    }

    private fun goToEtalasePicker() {
        val intent = Intent(activity, EtalasePickerActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_PICK_ETALASE)
    }

    override fun onItemClicked(t: ProductUiModel?) {
        // NO OP
    }

    override fun onPause() {
        super.onPause()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(addProductReceiver)
        }
        if (productManageAddEditMenuBottomSheet.isVisible) {
            productManageAddEditMenuBottomSheet.dismiss()
        }
        if (productManageBottomSheet?.isVisible == true) {
            productManageBottomSheet?.dismiss(childFragmentManager)
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_ADD_PRODUCT)
            LocalBroadcastManager.getInstance(it).registerReceiver(addProductReceiver, intentFilter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
        context?.let {
            if (addProductReceiver.isOrderedBroadcast) {
                LocalBroadcastManager.getInstance(it).unregisterReceiver(addProductReceiver)
            }
        }
        removeObservers()
    }

    private fun removeObservers() {
        removeObservers(viewModel.viewState)
        removeObservers(viewModel.productListResult)
        removeObservers(viewModel.getProductVariantsResult)
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
                REQUEST_CODE_PICK_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                    val productIds = itemsChecked.map { product -> product.id }
                    val etalaseId = it.getStringExtra(EXTRA_ETALASE_ID).orEmpty()
                    val etalaseName = it.getStringExtra(EXTRA_ETALASE_NAME).orEmpty()

                    viewModel.editProductsEtalase(productIds, etalaseId, etalaseName)
                }
                REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                    val shopId = userSession.shopId
                    val showcaseId = it.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    val isNeedToReloadData = it.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
                    val shopShowcaseIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST, shopId, showcaseId)
                    shopShowcaseIntent.putExtra(EXTRA_IS_NEED_TO_RELOAD_DATA_SHOP_PRODUCT_LIST, isNeedToReloadData)
                    startActivity(shopShowcaseIntent)
                }
                REQUEST_CODE_STOCK_REMINDER -> if (resultCode == Activity.RESULT_OK) {
                    val productName = it.getStringExtra(EXTRA_PRODUCT_NAME).orEmpty()
                    val threshold = it.getIntExtra(EXTRA_THRESHOLD, 0)
                    onSetStockReminderResult(threshold, productName)
                }
                SET_CASHBACK_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val cacheManagerId = it.getStringExtra(SET_CASHBACK_CACHE_MANAGER_KEY)
                    val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, cacheManagerId) }
                    onSetCashbackResult(cacheManager)
                }
                REQUEST_CODE_CAMPAIGN_STOCK ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val productId = it.getStringExtra(EXTRA_PRODUCT_ID).orEmpty()
                            val productName = it.getStringExtra(EXTRA_PRODUCT_NAME)
                            val stock = it.getIntExtra(EXTRA_UPDATED_STOCK, 0)
                            val status = valueOf(it.getStringExtra(EXTRA_UPDATED_STATUS).orEmpty())

                            productManageListAdapter.updateStock(productId, stock, status)

                            filterTab?.getSelectedFilter()?.let { productStatus ->
                                filterProductListByStatus(productStatus)
                                renderMultiSelectProduct()
                            }

                            getFiltersTab(withDelay = true)

                            val successMessage = getString(com.tokopedia.product.manage.common.R.string.product_manage_campaign_stock_success_toast, productName)
                            Toaster.build(
                                    coordinatorLayout,
                                    successMessage,
                                    Snackbar.LENGTH_SHORT,
                                    Toaster.TYPE_NORMAL)
                                    .show()
                        }
                        Activity.RESULT_CANCELED -> {
                            val errorMessage = it.getStringExtra(EXTRA_UPDATE_MESSAGE)
                                    ?: getString(com.tokopedia.product.manage.common.R.string.product_manage_campaign_stock_error_toast)
                            Toaster.build(
                                    coordinatorLayout,
                                    errorMessage,
                                    Snackbar.LENGTH_SHORT,
                                    Toaster.TYPE_ERROR)
                                    .show()
                        }
                        else -> {
                        }
                    }
                REQUEST_CODE_DRAFT_PRODUCT -> if (resultCode == Activity.RESULT_OK) {
                    activity?.runOnUiThread {
                        getFiltersTab(withDelay = true)
                        getProductList(withDelay = true, isRefresh = true)
                    }
                }
                else -> super.onActivityResult(requestCode, resultCode, it)
            }
        }
    }

    @Suppress("NAME_SHADOWING")
    private fun onSetCashbackResult(cacheManager: SaveInstanceCacheManager?) {
        val setCashbackResult: SetCashbackResult? = cacheManager?.get(SET_CASHBACK_RESULT, SetCashbackResult::class.java)
        setCashbackResult?.let { cashbackResult ->
            if (cashbackResult.limitExceeded) {
                onSetCashbackLimitExceeded()
            } else {
                onSuccessSetCashback(cashbackResult)
            }
        }
    }

    private fun onSetStockReminderResult(threshold: Int, productName: String) {
        if (threshold > 0) {
            Toaster.build(coordinatorLayout, getString(R.string.product_stock_reminder_toaster_success_desc, productName), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        } else {
            Toaster.build(coordinatorLayout, getString(R.string.product_stock_reminder_toaster_success_remove_desc, productName), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun showGetListError(throwable: Throwable?) {
        if(isLoadingInitialData) {
            showErrorPage()
        } else {
            updateStateScrollListener()
            showRetryToast()
        }
        hideStockTicker()
        hideLoading()
    }

    private fun getTopAdsFreeClaim() {
        val query = GraphqlHelper.loadRawString(resources, com.tokopedia.topads.common.R.raw.gql_get_deposit)
        viewModel.getFreeClaim(query, userSession.shopId)
    }

    private fun getGoldMerchantStatus() {
        viewModel.getGoldMerchantStatus()
    }

    private fun getTopAdsInfo() {
        viewModel.getTopAdsInfo()
    }

    private fun getProductManageAccess() {
        viewModel.getProductManageAccess()
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
                is Fail -> {
                    onFailedChangeFeaturedProduct(it.throwable)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeGetPopUpInfo() {
        observe(viewModel.getPopUpResult) {
            when (it) {
                is Success -> onSuccessGetPopUp(it.data.isSuccess, it.data.productId)
                is Fail -> {
                    onErrorGetPopUp()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeGetFreeClaim() {
        observe(viewModel.getFreeClaimResult) {
            when (it) {
                is Success -> onSuccessGetFreeClaim(it.data)
                is Fail -> {
                    onErrorGetFreeClaim()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeEditPrice() {
        observe(viewModel.editPriceResult) {
            when (it) {
                is Success -> onSuccessEditPrice(it.data.productId, it.data.price, it.data.productName)
                is Fail -> {
                    onErrorEditPrice(it.throwable as EditPriceResult)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeEditStock() {
        observe(viewModel.editStockResult) {
            when (it) {
                is Success -> {
                    with(it.data) {
                        onSuccessEditStock(productId, productName, stock, status)
                    }
                }
                is Fail -> {
                    onErrorEditStock(it.throwable as EditStockResult)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeMultiEdit() {
        observe(viewModel.multiEditProductResult) {
            when (it) {
                is Success -> onSuccessMultiEditProducts(it.data)
                is Fail -> {
                    showErrorToast()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun showDeleteProductsConfirmationDialog(data: MultipleProduct) {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val title = if(data.isMultiLocationShop) {
                    getString(R.string.product_manage_multi_location_delete_product_title)
                } else {
                    getString(R.string.product_manage_dialog_delete_products_title, itemsChecked.count())
                }

                val description = if(data.isMultiLocationShop) {
                    getString(R.string.product_manage_multi_location_delete_product_description)
                } else {
                    getString(R.string.product_manage_delete_product_description)
                }

                setTitle(title)
                setDescription(description)
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
                    renderMultiSelectProduct()
                }
                is Fail -> {
                    showGetListError(it.throwable)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
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
            when (it) {
                is Success -> {
                    val data = it.data

                    if (data is ShowFilterTab) {
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
            if (multiSelectEnabled) {
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
                is Fail -> {
                    onErrorDeleteProduct(it.throwable as DeleteProductResult)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
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
            when (it) {
                is ShowProgressDialog -> showLoadingProgress()
                is HideProgressDialog -> hideLoadingProgress()
                is ShowLoadingDialog -> showProgressDialogVariant()
                is HideLoadingDialog -> hideProgressDialogVariant()
            }
        }
        observe(viewModel.showStockTicker) { shouldShow ->
            stockTicker.showWithCondition(shouldShow)
        }
        observe(viewModel.refreshList) { shouldRefresh ->
            if(shouldRefresh) {
                resetProductList()
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
                is Fail -> {
                    showErrorMessageToast(it)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeEditVariantStock() {
        observe(viewModel.editVariantStockResult) {
            when (it) {
                is Success -> {
                    val message = context?.getString(
                            com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_stock_success,
                            it.data.productName
                    ).orEmpty()
                    updateVariantStock(it.data)
                    showMessageToast(message)
                }
                is Fail -> {
                    showErrorMessageToast(it)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        }
    }

    private fun observeClickTopAdsMenu() {
        observe(viewModel.onClickPromoTopAds) {
            when (it) {
                is OnBoarding -> goToTopAdsOnBoarding()
                is ManualAds -> goToCreateTopAds()
                is AutoAds -> goToPDP(it.productId, showTopAdsSheet = true)
            }
        }
    }

    private fun observeProductManageAccess() {
        observe(viewModel.productManageAccess) {
           when(it) {
               is Success -> {
                   val access = it.data

                   if(access.productList) {
                       loadInitialData()

                       getFiltersTab()
                       getProductListFeaturedOnlySize()
                       getTopAdsFreeClaim()
                       getGoldMerchantStatus()
                       getTopAdsInfo()

                       setupBottomSheet(access)
                       showHideOptionsMenu()

                       renderStockLocationBottomSheet()
                       hideNoAccessPage()
                       hideErrorPage()
                   } else {
                       showNoAccessPage()
                   }
               }
               is Fail -> showErrorPage()
           }
        }
    }

    private fun observeDeleteProductDialog() {
        observe(viewModel.deleteProductDialog) {
            when(it) {
                is SingleProduct -> showDialogDeleteProduct(it)
                is MultipleProduct -> showDeleteProductsConfirmationDialog(it)
            }
        }
    }

    private fun observeOptionsMenu() {
        observe(viewModel.showAddProductOptionsMenu) {
            optionsMenu?.findItem(R.id.add_product_menu)?.isVisible = it
        }
        observe(viewModel.showEtalaseOptionsMenu) {
            optionsMenu?.findItem(R.id.action_more_menu)?.isVisible = it
        }
    }
    // endregion

    private fun renderStockLocationBottomSheet() {
        val multiLocationShop = userSession.isMultiLocationShop
        val showStockLocationBottomSheet = productManageSession.getShowStockLocationBottomSheet()

        if(multiLocationShop && showStockLocationBottomSheet) {
            ProductManageStockLocationBottomSheet.newInstance().show(childFragmentManager)
            productManageSession.setShowStockLocationBottomSheet(false)
        }
    }

    private fun showNoAccessPage() {
        noAccessPage.show()
    }

    private fun hideNoAccessPage() {
        noAccessPage.hide()
    }

    private fun showErrorPage() {
        errorPage.show()
    }

    private fun hideErrorPage() {
        errorPage.hide()
    }

    private fun hideStockTicker() {
        stockTicker.hide()
    }

    private fun goToTopAdsOnBoarding() {
        RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_CREATION_ONBOARD)
    }

    private fun goToCreateTopAds() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS).apply {
            putExtra(DIRECTED_FROM_MANAGE_OR_PDP, true)
        }
        startActivity(intent)
    }

    private fun updateVariantStock(data: EditVariantResult) {
        val stock = data.countVariantStock()
        val status = data.getVariantStatus()

        productManageListAdapter.updateStock(data.productId, stock, status)

        filterTab?.getSelectedFilter()?.let {
            filterProductListByStatus(it)
            renderMultiSelectProduct()
        }

        getFiltersTab(withDelay = true)
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

    private fun initHeaderView(productList: List<ProductUiModel>) {
        if (isLoadingInitialData && showProductEmptyState()) {
            searchBar.showWithCondition(productList.isNotEmpty())
            tabSortFilter.showWithCondition(productList.isNotEmpty())
        }
    }

    private fun clearSelectedProduct() {
        itemsChecked.clear()
    }

    private fun resetSelectAllCheckBox() {
        if (checkBoxSelectAll.isChecked) {
            checkBoxSelectAll.isChecked = false
            checkBoxSelectAll.setIndeterminate(false)
        }
    }

    private fun renderProductCount() {
        val productCount = if (filterTab?.isFilterActive() == true) {
            filterTab?.getProductCount()
        } else {
            viewModel.getTotalProductCount()
        }
        textProductCount.text = getString(R.string.product_manage_count_format, productCount)
    }

    private fun showHideOptionsMenu() {
        viewModel.showHideOptionsMenu()
    }

    private fun showAddEditMenuBottomSheet() {
        productManageAddEditMenuBottomSheet.show()
    }

    companion object {
        private const val BOTTOM_SHEET_TAG = "BottomSheetTag"

        private const val MIN_FEATURED_PRODUCT = 0
        private const val MAX_FEATURED_PRODUCT = 5

    }

}