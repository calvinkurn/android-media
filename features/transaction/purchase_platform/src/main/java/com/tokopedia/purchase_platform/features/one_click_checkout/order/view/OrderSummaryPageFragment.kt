package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.logisticdata.data.constant.LogisticConstant
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.utils.Utils.convertDpToPixel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Address
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccGlobalEvent
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.OrderSummaryPageComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OccInfoBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.PreferenceListBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderPreferenceCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderProductCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.ButtonBayarState
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderPreference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderProduct
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.card_order_empty_preference.*
import kotlinx.android.synthetic.main.fragment_order_summary_page.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class OrderSummaryPageFragment : BaseDaggerFragment(), OrderProductCard.OrderProductCardListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: OrderSummaryPageViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[OrderSummaryPageViewModel::class.java]
    }

    private lateinit var orderProductCard: OrderProductCard
    private lateinit var orderPreferenceCard: OrderPreferenceCard

    private var progressDialog: AlertDialog? = null

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(OrderSummaryPageComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CREATE_PREFERENCE || requestCode == REQUEST_EDIT_PREFERENCE) {
            refresh()
        } else if (requestCode == REQUEST_CODE_COURIER_PINPOINT) {
            onResultFromCourierPinpoint(resultCode, data)
        }
    }

    private fun onResultFromCourierPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data?.extras != null) {
            val locationPass: LocationPass? = data.extras?.getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION)
            if (locationPass != null) {
                //update
                viewModel.savePinpoint(locationPass.longitude, locationPass.latitude)
            }
        }
        // show error
//        view?.let {
//            Toaster.make(it, "Pinpoint lokasi untuk melanjutkan dengan durasi/kurir sebelumnya")
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_summary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh_layout.isRefreshing = true
        initViews(view)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.orderPreference.observe(this, Observer {
            if (it is OccState.FirstLoad) {
                swipe_refresh_layout.isRefreshing = false
                global_error.gone()
                main_content.visible()
                view?.let { v ->
                    Toaster.make(v, "success first")
                    orderProductCard.setProduct(viewModel.orderProduct)
                    orderProductCard.setShop(viewModel.orderShop)
                    orderProductCard.initView()
                    showMessage(it.data.preference)
                    if (it.data.preference.address.addressId > 0) {
                        showPreferenceCard()
                        orderPreferenceCard.setPreference(it.data)
                    } else {
                        showEmptyPreferenceCard()
                    }
                }
            } else if (it is OccState.Success) {
                swipe_refresh_layout.isRefreshing = false
                main_content.visible()
                view?.let { v ->
                    //                    Toaster.make(v, "success")
                    if (it.data.preference.address.addressId > 0) {
                        orderPreferenceCard.setPreference(it.data)
                    }
                    setupInsurance(it)
                    if (it.data.shipping?.needPinpoint == true) {
                        goToPinpoint(it.data.preference.address)
                    }
                }
            } else if (it is OccState.Loading) {
                swipe_refresh_layout.isRefreshing = true
            } else if (it is OccState.Fail) {
                swipe_refresh_layout.isRefreshing = false
                if (it.throwable != null) {
                    handleError(it.throwable)
                }
            }
        })
        viewModel.orderTotal.observe(this, Observer {
            setupButtonBayar(it)
        })
        viewModel.globalEvent.observe(this, Observer {
            when (it) {
                is OccGlobalEvent.Loading -> {
                    if (progressDialog == null) {
                        progressDialog = AlertDialog.Builder(context!!)
                                .setView(R.layout.purchase_platform_progress_dialog_view)
                                .setCancelable(false)
                                .create()
                    }
                    progressDialog?.show()
                }
                is OccGlobalEvent.Normal -> {
                    progressDialog?.dismiss()
                }
                is OccGlobalEvent.TriggerRefresh -> {
                    progressDialog?.dismiss()
                    refresh(false)
                }
                is OccGlobalEvent.Error -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        var message = it.errorMessage
                        if (message.isBlank()) {
                            message = ErrorHandler.getErrorMessage(context, it.throwable)
                        }
                        Toaster.make(v, message, type = Toaster.TYPE_ERROR)
                    }
                }
            }
        })
        if (viewModel.orderProduct.parentId == 0) {
            refresh()
        }
    }

    private fun showPreferenceCard() {
        empty_preference_card.gone()
        preference_card.visible()
        tv_total_payment_label.visible()
        tv_total_payment_value.visible()
        btn_order_detail.visible()
        btn_pay.visible()
    }

    private fun showEmptyPreferenceCard() {
        ImageHandler.LoadImage(image_empty_profile, EMPTY_STATE_PICT_URL)
        empty_preference_card.visible()
        preference_card.gone()
        tv_total_payment_label.gone()
        tv_total_payment_value.gone()
        btn_order_detail.gone()
        btn_pay.gone()

        button_atur_pilihan.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
            intent.putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, 1)
            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
        }
    }

    private fun goToPinpoint(address: Address) {
        val locationPass = LocationPass()
        locationPass.cityName = address.cityName
        locationPass.districtName = address.districtName
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle()
        bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
        bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
        viewModel.changePinpoint()
    }

    private fun setupInsurance(it: OccState.Success<OrderPreference>) {
        val insuranceData = it.data.shipping?.insuranceData
        if (insuranceData != null) {
            if (insuranceData.insurancePrice > 0) {
                tv_insurance_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceData.insurancePrice, false)
                tv_insurance_price.visible()
            } else {
                tv_insurance_price.gone()
            }
            img_bt_insurance_info.setOnClickListener {
                showBottomSheet(img_bt_insurance_info.context,
                        img_bt_insurance_info.context.getString(R.string.title_bottomsheet_insurance),
                        insuranceData.insuranceUsedInfo,
                        R.drawable.ic_pp_insurance)
            }
            cb_insurance.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setInsuranceCheck(isChecked)
            }
            if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_MUST) {
                tv_insurance.setText(R.string.label_must_insurance)
                cb_insurance.isChecked = true
                cb_insurance.isEnabled = false
                viewModel.setInsuranceCheck(true)
                group_insurance.visible()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_NO) {
                group_insurance.gone()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tv_insurance.setText(R.string.label_shipment_insurance)
                cb_insurance.isEnabled = true
                if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                    cb_insurance.isChecked = true
                    viewModel.setInsuranceCheck(true)
                } else if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                    cb_insurance.isChecked = false
                    viewModel.setInsuranceCheck(false)
                }
                group_insurance.visible()
            }
        } else {
            group_insurance.gone()
        }
    }

    private fun showBottomSheet(context: Context, title: String, message: String, image: Int) {
        val tooltip = Tooltip(context)
        tooltip.setTitle(title)
        tooltip.setDesc(message)
        tooltip.setTextButton(context.getString(R.string.label_button_bottomsheet_close))
        tooltip.setIcon(image)
        tooltip.btnAction.setOnClickListener { tooltip.dismiss() }
        tooltip.show()
    }

    private fun setupButtonBayar(orderTotal: OrderTotal) {
        if (orderTotal.isButtonChoosePayment) {
            if (orderTotal.buttonState == ButtonBayarState.NORMAL) {
                btn_pay.layoutParams.width = convertDpToPixel(160f, context!!)
                btn_pay.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btn_pay.setText(R.string.label_choose_payment)
                btn_pay.isLoading = false
                btn_pay.isEnabled = true
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else if (orderTotal.buttonState == ButtonBayarState.DISABLE) {
                btn_pay.layoutParams.width = convertDpToPixel(160f, context!!)
                btn_pay.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btn_pay.setText(R.string.label_choose_payment)
                btn_pay.isLoading = false
                btn_pay.isEnabled = false
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                btn_pay.layoutParams.width = convertDpToPixel(160f, context!!)
                btn_pay.layoutParams.height = convertDpToPixel(48f, context!!)
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                btn_pay.isLoading = true
            }
        } else {
            if (orderTotal.buttonState == ButtonBayarState.NORMAL) {
                btn_pay.setText(R.string.pay)
                btn_pay.layoutParams.width = convertDpToPixel(140f, context!!)
                btn_pay.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                btn_pay.isLoading = false
                btn_pay.isEnabled = true
            } else if (orderTotal.buttonState == ButtonBayarState.DISABLE) {
                btn_pay.setText(R.string.pay)
                btn_pay.layoutParams.width = convertDpToPixel(140f, context!!)
                btn_pay.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                btn_pay.isLoading = false
                btn_pay.isEnabled = false
            } else {
                btn_pay.layoutParams.width = convertDpToPixel(140f, context!!)
                btn_pay.layoutParams.height = convertDpToPixel(48f, context!!)
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                btn_pay.isLoading = true
            }
        }

        if (orderTotal.orderCost.totalPrice > 0.0) {
            tv_total_payment_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderTotal.orderCost.totalPrice, false)
        } else {
            tv_total_payment_value.text = "-"
        }

        btn_order_detail.setOnClickListener {
            if (orderTotal.orderCost.totalPrice > 0.0) {
                OrderPriceSummaryBottomSheet().show(this, orderTotal.orderCost)
            }
        }
    }

    private fun showMessage(preference: ProfileResponse) {
        tv_header.text = "Barang yang dibeli"
        if (preference.hasPreference) {
            tv_header_2.text = "Pengiriman dan Pembayaran"
            tv_subheader.gone()
        } else {
            tv_header_2.gone()
            iv_subheader.visible()
            tv_subheader.text = preference.onboardingHeaderMessage
            tv_subheader_action.setOnClickListener {
                OccInfoBottomSheet().show(this, preference.onboardingComponent)
            }
            tv_subheader_action.visible()
            tv_subheader.visible()
        }
    }

    private fun initViews(view: View) {
        orderProductCard = OrderProductCard(view, this)
        orderPreferenceCard = OrderPreferenceCard(view, getOrderPreferenceCardListener())
    }

    private fun getOrderPreferenceCardListener() = object : OrderPreferenceCard.OrderPreferenceCardListener {

        override fun onChangePreferenceClicked() {
            showPreferenceListBottomSheet()
        }

        override fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel) {
            viewModel.chooseCourier(shippingCourierViewModel)
        }

        override fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
            viewModel.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
        }

        override fun chooseCourier() {
            if (viewModel.orderTotal.value?.buttonState != ButtonBayarState.LOADING) {
                orderPreferenceCard.showCourierBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun chooseDuration() {
            if (viewModel.orderTotal.value?.buttonState != ButtonBayarState.LOADING) {
                orderPreferenceCard.showDurationBottomSheet(this@OrderSummaryPageFragment)
            }
        }
    }

    fun showPreferenceListBottomSheet() {
        viewModel.updateCart()
        val profileId = viewModel._orderPreference?.preference?.profileId ?: 0
        val updateCartParam = viewModel.generateUpdateCartParam()
        if (profileId > 0 && updateCartParam != null) {
            PreferenceListBottomSheet(
                    getPreferenceListUseCase = viewModel.getPreferenceListUseCase,
                    listener = object : PreferenceListBottomSheet.PreferenceListBottomSheetListener {
                        override fun onChangePreference(preference: ProfilesItemModel) {
                            viewModel.updatePreference(preference)
                            refresh()
                        }

                        override fun onEditPreference(preference: ProfilesItemModel, adapterPosition: Int) {
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
                            intent.apply {
                                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, adapterPosition)
                                putExtra(PreferenceEditActivity.EXTRA_PROFILE_ID, preference.profileId)
                                putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, preference.addressModel?.addressId)
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, preference.shipmentModel?.serviceId)
                                putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, preference.paymentModel?.gatewayCode
                                        ?: "")
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                            }
                            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
                        }

                        override fun onAddPreference(itemCount: Int) {
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
                            intent.putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, itemCount + 1)
                            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
                        }
                    }).show(this@OrderSummaryPageFragment, profileId)
        }
    }

    override fun onProductChange(product: OrderProduct, shouldReloadRates: Boolean) {
        viewModel.updateProduct(product, shouldReloadRates)
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.make(it, "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable.message
                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                }
            }
        }
//        viewModel.consumePreferenceListFail()
    }

    private fun showGlobalError(type: Int) {
        global_error.setType(type)
        global_error.setActionClickListener {
            refresh(false)
        }
        main_content.gone()
        global_error.visible()
    }

    private fun refresh(shouldHideAll: Boolean = true) {
        swipe_refresh_layout.isRefreshing = true
        if (shouldHideAll) {
            main_content.gone()
            global_error.gone()
        }
        viewModel.getOccCart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateCart()
    }

    companion object {

        const val REQUEST_EDIT_PREFERENCE = 11
        const val REQUEST_CREATE_PREFERENCE = 12

        const val REQUEST_CODE_COURIER_PINPOINT = 13
        private const val EMPTY_STATE_PICT_URL = "https://ecs7.tokopedia.net/android/others/pilih_alamat_pengiriman3x.png"
    }
}