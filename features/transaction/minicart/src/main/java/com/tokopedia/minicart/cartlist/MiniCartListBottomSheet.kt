package com.tokopedia.minicart.cartlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cartcommon.domain.data.RemoveFromCartDomainModel
import com.tokopedia.cartcommon.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.MiniCartBottomSheetUnify
import com.tokopedia.minicart.MiniCartBottomSheetUnifyListener
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapter
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.cartlist.subpage.summarytransaction.SummaryTransactionBottomSheet
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListBottomSheet
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.data.tracker.ProductBundleRecomTracker
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.databinding.LayoutBottomsheetMiniCartListBinding
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor(private var miniCartListDecoration: MiniCartListDecoration,
                                                  var summaryTransactionBottomSheet: SummaryTransactionBottomSheet,
                                                  var analytics: MiniCartAnalytics)
    : MiniCartListActionListener, MiniCartBottomSheetUnifyListener {

    companion object {
        const val STATE_PRODUCT_BUNDLE_RECOM_ATC = "product_bundle_recom_atc"
        const val STATE_PRODUCT_BUNDLE_RECOM_CLICKED = "product_bundle_recom_clicked"
        const val STATE_PRODUCT_BUNDLE_RECOM_IMPRESSED = "product_bundle_recom_impressed"

        private const val LONG_DELAY = 500L
        private const val SHORT_DELAY = 200L

        private const val REQUEST_EDIT_BUNDLE = 101

        private const val KEY_OLD_BUNDLE_ID = "old_bundle_id"
        private const val KEY_NEW_BUNLDE_ID = "new_bundle_id"
        private const val KEY_IS_CHANGE_VARIANT = "is_variant_changed"

        private const val BSP_PAGE_SOURCE = "minicart"
    }

    private var viewBinding: LayoutBottomsheetMiniCartListBinding? = null
    private var viewModel: MiniCartViewModel? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var adapter: MiniCartListAdapter? = null
    private var bottomSheetListener: MiniCartListBottomSheetListener? = null
    private var miniCartChevronClickListener: View.OnClickListener? = null

    private var measureRecyclerViewPaddingDebounceJob: Job? = null
    private var updateCartDebounceJob: Job? = null
    private var calculationDebounceJob: Job? = null

    private var globalEventObserver: Observer<GlobalEvent>? = null
    private var bottomSheetUiModelObserver: Observer<MiniCartListUiModel>? = null
    private var productBundleRecomTrackerObserver: Observer<ProductBundleRecomTracker>? = null

    private var isShow: Boolean = false

    // temporary variable to handle case edit bundle
    // this is useful if there are multiple same bundleId in cart
    private var toBeDeletedBundleGroupId = ""

    @Inject
    lateinit var miniCartChatListBottomSheet: MiniCartChatListBottomSheet

    fun show(context: Context?,
             fragmentManager: FragmentManager,
             lifecycleOwner: LifecycleOwner,
             viewModel: MiniCartViewModel,
             bottomSheetListener: MiniCartListBottomSheetListener) {
        context?.let {
            if (!isShow) {
                this.bottomSheetListener = bottomSheetListener
                val viewBinding = LayoutBottomsheetMiniCartListBinding.inflate(LayoutInflater.from(context))
                this.viewBinding = viewBinding
                initializeView(it, viewBinding, fragmentManager)
                initializeViewModel(viewBinding, fragmentManager, viewModel, lifecycleOwner)
                initializeCartData(viewBinding, viewModel)
            }
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }

    private fun resetObserver() {
        globalEventObserver?.let {
            viewModel?.globalEvent?.removeObserver(it)
            globalEventObserver = null
        }
        bottomSheetUiModelObserver?.let {
            viewModel?.miniCartListBottomSheetUiModel?.removeObserver(it)
            bottomSheetUiModelObserver = null
        }
        productBundleRecomTrackerObserver?.let {
            viewModel?.productBundleRecomTracker?.removeObserver(it)
            productBundleRecomTrackerObserver = null
        }
    }

    private fun onResultFromEditBundle(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val oldBundleId = data?.getStringExtra(KEY_OLD_BUNDLE_ID) ?: ""
            val newBundleId = data?.getStringExtra(KEY_NEW_BUNLDE_ID) ?: ""
            val isChangeVariant = data?.getBooleanExtra(KEY_IS_CHANGE_VARIANT, false) ?: false
            if (((oldBundleId.isNotBlank() && newBundleId.isNotBlank() && oldBundleId != newBundleId) || isChangeVariant) && toBeDeletedBundleGroupId.isNotEmpty()) {
                val list = viewModel?.miniCartListBottomSheetUiModel?.value?.visitables ?: emptyList()
                val deletedItems = list.filter { it is MiniCartProductUiModel && it.isBundlingItem && it.bundleId == oldBundleId && it.bundleGroupId == toBeDeletedBundleGroupId }
                toBeDeletedBundleGroupId = ""
                if (deletedItems.isNotEmpty()) {
                    viewModel?.deleteMultipleCartItems(deletedItems as List<MiniCartProductUiModel>, isFromEditBundle = true)
                }
            } else {
                viewModel?.getCartList()
            }
        }
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager) {
        bottomSheet = MiniCartBottomSheetUnify().apply {
            listener = this@MiniCartListBottomSheet
            showCloseIcon = false
            showHeader = true
            isDragable = true
            isHideable = true
            clearContentPadding = true
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
            setShowListener {
                isShow = true
                knobView.setOnClickListener {
                    analytics.eventClickKnobToExpandMiniCartBottomSheet()
                }
            }
            setOnDismissListener {
                isShow = false
                cancelAllDebounceJob()
                resetObserver()
                Toaster.onCTAClick = View.OnClickListener { }
                bottomSheetListener?.onMiniCartListBottomSheetDismissed()
                this@MiniCartListBottomSheet.viewBinding = null
                this@MiniCartListBottomSheet.bottomSheet = null
            }
            setChild(viewBinding.root)
            show(fragmentManager, this.javaClass.simpleName)
        }
    }

    private fun initializeCartData(viewBinding: LayoutBottomsheetMiniCartListBinding, viewModel: MiniCartViewModel) {
        adapter?.clearAllElements()
        bottomSheet?.setTitle("")
        showLoading()
        setTotalAmountLoading(viewBinding, true)
        viewModel.getCartList(isFirstLoad = true)
    }

    private fun initializeView(context: Context, viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager) {
        initializeBottomSheet(viewBinding, fragmentManager)
        initializeTotalAmount(viewBinding, fragmentManager, context)
        initializeRecyclerView(viewBinding)
    }

    private fun initializeViewModel(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager, viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        this.viewModel = viewModel
        viewModel.initializeGlobalState()

        initializeGlobalEventObserver(viewBinding, viewModel, fragmentManager)
        initializeBottomSheetUiModelObserver(viewBinding, fragmentManager, viewModel, lifecycleOwner)
        initializeProductBundleRecomAtcTrackerObserver()

        observeGlobalEvent(viewModel, lifecycleOwner)
        observeMiniCartListUiModel(viewModel, lifecycleOwner)
        observeProductBundleRecomAtcTracker(viewModel, lifecycleOwner)
    }

    private fun initializeRecyclerView(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        val adapterTypeFactory = MiniCartListAdapterTypeFactory(this, multiProductBundleCallback(), singleProductBundleCallback())
        adapter = MiniCartListAdapter(adapterTypeFactory)
        viewBinding.rvMiniCartList.adapter = adapter
        viewBinding.rvMiniCartList.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
        viewBinding.rvMiniCartList.addItemDecoration(miniCartListDecoration)
    }

    private fun goToPDP(productId: String) {
        bottomSheet?.context?.let { context ->
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            context.startActivity(intent)
        }
    }

    private fun multiProductBundleCallback() = object : MultipleProductBundleListener {

        override fun onMultipleBundleProductClicked(
            shopId: String,
            warehouseId: String,
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundleType: String,
            bundlePosition: Int,
            widgetTitle: String,
            widgetName: String,
            productItemPosition: Int
        ) {
            viewModel?.trackProductBundleRecom(
                shopId = shopId,
                warehouseId = warehouseId,
                bundleId = selectedMultipleBundle.bundleId,
                bundleName = bundleName,
                bundleType = bundleType,
                bundlePosition = bundlePosition,
                priceCut = selectedMultipleBundle.displayPrice,
                state = STATE_PRODUCT_BUNDLE_RECOM_CLICKED
            )

            goToPDP(selectedProduct.productId)
        }

        override fun addMultipleBundleToCart(
            shopId: String,
            warehouseId: String,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleListSize: Int,
            productDetails: List<ShopHomeBundleProductUiModel>,
            bundleName: String,
            bundleType: String,
            bundlePosition: Int,
            widgetLayout: ShopHomeWidgetLayout,
            bundleGroupId: String
        ) {
            if (selectedMultipleBundle.isProductsHaveVariant) {
                goToBundlingSelectionPage(selectedMultipleBundle.bundleId)
            } else {
                showProgressLoading()

                viewModel?.addBundleToCart(
                    shopId = shopId,
                    warehouseId = warehouseId,
                    bundleId = selectedMultipleBundle.bundleId,
                    bundleName = bundleName,
                    bundleType = bundleType,
                    bundlePosition = bundlePosition,
                    priceCut = selectedMultipleBundle.displayPrice,
                    productDetails = productDetails,
                    productQuantity = selectedMultipleBundle.minOrder
                )
            }
        }

        override fun impressionProductBundleMultiple(
            shopId: String,
            warehouseId: String,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundleType: String,
            bundlePosition: Int
        ) {
            viewModel?.trackProductBundleRecom(
                shopId = shopId,
                warehouseId = warehouseId,
                bundleId = selectedMultipleBundle.bundleId,
                bundleName = bundleName,
                bundleType = bundleType,
                bundlePosition = bundlePosition,
                priceCut = selectedMultipleBundle.displayPrice,
                state = STATE_PRODUCT_BUNDLE_RECOM_IMPRESSED
            )
        }

        override fun impressionProductItemBundleMultiple(
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundlePosition: Int,
            widgetTitle: String,
            widgetName: String,
            productItemPosition: Int
        ) { /* nothing to do */ }

    }

    private fun singleProductBundleCallback() = object : SingleProductBundleListener {

        override fun onSingleBundleProductClicked(
            shopId: String,
            warehouseId: String,
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String,
            bundlePosition: Int,
            widgetTitle: String,
            widgetName: String,
            productItemPosition: Int,
            bundleType: String
        ) {
            viewModel?.trackProductBundleRecom(
                shopId = shopId,
                warehouseId = warehouseId,
                bundleId = selectedSingleBundle.bundleId,
                bundleName = bundleName,
                bundleType = bundleType,
                bundlePosition = bundlePosition,
                priceCut = selectedSingleBundle.displayPrice,
                state = STATE_PRODUCT_BUNDLE_RECOM_CLICKED
            )

            goToPDP(selectedProduct.productId)
        }

        override fun addSingleBundleToCart(
            shopId: String,
            warehouseId: String,
            selectedBundle: ShopHomeProductBundleDetailUiModel,
            bundleListSize: Int,
            bundleProducts: ShopHomeBundleProductUiModel,
            bundleName: String,
            bundleType: String,
            bundlePosition: Int,
            widgetLayout: ShopHomeWidgetLayout,
            bundleGroupId: String
        ) {
            if (selectedBundle.isProductsHaveVariant) {
                goToBundlingSelectionPage(selectedBundle.bundleId)
            } else {
                showProgressLoading()

                viewModel?.addBundleToCart(
                    shopId = shopId,
                    warehouseId = warehouseId,
                    bundleId = selectedBundle.bundleId,
                    bundleName = bundleName,
                    bundleType = bundleType,
                    bundlePosition = bundlePosition,
                    priceCut = selectedBundle.displayPrice,
                    productDetails = listOf(bundleProducts),
                    productQuantity = selectedBundle.minOrder
                )
            }
        }

        override fun onTrackSingleVariantChange(
            selectedProduct: ShopHomeBundleProductUiModel,
            selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
            bundleName: String
        ) { /* nothing to do */ }

        override fun impressionProductBundleSingle(
            shopId: String,
            warehouseId: String,
            selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
            selectedProduct: ShopHomeBundleProductUiModel,
            bundleName: String,
            bundlePosition: Int,
            widgetTitle: String,
            widgetName: String,
            bundleType: String
        ) {
            viewModel?.trackProductBundleRecom(
                shopId = shopId,
                warehouseId = warehouseId,
                bundleId = selectedSingleBundle.bundleId,
                bundleName = bundleName,
                bundleType = bundleType,
                bundlePosition = bundlePosition,
                priceCut = selectedSingleBundle.displayPrice,
                state = STATE_PRODUCT_BUNDLE_RECOM_IMPRESSED
            )
        }
    }

    private fun initializeTotalAmount(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager, context: Context) {
        viewBinding.totalAmount.let {
            miniCartChevronClickListener = View.OnClickListener {
                analytics.eventClickChevronToShowSummaryTransaction()
                viewModel?.miniCartListBottomSheetUiModel?.value?.miniCartSummaryTransactionUiModel?.let { miniCartSummaryTransaction ->
                    summaryTransactionBottomSheet.show(miniCartSummaryTransaction, fragmentManager, context)
                }
            }
            it.amountChevronView.setOnClickListener(miniCartChevronClickListener)
            it.amountCtaView.setOnClickListener {
                sendEventClickBuy()
                showProgressLoading()
                viewModel?.goToCheckout(GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET)
            }
            it.context?.let { context ->
                validateTotalAmountView(context, viewBinding)
            }
            setTotalAmountLoading(viewBinding, true)
        }
    }

    private fun sendEventClickBuy() {
        val pageName = viewModel?.currentPage?.value ?: MiniCartAnalytics.Page.HOME_PAGE
        val products = viewModel?.miniCartListBottomSheetUiModel?.value?.getMiniCartProductUiModelList()
                ?: emptyList()
        val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
        analytics.eventClickBuy(pageName, products, isOCCFlow)
    }

    private fun initializeGlobalEventObserver(viewBinding: LayoutBottomsheetMiniCartListBinding, viewModel: MiniCartViewModel, fragmentManager: FragmentManager) {
        globalEventObserver = Observer<GlobalEvent> {
            when (it.state) {
                GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM -> {
                    onSuccessDeleteCartItem(viewBinding, it, viewModel)
                }
                GlobalEvent.STATE_FAILED_DELETE_CART_ITEM -> {
                    onFailedDeleteCartItem(viewBinding, it)
                }
                GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM -> {
                    onSuccessUndoDeleteCartItem(viewBinding, it, viewModel)
                }
                GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM -> {
                    onFailedUndoDeleteCartItem(viewBinding, it)
                }
                GlobalEvent.STATE_SUCCESS_TO_CHECKOUT -> {
                    onSuccessGoToCheckout(it)
                }
                GlobalEvent.STATE_FAILED_TO_CHECKOUT -> {
                    onFailedGoToCheckout(viewBinding, it, fragmentManager)
                }
                GlobalEvent.STATE_SUCCESS_ADD_TO_CART_BUNDLE_RECOM_ITEM -> {
                    onSuccessAddToCartProductBundleRecom(viewBinding)
                }
                GlobalEvent.STATE_FAILED_ADD_TO_CART_BUNDLE_RECOM_ITEM -> {
                    onFailedAddToCartProductBundleRecom(it, viewBinding)
                }
            }
        }
    }

    private fun initializeProductBundleRecomAtcTrackerObserver() {
        productBundleRecomTrackerObserver = Observer<ProductBundleRecomTracker> {
            when(it.state) {
                STATE_PRODUCT_BUNDLE_RECOM_ATC -> analytics.eventClickProductBundleRecomAtc(
                    shopId = it.shopId,
                    warehouseId = it.warehouseId,
                    bundleId = it.bundleId,
                    bundleName = it.bundleName,
                    bundleType = it.bundleType,
                    bundlePosition = it.bundlePosition,
                    priceCut = it.priceCut,
                    atcItems = it.atcItems
                )
                STATE_PRODUCT_BUNDLE_RECOM_CLICKED -> analytics.eventClickProductBundleRecom(
                    shopId = it.shopId,
                    warehouseId = it.warehouseId,
                    bundleId = it.bundleId,
                    bundleName = it.bundleName,
                    bundleType = it.bundleType,
                    bundlePosition = it.bundlePosition,
                    priceCut = it.priceCut
                )
                STATE_PRODUCT_BUNDLE_RECOM_IMPRESSED -> analytics.eventProductBundleRecomImpression(
                    shopId = it.shopId,
                    warehouseId = it.warehouseId,
                    bundleId = it.bundleId,
                    bundleName = it.bundleName,
                    bundleType = it.bundleType,
                    bundlePosition = it.bundlePosition,
                    priceCut = it.priceCut
                )
            }
        }
    }

    private fun onSuccessAddToCartProductBundleRecom(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        viewModel?.getCartList()

        hideProgressLoading()

        viewBinding.bottomsheetContainer.let { container ->
            bottomSheetListener?.showToaster(
                view = container,
                message = container.context.getString(R.string.mini_cart_product_bundle_recommendation_success_toaster_description),
                type = Toaster.TYPE_NORMAL
            )
        }
    }

    private fun onFailedAddToCartProductBundleRecom(globalEvent: GlobalEvent, viewBinding: LayoutBottomsheetMiniCartListBinding) {
        hideProgressLoading()

        val messageError = globalEvent.data as? String

        viewBinding.bottomsheetContainer.let { container ->
            bottomSheetListener?.showToaster(
                view = container,
                message = if (messageError.isNullOrEmpty()) ErrorHandler.getErrorMessage(context = container.context, globalEvent.throwable) else messageError,
                type = Toaster.TYPE_ERROR
            )
        }
    }

    private fun onFailedUndoDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent) {
        hideProgressLoading()
        globalEvent.throwable?.let { throwable ->
            bottomSheet?.context?.let { context ->
                var message = ErrorHandler.getErrorMessage(context, throwable)
                if (throwable is ResponseErrorException) {
                    message = throwable.message.toString()
                }
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessUndoDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent, viewModel: MiniCartViewModel) {
        hideProgressLoading()
        viewModel.getCartList()
        val data = globalEvent.data as? UndoDeleteCartDomainModel
        val message = data?.undoDeleteCartDataResponse?.data?.message?.firstOrNull() ?: ""
        if (message.isNotBlank()) {
            val ctaText = bottomSheet?.context?.getString(R.string.mini_cart_cta_ok)
                    ?: ""
            viewBinding.bottomsheetContainer.let { view ->
                bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL, ctaText)
            }
        }
    }

    private fun initializeBottomSheetUiModelObserver(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager, viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        bottomSheetUiModelObserver = Observer<MiniCartListUiModel> {
            if (it == null) return@Observer

            if (it.miniCartWidgetUiModel.totalProductCount == 0 && it.miniCartWidgetUiModel.totalProductError == 0) {
                dismiss()
            }

            if (it.needToCalculateAfterLoad) {
                calculateProduct()
            } else {
                hideLoading()
                hideProgressLoading()
                // Need to try-catch since can't check is lateinit `bottomSheetClose` has already initialized or not using `isInitialized`
                try {
                    bottomSheet?.bottomSheetClose?.show()
                } catch (e: UninitializedPropertyAccessException) {
                    // No-op
                }
                bottomSheet?.setTitle(it.title)
                if (viewBinding.rvMiniCartList.isComputingLayout) {
                    viewBinding.rvMiniCartList.post {
                        adapter?.updateList(it.visitables)
                    }
                } else {
                    adapter?.updateList(it.visitables)
                }
                updateTotalAmount(viewBinding, it.miniCartWidgetUiModel)
                adjustRecyclerViewPaddingBottom(viewBinding)

                if (it.isFirstLoad) {
                    sendFirstLoadAnalytics(it)
                    it.isFirstLoad = false
                    // Collapse unavailable items on first load
                    viewModel?.toggleUnavailableItemsAccordion()
                }
            }

            viewBinding.totalAmount.totalAmountAdditionalButton.setOnClickListener {
                analytics.eventClickChatOnMiniCart()
                miniCartChatListBottomSheet.show(context = viewBinding.totalAmount.context, fragmentManager = fragmentManager, lifecycleOwner = lifecycleOwner, viewModel = viewModel)
                dismiss()
            }
        }
    }

    private fun sendFirstLoadAnalytics(miniCartListUiModel: MiniCartListUiModel) {
        analytics.eventLoadMiniCartBottomSheetSuccess(miniCartListUiModel.getMiniCartProductUiModelList())
        val overweightData = miniCartListUiModel.getMiniCartTickerWarningUiModel()
        if (overweightData != null) {
            analytics.eventViewErrorTickerOverweightInMiniCart(overweightData.warningMessage)
        }
        val shopId = viewModel?.currentShopIds?.value?.firstOrNull() ?: ""
        loop@ for (visitable in miniCartListUiModel.visitables) {
            if (visitable is MiniCartProductUiModel && visitable.isProductDisabled) {
                analytics.eventViewTickerErrorUnavailableProduct(shopId, visitable.errorType)
                break@loop
            }
        }
    }

    private fun observeGlobalEvent(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        globalEventObserver?.let {
            viewModel.globalEvent.observe(lifecycleOwner, it)
        }
    }

    private fun onFailedGoToCheckout(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent, fragmentManager: FragmentManager) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            hideProgressLoading()
            viewBinding.bottomsheetContainer.let { view ->
                bottomSheetListener?.onBottomSheetFailedGoToCheckout(view, fragmentManager, globalEvent)
            }
        }
    }

    private fun onSuccessGoToCheckout(globalEvent: GlobalEvent) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            bottomSheet?.context.let {
                hideProgressLoading()
                bottomSheetListener?.onBottomSheetSuccessGoToCheckout()
                dismiss()
            }
        }
    }

    private fun onFailedDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent) {
        hideProgressLoading()
        globalEvent.throwable?.let { throwable ->
            bottomSheet?.context?.let { context ->
                var message = ErrorHandler.getErrorMessage(context, throwable)
                if (throwable is ResponseErrorException) {
                    message = throwable.message.toString()
                }
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent, viewModel: MiniCartViewModel) {
        val data = globalEvent.data as? RemoveFromCartDomainModel
        // last item should be handled by mini cart widget, since the bottomsheet already dismissed
        if (data?.isLastItem == true) return

        hideProgressLoading()
        val message = if (data?.isFromEditBundle == true) {
            bottomSheet?.context?.getString(R.string.mini_cart_message_toaster_change_bundle_success)
        } else {
            data?.removeFromCartData?.data?.message?.firstOrNull()
        } ?: ""
        if (message.isNotBlank()) {
            val ctaText = bottomSheet?.context?.getString(R.string.mini_cart_cta_cancel) ?: ""
            viewModel.getCartList()
            if (data?.isFromEditBundle == true || data?.isBulkDelete == true) {
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL)
                }
            } else {
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL, ctaText) {
                        analytics.eventClickUndoDelete()
                        showProgressLoading()
                        viewModel.undoDeleteCartItem(false)
                    }
                }
            }
        }
    }

    private fun observeMiniCartListUiModel(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        bottomSheetUiModelObserver?.let {
            viewModel.miniCartListBottomSheetUiModel.observe(lifecycleOwner, it)
        }
    }

    private fun observeProductBundleRecomAtcTracker(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        productBundleRecomTrackerObserver?.let {
            viewModel.productBundleRecomTracker.observe(lifecycleOwner, it)
        }
    }

    private fun adjustRecyclerViewPaddingBottom(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        measureRecyclerViewPaddingDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(LONG_DELAY)
            adjustRecyclerViewPadding(viewBinding)
        }
    }

    private fun adjustRecyclerViewPadding(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        with(viewBinding) {
            if (rvMiniCartList.canScrollVertically(-1) || rvMiniCartList.canScrollVertically(1) || isBottomSheetFullPage(viewBinding)) {
                rvMiniCartList.setPadding(0, 0, 0, rvMiniCartList.resources?.getDimensionPixelSize(R.dimen.dp_64)
                        ?: 0)
            } else {
                rvMiniCartList.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun isBottomSheetFullPage(viewBinding: LayoutBottomsheetMiniCartListBinding): Boolean {
        val displayMetrics = Resources.getSystem().displayMetrics
        bottomSheet?.activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val bottomSheetHeight = (bottomSheet?.bottomSheetWrapper?.parent as? View)?.height ?: 0
        val recyclerViewPaddingBottom = viewBinding.rvMiniCartList.resources?.getDimensionPixelSize(R.dimen.dp_64)
                ?: 0
        val displayHeight = displayMetrics?.heightPixels ?: 0
        return bottomSheetHeight != 0 && displayHeight != 0 && (bottomSheetHeight + (recyclerViewPaddingBottom / 2)) >= displayHeight
    }

    private fun cancelAllDebounceJob() {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        updateCartDebounceJob?.cancel()
        calculationDebounceJob?.cancel()
    }

    private fun setTotalAmountLoading(viewBinding: LayoutBottomsheetMiniCartListBinding, isLoading: Boolean) {
        with(viewBinding) {
            if (isLoading) {
                if (!totalAmount.isTotalAmountLoading) {
                    totalAmount.isTotalAmountLoading = true
                }
            } else {
                if (totalAmount.isTotalAmountLoading) {
                    totalAmount.isTotalAmountLoading = false
                }
            }
            root.context?.let {
                validateTotalAmountView(it, viewBinding)
            }
        }
    }

    private fun validateTotalAmountView(context: Context, viewBinding: LayoutBottomsheetMiniCartListBinding) {
        with(viewBinding) {
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            totalAmount.setAdditionalButton(chatIcon)
            this.chatIcon.setImageDrawable(chatIcon)
            totalAmount.amountChevronView.setOnClickListener(miniCartChevronClickListener)
        }
    }

    private fun updateTotalAmount(viewBinding: LayoutBottomsheetMiniCartListBinding, miniCartWidgetData: MiniCartWidgetData) {
        viewBinding.totalAmount.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            if (miniCartWidgetData.totalProductCount == 0) {
                setAmount("-")
                val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                        ?: context.getString(R.string.mini_cart_widget_cta_text_default)
                setCtaText(ctaText)
                amountCtaView.isEnabled = false
                enableAmountChevron(false)
            } else {
                setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetData.totalProductPrice, false).removeDecimalSuffix())
                val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                        ?: context.getString(R.string.mini_cart_widget_cta_text_default)
                setCtaText("$ctaText (${miniCartWidgetData.totalProductCount})")
                amountCtaView.isEnabled = true
                enableAmountChevron(true)
            }
            amountCtaView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
            validateAmountCtaLabel(viewBinding, miniCartWidgetData)
        }
        setTotalAmountLoading(viewBinding, false)
    }

    private fun validateAmountCtaLabel(viewBinding: LayoutBottomsheetMiniCartListBinding, miniCartWidgetData: MiniCartWidgetData) {
        if (viewModel?.miniCartABTestData?.value?.isOCCFlow == true) {
            viewBinding.totalAmount.post {
                val ellipsis = viewBinding.totalAmount.amountCtaView.layout?.getEllipsisCount(0)
                        ?: 0
                if (ellipsis > 0) {
                    val ctaText = viewBinding.totalAmount.context.getString(R.string.mini_cart_widget_cta_text_default)
                    if (miniCartWidgetData.totalProductCount == 0) {
                        viewBinding.totalAmount.setCtaText(ctaText)
                    } else {
                        viewBinding.totalAmount.setCtaText("$ctaText (${miniCartWidgetData.totalProductCount})")
                    }
                }
            }
        }
    }

    private fun showLoading() {
        adapter?.let {
            it.removeErrorNetwork()
            it.setLoadingModel(LoadingModel())
            it.showLoading()
        }
    }

    private fun hideLoading() {
        adapter?.hideLoading()
    }

    private fun showProgressLoading() {
        bottomSheetListener?.showProgressLoading()
    }

    private fun goToBundlingSelectionPage(bundleId: String) {
        val bundlingSelectionPageAppLink = UriUtil.buildUri(
            ApplinkConstInternalMechant.MERCHANT_PRODUCT_BUNDLE,
            ShopHomeProductBundleItemUiModel.DEFAULT_BUNDLE_PRODUCT_PARENT_ID
        )
        val bundleAppLinkWithParams = Uri.parse(bundlingSelectionPageAppLink).buildUpon()
            .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_BUNDLE_ID, bundleId)
            .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_PAGE_SOURCE, BSP_PAGE_SOURCE)
            .build()
            .toString()
        bottomSheet?.context?.let {
            val bspIntent = RouteManager.getIntent(it, bundleAppLinkWithParams)
            bottomSheet?.startActivityForResult(bspIntent, REQUEST_EDIT_BUNDLE)
        }
    }

    private fun hideProgressLoading() {
        bottomSheetListener?.hideProgressLoading()
    }

    private fun updateCart() {
        updateCartDebounceJob?.cancel()
        updateCartDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(LONG_DELAY)
            viewModel?.updateCart()
        }
    }

    private fun calculateProduct() {
        calculationDebounceJob?.cancel()
        calculationDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(SHORT_DELAY)
            viewModel?.calculateProduct()
        }
    }

    override fun onDeleteClicked(element: MiniCartProductUiModel) {
        analytics.eventClickDeleteFromTrashBin()
        bottomSheetListener?.showProgressLoading()
        if (element.isBundlingItem) {
            val list = viewModel?.miniCartListBottomSheetUiModel?.value?.visitables ?: emptyList()
            val deletedItems = list.filter { it is MiniCartProductUiModel && it.isBundlingItem && it.bundleId == element.bundleId && it.bundleGroupId == element.bundleGroupId } as List<MiniCartProductUiModel>
            if (deletedItems.isNotEmpty()) {
                viewModel?.deleteMultipleCartItems(deletedItems)
            }
        } else {
            viewModel?.deleteSingleCartItem(element)
        }
    }

    override fun onBulkDeleteUnavailableItems() {
        analytics.eventClickDeleteAllUnavailableProduct()
        val unavailableProducts = viewModel?.miniCartListBottomSheetUiModel?.value?.getUnavailableProduct()
                ?: emptyList()
        val hiddenUnavailableProducts = viewModel?.tmpHiddenUnavailableItems ?: emptyList()
        val allUnavailableProducts = unavailableProducts + hiddenUnavailableProducts
        bottomSheet?.context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.mini_cart_label_dialog_title_delete_unavailable_multiple_item, allUnavailableProducts.size))
                setDescription(it.getString(R.string.mini_cart_label_dialog_message_remove_cart_unavailable_multiple_item))
                setPrimaryCTAText(it.getString(R.string.mini_cart_label_dialog_action_delete))
                setSecondaryCTAText(it.getString(R.string.mini_cart_label_dialog_action_cancel))
                setPrimaryCTAClickListener {
                    dismiss()
                    showProgressLoading()
                    viewModel?.bulkDeleteUnavailableCartItems()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    override fun onQuantityChanged(element: MiniCartProductUiModel, newQty: Int) {
        viewModel?.updateProductQty(element, newQty)
        calculateProduct()
        updateCart()
    }

    override fun onNotesChanged(productId: String, isBundlingItem: Boolean, bundleId: String, bundleGroupId: String, newNotes: String) {
        viewModel?.updateProductNotes(productId, isBundlingItem, bundleId, bundleGroupId, newNotes)
        updateCart()
    }

    override fun onShowSimilarProductClicked(appLink: String, element: MiniCartProductUiModel) {
        analytics.eventClickSeeSimilarProductOnUnavailableSection(element.productId, element.errorType)
        bottomSheet?.context?.let {
            RouteManager.route(it, appLink)
        }
    }

    override fun onShowUnavailableItemsCLicked() {
        scrollToUnavailableSection()
    }

    fun scrollToUnavailableSection() {
        val data = adapter?.data ?: emptyList()
        loop@ for ((index, visitable) in data.withIndex()) {
            if (visitable is MiniCartProductUiModel && visitable.isProductDisabled) {
                viewBinding?.rvMiniCartList?.smoothScrollToPosition(index)
                break@loop
            }
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel?.toggleUnavailableItemsAccordion()
        val lastItemPosition = (adapter?.list?.size ?: 0) - 1
        if (lastItemPosition != -1) {
            viewBinding?.rvMiniCartList?.smoothScrollToPosition(lastItemPosition)
        }
    }

    override fun onProductInfoClicked(element: MiniCartProductUiModel) {
        analytics.eventClickProductName(element.productId)
        bottomSheet?.context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, element.productId)
            it.startActivity(intent)
        }
    }

    override fun onQuantityPlusClicked() {
        analytics.eventClickQuantityPlus()
    }

    override fun onQuantityMinusClicked() {
        analytics.eventClickQuantityMinus()
    }

    override fun onInputQuantityClicked(qty: Int) {
        analytics.eventClickInputQuantity(qty)
    }

    override fun onWriteNotesClicked() {
        analytics.eventClickWriteNotes()
    }

    override fun onChangeNotesClicked() {
        analytics.eventClickChangeNotes()
    }

    override fun onChangeBundleClicked(element: MiniCartProductUiModel) {
        bottomSheet?.context?.let {
            val intent = RouteManager.getIntentNoFallback(it, element.editBundleApplink) ?: return
            analytics.eventClickChangeProductBundle()
            toBeDeletedBundleGroupId = element.bundleGroupId
            bottomSheet?.startActivityForResult(intent, REQUEST_EDIT_BUNDLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_EDIT_BUNDLE) {
            onResultFromEditBundle(resultCode, data)
        }
    }
}
