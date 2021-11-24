package com.tokopedia.manageaddress.ui.addresschoice

import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import android.os.Bundle
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.ui.addresschoice.recyclerview.ShipmentAddressListFragment
import com.tokopedia.purchase_platform.common.constant.CartConstant.SCREEN_NAME_CART_NEW_USER
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_REF
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.KERO_TOKEN

/**
 * @author Irfan Khoirul on 05/02/18
 * Aghny A. Putra on 07/02/18
 * Fajar U N
 */
class CartAddressChoiceActivity : BaseCheckoutActivity(), ShipmentAddressListFragment.ICartAddressChoiceActivityListener {
    private var typeRequest = 0
    private var token: Token? = null
    private var prevState = 0
    private val localChosenAddr: LocalCacheModel? = null
    private val PARAM_ADDRESS_MODEL = "EDIT_PARAM"
    private val mAnalytics = CheckoutAnalyticsChangeAddress()
    override fun setupBundlePass(extras: Bundle?) {
        extras?.let {
            typeRequest = it.getInt(CheckoutConstant.EXTRA_TYPE_REQUEST)
            token = it.getParcelable(CheckoutConstant.EXTRA_DISTRICT_RECOMMENDATION_TOKEN)
            prevState = it.getInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS)
        }
    }

    override fun initView() {
        updateTitle(getString(R.string.checkout_module_title_activity_shipping_address))
        val intent: Intent
        when (typeRequest) {
            CheckoutConstant.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS -> {
                intent = RouteManager.getIntent(this, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
                intent.putExtra(KERO_TOKEN, token)
                intent.putExtra(EXTRA_REF, SCREEN_NAME_CART_NEW_USER)
                startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY)
            }
            CheckoutConstant.TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN -> {
                val currentAddress: RecipientAddressModel? = getIntent().getParcelableExtra(CheckoutConstant.EXTRA_CURRENT_ADDRESS)
                val mapper = AddressModelMapper()
                intent = RouteManager.getIntent(this, ApplinkConstInternalLogistic.ADD_ADDRESS_V1,
                        LogisticConstant.INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT)
                intent.putExtra(PARAM_ADDRESS_MODEL, mapper.transform(currentAddress))
                intent.putExtra(KERO_TOKEN, token)
                startActivityForResult(intent, LogisticConstant.REQUEST_CODE_PARAM_EDIT)
            }
            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LogisticConstant.REQUEST_CODE_PARAM_CREATE ||
                requestCode == LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY) {
            if (resultCode == RESULT_OK) setResult(RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS)
            finish()
        } else if (requestCode == LogisticConstant.REQUEST_CODE_PARAM_EDIT) {
            setResult(RESULT_CODE_ACTION_EDIT_ADDRESS)
            finish()
        }
    }

    override fun finishAndSendResult(selectedAddressResult: RecipientAddressModel?) {
        val resultIntent: Intent
        when (typeRequest) {
            CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST, CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN -> {
                resultIntent = Intent()
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult)
                setResult(CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent)
                finish()
            }
            CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS -> {
                resultIntent = Intent()
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult)
                if (intent.hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST,
                            intent.getParcelableArrayListExtra<Parcelable>(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST))
                }
                if (intent.hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX,
                            intent.getIntExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX, -1))
                }
                if (intent.hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX,
                            intent.getIntExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1))
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
            CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT -> {
                resultIntent = Intent()
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult)
                if (intent.hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST,
                            intent.getParcelableArrayListExtra<Parcelable>(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST))
                }
                if (intent.hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX,
                            intent.getIntExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1))
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
            else -> {
            }
        }
    }

    override fun getNewFragment(): Fragment? {
        val currentAddress: RecipientAddressModel = intent.getParcelableExtra(CheckoutConstant.EXTRA_CURRENT_ADDRESS) ?: RecipientAddressModel()
        return when (typeRequest) {
            CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST -> ShipmentAddressListFragment.newInstance(currentAddress, prevState)
            CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN -> ShipmentAddressListFragment.newInstance(currentAddress, CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN)
            CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT, CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS -> ShipmentAddressListFragment.newInstanceFromMultipleAddressForm(currentAddress, prevState)
            else -> ShipmentAddressListFragment.newInstance(currentAddress, prevState)
        }
    }

    override fun onBackPressed() {
        mAnalytics.eventClickAtcCartChangeAddressClickArrowBackFromGantiAlamat()
        super.onBackPressed()
    }

    companion object {
        // Attention !!
        // If these constants will be used on other module, please move into CheckoutConstant.kt class on package purchase_platform_common
        const val RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS = 102
        const val RESULT_CODE_ACTION_EDIT_ADDRESS = 103
    }
}