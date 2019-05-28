package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.AddressConstants.*
import com.tokopedia.logisticaddaddress.R

/**
 * Created by fwidjaja on 2019-05-07.
 */
class PinpointMapActivity: BaseSimpleActivity() {
    companion object {
        val defaultLat: Double by lazy { -6.175794 }
        val defaultLong: Double by lazy { 106.826457 }

        /*@JvmStatic
        fun createInstanceFromCheckoutSingleAddressForm(activity: Activity,
                                                        token: Token?): Intent {
            return createInstance(activity, null, token, false,
                    false, INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT)

        }

        fun createInstance(
                activity: Activity,
                data: AddressModel?,
                token: Token?,
                isEdit: Boolean,
                isEmptyAddressFirst: Boolean,
                typeInstance: Int
        ): Intent {
            val intent = Intent(activity, PinpointMapActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(IS_DISTRICT_RECOMMENDATION, true)
            bundle.putString(EXTRA_PLATFORM_PAGE, PLATFORM_MARKETPLACE_CART)
            if (data != null)
                bundle.putParcelable(EDIT_PARAM, data.convertToDestination())
            bundle.putBoolean(EXTRA_FROM_CART_IS_EMPTY_ADDRESS_FIRST, isEmptyAddressFirst)
            bundle.putBoolean(IS_EDIT, isEdit)
            bundle.putParcelable(KERO_TOKEN, token)
            bundle.putInt(EXTRA_INSTANCE_TYPE, typeInstance)
            bundle.putDouble(EXTRA_DEFAULT_LAT, defaultLat)
            bundle.putDouble(EXTRA_DEFAULT_LONG, defaultLong)
            intent.putExtras(bundle)
            return intent
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_pinpoint_map

    override fun getNewFragment(): PinpointMapFragment? {
        var bundle = Bundle()
        if (intent.extras != null) @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        bundle = intent.extras else {
            bundle.putDouble(EXTRA_LAT, MONAS_LAT)
            bundle.putDouble(EXTRA_LONG, MONAS_LONG)
            bundle.putBoolean(EXTRA_SHOW_AUTOCOMPLETE, true)
        }
        return PinpointMapFragment.newInstance(bundle)
    }
}