package com.tokopedia.oneclickcheckout.preference.edit.view

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.di.DaggerPreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditModule
import com.tokopedia.oneclickcheckout.preference.edit.view.address.AddressListFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.summary.PreferenceSummaryFragment
import kotlinx.android.synthetic.main.activity_preference_edit.*
import javax.inject.Inject

class PreferenceEditActivity : BaseActivity(), HasComponent<PreferenceEditComponent>, PreferenceEditParent {

    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    private var _preferenceIndex = ""
    private var _profileId = 0
    private var _addressId = 0
    private var _shippingId = 0
    private var _gatewayCode = ""
    private var _paymentQuery = ""
    private var _shippingParam: ShippingParam? = null
    private var _listShopShipment: ArrayList<ShopShipment>? = null
    private var _isExtraProfile: Boolean = true
    private var _fromFlow = FROM_FLOW_PREF

    override fun getComponent(): PreferenceEditComponent {
        return DaggerPreferenceEditComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .preferenceEditModule(PreferenceEditModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_edit)
        initViews()
        component.inject(this)
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        val lastFragments = fragments[fragments.lastIndex]
        val checkLastFragment = checkLastFragment(lastFragments)
        if (!checkLastFragment && fragments.size > 1) {
            checkLastFragment(fragments[fragments.lastIndex - 1])
        }
        super.onBackPressed()
    }

    private fun initViews() {
        btn_back.setOnClickListener {
            onBackPressed()
        }

        _preferenceIndex = intent.getStringExtra(EXTRA_PREFERENCE_INDEX) ?: ""
        _profileId = intent.getIntExtra(EXTRA_PROFILE_ID, 0)
        _shippingParam = intent.getParcelableExtra(EXTRA_SHIPPING_PARAM)
        _listShopShipment = intent.getParcelableArrayListExtra(EXTRA_LIST_SHOP_SHIPMENT)
        _isExtraProfile = intent.getBooleanExtra(EXTRA_IS_EXTRA_PROFILE, true)
        _fromFlow = intent.getIntExtra(EXTRA_FROM_FLOW, FROM_FLOW_PREF)

        val ft = supportFragmentManager.beginTransaction()
        val fragments = supportFragmentManager.fragments
        if (fragments.size > 0) {
            for (fragment in fragments) {
                ft.remove(fragment)
            }
        }
        if (_profileId == 0) {
            ft.replace(R.id.container, AddressListFragment.newInstance()).commit()
        } else {
            ft.replace(R.id.container, PreferenceSummaryFragment.newInstance(true)).commit()
        }
    }

    private fun checkLastFragment(lastFragments: Fragment): Boolean {
        when (lastFragments) {
            is PreferenceSummaryFragment -> {
                preferenceListAnalytics.eventClickBackArrowInEditPreference()
                return true
            }
            is AddressListFragment -> {
                preferenceListAnalytics.eventClickBackArrowInPilihAlamat()
                return true
            }
            is ShippingDurationFragment -> {
                preferenceListAnalytics.eventClickBackArrowInPilihDurasi()
                return true
            }
            is PaymentMethodFragment -> {
                preferenceListAnalytics.eventClickBackArrowInPilihPembayaran()
                return true
            }
            else -> {
                return false
            }
        }
    }

    override fun setPreferenceIndex(preferenceIndex: String) {
        _preferenceIndex = preferenceIndex
    }

    override fun getPreferenceIndex(): String {
        return _preferenceIndex
    }

    override fun setProfileId(profileId: Int) {
        _profileId = profileId
    }

    override fun getProfileId(): Int {
        return _profileId
    }

    override fun setAddressId(addressId: Int) {
        _addressId = addressId
    }

    override fun getAddressId(): Int {
        return _addressId
    }

    override fun setShippingId(shippingId: Int) {
        _shippingId = shippingId
    }

    override fun getShippingId(): Int {
        return _shippingId
    }

    override fun setGatewayCode(gatewayCode: String) {
        _gatewayCode = gatewayCode
    }

    override fun getGatewayCode(): String {
        return _gatewayCode
    }

    override fun setPaymentQuery(paymentQuery: String) {
        _paymentQuery = paymentQuery
    }

    override fun getPaymentQuery(): String {
        return _paymentQuery
    }

    override fun setShippingParam(shippingParam: ShippingParam) {
        _shippingParam = shippingParam
    }

    override fun getShippingParam(): ShippingParam? {
        return _shippingParam
    }

    override fun getListShopShipment(): ArrayList<ShopShipment>? {
        return _listShopShipment
    }

    override fun isExtraProfile(): Boolean {
        return _isExtraProfile
    }

    override fun setHeaderTitle(title: String) {
        tv_title.text = title
    }

    override fun setHeaderSubtitle(subtitle: String) {
        tv_subtitle.text = subtitle
    }

    override fun addFragment(fragment: Fragment) {
        if (supportFragmentManager.isStateSaved) {
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onStart(owner: LifecycleOwner) {
                    if (owner is FragmentActivity) {
                        owner.supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit()
                    }
                    owner.lifecycle.removeObserver(this)
                }
            })
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit()
        }
    }

    override fun goBack() {
        if (supportFragmentManager.isStateSaved) {
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onStart(owner: LifecycleOwner) {
                    if (owner is FragmentActivity) {
                        owner.supportFragmentManager.popBackStack()
                    }
                    owner.lifecycle.removeObserver(this)
                }
            })
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun setStepperValue(value: Int, isSmooth: Boolean) {
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

    override fun showStepper() {
        tv_subtitle.visible()
        stepper.visible()
    }

    override fun hideStepper() {
        tv_subtitle.gone()
        stepper.gone()
    }

    override fun showAddButton() {
        hideDeleteButton()
        btn_add.visible()
    }

    override fun hideAddButton() {
        btn_add.gone()
    }

    override fun showDeleteButton() {
        hideAddButton()
        btn_delete.visible()
    }

    override fun hideDeleteButton() {
        btn_delete.gone()
    }

    override fun setDeleteButtonOnClickListener(onClick: () -> Unit) {
        btn_delete.setOnClickListener {
            onClick()
        }
    }

    override fun setAddButtonOnClickListener(onClick: () -> Unit) {
        btn_add.setOnClickListener {
            onClick()
        }
    }

    override fun getFromFlow(): Int {
        return _fromFlow
    }

    companion object {

        const val EXTRA_PREFERENCE_INDEX = "preference_index"
        const val EXTRA_PROFILE_ID = "profile_id"
        const val EXTRA_ADDRESS_ID = "address_id"
        const val EXTRA_SHIPPING_ID = "shipping_id"
        const val EXTRA_GATEWAY_CODE = "gateway_code"
        const val EXTRA_IS_EXTRA_PROFILE = "is_extra_profile"

        const val EXTRA_SHIPPING_PARAM = "shipping_param"
        const val EXTRA_LIST_SHOP_SHIPMENT = "list_shop_shipment"

        const val EXTRA_FROM_FLOW = "from_flow"
        const val FROM_FLOW_OSP = 1
        const val FROM_FLOW_PREF = 0

        const val EXTRA_RESULT_MESSAGE = "RESULT_MESSAGE"
    }
}
