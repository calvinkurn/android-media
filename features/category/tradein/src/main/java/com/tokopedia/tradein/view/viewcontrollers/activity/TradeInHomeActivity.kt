package com.tokopedia.tradein.view.viewcontrollers.activity

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
import com.tokopedia.iris.IrisAnalytics.Companion.getInstance
import com.tokopedia.keys.Keys
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tradein.R
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.TradeInGTMConstants
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.TradeinConstants.Deeplink.TRADEIN_DISCOVERY_INFO_URL
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.activity.BaseTradeInActivity.TRADEIN_OFFLINE
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInAddressFragment
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInFinalPriceFragment
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInInitialPriceFragment
import com.tokopedia.tradein.viewmodel.HomeResult
import com.tokopedia.tradein.viewmodel.HomeResult.PriceState
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import com.tokopedia.tradein.viewmodel.liveState.GoToCheckout
import com.tokopedia.tradein.viewmodel.liveState.GoToHargaFinal
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import kotlinx.android.synthetic.main.tradein_home_activity.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class TradeInHomeActivity : BaseViewModelActivity<TradeInHomeViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics

    private lateinit var viewModel: TradeInHomeViewModel
    private var isFirstTime = true
    private lateinit var laku6TradeIn: Laku6TradeIn
    private var currentFragment: Fragment = Fragment()
    private var inputImei = false
    private var maxPrice: String = "-"
    private var minPrice: String = "-"
    private var isEligibleForTradein: Boolean = false
    private var notEligibleMessage: String = ""
    private var deviceDisplayName: String? = null

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
    }

    private fun setObservers() {
        viewModel.askUserLogin.observe(this, Observer {
            if (it != null && it == TradeinConstants.LOGIN_REQUIRED) {
                startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), BaseTradeInActivity.LOGIN_REQUEST)
            } else {
                intent?.data?.lastPathSegment?.let { last ->
                    if (last == FINAL_PRICE) {
                        getTradeInParams()
                        init()
                        return@Observer
                    }
                }
                RouteManager.route(this, TRADEIN_DISCOVERY_INFO_URL)
                tradeInAnalytics.openEducationalScreen()
            }
        })
        viewModel.homeResultData.observe(this, Observer { homeResult: HomeResult ->
            if (!homeResult.isSuccess) {
                setNotElligible(getString(R.string.not_elligible))
            } else {
                when (homeResult.priceStatus) {
                    PriceState.DIAGNOSED_INVALID -> {
                        setNotElligible(getString(R.string.tradein_not_elligible_price_high))
                    }
                    PriceState.DIAGNOSED_VALID -> {
                        setDiagnosedValid(homeResult)
                    }
                    PriceState.NOT_DIAGNOSED -> {
                        setNotDiagnosed(homeResult)
                    }
                    else -> {
                    }
                }
            }
            if (!inputImei && currentFragment !is TradeInFinalPriceFragment) {
                setFragment()
            }
        })

        viewModel.tradeInHomeStateLiveData.observe(this, Observer {
            when (it) {
                is GoToCheckout -> {
                    goToCheckout(it.deviceId, it.price)
                }
                is GoToHargaFinal -> {
                    onInitialPriceClick(it.imei)
                }
            }
        })
    }

    private fun setNotElligible(message: String) {
        isEligibleForTradein = false
        maxPrice = "-"
        notEligibleMessage = message
    }

    private fun setDiagnosedValid(homeResult: HomeResult) {
        isEligibleForTradein = true
        maxPrice = homeResult.displayMessage
        minPrice = homeResult.minPrice?.toString() ?: "-"
        sendIrisEvent(if (homeResult.maxPrice != null) homeResult.maxPrice else 0, if (homeResult.minPrice != null) homeResult.minPrice else 0)
        intent?.data?.lastPathSegment?.let { last ->
            if (last == TRADEIN_INITIAL_PRICE) {
                deviceDisplayName = homeResult.deviceDisplayName
                addFinalPriceFragment()
                return
            }
        }
        replaceFinalPriceFragment()
        return
    }

    private fun setNotDiagnosed(homeResult: HomeResult) {
        isEligibleForTradein = true
        maxPrice = homeResult.displayMessage
        minPrice = homeResult.minPrice?.toString() ?: "-"
        sendIrisEvent(if (homeResult.maxPrice != null) homeResult.maxPrice else 0, if (homeResult.minPrice != null) homeResult.minPrice else 0)
        if (inputImei) {
            laku6TradeIn.startGUITest()
        }
    }

    private fun askPermissions() {
        if (!laku6TradeIn.permissionGranted()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE),
                    BaseTradeInActivity.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE)
        } else {
            viewModel.getMaxPrice(laku6TradeIn, TRADEIN_OFFLINE)
        }
    }

    private fun setFragment() {
        progress_bar_layout.hide()
        viewModel.tradeInParams.apply {
            intent.data?.lastPathSegment?.let {
                when (it) {
                    TRADEIN_SELLER_CHECK -> {
                        currentFragment = TradeInAddressFragment.getFragmentInstance()
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.tradein_parent_view, currentFragment, "")
                                .commit()
                    }
                    else -> {
                        if (currentFragment is TradeInInitialPriceFragment) {
                            (currentFragment as TradeInInitialPriceFragment).handleEligibility(maxPrice, isEligibleForTradein, notEligibleMessage)
                        } else {
                            currentFragment = TradeInInitialPriceFragment
                                    .getFragmentInstance(maxPrice, isEligibleForTradein, notEligibleMessage)
                            supportFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.tradein_right_to_left, R.anim.tradein_left_to_right)
                                    .add(R.id.tradein_parent_view, currentFragment, TRADEIN_INITIAL_FRAGMENT)
                                    .addToBackStack("")
                                    .commit()
                            tradeInAnalytics.viewInitialPricePage(
                                    deviceDisplayName
                                            ?: "none/other", minPrice, maxPrice, productId.toString())
                        }
                    }
                }
            }
        }
    }

    private fun replaceFinalPriceFragment() {
        progress_bar_layout.hide()
        deviceDisplayName = StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString()
        currentFragment = TradeInFinalPriceFragment.getFragmentInstance(deviceDisplayName)
        supportFragmentManager.beginTransaction()
                .replace(R.id.tradein_parent_view, currentFragment, TRADEIN_FINAL_PRICE_FRAGMENT)
                .commit()
    }

    private fun addFinalPriceFragment() {
        progress_bar_layout.hide()
        if (currentFragment is TradeInFinalPriceFragment)
            return
        currentFragment = TradeInFinalPriceFragment.getFragmentInstance(deviceDisplayName)
        supportFragmentManager.beginTransaction()
                .add(R.id.tradein_parent_view, currentFragment, TRADEIN_FINAL_PRICE_FRAGMENT)
                .setCustomAnimations(R.anim.tradein_right_to_left, R.anim.tradein_left_to_right)
                .addToBackStack("")
                .commit()
    }

    override fun getLayoutRes(): Int = R.layout.tradein_home_activity

    override fun onResume() {
        super.onResume()
        intent.data?.lastPathSegment?.let {
            if (it == TRADEIN && !isFirstTime) {
                finish()
                tradeInAnalytics.clickEducationalBackButton()
            } else
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
        var campaignId = TradeinConstants.CAMPAIGN_ID_PROD
        if (TokopediaUrl.getInstance().TYPE == Env.STAGING) campaignId = TradeinConstants.CAMPAIGN_ID_STAGING
        laku6TradeIn = Laku6TradeIn.getInstance(this, campaignId,
                TradeinConstants.APPID, Keys.AUTH_TRADE_IN_API_KEY_MA, TokopediaUrl.getInstance().TYPE == Env.STAGING, BaseTradeInActivity.TRADEIN_EXCHANGE, AuthKey.SAFETYNET_KEY_TRADE_IN)
        intent.data?.lastPathSegment?.let {
            if (it == TRADEIN_SELLER_CHECK || it == FINAL_PRICE)
                askPermissions()
            else {
                setFragment()
            }
            if (it == TRADEIN_SELLER_CHECK)
                tradeInAnalytics.clickEducationalTradeIn()
        }

        //init sessionid
        viewModel.initSessionId(laku6TradeIn)
    }

    private fun getTradeInParams() {
        if (intent.getParcelableExtra<TradeInParams>(TradeInParams::class.java.simpleName) != null)
            viewModel.tradeInParams = intent.getParcelableExtra(TradeInParams::class.java.simpleName)
                    ?: TradeInParams()
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
                viewModel.getMaxPrice(laku6TradeIn, TRADEIN_OFFLINE)
            } else {
                askPermissions()
            }
        }
    }

    private fun onInitialPriceClick(imei: String?) {
        tradeInAnalytics.clickInitialPriceContinueButton()
        if (imei != null) {
            inputImei = true
            viewModel.tradeInParams.deviceId = imei
            viewModel.getIMEI(laku6TradeIn, imei)
        } else {
            laku6TradeIn.startGUITest()
        }
    }

    private fun goToCheckout(deviceId: String?, price: String) {
        val intent = Intent(TradeinConstants.ACTION_GO_TO_SHIPMENT)
        intent.putExtra(TradeInParams.PARAM_DEVICE_ID, deviceId)
        intent.putExtra(TradeInParams.PARAM_PHONE_TYPE, deviceDisplayName?.toLowerCase(Locale.getDefault()))
        intent.putExtra(TradeInParams.PARAM_PHONE_PRICE, price)
        setResult(RESULT_OK, intent)
        finish()
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
            if (currentFragment is TradeInInitialPriceFragment)
                tradeInAnalytics.initialPricePageBackButtonClick(viewModel.tradeInParams.productId.toString())
            else if (currentFragment is TradeInFinalPriceFragment)
                tradeInAnalytics.clickFinalPriceBack()
            supportFragmentManager.popBackStack()
            currentFragment = Fragment()
        } else {
            if (currentFragment is TradeInFinalPriceFragment)
                tradeInAnalytics.clickFinalPriceBack()
            super.onBackPressed()
        }
    }

    companion object {
        private const val TRADEIN = "tradein"
        private const val FINAL_PRICE = "host_final_price"
        private const val TRADEIN_SELLER_CHECK = "seller_check"
        private const val TRADEIN_INITIAL_PRICE = "initial_price"
        private const val TRADEIN_INITIAL_FRAGMENT = "TRADEIN_INITIAL_FRAGMENT"
        private const val TRADEIN_FINAL_PRICE_FRAGMENT = "TRADEIN_FINAL_PRICE_FRAGMENT"
    }
}