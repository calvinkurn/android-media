package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.utils.Utils.convertDpToPixel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.OrderSummaryPageComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.PreferenceListBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderPreferenceCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderProductCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.ButtonBayarState
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderProduct
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_order_summary_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderSummaryPageFragment : BaseDaggerFragment(), OrderProductCard.OrderProductCardListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: OrderSummaryPageViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[OrderSummaryPageViewModel::class.java]
    }

    private lateinit var orderProductCard: OrderProductCard
    private lateinit var orderPreferenceCard: OrderPreferenceCard

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(OrderSummaryPageComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == REQUEST_CREATE_PREFERENCE || requestCode == REQUEST_EDIT_PREFERENCE) {
//            showPreferenceListBottomSheet()
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
            if (it is OccState.Success) {
                swipe_refresh_layout.isRefreshing = false
                main_content.visible()
                view?.let { v ->
                    Toaster.make(v, "success")
                    if (viewModel.orderProduct.quantity != null) {
                        orderProductCard.setProduct(viewModel.orderProduct)
                        orderProductCard.setShop(viewModel.orderShop)
                        orderProductCard.initView()
                        showMessage(it.data.preference)
                        if (it.data.preference.address.addressId > 0) {
                            orderPreferenceCard.setPreference(it.data)
                        }
                    }

                    setupInsurance(it)
                }
            } else if (it is OccState.Loading) {
                swipe_refresh_layout.isRefreshing = true
            } else {
                swipe_refresh_layout.isRefreshing = false
                view?.let {
                    Toaster.make(it, "Error", type = Toaster.TYPE_ERROR)
                }
            }
        })
        viewModel.orderTotal.observe(this, Observer {
            setupButtonBayar(it)
        })
        if (viewModel.orderProduct.parentId == 0) {
            viewModel.getOccCart()
        }
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
                group_insurance.visible()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_NO) {
                group_insurance.gone()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tv_insurance.setText(R.string.label_shipment_insurance)
                cb_insurance.isEnabled = true
                if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                    cb_insurance.isChecked = true
                } else if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                    cb_insurance.isChecked = false
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
        if (orderTotal.buttonState == ButtonBayarState.LOADING) {
            btn_pay.isLoading = true
            btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else if (orderTotal.isButtonChoosePayment) {
            if (orderTotal.buttonState == ButtonBayarState.NORMAL) {
                btn_pay.layoutParams.width = convertDpToPixel(160f, context!!)
                btn_pay.setText(R.string.label_choose_payment)
                btn_pay.isLoading = false
                btn_pay.isEnabled = true
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                btn_pay.layoutParams.width = convertDpToPixel(160f, context!!)
                btn_pay.setText(R.string.label_choose_payment)
                btn_pay.isLoading = false
                btn_pay.isEnabled = false
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        } else {
            if (orderTotal.buttonState == ButtonBayarState.NORMAL) {
                btn_pay.setText(R.string.pay)
                btn_pay.layoutParams.width = convertDpToPixel(140f, context!!)
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                btn_pay.isLoading = false
                btn_pay.isEnabled = true
            } else {
                btn_pay.setText(R.string.pay)
                btn_pay.layoutParams.width = convertDpToPixel(140f, context!!)
                btn_pay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                btn_pay.isLoading = false
                btn_pay.isEnabled = false
            }
        }

        if (orderTotal.subTotal > 0L) {
            tv_total_payment_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderTotal.subTotal, false)
        } else {
            tv_total_payment_value.text = "-"
        }
    }

    private fun showMessage(preference: ProfileResponse) {
        tv_header.text = "Barang yang dibeli"
        if (preference.hasPreference) {
            tv_header_2.text = "Pengiriman dan Pembayaran"
            tv_subheader.gone()
        } else {
            tv_header_2.text = "Hai!"
            val spannableString = SpannableString(preference.onboardingHeaderMessage + " Info")
            spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#03AC0E")), preference.onboardingHeaderMessage.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            tv_subheader.text = spannableString
            tv_subheader.setOnClickListener {
                //show bottomsheet
            }
        }
    }

    private fun initViews(view: View) {
        btn_order_detail.setOnClickListener {
            OrderPriceSummaryBottomSheet().show(this)
        }

        orderProductCard = OrderProductCard(view, this)

        orderPreferenceCard = OrderPreferenceCard(view, this, getOrderPreferenceCardListener())
        orderPreferenceCard.initView()

//        btn_pay.isEnabled = true
//        btn_pay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
//        btn_pay.setPadding(convertDpToPixel(32f, context!!), btn_pay.paddingTop, convertDpToPixel(32f, context!!), btn_pay.paddingBottom)

//        triggerLoading()
    }

    private fun triggerLoading() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(4000)
            btn_pay.layoutParams.width = convertDpToPixel(160f, context!!)
            btn_pay.setText(R.string.label_choose_payment)
            btn_pay.isLoading = false
            btn_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
////            btn_pay.width = convertDpToPixel(140f, context!!)
            delay(3000)
            btn_pay.isLoading = true
            delay(3000)
//            SpecificErrorBottomSheet().create(context!!).show(fragmentManager!!, null)
            btn_pay.layoutParams.width = convertDpToPixel(140f, context!!)
            btn_pay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
            btn_pay.isLoading = false
            btn_pay.post {
                btn_pay.setText(R.string.pay)
            }
            delay(3000)
            btn_pay.isEnabled = false
        }
    }

    private fun getOrderPreferenceCardListener() = object : OrderPreferenceCard.OrderPreferenceCardListener {

        override fun onChangePreferenceClicked() {
            showPreferenceListBottomSheet()
        }

//        override fun onCourierChange() {
//            viewModel.updateCourier()
//        }
    }

    fun showPreferenceListBottomSheet() {
        PreferenceListBottomSheet(useCase = viewModel.getPreferenceListUseCase, listener = object : PreferenceListBottomSheet.PreferenceListBottomSheetListener {
            override fun onChangePreference(preference: ProfilesItemModel) {
//                viewModel.updatePreference(preference)
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

            override fun onAddPreference() {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
                startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
            }
        }).show(this@OrderSummaryPageFragment)
    }

    override fun onProductChange(product: OrderProduct, shouldReloadRates: Boolean) {
        viewModel.updateProduct(product, shouldReloadRates)
    }

    fun isLoading(): Boolean {
        return viewModel.orderTotal.value?.buttonState == ButtonBayarState.LOADING
    }

    fun onChooseCourier(shippingCourierViewModel: ShippingCourierUiModel) {
        viewModel.chooseCourier(shippingCourierViewModel)
    }

    companion object {

        const val REQUEST_EDIT_PREFERENCE = 11
        const val REQUEST_CREATE_PREFERENCE = 12
    }
}