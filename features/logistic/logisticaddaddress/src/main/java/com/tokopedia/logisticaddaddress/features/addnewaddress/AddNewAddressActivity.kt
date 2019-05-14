package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.AddressConstants.*
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressActivity
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressFragment
import com.tokopedia.logisticdata.data.entity.address.AddressModel
import com.tokopedia.logisticdata.data.entity.address.Token

/**
 * Created by fwidjaja on 2019-05-07.
 */
class AddNewAddressActivity: BaseSimpleActivity() {
    companion object {
        private val defaultLat: Double by lazy { -6.175794 }
        private val defaultLong: Double by lazy { 106.826457 }

        @JvmStatic
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
            val intent = Intent(activity, AddNewAddressActivity::class.java)
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_add_new_address

    override fun getNewFragment(): MapFragment? {
        /*var fragment: Fragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = AddNewAddressFragment.newInstance(bundle)
        }*/
        // return MapFragment.newInstance(defaultLat, defaultLong)

        var fragment: MapFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = MapFragment.newInstance(bundle)
        }
        return fragment
    }

    private fun setupToolbar() {
        /*setSupportActionBar(toolbar_add_new_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)*/

        /*toolbar_add_new_address.setNavigationIcon(R.drawable.ic_icon_back_black)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.title = this.title
            updateTitle("")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }*/
    }
}