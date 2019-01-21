package com.tokopedia.expresscheckout.view.variant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.router.ExpressCheckoutRouter
import com.tokopedia.expresscheckout.view.errorview.ErrorBottomsheets
import com.tokopedia.expresscheckout.view.errorview.ErrorBottomsheetsActionListener
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileBottomSheet
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileFragmentListener
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.shipping_recommendation.domain.shipping.*
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationBottomsheetListener
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam
import kotlinx.android.synthetic.main.fragment_detail_product_page.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantFragment : BaseListFragment<Visitable<*>, CheckoutVariantAdapterTypeFactory>(),
        CheckoutVariantContract.View, CheckoutVariantActionListener, CheckoutProfileFragmentListener,
        ShippingDurationBottomsheetListener, ShippingCourierBottomsheetListener {

    val contextView: Context get() = activity!!
    private lateinit var presenter: CheckoutVariantContract.Presenter
    private lateinit var adapter: CheckoutVariantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorBottomSheets: ErrorBottomsheets
    private lateinit var fragmentListener: CheckoutVariantFragmentListener
    private lateinit var fragmentViewModel: FragmentViewModel
    private lateinit var tkpdProgressDialog: TkpdProgressDialog
    private lateinit var compositeSubscription: CompositeSubscription
    private lateinit var reloadRatesDebounceListener: ReloadRatesDebounceListener
    private lateinit var shippingDurationBottomsheet: ShippingDurationBottomsheet
    private lateinit var shippingCourierBottomsheet: ShippingCourierBottomsheet
    private lateinit var checkoutProfileBottomSheet: CheckoutProfileBottomSheet
    private lateinit var router: ExpressCheckoutRouter
    var isDataLoaded = false

    companion object {
        val REQUEST_CODE_GEOLOCATION = 63

        val ARGUMENT_ATC_REQUEST = "ARGUMENT_ATC_REQUEST"

        fun createInstance(atcRequestParam: AtcRequestParam): CheckoutVariantFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGUMENT_ATC_REQUEST, atcRequestParam)
            val fragment = CheckoutVariantFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = CheckoutVariantPresenter()
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product_page, container, false)
        tkpdProgressDialog = TkpdProgressDialog(activity, TkpdProgressDialog.NORMAL_PROGRESS)

        recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(CheckoutVariantItemDecorator())
        (recyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false

        errorBottomSheets = ErrorBottomsheets()
        fragmentViewModel = FragmentViewModel()
        compositeSubscription = CompositeSubscription()
        initUpdateShippingRatesDebouncer()

        shippingDurationBottomsheet = ShippingDurationBottomsheet.newInstance()
        shippingCourierBottomsheet = ShippingCourierBottomsheet.newInstance()
        checkoutProfileBottomSheet = CheckoutProfileBottomSheet.newInstance()

        return view
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

    override fun onBindProfile() {
        checkoutProfileBottomSheet.updateArguments(fragmentViewModel.getProfileViewModel())
    }

    override fun onClickEditProfile() {
        checkoutProfileBottomSheet.show(activity?.supportFragmentManager, "")
    }

    override fun onClickEditDuration() {
        showDurationOptions()
    }

    private fun showDurationOptions() {
        val shippingParam = presenter.getShippingParam(fragmentViewModel.getQuantityViewModel()?.orderQuantity
                ?: 0, fragmentViewModel.getProductViewModel()?.productPrice ?: 0)
        val shopShipmentList = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels
        val selectedServiceId = fragmentViewModel.getProfileViewModel()?.shippingDurationId
        shippingDurationBottomsheet.updateArguments(shippingParam, selectedServiceId
                ?: 0, shopShipmentList)
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
            val tooltip = Tooltip(contextView)
            tooltip.setTitle(contextView.getString(R.string.title_bottomsheet_insurance))
            tooltip.setDesc(insuranceInfo)
            tooltip.setTextButton(contextView.getString(R.string.label_button_bottomsheet_close))
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
                for ((key, value) in productViewModel.selectedVariantOptionsIdMap) {
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
                    summaryViewModel.itemPrice = quantityViewModel?.orderQuantity?.times(newSelectedProductChild.productPrice) ?: 0
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
                                optionViewModel.currentState == optionViewModel.STATE_NOT_AVAILABLE
                            } else if (optionViewModel.currentState != optionViewModel.STATE_SELECTED) {
                                optionViewModel.hasAvailableChild = true
                                optionViewModel.currentState == optionViewModel.STATE_NOT_SELECTED
                            }
                        }
                        onNeedToNotifySingleItem(fragmentViewModel.getIndex(variantTypeViewModel))
                    }
                }
            }

            reloadRatesDebounceListener.onNeedToRecalculateRates(true)
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

        if (productViewModel?.productChildrenList != null && productViewModel.productChildrenList.size > 0) {
            for (productChild: ProductChild in productViewModel.productChildrenList) {
                if (productChild.isSelected) {
                    summaryViewModel?.itemPrice = productChild.productPrice * quantityViewModel.orderQuantity
                    break
                }
            }
        }

        if (summaryViewModel != null) {
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
            onSummaryChanged(summaryViewModel)
        }

        onNeedToNotifySingleItem(fragmentViewModel.getIndex(quantityViewModel))
        reloadRatesDebounceListener.onNeedToRecalculateRates(true)
    }

    override fun onSummaryChanged(summaryViewModel: SummaryViewModel?) {
        val totalPayment = summaryViewModel?.itemPrice?.plus(summaryViewModel.shippingPrice)?.plus(summaryViewModel.servicePrice)?.plus(summaryViewModel.insurancePrice)
        fragmentViewModel.totalPayment = totalPayment

        tv_total_payment_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(fragmentViewModel.totalPayment
                ?: 0)
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

    override fun initInjector() {

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

    override fun navigateToOcs() {
        fragmentListener.navigateToOcs()
    }

    override fun navigateToNcf() {
        fragmentListener.navigateToNcf()
    }

    override fun showBottomsheetError(title: String, message: String, action: String) {
        errorBottomSheets.setError(title, message, action)
        if (errorBottomSheets.isVisible) {
            errorBottomSheets.dismiss()
        }
        errorBottomSheets.show(fragmentManager, title)
    }

    override fun updateFragmentViewModel(atcResponseModel: AtcResponseModel) {
        fragmentViewModel.atcResponseModel = atcResponseModel
    }

    override fun showData(viewModels: ArrayList<Visitable<*>>) {
        hideLoading()
        fragmentViewModel.viewModels = viewModels
        adapter.clearAllElements()
        adapter.addDataViewModel(viewModels)
        adapter.notifyDataSetChanged()

        onSummaryChanged(fragmentViewModel.getSummaryViewModel())

        rl_bottom_action_container.visibility = View.VISIBLE
        bt_buy.setOnClickListener { presenter.checkout(fragmentViewModel) }
        img_total_payment_info.setOnClickListener {
            recyclerView.smoothScrollToPosition(adapter.data.size - 1)
        }
    }

    override fun setShippingError() {
        val profileViewModel = fragmentViewModel.getProfileViewModel()
        if (profileViewModel != null) {
            profileViewModel.isDurationError = true
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(profileViewModel))
        }
    }

    override fun updateShippingData(productData: ProductData, serviceData: ServiceData, shippingCourierViewModels: MutableList<ShippingCourierViewModel>?) {
        if (shippingCourierViewModels != null) {
            fragmentViewModel.shippingCourierViewModels = shippingCourierViewModels
        }

        shippingDurationBottomsheet.setShippingDurationBottomsheetListener(this)
        shippingCourierBottomsheet.setShippingCourierBottomsheetListener(this)

        val profileViewModel = fragmentViewModel.getProfileViewModel()
        val insuranceViewModel = fragmentViewModel.getInsuranceViewModel()
        val summaryViewModel = fragmentViewModel.getSummaryViewModel()
        if (profileViewModel != null) {
            if (productData.error != null && productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                showBottomsheetError("Tandai Lokasi Pengiriman", productData.error.errorMessage, "Tandai Lokasi")
                errorBottomSheets.actionListener = object : ErrorBottomsheetsActionListener {
                    override fun onActionButtonClicked() {
                        val locationPass = LocationPass()
                        locationPass.districtName = profileViewModel.districtName
                        locationPass.cityName = profileViewModel.cityName
                        startActivityForResult(router.getGeolocationIntent(contextView, locationPass), REQUEST_CODE_GEOLOCATION)
                    }
                }
            } else {
                profileViewModel.isDurationError = false
                profileViewModel.shippingCourier = productData.shipperName
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
                    summaryViewModel.insurancePrice = productData.insurance.insurancePrice
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

    }

    private fun initUpdateShippingRatesDebouncer() {
        compositeSubscription.add(Observable.create(Observable.OnSubscribe<Boolean> { subscriber ->
            reloadRatesDebounceListener = object : ReloadRatesDebounceListener {
                override fun onNeedToRecalculateRates(boolean: Boolean) {
                    subscriber.onNext(boolean)
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

                    override fun onNext(boolean: Boolean) {
                        if (fragmentViewModel.getQuantityViewModel()?.orderQuantity != fragmentViewModel.lastQuantity ||
                                fragmentViewModel.getProductViewModel()?.productPrice != fragmentViewModel.lastPrice) {
                            fragmentViewModel.lastQuantity = fragmentViewModel.getQuantityViewModel()?.orderQuantity
                            fragmentViewModel.lastPrice = fragmentViewModel.getProductViewModel()?.productPrice
                            presenter.loadShippingRates(fragmentViewModel.getProductViewModel()?.productPrice
                                    ?: 0, fragmentViewModel.getQuantityViewModel()?.orderQuantity
                                    ?: 0, fragmentViewModel.getProfileViewModel()?.shippingDurationId
                                    ?: 0, true)
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

        fun onNeedToRecalculateRates(boolean: Boolean)

    }

}