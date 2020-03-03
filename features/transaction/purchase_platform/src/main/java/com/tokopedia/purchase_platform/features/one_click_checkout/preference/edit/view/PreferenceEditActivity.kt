package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.DaggerPreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address.AddressListFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.summary.PreferenceSummaryFragment
import kotlinx.android.synthetic.main.activity_preference_edit.*

class PreferenceEditActivity : BaseActivity(), HasComponent<PreferenceEditComponent> {

    var addressId = -1
    var shippingId = -1
    var gatewayCode = ""
    var paymentQuery = ""
    var shippingParam: ShippingParam? = null
    var listShopShipment: ArrayList<ShopShipment>? = null

    override fun getComponent(): PreferenceEditComponent {
        return DaggerPreferenceEditComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_edit)
        initViews()
    }

    private fun initViews() {
        btn_back.setOnClickListener {
            onBackPressed()
        }

        addressId = intent.getIntExtra(EXTRA_ADDRESS_ID, -1)
        shippingId = intent.getIntExtra(EXTRA_SHIPPING_ID, -1)
        gatewayCode = intent.getStringExtra(EXTRA_GATEWAY_CODE) ?: ""
        shippingParam = intent.getParcelableExtra(EXTRA_SHIPPING_PARAM)
        listShopShipment = intent.getParcelableArrayListExtra(EXTRA_LIST_SHOP_SHIPMENT)

        if (addressId == -1 || shippingId == -1 || gatewayCode.isNotBlank()) {
            supportFragmentManager.beginTransaction().replace(R.id.container, PaymentMethodFragment.newInstance()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container, PreferenceSummaryFragment.newInstance(true))
        }
    }

    fun setHeaderTitle(title: String) {
        tv_title.text = title
    }

    fun setHeaderSubtitle(subtitle: String) {
        tv_subtitle.text = subtitle
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit()
    }

    fun setStepperValue(value: Int, isSmooth: Boolean = true) {
        if (isSmooth && stepper != null) {
            try {
                ObjectAnimator.ofInt(stepper, "progress", value).setDuration(500).start()
            } catch (e: Exception) {
                stepper.progress = value
            }
        } else {
            stepper.progress = value
        }
    }

    fun showStepper() {
        tv_subtitle.visible()
        stepper.visible()
    }

    fun hideStepper() {
        tv_subtitle.gone()
        stepper.gone()
    }

    fun showAddButton() {
        hideDeleteButton()
        btn_add.visible()
    }

    fun hideAddButton() {
        btn_add.gone()
    }

    fun showDeleteButton() {
        hideAddButton()
        btn_delete.visible()
    }

    fun hideDeleteButton() {
        btn_delete.gone()
    }

    fun setDeleteButtonOnClickListener(onClick: () -> Unit) {
        btn_delete.setOnClickListener {
            onClick()
        }
    }

    fun setAddButtonOnClickListener(onClick: () -> Unit) {
        btn_add.setOnClickListener {
            onClick()
        }
    }

    companion object {

        const val EXTRA_ADDRESS_ID = "address_id"
        const val EXTRA_SHIPPING_ID = "shipping_id"
        const val EXTRA_GATEWAY_CODE = "gateway_code"

        const val EXTRA_SHIPPING_PARAM = "shipping_param"
        const val EXTRA_LIST_SHOP_SHIPMENT = "list_shop_shipment"
    }
}
