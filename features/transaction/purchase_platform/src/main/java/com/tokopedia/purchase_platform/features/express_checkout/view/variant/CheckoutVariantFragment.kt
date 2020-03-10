package com.tokopedia.purchase_platform.features.express_checkout.view.variant

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.fingerprint.view.FingerPrintDialog
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.logisticdata.data.constant.LogisticConstant
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam
import com.tokopedia.purchase_platform.common.sharedata.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.utils.FingerprintUtil
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheets
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheets.Companion.RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheets.Companion.RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheetsActionListener
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheetsActionListenerWithRetry
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentActivity
import com.tokopedia.purchase_platform.features.express_checkout.data.constant.MAX_QUANTITY
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.AtcResponseModel
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.WholesalePriceModel
import com.tokopedia.purchase_platform.features.express_checkout.view.profile.CheckoutProfileBottomSheet
import com.tokopedia.purchase_platform.features.express_checkout.view.profile.CheckoutProfileFragmentListener
import com.tokopedia.purchase_platform.features.express_checkout.view.profile.uimodel.ProfileUiModel
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.analytics.ExpressCheckoutAnalyticsTracker
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.di.DaggerCheckoutVariantComponent
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.util.isOnboardingStateHasNotShown
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.util.setOnboardingStateHasNotShown
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.*
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.OptionVariantUiModel.Companion.STATE_SELECTED
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
    lateinit var fragmentUiModel: FragmentUiModel
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

    private lateinit var adapter: CheckoutVariantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentListener: CheckoutVariantFragmentListener
    private lateinit var reloadRatesDebounceListener: ReloadRatesDebounceListener
    private lateinit var tkpdProgressDialog: ProgressDialog
    private var trackerAttribution: String? = ""
    private var trackerListName: String? = ""

    private var isDataLoaded = false

    companion object {
        const val REQUEST_CODE_GEOLOCATION = 63

        const val ARGUMENT_ATC_REQUEST = "ARGUMENT_ATC_REQUEST"
        const val TRACKER_ATTRIBUTION = "tracker_attribution"
        const val TRACKER_LIST_NAME = "tracker_list_name"

        fun createInstance(atcRequestParam: AtcRequestParam,
                           trackerAttribution: String? = "",
                           trackerListName: String? = ""): CheckoutVariantFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGUMENT_ATC_REQUEST, atcRequestParam)
            bundle.putString(TRACKER_ATTRIBUTION, trackerAttribution ?: "")
            bundle.putString(TRACKER_LIST_NAME, trackerListName ?: "")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        trackerAttribution = arguments?.getString(TRACKER_ATTRIBUTION) ?: ""
        trackerListName = arguments?.getString(TRACKER_LIST_NAME) ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product_page, container, false)
        tkpdProgressDialog = ProgressDialog(getActivity());
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentListener = context as CheckoutVariantFragmentListener
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
        tkpdProgressDialog.show()
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

    override fun onVariantGuidelineClick(variantGuideline: String) {
        context?.run {
            startActivity(ImagePreviewActivity.getCallingIntent(context!!,
                arrayListOf(variantGuideline),
                null, 0))
        }
    }

    override fun onClickEditProfile() {
        if (!checkoutProfileBottomSheet.isAdded) {
            checkoutProfileBottomSheet.updateArguments(fragmentUiModel.getProfileViewModel())
            activity?.supportFragmentManager?.run {
                checkoutProfileBottomSheet.show(this, "")
            }
        }
    }

    override fun onClickEditDuration() {
        showDurationOptions()
    }

    override fun showDurationOptions() {
        val shippingParam = presenter.getShippingParam(fragmentUiModel.getQuantityViewModel()?.orderQuantity
            ?: 0, fragmentUiModel.getProductViewModel()?.productPrice?.toLong() ?: 0)
        val shopShipmentList = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels
        val selectedServiceId = fragmentUiModel.getProfileViewModel()?.shippingDurationId
        shippingDurationBottomsheet.updateArguments(shippingParam, selectedServiceId
            ?: 0, shopShipmentList)
        if (!shippingDurationBottomsheet.isAdded) {
            activity?.supportFragmentManager?.run {
                shippingDurationBottomsheet.show(this, "")
            }
        }
    }

    override fun showDurationOptions(latitude: String, longitude: String) {
        fragmentUiModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.addressModel?.latitude = latitude
        fragmentUiModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.addressModel?.longitude = longitude
        showDurationOptions()
    }

    override fun onClickEditCourier() {
        shippingCourierBottomsheet.updateArguments(fragmentUiModel.shippingCourierUiModels)
        if (!shippingCourierBottomsheet.isAdded) {
            activity?.supportFragmentManager?.run {
                shippingCourierBottomsheet.show(this, "")
            }
        }
    }

    override fun onClickInsuranceInfo(insuranceInfo: String) {
        if (activity != null) {
            val tooltip = Tooltip(activity as Context)
            tooltip.setTitle(activity?.getString(R.string.title_bottomsheet_insurance))
            tooltip.setDesc(insuranceInfo)
            tooltip.setTextButton(activity?.getString(R.string.label_button_bottomsheet_close))
            tooltip.setIcon(R.drawable.ic_pp_insurance)
            tooltip.btnAction.setOnClickListener {
                tooltip.dismiss()
            }
            tooltip.show()
        }
    }

    override fun onChangeVariant(selectedOptionUiModel: OptionVariantUiModel) {
        val productViewModel = fragmentUiModel.getProductViewModel()
        val summaryViewModel = fragmentUiModel.getSummaryViewModel()
        val quantityViewModel = fragmentUiModel.getQuantityViewModel()

        if (productViewModel != null && productViewModel.productChildrenList.isNotEmpty()) {
            var selectedKey = 0
            for ((key, value) in productViewModel.selectedVariantOptionsIdMap) {
                if (key == selectedOptionUiModel.variantId && value != selectedOptionUiModel.optionId) {
                    selectedKey = key
                }
            }
            if (selectedKey != 0) {
                productViewModel.selectedVariantOptionsIdMap[selectedKey] = selectedOptionUiModel.optionId
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
                onNeedToNotifySingleItem(fragmentUiModel.getIndex(productViewModel))

                if (summaryViewModel != null) {
                    summaryViewModel.itemPrice = quantityViewModel?.orderQuantity?.times(newSelectedProductChild.productPrice.toLong())
                        ?: 0
                    onNeedToNotifySingleItem(fragmentUiModel.getIndex(summaryViewModel))
                }

                val variantTypeViewModels = fragmentUiModel.getVariantTypeViewModel()
                for (variantTypeUiModel: TypeVariantUiModel in variantTypeViewModels) {
                    if (variantTypeUiModel.variantId == selectedOptionUiModel.variantId) {
                        variantTypeUiModel.variantSelectedValue = selectedOptionUiModel.variantName
                        onNeedToNotifySingleItem(fragmentUiModel.getIndex(variantTypeUiModel))
                        break
                    }
                }

                for (variantTypeUiModel: TypeVariantUiModel in variantTypeViewModels) {
                    if (variantTypeUiModel.variantId != selectedOptionUiModel.variantId) {
                        for (optionUiModel: OptionVariantUiModel in variantTypeUiModel.variantOptions) {

                            // Get other variant type selected option id
                            val otherVariantSelectedOptionIds = ArrayList<Int>()
                            for (otherVariantUiModel: TypeVariantUiModel in variantTypeViewModels) {
                                if (otherVariantUiModel.variantId != variantTypeUiModel.variantId &&
                                    otherVariantUiModel.variantId != selectedOptionUiModel.variantId) {
                                    for (otherVariantTypeOption: OptionVariantUiModel in otherVariantUiModel.variantOptions) {
                                        if (otherVariantTypeOption.currentState == STATE_SELECTED) {
                                            otherVariantSelectedOptionIds.add(otherVariantTypeOption.optionId)
                                            break
                                        }
                                    }
                                }
                            }

                            // Look for available child
                            var hasAvailableChild = false
                            for (productChild: ProductChild in productViewModel.productChildrenList) {
                                hasAvailableChild = checkChildAvailable(productChild, optionUiModel.optionId, selectedOptionUiModel.optionId, otherVariantSelectedOptionIds)
                                if (hasAvailableChild) break
                            }

                            // Set option id state with checking result
                            if (!hasAvailableChild) {
                                optionUiModel.hasAvailableChild = false
                                optionUiModel.currentState = STATE_NOT_AVAILABLE
                            } else if (optionUiModel.currentState != STATE_SELECTED) {
                                optionUiModel.hasAvailableChild = true
                                optionUiModel.currentState = STATE_NOT_SELECTED
                            }
                        }
                        onNeedToNotifySingleItem(fragmentUiModel.getIndex(variantTypeUiModel))
                    }
                }

                if (quantityViewModel != null) {
                    if (newSelectedProductChild.isAvailable && newSelectedProductChild.stock == 0) {
                        quantityViewModel.maxOrderQuantity = MAX_QUANTITY
                    } else {
                        quantityViewModel.maxOrderQuantity = newSelectedProductChild.stock
                    }
                    onNeedToNotifySingleItem(fragmentUiModel.getIndex(quantityViewModel))
                }
            }

            reloadRatesDebounceListener.onNeedToRecalculateRates(false)
            fragmentUiModel.isStateChanged = true
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

    override fun onChangeQuantity(quantityUiModel: QuantityUiModel) {
        val productViewModel = fragmentUiModel.getProductViewModel()
        val summaryViewModel = fragmentUiModel.getSummaryViewModel()

        if (fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.wholesalePriceModel?.isNotEmpty() == true) {
            val wholesalePriceModels = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.wholesalePriceModel?.asReversed()
            if (wholesalePriceModels != null) {
                var eligibleForWholesalePrice = false
                for (wholesalePriceModel: WholesalePriceModel in wholesalePriceModels) {
                    if (quantityUiModel.orderQuantity >= wholesalePriceModel.qtyMax ||
                        (quantityUiModel.orderQuantity < wholesalePriceModel.qtyMax &&
                            quantityUiModel.orderQuantity >= wholesalePriceModel.qtyMin)) {
                        productViewModel?.productPrice = wholesalePriceModel.prdPrc
                        eligibleForWholesalePrice = true
                        break
                    }
                }
                if (!eligibleForWholesalePrice) {
                    productViewModel?.productPrice = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productPrice
                        ?: 0
                }
            }
        }

        if (productViewModel?.productChildrenList != null && productViewModel.productChildrenList.size > 0) {
            for (productChild: ProductChild in productViewModel.productChildrenList) {
                if (productChild.isSelected) {
                    summaryViewModel?.itemPrice = productChild.productPrice.toLong() * quantityUiModel.orderQuantity
                    break
                }
            }
        } else {
            summaryViewModel?.itemPrice = productViewModel?.productPrice?.toLong()?.times(quantityUiModel.orderQuantity)
                ?: 0
        }

        if (summaryViewModel != null) {
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(summaryViewModel))
            onSummaryChanged(summaryViewModel)
        }

        onNeedToNotifySingleItem(fragmentUiModel.getIndex(quantityUiModel))
        reloadRatesDebounceListener.onNeedToRecalculateRates(false)
        fragmentUiModel.isStateChanged = true
    }

    override fun onChangeNote(noteUiModel: NoteUiModel) {
        if (fragmentUiModel.isStateChanged == false && noteUiModel.note.isNotEmpty()) {
            fragmentUiModel.isStateChanged = true
        }
    }

    override fun onSummaryChanged(summaryUiModel: SummaryUiModel?) {
        val totalPayment = summaryUiModel?.itemPrice?.plus(summaryUiModel.shippingPrice)?.plus(summaryUiModel.servicePrice)?.plus(summaryUiModel.insurancePrice)
        fragmentUiModel.totalPayment = totalPayment

        tv_total_payment_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.totalPayment
            ?: 0, false)
    }

    override fun onInsuranceCheckChanged(insuranceUiModel: InsuranceUiModel) {
        val summaryViewModel = fragmentUiModel.getSummaryViewModel()
        if (summaryViewModel != null) {
            if (insuranceUiModel.isChecked) {
                summaryViewModel.insurancePrice = insuranceUiModel.insurancePrice
                summaryViewModel.isUseInsurance = true
            } else {
                summaryViewModel.insurancePrice = 0
                summaryViewModel.isUseInsurance = false
            }

            onNeedToNotifySingleItem(fragmentUiModel.getIndex(summaryViewModel))
        }
        onNeedToNotifySingleItem(fragmentUiModel.getIndex(insuranceUiModel))
        fragmentUiModel.isStateChanged = true
    }

    override fun onNeedToValidateButtonBuyVisibility() {
        var hasError = false
        when {
            fragmentUiModel.getProfileViewModel()?.isDurationError == true -> hasError = true
            fragmentUiModel.getProfileViewModel()?.isCourierError == true -> hasError = true
            fragmentUiModel.getQuantityViewModel()?.isStateError == true -> hasError = true
        }

        if (activity != null) {
            if (hasError) {
                bt_buy.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_disabled)
                bt_buy.setOnClickListener { }
            } else {
                bt_buy.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_orange_enabled)
                bt_buy.setOnClickListener { presenter.checkoutExpress(fragmentUiModel, trackerAttribution, trackerListName) }
            }
        }
    }

    override fun onNeedToRecalculateRatesAfterChangeTemplate() {
        fragmentUiModel.getProfileViewModel()?.isStateHasChangedProfile = false
        reloadRatesDebounceListener.onNeedToRecalculateRates(true)
    }

    override fun onNeedToUpdateOnboardingStatus() {
        setOnboardingStateHasNotShown(activity, false)
    }

    override fun onGetCompositeSubscriber(): CompositeSubscription {
        return compositeSubscription
    }

    override fun onBindProductUpdateQuantityViewModel(productUiModel: ProductUiModel, stockWording: String) {
        val quantityViewModel = fragmentUiModel.getQuantityViewModel()
        if (quantityViewModel != null) {
            quantityViewModel.maxOrderQuantity = productUiModel.maxOrderQuantity
            quantityViewModel.stockWording = stockWording
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(quantityViewModel))
        }
    }

    override fun onBindVariantGetProductViewModel(): ProductUiModel? {
        return fragmentUiModel.getProductViewModel()
    }

    override fun onBindVariantUpdateProductViewModel() {
        val productViewModel = fragmentUiModel.getProductViewModel()
        if (productViewModel != null) {
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(productViewModel))
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

    override fun generateFingerprintPublicKey() {
        if (!fragmentUiModel.hasGenerateFingerprintPublicKey) {
            context?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        FingerprintUtil.getEnableFingerprintPayment(this)) {
                    val publicKey = FingerPrintDialog.generatePublicKey(this)
                    if (publicKey != null) {
                        fragmentUiModel.fingerprintPublicKey = FingerPrintDialog.getPublicKey(publicKey)
                        fragmentUiModel.hasGenerateFingerprintPublicKey = true
                    }
                }
            }
        }
    }

    override fun navigateAtcToOcs() {
        fragmentListener.navigateAtcToOcs()
    }

    override fun navigateAtcToNcf() {
        fragmentListener.navigateAtcToNcf()
    }

    override fun navigateCheckoutToOcs() {
        if (activity != null) {
            startActivity(ShipmentActivity.createInstance(context, ShipmentFormRequest.BundleBuilder().build()))
        }
        activity?.finish()
    }

    override fun navigateCheckoutToPayment(paymentPassData: PaymentPassData) {
        activity?.run {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
            intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
            startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
            finish()
        }
    }

    override fun navigateCheckoutToThankYouPage(appLink: String) {
        var eventLabel = if (fragmentUiModel.getProfileViewModel()?.isStateHasChangedProfile == false) {
            ConstantTransactionAnalytics.EventLabel.SUCCESS_DEFAULT
        } else {
            ConstantTransactionAnalytics.EventLabel.SUCCESS_NOT_DEFAULT
        }
        analyticsTracker.enhanceEcommerceImpressionExpressCheckoutForm(generateEnhanceEcommerceData(EnhancedECommerceActionField.STEP_2), eventLabel)
        if (activity != null) startActivity(RouteManager.getIntent(activity, appLink))
        activity?.finish()
    }

    override fun showBottomSheetError(title: String, message: String, action: String, enableRetry: Boolean) {
        errorBottomsheets.setData(title, message, action, enableRetry)
        if (errorBottomsheets.isVisible) {
            errorBottomsheets.dismiss()
        }
        fragmentManager?.run {
            errorBottomsheets.show(this, title)
        }
        fragmentUiModel.isStateChanged = true
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
        fragmentUiModel.isStateChanged = true
    }

    override fun showErrorNotAvailable(message: String) {
        showBottomSheetError(getString(R.string.bottomsheet_title_product_not_available), message, getString(R.string.bottomsheet_action_close), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
            }
        }
        analyticsTracker.eventClickBuyAndError(message)
        fragmentUiModel.isStateChanged = true
    }

    override fun showErrorAPI(retryAction: String) {
        val message = getString(R.string.bottomsheet_message_global_error)
        showBottomSheetError(getString(R.string.bottomsheet_title_global_error), message, getString(R.string.bottomsheet_action_global_error), true)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListenerWithRetry {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
                presenter.checkoutOneClickShipment(fragmentUiModel, trackerAttribution, trackerListName)
            }

            override fun onRetryClicked() {
                errorBottomsheets.dismiss()
                when (retryAction) {
                    RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT -> {
                        presenter.checkoutExpress(fragmentUiModel, trackerAttribution, trackerListName)
                    }
                    RETRY_ACTION_RELOAD_CHECKOUT_FOR_PAYMENT -> {
                        presenter.hitOldCheckout(fragmentUiModel)
                    }
                }
            }
        }
        analyticsTracker.eventClickBuyAndError(message)
        fragmentUiModel.isStateChanged = true
    }

    override fun showErrorPayment(message: String) {
        showBottomSheetError(getString(R.string.bottomsheet_title_change_payment_method), message, getString(R.string.bottomsheet_action_choose_payment_method), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
                presenter.hitOldCheckout(fragmentUiModel)
                analyticsTracker.clickPilihMetodePembayaran(fragmentUiModel.getProfileViewModel()?.paymentDetail
                    ?: "")
            }
        }
        analyticsTracker.eventClickBuyAndError(message)
        analyticsTracker.viewErrorMetodePembayaran()
        fragmentUiModel.isStateChanged = true
    }

    override fun showErrorPinpoint() {
        showBottomSheetError(getString(R.string.bottomsheet_title_no_pinpoint), getString(R.string.bottomsheet_message_no_pinpoint), getString(R.string.bottomsheet_action_pin_location), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
                goToGeolocationActivity()
            }
        }
        fragmentUiModel.isStateChanged = true
    }

    private fun goToGeolocationActivity() {
        val locationPass = LocationPass()
        locationPass.districtName = fragmentUiModel.getProfileViewModel()?.districtName
        locationPass.cityName = fragmentUiModel.getProfileViewModel()?.cityName
        activity?.run {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
            val bundle = Bundle()
            bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
            intent.putExtras(bundle)
            startActivityForResult(intent, REQUEST_CODE_GEOLOCATION)
        }
    }

    override fun updateFragmentViewModel(atcResponseModel: AtcResponseModel) {
        fragmentUiModel.atcResponseModel = atcResponseModel
    }

    override fun showData(viewModels: ArrayList<Visitable<*>>) {
        for (uiModel: Visitable<*> in viewModels) {
            if (uiModel is com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.ProfileUiModel) {
                uiModel.isFirstTimeShowProfile = isOnboardingStateHasNotShown(activity)
                break
            }
        }
        fragmentUiModel.viewModels = viewModels
        adapter.clearAllElements()
        adapter.addDataViewModel(viewModels)
        adapter.notifyDataSetChanged()

        onSummaryChanged(fragmentUiModel.getSummaryViewModel())

        rl_bottom_action_container.visibility = View.VISIBLE
        img_total_payment_info.setOnClickListener {
            recyclerView.smoothScrollToPosition(adapter.data.size - 1)
        }

        analyticsTracker.sendScreenName(activity, ConstantTransactionAnalytics.ScreenName.EXPRESS_CHECKOUT)
        analyticsTracker.enhanceEcommerceImpressionExpressCheckoutForm(generateEnhanceEcommerceData(EnhancedECommerceActionField.STEP_1), "")
    }

    fun generateEnhanceEcommerceData(step: String): HashMap<String, Any> {
        val enhanceEcommerceData = HashMap<String, Any>()

        val actionField = EnhancedECommerceActionField()
        actionField.setStep(if (step == EnhancedECommerceActionField.STEP_1) EnhancedECommerceActionField.STEP_1 else EnhancedECommerceActionField.STEP_2)
        actionField.setOption(if (step == EnhancedECommerceActionField.STEP_1) EnhancedECommerceActionField.OPTION_CLICK_CHECKOUT else EnhancedECommerceActionField.OPTION_CLICK_BAYAR)

        val product = EnhancedECommerceProductCartMapData()
        val shopModel = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel
        val productChildren = fragmentUiModel.getProductViewModel()?.productChildrenList
        if (productChildren != null && productChildren.isNotEmpty()) {
            for (productChild: ProductChild in productChildren) {
                if (productChild.isSelected) {
                    product.setProductName(productChild.productName)
                    product.setProductID(productChild.productId.toString())
                    product.setPrice(productChild.productPrice.toString())
                    break
                }
            }
        } else {
            product.setProductName(fragmentUiModel.getProductViewModel()?.productName)
            product.setProductID(fragmentUiModel.getProductViewModel()?.parentId?.toString())
            product.setPrice(fragmentUiModel.getProductViewModel()?.productPrice?.toString())
        }

        if (shopModel?.isOfficial == 1) {
            product.setShopType(EnhancedECommerceProductCartMapData.SHOP_TYPE_OFFICIAL_STORE)
        } else if (shopModel?.isGold == 1) {
            product.setShopType(EnhancedECommerceProductCartMapData.SHOP_TYPE_GOLD_MERCHANT)
        } else {
            product.setShopType(EnhancedECommerceProductCartMapData.SHOP_TYPE_REGULER)
        }

        product.setQty(fragmentUiModel.getQuantityViewModel()?.orderQuantity ?: 0)
        product.setShopId(shopModel?.shopId?.toString())
        product.setShopName(shopModel?.shopName)

        val checkout = EnhancedECommerceCheckout()
        checkout.setActionField(actionField.actionFieldMap)
        checkout.addProduct(product.product)

        enhanceEcommerceData.put("checkout", checkout.checkoutMap)

        return enhanceEcommerceData
    }

    override fun setShippingDurationError(message: String) {
        val profileViewModel = fragmentUiModel.getProfileViewModel()
        if (profileViewModel != null) {
            profileViewModel.isDurationError = true
            profileViewModel.durationErrorMessage = message
            profileViewModel.shippingCourierId = 0
            profileViewModel.shippingDurationId = 0
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(profileViewModel))
        }
        fragmentUiModel.isStateChanged = true
        val insuranceViewModel = fragmentUiModel.getInsuranceViewModel()
        if (insuranceViewModel != null) {
            insuranceViewModel.isVisible = false
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(insuranceViewModel))
        }
    }

    override fun setShippingCourierError(message: String) {
        val profileViewModel = fragmentUiModel.getProfileViewModel()
        if (profileViewModel != null) {
            profileViewModel.isCourierError = true
            profileViewModel.courierErrorMessage = message
            profileViewModel.shippingCourierId = 0
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(profileViewModel))
        }
        fragmentUiModel.isStateChanged = true
        val insuranceViewModel = fragmentUiModel.getInsuranceViewModel()
        if (insuranceViewModel != null) {
            insuranceViewModel.isVisible = false
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(insuranceViewModel))
        }
    }

    override fun updateShippingData(productData: ProductData, serviceData: ServiceData, shippingCourierUiModels: MutableList<ShippingCourierUiModel>?) {
        if (shippingCourierUiModels != null) {
            for (shippingCourierUiModel: ShippingCourierUiModel in shippingCourierUiModels) {
                if (shippingCourierUiModel.productData.isRecommend) {
                    shippingCourierUiModel.isSelected = true
                    break
                }
            }
            fragmentUiModel.shippingCourierUiModels = shippingCourierUiModels
        }

        val profileViewModel = fragmentUiModel.getProfileViewModel()
        val insuranceViewModel = fragmentUiModel.getInsuranceViewModel()
        val summaryViewModel = fragmentUiModel.getSummaryViewModel()

        val shopShipmentList = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels
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
                showErrorPinpoint()
            } else {
                profileViewModel.isCourierError = productData.error.errorMessage.isNotEmpty()
                profileViewModel.isDurationError = false
                profileViewModel.shippingCourier = productData.shipperName
                profileViewModel.shippingCourierId = productData.shipperProductId
                profileViewModel.shippingDuration = serviceData.serviceName
                profileViewModel.shippingDurationId = serviceData.serviceId
                onNeedToNotifySingleItem(fragmentUiModel.getIndex(profileViewModel))
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
                onNeedToNotifySingleItem(fragmentUiModel.getIndex(insuranceViewModel))

                if (summaryViewModel != null) {
                    summaryViewModel.isUseInsurance = insuranceViewModel.isChecked
                    summaryViewModel.shippingPrice = productData.price.price
                    summaryViewModel.insurancePrice = if (insuranceViewModel.isChecked) productData.insurance.insurancePrice else 0
                    summaryViewModel.insuranceInfo = productData.insurance.insuranceUsedInfo
                    onNeedToNotifySingleItem(fragmentUiModel.getIndex(summaryViewModel))
                }
            } else {
                insuranceViewModel.isChecked = false
                insuranceViewModel.isVisible = false
                onNeedToRemoveSingleItem(fragmentUiModel.getIndex(insuranceViewModel))

                if (summaryViewModel != null) {
                    summaryViewModel.isUseInsurance = false
                    summaryViewModel.shippingPrice = productData.price.price
                    summaryViewModel.insurancePrice = 0
                    summaryViewModel.insuranceInfo = ""
                    onNeedToNotifySingleItem(fragmentUiModel.getIndex(summaryViewModel))
                }
            }
        }

        if (summaryViewModel != null) {
            onSummaryChanged(summaryViewModel)
        }
    }

    override fun onShippingDurationChoosen(shippingCourierUiModels: MutableList<ShippingCourierUiModel>,
                                           courierItemData: CourierItemData,
                                           recipientAddressModel: RecipientAddressModel,
                                           cartPosition: Int,
                                           selectedServiceId: Int,
                                           serviceData: ServiceData,
                                           flagNeedToSetPinpoint: Boolean,
                                           isDurationClick: Boolean,
                                           isClearPromo: Boolean) {
        if (shippingCourierUiModels != null) {
            val summaryViewModel = fragmentUiModel.getSummaryViewModel()
            if (summaryViewModel != null) {
                onSummaryChanged(summaryViewModel)
            }
            fragmentUiModel.shippingCourierUiModels = shippingCourierUiModels
            for (shippingCourierUiModel: ShippingCourierUiModel in shippingCourierUiModels) {
                if (shippingCourierUiModel.productData.isRecommend || shippingCourierUiModel.serviceData.serviceId == selectedServiceId) {
                    if (shippingCourierUiModel.serviceData.error != null &&
                        !TextUtils.isEmpty(shippingCourierUiModel.serviceData.error.errorMessage) &&
                        shippingCourierUiModel.serviceData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                        goToGeolocationActivity()
                    } else {
                        updateShippingData(shippingCourierUiModel.productData, shippingCourierUiModel.serviceData, shippingCourierUiModels)
                    }
                    break
                }
            }
        }
    }

    override fun onInsuranceSelectedStateChanged(element: InsuranceRecommendationUiModel?, isSelected: Boolean) {
    }

    override fun sendEventInsuranceInfoClicked() {
    }

    override fun sendEventInsuranceSelectedStateChanged(isChecked: Boolean, title: String) {
    }

    override fun onNoCourierAvailable(message: String?) {

    }

    override fun onShippingDurationButtonCloseClicked() {
        shippingDurationBottomsheet.dismiss()
    }

    override fun onShowDurationListWithCourierPromo(isCourierPromo: Boolean, duration: String?) {

    }

    override fun onCourierChoosen(shippingCourierUiModel: ShippingCourierUiModel, courierItemData: CourierItemData?,
                                  recipientAddressModel: RecipientAddressModel?, cartPosition: Int, hasCourierPromo: Boolean,
                                  isPromoCourier: Boolean, isNeedPinpoint: Boolean) {
        updateShippingData(shippingCourierUiModel.productData, shippingCourierUiModel.serviceData, null)
    }

    override fun onCourierShipmentRecpmmendationCloseClicked() {
        shippingCourierBottomsheet.dismiss()
    }

    override fun onRetryReloadCourier(shipmentCartItemModel: ShipmentCartItemModel?, cartPosition: Int, shopShipmentList: MutableList<ShopShipment>?) {

    }

    override fun onContinueWithoutProfile() {
        val profileViewModel = fragmentUiModel.getProfileViewModel()
        if (profileViewModel != null) {
            profileViewModel.isStateHasRemovedProfile = true
            profileViewModel.isSelected = false
            onNeedToNotifySingleItem(fragmentUiModel.getIndex(profileViewModel))
        }
        checkoutProfileBottomSheet.dismiss()
        analyticsTracker.eventClickContinueWithoutTemplate(fragmentUiModel.isStateChanged == false)
    }

    override fun onProfileChanged(selectedProfileUiModel: ProfileUiModel) {
        val currentProfileViewModel = fragmentUiModel.getProfileViewModel()
        if (currentProfileViewModel != null) {
            currentProfileViewModel.addressId = selectedProfileUiModel.addressId
            currentProfileViewModel.addressDetail = selectedProfileUiModel.addressDetail
            currentProfileViewModel.addressTitle = selectedProfileUiModel.addressTitle
            currentProfileViewModel.cityName = selectedProfileUiModel.cityName
            currentProfileViewModel.districtName = selectedProfileUiModel.districtName
            currentProfileViewModel.isStateHasRemovedProfile = false
            currentProfileViewModel.isSelected = true
            currentProfileViewModel.isDurationError = false
            currentProfileViewModel.isCourierError = false
            currentProfileViewModel.isStateHasChangedProfile = true
            currentProfileViewModel.isShowDefaultProfileCheckBox =
                selectedProfileUiModel.profileId != currentProfileViewModel.profileId
            currentProfileViewModel.isEditable = false
            currentProfileViewModel.isDefaultProfileCheckboxChecked = false
            currentProfileViewModel.paymentDetail = selectedProfileUiModel.paymentDetail
            currentProfileViewModel.paymentOptionImageUrl = selectedProfileUiModel.paymentImageUrl
            currentProfileViewModel.shippingDurationId = selectedProfileUiModel.durationId
            currentProfileViewModel.shippingDuration = selectedProfileUiModel.durationDetail

            onNeedToNotifySingleItem(fragmentUiModel.getIndex(currentProfileViewModel))
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
        }).debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Boolean>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(forceReload: Boolean) {
                    if (forceReload || fragmentUiModel.getQuantityViewModel()?.orderQuantity != fragmentUiModel.lastQuantity ||
                        fragmentUiModel.getProductViewModel()?.productPrice != fragmentUiModel.lastPrice) {
                        if (activity != null) bt_buy.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_disabled)
                        fragmentUiModel.lastQuantity = fragmentUiModel.getQuantityViewModel()?.orderQuantity
                        fragmentUiModel.lastPrice = fragmentUiModel.getProductViewModel()?.productPrice
                        presenter.loadShippingRates(fragmentUiModel.getProductViewModel()?.productPrice?.toLong()
                            ?: 0, fragmentUiModel.getQuantityViewModel()?.orderQuantity
                            ?: 0, fragmentUiModel.getProfileViewModel()?.shippingDurationId
                            ?: 0, fragmentUiModel.getProfileViewModel()?.shippingCourierId
                            ?: 0)
                    }
                }
            }))
    }

    override fun onLogisticPromoChosen(shippingCourierUiModels: MutableList<ShippingCourierUiModel>, courierData: CourierItemData, recipientAddressModel: RecipientAddressModel, cartPosition: Int, serviceData: ServiceData, flagNeedToSetPinpoint: Boolean, promoCode: String, selectedServiceId: Int) {
        // Haven't discussed yet
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_GEOLOCATION) {
            val locationPass = data?.extras?.getParcelable<LocationPass>(LogisticConstant.EXTRA_EXISTING_LOCATION)
            presenter.updateAddress(fragmentUiModel, locationPass?.latitude
                ?: "", locationPass?.longitude ?: "")
        }
    }

    private interface ReloadRatesDebounceListener {
        fun onNeedToRecalculateRates(forceReload: Boolean)
    }

}