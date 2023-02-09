package com.tokopedia.tradein.view.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.utils.TradeInPDPHelper
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.TradeInGTMConstants
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.view.fragment.TradeInEducationalPageFragment
import com.tokopedia.tradein.view.fragment.TradeInHomePageFragment
import com.tokopedia.tradein.viewmodel.TradeInHomePageVM
import com.tokopedia.tradein.viewmodel.liveState.GoToCheckout
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber
import javax.inject.Inject

const val APP_SETTINGS = 9988
const val LOGIN_REQUEST = 514
const val MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123

const val LAKU6_TEST_END ="laku6-test-end"
const val LAKU6_BACK_ACTION ="laku6-back-action"
const val LAKU6_GTM ="laku6-gtm"

class TradeInHomePageActivity : BaseViewModelActivity<TradeInHomePageVM>(),
    TradeInEducationalPageFragment.OnDoTradeInClick {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics

    private lateinit var viewModel: TradeInHomePageVM

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            viewModel.insertLogisticOptions(intent)
        }
    }

    private val mBackReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Timber.d("Do back action to parent")
        }
    }

    private val laku6GTMReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null && TradeInGTMConstants.ACTION_LAKU6_GTM == intent.action) {
                val page = intent.getStringExtra(TradeInGTMConstants.PAGE)
                val action = intent.getStringExtra(TradeInGTMConstants.ACTION)
                val value = intent.getStringExtra(TradeInGTMConstants.VALUE)
                val cekFisik = TradeInGTMConstants.CEK_FISIK_TRADE_IN
                val cekFungsi = TradeInGTMConstants.CEK_FUNGSI_TRADE_IN
                val cekFisikResult = TradeInGTMConstants.CEK_FISIK_RESULT_TRADE_IN
                when (page) {
                    TradeInGTMConstants.CEK_FISIK -> {
                        when (action) {
                            TradeInGTMConstants.CLICK_SALIN, TradeInGTMConstants.CLICK_SOCIAL_SHARE -> tradeInAnalytics.sendGeneralEvent(TradeInGTMConstants.ACTION_CLICK_TRADEIN,
                                cekFisik, action, value)
                        }
                    }
                    TradeInGTMConstants.CEK_FUNGSI_TRADE_IN -> {
                        tradeInAnalytics.sendGeneralEvent(TradeInGTMConstants.ACTION_CLICK_TRADEIN,
                            cekFungsi, action, value)
                    }
                    TradeInGTMConstants.CEK_FISIK_RESULT_TRADE_IN -> {
                        tradeInAnalytics.sendGeneralEvent(TradeInGTMConstants.ACTION_VIEW_TRADEIN,
                            cekFisikResult, action, value)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setObservers()
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
        viewModel.tradeInHomeStateLiveData.observe(this, Observer {
            when(it){
                is GoToCheckout -> goToCheckout(it.price)
            }
        })
        viewModel.addToCartLiveData.observe(this, Observer {
                if (it.errorReporter.eligible) {
                    showErrorToast(it.errorReporter.texts.submitTitle, getString(R.string.tradein_ok), {})
                } else {
                    goToCheckoutActivity(
                        ShipmentFormRequest.BundleBuilder()
                        .deviceId(viewModel.deviceId)
                        .build()
                        .bundle)
                }
        })
    }

    private fun askPermissions() {
        if (!viewModel.isPermissionGranted()) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE),
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                )
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE),
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE
                )
            }
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
        viewModel.setLaku6(this)

        val newFragment = TradeInEducationalPageFragment.getFragmentInstance()
        (newFragment as TradeInEducationalPageFragment).setUpTradeInClick(this)
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, newFragment, newFragment.tag)
            .commitAllowingStateLoss()
        tradeInAnalytics.openEducationalScreen()
    }

    override fun onClick() {
        tradeInAnalytics.clickEducationalPage()
        supportFragmentManager.popBackStack()
        setUpFragment()
    }

    override fun onBackClick() {
        onBackPressed()
    }

    private fun showErrorToast(
        message: String,
        actionText: String,
        listener: View.OnClickListener,
        duration: Int = Snackbar.LENGTH_LONG
    ) {
        Toaster.build(
            findViewById<FrameLayout>(com.tokopedia.abstraction.R.id.parent_view),
            message,
            duration, Toaster.TYPE_ERROR, actionText, listener
        ).show()
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

    private fun goToCheckout(finalPrice: String) {
        viewModel.data?.let { data->
            val addToCartOcsRequestParams = AddToCartOcsRequestParams().apply {
                productId = data.productId
                shopId = data.shopID.toZeroStringIfNull()
                quantity = data.minOrder
                notes = ""
                customerId = viewModel.userId
                warehouseId = data.selectedWarehouseId.toString()
                trackerAttribution = data.trackerAttributionPdp ?: ""
                trackerListName = data.trackerListNamePdp ?: ""
                isTradeIn = true
                shippingPrice = data.shippingMinimumPrice
                productName = data.getProductName ?: ""
                category = data.categoryName ?: ""
                price = finalPrice
                userId = viewModel.userId
            }
            viewModel.getAddToCartOcsUseCase(addToCartOcsRequestParams)
        }
    }

    private fun goToCheckoutActivity(shipmentFormRequest: Bundle) {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, true)
        intent.putExtras(shipmentFormRequest)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(mMessageReceiver, IntentFilter(LAKU6_TEST_END))
        localBroadcastManager.registerReceiver(mBackReceiver, IntentFilter(LAKU6_BACK_ACTION))
        localBroadcastManager.registerReceiver(laku6GTMReceiver, IntentFilter(LAKU6_GTM))
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            val localBroadcastManager = LocalBroadcastManager.getInstance(this)
            localBroadcastManager.unregisterReceiver(mMessageReceiver)
            localBroadcastManager.unregisterReceiver(mBackReceiver)
            localBroadcastManager.unregisterReceiver(laku6GTMReceiver)
        }
    }
}
