package com.tokopedia.tradein.view.viewcontrollers.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.utils.TradeInPDPHelper
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInEducationalPageFragment
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInHomePageFragment
import com.tokopedia.tradein.viewmodel.TradeInHomePageVM
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class TradeInHomePageActivity : BaseViewModelActivity<TradeInHomePageVM>(),
    TradeInEducationalPageFragment.OnDoTradeInClick {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics

    private lateinit var viewModel: TradeInHomePageVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setObservers()
        viewModel.setLaku6(this)
        viewModel.checkLogin()
    }

    private fun setObservers() {
        viewModel.askUserLogin.observe(this, Observer {
            if (it != null && it == TradeinConstants.LOGIN_REQUIRED) {
                startActivityForResult(
                    RouteManager.getIntent(this, ApplinkConst.LOGIN),
                    LOGIN_REQUEST
                )
            } else {
                askPermissions()
            }
        })
    }

    private fun askPermissions() {
        if (!viewModel.isPermissionGranted()) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE),
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE)
        } else {
            setUpEducationalFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_SETTINGS) {
            askPermissions()
        } else if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK)
                viewModel.checkLogin()
            else
                finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.isNotEmpty() && permissions.size == grantResults.size) {
                for (i in permissions.indices) {
                    val result = grantResults[i]
                    if (result == PackageManager.PERMISSION_DENIED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]
                                ?: "")) {
                            showToast(getString(R.string.tradein_permission_setting),
                                getString(R.string.tradein_ok)) {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                intent.data = Uri.parse("package:" + this.packageName)
                                this.startActivityForResult(intent, APP_SETTINGS)
                            }
                        } else {
                            showToast(getString(R.string.tradein_requires_permission_for_diagnostic),
                                getString(R.string.tradein_ok)) { askPermissions() }
                        }
                        return
                    }
                }
                setUpEducationalFragment()
            } else {
                showToast(getString(R.string.tradein_requires_permission_for_diagnostic),
                    getString(R.string.tradein_ok)) { askPermissions() }
            }
        }
    }

    private fun setUpEducationalFragment() {
        val newFragment = TradeInEducationalPageFragment.getFragmentInstance()
        (newFragment as TradeInEducationalPageFragment).setUpTradeInClick(this)
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, newFragment, newFragment.tag)
            .commit()
    }

    override fun onClick() {
        supportFragmentManager.popBackStack()
        setUpFragment()
    }

    override fun onBackClick() {
        onBackPressed()
    }

    private fun setUpFragment() {
        intent.getStringExtra(TradeInPDPHelper.TRADE_IN_PDP_CACHE_ID)?.let {
            val newFragment = TradeInHomePageFragment.getFragmentInstance(it)
            supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, newFragment.tag)
                .commit()
        }

    }

    private fun showToast(message: String, actionText: String, listener: View.OnClickListener) {
        Toaster.build(findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_NORMAL, actionText, listener).show()
    }

    override fun initInject() {
        DaggerTradeInComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInHomePageVM> {
        return TradeInHomePageVM::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInHomePageVM
    }

    private fun goToCheckout(deviceId: String?, displayName:String?, price: String) {
        val intent = Intent(TradeinConstants.ACTION_GO_TO_SHIPMENT)
        intent.putExtra(TradeInPDPHelper.PARAM_DEVICE_ID, deviceId)
        intent.putExtra(TradeInPDPHelper.PARAM_PHONE_TYPE, displayName)
        intent.putExtra(TradeInPDPHelper.PARAM_PHONE_PRICE, price)
        setResult(RESULT_OK, intent)
        finish()
    }

}