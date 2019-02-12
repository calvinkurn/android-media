package com.tokopedia.expresscheckout.view.variant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets.Companion.RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets.Companion.RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheetsActionListener
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheetsActionListenerWithRetry
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.domain.model.atc.WholesalePriceModel
import com.tokopedia.expresscheckout.router.ExpressCheckoutRouter
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileBottomSheet
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileFragmentListener
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.di.DaggerCheckoutVariantComponent
import com.tokopedia.expresscheckout.view.variant.util.isOnboardingStateHasNotShown
import com.tokopedia.expresscheckout.view.variant.util.setOnboardingStateHasNotShown
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.payment.activity.TopPayActivity
import com.tokopedia.payment.model.PaymentPassData
import com.tokopedia.shipping_recommendation.domain.shipping.*
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheetListener
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.transactionanalytics.ConstantTransactionAnalytics
import com.tokopedia.transactionanalytics.ExpressCheckoutAnalyticsTracker
import com.tokopedia.transactionanalytics.data.expresscheckout.ActionField
import com.tokopedia.transactionanalytics.data.expresscheckout.Checkout
import com.tokopedia.transactionanalytics.data.expresscheckout.Product
import com.tokopedia.transactiondata.entity.request.CheckoutRequest
import kotlinx.android.synthetic.main.fragment_detail_product_page.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantFragment : BaseListFragment<Visitable<*>, CheckoutVariantAdapterTypeFactory>(),
        CheckoutVariantContract.View, CheckoutVariantActionListener, CheckoutProfileFragmentListener,
        ShippingDurationBottomsheetListener, ShippingCourierBottomsheetListener {

    @Inject
    lateinit var presenter: CheckoutVariantContract.Presenter
    @Inject
    lateinit var itemDecorator: CheckoutVariantItemDecorator
    @Inject
    lateinit var tkpdProgressDialog: TkpdProgressDialog
    @Inject
    lateinit var fragmentViewModel: FragmentViewModel
    @Inject
    lateinit var compositeSubscription: CompositeSubscription
    @Inject
    lateinit var checkoutProfileBottomSheet: CheckoutProfileBottomSheet
    @Inject
    lateinit var shippingDurationBottomsheet: ShippingDurationBottomsheet
    @Inject
    lateinit var shippingCourierBottomsheet: ShippingCourierBottomsheet
    @Inject
    lateinit var errorBottomsheets: ErrorBottomsheets
    @Inject
    lateinit var analyticsTracker: ExpressCheckoutAnalyticsTracker

    private lateinit var router: ExpressCheckoutRouter
    private lateinit var adapter: CheckoutVariantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentListener: CheckoutVariantFragmentListener
    private lateinit var reloadRatesDebounceListener: ReloadRatesDebounceListener

    private var isDataLoaded = false

    companion object {
        const val REQUEST_CODE_GEOLOCATION = 63

        const val ARGUMENT_ATC_REQUEST = "ARGUMENT_ATC_REQUEST"

        fun createInstance(atcRequestParam: AtcRequestParam): CheckoutVariantFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGUMENT_ATC_REQUEST, atcRequestParam)
            val fragment = CheckoutVariantFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerCheckoutVariantComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product_page, container, false)

        recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(itemDecorator)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        initUpdateShippingRatesDebouncer()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        shippingDurationBottomsheet.setShippingDurationBottomsheetListener(this)
        shippingCourierBottomsheet.setShippingCourierBottomsheetListener(this)
        checkoutProfileBottomSheet.setListener(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentListener = context as CheckoutVariantFragmentListener
        router = context.applicationContext as ExpressCheckoutRouter
    }

    override fun onDetach() {
        compositeSubscription.unsubscribe()
        super.onDetach()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypeFactory> {
        adapter = CheckoutVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getActivityContext(): Context? {
        return activity
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showLoadingDialog() {
        tkpdProgressDialog.showDialog()
    }

    override fun hideLoadingDialog() {
        tkpdProgressDialog.dismiss()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun getAdapterTypeFactory(): CheckoutVariantAdapterTypeFactory {
        return CheckoutVariantAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun onNeedToNotifySingleItem(position: Int) {
        if (recyclerView.isComputingLayout) {
            recyclerView.post {
                adapter.notifyItemChanged(position)
            }
        } else {
            adapter.notifyItemChanged(position)
        }
    }

    override fun onNeedToRemoveSingleItem(position: Int) {
        if (recyclerView.isComputingLayout) {
            recyclerView.post {
                adapter.notifyItemRemoved(position)
            }
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onNeedToNotifyAllItem() {
        if (recyclerView.isComputingLayout) {
            recyclerView.post {
                adapter.notifyDataSetChanged()
            }
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClickEditProfile() {
        if (!checkoutProfileBottomSheet.isAdded) {
            checkoutProfileBottomSheet.updateArguments(fragmentViewModel.getProfileViewModel())
            checkoutProfileBottomSheet.show(activity?.supportFragmentManager, "")
        }
    }

    override fun onClickEditDuration() {
        showDurationOptions()
    }

    private fun showDurationOptions() {
        val shippingParam = presenter.getShippingParam(fragmentViewModel.getQuantityViewModel()?.orderQuantity
                ?: 0, fragmentViewModel.getProductViewModel()?.productPrice?.toLong() ?: 0)
        val shopShipmentList = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels
        val selectedServiceId = fragmentViewModel.getProfileViewModel()?.shippingDurationId
        shippingDurationBottomsheet.updateArguments(shippingParam, selectedServiceId
                ?: 0, -1, true, shopShipmentList)
        if (!shippingDurationBottomsheet.isAdded) {
            shippingDurationBottomsheet.show(activity?.supportFragmentManager, "")
        }
    }

    override fun onClickEditCourier() {
        shippingCourierBottomsheet.updateArguments(fragmentViewModel.shippingCourierViewModels)
        if (!shippingCourierBottomsheet.isAdded) {
            shippingCourierBottomsheet.show(activity?.supportFragmentManager, "")
        }
    }

    override fun onClickInsuranceInfo(insuranceInfo: String) {
        if (activity != null) {
            val tooltip = Tooltip(activity as Context)
            tooltip.setTitle(activity?.getString(R.string.title_bottomsheet_insurance))
            tooltip.setDesc(insuranceInfo)
            tooltip.setTextButton(activity?.getString(R.string.label_button_bottomsheet_close))
            tooltip.setIcon(R.drawable.ic_insurance)
            tooltip.btnAction.setOnClickListener {
                tooltip.dismiss()
            }
            tooltip.show()
        }
    }

    override fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel) {
        val productViewModel = fragmentViewModel.getProductViewModel()
        val summaryViewModel = fragmentViewModel.getSummaryViewModel()
        val quantityViewModel = fragmentViewModel.getQuantityViewModel()

        if (productViewModel != null && productViewModel.productChildrenList.isNotEmpty()) {
            var selectedKey = 0
            for ((key, value) in productViewModel.selectedVariantOptionsIdMap) {
                if (key == selectedOptionViewModel.variantId && value != selectedOptionViewModel.optionId) {
                    selectedKey = key
                }
            }
            if (selectedKey != 0) {
                productViewModel.selectedVariantOptionsIdMap[selectedKey] = selectedOptionViewModel.optionId
            }

            // Check is product child for selected variant is available
            var newSelectedProductChild: ProductChild? = null
            for (productChild: ProductChild in productViewModel.productChildrenList) {
                var matchOptionId = 0
                for ((_, value) in productViewModel.selectedVariantOptionsIdMap) {
                    if (value in productChild.optionsId) {
                        matchOptionId++
                    }
                }
                if (matchOptionId == productViewModel.selectedVariantOptionsIdMap.size) {
                    newSelectedProductChild = productChild
                    break
                }
            }

            if (newSelectedProductChild != null) {
                for (productChild: ProductChild in productViewModel.productChildrenList) {
                    productChild.isSelected = productChild.productId == newSelectedProductChild.productId
                }
                onNeedToNotifySingleItem(fragmentViewModel.getIndex(productViewModel))

                if (summaryViewModel != null) {
                    summaryViewModel.itemPrice = quantityViewModel?.orderQuantity?.times(newSelectedProductChild.productPrice.toLong()) ?: 0
                    onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
                }

                val variantTypeViewModels = fragmentViewModel.getVariantTypeViewModel()
                for (variantTypeViewModel: TypeVariantViewModel in variantTypeViewModels) {
                    if (variantTypeViewModel.variantId == selectedOptionViewModel.variantId) {
                        variantTypeViewModel.variantSelectedValue = selectedOptionViewModel.variantName
                        onNeedToNotifySingleItem(fragmentViewModel.getIndex(variantTypeViewModel))
                        break
                    }
                }

                for (variantTypeViewModel: TypeVariantViewModel in variantTypeViewModels) {
                    if (variantTypeViewModel.variantId != selectedOptionViewModel.variantId) {
                        for (optionViewModel: OptionVariantViewModel in variantTypeViewModel.variantOptions) {

                            // Get other variant type selected option id
                            val otherVariantSelectedOptionIds = ArrayList<Int>()
                            for (otherVariantViewModel: TypeVariantViewModel in variantTypeViewModels) {
                                if (otherVariantViewModel.variantId != variantTypeViewModel.variantId &&
                                        otherVariantViewModel.variantId != selectedOptionViewModel.variantId) {
                                    for (otherVariantTypeOption: OptionVariantViewModel in otherVariantViewModel.variantOptions) {
                                        if (otherVariantTypeOption.currentState == otherVariantTypeOption.STATE_SELECTED) {
                                            otherVariantSelectedOptionIds.add(otherVariantTypeOption.optionId)
                                            break
                                        }
                                    }
                                }
                            }

                            // Look for available child
                            var hasAvailableChild = false
                            for (productChild: ProductChild in productViewModel.productChildrenList) {
                                hasAvailableChild = checkChildAvailable(productChild, optionViewModel.optionId, selectedOptionViewModel.optionId, otherVariantSelectedOptionIds)
                                if (hasAvailableChild) break
                            }

                            // Set option id state with checking result
                            if (!hasAvailableChild) {
                                optionViewModel.hasAvailableChild = false
                                optionViewModel.currentState = optionViewModel.STATE_NOT_AVAILABLE
                            } else if (optionViewModel.currentState != optionViewModel.STATE_SELECTED) {
                                optionViewModel.hasAvailableChild = true
                                optionViewModel.currentState = optionViewModel.STATE_NOT_SELECTED
                            }
                        }
                        onNeedToNotifySingleItem(fragmentViewModel.getIndex(variantTypeViewModel))
                    }
                }
            }

            reloadRatesDebounceListener.onNeedToRecalculateRates(false)
            fragmentViewModel.isStateChanged = true
        }
    }

    private fun checkChildAvailable(productChild: ProductChild,
                                    optionViewModelId: Int,
                                    currentChangedOptionId: Int,
                                    otherVariantSelectedOptionIds: ArrayList<Int>): Boolean {

        // Check is child with newly selected option id, other variant selected option ids,
        // and current looping variant option id is available
        var otherSelectedOptionIdCount = 0
        for (optionId: Int in otherVariantSelectedOptionIds) {
            if (optionId in productChild.optionsId) {
                otherSelectedOptionIdCount++
            }
        }

        val otherSelectedOptionIdCountEqual = otherSelectedOptionIdCount == otherVariantSelectedOptionIds.size
        val currentChangedOptionIdAvailable = currentChangedOptionId in productChild.optionsId
        val optionViewModelIdAvailable = optionViewModelId in productChild.optionsId

        return productChild.isAvailable && currentChangedOptionIdAvailable && optionViewModelIdAvailable && otherSelectedOptionIdCountEqual
    }

    override fun onChangeQuantity(quantityViewModel: QuantityViewModel) {
        val productViewModel = fragmentViewModel.getProductViewModel()
        val summaryViewModel = fragmentViewModel.getSummaryViewModel()

        if (fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.wholesalePriceModel?.isNotEmpty() == true) {
            val wholesalePriceModels = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.wholesalePriceModel?.asReversed()
            if (wholesalePriceModels != null) {
                for (wholesalePriceModel: WholesalePriceModel in wholesalePriceModels) {
                    if (quantityViewModel.orderQuantity >= wholesalePriceModel.qtyMax ||
                            (quantityViewModel.orderQuantity < wholesalePriceModel.qtyMax &&
                                    quantityViewModel.orderQuantity >= wholesalePriceModel.qtyMin)) {
                        productViewModel?.productPrice = wholesalePriceModel.prdPrc
                        break
                    }
                }
            }
        }

        if (productViewModel?.productChildrenList != null && productViewModel.productChildrenList.size > 0) {
            for (productChild: ProductChild in productViewModel.productChildrenList) {
                if (productChild.isSelected) {
                    summaryViewModel?.itemPrice = productChild.productPrice.toLong() * quantityViewModel.orderQuantity
                    break
                }
            }
        } else {
            summaryViewModel?.itemPrice = productViewModel?.productPrice?.toLong()?.times(quantityViewModel.orderQuantity) ?: 0
        }

        if (summaryViewModel != null) {
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
            onSummaryChanged(summaryViewModel)
        }

        onNeedToNotifySingleItem(fragmentViewModel.getIndex(quantityViewModel))
        reloadRatesDebounceListener.onNeedToRecalculateRates(false)
        fragmentViewModel.isStateChanged = true
    }

    override fun onChangeNote(noteViewModel: NoteViewModel) {
        if (fragmentViewModel.isStateChanged == false && noteViewModel.note.isNotEmpty()) {
            fragmentViewModel.isStateChanged = true
        }
    }

    override fun onSummaryChanged(summaryViewModel: SummaryViewModel?) {
        val totalPayment = summaryViewModel?.itemPrice?.plus(summaryViewModel.shippingPrice)?.plus(summaryViewModel.servicePrice)?.plus(summaryViewModel.insurancePrice)
        fragmentViewModel.totalPayment = totalPayment

        tv_total_payment_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentViewModel.totalPayment
                ?: 0, false)
    }

    override fun onInsuranceCheckChanged(insuranceViewModel: InsuranceViewModel) {
        val summaryViewModel = fragmentViewModel.getSummaryViewModel()
        if (summaryViewModel != null) {
            if (insuranceViewModel.isChecked) {
                summaryViewModel.insurancePrice = insuranceViewModel.insurancePrice
                summaryViewModel.isUseInsurance = true
            } else {
                summaryViewModel.insurancePrice = 0
                summaryViewModel.isUseInsurance = false
            }

            onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
        }
        onNeedToNotifySingleItem(fragmentViewModel.getIndex(insuranceViewModel))
        fragmentViewModel.isStateChanged = true
    }

    override fun onNeedToValidateButtonBuyVisibility() {
        var hasError = false
        when {
            fragmentViewModel.getProfileViewModel()?.isDurationError == true -> hasError = true
            fragmentViewModel.getProfileViewModel()?.isCourierError == true -> hasError = true
            fragmentViewModel.getQuantityViewModel()?.isStateError == true -> hasError = true
        }

        if (activity != null) {
            if (hasError) {
                bt_buy.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_disabled)
                bt_buy.setOnClickListener { }
            } else {
                bt_buy.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_orange_enabled)
                bt_buy.setOnClickListener { presenter.checkoutExpress(fragmentViewModel) }
            }
        }
    }

    override fun onNeedToRecalculateRatesAfterChangeTemplate() {
        fragmentViewModel.getProfileViewModel()?.isStateHasChangedProfile = false
        reloadRatesDebounceListener.onNeedToRecalculateRates(true)
    }

    override fun onNeedToUpdateOnboardingStatus() {
        setOnboardingStateHasNotShown(activity, false)
    }

    override fun onGetCompositeSubscriber(): CompositeSubscription {
        return compositeSubscription
    }

    override fun onBindProductUpdateQuantityViewModel(stockWording: String) {
        val quantityViewModel = fragmentViewModel.getQuantityViewModel()
        if (quantityViewModel != null) {
            quantityViewModel.stockWording = stockWording
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(quantityViewModel))
        }
    }

    override fun onBindVariantGetProductViewModel(): ProductViewModel? {
        return fragmentViewModel.getProductViewModel()
    }

    override fun onBindVariantUpdateProductViewModel() {
        val productViewModel = fragmentViewModel.getProductViewModel()
        if (productViewModel != null) {
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(productViewModel))
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun loadData(page: Int) {
        if (!isDataLoaded) {
            presenter.loadExpressCheckoutData(arguments?.get(ARGUMENT_ATC_REQUEST) as AtcRequestParam)
        }
    }

    override fun showToasterError(message: String?) {
        ToasterError.make(view, message
                ?: activity?.getString(R.string.default_request_error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun finishWithError(messages: String) {
        fragmentListener.finishWithResult(messages)
    }

    override fun navigateAtcToOcs() {
        fragmentListener.navigateAtcToOcs()
    }

    override fun navigateAtcToNcf() {
        fragmentListener.navigateAtcToNcf()
    }

    override fun navigateCheckoutToOcs() {
        if (activity != null) startActivity(router.getCheckoutIntent(activity as Context))
        activity?.finish()
    }

    override fun navigateCheckoutToPayment(paymentPassData: PaymentPassData) {
        if (activity != null) startActivityForResult(
                TopPayActivity.createInstance(activity, paymentPassData),
                TopPayActivity.REQUEST_CODE)
        activity?.finish()
    }

    override fun navigateCheckoutToThankYouPage(appLink: String) {
        var eventLabel = if (fragmentViewModel.getProfileViewModel()?.isStateHasChangedProfile == false) {
            ConstantTransactionAnalytics.EventLabel.SUCCESS_DEFAULT
        } else {
            ConstantTransactionAnalytics.EventLabel.SUCCESS_NOT_DEFAULT
        }
        analyticsTracker.enhanceEcommerceImpressionExpressCheckoutForm(generateEnhanceEcommerceData(ActionField.STEP_2), eventLabel)
        if (activity != null) startActivity(RouteManager.getIntent(activity, appLink))
        activity?.finish()
    }

    override fun getAddToCartObservable(addToCartRequest: AddToCartRequest): Observable<AddToCartResult> {
        return router.addToCartProduct(addToCartRequest, true)
    }

    override fun getCheckoutObservable(checkoutRequest: CheckoutRequest): Observable<CheckoutData> {
        return router.getCheckoutObservable(checkoutRequest, true, true)
    }

    override fun showBottomSheetError(title: String, message: String, action: String, enableRetry: Boolean) {
        errorBottomsheets.setData(title, message, action, enableRetry)
        if (errorBottomsheets.isVisible) {
            errorBottomsheets.dismiss()
        }
        errorBottomsheets.show(fragmentManager, title)
        fragmentViewModel.isStateChanged = true
    }

    override fun showErrorCourier(message: String) {
        showBottomSheetError(getString(R.string.bottomsheet_title_choose_other_method), message, getString(R.string.bottomsheet_action_choose_other_courier), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
                showDurationOptions()
            }
        }
        analyticsTracker.eventClickBuyAndError(message)
        fragmentViewModel.isStateChanged = true
    }

    override fun showErrorNotAvailable(message: String) {
        showBottomSheetError(getString(R.string.bottomsheet_title_product_not_available), message, getString(R.string.bottomsheet_action_close), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
            }
        }
        analyticsTracker.eventClickBuyAndError(message)
        fragmentViewModel.isStateChanged = true
    }

    override fun showErrorAPI(retryAction: String) {
        val message = getString(R.string.bottomsheet_message_global_error)
        showBottomSheetError(getString(R.string.bottomsheet_title_global_error), message, getString(R.string.bottomsheet_action_global_error), true)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListenerWithRetry {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
                presenter.checkoutOneClickShipment(fragmentViewModel)
            }

            override fun onRetryClicked() {
                errorBottomsheets.dismiss()
                when (retryAction) {
                    RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT -> {
                        presenter.checkoutExpress(fragmentViewModel)
                    }
                    RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT -> {
                        presenter.hitOldCheckout(fragmentViewModel)
                    }
                }
            }
        }
        analyticsTracker.eventClickBuyAndError(message)
        fragmentViewModel.isStateChanged = true
    }

    override fun showErrorPayment(message: String) {
        showBottomSheetError(getString(R.string.bottomsheet_title_change_payment_method), message, getString(R.string.bottomsheet_action_choose_payment_method), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
                presenter.hitOldCheckout(fragmentViewModel)
                analyticsTracker.clickPilihMetodePembayaran(fragmentViewModel.getProfileViewModel()?.paymentDetail ?: "")
            }
        }
        analyticsTracker.eventClickBuyAndError(message)
        analyticsTracker.viewErrorMetodePembayaran()
        fragmentViewModel.isStateChanged = true
    }

    private fun showErrorPinpoint(productData: ProductData?, profileViewModel: com.tokopedia.expresscheckout.view.variant.viewmodel.ProfileViewModel) {
        showBottomSheetError(getString(R.string.bottomsheet_title_no_pinpoint), productData?.error?.errorMessage
                ?: "", getString(R.string.bottomsheet_action_pin_location), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
                val locationPass = LocationPass()
                locationPass.districtName = profileViewModel.districtName
                locationPass.cityName = profileViewModel.cityName
                if (activity != null) startActivityForResult(router.getGeolocationIntent(activity as Context, locationPass), REQUEST_CODE_GEOLOCATION)
            }
        }
        fragmentViewModel.isStateChanged = true
    }

    override fun updateFragmentViewModel(atcResponseModel: AtcResponseModel) {
        fragmentViewModel.atcResponseModel = atcResponseModel
    }

    override fun showData(viewModels: ArrayList<Visitable<*>>) {
        for (viewModel: Visitable<*> in viewModels) {
            if (viewModel is com.tokopedia.expresscheckout.view.variant.viewmodel.ProfileViewModel) {
                viewModel.isFirstTimeShowProfile = isOnboardingStateHasNotShown(activity)
                break
            }
        }
        fragmentViewModel.viewModels = viewModels
        adapter.clearAllElements()
        adapter.addDataViewModel(viewModels)
        adapter.notifyDataSetChanged()

        onSummaryChanged(fragmentViewModel.getSummaryViewModel())

        rl_bottom_action_container.visibility = View.VISIBLE
        img_total_payment_info.setOnClickListener {
            recyclerView.smoothScrollToPosition(adapter.data.size - 1)
        }

        analyticsTracker.sendScreenName(activity, ConstantTransactionAnalytics.ScreenName.EXPRESS_CHECKOUT)
        analyticsTracker.enhanceEcommerceImpressionExpressCheckoutForm(generateEnhanceEcommerceData(ActionField.STEP_1), "")
    }

    fun generateEnhanceEcommerceData(step: Int): HashMap<String, JsonObject> {
        val enhanceEcommerceData = HashMap<String, JsonObject>()

        val actionField = ActionField()
        actionField.step = if (step == ActionField.STEP_1) ActionField.STEP_1 else ActionField.STEP_2
        actionField.option = if (step == ActionField.STEP_1) ActionField.OPTION_CLICK_CHECKOUT else ActionField.OPTION_CLICK_BAYAR

        val product = Product()
        val shopModel = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel
        val productChildren = fragmentViewModel.getProductViewModel()?.productChildrenList
        if (productChildren != null && productChildren.isNotEmpty()) {
            for (productChild: ProductChild in productChildren) {
                if (productChild.isSelected) {
                    product.name = productChild.productName
                    product.id = productChild.productId
                    product.price = productChild.productPrice
                    break
                }
            }
        } else {
            product.name = fragmentViewModel.getProductViewModel()?.productName
            product.id = fragmentViewModel.getProductViewModel()?.parentId
            product.price = fragmentViewModel.getProductViewModel()?.productPrice
        }

        if (shopModel?.isOfficial == 1) {
            product.shopType = Product.SHOP_TYPE_OFFICIAL_STORE
        } else if (shopModel?.isGold == 1) {
            product.shopType = Product.SHOP_TYPE_GOLD_MERCHANT
        } else {
            product.shopType = Product.SHOP_TYPE_REGULER
        }

        product.quantity = fragmentViewModel.getQuantityViewModel()?.orderQuantity
        product.shopId = shopModel?.shopId
        product.shopName = shopModel?.shopName

        val checkout = Checkout()
        checkout.actionField = actionField
        checkout.products = arrayListOf(product)

        val jsonTreeEnhanceEcommerceData = Gson().toJsonTree(checkout)
        val jsonObjectEnhanceEcommerceData = jsonTreeEnhanceEcommerceData.asJsonObject
        enhanceEcommerceData.put("checkout", jsonObjectEnhanceEcommerceData)

        return enhanceEcommerceData
    }

    override fun setShippingDurationError(message: String) {
        val profileViewModel = fragmentViewModel.getProfileViewModel()
        if (profileViewModel != null) {
            profileViewModel.isDurationError = true
            profileViewModel.durationErrorMessage = message
            profileViewModel.shippingCourierId = 0
            profileViewModel.shippingDurationId = 0
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(profileViewModel))
        }
        fragmentViewModel.isStateChanged = true
    }

    override fun setShippingCourierError(message: String) {
        val profileViewModel = fragmentViewModel.getProfileViewModel()
        if (profileViewModel != null) {
            profileViewModel.isCourierError = true
            profileViewModel.courierErrorMessage = message
            profileViewModel.shippingCourierId = 0
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(profileViewModel))
        }
        fragmentViewModel.isStateChanged = true
    }

    override fun updateShippingData(productData: ProductData, serviceData: ServiceData, shippingCourierViewModels: MutableList<ShippingCourierViewModel>?) {
        if (shippingCourierViewModels != null) {
            fragmentViewModel.shippingCourierViewModels = shippingCourierViewModels
        }

        val profileViewModel = fragmentViewModel.getProfileViewModel()
        val insuranceViewModel = fragmentViewModel.getInsuranceViewModel()
        val summaryViewModel = fragmentViewModel.getSummaryViewModel()

        val shopShipmentList = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels
        if (shopShipmentList != null) {
            for (shopShipment: ShopShipment in shopShipmentList) {
                var foundData = false
                for (shipProd: ShipProd in shopShipment.shipProds) {
                    if (shipProd.shipProdId == productData.shipperProductId && summaryViewModel != null) {
                        summaryViewModel.servicePrice = shipProd.additionalFee
                        foundData = true
                        break
                    }
                }
                if (foundData) break
            }
        }

        if (profileViewModel != null) {
            if (productData.error != null && productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                showErrorPinpoint(productData, profileViewModel)
            } else {
                profileViewModel.isCourierError = productData.error.errorMessage.isNotEmpty()
                profileViewModel.isDurationError = false
                profileViewModel.shippingCourier = productData.shipperName
                profileViewModel.shippingCourierId = productData.shipperProductId
                profileViewModel.shippingDuration = serviceData.serviceName
                profileViewModel.shippingDurationId = serviceData.serviceId
                onNeedToNotifySingleItem(fragmentViewModel.getIndex(profileViewModel))
            }
        }

        if (insuranceViewModel != null) {
            if (productData.insurance.insuranceType != InsuranceConstant.INSURANCE_TYPE_NO) {
                insuranceViewModel.insuranceLongInfo = productData.insurance.insuranceUsedInfo
                insuranceViewModel.insurancePrice = productData.insurance.insurancePrice
                insuranceViewModel.insuranceType = productData.insurance.insuranceType
                insuranceViewModel.insuranceUsedDefault = productData.insurance.insuranceUsedDefault
                insuranceViewModel.shippingId = productData.shipperId
                insuranceViewModel.spId = productData.shipperProductId
                insuranceViewModel.isChecked = insuranceViewModel.isChecked ||
                        productData.insurance.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES ||
                        productData.insurance.insuranceType == InsuranceConstant.INSURANCE_TYPE_MUST
                insuranceViewModel.isVisible = true
                onNeedToNotifySingleItem(fragmentViewModel.getIndex(insuranceViewModel))

                if (summaryViewModel != null) {
                    summaryViewModel.isUseInsurance = insuranceViewModel.isChecked
                    summaryViewModel.shippingPrice = productData.price.price
                    summaryViewModel.insurancePrice = if (insuranceViewModel.isChecked) productData.insurance.insurancePrice else 0
                    summaryViewModel.insuranceInfo = productData.insurance.insuranceUsedInfo
                    onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
                }
            } else {
                insuranceViewModel.isChecked = false
                insuranceViewModel.isVisible = false
                onNeedToRemoveSingleItem(fragmentViewModel.getIndex(insuranceViewModel))

                if (summaryViewModel != null) {
                    summaryViewModel.isUseInsurance = false
                    summaryViewModel.shippingPrice = productData.price.price
                    summaryViewModel.insurancePrice = 0
                    summaryViewModel.insuranceInfo = ""
                    onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
                }
            }
        }
    }

    override fun onShippingDurationChoosen(shippingCourierViewModels: MutableList<ShippingCourierViewModel>?,
                                           courierItemData: CourierItemData?,
                                           recipientAddressModel: RecipientAddressModel?,
                                           cartPosition: Int,
                                           selectedServiceId: Int,
                                           selectedServiceName: String,
                                           flagNeedToSetPinpoint: Boolean,
                                           hasCourierPromo: Boolean) {
        if (shippingCourierViewModels != null) {
            fragmentViewModel.shippingCourierViewModels = shippingCourierViewModels
            for (shippingCourierViewModel: ShippingCourierViewModel in shippingCourierViewModels) {
                if (shippingCourierViewModel.productData.isRecommend) {
                    updateShippingData(shippingCourierViewModel.productData, shippingCourierViewModel.serviceData, shippingCourierViewModels)
                    break
                }
            }
        }
    }

    override fun onNoCourierAvailable(message: String?) {

    }

    override fun onShippingDurationButtonCloseClicked() {
        shippingDurationBottomsheet.dismiss()
    }

    override fun onShippingDurationButtonShowCaseDoneClicked() {

    }

    override fun onShowDurationListWithCourierPromo(isCourierPromo: Boolean, duration: String?) {

    }

    override fun onCourierChoosen(shippingCourierViewModel: ShippingCourierViewModel, courierItemData: CourierItemData?,
                                  recipientAddressModel: RecipientAddressModel?, cartPosition: Int, hasCourierPromo: Boolean,
                                  isPromoCourier: Boolean, isNeedPinpoint: Boolean) {
        updateShippingData(shippingCourierViewModel.productData, shippingCourierViewModel.serviceData, null)
    }

    override fun onCourierShipmentRecpmmendationCloseClicked() {
        shippingCourierBottomsheet.dismiss()
    }

    override fun onRetryReloadCourier(shipmentCartItemModel: ShipmentCartItemModel?, cartPosition: Int, shopShipmentList: MutableList<ShopShipment>?) {

    }

    override fun onContinueWithoutProfile() {
        val profileViewModel = fragmentViewModel.getProfileViewModel()
        if (profileViewModel != null) {
            profileViewModel.isStateHasRemovedProfile = true
            profileViewModel.isSelected = false
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(profileViewModel))
        }
        checkoutProfileBottomSheet.dismiss()
        analyticsTracker.eventClickContinueWithoutTemplate(fragmentViewModel.isStateChanged == false)
    }

    override fun onProfileChanged(selectedProfileViewModel: ProfileViewModel) {
        val currentProfileViewModel = fragmentViewModel.getProfileViewModel()
        if (currentProfileViewModel != null) {
            currentProfileViewModel.addressId = selectedProfileViewModel.addressId
            currentProfileViewModel.addressDetail = selectedProfileViewModel.addressDetail
            currentProfileViewModel.addressTitle = selectedProfileViewModel.addressTitle
            currentProfileViewModel.cityName = selectedProfileViewModel.cityName
            currentProfileViewModel.districtName = selectedProfileViewModel.districtName
            currentProfileViewModel.isStateHasRemovedProfile = false
            currentProfileViewModel.isSelected = true
            currentProfileViewModel.isDurationError = false
            currentProfileViewModel.isCourierError = false
            currentProfileViewModel.isStateHasChangedProfile = true
            currentProfileViewModel.isShowDefaultProfileCheckBox =
                    selectedProfileViewModel.profileId != currentProfileViewModel.profileId
            currentProfileViewModel.isEditable = false
            currentProfileViewModel.isDefaultProfileCheckboxChecked = false
            currentProfileViewModel.paymentDetail = selectedProfileViewModel.paymentDetail
            currentProfileViewModel.paymentOptionImageUrl = selectedProfileViewModel.paymentImageUrl
            currentProfileViewModel.shippingDurationId = selectedProfileViewModel.durationId
            currentProfileViewModel.shippingDuration = selectedProfileViewModel.durationDetail

            onNeedToNotifySingleItem(fragmentViewModel.getIndex(currentProfileViewModel))
        }

        checkoutProfileBottomSheet.dismiss()
    }

    override fun onChangeTemplateBottomshictButtonCloseClicked() {
        analyticsTracker.eventClickButtonX()
    }

    private fun initUpdateShippingRatesDebouncer() {
        compositeSubscription.add(Observable.create(Observable.OnSubscribe<Boolean> { subscriber ->
            reloadRatesDebounceListener = object : ReloadRatesDebounceListener {
                override fun onNeedToRecalculateRates(forceReload: Boolean) {
                    subscriber.onNext(forceReload)
                }
            }
        }).debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Boolean>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(forceReload: Boolean) {
                        if (forceReload || fragmentViewModel.getQuantityViewModel()?.orderQuantity != fragmentViewModel.lastQuantity ||
                                fragmentViewModel.getProductViewModel()?.productPrice != fragmentViewModel.lastPrice) {
                            if (activity != null) bt_buy.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_disabled)
                            fragmentViewModel.lastQuantity = fragmentViewModel.getQuantityViewModel()?.orderQuantity
                            fragmentViewModel.lastPrice = fragmentViewModel.getProductViewModel()?.productPrice
                            presenter.loadShippingRates(fragmentViewModel.getProductViewModel()?.productPrice?.toLong()
                                    ?: 0, fragmentViewModel.getQuantityViewModel()?.orderQuantity
                                    ?: 0, fragmentViewModel.getProfileViewModel()?.shippingDurationId
                                    ?: 0, fragmentViewModel.getProfileViewModel()?.shippingCourierId
                                    ?: 0)
                        }
                    }
                }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_GEOLOCATION) {
            showDurationOptions()
        }
    }

    private interface ReloadRatesDebounceListener {
        fun onNeedToRecalculateRates(forceReload: Boolean)
    }

}