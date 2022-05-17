package com.tokopedia.tokofood.purchase.purchasepage.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.TestMerchantFragment
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodConsentBottomSheet
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchaseBinding
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoFragment
import com.tokopedia.tokofood.purchase.purchasepage.di.DaggerTokoFoodPurchaseComponent
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapter
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory
import com.tokopedia.tokofood.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.presentation.subview.TokoFoodPurchaseGlobalErrorBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.presentation.subview.TokoFoodPurchaseNoteBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.presentation.subview.TokoFoodPurchaseSurgeBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbar
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbarListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.unifycomponents.Toaster
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
                //TODO: Set correct value
                viewModel.setIsHasPinpoint(addressData.address_id, true)
//                viewModel.setIsHasPinpoint(addressData.address_id, addressData.latLong.isNotEmpty())
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
                    (it.data as? CheckoutTokoFoodResponse)?.let { response ->
                        activityViewModel?.loadCartList(response)
                        if (response.data.popupMessage.isNotEmpty()) {
                            showToaster(response.data.popupMessage, "Oke") {}
                        }
                        if (response.data.popupErrorMessage.isNotEmpty()) {
                            showToasterError(response.data.popupErrorMessage, "Oke") {}
                        }
                    }
                }
                PurchaseUiEvent.EVENT_NO_PINPOINT -> {
                    hideLoading()
                    renderNoPinpoint()
                }
                PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE -> {
                    hideLoading()
                    renderRecyclerView()
                    it.throwable?.let { throwable ->
                        showToasterError(throwable)
                    }
                }
                PurchaseUiEvent.EVENT_FAILED_LOAD_FIRST_TIME_PURCHASE_PAGE -> {
                    hideLoading()
                    renderRecyclerView()
                    it.throwable?.let { throwable ->
                        renderGlobalError(throwable)
                    }
                }
                PurchaseUiEvent.EVENT_REMOVE_ALL_PRODUCT -> navigateToMerchantPage()
                PurchaseUiEvent.EVENT_SUCCESS_REMOVE_PRODUCT -> onSuccessRemoveProduct(it.data as Int)
                PurchaseUiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS -> scrollToIndex(it.data as Int)
                PurchaseUiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG -> showBulkDeleteConfirmationDialog(it.data as Int)
                PurchaseUiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT -> navigateToSetPinpoint(it.data as LocationPass)
                PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT -> viewModel.loadData()
                PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT -> {
                    // TODO: Show error
                    viewModel.loadData()
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
                    viewModel.setPaymentButtonLoading()
                    // TODO: Go to payment page
                    navigateToNewFragment(TestMerchantFragment.createInstance())
                }
                PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL -> {
                    consentBottomSheet?.dismiss()
                    viewModel.setPaymentButtonLoading()
                    val globalErrorType = it.data as? Int
                    if (globalErrorType == null) {
                        showToasterError(
                            "Oops, gagal lanjut ke Pembayaran.",
                            "Coba Lagi"
                        ) {
                            viewModel.setPaymentButtonLoading(true)
                            viewModel.checkoutGeneral()
                        }
                    } else {
                        showGlobalError(globalErrorType)
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
                                    viewModel.deleteProduct(product.productId, previousCartId)
                                }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS -> {
                        viewModel.bulkDeleteUnavailableProducts()
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_NOTES -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()?.cartId?.let { cartId ->
                                (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                    cartTokoFoodData.carts.firstOrNull()?.let { product ->
                                        if (viewModel.isDebug) {
                                            (pair.first as? UpdateParam)?.productList?.firstOrNull()?.notes?.let { notes ->
                                                viewModel.updateNotesDebug(product, cartId, notes)
                                            }
                                        } else {
                                            viewModel.updateNotes(product, cartId)
                                        }
                                        view?.let {
                                            Toaster.build(
                                                view = it,
                                                text = "Sip! Catatan berhasil disimpan.",
                                                duration = Toaster.LENGTH_SHORT,
                                                type = Toaster.TYPE_NORMAL,
                                                actionText = "Oke"
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
                        // TODO: update cart id
                        viewModel.refreshPartialCartInformation()
                        view?.let {
                            Toaster.build(
                                view = it,
                                text = "Quantity berhasil diubah",
                                duration = Toaster.LENGTH_SHORT,
                                type = Toaster.TYPE_NORMAL,
                                actionText = "Oke"
                            ).show()
                        }
                    }
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {

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
                loadInitialData()
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
            errorTitle.text = "Kamu belum set pinpoint lokasi"
            errorDescription.text = "Untuk meningkatkan pengalaman  belanja kamu, set pinpoint terlebih dahulu, ya!"
            errorAction.text = "Set pin point"
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
                setTitle("Hapus $productCount item yang tidak bisa diproses?")
                setDescription("Semua item ini akan dihapus dari keranjangmu.")
                setPrimaryCTAText("Hapus")
                setSecondaryCTAText("Kembali")
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
        view?.let {
            Toaster.build(
                    view = it,
                    text = "$productCount item berhasil dihapus.",
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_NORMAL,
                    actionText = "Oke"
            ).show()
        }
    }

    private fun navigateToMerchantPage() {
        // Todo : navigate to merchant page
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
            REQUEST_CODE_CHANGE_ADDRESS -> onResultFromChangeAddress()
            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
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

    private fun onResultFromChangeAddress() {
        loadData()
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
                    this@TokoFoodPurchaseFragment.viewModel.setPaymentButtonLoading()
                }
            }
            consentBottomSheet?.show(childFragmentManager)
        }
    }

    private fun showGlobalError(globalErrorType: Int) {
        // Todo : hit checkout API, validate response, show global error / toaster, reload
        val bottomSheet = TokoFoodPurchaseGlobalErrorBottomSheet.createInstance(
            globalErrorType = globalErrorType,
            listener = object : TokoFoodPurchaseGlobalErrorBottomSheet.Listener {
                override fun onGoToHome() {

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
        )
        bottomSheet.show(parentFragmentManager)
    }

    private fun showToasterError(errorMessage: String,
                                 actionMessage: String,
                                 onActionClicked: () -> Unit) {
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
                            onActionClicked: () -> Unit) {
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
                actionText = "Oke"
            ).show()
        }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: throwable.message.orEmpty()
    }

    override fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        return viewModel.getNextItems(currentIndex, count)
    }

    override fun onTextChangeShippingAddressClicked() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        startActivityForResult(intent, REQUEST_CODE_CHANGE_ADDRESS)
    }

    override fun onTextSetPinpointClicked() {}

    override fun onTextAddItemClicked() {
        // Todo : navigate to merchant page
        navigateToNewFragment(TestMerchantFragment.createInstance())
    }

    override fun onTextBulkDeleteUnavailableProductsClicked() {
        viewModel.validateBulkDelete()
    }

    override fun onQuantityChanged() {
        viewModel.triggerEditQuantity()
    }

    override fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        activityViewModel?.deleteProduct(element.id, element.cartId, SOURCE)
    }

    override fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        val addOnBottomSheet = TokoFoodPurchaseNoteBottomSheet(element.notes,
                object : TokoFoodPurchaseNoteBottomSheet.Listener {
                    override fun onSaveNotesClicked(notes: String) {
                        val updateParam =
                            TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateParam(
                                listOf(element.copy(notes = notes))
                            )
                        activityViewModel?.updateNotes(updateParam, SOURCE)
                    }
                }
        )
        addOnBottomSheet.show(parentFragmentManager, "")
    }

    override fun onTextChangeNoteAndVariantClicked() {
        // Todo : navigate to edit variant page
        view?.let {
            Toaster.build(it, "onTextChangeNoteAndVariantClicked", Toaster.LENGTH_SHORT).show()
        }
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
        viewModel.setPaymentButtonLoading()
        consentBottomSheet?.dismiss()
        showToasterError(throwable)
    }

    companion object {
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        const val REQUEST_CODE_CHANGE_ADDRESS = 111
        const val REQUEST_CODE_SET_PINPOINT = 112

        private const val SOURCE = "checkout_page"

        fun createInstance(): TokoFoodPurchaseFragment {
            return TokoFoodPurchaseFragment()
        }
    }

}