package com.tokopedia.tradein.view.viewcontrollers.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.authentication.AuthKey
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.iris.IrisAnalytics.Companion.getInstance
import com.tokopedia.tradein.Constants
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInGTMConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.activity.BaseTradeInActivity.TRADEIN_OFFLINE
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInAddressFragment
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInInitialPriceFragment
import com.tokopedia.tradein.viewmodel.HomeResult
import com.tokopedia.tradein.viewmodel.HomeResult.PriceState
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import timber.log.Timber
import java.util.*
import javax.inject.Inject

const val TRADEIN_DISCOVERY_INFO = "tokopedia://discovery/tukar-tambah-edukasi"

class TradeInHomeActivity : BaseViewModelActivity<TradeInHomeViewModel>(), TradeInInitialPriceFragment.TradeInInitialPriceClick {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInHomeViewModel
    private var isFirstTime = true
    private lateinit var laku6TradeIn: Laku6TradeIn
    private lateinit var currentFragment: Fragment
    private var inputImei = false

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            viewModel.processMessage(intent)
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
//                if (TradeInGTMConstants.CEK_FISIK == page) {
//                    if (TradeInGTMConstants.CLICK_SALIN == action || TradeInGTMConstants.CLICK_SOCIAL_SHARE == action)
//                        sendGeneralEvent(clickEvent,
//                                cekFisik, action, value)
//                } else if (TradeInGTMConstants.CEK_FUNGSI_TRADE_IN == page) {
//                    sendGeneralEvent(clickEvent,
//                            cekFungsi, action, value)
//                } else if (TradeInGTMConstants.CEK_FISIK_RESULT_TRADE_IN == page) {
//                    sendGeneralEvent(viewEvent,
//                            cekFisikResult, action, value)
//                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setObservers()
    }

    private fun setObservers() {
        viewModel.askUserLogin.observe(this, Observer {
            if (it != null && it == Constants.LOGIN_REQUIRED) {
                startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), BaseTradeInActivity.LOGIN_REQUEST)
            } else {
                RouteManager.route(this, TRADEIN_DISCOVERY_INFO)
            }
        })
        viewModel.homeResultData.observe(this, Observer { homeResult: HomeResult ->
            if (!homeResult.isSuccess) {
                (currentFragment as TradeInInitialPriceFragment).notElligible(getString(R.string.not_elligible),
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(0, true))
            } else {
                when (homeResult.priceStatus) {
                    PriceState.DIAGNOSED_INVALID -> {
                        (currentFragment as TradeInInitialPriceFragment).notElligible(getString(R.string.not_elligible_price_high),
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(0, true))
                    }
                    PriceState.DIAGNOSED_VALID -> {
                        (currentFragment as TradeInInitialPriceFragment).isElligible(homeResult.displayMessage)
                        sendIrisEvent(if (homeResult.maxPrice != null) homeResult.maxPrice else 0, if (homeResult.minPrice != null) homeResult.minPrice else 0)
//                        goToHargaFinal(homeResult.deviceDisplayName)
                    }
                    PriceState.NOT_DIAGNOSED -> {
                        (currentFragment as TradeInInitialPriceFragment).isElligible(homeResult.displayMessage)
                        sendIrisEvent(if (homeResult.maxPrice != null) homeResult.maxPrice else 0, if (homeResult.minPrice != null) homeResult.minPrice else 0)
                        if (inputImei) {
                            laku6TradeIn.startGUITest()
                        }
                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun askPermissions() {
        if (!laku6TradeIn.permissionGranted()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA),
                    BaseTradeInActivity.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE)
        }
    }

    private fun setFragment() {
        viewModel.tradeInParams.apply {
            intent.data?.lastPathSegment?.let {
                if (it == TRADEIN_SELLER_CHECK) {
                    currentFragment = TradeInAddressFragment.getFragmentInstance(origin, weight, productName)
                    supportFragmentManager.beginTransaction()
                            .replace(parentViewResourceID, currentFragment, tagFragment)
                            .commit()
                } else {
                    currentFragment = TradeInInitialPriceFragment
                            .getFragmentInstance(productName, productImage,
                                    CurrencyFormatUtil.convertPriceValueToIdrFormat(newPrice, true),
                                    getTradeInDeviceId())
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                            .add(parentViewResourceID, currentFragment, tagFragment)
                            .addToBackStack("")
                            .commit()
                    (currentFragment as TradeInInitialPriceFragment).setListener(this@TradeInHomeActivity)
                    viewModel.getMaxPrice(laku6TradeIn, TRADEIN_OFFLINE)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        intent.data?.lastPathSegment?.let {
            if (it == TRADEIN && !isFirstTime)
                finish()
            else
                isFirstTime = false
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getTradeInParams()
        setIntent(intent)
        init()
    }

    private fun init() {
        setFragment()
        var campaignId = Constants.CAMPAIGN_ID_PROD
        if (Constants.LAKU6_BASEURL == Constants.LAKU6_BASEURL_STAGING) campaignId = Constants.CAMPAIGN_ID_STAGING
        laku6TradeIn = Laku6TradeIn.getInstance(this, campaignId,
                Constants.APPID, Constants.APIKEY, Constants.LAKU6_BASEURL, BaseTradeInActivity.TRADEIN_EXCHANGE, AuthKey.SAFETYNET_KEY_TRADE_IN)
    }

    private fun getTradeInParams() {
        if (intent.getParcelableExtra<TradeInParams>(TradeInParams::class.java.simpleName) != null)
            viewModel.tradeInParams = intent.getParcelableExtra(TradeInParams::class.java.simpleName)
                    ?: TradeInParams()
    }

    private fun getTradeInDeviceId(): String? {
        return TradeInUtils.getDeviceId(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BaseTradeInActivity.APP_SETTINGS) {
            askPermissions()
        } else if (requestCode == BaseTradeInActivity.LOGIN_REQUEST) {
            if (resultCode == RESULT_OK)
                viewModel.checkLogin()
            else
                finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == BaseTradeInActivity.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.isNotEmpty() && permissions.size == grantResults.size) {
                for (i in permissions.indices) {
                    val result = grantResults[i]
                    if (result == PackageManager.PERMISSION_DENIED) {
                        (currentFragment as TradeInAddressFragment).setPermissionDeniedUI()
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]
                                        ?: "")) {
                            showToast(getString(R.string.tradein_permission_setting),
                                    getString(R.string.tradein_ok), View.OnClickListener {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                intent.data = Uri.parse("package:" + this.packageName)
                                this.startActivityForResult(intent, BaseTradeInActivity.APP_SETTINGS)
                            })
                        } else {
                            showToast(getString(R.string.tradein_requires_permission_for_diagnostic),
                                    getString(R.string.tradein_ok), View.OnClickListener { askPermissions() })
                        }
                        return
                    }
                }
            } else {
                askPermissions()
            }
        }
    }

    override fun onClick(imei: String?) {
        if (imei != null) {
            inputImei = true
            viewModel.getIMEI(laku6TradeIn, imei)
        } else {
            laku6TradeIn.startGUITest()
        }
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInHomeViewModel
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInHomeViewModel> {
        return TradeInHomeViewModel::class.java
    }

    override fun initInject() {
        DaggerTradeInComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    private fun sendIrisEvent(maxPrice: Int, minPrice: Int) {
        val values = HashMap<String, Any>()
        values[TradeInGTMConstants.EVENT] = TradeInGTMConstants.ACTION_VIEW_TRADEIN_IRIS
        values[TradeInGTMConstants.EVENT_CATEGORY] = TradeInGTMConstants.CATEGORY_TRADEIN_START_PAGE
        values[TradeInGTMConstants.EVENT_ACTION] = TradeInGTMConstants.VIEW_PRICE_RANGE_PAGE
        values[TradeInGTMConstants.EVENT_LABEL] = getString(R.string.trade_in_event_label_phone_type_min_price_max_price,
                viewModel.tradeInParams.productName, minPrice.toString(), maxPrice.toString())
        values[TradeInGTMConstants.PRODUCT_ID] = viewModel.tradeInParams.productId
        getInstance(this).sendEvent(values)
    }

    override fun onStart() {
        super.onStart()
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(mMessageReceiver, IntentFilter("laku6-test-end"))
        localBroadcastManager.registerReceiver(mBackReceiver, IntentFilter("laku6-back-action"))
        localBroadcastManager.registerReceiver(laku6GTMReceiver, IntentFilter("laku6-gtm"))
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

    private fun showToast(message: String, actionText: String, listener: View.OnClickListener) {
        Toaster.build(findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_NORMAL, actionText, listener).show()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TRADEIN = "tradein"
        private const val TRADEIN_SELLER_CHECK = "tradein_seller_check"
    }
}