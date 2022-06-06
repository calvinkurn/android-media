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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.activity.BaseToolbarActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodConsentBottomSheet
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodExt.getSuccessUpdateResultPair
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchaseBinding
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.OrderCustomizationFragment
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
import kotlinx.coroutines.flow.collect
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodPurchaseFragment : BaseListFragment<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory>(),
    TokoFoodPurchaseActionListener, TokoFoodPurchaseToolbarListener,
    TokoFoodPurchaseConsentBottomSheet.Listener,IBaseMultiFragment {

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

    private var toolbar: TokoFoodPurchaseToolbar? = null
    private var loaderDialog: LoaderDialog? = null
    private var consentBottomSheet: TokoFoodPurchaseConsentBottomSheet? = null

    private var shopId = ""

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = LayoutFragmentPurchaseBinding.inflate(inflater, container, false)
        val view = viewBinding?.root
        (getRecyclerView(view)?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
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
        loadData()
    }

    override fun onStop() {
        super.onStop()
        viewModel.resetValues()
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun getFragmentTitle(): String? {
        return ""
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No-op
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view_purchase

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodPurchaseComponent
                    .builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun showLoading() {
        super.showLoading()
        toolbar?.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
        toolbar?.hideLoading()
    }

    override fun loadData(page: Int) {

    }

    private fun loadData() {
        showLoadingLayout()
        context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it).let { addressData ->
                viewModel.setIsHasPinpoint(addressData.address_id, addressData.latLong.isNotEmpty())
            }
        }
        viewModel.loadData()
    }

    private fun showLoadingLayout() {
        viewBinding?.layoutGlobalErrorPurchase?.gone()
        viewBinding?.noPinpointPurchase?.gone()
        viewBinding?.recyclerViewPurchase?.show()
        adapter.clearAllElements()
        showLoading()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory> {
        return TokoFoodPurchaseAdapter(adapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): TokoFoodPurchaseAdapterTypeFactory {
        return TokoFoodPurchaseAdapterTypeFactory(this)
    }

    override fun onBackPressed() {
        (activity as BaseTokofoodActivity).onBackPressed()
    }

    private fun initializeToolbar() {
        activity?.let {
            viewBinding?.toolbarPurchase?.removeAllViews()
            val tokoFoodPurchaseToolbar = TokoFoodPurchaseToolbar(it).apply {
                listener = this@TokoFoodPurchaseFragment
            }

            toolbar = tokoFoodPurchaseToolbar

            toolbar?.let {
                viewBinding?.toolbarPurchase?.addView(toolbar)
                it.setContentInsetsAbsolute(0, 0);
                (activity as AppCompatActivity).setSupportActionBar(viewBinding?.toolbarPurchase)
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
            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        getRecyclerView(view)?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // No-op
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(-1)) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
            }
        })
    }

    private fun observeList() {
        viewModel.visitables.observe(viewLifecycleOwner, {
            (adapter as TokoFoodPurchaseAdapter).updateList(it)
        })
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(viewLifecycleOwner, {
            toolbar?.setToolbarData(it.shopName, it.shopLocation)
        })
    }

    private fun observeUiEvent() {
        viewModel.purchaseUiEvent.observe(viewLifecycleOwner, {
            when (it.state) {
                PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE -> {
                    hideLoading()
                    renderRecyclerView()
                    (it.data as? Pair<*,*>)?.let { pair ->
                        (pair.first as? CheckoutTokoFood)?.let { response ->
                            (pair.second as? Boolean)?.let { isPreviousPopupPromo ->
                                shopId = response.data.shop.shopId
                                activityViewModel?.loadCartList(response)
                                when {
                                    response.data.popupErrorMessage.isNotEmpty() -> {
                                        showToasterError(response.data.popupErrorMessage, getOkayMessage()) {}
                                    }
                                    response.data.popupMessage.isNotEmpty() -> {
                                        if (!isPreviousPopupPromo || !response.data.isPromoPopupType()) {
                                            showToaster(response.data.popupMessage, getOkayMessage()) {}
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
                    val emptyProductShopId = (it.data as? String).orEmpty()
                    if (emptyProductShopId.isBlank()) {
                        navigateToHomePage()
                    } else {
                        navigateToMerchantPage(emptyProductShopId)
                    }
                }
                PurchaseUiEvent.EVENT_SUCCESS_REMOVE_PRODUCT -> onSuccessRemoveProduct(it.data as Int)
                PurchaseUiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS -> scrollToIndex(it.data as Int)
                PurchaseUiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG -> showBulkDeleteConfirmationDialog(it.data as Int)
                PurchaseUiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT -> navigateToSetPinpoint(it.data as LocationPass)
                PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT -> {
                    loadData()
                }
                PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT -> {

                }
                PurchaseUiEvent.EVENT_SUCCESS_GET_CONSENT -> {
                    (it.data as? CheckoutTokoFoodConsentBottomSheet)?.let { data ->
                        showConsentBottomSheet(data)
                    }
                }
                PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT -> {
                    onSuccessAgreeConsent()
                }
                PurchaseUiEvent.EVENT_FAILED_VALIDATE_CONSENT -> {
                    it.throwable?.let { throwable -> showToasterError(throwable) }
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
                    (it.data as? CheckoutGeneralTokoFoodData)?.let { checkoutData ->
                        val errorMetadata = checkoutData.getErrorMetadataObject()
                        when {
                            errorMetadata?.popupErrorMessage?.text?.isNotEmpty() == true -> {
                                showToasterFromMetadata(true, errorMetadata.popupErrorMessage)
                            }
                            errorMetadata?.popupMessage?.text?.isNotEmpty() == true -> {
                                showToasterFromMetadata(false, errorMetadata.popupMessage)
                            }
                            else -> {
                                showDefaultCheckoutGeneralError(checkoutData.message.takeIf { it.isNotBlank() })
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
            }
        })
    }

    private fun collectSharedUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect {
                hideLoadingDialog()
                when(it.state) {
                    UiEvent.EVENT_LOADING_DIALOG -> {
                        showLoadingDialog()
                    }
                    UiEvent.EVENT_SUCCESS_DELETE_PRODUCT -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? String)?.let { previousCartId ->
                                (pair.second as? CartTokoFoodData)?.carts?.firstOrNull()?.let { product ->
                                    viewBinding?.recyclerViewPurchase?.post {
                                        viewModel.deleteProduct(product.productId, previousCartId)
                                    }
                                }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS -> {
                        viewBinding?.recyclerViewPurchase?.post {
                            viewModel.bulkDeleteUnavailableProducts()
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_NOTES -> {
                        it.data?.getSuccessUpdateResultPair()?.let { (_, cartTokoFoodData) ->
                            cartTokoFoodData.carts.firstOrNull()?.let { product ->
                                viewBinding?.recyclerViewPurchase?.post {
                                    viewModel.updateNotes(product)
                                }
                                showToaster(
                                    context?.getString(R.string.text_purchase_success_notes).orEmpty(),
                                    getOkayMessage()
                                )
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
                        it.data?.getSuccessUpdateResultPair()?.let { (updateParams, cartTokoFoodData) ->
                            viewBinding?.recyclerViewPurchase?.post {
                                viewModel.updateCartId(updateParams, cartTokoFoodData)
                            }
                        }
                        viewBinding?.recyclerViewPurchase?.post {
                            viewModel.refreshPartialCartInformation()
                        }
                        showToaster(
                            context?.getString(R.string.text_purchase_success_quantity).orEmpty(),
                            getOkayMessage()
                        )
                    }
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {

                    }
                    UiEvent.EVENT_FAILED_DELETE_PRODUCT -> {
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
                    }
                    UiEvent.EVENT_FAILED_UPDATE_QUANTITY -> {
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
                    }
                    UiEvent.EVENT_FAILED_UPDATE_NOTES -> {
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
                        activityViewModel?.updateQuantity(it, SOURCE)
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
            // TODO: Move to viewmodel for testability
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
            // TODO: Set image url
            errorTitle.text = context?.getString(R.string.text_purchase_no_pinpoint).orEmpty()
            errorDescription.text = context?.getString(R.string.text_purchase_pinpoint_benefit).orEmpty()
            errorAction.text = context?.getString(R.string.text_purchase_set_pinpoint).orEmpty()
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

    private fun navigateToSetPinpoint(locationPass: LocationPass) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle().apply {
            putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_SET_PINPOINT)
    }

    private fun showBulkDeleteConfirmationDialog(productCount: Int) {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.text_purchase_delete_item, productCount.toString()))
                setDescription(getString(R.string.text_purchase_delete_all))
                setPrimaryCTAText(getString(R.string.text_purchase_delete))
                setSecondaryCTAText(getString(R.string.text_purchase_back))
                setPrimaryCTAClickListener {
                    activityViewModel?.deleteUnavailableProducts(SOURCE)
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
            context?.getString(R.string.text_purchase_success_delete, productCount).orEmpty(),
            getOkayMessage()
        )
    }

    private fun navigateToHomePage() {
        TokofoodRouteManager.mapUriToFragment(ApplinkConstInternalTokoFood.HOME.toUri())?.let { homeFragment ->
            navigateToNewFragment(homeFragment)
        }
    }

    private fun navigateToMerchantPage(merchantId: String) {
        val merchantPageUri = Uri.parse(ApplinkConstInternalTokoFood.MERCHANT)
            .buildUpon()
            .appendQueryParameter(DeeplinkMapperTokoFood.PARAM_MERCHANT_ID, merchantId)
            .build()
        TokofoodRouteManager.mapUriToFragment(merchantPageUri)?.let { merchantPageFragment ->
            navigateToNewFragment(merchantPageFragment)
        }
    }

    private fun scrollToIndex(index: Int) {
        activity?.let { activity ->
            getRecyclerView(view)?.layoutManager?.let {
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
                        // TODO: Discuss on what to do when payment failed
                        showDefaultCheckoutGeneralError()
                    }
                }
            }
        }
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val locationPass = it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                locationPass?.let {
                    showLoadingLayout()
                    viewModel.updateAddressPinpoint(locationPass.latitude, locationPass.longitude)
                }
            }
        }
    }

    private fun onResultFromChangeAddress(intent: Intent?) {
        showToaster(
            context?.getString(R.string.text_purchase_success_edit_address).orEmpty(),
            getOkayMessage()
        )
        loadData()
        intent?.getParcelableExtra<ChosenAddressModel>(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA)?.let { chosenAddressModel ->
            val hasPinpoint = chosenAddressModel.latitude.isNotBlank() && chosenAddressModel.longitude.isNotBlank()
            viewModel.setIsHasPinpoint(chosenAddressModel.addressId.toString(), hasPinpoint)
        }
    }

    private fun onResultFromPaymentSuccess() {
        // TODO: Go to home, but elegantly
        navigateToNewFragment(TokoFoodHomeFragment.createInstance())
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

    private fun convertProductListToUpdateParam(productList: List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>): CartTokoFoodParam {
        val cartList = productList.map {
            CartItemTokoFoodParam(
                productId = it.id,
                quantity = it.quantity
            )
        }
        return CartTokoFoodParam(
            carts = cartList
        )
    }

    private fun showConsentBottomSheet(data: CheckoutTokoFoodConsentBottomSheet) {
        if (data.isShowBottomsheet) {
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
                    viewModel.setPaymentButtonLoading(true)
                    viewModel.checkoutGeneral()
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

    private fun showToasterError(errorMessage: String,
                                 actionMessage: String,
                                 onActionClicked: () -> Unit = {}) {
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

    private fun showToaster(message: String,
                            actionMessage: String,
                            onActionClicked: () -> Unit = {}) {
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

    private fun getErrorMessage(throwable: Throwable): String {
        return context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: throwable.message.orEmpty()
    }

    private fun getOkayMessage(): String = context?.getString(R.string.text_purchase_okay).orEmpty()

    private fun showDefaultCheckoutGeneralError(message: String? = null) {
        val errorMessage =
            message ?: context?.getString(R.string.text_purchase_failed_to_payment).orEmpty()
        val actionMessage = context?.getString(R.string.text_purchase_try_again).orEmpty()
        showToasterError(errorMessage, actionMessage) {
            viewModel.setPaymentButtonLoading(true)
            viewModel.checkoutGeneral()
        }
    }

    private fun showToasterFromMetadata(isErrorToaster: Boolean,
                                        errorDetail: CheckoutErrorMetadataDetail) {
        val actionText = errorDetail.actionText.takeIf { it.isNotEmpty() } ?: getOkayMessage()
        val toasterAction: () -> Unit = {
            when(errorDetail.action) {
                CheckoutErrorMetadataDetail.REFRESH_ACTION -> {
                    viewModel.setPaymentButtonLoading(true)
                    viewModel.checkoutGeneral()
                }
                CheckoutErrorMetadataDetail.REDIRECT_ACTION -> {
                    context?.let {
                        RouteManager.route(it, errorDetail.link)
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
            startActivityForResult(intent, REQUEST_CODE_PAYMENT)
        }
    }

    override fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        return viewModel.getNextItems(currentIndex, count)
    }

    override fun onTextChangeShippingAddressClicked() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS).apply {
            putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true)
        }
        startActivityForResult(intent, REQUEST_CODE_CHANGE_ADDRESS)
    }

    override fun onTextSetPinpointClicked() {}

    override fun onTextAddItemClicked() {
        navigateToMerchantPage(shopId)
    }

    override fun onTextBulkDeleteUnavailableProductsClicked() {
        viewModel.validateBulkDelete()
    }

    override fun onQuantityChanged() {
        viewModel.triggerEditQuantity()
    }

    override fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        activityViewModel?.deleteProduct(
            productId = element.id,
            cartId = element.cartId,
            source = SOURCE)
    }

    override fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        val addOnBottomSheet = TokoFoodPurchaseNoteBottomSheet(element.notes,
                object : TokoFoodPurchaseNoteBottomSheet.Listener {
                    override fun onSaveNotesClicked(notes: String) {
                        val updateParam =
                            TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateParam(
                                listOf(element.copy(notes = notes)),
                                shopId
                            )
                        activityViewModel?.updateNotes(updateParam, SOURCE)
                    }
                }
        )
        addOnBottomSheet.show(parentFragmentManager)
    }

    override fun onTextChangeNoteAndVariantClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        val productUiModel = TokoFoodPurchaseUiModelMapper.mapUiModelToCustomizationUiModel(element)
        val orderCustomizationFragment = OrderCustomizationFragment.createInstance(
            productUiModel = productUiModel,
            cartId = element.cartId,
            merchantId = shopId
        )
        navigateToNewFragment(orderCustomizationFragment)
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel.toggleUnavailableProductsAccordion()
    }

    override fun onTextShowUnavailableItemClicked() {
        viewModel.scrollToUnavailableItem()
    }

    override fun onPromoWidgetClicked() {
        navigateToNewFragment(TokoFoodPromoFragment.createInstance())
    }

    override fun onButtonCheckoutClicked() {
        viewModel.checkUserConsent()
    }

    override fun onSurgePriceIconClicked(title: String, desc: String) {
        TokoFoodPurchaseSurgeBottomSheet.createInstance(title, desc).show(childFragmentManager)
    }

    override fun onSuccessAgreeConsent() {
        viewModel.setConsentAgreed()
        viewModel.checkoutGeneral()
    }

    override fun onFailedAgreeConsent(throwable: Throwable) {
        viewModel.setPaymentButtonLoading(false)
        consentBottomSheet?.dismiss()
        showToasterError(throwable)
    }

    companion object {
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        const val REQUEST_CODE_CHANGE_ADDRESS = 111
        const val REQUEST_CODE_SET_PINPOINT = 112
        const val REQUEST_CODE_PAYMENT = 113

        private const val SOURCE = "checkout_page"
        private const val PAGE_NAME = "checkout_page"

        fun createInstance(): TokoFoodPurchaseFragment {
            return TokoFoodPurchaseFragment()
        }
    }

}