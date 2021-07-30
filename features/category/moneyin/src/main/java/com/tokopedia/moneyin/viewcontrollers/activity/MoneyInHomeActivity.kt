package com.tokopedia.moneyin.viewcontrollers.activity

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
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.laku6.tradeinsdk.api.Laku6TradeIn.TradeInListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.authentication.AuthKey
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.model.HomeResult
import com.tokopedia.common_tradein.model.HomeResult.PriceState
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.keys.Keys
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.moneyin.MoneyInGTMConstants
import com.tokopedia.moneyin.MoneyinConstants
import com.tokopedia.moneyin.MoneyinConstants.MONEYIN
import com.tokopedia.moneyin.R
import com.tokopedia.moneyin.viewcontrollers.bottomsheet.MoneyInImeiHelpBottomSheet.Companion.newInstance
import com.tokopedia.moneyin.viewmodel.MoneyInHomeViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.*

open class MoneyInHomeActivity : BaseMoneyInActivity<MoneyInHomeViewModel>(), TradeInListener {
    private var mTvPriceElligible: Typography? = null
    private var mButtonRemove: ImageView? = null
    private var mTvModelName: Typography? = null
    private var mTvHeaderPrice: Typography? = null
    private var mTvInitialPrice: Typography? = null
    private var mTvGoToProductDetails: Typography? = null
    private var mTvNotUpto: Typography? = null
    private var tvIndicateive: Typography? = null
    private lateinit var moneyInHomeViewModel: MoneyInHomeViewModel
    private var closeButtonText = 0
    private var notElligibleText = 0
    private var isShowingPermissionPopup = false
    private var category = MoneyInGTMConstants.CATEGORY_TRADEIN_START_PAGE
    private var errorDialogGTMLabel = ""
    private var productName = ""
    private var imeiView: LinearLayout? = null
    private var editTextImei: EditText? = null
    private var typographyImeiDescription: Typography? = null
    private var typographyImeiHelp: Typography? = null
    private var inputImei = false
    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            moneyInHomeViewModel.processMessage(intent)
        }
    }
    private val mBackReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent. DO BACK TO PARENT
            Timber.d("Do back action to parent")
        }
    }
    private val laku6GTMReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null && MoneyInGTMConstants.ACTION_LAKU6_GTM == intent.action) {
                val page = intent.getStringExtra(MoneyInGTMConstants.PAGE)
                val action = intent.getStringExtra(MoneyInGTMConstants.ACTION)
                val value = intent.getStringExtra(MoneyInGTMConstants.VALUE)
                val cekFisik = MoneyInGTMConstants.CEK_FISIK_MONEY_IN
                val cekFungsi = MoneyInGTMConstants.CEK_FUNGSI_MONEY_IN
                val cekFisikResult = MoneyInGTMConstants.CEK_FISIK_RESULT_MONEY_IN
                if (MoneyInGTMConstants.CEK_FISIK == page) {
                    if (MoneyInGTMConstants.CLICK_SALIN == action || MoneyInGTMConstants.CLICK_SOCIAL_SHARE == action) sendGeneralEvent(clickEvent,
                            cekFisik, action, value)
                } else if (MoneyInGTMConstants.CEK_FUNGSI_TRADE_IN == page) {
                    sendGeneralEvent(clickEvent,
                            cekFungsi, action, value)
                } else if (MoneyInGTMConstants.CEK_FISIK_RESULT_TRADE_IN == page) {
                    sendGeneralEvent(viewEvent,
                            cekFisikResult, action, value)
                }
            }
        }
    }
    private lateinit var laku6TradeIn: Laku6TradeIn

    override fun initInject() {
        component.inject(this)
    }

    override fun initView() {
        setTradeInParams()
        mTvPriceElligible = findViewById(R.id.tv_price_elligible)
        mButtonRemove = findViewById(R.id.button_remove)
        mTvModelName = findViewById(R.id.tv_model_name)
        mTvHeaderPrice = findViewById(R.id.tv_header_price)
        mTvInitialPrice = findViewById(R.id.tv_initial_price)
        mTvNotUpto = findViewById(R.id.tv_not_upto)
        mTvGoToProductDetails = findViewById(R.id.tv_go_to_product_details)
        tvIndicateive = findViewById(R.id.tv_indicative)
        imeiView = findViewById(R.id.imei_view)
        editTextImei = findViewById(R.id.edit_text_imei)
        typographyImeiDescription = findViewById(R.id.typography_imei_description)
        typographyImeiHelp = findViewById(R.id.typography_imei_help)
        mTvModelName?.text = StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString()
        closeButtonText = R.string.tradein_return
        notElligibleText = R.string.not_elligible_money_in
        category = MoneyInGTMConstants.CATEGORY_MONEYIN_PRICERANGE_PAGE
        mTvGoToProductDetails?.setText(closeButtonText)
        mTvGoToProductDetails?.setOnClickListener { finish() }
        typographyImeiHelp?.setOnClickListener {
            val tradeInImeiHelpBottomSheet = newInstance()
            tradeInImeiHelpBottomSheet.show(supportFragmentManager, "")
        }
    }

    private fun setTradeInParams() {
        if (intent.hasExtra(TradeInParams::class.java.simpleName)) {
            moneyInHomeViewModel.tradeInParams = intent.getParcelableExtra(TradeInParams::class.java.simpleName)
        }
    }

    override fun getViewModelType(): Class<MoneyInHomeViewModel> {
        return MoneyInHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        moneyInHomeViewModel = viewModel as MoneyInHomeViewModel
        lifecycle.addObserver(moneyInHomeViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moneyInHomeViewModel.homeResultData.observe(this, { homeResult: HomeResult ->
            if (!homeResult.isSuccess) {
                mTvInitialPrice?.text = homeResult.displayMessage
                mTvPriceElligible?.text = getString(notElligibleText)
                mTvPriceElligible?.visibility = View.VISIBLE
                tvIndicateive?.visibility = View.GONE
                mTvGoToProductDetails?.setText(closeButtonText)
                mTvGoToProductDetails?.setOnClickListener { v: View? ->
                    sendGoToProductDetailGTM()
                    finish()
                }
            } else {
                val state = homeResult.priceStatus
                productName = if (moneyInHomeViewModel.tradeInParams.productName != null) moneyInHomeViewModel.tradeInParams.productName?.toLowerCase(Locale.getDefault()).toString() else ""
                when (state) {
                    PriceState.DIAGNOSED_INVALID -> {
                        tvIndicateive?.visibility = View.GONE
                        mTvGoToProductDetails?.setText(closeButtonText)
                        mTvGoToProductDetails?.setOnClickListener { v: View? ->
                            sendGoToProductDetailGTM()
                            finish()
                        }
                        showDeviceNotElligiblePopup(R.string.tradein_not_elligible_price_high)
                        mTvNotUpto?.visibility = View.GONE
                        mTvModelName?.text = homeResult.deviceDisplayName
                        mTvInitialPrice?.text = homeResult.displayMessage
                        viewMoneyInPriceGTM(homeResult.displayMessage)
                    }
                    PriceState.DIAGNOSED_VALID -> {
                        tvIndicateive?.visibility = View.GONE
                        mTvModelName?.text = homeResult.deviceDisplayName
                        mTvInitialPrice?.text = homeResult.displayMessage
                        mTvGoToProductDetails?.setText(R.string.moneyin_sell_now)
                        mTvGoToProductDetails?.setOnClickListener { goToHargaFinal(homeResult.deviceDisplayName) }
                        goToHargaFinal(homeResult.deviceDisplayName)
                    }
                    PriceState.NOT_DIAGNOSED -> {
                        mTvInitialPrice?.text = homeResult.displayMessage
                        mTvGoToProductDetails?.text = getString(R.string.text_check_functionality)
                        mTvGoToProductDetails?.setOnClickListener {
                            if (imeiView?.visibility == View.VISIBLE) {
                                when {
                                    editTextImei?.text?.length == 15 -> {
                                        showProgressBar()
                                        laku6TradeIn.checkImeiValidation(this, editTextImei?.text.toString())
                                    }
                                    editTextImei?.text.isNullOrEmpty() -> {
                                        typographyImeiDescription?.text = getString(R.string.enter_the_imei_number_text)
                                        typographyImeiDescription?.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                                    }
                                    else -> {
                                        typographyImeiDescription?.text = getString(R.string.wrong_imei_string)
                                        typographyImeiDescription?.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                                    }
                                }
                            } else {
                                laku6TradeIn.startGUITest()
                            }
                            sendGeneralEvent(clickEvent,
                                    category,
                                    MoneyInGTMConstants.ACTION_CLICK_MULAI_FUNGSI,
                                    "")
                        }
                        if (inputImei) {
                            laku6TradeIn.startGUITest()
                            return@observe
                        }
                        viewMoneyInPriceGTM(homeResult.deviceDisplayName + " - " + homeResult.displayMessage)
                    }
                    PriceState.MONEYIN_ERROR -> {
                        showMessageWithAction(homeResult.displayMessage,
                                getString(R.string.tradein_return)) { this.finish() }
                        errorDialogGTMLabel = homeResult.displayMessage
                    }
                    else -> {
                    }
                }
            }
        })
        moneyInHomeViewModel.askUserLogin.observe(this, { userLoginStatus: Int? ->
            if (userLoginStatus != null && userLoginStatus == MoneyinConstants.LOGIN_REQUIRED) {
                navigateToActivityRequest(RouteManager.getIntent(this, ApplinkConst.LOGIN), LOGIN_REQUEST)
            } else {
                showPermissionDialog()
            }
        })
        moneyInHomeViewModel.imeiStateLiveData.observe(this, { showImei: Boolean ->
            if (showImei) {
                imeiView?.visibility = View.VISIBLE
            } else {
                imeiView?.visibility = View.GONE
            }
        })
    }

    private fun showDialogFragment(titleText: String?, bodyText: String?, positiveButton: String?, negativeButton: String?) {
        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(titleText ?: "")
            setDescription(bodyText ?: "")
            setPrimaryCTAText(positiveButton ?: "")
            setPrimaryCTAClickListener {
                clickAccept()
                this.dismiss()
            }
            setSecondaryCTAClickListener {
                clickDeny()
                dismiss()
            }
            setSecondaryCTAText(negativeButton ?: "")
        }
        dialog.show()
    }

    private fun sendGoToProductDetailGTM() {
        sendGeneralEvent(clickEvent,
                category,
                MoneyInGTMConstants.ACTION_KEMBALI_KE_DETAIL_PRODUK,
                "")
    }

    private fun viewMoneyInPriceGTM(label: String) {
        sendGeneralEvent(viewEvent,
                category,
                MoneyInGTMConstants.VIEW_PRICE_RANGE_PAGE,
                label)
    }

    private fun goToHargaFinal(deviceDisplayName: String?) {
        val finalPriceIntent = Intent(this, FinalPriceActivity::class.java)
        val params = moneyInHomeViewModel.tradeInParams
        finalPriceIntent.putExtra(TradeInParams::class.java.simpleName, params)
        if (deviceDisplayName != null) finalPriceIntent.putExtra(FinalPriceActivity.PARAM_TRADEIN_PHONE_TYPE, deviceDisplayName.toLowerCase(Locale.getDefault()))
        finalPriceIntent.putExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, MONEYIN)
        navigateToActivityRequest(finalPriceIntent, ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE)
    }

    private fun getPriceFromSDK(context: Context) {
        var campaignId = MoneyinConstants.CAMPAIGN_ID_PROD
        if (TokopediaUrl.getInstance().TYPE == Env.STAGING) campaignId = MoneyinConstants.CAMPAIGN_ID_STAGING
        laku6TradeIn = Laku6TradeIn.getInstance(context, campaignId,
                MoneyinConstants.APPID, Keys.AUTH_TRADE_IN_API_KEY_MA, TokopediaUrl.getInstance().TYPE == Env.STAGING, TEST_TYPE, AuthKey.SAFETYNET_KEY_TRADE_IN)
        requestPermission()
    }

    private fun requestPermission() {
        if (!laku6TradeIn.permissionGranted()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE)
        } else {
            moneyInHomeViewModel.getMaxPrice(laku6TradeIn)
        }
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

    override val menuRes: Int
        get() = R.menu.trade_in_home

    override fun getLayoutRes(): Int {
        return R.layout.money_in_home_activity
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.isNotEmpty() && permissions.size == grantResults.size) {
                for (i in permissions.indices) {
                    val result = grantResults[i]
                    if (result == PackageManager.PERMISSION_DENIED) {
                        mTvGoToProductDetails?.setText(closeButtonText)
                        mTvGoToProductDetails?.setOnClickListener {
                            sendGoToProductDetailGTM()
                            finish()
                        }
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            showMessageWithAction(getString(R.string.tradein_permission_setting),
                                    getString(com.tokopedia.abstraction.R.string.title_ok)) {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                intent.addCategory(Intent.CATEGORY_DEFAULT)
                                intent.data = Uri.parse("package:" + this.packageName)
                                this.startActivityForResult(intent, APP_SETTINGS)
                            }
                        } else {
                            showMessageWithAction(getString(R.string.tradein_requires_permission_for_diagnostic),
                                    getString(com.tokopedia.abstraction.R.string.title_ok)) { requestPermission() }
                        }
                        return
                    }
                }
                showProgressBar()
                moneyInHomeViewModel.getMaxPrice(laku6TradeIn)
            } else {
                requestPermission()
            }
        }
    }

    private fun showPermissionDialog() {
        isShowingPermissionPopup = true
        if (intent.getBooleanExtra(TradeInParams.PARAM_PERMISSION_GIVEN, false)) {
            clickAccept()
        } else {
            showDialogFragment(getString(R.string.tradein_text_request_access),
                    getString(R.string.tradein_text_permission_description), getString(R.string.money_in_beri_akses), getString(R.string.money_in_kembali))
        }
    }

    private fun showDeviceNotElligiblePopup(messageStringId: Int) {
        val greenColor = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
        val foregroundColorSpan = ForegroundColorSpan(greenColor)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTnC()
            }
        }
        val spannableString = SpannableString(getString(messageStringId))
        if (messageStringId == R.string.money_in_need_permission) {
            spannableString.setSpan(foregroundColorSpan, 62, 83, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(clickableSpan, 62, 83, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        mTvPriceElligible?.text = spannableString
        mTvPriceElligible?.show()
        mButtonRemove?.show()
        mButtonRemove?.setOnClickListener {
            mTvPriceElligible?.gone()
            mButtonRemove?.gone()
        }
        mTvPriceElligible?.isClickable = true
        mTvPriceElligible?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data)
                finish()
            }
        } else if (requestCode == APP_SETTINGS) {
            requestPermission()
        } else if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) moneyInHomeViewModel.checkLogin() else finish()
        }
    }

    private fun clickAccept() {
        if (isShowingPermissionPopup) {
            isShowingPermissionPopup = false
            getPriceFromSDK(this)
            sendGeneralEvent(clickEvent,
                    category,
                    MoneyInGTMConstants.ACTION_CLICK_SETUJU_BUTTON,
                    MoneyInGTMConstants.BERI_IZIN_PENG_HP)
        } else {
            sendGeneralEvent(clickEvent,
                    category,
                    MoneyInGTMConstants.ACTION_CLICK_KEMBALI_BUTTON,
                    errorDialogGTMLabel)
            finish()
        }
    }

    private fun clickDeny() {
        if (isShowingPermissionPopup) {
            isShowingPermissionPopup = false
            showDeviceNotElligiblePopup(R.string.money_in_need_permission)
            mTvInitialPrice?.text = ""
            mTvGoToProductDetails?.setText(R.string.money_in_request_permission)
            mTvGoToProductDetails?.setOnClickListener { showPermissionDialog() }
            sendGeneralEvent(clickEvent,
                    category,
                    MoneyInGTMConstants.ACTION_CLICK_BATAL_BUTTON,
                    MoneyInGTMConstants.BERI_IZIN_PENG_HP)
        }
    }

    override fun onFinished(jsonObject: JSONObject) {
        hideProgressBar()
        moneyInHomeViewModel.setDeviceId(editTextImei?.text.toString())
        inputImei = true
        TradeInUtils.setImeiNumber(this, editTextImei?.text.toString())
        getPriceFromSDK(this)
        typographyImeiDescription?.text = getString(R.string.enter_the_imei_number_text)
        typographyImeiDescription?.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
    }

    override fun onError(error: JSONObject) {
        hideProgressBar()
        var errorMessage: String? = getString(R.string.wrong_imei_string)
        try {
            errorMessage = error.getString("message")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        typographyImeiDescription?.text = errorMessage
        typographyImeiDescription?.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_R600))
    }

    companion object {
        const val TEST_TYPE = "money-in"
    }
}