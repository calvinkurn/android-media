package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.ADDON
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant.QUERY_PARAM_WAREHOUSE_ID
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.checkoutpayment.data.PaymentRequest
import com.tokopedia.checkoutpayment.list.view.PaymentListingActivity
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.payment.utils.LINK_ACCOUNT_BACK_BUTTON_APPLINK
import com.tokopedia.common.payment.utils.LINK_ACCOUNT_SOURCE_PAYMENT
import com.tokopedia.common.payment.utils.LinkStatusMatcher
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.Companion.EXTRA_IS_FULL_FLOW
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.Companion.EXTRA_IS_LOGISTIC_LABEL
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddEditAddressSource
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.address.AddressListBottomSheet
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.OCC_OVO_ACTIVATION_URL
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.utils.animateGone
import com.tokopedia.oneclickcheckout.common.view.utils.animateShow
import com.tokopedia.oneclickcheckout.databinding.FragmentOrderSummaryPageBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.di.OrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.oneclickcheckout.order.view.card.OrderInsuranceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPreferenceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPromoCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderShopCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderTotalPaymentCard
import com.tokopedia.oneclickcheckout.order.view.mapper.AddOnMapper
import com.tokopedia.oneclickcheckout.order.view.model.AddressState
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccResult
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccOnboarding
import com.tokopedia.oneclickcheckout.order.view.model.OccOnboarding.Companion.COACHMARK_TYPE_NEW_BUYER_REMOVE_PROFILE
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.OccPromptButton
import com.tokopedia.oneclickcheckout.order.view.model.OccUIMessage
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardAdditionalData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilTerms
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentWalletAdditionalData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPreference
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.payment.activation.PaymentActivationWebViewBottomSheet
import com.tokopedia.oneclickcheckout.payment.creditcard.CreditCardPickerActivity
import com.tokopedia.oneclickcheckout.payment.creditcard.CreditCardPickerFragment
import com.tokopedia.oneclickcheckout.payment.creditcard.installment.CreditCardInstallmentDetailBottomSheet
import com.tokopedia.oneclickcheckout.payment.installment.GoCicilInstallmentDetailBottomSheet
import com.tokopedia.oneclickcheckout.payment.topup.view.PaymentTopUpWebViewActivity
import com.tokopedia.promousage.analytics.PromoUsageEntryPointAnalytics
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.bottomsheet.PromoUsageBottomSheet
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.constant.ARGS_BBO_PROMO_CODES
import com.tokopedia.purchase_platform.common.constant.ARGS_CLEAR_PROMO_RESULT
import com.tokopedia.purchase_platform.common.constant.ARGS_FINISH_ERROR
import com.tokopedia.purchase_platform.common.constant.ARGS_LAST_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_PAGE_SOURCE
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_ERROR
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_DATA_RESULT
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_ADDON_PRODUCT
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_CART_ID
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_CATEGORY_ID
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_DESELECTED_ADDON_IDS
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_DISCOUNTED_PRICE
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_IS_TOKOCABANG
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_PAGE_ATC_SOURCE
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_PRICE
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_QUANTITY
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_SELECTED_ADDON_IDS
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.QUERY_PARAM_SHOP_ID
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.SOURCE_ONE_CLICK_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.PAGE_OCC
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.bottomsheet.InsuranceBottomSheet
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoExternalAutoApply
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleActionListener
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleBottomSheet
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.revamp.CartCheckoutRevampRollenceManager
import com.tokopedia.purchase_platform.common.revamp.PromoEntryPointImprovementRollenceManager
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import com.tokopedia.purchase_platform.common.utils.removeSingleDecimalSuffix
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import dagger.Lazy
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.purchase_platform.common.R as purchase_platformcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class OrderSummaryPageFragment : BaseDaggerFragment(), PromoUsageBottomSheet.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    @Inject
    lateinit var ePharmacyAnalytics: EPharmacyAnalytics

    @Inject
    lateinit var userSession: Lazy<UserSessionInterface>

    @Inject
    @field:Named(OCC_OVO_ACTIVATION_URL)
    lateinit var ovoActivationUrl: Lazy<String>

    @Inject
    lateinit var getAddressCornerUseCase: Lazy<GetAddressCornerUseCase>

    @Inject
    lateinit var getTargetedTickerUseCase: Lazy<GetTargetedTickerUseCase>

    @Inject
    lateinit var promoEntryPointAnalytics: PromoUsageEntryPointAnalytics

    @Inject
    lateinit var gson: Lazy<Gson>

    private val viewModel: OrderSummaryPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OrderSummaryPageViewModel::class.java]
    }

    private var orderProfile: OrderProfile? = null

    private lateinit var adapter: OrderSummaryPageAdapter

    private var progressDialog: AlertDialog? = null

    private var shouldUpdateCart: Boolean = true
    private var shouldDismissProgressDialog: Boolean = false

    // Last saved PPP state based on productId
    private val lastPurchaseProtectionCheckStates: HashMap<String, Int> = HashMap()

    private var source: String = SOURCE_OTHERS
    private var tenor: Int = 0
    private var gatewayCode: String = ""
    private var shouldShowToaster: Boolean = false

    private var binding by autoCleared<FragmentOrderSummaryPageBinding> {
        try {
            val childCount = it.rvOrderSummaryPage.childCount
            for (index in 0 until childCount) {
                val childAt = it.rvOrderSummaryPage.getChildAt(index)
                val childViewHolder = it.rvOrderSummaryPage.getChildViewHolder(childAt)
                if (childViewHolder is OrderProductCard) {
                    childViewHolder.clearJob()
                }
            }
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(OrderSummaryPageComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        source = SOURCE_OTHERS
        when (requestCode) {
            REQUEST_CODE_COURIER_PINPOINT -> onResultFromCourierPinpoint(resultCode, data)
            REQUEST_CODE_PROMO -> onResultFromPromo(resultCode, data)
            PaymentConstant.REQUEST_CODE -> onResultFromPayment(resultCode)
            REQUEST_CODE_CREDIT_CARD -> onResultFromCreditCardPicker(data)
            REQUEST_CODE_CREDIT_CARD_ERROR -> refresh()
            REQUEST_CODE_PAYMENT_TOP_UP -> refresh()
            REQUEST_CODE_EDIT_PAYMENT -> onResultFromEditPayment(data)
            REQUEST_CODE_OPEN_ADDRESS_LIST -> onResultFromAddressList(resultCode)
            REQUEST_CODE_ADD_NEW_ADDRESS -> onResultFromAddNewAddress(resultCode, data)
            REQUEST_CODE_LINK_ACCOUNT -> onResultFromLinkAccount(resultCode, data)
            REQUEST_CODE_WALLET_ACTIVATION -> refresh()
            REQUEST_CODE_ADD_ON_GIFTING -> onResultFromAddOn(resultCode, data)
            REQUEST_CODE_UPLOAD_PRESCRIPTION -> onResultFromUploadPrescription(data)
            REQUEST_CODE_ADD_ON_PRODUCT_SERVICE_BOTTOMSHEET -> onResultFromAddOnProductBottomSheet(resultCode, data)
        }
    }

    private fun onResultFromAddressList(resultCode: Int) {
        if (resultCode == Activity.RESULT_CANCELED) {
            activity?.finish()
        } else {
            refresh()
        }
    }

    private fun onResultFromPromo(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val errorPromoExtra = data?.getStringExtra(ARGS_PROMO_ERROR) ?: ""
            if (errorPromoExtra.isNotBlank()) {
                if (errorPromoExtra.equals(ARGS_FINISH_ERROR, true)) {
                    activity?.finish()
                }
            } else {
                val validateUsePromoRequest = data?.getParcelableExtra<ValidateUsePromoRequest>(ARGS_LAST_VALIDATE_USE_REQUEST)
                val validateUse = data?.getParcelableExtra<ValidateUsePromoRevampUiModel>(ARGS_VALIDATE_USE_DATA_RESULT)
                val clearPromo = data?.getParcelableExtra<ClearPromoUiModel>(ARGS_CLEAR_PROMO_RESULT)

                if (validateUse != null) {
                    onApplyPromo(validateUsePromoRequest, validateUse)
                }

                if (clearPromo != null) {
                    onClearPromo(validateUsePromoRequest, clearPromo)
                }
            }
        }
    }

    private fun onApplyPromo(
        validateUsePromoRequest: ValidateUsePromoRequest?,
        validateUse: ValidateUsePromoRevampUiModel
    ) {
        viewModel.lastValidateUsePromoRequest = validateUsePromoRequest
        viewModel.validateUsePromoRevampUiModel = validateUse
        viewModel.validateBboStacking()
        viewModel.updatePromoStateWithoutCalculate(validateUse.promoUiModel, viewModel.orderPromo.value.lastApply)
        viewModel.reloadRates()
    }

    private fun onClearPromo(
        validateUsePromoRequest: ValidateUsePromoRequest?,
        clearPromo: ClearPromoUiModel
    ) {
        // reset
        viewModel.lastValidateUsePromoRequest = validateUsePromoRequest
        viewModel.validateUsePromoRevampUiModel = null
        viewModel.updatePromoStateWithoutCalculate(
            PromoUiModel().apply {
                titleDescription = clearPromo.successDataModel.defaultEmptyPromoMessage
            },
            viewModel.orderPromo.value.lastApply
        )
        viewModel.autoUnApplyBBO()
        // refresh shipping section and calculate total
        viewModel.reloadRates()
    }

    private fun onResultFromCourierPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data?.extras != null) {
            val locationPass: LocationPass? = data.extras?.getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION)
            if (locationPass != null) {
                viewModel.savePinpoint(locationPass.longitude, locationPass.latitude)
            } else {
                val addressData: SaveAddressDataModel? = data.extras?.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
                addressData?.let { address ->
                    viewModel.savePinpoint(address.longitude, address.latitude)
                }
            }
        }
    }

    private fun onResultFromAddNewAddress(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data?.hasExtra(LogisticConstant.EXTRA_ADDRESS_NEW) == true) {
            val addressDataModel: SaveAddressDataModel? = data.getParcelableExtra(LogisticConstant.EXTRA_ADDRESS_NEW)
            if (addressDataModel != null) {
                updateLocalCacheAddressData(addressDataModel)
                refresh()
            }
        }
    }

    private fun onResultFromPayment(resultCode: Int) {
        if (activity != null) {
            if (resultCode != PaymentConstant.PAYMENT_CANCELLED && resultCode != PaymentConstant.PAYMENT_FAILED) {
                activity?.finish()
            }
        }
    }

    private fun onResultFromCreditCardPicker(data: Intent?) {
        val metadata = data?.getStringExtra(CreditCardPickerFragment.EXTRA_RESULT_METADATA)
        val gatewayCode = data?.getStringExtra(CreditCardPickerFragment.EXTRA_RESULT_GATEWAY_CODE)
        if (gatewayCode != null && metadata != null) {
            viewModel.choosePayment(gatewayCode, metadata)
        }
    }

    private fun onResultFromEditPayment(data: Intent?) {
        val gateway = data?.getStringExtra(PaymentListingActivity.EXTRA_RESULT_GATEWAY)
        val metadata = data?.getStringExtra(PaymentListingActivity.EXTRA_RESULT_METADATA)
        if (gateway != null && metadata != null) {
            orderSummaryAnalytics.eventClickSelectedPaymentOption(gateway, userSession.get().userId)
            viewModel.choosePayment(gateway, metadata)
        }
    }

    private fun onResultFromLinkAccount(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val status = data?.getStringExtra(ApplinkConstInternalGlobal.PARAM_STATUS) ?: ""
            if (status.isNotEmpty()) {
                val message = LinkStatusMatcher.getStatus(status)
                val v = view
                if (message.isNotEmpty() && v != null) {
                    Toaster.build(v, message, Toaster.LENGTH_LONG).show()
                }
            }
        }
        refresh()
    }

    private fun onResultFromAddOnProductBottomSheet(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val addOnProductDataResult = data?.getParcelableExtra(AddOnExtraConstant.EXTRA_ADDON_PAGE_RESULT) ?: AddOnPageResult()

            if (addOnProductDataResult.aggregatedData.isGetDataSuccess) {
                val cartId = addOnProductDataResult.cartId.toString()
                val listProducts = adapter.products
                for (index in listProducts.indices) {
                    if (listProducts[index].cartId == cartId) {
                        for (addOnUiModel in addOnProductDataResult.aggregatedData.selectedAddons) {
                            listProducts[index].addOnsProductData.data.forEach { addOnExisting ->
                                if (addOnUiModel.addOnType == addOnExisting.type) {
                                    addOnExisting.apply {
                                        id = addOnUiModel.id
                                        uniqueId = addOnUiModel.uniqueId
                                        price = addOnUiModel.price
                                        infoLink = addOnUiModel.eduLink
                                        name = addOnUiModel.name
                                        status = addOnUiModel.getSaveAddonSelectedStatus().value
                                        type = addOnUiModel.addOnType
                                        productQuantity = listProducts[index].orderQuantity
                                    }
                                }
                            }
                        }
                        adapter.notifyItemChanged(adapter.getAddOnProductServiceIndex(cartId))
                    }
                }
                viewModel.calculateTotal()
            } else {
                view?.let { v ->
                    Toaster.build(v, addOnProductDataResult.aggregatedData.getDataErrorMessage, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrderSummaryPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel(savedInstanceState)
    }

    override fun onStart() {
        shouldUpdateCart = true
        shouldDismissProgressDialog = false
        super.onStart()
    }

    fun setIsFinishing() {
        shouldUpdateCart = false
    }

    override fun onStop() {
        super.onStop()
        if (binding.loaderContent.visibility == View.GONE && shouldUpdateCart) {
            viewModel.updateCart()
        }
        if (shouldDismissProgressDialog && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            viewModel.globalEvent.value = OccGlobalEvent.Normal
        }
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVE_HAS_DONE_ATC, viewModel.orderProducts.value.isNotEmpty())
    }

    private fun initViews() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    unifyprinciplesR.color.Unify_Background
                )
            )
        }
        adapter = OrderSummaryPageAdapter(
            orderSummaryAnalytics,
            getOrderShopCardListener(),
            getOrderProductCardListener(),
            getOrderPreferenceCardListener(),
            getOrderInsuranceCardListener(),
            getOrderPromoCardListener(),
            getUploadPrescriptionListener(),
            getOrderTotalPaymentCardListener()
        )
        binding.rvOrderSummaryPage.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrderSummaryPage.adapter = adapter
        binding.rvOrderSummaryPage.itemAnimator = null
    }

    private fun initViewModel(savedInstanceState: Bundle?) {
        viewModel.isCartCheckoutRevamp = CartCheckoutRevampRollenceManager().isRevamp()
        viewModel.usePromoEntryPointNewInterface = PromoEntryPointImprovementRollenceManager(
            RemoteConfigInstance.getInstance().abTestPlatform
        ).enableNewInterface()

        observeAddressState()

        observeOrderShop()

        observeOrderProducts()

        observeOrderProfile()

        observeOrderPreference()

        observeOrderShipment()

        observeOrderPayment()

        observeOrderPromo()

        observeOrderTotal()

        observeGlobalEvent()

        observeUploadPrescription()

        // first load
        if (viewModel.orderProducts.value.isEmpty()) {
            checkPromoFromPdp()
            val productIds = arguments?.getString(QUERY_PRODUCT_ID)
            if (productIds.isNullOrBlank() || savedInstanceState?.getBoolean(SAVE_HAS_DONE_ATC) == true) {
                setSourceFromPDP()
                setAdditionalParams()
                refresh()
            } else {
                atcOcc(productIds)
            }
        }
    }

    private fun navigateAddAddress(token: Token? = null) {
        startActivityForResult(
            RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3).apply {
                putExtra(EXTRA_IS_FULL_FLOW, true)
                putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
                putExtra(CheckoutConstant.KERO_TOKEN, token)
                putExtra(PARAM_SOURCE, AddEditAddressSource.OCC.source)
            },
            REQUEST_CODE_ADD_NEW_ADDRESS
        )
    }

    private fun observeAddressState() {
        viewModel.addressState.observe(viewLifecycleOwner) {
            validateAddressState(it)
        }
    }

    private fun observeOrderShop() {
        viewModel.orderShop.observe(viewLifecycleOwner) {
            adapter.shop = it
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemChanged(OrderSummaryPageAdapter.shopIndex)
                }
            } else {
                adapter.notifyItemChanged(OrderSummaryPageAdapter.shopIndex)
            }
        }
    }

    private fun observeUploadPrescription() {
        viewModel.uploadPrescriptionUiModel.observe(viewLifecycleOwner) {
            if (it.isError && it.frontEndValidation) {
                binding.rvOrderSummaryPage.smoothScrollToPosition(adapter.uploadPrescriptionIndex)
                view?.let { v ->
                    Toaster.build(
                        v,
                        getString(purchase_platformcommonR.string.pp_epharmacy_message_error_prescription_not_found),
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            }
            if ((it.uploadedImageCount ?: 0) > 0) {
                it.uploadImageText = requireActivity().getString(
                    purchase_platformcommonR.string.pp_epharmacy_upload_prescription_attached_title_text
                )
                it.descriptionText = requireActivity().getString(
                    purchase_platformcommonR.string.pp_epharmacy_upload_prescription_count_text,
                    it.uploadedImageCount
                )
                it.isError = false
            }
            adapter.uploadPrescription = it
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemChanged(adapter.uploadPrescriptionIndex)
                }
            } else {
                adapter.notifyItemChanged(adapter.uploadPrescriptionIndex)
            }
        }
    }

    private fun observeOrderProducts() {
        viewModel.orderProducts.observe(viewLifecycleOwner) {
            val oldSize = adapter.products.size
            val newSize = it.size
            adapter.products = it
            when {
                newSize > oldSize -> {
                    adapter.notifyItemRangeChanged(OrderSummaryPageAdapter.productStartIndex, oldSize)
                    adapter.notifyItemRangeInserted(oldSize, newSize - oldSize)
                }
                newSize == oldSize -> {
                    adapter.notifyItemRangeChanged(OrderSummaryPageAdapter.productStartIndex, oldSize)
                }
                newSize < oldSize -> {
                    adapter.notifyItemRangeChanged(OrderSummaryPageAdapter.productStartIndex, newSize)
                    adapter.notifyItemRangeRemoved(OrderSummaryPageAdapter.productStartIndex + newSize, oldSize - newSize)
                }
            }
        }

        viewModel.updateOrderProducts.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                var startGroupIndex = -1
                var lastGroupIndex = startGroupIndex
                for (index in it) {
                    when {
                        startGroupIndex == -1 -> {
                            startGroupIndex = index
                            lastGroupIndex = index
                        }
                        index == lastGroupIndex + 1 -> {
                            lastGroupIndex = index
                        }
                        else -> {
                            adapter.notifyItemRangeChanged(OrderSummaryPageAdapter.productStartIndex + startGroupIndex, lastGroupIndex + 1 - startGroupIndex)
                            startGroupIndex = -1
                            lastGroupIndex = startGroupIndex
                        }
                    }
                }
                if (startGroupIndex > -1 && lastGroupIndex > -1) {
                    adapter.notifyItemRangeChanged(OrderSummaryPageAdapter.productStartIndex + startGroupIndex, lastGroupIndex + 1 - startGroupIndex)
                }
            }
        }
    }

    private fun observeOrderProfile() {
        viewModel.orderProfile.observe(viewLifecycleOwner) {
            orderProfile = it
            adapter.profile = it
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemChanged(adapter.preferenceIndex)
                }
            } else {
                adapter.notifyItemChanged(adapter.preferenceIndex)
            }
        }
    }

    private fun observeOrderPreference() {
        viewModel.orderPreference.observe(viewLifecycleOwner) {
            when (it) {
                is OccState.Loading -> {
                    binding.rvOrderSummaryPage.gone()
                    binding.globalError.animateGone()
                    binding.layoutNoAddress.root.animateGone()
                    binding.loaderContent.animateShow()
                }
                is OccState.Failed -> {
                    binding.loaderContent.animateGone()
                    it.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }
                is OccState.FirstLoad -> {
                    showMainContent(it.data)
                }
                is OccState.Success -> showMainContent(it.data)
            }
        }
    }

    private fun observeOrderShipment() {
        viewModel.orderShipment.observe(viewLifecycleOwner) {
            adapter.shipment = it
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemChanged(adapter.preferenceIndex)
                    adapter.notifyItemChanged(adapter.insuranceIndex)
                }
            } else {
                adapter.notifyItemChanged(adapter.preferenceIndex)
                adapter.notifyItemChanged(adapter.insuranceIndex)
            }
            if (orderProfile?.shipment?.isDisableChangeCourier == false && it?.needPinpoint == true && orderProfile?.address != null) {
                goToPinpoint(orderProfile?.address)
            }
        }
    }

    private fun observeOrderPayment() {
        viewModel.orderPayment.observe(viewLifecycleOwner) {
            if (shouldShowToaster) showToasterSuccess()
            adapter.payment = it
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemChanged(adapter.preferenceIndex)
                }
            } else {
                adapter.notifyItemChanged(adapter.preferenceIndex)
            }
        }
    }

    private fun observeOrderPromo() {
        viewModel.orderPromo.observe(viewLifecycleOwner) {
            adapter.promo = it
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemChanged(adapter.promoIndex)
                }
            } else {
                adapter.notifyItemChanged(adapter.promoIndex)
            }
        }
    }

    private fun observeOrderTotal() {
        viewModel.orderTotal.observe(viewLifecycleOwner) {
            adapter.total = it
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemChanged(adapter.totalPaymentIndex)
                }
            } else {
                adapter.notifyItemChanged(adapter.totalPaymentIndex)
            }
        }
    }

    private fun observeGlobalEvent() {
        viewModel.globalEvent.observe(viewLifecycleOwner) {
            when (it) {
                is OccGlobalEvent.Loading -> {
                    if (progressDialog == null) {
                        context?.let { ctx ->
                            progressDialog = AlertDialog.Builder(ctx)
                                .setView(purchase_platformcommonR.layout.purchase_platform_progress_dialog_view)
                                .setCancelable(false)
                                .create()
                        }
                    }
                    if (progressDialog?.isShowing == false) {
                        progressDialog?.show()
                    }
                }
                is OccGlobalEvent.Normal -> {
                    progressDialog?.dismiss()
                }
                is OccGlobalEvent.TriggerRefresh -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        var errorMessage = it.errorMessage
                        if (errorMessage.isBlank() && it.throwable != null) {
                            errorMessage = if (it.throwable is AkamaiErrorException) {
                                it.throwable.message ?: DEFAULT_LOCAL_ERROR_MESSAGE
                            } else {
                                ErrorHandler.getErrorMessage(context, it.throwable)
                            }
                        }
                        if (errorMessage.isNotBlank()) {
                            Toaster.build(v, errorMessage, type = Toaster.TYPE_ERROR).show()
                            if (it.shouldTriggerAnalytics) {
                                orderSummaryAnalytics.eventViewErrorToasterMessage(viewModel.getShopId(), errorMessage)
                            }
                        } else if (it.successMessage.isNotBlank()) {
                            Toaster.build(v, it.successMessage, actionText = getString(purchase_platformcommonR.string.checkout_flow_toaster_action_ok)).show()
                        }
                        source = SOURCE_OTHERS
                        shouldShowToaster = false
                        refresh(uiMessage = it.uiMessage)
                    }
                }
                is OccGlobalEvent.Error -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        var message = it.errorMessage
                        if (message.isBlank()) {
                            message = if (it.throwable is AkamaiErrorException) {
                                it.throwable.message ?: DEFAULT_LOCAL_ERROR_MESSAGE
                            } else {
                                ErrorHandler.getErrorMessage(context, it.throwable)
                            }
                        }
                        Toaster.build(v, message, type = Toaster.TYPE_ERROR, actionText = it.ctaText).show()
                    }
                }

                is OccGlobalEvent.PriceChangeError -> {
                    progressDialog?.dismiss()
                    if (activity != null) {
                        val messageData = it.message
                        val priceValidationDialog =
                            DialogUnify(requireActivity(), DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
                        priceValidationDialog.setOverlayClose(false)
                        priceValidationDialog.setCancelable(false)
                        priceValidationDialog.setTitle(messageData.title)
                        priceValidationDialog.setDescription(messageData.desc)
                        priceValidationDialog.setPrimaryCTAText(messageData.action)
                        priceValidationDialog.setPrimaryCTAClickListener {
                            priceValidationDialog.dismiss()
                            source = SOURCE_OTHERS
                            shouldShowToaster = false
                            refresh()
                        }
                        priceValidationDialog.show()
                        orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PRICE_CHANGE)
                    }
                }
                is OccGlobalEvent.PromoClashing -> {
                    progressDialog?.dismiss()
                    if (activity != null) {
                        val promoNotEligibleBottomSheet = PromoNotEligibleBottomSheet(
                            it.notEligiblePromoHolderDataList,
                            object : PromoNotEligibleActionListener {
                                override fun onShow() {
                                    // no op
                                }

                                override fun onButtonContinueClicked() {
                                    viewModel.cancelIneligiblePromoCheckout(
                                        it.notEligiblePromoHolderDataList,
                                        onSuccessCheckout()
                                    )
                                    orderSummaryAnalytics.eventClickLanjutBayarPromoErrorOSP()
                                }

                                override fun onButtonChooseOtherPromo() {
                                    if (viewModel.useNewPromoPage()) {
                                        goToNewPromoPage(
                                            validateUsePromoRequest = viewModel.generateValidateUsePromoRequest(),
                                            promoRequest = viewModel.generatePromoRequest(),
                                            bboCodes = viewModel.generateBboPromoCodes()
                                        )
                                    } else {
                                        orderSummaryAnalytics.eventClickPilihPromoLainPromoErrorOSP()
                                        goToOldPromoPage(
                                            validateUsePromoRequest = viewModel.generateValidateUsePromoRequest(),
                                            promoRequest = viewModel.generatePromoRequest(),
                                            bboCodes = viewModel.generateBboPromoCodes()
                                        )
                                    }
                                }
                            }
                        )
                        promoNotEligibleBottomSheet.dismissListener = {
                            if (view != null) {
                                refresh()
                            }
                        }
                        promoNotEligibleBottomSheet.show(requireContext(), parentFragmentManager)
                        orderSummaryAnalytics.eventViewBottomSheetPromoError()
                    }
                }
                is OccGlobalEvent.AtcError -> {
                    progressDialog?.dismiss()
                    binding.loaderContent.animateGone()
                    handleAtcError(it)
                }
                is OccGlobalEvent.AtcSuccess -> {
                    if (it.shouldGoToCheckoutPage) {
                        goToCheckoutPage(it)
                    } else {
                        progressDialog?.dismiss()
                        binding.loaderContent.animateGone()
                        view?.let { v ->
                            if (it.message.isNotBlank()) {
                                Toaster.build(v, it.message).show()
                            }
                        }
                        setSourceFromPDP()
                        refresh()
                    }
                }
                is OccGlobalEvent.Prompt -> {
                    progressDialog?.dismiss()
                    showPrompt(it.prompt)
                }
                is OccGlobalEvent.ToasterAction -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        Toaster.build(v, it.toast.message, type = Toaster.TYPE_ERROR, actionText = it.toast.ctaText, clickListener = {
                            binding.rvOrderSummaryPage.smoothScrollToPosition(adapter.getFirstErrorIndex())
                        }).show()
                        orderSummaryAnalytics.eventViewErrorToasterMessage(viewModel.getShopId(), it.toast.message)
                    }
                }
                is OccGlobalEvent.ForceOnboarding -> {
                    forceShowOnboarding(it.onboarding)
                }
                is OccGlobalEvent.UpdateLocalCacheAddress -> {
                    updateLocalCacheAddressData(it.addressModel)
                }
                is OccGlobalEvent.AdjustAdminFeeError -> {
                    view?.let { v ->
                        Toaster.build(v, getString(R.string.default_afpb_error), type = Toaster.TYPE_ERROR).show()
                    }
                }
                is OccGlobalEvent.AdjustShippingToaster -> {
                    view?.let { v ->
                        Toaster.build(v, getString(purchase_platformcommonR.string.pp_auto_unapply_bo_toaster_message)).show()
                    }
                }
                is OccGlobalEvent.ToasterInfo -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        Toaster.build(v, it.message).show()
                    }
                }
                is OccGlobalEvent.PopUp -> {
                    showPopUpDialog(it.popUp)
                }
            }
        }
    }

    private fun validateAddressState(addressState: AddressState) {
        when (addressState.errorCode) {
            AddressState.ERROR_CODE_OPEN_ADDRESS_LIST -> {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
                intent.putExtra(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS, addressState.address.state)
                intent.putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_SNIPPET, true)
                intent.putExtra(PARAM_SOURCE, ManageAddressSource.DISTRICT_NOT_MATCH.source)
                startActivityForResult(intent, REQUEST_CODE_OPEN_ADDRESS_LIST)
            }
            AddressState.ERROR_CODE_OPEN_ANA -> {
                showLayoutNoAddress()
            }
            else -> {
                updateLocalCacheAddressData(addressState.address)
            }
        }
    }

    private fun updateLocalCacheAddressData(addressModel: ChosenAddressModel) {
        activity?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = it,
                addressId = addressModel.addressId.toString(),
                cityId = addressModel.cityId.toString(),
                districtId = addressModel.districtId.toString(),
                lat = addressModel.latitude,
                long = addressModel.longitude,
                label = "${addressModel.addressName} ${addressModel.receiverName}",
                postalCode = addressModel.postalCode,
                shopId = addressModel.tokonowModel.shopId.toString(),
                warehouseId = addressModel.tokonowModel.warehouseId.toString(),
                warehouses = TokonowWarehouseMapper.mapWarehousesModelToLocal(addressModel.tokonowModel.warehouses),
                serviceType = addressModel.tokonowModel.serviceType
            )
        }
    }

    private fun updateLocalCacheAddressData(addressModel: SaveAddressDataModel) {
        activity?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = it,
                addressId = addressModel.id.toString(),
                cityId = addressModel.cityId.toString(),
                districtId = addressModel.districtId.toString(),
                lat = addressModel.latitude,
                long = addressModel.longitude,
                label = "${addressModel.addressName} ${addressModel.receiverName}",
                postalCode = addressModel.postalCode,
                shopId = addressModel.shopId.toString(),
                warehouseId = addressModel.warehouseId.toString(),
                warehouses = TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressModel.warehouses),
                serviceType = addressModel.serviceType
            )
        }
    }

    private fun updateLocalCacheAddressData(addressModel: OrderProfileAddress) {
        if (addressModel.addressId.isNotBlankOrZero()) {
            activity?.let {
                val localCache = ChooseAddressUtils.getLocalizingAddressData(it)
                val newTokoNowData = addressModel.tokoNow
                val shouldUpdateTokoNowData = newTokoNowData.isModified
                if (addressModel.state == OrderProfileAddress.STATE_OCC_ADDRESS_ID_NOT_MATCH ||
                    localCache.address_id.isEmpty() || localCache.address_id == "0"
                ) {
                    ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                        context = it,
                        addressId = addressModel.addressId,
                        cityId = addressModel.cityId,
                        districtId = addressModel.districtId,
                        lat = addressModel.latitude,
                        long = addressModel.longitude,
                        label = "${addressModel.addressName} ${addressModel.receiverName}",
                        postalCode = addressModel.postalCode,
                        shopId = if (shouldUpdateTokoNowData) addressModel.tokoNow.shopId else localCache.shop_id,
                        warehouseId = if (shouldUpdateTokoNowData) addressModel.tokoNow.warehouseId else localCache.warehouse_id,
                        warehouses = if (shouldUpdateTokoNowData) TokonowWarehouseMapper.mapWarehousesResponseToLocal(addressModel.tokoNow.warehouses) else localCache.warehouses,
                        serviceType = if (shouldUpdateTokoNowData) addressModel.tokoNow.serviceType else localCache.service_type
                    )
                } else if (shouldUpdateTokoNowData) {
                    ChooseAddressUtils.updateTokoNowData(
                        context = it,
                        shopId = addressModel.tokoNow.shopId,
                        warehouseId = addressModel.tokoNow.warehouseId,
                        warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(addressModel.tokoNow.warehouses),
                        serviceType = addressModel.tokoNow.serviceType
                    )
                }
            }
        }
    }

    private fun showLayoutNoAddress() {
        binding.layoutNoAddress.root.animateShow()
        binding.layoutNoAddress.iuNoAddress.setImageUrl(NO_ADDRESS_IMAGE)
        binding.layoutNoAddress.descNoAddress.text = getString(R.string.occ_lbl_desc_no_address)
        binding.layoutNoAddress.btnOccAddNewAddress.setOnClickListener {
            navigateAddAddress()
        }
    }

    private fun goToPinpoint(
        address: OrderProfileAddress?,
        shouldUpdatePinpointFlag: Boolean = true
    ) {
        address?.let {
            val locationPass = LocationPass()
            locationPass.cityName = it.cityName
            locationPass.districtName = it.districtName
            activity?.let { activity ->
                val bundle = Bundle().apply {
                    putBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY, true)
                    putString(AddressConstant.EXTRA_CITY_NAME, locationPass.cityName)
                    putString(AddressConstant.EXTRA_DISTRICT_NAME, locationPass.districtName)
                }
                RouteManager.getIntent(activity, ApplinkConstInternalLogistic.PINPOINT).apply {
                    putExtra(AddressConstant.EXTRA_BUNDLE, bundle)
                    startActivityForResult(this, REQUEST_CODE_COURIER_PINPOINT)
                }

                if (shouldUpdatePinpointFlag) {
                    viewModel.changePinpoint()
                }
            }
        }
    }

    private fun onChoosePromoLogisticShipping(logisticPromoUiModel: LogisticPromoUiModel) {
        orderSummaryAnalytics.eventChooseBboAsDuration()
        viewModel.chooseLogisticPromo(logisticPromoUiModel)
    }

    private fun forceShowOnboarding(onboarding: OccOnboarding?) {
        if (onboarding?.isForceShowCoachMark == true) {
            if (onboarding.coachmarkType == COACHMARK_TYPE_NEW_BUYER_REMOVE_PROFILE) {
                showNewOnboarding(onboarding)
            }
            viewModel.consumeForceShowOnboarding()
        }
    }

    private fun showNewOnboarding(onboarding: OccOnboarding) {
        view?.let {
            it.post {
                try {
                    val scrollview = binding.rvOrderSummaryPage
                    val childViewHolder = scrollview.findViewHolderForAdapterPosition(adapter.preferenceIndex) as? OrderPreferenceCard
                        ?: return@post
                    val coachMarkItems = ArrayList<CoachMark2Item>()
                    for (detailIndexed in onboarding.onboardingCoachMark.details.withIndex()) {
                        val newView: View = generateNewCoachMarkAnchorForNewBuyerRemoveProfile(childViewHolder, detailIndexed.index)
                        coachMarkItems.add(CoachMark2Item(newView, detailIndexed.value.title, detailIndexed.value.message, CoachMark2.POSITION_TOP))
                    }
                    val coachMark = CoachMark2(it.context)
                    coachMark.setStepListener(object : CoachMark2.OnStepListener {
                        override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                            triggerCoachMarkAnalytics(onboarding, currentIndex)
                        }
                    })
                    coachMark.onFinishListener = {
                        when (onboarding.coachmarkType) {
                            COACHMARK_TYPE_NEW_BUYER_REMOVE_PROFILE -> orderSummaryAnalytics.eventClickDoneOnCoachmark3ForNewBuyerAfterCreateProfile(userSession.get().userId)
                        }
                    }
                    // manual scroll first item
                    val firstView = coachMarkItems.firstOrNull()?.anchorView
                    firstView?.post {
                        scrollview.scrollToPosition(adapter.preferenceIndex)
                        coachMark.showCoachMark(coachMarkItems, null)
                        // trigger first analytics
                        triggerCoachMarkAnalytics(onboarding, 0)
                    }
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
    }

    private fun triggerCoachMarkAnalytics(onboarding: OccOnboarding, currentIndex: Int) {
        when (onboarding.coachmarkType) {
            COACHMARK_TYPE_NEW_BUYER_REMOVE_PROFILE -> when (currentIndex) {
                0 -> orderSummaryAnalytics.eventViewCoachmark1ForNewBuyerAfterCreateProfile(userSession.get().userId)
                1 -> orderSummaryAnalytics.eventViewCoachmark2ForNewBuyerAfterCreateProfile(userSession.get().userId)
                2 -> orderSummaryAnalytics.eventViewCoachmark3ForNewBuyerAfterCreateProfile(userSession.get().userId)
            }
        }
    }

    private fun generateNewCoachMarkAnchorForNewBuyerRemoveProfile(view: OrderPreferenceCard, index: Int): View {
        return when (index) {
            1 -> view.binding.btnChangeAddress
            else -> view.binding.tvProfileHeader
        }
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    var message = throwable?.message
                    if (throwable !is AkamaiErrorException) {
                        message = ErrorHandler.getErrorMessage(it.context, throwable)
                    }
                    Toaster.build(
                        it,
                        message
                            ?: getString(R.string.default_osp_error_message),
                        type = Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        binding.globalError.setType(type)
        binding.globalError.setActionClickListener {
            shouldShowToaster = false
            refresh()
        }
        binding.rvOrderSummaryPage.gone()
        binding.layoutNoAddress.root.animateGone()
        binding.globalError.animateShow()
    }

    private fun handleAtcError(atcError: OccGlobalEvent.AtcError) {
        if (atcError.throwable != null) {
            when (atcError.throwable) {
                is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                    view?.let {
                        showAtcGlobalError(GlobalError.NO_CONNECTION)
                    }
                }
                is RuntimeException -> {
                    when (atcError.throwable.localizedMessage?.toIntOrNull() ?: 0) {
                        ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showAtcGlobalError(GlobalError.NO_CONNECTION)
                        ReponseStatus.NOT_FOUND -> showAtcGlobalError(GlobalError.PAGE_NOT_FOUND)
                        ReponseStatus.INTERNAL_SERVER_ERROR -> showAtcGlobalError(GlobalError.SERVER_ERROR)
                        else -> {
                            view?.let {
                                showAtcGlobalError(GlobalError.SERVER_ERROR)
                            }
                        }
                    }
                }
                else -> {
                    view?.let {
                        showAtcGlobalError(GlobalError.SERVER_ERROR)
                    }
                }
            }
            if (atcError.throwable is AkamaiErrorException) {
                view?.let {
                    Toaster.build(
                        it,
                        atcError.throwable.message
                            ?: DEFAULT_LOCAL_ERROR_MESSAGE,
                        type = Toaster.TYPE_ERROR
                    ).show()
                }
            }
        } else {
            binding.globalError.setType(GlobalError.SERVER_ERROR)
            binding.globalError.setActionClickListener {
                arguments?.getString(QUERY_PRODUCT_ID)?.let { productIds ->
                    atcOcc(productIds)
                }
            }
            if (atcError.errorMessage.isNotBlank()) {
                binding.globalError.errorDescription.text = atcError.errorMessage
            }
            binding.globalError.errorAction.text = context?.getString(R.string.lbl_try_again)
            binding.globalError.errorSecondaryAction.text = context?.getString(R.string.lbl_go_to_home)
            binding.globalError.errorSecondaryAction.visible()
            binding.globalError.setSecondaryActionClickListener {
                RouteManager.route(context, ApplinkConst.HOME)
                activity?.finish()
            }
            binding.rvOrderSummaryPage.gone()
            binding.layoutNoAddress.root.animateGone()
            binding.globalError.animateShow()
        }
    }

    private fun showAtcGlobalError(type: Int) {
        binding.globalError.setType(type)
        binding.globalError.setActionClickListener {
            arguments?.getString(QUERY_PRODUCT_ID)?.let { productIds ->
                atcOcc(productIds)
            }
        }
        binding.globalError.errorAction.text = context?.getString(R.string.lbl_try_again)
        binding.globalError.errorSecondaryAction.text = context?.getString(R.string.lbl_go_to_home)
        binding.globalError.errorSecondaryAction.visible()
        binding.globalError.setSecondaryActionClickListener {
            RouteManager.route(context, ApplinkConst.HOME)
            activity?.finish()
        }
        binding.rvOrderSummaryPage.gone()
        binding.layoutNoAddress.root.animateGone()
        binding.globalError.animateShow()
    }

    private fun goToCheckoutPage(atcSuccess: OccGlobalEvent.AtcSuccess) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra(CheckoutConstant.EXTRA_ATC_OCC_MESSAGE, atcSuccess.message)
        startActivity(intent)
        activity?.finish()
    }

    private fun atcOcc(productIds: String) {
        viewModel.atcOcc(productIds)
    }

    private fun refresh(uiMessage: OccUIMessage? = null) {
        binding.rvOrderSummaryPage.gone()
        binding.layoutNoAddress.root.animateGone()
        binding.globalError.animateGone()
        binding.loaderContent.animateShow()
        viewModel.getOccCart(source, uiMessage, gatewayCode, tenor)
    }

    private fun setSourceFromPDP() {
        var sourceArgs = arguments?.getString(QUERY_SOURCE, SOURCE_PDP)
        if (sourceArgs != SOURCE_PDP && sourceArgs != SOURCE_MINICART && sourceArgs != SOURCE_FINTECH) {
            sourceArgs = SOURCE_PDP
        }
        source = sourceArgs
    }

    private fun setAdditionalParams() {
        tenor = arguments?.getString(QUERY_TENURE_TYPE, "").toIntOrZero()
        gatewayCode = arguments?.getString(QUERY_GATEWAY_CODE, "") ?: ""
    }

    private fun resetAdditionalParams() {
        // overwrite arguments
        arguments?.putString(QUERY_TENURE_TYPE, "")
        arguments?.putString(QUERY_GATEWAY_CODE, "")

        // overwrite field
        tenor = 0
        gatewayCode = ""
        if (source == SOURCE_FINTECH) {
            source = SOURCE_OTHERS
        }
    }

    private fun showToasterSuccess() {
        shouldShowToaster = false
        if (viewModel.orderPreference.value is OccState.FirstLoad) {
            view?.let {
                it.post {
                    val successToaster = viewModel.getActivationData().successToaster
                    if (successToaster.isNotBlank()) {
                        Toaster.build(it, successToaster, actionText = getString(R.string.button_ok_message_ovo_activation)).show()
                    }
                }
            }
        }
    }

    private fun showMainContent(data: OrderPreference) {
        view?.also { _ ->
            binding.loaderContent.animateGone()
            binding.globalError.animateGone()
            adapter.onboarding = data.onboarding
            adapter.ticker = data.ticker
            binding.rvOrderSummaryPage.scrollToPosition(0)
            if (binding.rvOrderSummaryPage.isComputingLayout) {
                binding.rvOrderSummaryPage.post {
                    adapter.notifyItemRangeChanged(OrderSummaryPageAdapter.tickerIndex, 2)
                }
            } else {
                adapter.notifyItemRangeChanged(OrderSummaryPageAdapter.tickerIndex, 2)
            }
            if (data.hasValidProfile) {
                binding.rvOrderSummaryPage.show()
                binding.layoutNoAddress.root.animateGone()
            } else {
                binding.rvOrderSummaryPage.gone()
            }
            resetAdditionalParams()
        }
    }

    private fun showPrompt(prompt: OccPrompt) {
        val ctx = context ?: return
        if (prompt.type == OccPrompt.TYPE_DIALOG) {
            showDialogPrompt(prompt, ctx)
        } else if (prompt.type == OccPrompt.TYPE_BOTTOM_SHEET) {
            showBottomSheetPrompt(prompt, parentFragmentManager, ctx)
        }
    }

    private fun showDialogPrompt(prompt: OccPrompt, ctx: Context) {
        val actionType = if (prompt.buttons.size > 1) DialogUnify.HORIZONTAL_ACTION else DialogUnify.SINGLE_ACTION
        val dialogUnify = DialogUnify(ctx, actionType, DialogUnify.NO_IMAGE)
        dialogUnify.apply {
            setTitle(prompt.title)
            setDescription(prompt.description)
            prompt.getPrimaryButton()?.also { primaryButton ->
                setPrimaryCTAText(primaryButton.text)
                setPrimaryCTAClickListener { onDialogPromptButtonClicked(dialogUnify, primaryButton) }
                prompt.getSecondButton(primaryButton)?.also { secondaryButton ->
                    setSecondaryCTAText(secondaryButton.text)
                    setSecondaryCTAClickListener { onDialogPromptButtonClicked(dialogUnify, secondaryButton) }
                }
            }
            setOverlayClose(false)
            setCancelable(false)
        }.show()
    }

    private fun onDialogPromptButtonClicked(dialog: DialogUnify, button: OccPromptButton) {
        when (button.action) {
            OccPromptButton.ACTION_OPEN -> {
                RouteManager.route(context, button.link)
                activity?.finish()
            }
            OccPromptButton.ACTION_RELOAD -> {
                dialog.dismiss()
                source = SOURCE_OTHERS
                shouldShowToaster = false
                refresh()
            }
            OccPromptButton.ACTION_RETRY -> {
                dialog.dismiss()
                viewModel.finalUpdate(onSuccessCheckout(), false)
            }
        }
    }

    private fun showBottomSheetPrompt(prompt: OccPrompt, fm: FragmentManager, ctx: Context) {
        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            showCloseIcon = true
            val child = View.inflate(ctx, R.layout.bottom_sheet_error_checkout, null)
            child.findViewById<EmptyStateUnify>(R.id.es_checkout).apply {
                setImageUrl(prompt.imageUrl)
                setTitle(prompt.title)
                setDescription(prompt.description)
                prompt.getPrimaryButton()?.also { primaryButton ->
                    setPrimaryCTAText(primaryButton.text)
                    setPrimaryCTAClickListener { onBottomSheetPromptButtonClicked(bottomSheetUnify, primaryButton) }
                    prompt.getSecondButton(primaryButton)?.also { secondaryButton ->
                        setSecondaryCTAText(secondaryButton.text)
                        setSecondaryCTAClickListener { onBottomSheetPromptButtonClicked(bottomSheetUnify, secondaryButton) }
                    }
                }
            }
            setChild(child)
        }.show(fm, null)
    }

    private fun onBottomSheetPromptButtonClicked(bottomSheet: BottomSheetUnify, button: OccPromptButton) {
        when (button.action) {
            OccPromptButton.ACTION_OPEN -> {
                RouteManager.route(context, button.link)
                activity?.finish()
            }
            OccPromptButton.ACTION_RELOAD -> {
                bottomSheet.dismiss()
                source = SOURCE_OTHERS
                shouldShowToaster = false
                refresh()
            }
            OccPromptButton.ACTION_RETRY -> {
                bottomSheet.dismiss()
                viewModel.finalUpdate(onSuccessCheckout(), false)
            }
        }
    }

    private fun onSuccessCheckout(): (CheckoutOccResult) -> Unit = { checkoutOccResult: CheckoutOccResult ->
        view?.let { v ->
            activity?.let {
                val redirectParam = checkoutOccResult.paymentParameter.redirectParam
                if (redirectParam.url.isNotEmpty() && redirectParam.method.isNotEmpty()) {
                    val paymentPassData = PaymentPassData()
                    paymentPassData.redirectUrl = redirectParam.url
                    paymentPassData.queryString = redirectParam.form
                    paymentPassData.method = redirectParam.method
                    paymentPassData.transactionId = checkoutOccResult.paymentParameter.transactionId
                    paymentPassData.paymentId = checkoutOccResult.paymentParameter.transactionId

                    shouldUpdateCart = false
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
                    intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
                    intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_TOASTER_MESSAGE, checkoutOccResult.error.message)
                    startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
                    shouldDismissProgressDialog = true
                } else {
                    viewModel.globalEvent.value = OccGlobalEvent.Normal
                    Toaster.build(v, getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showPopUpDialog(popUpData: PopUpData) {
        activity?.let {
            DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(popUpData.title)
                setDescription(popUpData.description)
                setPrimaryCTAText(popUpData.button.text)
                setPrimaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    private fun getOrderShopCardListener(): OrderShopCard.OrderShopCardListener = object : OrderShopCard.OrderShopCardListener {
        override fun onClickLihatProductError(index: Int) {
            binding.rvOrderSummaryPage.layoutManager?.let {
                val linearSmoothScroller = object : LinearSmoothScroller(binding.rvOrderSummaryPage.context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                linearSmoothScroller.targetPosition = index + OrderSummaryPageAdapter.productStartIndex
                it.startSmoothScroll(linearSmoothScroller)
            }
        }
    }

    private fun getOrderProductCardListener(): OrderProductCard.OrderProductCardListener = object : OrderProductCard.OrderProductCardListener {
        override fun onProductChange(
            product: OrderProduct,
            productIndex: Int,
            shouldReloadRates: Boolean,
            shouldUpdateActionMetadata: Boolean
        ) {
            viewModel.updateProduct(product, productIndex, shouldReloadRates, shouldUpdateActionMetadata)
        }

        override fun forceUpdateCart() {
            viewModel.updateCart()
        }

        override fun onPurchaseProtectionInfoClicked(url: String, categoryId: String, protectionPricePerProduct: Int, protectionTitle: String) {
            orderSummaryAnalytics.eventPPClickTooltip(userSession.get().userId, categoryId, protectionPricePerProduct, protectionTitle)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalFintech.INSURANCE_INFO)
            intent.putExtra(
                ApplinkConstInternalFintech.PARAM_INSURANCE_INFO_URL,
                url
            )
            startActivity(intent)
        }

        override fun onPurchaseProtectionCheckedChange(isChecked: Boolean, productId: String) {
            lastPurchaseProtectionCheckStates[productId] = if (isChecked) {
                PurchaseProtectionPlanData.STATE_TICKED
            } else {
                PurchaseProtectionPlanData.STATE_UNTICKED
            }
            viewModel.calculateTotal()
        }

        override fun getLastPurchaseProtectionCheckState(productId: String): Int {
            return lastPurchaseProtectionCheckStates[productId]
                ?: PurchaseProtectionPlanData.STATE_EMPTY
        }

        override fun onClickAddOnGiftingButton(addOnButtonType: Int, addOn: AddOnGiftingDataModel, product: OrderProduct, shop: OrderShop) {
            // No need to open add on bottom sheet if action = 0
            if (addOn.addOnsButtonModel.action != 0) {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.ADD_ON_GIFTING)
                intent.putExtra(
                    AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA,
                    AddOnMapper.mapAddOnBottomSheetParam(addOnButtonType, addOn, product, shop, viewModel.orderCart, viewModel.addressState.value.address, userSession.get().name)
                )
                intent.putExtra(AddOnConstant.EXTRA_ADD_ON_SOURCE, AddOnConstant.ADD_ON_SOURCE_OCC)
                startActivityForResult(intent, REQUEST_CODE_ADD_ON_GIFTING)
            }
        }

        override fun onCheckAddOnProduct(
            newAddOnProductData: AddOnsProductDataModel.Data,
            product: OrderProduct
        ) {
            viewModel.updateAddOnProduct(
                newAddOnProductData = newAddOnProductData,
                product = product
            )
        }

        override fun onClickAddOnProductInfoIcon(
            url: String
        ) {
            RouteManager.route(
                context,
                ApplinkConstInternalGlobal.WEBVIEW,
                url
            )
        }

        override fun onClickSeeAllAddOnProductService(product: OrderProduct, addOnsProductData: AddOnsProductDataModel, shop: OrderShop) {
            // tokopedia://addon/2150637806/?cartId=341303&selectedAddonIds=4730&source=cart&
            // warehouseId=789789&isTokocabang=false&atcSource=normal&categoryId=2134&shopId=481108&
            // quantity=99923&price=1500000&discountedPrice=1500000&condition=NEW

            val productId = product.productId
            val cartId = product.cartId

            val addOnIds = arrayListOf<String>()
            val deselectAddOnIds = arrayListOf<String>()

            addOnsProductData.data.forEach { addOnItem ->
                if (addOnItem.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK) {
                    addOnIds.add(addOnItem.id)
                } else if (addOnItem.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_UNCHECK) deselectAddOnIds.add(addOnItem.id)
            }

            val price: Double
            val discountedPrice: Double
            if (product.campaignId == "0") {
                price = product.productPrice
                discountedPrice = product.productPrice
            } else {
                price = product.originalPrice
                discountedPrice = product.productPrice
            }

            val applinkAddon = ADDON.replace(QUERY_PARAM_ADDON_PRODUCT, productId)
            val applink = UriUtil.buildUriAppendParams(
                applinkAddon,
                mapOf(
                    QUERY_PARAM_CART_ID to cartId,
                    QUERY_PARAM_SELECTED_ADDON_IDS to addOnIds.toString().replace("[", "").replace("]", ""),
                    QUERY_PARAM_DESELECTED_ADDON_IDS to deselectAddOnIds.toString().replace("[", "").replace("]", ""),
                    QUERY_PARAM_PAGE_ATC_SOURCE to SOURCE_ONE_CLICK_CHECKOUT,
                    QUERY_PARAM_WAREHOUSE_ID to product.warehouseId,
                    QUERY_PARAM_IS_TOKOCABANG to product.isFulfillment,
                    QUERY_PARAM_CATEGORY_ID to product.categoryId,
                    QUERY_PARAM_SHOP_ID to shop.shopId,
                    QUERY_PARAM_QUANTITY to product.orderQuantity,
                    QUERY_PARAM_PRICE to price.toBigDecimal().toPlainString().removeSingleDecimalSuffix(),
                    QUERY_PARAM_DISCOUNTED_PRICE to discountedPrice.toBigDecimal().toPlainString().removeSingleDecimalSuffix()
                )
            )

            activity?.let {
                val intent = RouteManager.getIntent(it, applink)
                startActivityForResult(intent, REQUEST_CODE_ADD_ON_PRODUCT_SERVICE_BOTTOMSHEET)
            }
        }
    }

    private fun getOrderPreferenceCardListener(): OrderPreferenceCard.OrderPreferenceCardListener = object : OrderPreferenceCard.OrderPreferenceCardListener {

        override fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel) {
            onChoosePromoLogisticShipping(logisticPromoUiModel)
        }

        override fun reloadShipping(shopId: String) {
            viewModel.reloadRates()
            orderSummaryAnalytics.eventClickRefreshOnCourierSection(shopId)
        }

        override fun chooseAddress(currentAddressId: String) {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                orderSummaryAnalytics.eventClickArrowToChangeAddressOption(currentAddressId, userSession.get().userId)
                AddressListBottomSheet(
                    getAddressCornerUseCase.get(),
                    getTargetedTickerUseCase.get(),
                    object : AddressListBottomSheet.AddressListBottomSheetListener {
                        override fun onSelect(addressModel: RecipientAddressModel) {
                            orderSummaryAnalytics.eventClickSelectedAddressOption(addressModel.id, userSession.get().userId)
                            viewModel.chooseAddress(addressModel)
                        }

                        override fun onAddAddress(token: Token?) {
                            navigateAddAddress(token)
                        }

                        override fun onClickAddressTickerApplink(applink: String) {
                            startActivity(RouteManager.getIntent(context, applink))
                        }

                        override fun onClickAddressTickerUrl(url: String) {
                            RouteManager.route(
                                context,
                                "${ApplinkConst.WEBVIEW}?url=$url"
                            )
                        }
                    }
                ).show(this@OrderSummaryPageFragment, currentAddressId, viewModel.addressState.value.address.state)
            }
        }

        override fun chooseCourier(shipment: OrderShipment, list: ArrayList<ShippingCourierUiModel>) {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                orderSummaryAnalytics.eventChangeCourierOSP(shipment.getRealShipperId().toString())
                activity?.let {
                    ShippingCourierBottomsheet.show(
                        parentFragmentManager,
                        object : ShippingCourierBottomsheetListener {
                            override fun onCourierChoosen(
                                shippingCourierUiModel: ShippingCourierUiModel,
                                courierItemData: CourierItemData,
                                recipientAddressModel: RecipientAddressModel?,
                                cartPosition: Int,
                                isCod: Boolean,
                                isPromoCourier: Boolean,
                                isNeedPinpoint: Boolean,
                                shippingCourierList: List<ShippingCourierUiModel>
                            ) {
                                orderSummaryAnalytics.eventChooseCourierSelectionOSP(
                                    shippingCourierUiModel.productData.shipperId.toString()
                                )
                                viewModel.chooseCourier(shippingCourierUiModel)
                            }

                            override fun onCourierShipmentRecommendationCloseClicked() {
                                // no op
                            }
                        },
                        list,
                        null,
                        0,
                        true
                    )
                }
            }
        }

        override fun chooseDuration(isDurationError: Boolean, currentSpId: String) {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                if (isDurationError) {
                    orderSummaryAnalytics.eventClickUbahWhenDurationError(userSession.get().userId)
                } else if (currentSpId.isNotEmpty()) {
                    orderSummaryAnalytics.eventClickArrowToChangeDurationOption(currentSpId, userSession.get().userId)
                }
                viewModel.getShippingBottomsheetParam()?.let { param ->
                    ShippingDurationBottomsheet.show(
                        ratesParam = param,
                        fragmentManager = parentFragmentManager,
                        selectedServiceId = viewModel.orderShipment.value.serviceId.toZeroIfNull(),
                        selectedSpId = viewModel.orderShipment.value.shipperProductId.toZeroIfNull(),
                        cartPosition = 0,
                        isDisableOrderPrioritas = true,
                        isRatesTradeInApi = false,
                        recipientAddressModel = null,
                        isOcc = true,
                        shippingDurationBottomsheetListener = getShippingDurationListener()
                    )
                }
            }
        }

        override fun choosePinpoint(address: OrderProfileAddress) {
            goToPinpoint(address, false)
        }

        override fun choosePayment(profile: OrderProfile, payment: OrderPayment) {
            val currentGatewayCode = payment.gatewayCode
            orderSummaryAnalytics.eventClickArrowToChangePaymentOption(currentGatewayCode, userSession.get().userId)
            val intent = Intent(context, PaymentListingActivity::class.java).apply {
                putExtra(PaymentListingActivity.EXTRA_ADDRESS_ID, profile.address.addressId)
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_PROFILE, payment.creditCard.additionalData.profileCode)
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_MERCHANT, payment.creditCard.additionalData.merchantCode)
                val orderCost = viewModel.orderTotal.value.orderCost
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_AMOUNT, orderCost.totalPriceWithoutPaymentFees)
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_BID, payment.bid)
                val goCicilInstallmentRequest = viewModel.generateGoCicilInstallmentRequest(orderCost)
                putExtra(
                    PaymentListingActivity.EXTRA_ORDER_METADATA,
                    goCicilInstallmentRequest.orderMetadata
                )
                putExtra(
                    PaymentListingActivity.EXTRA_PROMO_PARAM,
                    goCicilInstallmentRequest.promoParam
                )
                putExtra(
                    PaymentListingActivity.EXTRA_CALLBACK_URL,
                    payment.creditCard.additionalData.callbackUrl
                )
                val paymentRequest = viewModel.generatePaymentRequest(orderCost)
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_REQUEST, gson.get().toJson(paymentRequest, PaymentRequest::class.java))
            }
            startActivityForResult(intent, REQUEST_CODE_EDIT_PAYMENT)
        }

        override fun onCreditCardInstallmentDetailClicked(payment: OrderPayment) {
            val orderTotal = viewModel.orderTotal.value
            if (orderTotal.buttonState != OccButtonState.LOADING) {
                CreditCardInstallmentDetailBottomSheet(viewModel.paymentProcessor.get()).show(
                    this@OrderSummaryPageFragment,
                    payment,
                    viewModel.orderCart,
                    orderTotal.orderCost,
                    viewModel.generatePaymentRequest(orderTotal.orderCost),
                    userSession.get().userId,
                    object : CreditCardInstallmentDetailBottomSheet.InstallmentDetailBottomSheetListener {
                        override fun onSelectInstallment(selectedInstallment: OrderPaymentInstallmentTerm, installmentList: List<OrderPaymentInstallmentTerm>) {
                            viewModel.chooseInstallment(selectedInstallment, installmentList)
                        }

                        override fun onFailedLoadInstallment() {
                            view?.let { v ->
                                Toaster.build(v, getString(R.string.default_afpb_error), type = Toaster.TYPE_ERROR).show()
                            }
                        }
                    }
                )
                orderSummaryAnalytics.eventClickTenureOptionsBottomSheet()
            }
        }

        override fun onGopayInstallmentDetailClicked() {
            val orderTotal = viewModel.orderTotal.value
            if (orderTotal.buttonState != OccButtonState.LOADING) {
                GoCicilInstallmentDetailBottomSheet(viewModel.paymentProcessor.get()).show(
                    this@OrderSummaryPageFragment,
                    viewModel.generateGoCicilInstallmentRequest(orderTotal.orderCost),
                    viewModel.orderPayment.value,
                    object :
                        GoCicilInstallmentDetailBottomSheet.InstallmentDetailBottomSheetListener {
                        override fun onSelectInstallment(
                            selectedInstallment: OrderPaymentGoCicilTerms,
                            installmentList: List<OrderPaymentGoCicilTerms>,
                            tickerMessage: String,
                            isSilent: Boolean
                        ) {
                            viewModel.chooseInstallment(
                                selectedInstallment,
                                installmentList,
                                tickerMessage,
                                isSilent
                            )
                        }

                        override fun onFailedLoadInstallment() {
                            view?.let { v ->
                                Toaster.build(
                                    v,
                                    getString(R.string.default_afpb_error),
                                    type = Toaster.TYPE_ERROR
                                ).show()
                            }
                        }
                    }
                )
                orderSummaryAnalytics.eventClickTenureOptionsBottomSheet()
            }
        }

        override fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData) {
            context?.let {
                startActivityForResult(CreditCardPickerActivity.createIntent(it, additionalData), REQUEST_CODE_CREDIT_CARD)
            }
        }

        override fun onOvoActivateClicked(callbackUrl: String) {
            PaymentActivationWebViewBottomSheet(
                ovoActivationUrl.get(),
                callbackUrl,
                getString(R.string.lbl_activate_ovo_now),
                true,
                object : PaymentActivationWebViewBottomSheet.PaymentActivationWebViewBottomSheetListener {
                    override fun onActivationResult(isSuccess: Boolean) {
                        view?.let {
                            it.post {
                                if (isSuccess) {
                                    Toaster.build(it, getString(R.string.message_ovo_activation_success), actionText = getString(R.string.button_ok_message_ovo_activation)).show()
                                } else {
                                    Toaster.build(it, getString(R.string.message_ovo_activation_failed), type = Toaster.TYPE_ERROR, actionText = getString(R.string.button_ok_message_ovo_activation)).show()
                                }
                                source = SOURCE_OTHERS
                                shouldShowToaster = false
                                refresh()
                            }
                        }
                    }
                }
            ).show(this@OrderSummaryPageFragment, userSession.get())
        }

        override fun onWalletActivateClicked(headerTitle: String, activationUrl: String, callbackUrl: String) {
            context?.let { ctx ->
                if (!URLUtil.isNetworkUrl(activationUrl) && RouteManager.isSupportApplink(ctx, activationUrl)) {
                    if (activationUrl.startsWith(ApplinkConst.LINK_ACCOUNT)) {
                        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW).apply {
                            putExtra(ApplinkConstInternalGlobal.PARAM_LD, LINK_ACCOUNT_BACK_BUTTON_APPLINK)
                            putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, LINK_ACCOUNT_SOURCE_PAYMENT)
                        }
                        startActivityForResult(intent, REQUEST_CODE_LINK_ACCOUNT)
                    } else {
                        val intent = RouteManager.getIntentNoFallback(ctx, activationUrl) ?: return
                        startActivityForResult(intent, REQUEST_CODE_WALLET_ACTIVATION)
                    }
                } else {
                    PaymentActivationWebViewBottomSheet(
                        activationUrl,
                        callbackUrl,
                        headerTitle,
                        false,
                        object : PaymentActivationWebViewBottomSheet.PaymentActivationWebViewBottomSheetListener {
                            override fun onActivationResult(isSuccess: Boolean) {
                                view?.let {
                                    it.post {
                                        source = SOURCE_OTHERS
                                        shouldShowToaster = true
                                        refresh()
                                    }
                                }
                            }
                        }
                    ).show(this@OrderSummaryPageFragment, userSession.get())
                }
            }
        }

        override fun onOvoTopUpClicked(callbackUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData) {
            context?.let {
                startActivityForResult(PaymentTopUpWebViewActivity.createIntent(it, it.getString(R.string.title_one_click_checkout_top_up_ovo), redirectUrl = callbackUrl, isHideDigital = isHideDigital, customerData = customerData), REQUEST_CODE_PAYMENT_TOP_UP)
            }
        }

        override fun onWalletTopUpClicked(walletType: Int, url: String, callbackUrl: String, isHideDigital: Int, title: String) {
            context?.let {
                startActivityForResult(PaymentTopUpWebViewActivity.createIntent(it, title, url = url, redirectUrl = callbackUrl, isHideDigital = isHideDigital), REQUEST_CODE_PAYMENT_TOP_UP)
                if (walletType == OrderPaymentWalletAdditionalData.WALLET_TYPE_GOPAY) {
                    orderSummaryAnalytics.eventClickTopUpGoPayButton()
                }
            }
        }
    }

    private fun getShippingDurationListener(): ShippingDurationBottomsheetListener =
        object : ShippingDurationBottomsheetListener {
            override fun onShippingDurationChoosen(
                shippingCourierUiModels: List<ShippingCourierUiModel>,
                selectedCourier: ShippingCourierUiModel?,
                recipientAddressModel: RecipientAddressModel?,
                cartPosition: Int,
                selectedServiceId: Int,
                serviceData: ServiceData,
                flagNeedToSetPinpoint: Boolean,
                isDurationClick: Boolean,
                isClearPromo: Boolean
            ) {
                if (selectedCourier != null) {
                    orderSummaryAnalytics.eventClickSelectedDurationOptionNew(
                        selectedCourier.productData.shipperProductId.toString(),
                        userSession.get().userId
                    )
                    val serviceId =
                        if (flagNeedToSetPinpoint) selectedServiceId else serviceData.serviceId
                    viewModel.chooseDuration(
                        serviceId,
                        selectedCourier,
                        flagNeedToSetPinpoint
                    )
                }
            }

            override fun onLogisticPromoChosen(
                shippingCourierUiModels: List<ShippingCourierUiModel>,
                courierData: ShippingCourierUiModel,
                recipientAddressModel: RecipientAddressModel?,
                cartPosition: Int,
                serviceData: ServiceData,
                flagNeedToSetPinpoint: Boolean,
                promoCode: String,
                selectedServiceId: Int,
                logisticPromo: LogisticPromoUiModel
            ) {
                onChoosePromoLogisticShipping(logisticPromo)
            }

            override fun onShowLogisticPromo(listLogisticPromo: List<LogisticPromoUiModel>) {
                listLogisticPromo.forEach { promo ->
                    if (promo.disabled && promo.description.contains(BBO_DESCRIPTION_MINIMUM_LIMIT[0]) && promo.description.contains(
                            BBO_DESCRIPTION_MINIMUM_LIMIT[1]
                        )
                    ) {
                        orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_BBO_MINIMUM)
                    }
                }
            }
        }

    private fun getOrderInsuranceCardListener(): OrderInsuranceCard.OrderInsuranceCardListener {
        return object : OrderInsuranceCard.OrderInsuranceCardListener {
            override fun onInsuranceChecked(isChecked: Boolean) {
                viewModel.setInsuranceCheck(isChecked)
            }

            override fun onClickInsuranceInfo(message: String) {
                orderSummaryAnalytics.eventClickOnInsuranceInfoTooltip(userSession.get().userId)
                showInsuranceBottomSheet(message)
            }
        }
    }

    private fun showInsuranceBottomSheet(message: String) {
        context?.let { ctx ->
            InsuranceBottomSheet().apply {
                setDesc(message)
            }.show(getString(purchase_platformcommonR.string.title_bottomsheet_insurance), ctx, parentFragmentManager)
        }
    }

    private fun getOrderPromoCardListener(): OrderPromoCard.OrderPromoCardListener {
        return object : OrderPromoCard.OrderPromoCardListener {
            override fun onClickRetryValidatePromo() {
                viewModel.validateUsePromo(forceValidateUse = true)
            }

            override fun onClickPromo() {
                viewModel.updateCartPromo { validateUsePromoRequest, promoRequest, bboCodes ->
                    if (viewModel.useNewPromoPage()) {
                        goToNewPromoPage(validateUsePromoRequest, promoRequest, bboCodes)
                    } else {
                        val codes = validateUsePromoRequest.codes
                        val promoCodes = ArrayList<String>()
                        for (code in codes) {
                            promoCodes.add(code)
                        }
                        if (validateUsePromoRequest.orders.isNotEmpty()) {
                            val orderCodes = validateUsePromoRequest.orders[0].codes
                            for (code in orderCodes) {
                                promoCodes.add(code)
                            }
                        }
                        orderSummaryAnalytics.eventClickPromoOSP(promoCodes)
                        goToOldPromoPage(validateUsePromoRequest, promoRequest, bboCodes)
                    }
                }
            }

            override fun sendImpressionUserSavingTotalSubsidyEvent(
                entryPointMessages: List<String>,
                entryPointInfo: PromoEntryPointInfo?,
                lastApply: LastApplyUiModel
            ) {
                promoEntryPointAnalytics
                    .sendImpressionUserSavingTotalSubsidyEvent(
                        userSession.get().userId,
                        PromoPageEntryPoint.CHECKOUT_PAGE,
                        entryPointMessages,
                        entryPointInfo,
                        lastApply
                    )
            }

            override fun sendClickUserSavingAndPromoEntryPointEvent(
                entryPointMessages: List<String>,
                entryPointInfo: PromoEntryPointInfo?,
                lastApply: LastApplyUiModel
            ) {
                promoEntryPointAnalytics
                    .sendClickUserSavingAndPromoEntryPointEvent(
                        userSession.get().userId,
                        PromoPageEntryPoint.CHECKOUT_PAGE,
                        entryPointMessages,
                        entryPointInfo,
                        lastApply
                    )
            }

            override fun sendImpressionUserSavingDetailTotalSubsidyEvent(
                entryPointMessages: List<String>,
                entryPointInfo: PromoEntryPointInfo?,
                lastApply: LastApplyUiModel
            ) {
                promoEntryPointAnalytics
                    .sendImpressionUserSavingDetailTotalSubsidyEvent(
                        userSession.get().userId,
                        PromoPageEntryPoint.CHECKOUT_PAGE,
                        entryPointMessages,
                        entryPointInfo,
                        lastApply
                    )
            }

            override fun sendClickUserSavingDetailTotalSubsidyEvent(
                entryPointMessages: List<String>,
                entryPointInfo: PromoEntryPointInfo?,
                lastApply: LastApplyUiModel
            ) {
                promoEntryPointAnalytics
                    .sendClickUserSavingDetailTotalSubsidyEvent(
                        userSession.get().userId,
                        PromoPageEntryPoint.CHECKOUT_PAGE,
                        entryPointMessages,
                        entryPointInfo,
                        lastApply
                    )
            }

            override fun sendImpressionPromoEntryPointErrorEvent(
                errorMessage: String,
                lastApply: LastApplyUiModel
            ) {
                promoEntryPointAnalytics
                    .sendImpressionPromoEntryPointErrorEvent(
                        userId = userSession.get().userId,
                        entryPoint = PromoPageEntryPoint.CHECKOUT_PAGE,
                        errorMessage = errorMessage,
                        lastApply = lastApply
                    )
            }
        }
    }

    private fun goToOldPromoPage(
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoRequest: PromoRequest,
        bboCodes: List<String>
    ) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
        intent.putExtra(ARGS_PAGE_SOURCE, PAGE_OCC)
        intent.putExtra(ARGS_PROMO_REQUEST, promoRequest)
        intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUsePromoRequest)
        intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, ArrayList(bboCodes))
        startActivityForResult(intent, REQUEST_CODE_PROMO)
    }

    private fun goToNewPromoPage(
        validateUsePromoRequest: ValidateUsePromoRequest,
        promoRequest: PromoRequest,
        bboCodes: List<String>
    ) {
        val totalAmount = viewModel.orderTotal.value.orderCost.totalPrice
        val bottomSheetPromo = PromoUsageBottomSheet.newInstance(
            entryPoint = PromoPageEntryPoint.OCC_PAGE,
            promoRequest = promoRequest,
            validateUsePromoRequest = validateUsePromoRequest,
            boPromoCodes = bboCodes,
            totalAmount = totalAmount,
            listener = this@OrderSummaryPageFragment
        )
        bottomSheetPromo.show(childFragmentManager)
    }

    private fun getUploadPrescriptionListener(): UploadPrescriptionListener {
        return object : UploadPrescriptionListener {
            override fun uploadPrescriptionAction(
                uploadPrescriptionUiModel: UploadPrescriptionUiModel,
                buttonText: String,
                buttonNotes: String
            ) {
                ePharmacyAnalytics.sendPrescriptionWidgetClick(uploadPrescriptionUiModel.checkoutId)
                val uploadPrescriptionIntent = RouteManager.getIntent(
                    context,
                    UploadPrescriptionViewHolder.EPharmacyAppLink
                )
                uploadPrescriptionIntent.putExtra(
                    EXTRA_CHECKOUT_ID_STRING,
                    uploadPrescriptionUiModel.checkoutId
                )
                uploadPrescriptionIntent.putExtra(
                    EXTRA_SOURCE_STRING,
                    SOURCE_OCC
                )
                startActivityForResult(
                    uploadPrescriptionIntent,
                    REQUEST_CODE_UPLOAD_PRESCRIPTION
                )
            }
        }
    }

    private fun onResultFromUploadPrescription(data: Intent?) {
        if (data != null &&
            data.extras != null &&
            data.extras!!.containsKey(KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA) &&
            activity != null
        ) {
            val prescriptions = data.extras!!.getStringArrayList(KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA)
            prescriptions?.let {
                if (it.isNotEmpty()) {
                    viewModel.updatePrescriptionIds(it)
                    view?.let { v ->
                        Toaster.build(
                            v,
                            getString(purchase_platformcommonR.string.pp_epharmacy_upload_success_text),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_NORMAL,
                            getString(purchase_platformcommonR.string.checkout_flow_toaster_action_ok)
                        ).show()
                    }
                }
            }
        }
    }

    private fun getOrderTotalPaymentCardListener(): OrderTotalPaymentCard.OrderTotalPaymentCardListener {
        return object : OrderTotalPaymentCard.OrderTotalPaymentCardListener {
            override fun onOrderDetailClicked(orderCost: OrderCost) {
                orderSummaryAnalytics.eventClickRingkasanBelanjaOSP(orderCost.totalPrice.toLong().toString())
                OrderPriceSummaryBottomSheet().show(this@OrderSummaryPageFragment, orderCost)
            }

            override fun onPayClicked() {
                viewModel.finalUpdate(onSuccessCheckout(), false)
            }

            override fun onRefreshPaymentClicked() {
                viewModel.calculateTotal()
            }
        }
    }

    private fun onResultFromAddOn(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getParcelableExtra<SaveAddOnStateResult>(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA_RESULT)
            result?.let {
                viewModel.updateAddOn(it)
            }
        }
    }

    override fun onClosePageWithApplyPromo(
        entryPoint: PromoPageEntryPoint,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest
    ) {
        onApplyPromo(lastValidateUsePromoRequest, validateUse)
    }

    override fun onClosePageWithClearPromo(
        entryPoint: PromoPageEntryPoint,
        clearPromo: ClearPromoUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        isFlowMvcLockToCourier: Boolean,
        clearedPromos: List<PromoItem>
    ) {
        onClearPromo(lastValidateUsePromoRequest, clearPromo)
    }

    override fun onClosePageWithNoAction() {
        // no-op
    }

    override fun onApplyPromo(
        entryPoint: PromoPageEntryPoint,
        validateUse: ValidateUsePromoRevampUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest
    ) {
        onApplyPromo(lastValidateUsePromoRequest, validateUse)
    }

    override fun onApplyPromoNoAction() {
        // no-op
    }

    override fun onApplyPromoFailed(throwable: Throwable) {
        val message = throwable.message ?: ""
        if (message.isNotBlank()) {
            showToast(throwable.message)
        }
    }

    override fun onClearPromoSuccess(
        entryPoint: PromoPageEntryPoint,
        clearPromo: ClearPromoUiModel,
        lastValidateUsePromoRequest: ValidateUsePromoRequest,
        isFlowMvcLockToCourier: Boolean
    ) {
        onClearPromo(lastValidateUsePromoRequest, clearPromo)
    }

    override fun onClearPromoFailed(throwable: Throwable) {
        val message = throwable.message ?: ""
        if (message.isNotBlank()) {
            showToast(throwable.message)
        }
    }

    private fun checkPromoFromPdp() {
        val listPromoAutoApply = arguments?.getParcelableArrayList<PromoExternalAutoApply>(QUERY_LIST_PROMO_AUTO_APPLY)
        if (listPromoAutoApply?.isNotEmpty() == true) {
            viewModel.listPromoExternalAutoApplyCode = listPromoAutoApply
        }
    }

    companion object {
        const val REQUEST_CODE_COURIER_PINPOINT = 13

        const val REQUEST_CODE_PROMO = 14

        const val REQUEST_CODE_CREDIT_CARD = 15
        const val REQUEST_CODE_CREDIT_CARD_ERROR = 16

        const val REQUEST_CODE_PAYMENT_TOP_UP = 17

        const val REQUEST_CODE_ADD_ADDRESS = 18

        const val REQUEST_CODE_EDIT_PAYMENT = 19

        const val REQUEST_CODE_OPEN_ADDRESS_LIST = 20

        const val REQUEST_CODE_ADD_NEW_ADDRESS = 21

        const val REQUEST_CODE_LINK_ACCOUNT = 22
        const val REQUEST_CODE_WALLET_ACTIVATION = 23

        const val REQUEST_CODE_ADD_ON_GIFTING = 24

        const val REQUEST_CODE_UPLOAD_PRESCRIPTION = 25

        const val REQUEST_CODE_ADD_ON_PRODUCT_SERVICE_BOTTOMSHEET = 26

        const val QUERY_PRODUCT_ID = "product_id"
        const val QUERY_SOURCE = "source"
        const val QUERY_GATEWAY_CODE = "gateway_code"
        const val QUERY_TENURE_TYPE = "tenure_type"
        const val QUERY_LIST_PROMO_AUTO_APPLY = "list_promo_auto_apply"

        private const val NO_ADDRESS_IMAGE = TokopediaImageUrl.NO_ADDRESS_IMAGE

        private const val SOURCE_OTHERS = "others"
        private const val SOURCE_PDP = "pdp"
        private const val SOURCE_OCC = "occ"
        private const val SOURCE_MINICART = "minicart"
        private const val SOURCE_FINTECH = "fintech"

        private const val SAVE_HAS_DONE_ATC = "has_done_atc"

        private val BBO_DESCRIPTION_MINIMUM_LIMIT = arrayOf("belum", "min")

        private const val EXTRA_CHECKOUT_ID_STRING = "extra_checkout_id_string"
        private const val EXTRA_SOURCE_STRING = "source"
        private const val KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA = "epharmacy_prescription_ids"

        @JvmStatic
        fun newInstance(
            productId: String?,
            gatewayCode: String?,
            tenureType: String?,
            source: String?,
            listPromoAutoApply: ArrayList<PromoExternalAutoApply>
        ): OrderSummaryPageFragment {
            return OrderSummaryPageFragment().apply {
                arguments = Bundle().apply {
                    putString(QUERY_PRODUCT_ID, productId)
                    putString(QUERY_GATEWAY_CODE, gatewayCode)
                    putString(QUERY_TENURE_TYPE, tenureType)
                    putString(QUERY_SOURCE, source)
                    putParcelableArrayList(QUERY_LIST_PROMO_AUTO_APPLY, listPromoAutoApply)
                }
            }
        }
    }
}
