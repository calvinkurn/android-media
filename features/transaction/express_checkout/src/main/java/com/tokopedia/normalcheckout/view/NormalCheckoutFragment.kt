package com.tokopedia.normalcheckout.view

import android.app.Activity
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantItemDecorator
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.normalcheckout.adapter.NormalCheckoutAdapterTypeFactory
import com.tokopedia.normalcheckout.constant.*
import com.tokopedia.normalcheckout.di.DaggerNormalCheckoutComponent
import com.tokopedia.normalcheckout.model.ProductInfoAndVariant
import com.tokopedia.normalcheckout.presenter.NormalCheckoutViewModel
import com.tokopedia.normalcheckout.router.NormalCheckoutRouter
import com.tokopedia.payment.activity.TopPayActivity
import com.tokopedia.payment.model.PaymentPassData
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.track.TrackApp
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_normal_checkout.*
import com.tokopedia.tradein.model.TradeInParams
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import com.tokopedia.tradein.view.viewcontrollers.FinalPriceActivity
import com.tokopedia.tradein.view.viewcontrollers.TradeInHomeActivity
import javax.inject.Inject

class NormalCheckoutFragment : BaseListFragment<Visitable<*>, CheckoutVariantAdapterTypeFactory>(),
        NormalCheckoutContract.View, CheckoutVariantActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: NormalCheckoutViewModel

    var loadingProgressDialog: ProgressDialog? = null
    val fragmentViewModel: FragmentViewModel by lazy {
        FragmentViewModel()
    }
    private val normalCheckoutTracking: NormalCheckoutTracking by lazy {
        NormalCheckoutTracking()
    }
    private lateinit var router: NormalCheckoutRouter
    private lateinit var adapter: CheckoutVariantAdapter

    var shopId: String? = null
    lateinit var productId: String
    var notes: String? = null
    var quantity: Int = 0
    var tempQuantity = quantity
    var isTradeIn = 0
    var isOcs = true
    var isLeasing = true
    var selectedVariantId: String? = null
    var placeholderProductImage: String? = null
    @ProductAction
    var action: Int = ATC_AND_BUY

    var selectedProductInfo: ProductInfo? = null
    var originalProduct: ProductInfoAndVariant? = null

    var trackerAttribution: String? = null
    var trackerListName: String? = null
    var shopType: String? = null
    var shopName: String? = null
    private var tradeInParams: TradeInParams? = null
    val currencyLabel = "IDR"


    companion object {
        const val EXTRA_SHOP_ID = "shop_id"
        const val EXTRA_PRODUCT_ID = "product_id"
        const val EXTRA_NOTES = "notes"
        const val EXTRA_QUANTITY = "quantity"
        const val EXTRA_SELECTED_VARIANT_ID = "selected_variant_id"
        const val EXTRA_ACTION = "action"
        const val EXTRA_PRODUCT_IMAGE = "product_image"
        const val EXTRA_SHOP_TYPE = "shop_type"
        const val EXTRA_SHOP_NAME = "shop_name"
        const val EXTRA_OCS = "ocs"
        const val EXTRA_IS_LEASING = "is_leasing"
        const val EXTRA_TRADE_IN_PARAMS = "trade_in_params"
        const val EXTRA_CART_ID = "cart_id"
        private const val TRACKER_ATTRIBUTION = "tracker_attribution"
        private const val TRACKER_LIST_NAME = "tracker_list_name"

        const val RESULT_PRODUCT_DATA_CACHE_ID = "product_data_cache"
        const val RESULT_SELECTED_WAREHOUSE = "selected_warehouse"
        const val RESULT_PRODUCT_DATA = "product_data"
        const val RESULT_ATC_SUCCESS_MESSAGE = "atc_success_message"

        const val REQUEST_CODE_LOGIN = 561
        const val REQUEST_CODE_LOGIN_THEN_ATC = 562
        const val REQUEST_CODE_LOGIN_THEN_BUY = 563
        const val REQUEST_CODE_LOGIN_THEN_TRADE_IN = 564

        fun createInstance(shopId: String?, productId: String?,
                           notes: String? = "", quantity: Int? = 0,
                           selectedVariantId: String? = null,
                           @ProductAction action: Int = ATC_AND_BUY,
                           placeholderProductImage: String?,
                           trackerAttribution: String? = "",
                           trackerListName: String? = "",
                           shopType: String? = "",
                           shopName: String? = "",
                           isOneClickShipment: Boolean,
                           isLeasing: Boolean,
                           tradeInParams: TradeInParams?): NormalCheckoutFragment {
            val fragment = NormalCheckoutFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SHOP_ID, shopId)
                    putString(EXTRA_PRODUCT_ID, productId)
                    putString(EXTRA_NOTES, notes)
                    putInt(EXTRA_QUANTITY, quantity ?: 0)
                    putInt(EXTRA_ACTION, action)
                    putString(EXTRA_PRODUCT_IMAGE, placeholderProductImage)
                    putString(EXTRA_SELECTED_VARIANT_ID, selectedVariantId ?: "")
                    putString(TRACKER_ATTRIBUTION, trackerAttribution ?: "")
                    putString(TRACKER_LIST_NAME, trackerListName ?: "")
                    putString(EXTRA_SHOP_TYPE, shopType ?: "")
                    putString(EXTRA_SHOP_NAME, shopName ?: "")
                    putBoolean(EXTRA_OCS, isOneClickShipment)
                    putBoolean(EXTRA_IS_LEASING, isLeasing)
                    putParcelable(EXTRA_TRADE_IN_PARAMS, tradeInParams)
                }
            }

            return fragment
        }
    }

    override fun initInjector() {
        activity?.run {
            DaggerNormalCheckoutComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
                    .inject(this@NormalCheckoutFragment)
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(NormalCheckoutViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.productInfoResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetProductInfo(it.data)
                is Fail -> onErrorGetProductInfo(it.throwable)
            }
        })
    }

    private fun onSuccessGetProductInfo(productInfoAndVariant: ProductInfoAndVariant) {
        if (action == TRADEIN_BUY) {
            tv_trade_in.gone()
        } else {
            if (tradeInParams != null && tradeInParams!!.isEligible == 1) {
                tv_trade_in.visible()
                tv_trade_in.tradeInReceiver.checkTradeIn(tradeInParams, false)
                if (tradeInParams!!.usedPrice > 0) {
                    tv_trade_in.setOnClickListener {
                        goToHargaFinal()
                        trackClickTradeIn()
                    }
                } else {
                    if (!viewModel.isUserSessionActive()) {
                        tv_trade_in.setOnClickListener {
                            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                                    REQUEST_CODE_LOGIN_THEN_TRADE_IN)
                        }
                    } else {
                        tv_trade_in.setOnClickListener(null)
                    }
                }
            }
        }
        originalProduct = productInfoAndVariant
        if (selectedVariantId.isNullOrEmpty()) {
            selectedVariantId = productInfoAndVariant.productVariant.defaultChildString
        }
        originalProduct?.run {
            onProductChange(this, selectedVariantId)
        }
        prescription_ticker.showWithCondition(productInfoAndVariant.productInfo.basic.needPrescription)
    }

    private fun goToHargaFinal() {
        val intent = FinalPriceActivity.getHargaFinalIntent(context)

        if (tradeInParams != null && selectedProductInfo != null) {
            tradeInParams!!.setPrice(selectedProductInfo!!.basic.price.toInt())
            tradeInParams!!.productId = selectedProductInfo!!.basic.id
            tradeInParams!!.productName = selectedProductInfo!!.basic.name
        }

        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeInParams)
        startActivityForResult(intent, FinalPriceActivity.FINAL_PRICE_REQUEST_CODE)
    }

    fun goToTradeInHome() {
        val intent = TradeInHomeActivity.getIntent(context)

        if (tradeInParams != null && selectedProductInfo != null) {
            tradeInParams!!.setPrice(selectedProductInfo!!.basic.price.toInt())
            tradeInParams!!.productId = selectedProductInfo!!.basic.id
            tradeInParams!!.productName = selectedProductInfo!!.basic.name
        }

        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeInParams)
        startActivityForResult(intent, TradeInHomeActivity.TRADEIN_HOME_REQUEST)
    }

    fun trackClickTradeIn() {
        val whichbutton: String
        if (action == ATC_ONLY)
            whichbutton = "tambah keranjang"
        else
            whichbutton = "beli"
        tradeInParams?.let {
            val label: String
            if (tradeInParams!!.usedPrice > 0)
                label = "after diagnostic"
            else
                label = "before diagnostic"

            sendGeneralEvent("clickPDP",
                    "product detail page",
                    "click trade in widget on variants page - "
                            + whichbutton,
                    label)

        }
    }

    private fun sendGeneralEvent(event: String, category: String, action: String, label: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(event,
                category,
                action,
                label)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FinalPriceActivity.FINAL_PRICE_REQUEST_CODE -> if (data != null)
                    onGotoTradeinShipment(data.getStringExtra(TradeInParams.PARAM_DEVICE_ID))
                TradeInHomeActivity.TRADEIN_HOME_REQUEST -> if (data != null)
                    onGotoTradeinShipment(data.getStringExtra(TradeInParams.PARAM_DEVICE_ID))
                REQUEST_CODE_LOGIN_THEN_BUY -> doCheckoutAction(ATC_AND_BUY)
                REQUEST_CODE_LOGIN_THEN_ATC -> doCheckoutAction(ATC_ONLY)
                REQUEST_CODE_LOGIN_THEN_TRADE_IN -> {
                    tv_trade_in.setOnClickListener(null)
                    doCheckoutAction(TRADEIN_BUY)
                }
            }
        }
    }

    override fun onVariantGuidelineClick(variantGuideline: String) {
        context?.run {
            startActivity(ImagePreviewActivity.getCallingIntent(context!!,
                    arrayListOf(variantGuideline),
                    null, 0))
        }
    }

    /**
     * selectedVariantId comes from the parameter of the fragment or might come from the user input
     * This function will get the product corresponding to the id.
     * If the id is not given, then the default product is return
     * If the id exists, it will search for the variant in the product and then return the mapping
     */
    fun getSelectedProductInfo(productInfoAndVariant: ProductInfoAndVariant, selectedVariantId: String?): ProductInfo {
        if (selectedVariantId.isNullOrEmpty()) {
            return productInfoAndVariant.productInfo
        } else {
            val selectedVariant = productInfoAndVariant.productVariant.getVariant(selectedVariantId)
            if (selectedVariant != null) {
                if (selectedVariant.isBuyable) {
                    return ModelMapper.convertVariantToModels(originalProduct?.productInfo!!, selectedVariant,
                            productInfoAndVariant.productVariant.variant)
                } else {
                    val child = getOtherSiblingProduct(originalProduct!!, selectedVariant.optionIds)
                    return if (child == null) {
                        productInfoAndVariant.productInfo
                    } else {
                        ModelMapper.convertVariantToModels(productInfoAndVariant.productInfo, child,
                                productInfoAndVariant.productVariant.variant)
                    }
                }
            } else {
                return productInfoAndVariant.productInfo
            }
        }
    }

    /**
     * When the optionId given is actually not n the children of the variant, we want to switch to another product
     * For example, option ID for [101,201,301] is not found as a children for variant,
     * So, another first product is searched: [101,201,**] that is buyable. The first item found is returned.
     * If still not find the product, previous level is searched: [101,***,***]
     * If still not find, root level is searched: [***,***,***]
     * If not find any, will return null
     */
    private fun getOtherSiblingProduct(productInfoAndVariant: ProductInfoAndVariant?, optionId: List<Int>): Child? {
        var selectedChild: Child? = null
        // we need to reselect other variant
        productInfoAndVariant?.run {
            var optionPartialSize = optionId.size - 1
            while (optionPartialSize > -1) {
                val partialOptionIdList = optionId.subList(0, optionPartialSize)
                for (childLoop: Child in productVariant.children) {
                    if (!childLoop.isBuyable) {
                        continue
                    }
                    if (optionPartialSize == 0) {
                        selectedChild = childLoop
                        break
                    }
                    if (childLoop.optionIds.subList(0, optionPartialSize) == partialOptionIdList) {
                        selectedChild = childLoop
                        break
                    }
                }
                if (selectedChild != null) {
                    break
                }
                optionPartialSize--
            }
        }
        return selectedChild
    }

    private fun renderActionButton(productInfo: ProductInfo) {
        if (GlobalConfig.isCustomerApp() && !viewModel.isShopOwner(productInfo.basic.shopID) &&
                productInfo.basic.isActive()) {
            button_buy_full.gone()
            rl_bottom_action_container.visible()
            if (action == ATC_AND_BUY) {
                button_cart.visible()
            } else {
                button_cart.gone()
            }
            button_buy_partial.text = if (action == ATC_ONLY) {
                getString(R.string.add_to_cart)
            } else if (action == TRADEIN_BUY) {
                getString(R.string.tukar_tambah)
            } else if (productInfo.isPreorderActive) {
                getString(R.string.label_button_preorder)
            } else {
                getString(R.string.label_button_buy)
            }
            if (hasError()) {
                button_buy_partial.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_disabled)
            } else {
                button_buy_partial.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_orange_enabled)
            }
            if(isLeasing){
                button_cart.gone()
                button_buy_partial.gone()
            }
        } else { // sellerapp or warehouse product or owner
            showFullButton(!productInfo.basic.isActive(), productInfo.isPreorderActive, false)
        }
    }

    private fun renderTotalPrice(productInfo: ProductInfo, selectedwarehouse: MultiOriginWarehouse?) {
        // if it has campaign, use campaign price
        var totalString = ""
        if (productInfo.campaign.activeAndHasId) {
            val discountedPrice = if (selectedwarehouse != null && selectedwarehouse.warehouseInfo.id.isNotBlank()) {
                selectedwarehouse.price.toFloat()
            } else productInfo.campaign.discountedPrice

            totalString = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    discountedPrice.toDouble() * quantity, true)
        } else {
            // if it has wholesale, use the price in the wholesale range
            if (productInfo.hasWholesale) {
                productInfo.wholesale!!.forEachIndexed { index, item ->
                    val hasNextItem = (index + 1) < (productInfo.wholesale!!.size)
                    val isLessThanNextMinQty = if (hasNextItem) {
                        quantity < productInfo.wholesale!![index + 1].minQty
                    } else true
                    if (quantity >= item.minQty && isLessThanNextMinQty) {
                        totalString = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                item.price.toDouble() * quantity, true)
                    }
                }
            }
        }
        tv_total.text = if (totalString.isEmpty()) {
            val price = if (selectedwarehouse != null && selectedwarehouse.warehouseInfo.id.isNotBlank())
                selectedwarehouse.price.toDouble()
            else productInfo.basic.price.toDouble()

            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    price * quantity, true)
        } else {
            totalString
        }
    }

    // show disabled secondary-buy button, and hide main buttons
    private fun showFullButton(hasStock: Boolean = false,
                               isPreorder: Boolean = false, enabled: Boolean = false) {
        rl_bottom_action_container.gone()
        button_buy_full.visible()
        button_buy_full.text = if (!hasStock) {
            getString(R.string.no_stock)
        } else if (isPreorder) {
            getString(R.string.label_button_preorder)
        } else {
            getString(R.string.label_button_buy)
        }
        button_buy_full.isClickable = enabled
        button_buy_full.isEnabled = enabled
    }

    private fun onErrorGetProductInfo(throwable: Throwable) {
        showToastError(throwable) {
            loadInitialData()
        }
    }

    private fun showToastError(throwable: Throwable?, onRetry: ((v: View) -> Unit)?) {
        val message = if (throwable is MessageErrorException && throwable.message?.isNotEmpty() == true) {
            throwable.message
        } else {
            ErrorHandler.getErrorMessage(context, throwable)
        }
        activity?.run {
            val snackbar = ToasterError.make(findViewById(android.R.id.content), message)
            if (onRetry != null) {
                snackbar.setAction(R.string.retry_label) { onRetry.invoke(it) }
            }
            snackbar.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val argument = arguments
        if (argument != null) {
            shopId = argument.getString(EXTRA_SHOP_ID)
            productId = argument.getString(EXTRA_PRODUCT_ID) ?: ""
            notes = argument.getString(EXTRA_NOTES)
            quantity = argument.getInt(EXTRA_QUANTITY)
            placeholderProductImage = argument.getString(EXTRA_PRODUCT_IMAGE)
            action = argument.getInt(EXTRA_ACTION, ATC_AND_BUY)
            trackerAttribution = argument.getString(TRACKER_ATTRIBUTION)
            trackerListName = argument.getString(TRACKER_LIST_NAME)
            shopType = argument.getString(EXTRA_SHOP_TYPE)
            shopName = argument.getString(EXTRA_SHOP_NAME)
            tradeInParams = argument.getParcelable(EXTRA_TRADE_IN_PARAMS)
            isOcs = argument.getBoolean(EXTRA_OCS)
            isLeasing = argument.getBoolean(EXTRA_IS_LEASING)
        }
        if (savedInstanceState == null) {
            if (argument != null) {
                selectedVariantId = argument.getString(EXTRA_SELECTED_VARIANT_ID)
            }
        } else {
            selectedVariantId = savedInstanceState.getString(EXTRA_SELECTED_VARIANT_ID)
            notes = savedInstanceState.getString(EXTRA_NOTES)
            quantity = savedInstanceState.getInt(EXTRA_QUANTITY)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_normal_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(CheckoutVariantItemDecorator())
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        super.onViewCreated(view, savedInstanceState)
        button_buy_partial.setOnClickListener {
            if (hasError()) {
                return@setOnClickListener
            }
            if (!viewModel.isUserSessionActive()) {
                context?.run {
                    //do tracking
                    if (action == ATC_ONLY) {
                        normalCheckoutTracking.eventClickAtcInVariantNotLogin(productId)
                        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                                REQUEST_CODE_LOGIN_THEN_ATC)
                    } else if (action == ATC_AND_BUY) {
                        normalCheckoutTracking.eventClickBuyInVariantNotLogin(productId)
                        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                                REQUEST_CODE_LOGIN_THEN_BUY)
                    } else {
                        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                                REQUEST_CODE_LOGIN_THEN_TRADE_IN)
                    }
                }
                return@setOnClickListener
            }
            doCheckoutAction(action)
        }
        btn_apply_credit.setOnClickListener {
            val urlApplyCreditWithProductId = String.format(
                    URL_APPLY_CREDIT,
                    productId
            )
            RouteManager.route(
                    context,
                    ApplinkConstInternalGlobal.WEBVIEW,
                    urlApplyCreditWithProductId
            )
        }
        tv_trade_in.setTrackListener { trackClickTradeIn() }
        button_cart.setOnClickListener {
            if (hasError()) {
                return@setOnClickListener
            }
            if (!viewModel.isUserSessionActive()) {
                //do tracking
                normalCheckoutTracking.eventClickAtcInVariantNotLogin(productId)
                //do login
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN_THEN_ATC)
            } else {
                addToCart()
            }
        }
    }

    override fun onDestroy() {
        viewModel.unsubscribe()
        super.onDestroy()
    }

    private fun doCheckoutAction(action: Int) {
        when (action) {
            ATC_ONLY -> addToCart()
            TRADEIN_BUY -> doTradeIn()
            else -> doBuyOrPreorder(isOcs)
        }
    }

    private fun doTradeIn() {
        if (tradeInParams != null) {
            val label: String
            if (tradeInParams!!.usedPrice > 0) {
                goToHargaFinal()
                label = "after diagnostic"
            } else {
                tv_trade_in.setTrackListener(null)
                tv_trade_in.performClick()
                label = "before diagnostic"
            }
            sendGeneralEvent("clickPDP",
                    "product detail page",
                    "click trade in button on variants page",
                    label)
        }
    }

    /**
     * called when backpressed
     */
    fun selectVariantAndFinish() {
        activity?.run {
            if (fragmentViewModel.isStateChanged == true) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    if (!selectedVariantId.isNullOrEmpty()) {
                        putExtra(EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
                        selectedProductInfo?.let {
                            val cacheManager =
                                    SaveInstanceCacheManager(this@run, true).apply {
                                        put(RESULT_PRODUCT_DATA, it)
                                        viewModel.selectedwarehouse?.let {
                                            put(RESULT_SELECTED_WAREHOUSE, it)
                                        }
                                    }
                            putExtra(RESULT_PRODUCT_DATA_CACHE_ID, cacheManager.id)
                        }
                    }
                    putExtra(EXTRA_QUANTITY, quantity)
                    putExtra(EXTRA_NOTES, notes)
                })
            }
            finish()
        }
    }

    /**
     * called when on Success Add to Cart
     */
    fun onFinishAddToCart(atcSuccessMessage: String? = null) {

        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
                if (!selectedVariantId.isNullOrEmpty()) {
                    selectedProductInfo
                } else {
                    originalProduct?.productInfo
                }
                        .let { it ->
                            val cacheManager =
                                    SaveInstanceCacheManager(this@run, true).apply {
                                        put(RESULT_PRODUCT_DATA, it)
                                    }
                            putExtra(RESULT_PRODUCT_DATA_CACHE_ID, cacheManager.id)
                        }
                putExtra(EXTRA_QUANTITY, quantity)
                putExtra(EXTRA_NOTES, notes)
                atcSuccessMessage?.let {
                    putExtra(RESULT_ATC_SUCCESS_MESSAGE, atcSuccessMessage)
                }
            })
            sendBranchAddToCardEvent()
            finish()
        }
    }

    private fun sendBranchAddToCardEvent(){
        if(selectedProductInfo != null) {
            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ADD_TO_CART, createLinkerData(selectedProductInfo,
                    (UserSession(activity)).userId)))
        }
    }

    private fun createLinkerData(productInfo: ProductInfo?, userId: String?): LinkerData{
        var linkerData = LinkerData()
        linkerData.id = productInfo?.basic?.id.toString()
        linkerData.price = productInfo?.basic?.price?.toInt().toString()
        linkerData.description = productInfo?.basic?.description
        linkerData.shopId = productInfo?.basic?.shopID.toString()
        linkerData.catLvl1 = productInfo?.category?.name
        linkerData.userId = userId ?: ""
        linkerData.currency = currencyLabel
        linkerData.quantity = tempQuantity.toString()
        return linkerData
    }

    private fun doBuyOrPreorder(isOcs: Boolean) {
        tempQuantity = quantity
        isTradeIn = 0
        addToCart(isOcs, onFinish = { message: String?, cartId: String? ->
            onFinishAddToCart()
            selectedProductInfo?.run {
                normalCheckoutTracking.eventClickBuyInVariant(
                        originalProduct,
                        selectedVariantId ?: "",
                        this, quantity,
                        shopId, shopType, shopName, cartId,
                        trackerAttribution, trackerListName,
                        viewModel.selectedwarehouse?.warehouseInfo?.isFulfillment ?: false)
            }
            activity?.run {
                if (isOcs) {
                    val intent = router.getCheckoutIntent(this, ShipmentFormRequest.BundleBuilder().build())
                    startActivity(intent)
                } else {
                    val cartUriString = ApplinkConstInternalMarketplace.CART
                    val intent = RouteManager.getIntent(this, cartUriString)
                    intent?.run {
                        putExtra(EXTRA_CART_ID, cartId)
                        startActivity(intent)
                    }
                }
            }
        }, onRetryWhenError = {
            doBuyOrPreorder(isOcs)
        })
    }

    private fun onGotoTradeinShipment(deviceid: String) {
        tempQuantity = 1
        isTradeIn = 1
        addToCart(true, onFinish = { message: String?, cartId: String? ->
            onFinishAddToCart()
            selectedProductInfo?.run {
                normalCheckoutTracking.eventClickBuyTradeIn(
                        originalProduct,
                        selectedVariantId ?: "",
                        this, quantity,
                        shopId, shopType, shopName, cartId,
                        trackerAttribution, trackerListName)
            }
            activity?.run {
                val intent = router.getCheckoutIntent(this, deviceid)
                startActivity(intent)
            }
        }, onRetryWhenError = {
            onGotoTradeinShipment(deviceid)
        })
    }

    private fun addToCart() {
        tempQuantity = quantity
        isTradeIn = 0
        addToCart(false, onFinish = { message: String?, cartId: String? ->
            onFinishAddToCart(message)
            selectedProductInfo?.run {
                normalCheckoutTracking.eventClickAddToCartInVariant(
                        originalProduct,
                        selectedVariantId ?: "",
                        this, quantity,
                        shopId, shopType, shopName, cartId,
                        trackerAttribution, trackerListName,
                        viewModel.selectedwarehouse?.warehouseInfo?.isFulfillment ?: false)
            }
        }, onRetryWhenError = {
            addToCart()
        })
    }

    private fun addToCart(oneClickShipment: Boolean, onFinish: ((message: String?, cartId: String?) -> Unit),
                          onRetryWhenError: (() -> Unit)) {
        val selectedVariant = selectedVariantId
        val selectedWarehouseId: Int = viewModel.selectedwarehouse?.warehouseInfo?.id?.toInt() ?: 0
        showLoadingDialog()
        //initiate checkout
        normalCheckoutTracking.eventAppsFlyerInitiateCheckout(productId,
                selectedProductInfo?.basic?.price.toString(),
                quantity,
                selectedProductInfo?.basic?.name ?: "",
                selectedProductInfo?.category?.name ?: "")

        if (oneClickShipment) {
            val addToCartOcsRequestParams = AddToCartOcsRequestParams()
            addToCartOcsRequestParams.productId = if (selectedVariant != null && selectedVariant.toInt() > 0) {
                selectedVariant.toLong()
            } else {
                productId.toLong()
            }
            addToCartOcsRequestParams.shopId = shopId?.toInt() ?: 0
            addToCartOcsRequestParams.quantity = tempQuantity
            addToCartOcsRequestParams.notes = notes ?: ""
            addToCartOcsRequestParams.warehouseId = selectedWarehouseId
            addToCartOcsRequestParams.trackerAttribution = trackerAttribution ?: ""
            addToCartOcsRequestParams.trackerListName = trackerListName ?: ""
            addToCartOcsRequestParams.isTradeIn = isTradeIn == 1

            viewModel.addToCartProduct(addToCartOcsRequestParams, ::onSuccessAtc, ::onErrorAtc, onFinish, onRetryWhenError)
        } else {
            val addToCartRequestParams = AddToCartRequestParams()
            addToCartRequestParams.productId = if (selectedVariant != null && selectedVariant.toInt() > 0) {
                selectedVariant.toLong()
            } else {
                productId.toLong()
            }
            addToCartRequestParams.shopId = shopId?.toInt() ?: 0
            addToCartRequestParams.quantity = tempQuantity
            addToCartRequestParams.notes = notes ?: ""
            addToCartRequestParams.attribution = trackerAttribution ?: ""
            addToCartRequestParams.listTracker = trackerListName ?: ""
            addToCartRequestParams.warehouseId = selectedWarehouseId

            viewModel.addToCartProduct(addToCartRequestParams, ::onSuccessAtc, ::onErrorAtc, onFinish, onRetryWhenError)
        }
    }

    private fun onSuccessAtc(addToCartDataModel: AddToCartDataModel?, onFinish: (message: String?, cartId: String?) -> Unit) {
        hideLoadingDialog()
        addToCartDataModel?.run {
            if (addToCartDataModel.status == "OK" && addToCartDataModel.data.success == 1) {
                //success checkout
                normalCheckoutTracking.eventAppsFlyerAddToCart(productId,
                        selectedProductInfo?.basic?.price.toString(),
                        quantity,
                        selectedProductInfo?.basic?.name ?: "",
                        selectedProductInfo?.category?.name ?: "")
                onFinish(addToCartDataModel.data.message[0], addToCartDataModel.data.cartId.toString())
            } else {
                activity?.findViewById<View>(android.R.id.content)?.showErrorToaster(
                        addToCartDataModel.errorMessage[0])
            }
        }

    }

    private fun onErrorAtc(e: Throwable?, onRetryWhenError: (() -> Unit)) {
        hideLoadingDialog()
        showToastError(e) {
            onRetryWhenError()
        }
    }

    fun showLoadingDialog(onCancelClicked: (() -> Unit)? = null) {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = activity?.createDefaultProgressDialog(
                    getString(R.string.title_loading),
                    cancelable = true,
                    onCancelClicked = {
                        onCancelClicked?.invoke()
                    })
        }
        loadingProgressDialog?.run {
            if (!isShowing) {
                show()
            }
        }
    }

    fun hideLoadingDialog() {
        loadingProgressDialog?.run {
            if (isShowing)
                dismiss()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.run {
            if (applicationContext is NormalCheckoutRouter) {
                router = applicationContext as NormalCheckoutRouter
            } else {
                activity?.finish()
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypeFactory> {
        adapter = CheckoutVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun getAdapterTypeFactory(): CheckoutVariantAdapterTypeFactory {
        return NormalCheckoutAdapterTypeFactory(this)
    }

    override fun loadData(page: Int) {
        viewModel.getProductInfo(ProductParams(productId, null, null), resources)
    }

    override fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel) {
        val optionList = mutableListOf<Int>()
        var variantSize = 0
        for (viewModel: Visitable<*> in fragmentViewModel.viewModels) {
            if (viewModel is TypeVariantViewModel) {
                viewModel.getSelectedOption()?.let {
                    optionList.add(it)
                }
                if (viewModel.getSelectedOption() == selectedOptionViewModel.optionId) {
                    if (viewModel.isColorIdentifier) {
                        normalCheckoutTracking.eventSelectColorVariant(selectedOptionViewModel.variantName)
                    } else if (viewModel.isSizeIdentifier) {
                        normalCheckoutTracking.eventSelectSizeVariant(selectedOptionViewModel.variantName)
                    }
                }
                variantSize++
            }
        }
        //selection option might partial selected
        if (optionList.isNotEmpty()) {
            originalProduct?.run {
                if (productVariant.hasChildren) {
                    var selectedChild: Child? = null
                    // find exact size of option, we only care for the full selection
                    if (optionList.size == variantSize) {
                        for (childModel: Child in productVariant.children) {
                            if (childModel.optionIds == optionList) {
                                selectedChild = childModel
                                break
                            }
                        }
                    }
                    if (selectedChild == null) {
                        val child = getOtherSiblingProduct(this, optionList)
                        if (child == null) {
                            onProductChange(this, productInfo.basic.id.toString())
                        } else {
                            onProductChange(this, child.productId.toString())
                        }
                    } else {
                        onProductChange(this, selectedChild.productId.toString())
                    }
                }
            }

            fragmentViewModel.isStateChanged = true
        }
    }

    private fun onProductChange(originalProduct: ProductInfoAndVariant, inputSelectedVariantId: String?) {
        inputSelectedVariantId?.let {
            if (viewModel.warehouses.isNotEmpty()) {
                viewModel.selectedwarehouse = viewModel.warehouses[it]
            }
        }
        selectedVariantId = inputSelectedVariantId
        selectedProductInfo = getSelectedProductInfo(originalProduct, selectedVariantId)
        selectedProductInfo?.let {
            val viewModels = ModelMapper.convertVariantToModels(it, viewModel.selectedwarehouse,
                    originalProduct.productVariant, notes, quantity)
            fragmentViewModel.viewModels = viewModels
            quantity = fragmentViewModel.getQuantityViewModel()?.orderQuantity
                    ?: 0
            adapter.clearAllElements()
            adapter.addDataViewModel(viewModels)
            adapter.notifyDataSetChanged()
            renderActionButton(it)
            renderTotalPrice(it, viewModel.selectedwarehouse)
        }
    }

    override fun onChangeQuantity(quantityViewModel: QuantityViewModel) {
        quantity = quantityViewModel.orderQuantity
        selectedProductInfo?.let {
            renderActionButton(it)
            renderTotalPrice(it, viewModel.selectedwarehouse)
        }
        fragmentViewModel.isStateChanged = true
    }

    override fun onChangeNote(noteViewModel: NoteViewModel) {
        if (fragmentViewModel.isStateChanged == false && noteViewModel.note.isNotEmpty()) {
            fragmentViewModel.isStateChanged = true
        }
        notes = noteViewModel.note
    }

    private fun hasError(): Boolean {
        var hasError = false
        when {
            fragmentViewModel.getQuantityViewModel()?.isStateError == true -> hasError = true
        }
        return hasError
    }

    override fun onGetCompositeSubscriber() = null

    override fun onBindProductUpdateQuantityViewModel(productViewModel: ProductViewModel, stockWording: String) {}

    override fun onBindVariantGetProductViewModel(): ProductViewModel? {
        return fragmentViewModel.getProductViewModel()
    }

    override fun getScreenName(): String? = null

    override fun showToasterError(message: String?) {
        ToasterError.make(view, message
                ?: activity?.getString(R.string.default_request_error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun navigateCheckoutToPayment(paymentPassData: PaymentPassData) {
        if (activity != null) startActivityForResult(
                TopPayActivity.createInstance(activity, paymentPassData),
                TopPayActivity.REQUEST_CODE)
        activity?.finish()
    }

    override fun navigateCheckoutToThankYouPage(appLink: String) {
        if (activity != null) startActivity(RouteManager.getIntent(activity, appLink))
        activity?.finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
        outState.putInt(EXTRA_QUANTITY, quantity)
        outState.putString(EXTRA_NOTES, notes)
    }

    override fun showData(viewModels: ArrayList<Visitable<*>>) { /* no op we use onSuccess */
    }

    override fun showBottomSheetError(title: String, message: String, action: String, enableRetry: Boolean) {}
    override fun showErrorNotAvailable(message: String) {}
    override fun updateFragmentViewModel(atcResponseModel: AtcResponseModel) {}
    override fun onNeedToNotifySingleItem(position: Int) {}
    override fun onItemClicked(t: Visitable<*>?) {}
    override fun onClickEditProfile() {}
    override fun onClickEditDuration() {}
    override fun onClickEditCourier() {}
    override fun onNeedToRemoveSingleItem(position: Int) {}
    override fun onNeedToNotifyAllItem() {}
    override fun onClickInsuranceInfo(insuranceInfo: String) {}
    override fun onSummaryChanged(summaryViewModel: SummaryViewModel?) {}
    override fun onInsuranceCheckChanged(insuranceViewModel: InsuranceViewModel) {}
    override fun onNeedToValidateButtonBuyVisibility() {}
    override fun onNeedToRecalculateRatesAfterChangeTemplate() {}
    override fun onNeedToUpdateOnboardingStatus() {}
    override fun onBindVariantUpdateProductViewModel() {}

}