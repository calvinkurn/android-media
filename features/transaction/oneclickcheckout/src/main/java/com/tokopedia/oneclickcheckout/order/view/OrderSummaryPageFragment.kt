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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.Companion.EXTRA_IS_FULL_FLOW
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.Companion.EXTRA_IS_LOGISTIC_LABEL
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingcourierocc.ShippingCourierOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheet
import com.tokopedia.logisticcart.shipping.features.shippingdurationocc.ShippingDurationOccBottomSheetListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
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
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.PurchaseProtectionInfoBottomsheet
import com.tokopedia.oneclickcheckout.order.view.card.OrderInsuranceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPreferenceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPromoCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderShopCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderTotalPaymentCard
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.model.OccOnboarding.Companion.COACHMARK_TYPE_NEW_BUYER_REMOVE_PROFILE
import com.tokopedia.oneclickcheckout.payment.activation.PaymentActivationWebViewBottomSheet
import com.tokopedia.oneclickcheckout.payment.creditcard.CreditCardPickerActivity
import com.tokopedia.oneclickcheckout.payment.creditcard.CreditCardPickerFragment
import com.tokopedia.oneclickcheckout.payment.creditcard.installment.CreditCardInstallmentDetailBottomSheet
import com.tokopedia.oneclickcheckout.payment.installment.GoCicilInstallmentDetailBottomSheet
import com.tokopedia.oneclickcheckout.payment.list.view.PaymentListingActivity
import com.tokopedia.oneclickcheckout.payment.topup.view.PaymentTopUpWebViewActivity
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.constant.OccConstant.SOURCE_FINTECH
import com.tokopedia.purchase_platform.common.constant.OccConstant.SOURCE_MINICART
import com.tokopedia.purchase_platform.common.constant.OccConstant.SOURCE_PDP
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleActionListener
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleBottomSheet
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
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

class OrderSummaryPageFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    @Inject
    lateinit var userSession: Lazy<UserSessionInterface>

    @Inject
    @field:Named(OCC_OVO_ACTIVATION_URL)
    lateinit var ovoActivationUrl: Lazy<String>

    @Inject
    lateinit var getAddressCornerUseCase: Lazy<GetAddressCornerUseCase>

    private val viewModel: OrderSummaryPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OrderSummaryPageViewModel::class.java]
    }

    private var orderProfile: OrderProfile? = null

    private lateinit var adapter: OrderSummaryPageAdapter

    private var progressDialog: AlertDialog? = null

    private var shouldUpdateCart: Boolean = true
    private var shouldDismissProgressDialog: Boolean = false

    // Last saved PPP state based on productId
    private val lastPurchaseProtectionCheckStates: HashMap<Long, Int> = HashMap()

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
                val validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = data?.getParcelableExtra(ARGS_VALIDATE_USE_DATA_RESULT)
                if (validateUsePromoRevampUiModel != null) {
                    viewModel.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel
                    viewModel.updatePromoState(validateUsePromoRevampUiModel.promoUiModel)
                }

                val validateUsePromoRequest: ValidateUsePromoRequest? = data?.getParcelableExtra(ARGS_LAST_VALIDATE_USE_REQUEST)
                if (validateUsePromoRequest != null) {
                    viewModel.lastValidateUsePromoRequest = validateUsePromoRequest
                }

                val clearPromoUiModel: ClearPromoUiModel? = data?.getParcelableExtra(ARGS_CLEAR_PROMO_RESULT)
                if (clearPromoUiModel != null) {
                    //reset
                    viewModel.validateUsePromoRevampUiModel = null
                    viewModel.updatePromoState(PromoUiModel().apply {
                        titleDescription = clearPromoUiModel.successDataModel.defaultEmptyPromoMessage
                    })
                    // trigger validate to reset BBO benefit
                    viewModel.validateUsePromo()
                }
            }
        }
    }

    private fun onResultFromCourierPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data?.extras != null) {
            val locationPass: LocationPass? = data.extras?.getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION)
            if (locationPass != null) {
                viewModel.savePinpoint(locationPass.longitude, locationPass.latitude)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVE_HAS_DONE_ATC, viewModel.orderProducts.value.isNotEmpty())
    }

    private fun initViews() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }
        adapter = OrderSummaryPageAdapter(orderSummaryAnalytics, getOrderShopCardListener(), getOrderProductCardListener(), getOrderPreferenceCardListener(),
                getOrderInsuranceCardListener(), getOrderPromoCardListener(), getOrderTotalPaymentCardListener())
        binding.rvOrderSummaryPage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrderSummaryPage.adapter = adapter
        binding.rvOrderSummaryPage.itemAnimator = null
    }

    private fun initViewModel(savedInstanceState: Bundle?) {
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

        observeEligibilityForAnaRevamp()

        // first load
        if (viewModel.orderProducts.value.isEmpty()) {
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

    private fun observeEligibilityForAnaRevamp() {
        viewModel.eligibleForAnaRevamp.observe(viewLifecycleOwner) {
            when (it) {
                is OccState.Success -> {
                    if (it.data.eligibleForAddressFeatureData.eligibleForRevampAna.eligible) {
                        startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3).apply {
                            putExtra(EXTRA_IS_FULL_FLOW, true)
                            putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
                            putExtra(CheckoutConstant.KERO_TOKEN, it.data.token)
                        }, REQUEST_CODE_ADD_NEW_ADDRESS)
                    } else {
                        startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
                            putExtra(EXTRA_IS_FULL_FLOW, true)
                            putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
                            putExtra(CheckoutConstant.KERO_TOKEN, it.data.token)
                        }, REQUEST_CODE_ADD_NEW_ADDRESS)
                    }
                }

                is OccState.Failed -> {
                    view?.let { view ->
                        Toaster.build(view, it.getFailure()?.throwable?.message
                                ?: getString(R.string.default_osp_error_message), Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    }
                }

                else -> {
                    /* no-op */
                }
            }
        }
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
                is OccState.FirstLoad -> showMainContent(it.data)
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
                                    .setView(com.tokopedia.purchase_platform.common.R.layout.purchase_platform_progress_dialog_view)
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
                            Toaster.build(v, it.successMessage, actionText = getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok)).show()
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
                        Toaster.build(v, message, type = Toaster.TYPE_ERROR).show()
                    }
                }
                is OccGlobalEvent.PriceChangeError -> {
                    progressDialog?.dismiss()
                    if (activity != null) {
                        val messageData = it.message
                        val priceValidationDialog = DialogUnify(requireActivity(), DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
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
                        val promoNotEligibleBottomSheet = PromoNotEligibleBottomSheet(it.notEligiblePromoHolderDataList,
                                object : PromoNotEligibleActionListener {
                                    override fun onShow() {
                                        //no op
                                    }

                                    override fun onButtonContinueClicked() {
                                        viewModel.cancelIneligiblePromoCheckout(it.notEligiblePromoHolderDataList, onSuccessCheckout())
                                        orderSummaryAnalytics.eventClickLanjutBayarPromoErrorOSP()
                                    }

                                    override fun onButtonChooseOtherPromo() {
                                        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
                                        intent.putExtra(ARGS_PAGE_SOURCE, PAGE_OCC)
                                        intent.putExtra(ARGS_VALIDATE_USE_REQUEST, viewModel.generateValidateUsePromoRequest())
                                        intent.putExtra(ARGS_PROMO_REQUEST, viewModel.generatePromoRequest())
                                        intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, viewModel.generateBboPromoCodes())

                                        orderSummaryAnalytics.eventClickPilihPromoLainPromoErrorOSP()
                                        startActivityForResult(intent, REQUEST_CODE_PROMO)
                                    }
                                })
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
                is OccGlobalEvent.ToasterInfo -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        Toaster.build(v, it.message).show()
                    }
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
                    label = String.format("%s %s", addressModel.addressName, addressModel.receiverName),
                    postalCode = addressModel.postalCode,
                    shopId = addressModel.tokonowModel.shopId.toString(),
                    warehouseId = addressModel.tokonowModel.warehouseId.toString())
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
                    label = String.format("%s %s", addressModel.addressName, addressModel.receiverName),
                    postalCode = addressModel.postalCode,
                    shopId = addressModel.shopId.toString(),
                    warehouseId = addressModel.warehouseId.toString())
        }
    }

    private fun updateLocalCacheAddressData(addressModel: OrderProfileAddress) {
        if (addressModel.addressId > 0) {
            activity?.let {
                val localCache = ChooseAddressUtils.getLocalizingAddressData(it)
                if (addressModel.state == OrderProfileAddress.STATE_OCC_ADDRESS_ID_NOT_MATCH
                        || localCache?.address_id.isNullOrEmpty() || localCache?.address_id == "0") {
                    ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                            context = it,
                            addressId = addressModel.addressId.toString(),
                            cityId = addressModel.cityId.toString(),
                            districtId = addressModel.districtId.toString(),
                            lat = addressModel.latitude,
                            long = addressModel.longitude,
                            label = String.format("%s %s", addressModel.addressName, addressModel.receiverName),
                            postalCode = addressModel.postalCode,
                            shopId = addressModel.tokoNowShopId,
                            warehouseId = addressModel.tokoNowWarehouseId)
                }
            }
        }
    }

    private fun showLayoutNoAddress() {
        binding.layoutNoAddress.root.animateShow()
        binding.layoutNoAddress.iuNoAddress.setImageUrl(NO_ADDRESS_IMAGE)
        binding.layoutNoAddress.descNoAddress.text = getString(R.string.occ_lbl_desc_no_address)
        binding.layoutNoAddress.btnOccAddNewAddress.setOnClickListener {
            viewModel.checkUserEligibilityForAnaRevamp()
        }
    }

    private fun goToPinpoint(address: OrderProfileAddress?, shouldUpdatePinpointFlag: Boolean = true) {
        address?.let {
            val locationPass = LocationPass()
            locationPass.cityName = it.cityName
            locationPass.districtName = it.districtName
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
            val bundle = Bundle()
            bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
            intent.putExtras(bundle)
            startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
            if (shouldUpdatePinpointFlag) {
                viewModel.changePinpoint()
            }
        }
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
                    Toaster.build(it, message
                            ?: getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR).show()
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
                    Toaster.build(it, atcError.throwable.message
                            ?: DEFAULT_LOCAL_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
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
        override fun onProductChange(product: OrderProduct, productIndex: Int, shouldReloadRates: Boolean) {
            viewModel.updateProduct(product, productIndex, shouldReloadRates)
        }

        override fun forceUpdateCart() {
            viewModel.updateCart()
        }

        override fun onPurchaseProtectionInfoClicked(url: String, categoryId: String, protectionPricePerProduct: Int, protectionTitle: String) {
            PurchaseProtectionInfoBottomsheet(url).show(this@OrderSummaryPageFragment)
            orderSummaryAnalytics.eventPPClickTooltip(userSession.get().userId, categoryId, protectionPricePerProduct, protectionTitle)
        }

        override fun onPurchaseProtectionCheckedChange(isChecked: Boolean, productId: Long) {
            lastPurchaseProtectionCheckStates[productId] = if (isChecked) {
                PurchaseProtectionPlanData.STATE_TICKED
            } else {
                PurchaseProtectionPlanData.STATE_UNTICKED
            }
            viewModel.calculateTotal()
        }

        override fun getLastPurchaseProtectionCheckState(productId: Long): Int {
            return lastPurchaseProtectionCheckStates[productId]
                    ?: PurchaseProtectionPlanData.STATE_EMPTY
        }
    }

    private fun getOrderPreferenceCardListener(): OrderPreferenceCard.OrderPreferenceCardListener = object : OrderPreferenceCard.OrderPreferenceCardListener {

        override fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel) {
            orderSummaryAnalytics.eventChooseBboAsDuration()
            viewModel.chooseLogisticPromo(logisticPromoUiModel)
        }

        override fun reloadShipping(shopId: String) {
            viewModel.reloadRates()
            orderSummaryAnalytics.eventClickRefreshOnCourierSection(shopId)
        }

        override fun chooseAddress(currentAddressId: String) {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                orderSummaryAnalytics.eventClickArrowToChangeAddressOption(currentAddressId, userSession.get().userId)
                AddressListBottomSheet(getAddressCornerUseCase.get(), object : AddressListBottomSheet.AddressListBottomSheetListener {
                    override fun onSelect(addressModel: RecipientAddressModel) {
                        orderSummaryAnalytics.eventClickSelectedAddressOption(addressModel.id, userSession.get().userId)
                        viewModel.chooseAddress(addressModel)
                    }

                    override fun onAddAddress(token: Token?) {
                        viewModel.checkUserEligibilityForAnaRevamp(token)
                    }
                }).show(this@OrderSummaryPageFragment, currentAddressId, viewModel.addressState.value.address.state)
            }
        }

        override fun chooseCourier(shipment: OrderShipment, list: ArrayList<RatesViewModelType>) {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                orderSummaryAnalytics.eventChangeCourierOSP(shipment.getRealShipperId().toString())
                ShippingCourierOccBottomSheet().showBottomSheet(this@OrderSummaryPageFragment, list, object : ShippingCourierOccBottomSheetListener {
                    override fun onCourierChosen(shippingCourierViewModel: ShippingCourierUiModel) {
                        orderSummaryAnalytics.eventChooseCourierSelectionOSP(shippingCourierViewModel.productData.shipperId.toString())
                        viewModel.chooseCourier(shippingCourierViewModel)
                    }

                    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
                        onLogisticPromoClick(data)
                    }
                })
            }
        }

        override fun chooseDuration(isDurationError: Boolean, currentSpId: String, list: ArrayList<RatesViewModelType>) {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                if (isDurationError) {
                    orderSummaryAnalytics.eventClickUbahWhenDurationError(userSession.get().userId)
                } else if (currentSpId.isNotEmpty()) {
                    orderSummaryAnalytics.eventClickArrowToChangeDurationOption(currentSpId, userSession.get().userId)
                }
                ShippingDurationOccBottomSheet().showBottomSheet(this@OrderSummaryPageFragment, list, object : ShippingDurationOccBottomSheetListener {
                    override fun onDurationChosen(serviceData: ServiceData, selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
                        orderSummaryAnalytics.eventClickSelectedDurationOptionNew(selectedShippingCourierUiModel.productData.shipperProductId.toString(), userSession.get().userId)
                        viewModel.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
                    }

                    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
                        onLogisticPromoClick(data)
                    }
                })
            }
        }

        override fun choosePinpoint(address: OrderProfileAddress) {
            goToPinpoint(address, false)
        }

        override fun choosePayment(profile: OrderProfile, payment: OrderPayment) {
            val currentGatewayCode = profile.payment.gatewayCode
            orderSummaryAnalytics.eventClickArrowToChangePaymentOption(currentGatewayCode, userSession.get().userId)
            val intent = Intent(context, PaymentListingActivity::class.java).apply {
                putExtra(PaymentListingActivity.EXTRA_ADDRESS_ID, profile.address.addressId.toString())
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_PROFILE, payment.creditCard.additionalData.profileCode)
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_MERCHANT, payment.creditCard.additionalData.merchantCode)
                val orderCost = viewModel.orderTotal.value.orderCost
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_AMOUNT, orderCost.totalPriceWithoutPaymentFees)
                putExtra(PaymentListingActivity.EXTRA_PAYMENT_BID, payment.bid)
            }
            startActivityForResult(intent, REQUEST_CODE_EDIT_PAYMENT)
        }

        override fun onCreditCardInstallmentDetailClicked(creditCard: OrderPaymentCreditCard) {
            val orderTotal = viewModel.orderTotal.value
            if (orderTotal.buttonState != OccButtonState.LOADING) {
                CreditCardInstallmentDetailBottomSheet(viewModel.paymentProcessor.get()).show(this@OrderSummaryPageFragment, creditCard,
                        viewModel.orderCart, orderTotal.orderCost, userSession.get().userId,
                        object : CreditCardInstallmentDetailBottomSheet.InstallmentDetailBottomSheetListener {
                            override fun onSelectInstallment(selectedInstallment: OrderPaymentInstallmentTerm, installmentList: List<OrderPaymentInstallmentTerm>) {
                                viewModel.chooseInstallment(selectedInstallment, installmentList)
                            }

                            override fun onFailedLoadInstallment() {
                                view?.let { v ->
                                    Toaster.build(v, getString(R.string.default_afpb_error), type = Toaster.TYPE_ERROR).show()
                                }
                            }
                        })
            }
        }

        override fun onGopayInstallmentDetailClicked() {
            val orderTotal = viewModel.orderTotal.value
            if (orderTotal.buttonState != OccButtonState.LOADING) {
                GoCicilInstallmentDetailBottomSheet(viewModel.paymentProcessor.get()).show(this@OrderSummaryPageFragment,
                    viewModel.orderCart, viewModel.orderPayment.value.walletData, orderTotal.orderCost, userSession.get().userId,
                    object : GoCicilInstallmentDetailBottomSheet.InstallmentDetailBottomSheetListener {
                        override fun onSelectInstallment(selectedInstallment: OrderPaymentGoCicilTerms, installmentList: List<OrderPaymentGoCicilTerms>, isSilent: Boolean) {
                            viewModel.chooseInstallment(selectedInstallment, installmentList, isSilent)
                        }

                        override fun onFailedLoadInstallment() {
                            view?.let { v ->
                                Toaster.build(v, getString(R.string.default_afpb_error), type = Toaster.TYPE_ERROR).show()
                            }
                        }
                    })
            }
        }

        override fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData) {
            context?.let {
                startActivityForResult(CreditCardPickerActivity.createIntent(it, additionalData), REQUEST_CODE_CREDIT_CARD)
            }
        }

        override fun onOvoActivateClicked(callbackUrl: String) {
            PaymentActivationWebViewBottomSheet(ovoActivationUrl.get(), callbackUrl,
                    getString(R.string.lbl_activate_ovo_now), true,
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
                    }).show(this@OrderSummaryPageFragment, userSession.get())
        }

        override fun onWalletActivateClicked(headerTitle: String, activationUrl: String, callbackUrl: String) {
            context?.let { ctx ->
                if (!URLUtil.isNetworkUrl(activationUrl) && RouteManager.isSupportApplink(ctx, activationUrl)) {
                    if (activationUrl.startsWith(ApplinkConst.LINK_ACCOUNT)) {
                        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.LINK_ACCOUNT_WEBVIEW).apply {
                            putExtra(ApplinkConstInternalGlobal.PARAM_LD, LINK_ACCOUNT_BACK_BUTTON_APPLINK)
                            putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, LINK_ACCOUNT_SOURCE_PAYMENT)
                        }
                        startActivityForResult(intent, REQUEST_CODE_LINK_ACCOUNT)
                    } else {
                        val intent = RouteManager.getIntentNoFallback(ctx, activationUrl) ?: return
                        startActivityForResult(intent, REQUEST_CODE_WALLET_ACTIVATION)
                    }
                } else {
                    PaymentActivationWebViewBottomSheet(activationUrl, callbackUrl, headerTitle, false,
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
                            }).show(this@OrderSummaryPageFragment, userSession.get())
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

    private fun getOrderInsuranceCardListener(): OrderInsuranceCard.OrderInsuranceCardListener {
        return object : OrderInsuranceCard.OrderInsuranceCardListener {
            override fun onInsuranceChecked(isChecked: Boolean) {
                viewModel.setInsuranceCheck(isChecked)
            }

            override fun onClickInsuranceInfo(title: String, message: String, image: Int) {
                context?.also { ctx ->
                    GeneralBottomSheet().apply {
                        setTitle(title)
                        setDesc(message)
                        setButtonText(getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
                        setIcon(image)
                        setButtonOnClickListener { it.dismiss() }
                    }.show(ctx, parentFragmentManager)
                }
            }
        }
    }

    private fun getOrderPromoCardListener(): OrderPromoCard.OrderPromoCardListener {
        return object : OrderPromoCard.OrderPromoCardListener {
            override fun onClickRetryValidatePromo() {
                viewModel.validateUsePromo()
            }

            override fun onClickPromo() {
                viewModel.updateCartPromo { validateUsePromoRequest, promoRequest, bboCodes ->
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
                    intent.putExtra(ARGS_PAGE_SOURCE, PAGE_OCC)
                    intent.putExtra(ARGS_PROMO_REQUEST, promoRequest)
                    intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUsePromoRequest)
                    intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, bboCodes)

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
                    startActivityForResult(intent, REQUEST_CODE_PROMO)
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

        const val QUERY_PRODUCT_ID = "product_id"
        const val QUERY_SOURCE = "source"
        const val QUERY_GATEWAY_CODE = "gateway_code"
        const val QUERY_TENURE_TYPE = "tenure_type"

        private const val NO_ADDRESS_IMAGE = "https://images.tokopedia.net/img/android/cart/ic_occ_no_address.png"

        private const val SOURCE_OTHERS = "others"

        private const val SAVE_HAS_DONE_ATC = "has_done_atc"

        @JvmStatic
        fun newInstance(productId: String?, gatewayCode: String?,
                        tenureType: String?, source: String?): OrderSummaryPageFragment {
            return OrderSummaryPageFragment().apply {
                arguments = Bundle().apply {
                    putString(QUERY_PRODUCT_ID, productId)
                    putString(QUERY_GATEWAY_CODE, gatewayCode)
                    putString(QUERY_TENURE_TYPE, tenureType)
                    putString(QUERY_SOURCE, source)
                }
            }
        }
    }
}