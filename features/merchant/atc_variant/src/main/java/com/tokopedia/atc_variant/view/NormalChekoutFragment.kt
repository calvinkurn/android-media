package com.tokopedia.atc_variant.view

import android.app.Activity
import android.app.ProgressDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.SimpleItemAnimator
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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_variant.R
import com.tokopedia.atc_variant.data.request.*
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.URL_APPLY_LEASING
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.constant.NormalCheckoutConstant.Companion.RESULT_PRODUCT_DATA
import com.tokopedia.purchase_platform.common.constant.NormalCheckoutConstant.Companion.RESULT_PRODUCT_DATA_CACHE_ID
import com.tokopedia.purchase_platform.common.constant.NormalCheckoutConstant.Companion.RESULT_SELECTED_WAREHOUSE
import com.tokopedia.atc_variant.di.DaggerNormalCheckoutComponent
import com.tokopedia.atc_variant.model.Fail
import com.tokopedia.atc_variant.model.InsuranceRecommendationContainer
import com.tokopedia.atc_variant.model.ProductInfoAndVariant
import com.tokopedia.atc_variant.model.ProductInfoAndVariantContainer
import com.tokopedia.atc_variant.view.adapter.AddToCartVariantAdapter
import com.tokopedia.atc_variant.view.adapter.AddToCartVariantAdapterTypeFactory
import com.tokopedia.atc_variant.view.presenter.NormalCheckoutViewModel
import com.tokopedia.atc_variant.view.viewmodel.*
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.EXTRA_IS_ONE_CLICK_SHIPMENT
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceRecommendationGqlResponse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_INSURANCE_RECOMMENDATION
import com.tokopedia.track.TrackApp
import com.tokopedia.tradein.model.TradeInParams
import com.tokopedia.tradein.view.viewcontrollers.FinalPriceActivity
import com.tokopedia.tradein.view.viewcontrollers.TradeInHomeActivity
import com.tokopedia.transaction.common.sharedata.RESULT_CODE_ERROR_TICKET
import com.tokopedia.transaction.common.sharedata.RESULT_TICKET_DATA
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_normal_checkout.*
import javax.inject.Inject

class NormalChekoutFragment : BaseListFragment<Visitable<*>, AddToCartVariantAdapterTypeFactory>(),
        NormalCheckoutContract.View, AddToCartVariantActionListener {

    private var isInsuranceSelected: Boolean = false

    private var selectedInsuranceProduct = InsuranceRecommendationViewModel()

    private var insuranceEnabled: Boolean = false
    private var productPrice: Float? = 0f
    private var insuranceRecommendationRequest = InsuranceRecommendationRequest()
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
    private lateinit var adapter: AddToCartVariantAdapter

    private var insuranceViewModel = InsuranceRecommendationViewModel()
    var shopId: String = ""
    var categoryId: String? = null
    lateinit var productId: String
    lateinit var productTitle: String
    var categoryName: String = ""
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


    private var condition: String = ""
    private val page = "pdp"
    private val clientVersion = Build.VERSION.SDK_INT.toString()

    companion object {
        const val EXTRA_IS_LEASING = "is_leasing"
        const val EXTRA_CART_ID = "cart_id"

        const val REQUEST_CODE_LOGIN = 561
        const val REQUEST_CODE_LOGIN_THEN_ATC = 562
        const val REQUEST_CODE_LOGIN_THEN_BUY = 563
        const val REQUEST_CODE_LOGIN_THEN_TRADE_IN = 564
        const val REQUEST_CODE_LOGIN_THEN_APPLY_CREDIT = 565

        fun createInstance(shopId: String?, categoryId: String?, categoryName: String?, productId: String?,
                           productTitle: String?, productPrice: Float? = 0f, condition: String?, notes: String? = "", quantity: Int? = 0,
                           selectedVariantId: String? = null,
                           @ProductAction action: Int = ATC_AND_BUY,
                           placeholderProductImage: String?,
                           trackerAttribution: String? = "",
                           trackerListName: String? = "",
                           shopType: String? = "",
                           shopName: String? = "",
                           isOneClickShipment: Boolean,
                           isNeedRefresh: Boolean,
                           isLeasing: Boolean,
                           reference: String?,
                           customEventLabel: String?,
                           customEventAction: String?,
                           tradeInParams: TradeInParams?): NormalChekoutFragment {
            val fragment = NormalChekoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ApplinkConst.Transaction.EXTRA_SHOP_ID, shopId)
                    putString(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, productId)
                    putString(ApplinkConst.Transaction.EXTRA_NOTES, notes)
                    putInt(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity ?: 0)
                    putInt(ApplinkConst.Transaction.EXTRA_ACTION, action)
                    putString(ApplinkConst.Transaction.EXTRA_PRODUCT_IMAGE, placeholderProductImage)
                    putString(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, selectedVariantId
                            ?: "")
                    putString(ApplinkConst.Transaction.TRACKER_ATTRIBUTION, trackerAttribution
                            ?: "")
                    putString(ApplinkConst.Transaction.TRACKER_LIST_NAME, trackerListName ?: "")
                    putString(ApplinkConst.Transaction.EXTRA_SHOP_TYPE, shopType ?: "")
                    putString(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopName ?: "")
                    putBoolean(ApplinkConst.Transaction.EXTRA_OCS, isOneClickShipment)
                    putBoolean(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, isNeedRefresh)
                    putParcelable(ApplinkConst.Transaction.EXTRA_TRADE_IN_PARAMS, tradeInParams)
                    putString(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, categoryId)
                    putString(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, categoryName)
                    putString(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, productTitle)
                    putFloat(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, productPrice!!)
                    putString(ApplinkConst.Transaction.EXTRA_PRODUCT_CONDITION, condition)
                    putBoolean(EXTRA_IS_LEASING, isLeasing)
                    putString(ApplinkConst.Transaction.EXTRA_REFERENCE, reference)
                    putString(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_LABEL, customEventLabel)
                    putString(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, customEventAction)
                }
            }
            return fragment
        }
    }

    override fun initInjector() {
        activity?.run {
            DaggerNormalCheckoutComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
                    .inject(this@NormalChekoutFragment)
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(NormalCheckoutViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.productInfoResp.observe(this, Observer {
            when (it) {
                is ProductInfoAndVariantContainer -> onSuccessGetProductInfo(it.productInfoAndVariant)
                is InsuranceRecommendationContainer -> onSuccessInsuranceRecommendation(it.insuranceRecommendation)
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

        if (viewModel.isUserSessionActive() && insuranceEnabled) {
            generateInsuranceRequest()
            viewModel.getInsuranceProductRecommendation(insuranceRecommendationRequest)
        }
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

        intent.putExtra(TradeInParams.PARAM_PERMISSION_GIVEN, true)
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
                REQUEST_CODE_LOGIN_THEN_APPLY_CREDIT -> {
                    goToApplyLeasing()
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
            if (isLeasing) {
                button_cart.gone()
                button_buy_partial.gone()
                btn_apply_leasing.visible()
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

            categoryId = argument.getString(ApplinkConst.Transaction.EXTRA_CATEGORY_ID)
            categoryName = argument.getString(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME) ?: ""
            productTitle = argument.getString(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE) ?: ""
            productPrice = argument.getFloat(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE) ?: 0f
            condition = argument.getString(ApplinkConst.Transaction.EXTRA_PRODUCT_CONDITION) ?: ""


            shopId = argument.getString(ApplinkConst.Transaction.EXTRA_SHOP_ID) ?: ""
            productId = argument.getString(ApplinkConst.Transaction.EXTRA_PRODUCT_ID) ?: ""
            notes = argument.getString(ApplinkConst.Transaction.EXTRA_NOTES)
            quantity = argument.getInt(ApplinkConst.Transaction.EXTRA_QUANTITY)
            placeholderProductImage = argument.getString(ApplinkConst.Transaction.EXTRA_PRODUCT_IMAGE)
            action = argument.getInt(ApplinkConst.Transaction.EXTRA_ACTION, ATC_AND_BUY)
            trackerAttribution = argument.getString(ApplinkConst.Transaction.TRACKER_ATTRIBUTION)
            trackerListName = argument.getString(ApplinkConst.Transaction.TRACKER_LIST_NAME)
            shopType = argument.getString(ApplinkConst.Transaction.EXTRA_SHOP_TYPE)
            shopName = argument.getString(ApplinkConst.Transaction.EXTRA_SHOP_NAME)
            tradeInParams = argument.getParcelable(ApplinkConst.Transaction.EXTRA_TRADE_IN_PARAMS)
            isOcs = argument.getBoolean(ApplinkConst.Transaction.EXTRA_OCS)
            isLeasing = argument.getBoolean(EXTRA_IS_LEASING)
        }
        if (savedInstanceState == null) {
            if (argument != null) {
                selectedVariantId = argument.getString(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID)
            }
        } else {
            selectedVariantId = savedInstanceState.getString(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID)
            notes = savedInstanceState.getString(ApplinkConst.Transaction.EXTRA_NOTES)
            quantity = savedInstanceState.getInt(ApplinkConst.Transaction.EXTRA_QUANTITY)
        }

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        insuranceEnabled = remoteConfig.getBoolean(APP_ENABLE_INSURANCE_RECOMMENDATION, false)

        super.onCreate(savedInstanceState)
        viewModel.parseDataFrom(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_normal_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(NormalCheckoutItemDecorator())
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
        btn_apply_leasing.setOnClickListener {
            if (!viewModel.isUserSessionActive()) {
                context?.run {
                    //do tracking
                    if (action == APPLY_CREDIT) {
                        startActivityForResult(
                                RouteManager.getIntent(context, ApplinkConst.LOGIN),
                                REQUEST_CODE_LOGIN_THEN_APPLY_CREDIT
                        )
                    }
                }
                return@setOnClickListener
            }
            goToApplyLeasing()
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

    private fun goToApplyLeasing() {
        val selectedProductId =  if (selectedVariantId.toIntOrZero() > 0) {
            selectedVariantId
        } else {
            productId
        }
        val urlApplyLeasingWithProductId = String.format(
                URL_APPLY_LEASING,
                selectedProductId
        )
        val webViewUrl = String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW,
                urlApplyLeasingWithProductId
        )
        RouteManager.route(context, webViewUrl)
    }

    override fun onDestroy() {
        viewModel.unsubscribe()
        super.onDestroy()
    }

    private fun generateInsuranceRequest() {


        insuranceRecommendationRequest = InsuranceRecommendationRequest()
        insuranceRecommendationRequest.page = page
        insuranceRecommendationRequest.clientVersion = clientVersion

        val insuranceShopsDataArrayList = ArrayList<InsuranceShopsData>()

        val insuranceShopsData = InsuranceShopsData()
        insuranceShopsData.shopId = java.lang.Long.parseLong(shopId)

        val insuranceShopsArrayList = java.util.ArrayList<InsuranceShops>()
        val insuranceShops: InsuranceShops

        val insurnaceShopCategory = InsuranceShopCategory()

        insurnaceShopCategory.categoryId = categoryId.toLongOrZero()
        insurnaceShopCategory.categoryName = categoryName

        insuranceShops = InsuranceShops()
        insuranceShops.productId = productId.toLong()
        insuranceShops.productQuantity = quantity
        insuranceShops.categoryId = categoryId.toLongOrZero()
        insuranceShops.productTitle = productTitle
        insuranceShops.productPrice = productPrice?.toLong()!!
        insuranceShops.condition = condition!!.toLowerCase()

        insuranceShops.shopCategory = insurnaceShopCategory
        insuranceShopsArrayList.add(insuranceShops)


        insuranceShopsData.shopItems = insuranceShopsArrayList
        insuranceShopsDataArrayList.add(insuranceShopsData)

        insuranceRecommendationRequest.shopsArrayList = insuranceShopsDataArrayList


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
                        putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
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
                    putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
                    putExtra(ApplinkConst.Transaction.EXTRA_NOTES, notes)
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
                putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
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
                putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
                putExtra(ApplinkConst.Transaction.EXTRA_NOTES, notes)
                atcSuccessMessage?.let {
                    putExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE, atcSuccessMessage)
                }
            })
            sendBranchAddToCardEvent()
            finish()
        }
    }

    private fun onFinishError(errorModel: AddToCartDataModel) {
        activity?.run {
            setResult(RESULT_CODE_ERROR_TICKET, Intent().apply {
                putExtra(RESULT_TICKET_DATA, errorModel)
            })
            finish()
        }
    }

    private fun sendBranchAddToCardEvent() {
        if (selectedProductInfo != null) {
            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ADD_TO_CART, createLinkerData(selectedProductInfo,
                    (UserSession(activity)).userId)))
        }
    }

    private fun createLinkerData(productInfo: ProductInfo?, userId: String?): LinkerData {
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
                        viewModel.selectedwarehouse?.warehouseInfo?.isFulfillment ?: false,
                        freeOngkir.isFreeOngkirActive)
            }
            activity?.run {
                if (isOcs) {
                    val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.CHECKOUT)
                    intent.putExtra(EXTRA_IS_ONE_CLICK_SHIPMENT, true)
                    intent.putExtras(ShipmentFormRequest.BundleBuilder().build().bundle)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    val cartUriString = ApplinkConst.CART
                    val intent = RouteManager.getIntent(this, cartUriString)
                    intent?.run {
                        putExtra(ApplinkConst.Transaction.EXTRA_CART_ID, cartId)
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
                val shipmentFormRequest = ShipmentFormRequest.BundleBuilder()
                        .deviceId(deviceid)
                        .build()
                val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.CHECKOUT)
                intent.putExtra(EXTRA_IS_ONE_CLICK_SHIPMENT, true)
                intent.putExtras(shipmentFormRequest.bundle)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(intent)
            }
        }, onRetryWhenError = {
            onGotoTradeinShipment(deviceid)
        })
    }

    private fun addToCart() {

        if (isErrorInInsurance() ||
                (isInsuranceSelected &&
                        selectedInsuranceProduct.cartShopsList.isNullOrEmpty())) {

            return
        }

        addToInsuranceCart(onFinish = { message: String?, cartId: String? ->

            hideLoadingDialog()
            normalCheckoutTracking.eventAppsFlyerAddToCart(productId,
                    selectedProductInfo?.basic?.price.toString(),
                    quantity,
                    selectedProductInfo?.basic?.name ?: "",
                    selectedProductInfo?.category?.name ?: "")

            selectedProductInfo?.run {
                normalCheckoutTracking.eventClickAddToCartInVariant(
                        originalProduct,
                        selectedVariantId ?: "",
                        this, quantity,
                        shopId, shopType, shopName, cartId,
                        trackerAttribution, trackerListName,
                        viewModel.selectedwarehouse?.warehouseInfo?.isFulfillment ?: false,
                        getPageReference(),
                        freeOngkir.isFreeOngkirActive,
                        getCustomEventLabel(),
                        getCustomEventAction()
                )
            }
            onFinishAddToCart(message)
        }, onRetryWhenError = { message: String ->
            hideLoadingDialog()
            var toastMessage = if (message.isNullOrBlank()) {
                getString(R.string.default_request_error_unknown_short)
            } else {
                message
            }
            activity?.findViewById<View>(android.R.id.content)?.showErrorToaster(toastMessage)
        }, onGqlError = { e: Throwable? ->
            hideLoadingDialog()
            showToastError(e) {
                addToCart()
            }
        })

    }

    private fun isErrorInInsurance(): Boolean {

        for (insuranceCartShopsViewModel in selectedInsuranceProduct.cartShopsList) {
            for (shopItem in insuranceCartShopsViewModel.shopItemsList) {
                for (insuranceCartDigitalProduct in shopItem.digitalProductList) {
                    for (applicationDetail in insuranceCartDigitalProduct.applicationDetails) {
                        if (applicationDetail.isError) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    private fun addToInsuranceCart(onFinish: ((message: String?, cartId: String?) -> Unit),
                                   onRetryWhenError: ((message: String) -> Unit), onGqlError: ((e: Throwable?) -> Unit)) {
        val selectedVariant = selectedVariantId
        val selectedWarehouseId = viewModel.selectedwarehouse?.warehouseInfo?.id?.toLong() ?: 0
        showLoadingDialog()
        //initiate add to insurance cart

        normalCheckoutTracking.eventAppsFlyerInitiateCheckout(productId,
                selectedProductInfo?.basic?.price.toString(),
                quantity,
                selectedProductInfo?.basic?.name ?: "",
                selectedProductInfo?.category?.name ?: "")

        val addMarketPlaceToCartRequest = AddMarketPlaceToCartRequest()
        addMarketPlaceToCartRequest.notes = notes ?: ""
        addMarketPlaceToCartRequest.productId = if (selectedVariant != null && selectedVariant.toInt() > 0) {
            selectedVariant.toLong()
        } else {
            productId.toLong()
        }
        addMarketPlaceToCartRequest.quantity = quantity
        addMarketPlaceToCartRequest.shoppId = shopId?.toLong() ?: 0
        addMarketPlaceToCartRequest.warehouseID = selectedWarehouseId

        val addInsuranceProductToCartRequest = AddInsuranceProductToCartRequest()


        if (isInsuranceSelected) {
            addInsuranceProductToCartRequest.page = page
            addInsuranceProductToCartRequest.clientVersion = clientVersion
            addInsuranceProductToCartRequest.clientType = "android"
            addInsuranceProductToCartRequest.clientLanguage = "bhasa"

            val addInsuranceProductDataList = ArrayList<AddInsuranceProductData>()

            for (insuranceCartShopsViewModel in selectedInsuranceProduct.cartShopsList) {
                val model = AddInsuranceProductData()
                model.shopId = insuranceCartShopsViewModel.shopId
                val addInsuranceProductItemList = ArrayList<AddInsuranceProductItems>()

                for (shopItem in insuranceCartShopsViewModel.shopItemsList) {
                    val addShopItem = AddInsuranceProductItems()

                    addShopItem.productId = shopItem.productId
                    addShopItem.productQuantity = 1

                    val digitalProductList = ArrayList<AddInsuranceProduct>()

                    for (insuranceCartDigitalProduct in shopItem.digitalProductList) {

                        val digitalProduct = AddInsuranceProduct()
                        digitalProduct.typeId = insuranceCartDigitalProduct.typeId
                        digitalProduct.digitalProductId = insuranceCartDigitalProduct.digitalProductId
                        val applicationDetailList = ArrayList<AddInsuranceProductApplicationDetails>()

                        for (applicationDetail in insuranceCartDigitalProduct.applicationDetails) {
                            val addApplicationDetail = AddInsuranceProductApplicationDetails()
                            addApplicationDetail.id = applicationDetail.id
                            addApplicationDetail.value = applicationDetail.value
                            applicationDetailList.add(addApplicationDetail)
                        }

                        digitalProduct.applicationDetails = applicationDetailList
                        digitalProductList.add(digitalProduct)

                    }

                    addShopItem.digitalProductList = digitalProductList
                    addInsuranceProductItemList.add(addShopItem)
                }

                model.shopItems = addInsuranceProductItemList
                addInsuranceProductDataList.add(model)
            }

            addInsuranceProductToCartRequest.addInsuranceData = addInsuranceProductDataList
        }


        viewModel.addInsuranceProductToCart(addInsuranceProductToCartRequest,
                addMarketPlaceToCartRequest, onFinish, onRetryWhenError, onGqlError)

    }

    private fun getPageReference(): String {
        return arguments?.getString(ApplinkConst.Transaction.EXTRA_REFERENCE, "") ?: ""
    }

    private fun getCustomEventLabel(): String {
        return arguments?.getString(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_LABEL, "") ?: ""
    }


    private fun getCustomEventAction(): String {
        return arguments?.getString(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, "") ?: ""
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
            } else if (addToCartDataModel.errorReporter.eligible) {
                onFinishError(addToCartDataModel)
            } else {
                activity?.findViewById<View>(android.R.id.content)?.showErrorToaster(
                        addToCartDataModel.errorMessage[0])
                normalCheckoutTracking.eventViewErrorWhenAddToCart(addToCartDataModel.errorMessage[0])
            }
        }

    }

    private fun onErrorAtc(e: Throwable?, onRetryWhenError: (() -> Unit)) {
        hideLoadingDialog()
        normalCheckoutTracking.eventViewErrorWhenAddToCart(ErrorHandler.getErrorMessage(context, e))
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

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AddToCartVariantAdapterTypeFactory> {
        adapter = AddToCartVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun getAdapterTypeFactory(): AddToCartVariantAdapterTypeFactory {
        return AddToCartVariantAdapterTypeFactory(this)
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
                        normalCheckoutTracking.eventSelectColorVariant(selectedOptionViewModel.variantName, productId)
                    } else if (viewModel.isSizeIdentifier) {
                        normalCheckoutTracking.eventSelectSizeVariant(selectedOptionViewModel.variantName, productId)
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
            if (insuranceViewModel.cartShopsList.isNotEmpty()) {
                adapter.addSingleDataViewModel(insuranceViewModel)
            }
            adapter.notifyDataSetChanged()
            renderActionButton(it)
            renderTotalPrice(it, viewModel.selectedwarehouse)
        }
    }

    private fun onSuccessInsuranceRecommendation(insuranceRecommendation: InsuranceRecommendationGqlResponse) {
        selectedProductInfo?.let {
            insuranceViewModel = ModelMapper.convertToInsuranceRecommendationViewModel(insuranceRecommendation)
            fragmentViewModel.viewModels.add(insuranceViewModel)
            adapter.addSingleDataViewModel(insuranceViewModel)
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

    override fun navigateCheckoutToThankYouPage(appLink: String) {
        if (activity != null) startActivity(RouteManager.getIntent(activity, appLink))
        activity?.finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
        outState.putInt(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
        outState.putString(ApplinkConst.Transaction.EXTRA_NOTES, notes)
    }

    override fun onInsuranceSelectedStateChanged(element: InsuranceRecommendationViewModel?, isSelected: Boolean) {
        this.selectedInsuranceProduct = element ?: InsuranceRecommendationViewModel()
        isInsuranceSelected = isSelected
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No op
    }

}