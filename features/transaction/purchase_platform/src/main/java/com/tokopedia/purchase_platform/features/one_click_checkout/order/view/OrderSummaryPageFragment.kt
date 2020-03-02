package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.utils.Utils.convertDpToPixel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.OrderSummaryPageComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.PreferenceListBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.SpecificErrorBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.*
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.*
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
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
    }

    private fun initViews(view: View) {
        btn_order_detail.setOnClickListener {
            OrderPriceSummaryBottomSheet().show(this)
        }

        orderProductCard = OrderProductCard(view, this)
        val product = OrderProduct()
        product.apply {
            quantity = QuantityUiModel("", 100, 1, 1, "", "", "", "", "", "", "", false, 100, "")

            selectedVariantOptionsIdMap = linkedMapOf(
                    9798944 to 41522204,
                    9798945 to 29773874
            )

            productChildrenList = arrayListOf(
                    OrderProductChild(632090913, "", 398000, "", true, true, "", 100, 1, 52, arrayListOf(41522203, 29773874)),
                    OrderProductChild(632090914, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522203, 29773875)),
                    OrderProductChild(632090915, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522203, 29773876)),
                    OrderProductChild(632090916, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522203, 29773877)),
                    OrderProductChild(632090917, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522203, 29773878)),
                    OrderProductChild(632090918, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522203, 29773879)),
                    OrderProductChild(632090919, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522204, 29773874)),
                    OrderProductChild(632090920, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522204, 29773875)),
                    OrderProductChild(632090921, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522204, 29773876)),
                    OrderProductChild(632090922, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522204, 29773877)),
                    OrderProductChild(632090923, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522204, 29773878)),
                    OrderProductChild(632090924, "", 398000, "", true, false, "", 100, 1, 52, arrayListOf(41522204, 29773879))
            )

            typeVariantList = arrayListOf(
                    TypeVariantUiModel(
                            9798944, "warna", "Hitam merah", "", "", arrayListOf(
                            OptionVariantUiModel(
                                    9798944, 41522204, OptionVariantUiModel.STATE_SELECTED, "#ffffff", "Hitam merah", true
                            ), OptionVariantUiModel(
                            9798944, 41522203, OptionVariantUiModel.STATE_NOT_SELECTED, "#ffffff", "Hitam polos", true
                    )
                    )
                    ), TypeVariantUiModel(
                    9798945, "ukuran", "39", "", "", arrayListOf(
                    OptionVariantUiModel(
                            9798945, 29773874, OptionVariantUiModel.STATE_SELECTED, "", "39", true
                    ), OptionVariantUiModel(
                    9798945, 29773875, OptionVariantUiModel.STATE_NOT_SELECTED, "", "40", true
            ), OptionVariantUiModel(
                    9798945, 29773876, OptionVariantUiModel.STATE_NOT_SELECTED, "", "41", true
            ), OptionVariantUiModel(
                    9798945, 29773877, OptionVariantUiModel.STATE_NOT_SELECTED, "", "42", true
            ), OptionVariantUiModel(
                    9798945, 29773878, OptionVariantUiModel.STATE_NOT_SELECTED, "", "43", true
            ), OptionVariantUiModel(
                    9798945, 29773879, OptionVariantUiModel.STATE_NOT_SELECTED, "", "44", true
            )
            )
            ),
                    SelectedTypeVariantUiModel(
                            arrayListOf(TypeVariantUiModel(
                                    9798944, "warna", "Hitam merah", "", "", arrayListOf(
                                    OptionVariantUiModel(
                                            9798944, 41522204, OptionVariantUiModel.STATE_SELECTED, "#ffffff", "Hitam merah", true
                                    ), OptionVariantUiModel(
                                    9798944, 41522203, OptionVariantUiModel.STATE_NOT_SELECTED, "#ffffff", "Hitam polos", true
                            )
                            )
                            ), TypeVariantUiModel(
                                    9798945, "ukuran", "39", "", "", arrayListOf(
                                    OptionVariantUiModel(
                                            9798945, 29773874, OptionVariantUiModel.STATE_SELECTED, "", "39", true
                                    ), OptionVariantUiModel(
                                    9798945, 29773875, OptionVariantUiModel.STATE_NOT_SELECTED, "", "40", true
                            ), OptionVariantUiModel(
                                    9798945, 29773876, OptionVariantUiModel.STATE_NOT_SELECTED, "", "41", true
                            ), OptionVariantUiModel(
                                    9798945, 29773877, OptionVariantUiModel.STATE_NOT_SELECTED, "", "42", true
                            ), OptionVariantUiModel(
                                    9798945, 29773878, OptionVariantUiModel.STATE_NOT_SELECTED, "", "43", true
                            ), OptionVariantUiModel(
                                    9798945, 29773879, OptionVariantUiModel.STATE_NOT_SELECTED, "", "44", true
                            )
                            )
                            ))
                    )
            )
        }
        orderProductCard.setProduct(product)
        orderProductCard.initView()

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
            SpecificErrorBottomSheet().create(context!!).show(fragmentManager!!, null)
//            btn_pay.layoutParams.width = convertDpToPixel(140f, context!!)
//            btn_pay.setText(R.string.pay)
//            btn_pay.isLoading = true
//            btn_pay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
//            delay(3000)
//            btn_pay.isEnabled = false
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
        PreferenceListBottomSheet(listener = object : PreferenceListBottomSheet.PreferenceListBottomSheetListener {
            override fun onChangePreference(preference: Preference) {
                viewModel.updatePreference(preference)
            }

            override fun onEditPreference(preference: Preference) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT)
                intent.apply {
                    putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, 1)
                    putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, 1)
                    putExtra(PreferenceEditActivity.EXTRA_PAYMENT_ID, 1)
                    putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                    putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, viewModel.generateListShopShipment())
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