package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseToolbarActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.enums.BaseMultiFragmentLaunchMode
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataBottomSheet
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodAddressExt.updateLocalChosenAddressPinpoint
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodExt.getSuccessAddToCartResultPair
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchaseBinding
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.OrderCustomizationFragment
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.purchase.analytics.TokoFoodPurchaseAnalytics
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.DaggerTokoFoodPurchaseComponent
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata.CheckoutErrorMetadataDetail
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodData
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodMainData
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapter
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentBottomSheet
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseGlobalErrorBottomSheet
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseNoteBottomSheet
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseSurgeBottomSheet
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbar
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbarListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.tokofood.R as tokofoodR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@FlowPreview
@ExperimentalCoroutinesApi
open class TokoFoodPurchaseFragment :
    BaseMultiFragment(),
    TokoFoodPurchaseActionListener,
    TokoFoodPurchaseToolbarListener,
    TokoFoodPurchaseConsentBottomSheet.Listener,
    IBaseMultiFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var viewBinding by autoClearedNullable<LayoutFragmentPurchaseBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodPurchaseViewModel::class.java)
    }

    private val adapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        TokoFoodPurchaseAdapterTypeFactory(this)
    }
    private val rvLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        activity?.let {
            LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
        }
    }

    protected var rvAdapter: TokoFoodPurchaseAdapter? = null
    private var toolbar: TokoFoodPurchaseToolbar? = null
    private var loaderDialog: LoaderDialog? = null
    private var consentBottomSheet: TokoFoodPurchaseConsentBottomSheet? = null

    private var shopId = ""
    private var currentCartIdList: List<String> = listOf()
    private var isPaymentButtonLoading: Boolean = false
    private var canPaymentButtonClicked: Boolean = true

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onResume() {
        super.onResume()
        val actvt = activity
        if (actvt != null && actvt is BaseToolbarActivity) {
            actvt.title = getFragmentTitle()
            actvt.setUpActionBar(getFragmentToolbar())
        }
        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LayoutFragmentPurchaseBinding.inflate(inflater, container, false)
        val view = viewBinding?.root
        (viewBinding?.recyclerViewPurchase?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations =
            false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetValues()
        setBackground()
        setupRecyclerView()
        initializeToolbar()
        initializeRecyclerViewScrollListener()
        observeList()
        observeFragmentUiModel()
        observeUiEvent()
        collectSharedUiState()
        collectDebouncedQuantityUpdate()
        collectShouldRefreshCartData()
        collectTrackerLoadCheckoutData()
        collectTrackerPaymentCheckoutData()
        collectIsPaymentButtonLoading()
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun getFragmentTitle(): String {
        return String.EMPTY
    }

    override fun getScreenName(): String {
        return String.EMPTY
    }

    override fun getLaunchMode(): BaseMultiFragmentLaunchMode {
        return BaseMultiFragmentLaunchMode.SINGLE_TOP
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodPurchaseComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onDestroyView() {
        loaderDialog?.dialog?.dismiss()
        loaderDialog = null
        viewBinding?.recyclerViewPurchase?.adapter = null
        super.onDestroyView()
    }

    private fun loadData() {
        showLoadingLayout()
        getLocalCacheModel()?.let { addressData ->
            viewModel.setIsHasPinpoint(addressData.address_id, addressData.latLong.isNotEmpty())
        }
        viewModel.loadData()
    }

    open fun getLocalCacheModel(): LocalCacheModel? {
        return context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun showLoading() {
        toolbar?.showLoading()
        rvAdapter?.showLoading()
    }

    private fun hideLoading() {
        toolbar?.hideLoading()
    }

    private fun showLoadingLayout() {
        viewBinding?.layoutGlobalErrorPurchase?.gone()
        viewBinding?.noPinpointPurchase?.gone()
        viewBinding?.recyclerViewPurchase?.show()
        rvAdapter?.clearAllElements()
        showLoading()
    }

    override fun onBackPressed() {
        (activity as BaseTokofoodActivity).onBackPressed()
    }

    private fun initializeToolbar() {
        activity?.let {
            toolbar = viewBinding?.toolbarPurchase
            toolbar?.let { toolbar ->
                toolbar.listener = this@TokoFoodPurchaseFragment
                toolbar.setContentInsetsAbsolute(Int.ZERO, Int.ZERO)
                (activity as AppCompatActivity).setSupportActionBar(toolbar)
            }

            setToolbarShadowVisibility(false)
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (show) {
            viewBinding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
        } else {
            viewBinding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
        }
    }

    private fun setBackground() {
        activity?.let {
            it.window.decorView.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    unifyprinciplesR.color.Unify_NN0
                )
            )
        }
    }

    private fun setupRecyclerView() {
        viewBinding?.recyclerViewPurchase?.run {
            rvAdapter = TokoFoodPurchaseAdapter(adapterTypeFactory).also {
                adapter = it
            }
            layoutManager = rvLayoutManager
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        viewBinding?.recyclerViewPurchase?.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    // No-op
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.canScrollVertically(RV_DIRECTION_UP)) {
                        setToolbarShadowVisibility(true)
                    } else {
                        setToolbarShadowVisibility(false)
                    }
                }
            })
    }

    private fun observeList() {
        viewModel.visitables.observe(viewLifecycleOwner) {
            rvAdapter?.updateList(it)
            setCurrentCartList(it)
        }
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(viewLifecycleOwner) {
            toolbar?.setToolbarData(it.shopName, it.shopLocation)
        }
    }

    private fun observeUiEvent() {
        viewModel.purchaseUiEvent.observe(viewLifecycleOwner) {
            when (it.state) {
                PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE -> {
                    hideLoading()
                    renderRecyclerView()
                    (it.data as? Pair<*, *>)?.let { pair ->
                        (pair.first as? CartGeneralCartListData)?.let { response ->
                            (pair.second as? Boolean)?.let { isPreviousPopupPromo ->
                                val businessData = response.data.getTokofoodBusinessData()
                                shopId = businessData.customResponse.shop.shopId
                                loadCartData(response)
                                when {
                                    businessData.customResponse.popupErrorMessage.isNotEmpty() -> {
                                        showToasterError(
                                            businessData.customResponse.popupErrorMessage,
                                            getOkayMessage()
                                        ) {}
                                    }

                                    businessData.customResponse.popupMessage.isNotEmpty() -> {
                                        if (!isPreviousPopupPromo || !businessData.isPromoPopupType()) {
                                            showToaster(
                                                businessData.customResponse.popupMessage,
                                                getOkayMessage()
                                            ) {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                PurchaseUiEvent.EVENT_NO_PINPOINT -> {
                    hideLoading()
                    renderNoPinpoint()
                }

                PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE_PARTIAL -> {
                    hideLoading()
                    renderRecyclerView()
                    it.throwable?.let { throwable ->
                        showToasterError(throwable)
                        TokofoodErrorLogger.logExceptionToServerLogger(
                            TokofoodErrorLogger.PAGE.PURCHASE,
                            throwable,
                            TokofoodErrorLogger.ErrorType.ERROR_PAGE,
                            userSession.deviceId.orEmpty(),
                            TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR,
                            mapOf(
                                TokofoodErrorLogger.PAGE_KEY to PAGE_NAME
                            )
                        )
                    }
                }

                PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE -> {
                    hideLoading()
                    renderRecyclerView()
                    it.throwable?.let { throwable ->
                        renderGlobalError(throwable)
                        TokofoodErrorLogger.logExceptionToServerLogger(
                            TokofoodErrorLogger.PAGE.PURCHASE,
                            throwable,
                            TokofoodErrorLogger.ErrorType.ERROR_PAGE,
                            userSession.deviceId.orEmpty(),
                            TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR,
                            mapOf(
                                TokofoodErrorLogger.PAGE_KEY to PAGE_NAME
                            )
                        )
                    }
                }

                PurchaseUiEvent.EVENT_EMPTY_PRODUCTS -> {
                    activityViewModel?.loadCartList(null)
                    parentFragmentManager.popBackStack()
                }

                PurchaseUiEvent.EVENT_SUCCESS_REMOVE_PRODUCT -> onSuccessRemoveProduct(it.data as Int)
                PurchaseUiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS -> scrollToIndex(it.data as Int)
                PurchaseUiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG -> showBulkDeleteConfirmationDialog(
                    it.data as Int
                )

                PurchaseUiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT -> navigateToSetPinpoint(it.data as LocationPass)
                PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT -> {
                    (it.data as? KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse)?.let { address ->
                        address.updateLocalChosenAddressPinpoint(requireContext())
                        loadData()
                    }
                }

                PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT -> {
                    (it.data as? Throwable)?.message?.let { error ->
                        showToasterError(error)
                    }
                }

                PurchaseUiEvent.EVENT_SUCCESS_GET_CONSENT -> {
                    (it.data as? CartListBusinessDataBottomSheet)?.let { data ->
                        showConsentBottomSheet(data)
                    }
                }

                PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT -> {
                    onSuccessAgreeConsent()
                }

                PurchaseUiEvent.EVENT_SUCCESS_CHECKOUT_GENERAL -> {
                    consentBottomSheet?.dismiss()
                    viewModel.setPaymentButtonLoading(false)
                    (it.data as? CheckoutGeneralTokoFoodMainData)?.let { mainData ->
                        val paymentData = mainData.queryString
                        val paymentURL = mainData.redirectUrl
                        val callbackUrl = mainData.callbackUrl
                        goToPaymentPage(paymentData, paymentURL, callbackUrl)
                    }
                }

                PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL_BOTTOMSHEET -> {
                    consentBottomSheet?.dismiss()
                    viewModel.setPaymentButtonLoading(false)
                    loadData()
                    val globalErrorType = it.data as? Int
                    if (globalErrorType == null) {
                        showDefaultCheckoutGeneralError()
                    } else {
                        showCheckoutGeneralGlobalError(globalErrorType)
                    }
                    it.throwable?.let { throwable ->
                        TokofoodErrorLogger.logExceptionToServerLogger(
                            TokofoodErrorLogger.PAGE.PURCHASE,
                            throwable,
                            TokofoodErrorLogger.ErrorType.ERROR_PAYMENT,
                            userSession.deviceId.orEmpty(),
                            TokofoodErrorLogger.ErrorDescription.PAYMENT_ERROR
                        )
                    }
                }

                PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL_TOASTER -> {
                    consentBottomSheet?.dismiss()
                    viewModel.setPaymentButtonLoading(false)
                    loadData()
                    (it.data as? CheckoutGeneralTokoFoodData)?.let { checkoutData ->
                        val errorMetadata = checkoutData.getErrorMetadataObject()
                        when {
                            errorMetadata?.popupErrorMessage?.text?.isNotEmpty() == true -> {
                                showToasterFromMetadata(true, errorMetadata.popupErrorMessage)
                            }

                            errorMetadata?.popupMessage?.text?.isNotEmpty() == true -> {
                                showToasterFromMetadata(false, errorMetadata.popupMessage)
                            }

                            checkoutData.error.isNotEmpty() -> {
                                showToasterError(checkoutData.error)
                            }

                            else -> {
                                showDefaultCheckoutGeneralError(
                                    checkoutData.message.takeIf { errorMessage ->
                                        errorMessage.isNotBlank()
                                    }
                                )
                            }
                        }
                    }
                    it.throwable?.let { throwable ->
                        TokofoodErrorLogger.logExceptionToServerLogger(
                            TokofoodErrorLogger.PAGE.PURCHASE,
                            throwable,
                            TokofoodErrorLogger.ErrorType.ERROR_PAYMENT,
                            userSession.deviceId.orEmpty(),
                            TokofoodErrorLogger.ErrorDescription.PAYMENT_ERROR
                        )
                    }
                }

                PurchaseUiEvent.EVENT_GO_TO_ORDER_CUSTOMIZATION -> {
                    (it.data as? ProductUiModel)?.let { productUiModel ->
                        val orderCustomizationFragment = OrderCustomizationFragment.createInstance(
                            productUiModel = productUiModel,
                            cartId = productUiModel.cartId,
                            merchantId = shopId,
                            source = SOURCE,
                            cacheManagerId = ""
                        )
                        navigateToNewFragment(orderCustomizationFragment)
                    }
                }

                PurchaseUiEvent.EVENT_SUCCESS_UPDATE_NOTES -> {
                    (it.data as? CartGeneralCartListData)?.let { cartData ->
                        loadCartData(cartData)
                    }
                }
            }
        }
    }

    private fun collectSharedUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            activityViewModel?.cartDataValidationFlow?.collect {
                hideLoadingDialog()
                when (it.state) {
                    UiEvent.EVENT_LOADING_DIALOG -> {
                        showLoadingDialog()
                    }

                    UiEvent.EVENT_SUCCESS_DELETE_PRODUCT -> {
                        if (it.source == SOURCE) {
                            (it.data as? Pair<*, *>)?.let { pair ->
                                (pair.first as? String)?.let { productId ->
                                    (pair.second as? String)?.let { cartId ->
                                        viewBinding?.recyclerViewPurchase?.post {
                                            viewModel.deleteProduct(productId, cartId)
                                        }
                                    }
                                }
                            }
                            canPaymentButtonClicked = true
                        }
                    }

                    UiEvent.EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS -> {
                        viewBinding?.recyclerViewPurchase?.post {
                            viewModel.bulkDeleteUnavailableProducts()
                        }
                        canPaymentButtonClicked = true
                    }

                    UiEvent.EVENT_SUCCESS_UPDATE_NOTES -> {
                        if (it.source == SOURCE) {
                            it.data?.getSuccessAddToCartResultPair()?.let { (_, cartTokoFoodData) ->
                                cartTokoFoodData.data.getTokofoodBusinessData()
                                    .getAvailableSectionProducts().firstOrNull()?.let { product ->
                                        viewBinding?.recyclerViewPurchase?.post {
                                            viewModel.updateNotes(product)
                                        }

                                        val toasterMessage =
                                            context?.getString(tokofoodR.string.text_purchase_success_notes)
                                                .orEmpty()
                                        showToaster(toasterMessage, getOkayMessage())
                                    }
                                canPaymentButtonClicked = true
                            }
                        }
                    }

                    UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
                        if (it.source == SOURCE) {
                            (it.data as? Pair<*, *>)?.let { pair ->
                                (pair.first as? String)?.let { message ->
                                    val toasterMessage = message.takeIf { cartMessage ->
                                        cartMessage.isNotBlank()
                                    }
                                        ?: context?.getString(tokofoodR.string.text_purchase_success_quantity)
                                            .orEmpty()
                                    showToaster(toasterMessage, getOkayMessage())
                                }
                                viewBinding?.recyclerViewPurchase?.post {
                                    viewModel.refreshPartialCartInformation()
                                }
                                canPaymentButtonClicked = true
                            }
                        }
                    }

                    UiEvent.EVENT_FAILED_DELETE_PRODUCT -> {
                        if (it.source == SOURCE) {
                            it.throwable?.let { throwable ->
                                TokofoodErrorLogger.logExceptionToServerLogger(
                                    TokofoodErrorLogger.PAGE.PURCHASE,
                                    throwable,
                                    TokofoodErrorLogger.ErrorType.ERROR_REMOVE_FROM_CART,
                                    userSession.deviceId.orEmpty(),
                                    TokofoodErrorLogger.ErrorDescription.REMOVE_FROM_CART_ERROR,
                                    mapOf(
                                        TokofoodErrorLogger.PAGE_KEY to PAGE_NAME
                                    )
                                )
                            }
                            canPaymentButtonClicked = true
                        }
                    }

                    UiEvent.EVENT_FAILED_UPDATE_QUANTITY -> {
                        if (it.source == SOURCE) {
                            it.throwable?.let { throwable ->
                                TokofoodErrorLogger.logExceptionToServerLogger(
                                    TokofoodErrorLogger.PAGE.PURCHASE,
                                    throwable,
                                    TokofoodErrorLogger.ErrorType.ERROR_UPDATE_CART,
                                    userSession.deviceId.orEmpty(),
                                    TokofoodErrorLogger.ErrorDescription.UPDATE_CART_ERROR,
                                    mapOf(
                                        TokofoodErrorLogger.PAGE_KEY to PAGE_NAME
                                    )
                                )
                            }
                            viewBinding?.recyclerViewPurchase?.post {
                                viewModel.refreshPartialCartInformation()
                            }
                            canPaymentButtonClicked = true
                        }
                    }

                    UiEvent.EVENT_FAILED_UPDATE_NOTES -> {
                        if (it.source == SOURCE) {
                            it.throwable?.let { throwable ->
                                TokofoodErrorLogger.logExceptionToServerLogger(
                                    TokofoodErrorLogger.PAGE.PURCHASE,
                                    throwable,
                                    TokofoodErrorLogger.ErrorType.ERROR_UPDATE_CART,
                                    userSession.deviceId.orEmpty(),
                                    TokofoodErrorLogger.ErrorDescription.UPDATE_CART_ERROR,
                                    mapOf(
                                        TokofoodErrorLogger.PAGE_KEY to PAGE_NAME
                                    )
                                )
                                showToasterError(throwable)
                            }
                            canPaymentButtonClicked = true
                        }
                    }
                }
            }
        }
    }

    private fun collectDebouncedQuantityUpdate() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.updateQuantityStateFlow
                .collect { param ->
                    param?.let {
                        activityViewModel?.updateQuantity(it, SOURCE, false)
                    }
                }
        }
    }

    private fun collectShouldRefreshCartData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.shouldRefreshCartData
                .collect { shouldRefresh ->
                    if (shouldRefresh) {
                        viewModel.loadDataPartial()
                    }
                }
        }
    }

    private fun collectTrackerLoadCheckoutData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.trackerLoadCheckoutData
                .collect { checkoutData ->
                    val userId = userSession.userId
                    TokoFoodPurchaseAnalytics.sendLoadCheckoutTracking(checkoutData, userId)
                }
        }
    }

    private fun collectTrackerPaymentCheckoutData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.trackerPaymentCheckoutData
                .collect { checkoutData ->
                    val userId = userSession.userId
                    TokoFoodPurchaseAnalytics.sendSuccessChoosePayment(checkoutData, userId)
                }
        }
    }

    private fun collectIsPaymentButtonLoading() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isPaymentButtonLoading
                .collect { isLoading ->
                    isPaymentButtonLoading = isLoading
                }
        }
    }

    private fun renderRecyclerView() {
        viewBinding?.let {
            it.layoutGlobalErrorPurchase.gone()
            it.noPinpointPurchase.gone()
            it.recyclerViewPurchase.show()
        }
    }

    private fun renderGlobalError(throwable: Throwable) {
        viewBinding?.let {
            it.layoutGlobalErrorPurchase.show()
            it.noPinpointPurchase.gone()
            it.recyclerViewPurchase.gone()
            val errorType = getGlobalErrorType(throwable)
            it.layoutGlobalErrorPurchase.setType(errorType)
            it.layoutGlobalErrorPurchase.setActionClickListener {
                loadData()
            }
        }
    }

    private fun renderNoPinpoint() {
        viewBinding?.run {
            layoutGlobalErrorPurchase.gone()
            recyclerViewPurchase.gone()
        }
        viewBinding?.noPinpointPurchase?.run {
            setType(GlobalError.PAGE_NOT_FOUND)
            errorIllustration.loadImage(NO_PINPOINT_URL)
            errorIllustration.adjustViewBounds = true
            errorTitle.text =
                context?.getString(tokofoodR.string.text_purchase_no_pinpoint)
                    .orEmpty()
            errorDescription.text =
                context?.getString(tokofoodR.string.text_purchase_pinpoint_benefit)
                    .orEmpty()
            errorAction.text =
                context?.getString(tokofoodR.string.text_purchase_set_pinpoint)
                    .orEmpty()
            setActionClickListener {
                viewModel.validateSetPinpoint()
            }
            show()
        }
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun loadCartData(response: CartGeneralCartListData) {
        if (response.isEnabled() && !response.data.shoppingSummary.getTokofoodBusinessBreakdown().customResponse.hideSummary) {
            activityViewModel?.loadCartList(response)
        } else {
            activityViewModel?.loadCartList(SOURCE)
        }
    }

    private fun navigateToSetPinpoint(locationPass: LocationPass) {
        activity?.let {
            val bundle = Bundle().apply {
                putBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY, true)
                putDouble(AddressConstant.EXTRA_LAT, locationPass.latitude.toDouble())
                putDouble(AddressConstant.EXTRA_LONG, locationPass.longitude.toDouble())
            }
            RouteManager.getIntent(it, ApplinkConstInternalLogistic.PINPOINT).apply {
                putExtra(AddressConstant.EXTRA_BUNDLE, bundle)
                startActivityForResult(this, REQUEST_CODE_SET_PINPOINT)
            }
        }
    }

    private fun showBulkDeleteConfirmationDialog(productCount: Int) {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(
                    getString(
                        tokofoodR.string.text_purchase_delete_item,
                        productCount.toString()
                    )
                )
                setDescription(getString(tokofoodR.string.text_purchase_delete_all))
                setPrimaryCTAText(getString(tokofoodR.string.text_purchase_delete))
                setSecondaryCTAText(getString(tokofoodR.string.text_purchase_back))
                setPrimaryCTAClickListener {
                    activityViewModel?.deleteUnavailableProducts()
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    private fun onSuccessRemoveProduct(productCount: Int) {
        showToaster(
            context?.getString(
                tokofoodR.string.text_purchase_success_delete,
                productCount
            ).orEmpty(),
            getOkayMessage()
        )
    }

    private fun navigateToHomePage(isFinishCurrent: Boolean = false) {
        TokofoodRouteManager.routePrioritizeInternal(
            context,
            ApplinkConstInternalTokoFood.HOME,
            isFinishCurrent
        )
    }

    private fun navigateToMerchantPage(merchantId: String, isFinishCurrent: Boolean = false) {
        val merchantPageUri = Uri.parse(ApplinkConstInternalTokoFood.MERCHANT)
            .buildUpon()
            .appendQueryParameter(DeeplinkMapperTokoFood.PARAM_MERCHANT_ID, merchantId)
            .build()
        TokofoodRouteManager.routePrioritizeInternal(
            context,
            merchantPageUri.toString(),
            isFinishCurrent
        )
    }

    private fun scrollToIndex(index: Int) {
        activity?.let { activity ->
            viewBinding?.recyclerViewPurchase?.layoutManager?.let {
                val linearSmoothScroller = object : LinearSmoothScroller(activity) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                linearSmoothScroller.targetPosition = index
                it.startSmoothScroll(linearSmoothScroller)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHANGE_ADDRESS -> {
                if (resultCode == CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS) {
                    onResultFromChangeAddress(data)
                }
            }

            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
            REQUEST_CODE_PAYMENT -> {
                when (resultCode) {
                    PaymentConstant.PAYMENT_SUCCESS -> {
                        onResultFromPaymentSuccess()
                    }

                    PaymentConstant.PAYMENT_FAILED -> {
                        showDefaultCheckoutGeneralError()
                    }
                }
            }
        }
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val locationPass =
                    it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                if (locationPass == null) {
                    val addressData =
                        it.getParcelableExtra(AddressConstant.EXTRA_SAVE_DATA_UI_MODEL) as? SaveAddressDataModel
                    addressData?.let { address ->
                        showLoadingLayout()
                        viewModel.updateAddressPinpoint(address.latitude, address.longitude)
                    }
                } else {
                    showLoadingLayout()
                    viewModel.updateAddressPinpoint(locationPass.latitude, locationPass.longitude)
                }
            }
        }
    }

    private fun onResultFromChangeAddress(intent: Intent?) {
        showAddressToaster(intent)
        setPinpointOnResult(intent)
        loadData()
    }

    private fun getAddressMessage(isNewAddress: Boolean): String {
        return if (isNewAddress) {
            context?.getString(tokofoodR.string.text_purchase_success_add_address)
                .orEmpty()
        } else {
            context?.getString(tokofoodR.string.text_purchase_success_edit_address)
                .orEmpty()
        }
    }

    private fun getAddressMessageAction(isNewAddress: Boolean): String {
        return if (isNewAddress) {
            String.EMPTY
        } else {
            getOkayMessage()
        }
    }

    private fun showAddressToaster(intent: Intent?) {
        val isNewAddress =
            intent?.getBooleanExtra(ChooseAddressConstant.EXTRA_IS_FROM_ANA, false) == true
        val toasterMessage = getAddressMessage(isNewAddress)
        val toasterActionMessage = getAddressMessageAction(isNewAddress)
        showToaster(
            toasterMessage,
            toasterActionMessage
        )
    }

    private fun setPinpointOnResult(intent: Intent?) {
        intent?.getParcelableExtra<ChosenAddressModel>(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA)
            ?.let { chosenAddressModel ->
                val hasPinpoint =
                    chosenAddressModel.latitude.isNotBlank() && chosenAddressModel.longitude.isNotBlank()
                viewModel.setIsHasPinpoint(chosenAddressModel.addressId.toString(), hasPinpoint)
            }
    }

    private fun onResultFromPaymentSuccess() {
        navigateToHomePage()
    }

    private fun showLoadingDialog() {
        context?.let {
            loaderDialog = LoaderDialog(it).apply {
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
            }
            loaderDialog?.show()
        }
    }

    private fun hideLoadingDialog() {
        if (loaderDialog?.dialog?.isShowing == true) loaderDialog?.dialog?.dismiss()
    }

    private fun showConsentBottomSheet(data: CartListBusinessDataBottomSheet) {
        if (data.isShowBottomSheet) {
            consentBottomSheet = TokoFoodPurchaseConsentBottomSheet.createInstance(
                data.title,
                data.description,
                data.termsAndCondition,
                data.imageUrl,
                this
            ).apply {
                setOnDismissListener {
                    this@TokoFoodPurchaseFragment.viewModel.setPaymentButtonLoading(false)
                }
            }
            consentBottomSheet?.show(childFragmentManager)
        }
    }

    private fun showCheckoutGeneralGlobalError(globalErrorType: Int) {
        val bottomSheet = TokoFoodPurchaseGlobalErrorBottomSheet.createInstance(
            globalErrorType = globalErrorType,
            listener = object : TokoFoodPurchaseGlobalErrorBottomSheet.Listener {
                override fun onGoToHome() {
                    navigateToNewFragment(TokoFoodHomeFragment.createInstance())
                }

                override fun onRetry() {
                    if (canPaymentButtonClicked) {
                        viewModel.setPaymentButtonLoading(true)
                        viewModel.checkoutGeneral()
                    }
                }

                override fun onCheckOtherMerchant() {
                }

                override fun onStayOnCurrentPage() {
                }
            }
        ).apply {
            setOnDismissListener {
                viewModel.setPaymentButtonLoading(false)
            }
        }
        bottomSheet.show(parentFragmentManager)
    }

    private fun showToasterError(
        errorMessage: String,
        actionMessage: String,
        onActionClicked: () -> Unit = {}
    ) {
        view?.let {
            Toaster.build(
                view = it,
                text = errorMessage,
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                actionText = actionMessage,
                clickListener = {
                    onActionClicked()
                }
            ).show()
        }
    }

    private fun showToaster(
        message: String,
        actionMessage: String,
        onActionClicked: () -> Unit = {}
    ) {
        view?.let {
            Toaster.build(
                view = it,
                text = message,
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL,
                actionText = actionMessage,
                clickListener = {
                    onActionClicked()
                }
            ).show()
        }
    }

    private fun showToasterError(throwable: Throwable) {
        view?.let {
            Toaster.build(
                view = it,
                text = getErrorMessage(throwable),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                actionText = getOkayMessage()
            ).show()
        }
    }

    private fun showToasterError(errorMessage: String) {
        view?.let {
            Toaster.build(
                view = it,
                text = errorMessage,
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_ERROR,
                actionText = getOkayMessage()
            ).show()
        }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: throwable.message.orEmpty()
    }

    private fun getOkayMessage(): String =
        context?.getString(tokofoodR.string.text_purchase_okay).orEmpty()

    private fun showDefaultCheckoutGeneralError(message: String? = null) {
        val errorMessage =
            message
                ?: context?.getString(tokofoodR.string.text_purchase_failed_to_payment)
                    .orEmpty()
        val actionMessage =
            context?.getString(tokofoodR.string.text_purchase_try_again).orEmpty()
        showToasterError(errorMessage, actionMessage) {
            if (canPaymentButtonClicked) {
                viewModel.setPaymentButtonLoading(true)
                viewModel.checkoutGeneral()
            }
        }
    }

    private fun showToasterFromMetadata(
        isErrorToaster: Boolean,
        errorDetail: CheckoutErrorMetadataDetail
    ) {
        val actionText = errorDetail.actionText.takeIf { it.isNotEmpty() } ?: getOkayMessage()
        val toasterAction: () -> Unit = {
            when (errorDetail.action) {
                CheckoutErrorMetadataDetail.REFRESH_ACTION -> {
                    if (canPaymentButtonClicked) {
                        viewModel.setPaymentButtonLoading(true)
                        viewModel.checkoutGeneral()
                    }
                }

                CheckoutErrorMetadataDetail.REDIRECT_ACTION -> {
                    context?.let {
                        TokofoodRouteManager.routePrioritizeInternal(it, errorDetail.link)
                    }
                }

                else -> {
                    // Dismiss only
                }
            }
        }
        if (isErrorToaster) {
            showToasterError(errorDetail.text, actionText) {
                toasterAction()
            }
        } else {
            showToaster(errorDetail.text, actionText) {
                toasterAction()
            }
        }
    }

    private fun goToPaymentPage(paymentData: String, paymentURL: String, callbackUrl: String) {
        val isContainsPaymentInformation =
            paymentData.isNotEmpty() || paymentURL.isNotEmpty() || callbackUrl.isNotEmpty()
        if (isContainsPaymentInformation) {
            val checkoutResultData = PaymentPassData()
            checkoutResultData.queryString = paymentData
            checkoutResultData.redirectUrl = paymentURL
            checkoutResultData.callbackSuccessUrl = callbackUrl

            val paymentCheckoutString = ApplinkConstInternalPayment.PAYMENT_CHECKOUT
            val intent = RouteManager.getIntent(context, paymentCheckoutString)
            intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, checkoutResultData)
            try {
                startActivityForResult(intent, REQUEST_CODE_PAYMENT)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    private fun setCurrentCartList(visitableList: List<Visitable<*>>) {
        currentCartIdList =
            visitableList.filterIsInstance(TokoFoodPurchaseProductTokoFoodPurchaseUiModel::class.java)
                .map { it.cartId }
    }

    override fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        return viewModel.getNextItems(currentIndex, count)
    }

    override fun onTextChangeShippingAddressClicked() {
        val intent =
            RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS).apply {
                putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true)
                putExtra(PARAM_SOURCE, ManageAddressSource.TOKOFOOD.source)
            }
        startActivityForResult(intent, REQUEST_CODE_CHANGE_ADDRESS)
    }

    override fun onTextSetPinpointClicked() {
        val locationPass =
            LocationPass().apply {
                latitude = TokoFoodPurchaseViewModel.TOTO_LATITUDE
                longitude = TokoFoodPurchaseViewModel.TOTO_LONGITUDE
            }
        navigateToSetPinpoint(locationPass)
    }

    override fun onTextAddItemClicked() {
        navigateToMerchantPage(shopId, isFinishCurrent = true)
    }

    override fun onTextBulkDeleteUnavailableProductsClicked() {
        viewModel.validateBulkDelete()
    }

    override fun onQuantityChanged() {
        if (!isPaymentButtonLoading) {
            canPaymentButtonClicked = false
            viewModel.triggerEditQuantity()
        }
    }

    override fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        if (!isPaymentButtonLoading) {
            activityViewModel?.deleteProduct(
                cartId = element.cartId,
                productId = element.id,
                source = SOURCE,
                shouldRefreshCart = false
            )
        }
    }

    override fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        val addOnBottomSheet = TokoFoodPurchaseNoteBottomSheet(
            element.notes,
            object : TokoFoodPurchaseNoteBottomSheet.Listener {
                override fun onSaveNotesClicked(notes: String) {
                    val updateParam =
                        TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateParam(
                            listOf(element.copy(notes = notes)),
                            shopId
                        )
                    activityViewModel?.updateNotes(updateParam, SOURCE, false)
                }
            }
        )
        addOnBottomSheet.show(parentFragmentManager)
    }

    override fun onTextChangeNoteAndVariantClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        viewModel.updateProductVariant(element)
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel.toggleUnavailableProductsAccordion()
    }

    override fun onTextShowUnavailableItemClicked() {
        viewModel.scrollToUnavailableItem()
    }

    override fun onPromoWidgetClicked() {
        navigateToNewFragment(
            TokoFoodPromoFragment.createInstance(
                SOURCE,
                String.EMPTY,
                currentCartIdList
            )
        )
    }

    override fun onButtonCheckoutClicked() {
        viewModel.checkUserConsent()
    }

    override fun onSurgePriceIconClicked(title: String, desc: String) {
        TokoFoodPurchaseSurgeBottomSheet.createInstance(title, desc).show(childFragmentManager)
    }

    override fun onSuccessAgreeConsent() {
        if (canPaymentButtonClicked) {
            viewModel.setConsentAgreed(true)
            viewModel.checkoutGeneral()
        }
    }

    override fun onFailedAgreeConsent(throwable: Throwable) {
        viewModel.setPaymentButtonLoading(false)
        consentBottomSheet?.dismiss()
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.PURCHASE,
            throwable,
            TokofoodErrorLogger.ErrorType.ERROR_PAGE,
            userSession.deviceId.orEmpty(),
            TokofoodErrorLogger.ErrorDescription.AGREE_CONSENT_ERROR,
            mapOf(
                TokofoodErrorLogger.PAGE_KEY to PAGE_NAME
            )
        )
        showToasterError(throwable)
    }

    companion object {
        const val RV_DIRECTION_UP = -1

        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        const val REQUEST_CODE_CHANGE_ADDRESS = 111
        const val REQUEST_CODE_SET_PINPOINT = 112
        const val REQUEST_CODE_PAYMENT = 113

        private const val SOURCE = "checkout_page"
        private const val PAGE_NAME = "checkout_page"

        private const val NO_PINPOINT_URL = TokopediaImageUrl.IMG_STATIC_URI_NO_PIN_POIN

        fun createInstance(): TokoFoodPurchaseFragment {
            return TokoFoodPurchaseFragment()
        }
    }
}
