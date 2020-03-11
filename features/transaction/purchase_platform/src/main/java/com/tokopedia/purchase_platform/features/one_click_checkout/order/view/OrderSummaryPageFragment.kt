package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.utils.Utils.convertDpToPixel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.OrderSummaryPageComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.PreferenceListBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.*
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.*
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

        if (requestCode == REQUEST_CREATE_PREFERENCE || requestCode == REQUEST_EDIT_PREFERENCE) {
            showPreferenceListBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_summary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.orderPreference.observe(this, Observer {
            view?.let {
                Toaster.make(it, "success")
            }
            if (viewModel.orderProduct.quantity != null) {
                orderProductCard.setProduct(viewModel.orderProduct)
                orderProductCard.setShop(viewModel.orderShop)
                orderProductCard.initView()
                showMessage(it.preference)
                if (it.preference.address.addressId > 0) {
                    orderPreferenceCard.setPreference(it)
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

        override fun onCourierChange() {
            viewModel.updateCourier()
        }
    }

    fun showPreferenceListBottomSheet() {
        PreferenceListBottomSheet(useCase = viewModel.getPreferenceListUseCase, listener = object : PreferenceListBottomSheet.PreferenceListBottomSheetListener {
            override fun onChangePreference(preference: ProfilesItemModel) {
//                viewModel.updatePreference(preference)
            }

            override fun onEditPreference(preference: ProfilesItemModel) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
                intent.apply {
                    putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, 1)
                    putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, 1)
                    putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, "")
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

    companion object {

        const val REQUEST_EDIT_PREFERENCE = 11
        const val REQUEST_CREATE_PREFERENCE = 12
    }
}