package com.tokopedia.product.manage.feature.cashback.presentation.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.EXTRA_CASHBACK_IS_DRAFTING
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.EXTRA_CASHBACK_SHOP_ID
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_PRODUCT_PRICE
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment.Companion.PARAM_SET_CASHBACK_VALUE
import com.tokopedia.utils.accelerometer.orientation.AccelerometerOrientationListener

class ProductManageSetCashbackActivity: BaseSimpleActivity() {

    companion object {
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_PRODUCT_NAME = "product_name"
        const val PARAM_CASHBACK = "cashback"
        const val PARAM_PRICE = "price"

        fun createIntent(context: Context, productId: String?, productName: String?, cashback: Int?, price: String?) : Intent {
            val intent = Intent(context, ProductManageSetCashbackActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_PRODUCT_ID, productId)
            bundle.putString(PARAM_PRODUCT_NAME, productName)
            bundle.putInt(PARAM_CASHBACK, cashback ?: 0)
            bundle.putString(PARAM_PRICE, price)
            intent.putExtras(bundle)
            return intent
        }
    }

    private val accelerometerOrientationListener: AccelerometerOrientationListener by lazy {
        AccelerometerOrientationListener(contentResolver) {
            onAccelerometerOrientationSettingChange(it)
        }
    }

    override fun getNewFragment(): Fragment? {
        var productId = "0"
        var cashback = 0
        var productName = ""
        var price = ""
        val uri = intent.data
        if (uri != null) {
            val segments = uri.pathSegments
            productId = segments[segments.size - 1]
            productName = uri.getQueryParameter(PARAM_PRODUCT_NAME).toBlankOrString()
            cashback = uri.getQueryParameter(PARAM_SET_CASHBACK_VALUE).toIntOrZero()
            price = uri.getQueryParameter(PARAM_SET_CASHBACK_PRODUCT_PRICE) ?: ""
        } else {
            intent.extras?.let {
                productId = it.getString(PARAM_PRODUCT_ID, "")
                productName = it.getString(PARAM_PRODUCT_NAME, "")
                cashback = it.getInt(PARAM_CASHBACK)
                price = it.getString(PARAM_PRICE, "")
            }
        }
        val shopId = intent?.getStringExtra(EXTRA_CASHBACK_SHOP_ID) ?: ""
        val isDrafting = intent?.getBooleanExtra(EXTRA_CASHBACK_IS_DRAFTING, false) ?: false
        if (shopId != "") {
            return ProductManageSetCashbackFragment.createInstance(productId, cashback, productName, price, shopId, isDrafting)
        }
        return ProductManageSetCashbackFragment.createInstance(productId, cashback, productName, price)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setActivityOrientation()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val f = fragment
        if(f != null && f is ProductManageSetCashbackFragment) {
            f.setTrackerOnBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.register()
        }
    }

    override fun onPause() {
        super.onPause()
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.unregister()
        }
    }

    private fun onAccelerometerOrientationSettingChange(isEnabled: Boolean) {
        if (DeviceScreenInfo.isTablet(this)) {
            requestedOrientation = if (isEnabled)
                ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            else
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        }
    }

    private fun setActivityOrientation() {
        requestedOrientation = if (DeviceScreenInfo.isTablet(this)) {
            val isAccelerometerRotationEnabled = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            ) == 1

            if (isAccelerometerRotationEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        } else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}