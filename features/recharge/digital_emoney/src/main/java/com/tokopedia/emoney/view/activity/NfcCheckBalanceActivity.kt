package com.tokopedia.emoney.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.authentication.AuthKey
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.emoney.EmoneyAnalytics
import com.tokopedia.emoney.R
import com.tokopedia.emoney.data.EmoneyInquiry
import com.tokopedia.emoney.di.DaggerDigitalEmoneyComponent
import com.tokopedia.emoney.view.compoundview.ETollUpdateBalanceResultView
import com.tokopedia.emoney.view.compoundview.NFCDisabledView
import com.tokopedia.emoney.view.compoundview.TapETollCardView
import com.tokopedia.emoney.viewmodel.BrizziBalanceViewModel
import com.tokopedia.emoney.viewmodel.EmoneyBalanceViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSessionInterface
import id.co.bri.sdk.Brizzi
import javax.inject.Inject

/**
 * applink
 * tokopedia-android-internal://digital/smartcard/emoneybrizzi
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.SMARTCARD_WITH_BRIZZI)
 */

class NfcCheckBalanceActivity : BaseSimpleActivity() {

    private lateinit var tapETollCardView: TapETollCardView
    private lateinit var nfcDisabledView: NFCDisabledView
    private lateinit var eTollUpdateBalanceResultView: ETollUpdateBalanceResultView
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var emoneyBalanceViewModel: EmoneyBalanceViewModel
    private lateinit var brizziBalanceViewModel: BrizziBalanceViewModel

    @Inject
    lateinit var remoteConfig: RemoteConfig
    @Inject
    lateinit var emoneyAnalytics: EmoneyAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var brizziInstance: Brizzi


    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        initViewModel()
        bindView()
        processTagIntent(intent)

        eTollUpdateBalanceResultView.setListener(object : ETollUpdateBalanceResultView.OnTopupETollClickListener {
            override fun onClick(operatorId: String, issuerId: Int) {
                emoneyAnalytics.onClickTopupEmoney(getOperatorName(issuerId))

                val passData = DigitalCategoryDetailPassData.Builder()
                        .categoryId(ETOLL_CATEGORY_ID)
                        .operatorId(operatorId)
                        .clientNumber(eTollUpdateBalanceResultView.cardNumber)
                        .additionalETollLastBalance(eTollUpdateBalanceResultView.cardLastBalance)
                        .additionalETollLastUpdatedDate(eTollUpdateBalanceResultView.cardLastUpdatedDate)
                        .additionalETollOperatorName(getOperatorName(issuerId))
                        .build()

                if (intent != null && intent.getStringExtra(ApplinkConsInternalDigital.PARAM_SMARTCARD) != null) {
                    if (intent.getStringExtra(ApplinkConsInternalDigital.PARAM_SMARTCARD) == DigitalExtraParam.EXTRA_NFC) {
                        navigatePageToDigitalProduct(passData)
                    } else {
                        val intentReturn = Intent()
                        intentReturn.putExtra(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData)
                        setResult(Activity.RESULT_OK, intentReturn)
                    }
                } else {
                    navigatePageToDigitalProduct(passData)
                }
                finish()
            }
        })

        tapETollCardView.setListener(object : TapETollCardView.OnTapEtoll {
            override fun tryAgainTopup(issuerId: Int) {
                emoneyAnalytics.onClickTryAgainTapEmoney(getOperatorName(issuerId))
            }
        })

        nfcDisabledView.setListener(object : NFCDisabledView.OnActivateNFCClickListener {
            override fun onClick() {
                emoneyAnalytics.onClickActivateNFC()
                navigateToNFCSettings()
            }
        })
    }

    /**
     * this method will be executed first time after NFC card detected
     * @param intent is needed to process data NFC card brizzi and emoney mandiri
     */
    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            processTagIntent(intent)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_emoney_balance_nfc
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    private fun initInjector() {
        val emoneyComponent = DaggerDigitalEmoneyComponent.builder()
                .baseAppComponent((this.application as BaseMainApplication).baseAppComponent)
                .build()
        emoneyComponent.inject(this)
        if (brizziInstance.nfcAdapter == null) brizziInstance.setNfcAdapter(this)
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        emoneyBalanceViewModel = viewModelProvider.get(EmoneyBalanceViewModel::class.java)
        brizziBalanceViewModel = viewModelProvider.get(BrizziBalanceViewModel::class.java)
    }

    private fun bindView() {
        eTollUpdateBalanceResultView = findViewById(R.id.view_update_balance_result)
        nfcDisabledView = findViewById(R.id.view_nfc_disabled)
        tapETollCardView = findViewById(R.id.view_tap_emoney_card)
    }

    private fun processTagIntent(intent: Intent) {
        if (intent != null && !TextUtils.isEmpty(intent.action) &&
                (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED ||
                        intent.action == NfcAdapter.ACTION_TAG_DISCOVERED)) {

            if (!isDigitalSmartcardEnabled()) {
                Toast.makeText(this, getString(R.string.emoney_nfc_feature_unavailable), Toast.LENGTH_SHORT).show()
                RouteManager.route(this, ApplinkConst.HOME_FEED)
                finish()
            } else {
                // nfc enabled and process Mandiri NFC as default
                showLoading()
                executeMandiri(intent)
            }
        }
    }

    private fun executeMandiri(intent: Intent) {
        emoneyBalanceViewModel.processEmoneyTagIntent(intent,
                GraphqlHelper.loadRawString(resources, R.raw.query_emoney_inquiry_balance),
                0)

        emoneyBalanceViewModel.emoneyInquiry.observe(this, Observer { emoneyInquiry ->
            emoneyInquiry.attributesEmoneyInquiry?.let { attributes ->
                when (attributes.status) {
                    1 -> showCardLastBalance(emoneyInquiry)
                    2 -> emoneyInquiry.error?.let { error ->
                        showError(error.title)
                    }
                    else -> return@let
                }
            }
        })

        emoneyBalanceViewModel.errorInquiryBalance.observe(this, Observer {
            if (it is MessageErrorException) {
                showError(it.message ?: "")
            } else {
                showError(getString(R.string.emoney_update_balance_failed))
            }
        })

        emoneyBalanceViewModel.errorCardMessage.observe(this, Observer {
            showError(it)
        })

        emoneyBalanceViewModel.issuerId.observe(this, Observer {
            tapETollCardView.setIssuerId(it)
        })

        emoneyBalanceViewModel.cardIsEmoney.observe(this, Observer { cardIsEmoney ->
            if (!cardIsEmoney) executeBrizzi(false, intent)
        })
    }

    private fun isSupportBrizzi(): Boolean {
        var abiName = ""
        val abis = mutableListOf<String>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            abiName = Build.CPU_ABI
        } else {
            abis.addAll(Build.SUPPORTED_ABIS)
        }
        return abiName == ARCHITECTURE_ARM64 || abiName == ARCHITECTURE_ARM32 ||
                abis.contains(ARCHITECTURE_ARM64) || abis.contains(ARCHITECTURE_ARM32)
    }

    private fun executeBrizzi(refresh: Boolean, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isSupportBrizzi()) {
            brizziBalanceViewModel.getTokenBrizzi(GraphqlHelper.loadRawString(resources, R.raw.query_token_brizzi), refresh)

            brizziBalanceViewModel.tokenBrizzi.observe(this, Observer { token ->
                brizziInstance.Init(token, AuthKey.BRIZZI_CLIENT_SECRET)
                brizziInstance.setUserName(AuthKey.BRIZZI_CLIENT_ID)

                brizziBalanceViewModel.processBrizziTagIntent(intent, GraphqlHelper.loadRawString(resources, R.raw.mutation_emoney_log_brizzi),
                        brizziInstance)
            })

            brizziBalanceViewModel.emoneyInquiry.observe(this, Observer {
                showCardLastBalance(it)
            })

            brizziBalanceViewModel.tokenNeedRefresh.observe(this, Observer {
                brizziBalanceViewModel.getTokenBrizzi(GraphqlHelper.loadRawString(resources, R.raw.query_token_brizzi), true)
            })

            brizziBalanceViewModel.issuerId.observe(this, Observer {
                tapETollCardView.setIssuerId(it)
            })

            brizziBalanceViewModel.errorCardMessage.observe(this, Observer {
                showError(it)
            })

            brizziBalanceViewModel.cardIsBrizzi.observe(this, Observer { cardIsBrizzi ->
                if (!cardIsBrizzi) {
                    emoneyAnalytics.onErrorReadingCard()
                    showError(resources.getString(R.string.emoney_card_isnot_supported))
                }
            })
        } else {
            showError(resources.getString(R.string.emoney_device_isnot_supported))
        }
    }

    private fun getOperatorName(issuerId: Int): String {
        if (issuerId == ISSUER_ID_BRIZZI) {
            return OPERATOR_NAME_BRIZZI
        }
        return OPERATOR_NAME_EMONEY
    }

    private fun showError(errorMessage: String) {
        emoneyAnalytics.onShowErrorTracking()
        if (eTollUpdateBalanceResultView.visibility == View.VISIBLE) {
            eTollUpdateBalanceResultView.showError(errorMessage)
        } else {
            tapETollCardView.visibility = View.VISIBLE
            tapETollCardView.showInitialState()
            tapETollCardView.showErrorState(errorMessage)
        }
    }

    private fun showErrorDeviceUnsupported(errorMessage: String) {
        tapETollCardView.visibility = View.VISIBLE
        tapETollCardView.showErrorDeviceUnsupportedState(errorMessage)
    }

    private fun showCardLastBalance(emoneyInquiry: EmoneyInquiry) {
        emoneyAnalytics.onShowLastBalance()
        tapETollCardView.visibility = View.GONE
        eTollUpdateBalanceResultView.visibility = View.VISIBLE
        eTollUpdateBalanceResultView.showCardInfoFromApi(emoneyInquiry)
        emoneyInquiry.error?.let {
            NetworkErrorHelper.showGreenCloseSnackbar(this, it.title)
        }
    }

    private fun isDigitalSmartcardEnabled(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_RECHARGE_SMARTCARD, false)
    }

    private fun navigateToNFCSettings() {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        startActivity(intent)
    }

    private fun showLoading() {
        if (eTollUpdateBalanceResultView.visibility == View.VISIBLE) {
            eTollUpdateBalanceResultView.showLoading()
        } else {
            tapETollCardView.visibility = View.VISIBLE
            tapETollCardView.showLoading()
            emoneyAnalytics.onTapEmoneyCardShowLoading()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (brizziInstance.nfcAdapter != null) {
            brizziInstance.nfcAdapter.disableForegroundDispatch(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (brizziInstance.nfcAdapter != null) {
            if (!::permissionCheckerHelper.isInitialized) {
                permissionCheckerHelper = PermissionCheckerHelper()
            }
            permissionCheckerHelper.checkPermission(this,
                    PermissionCheckerHelper.Companion.PERMISSION_NFC,
                    object : PermissionCheckerHelper.PermissionCheckListener {

                        override fun onPermissionDenied(permissionText: String) {
                            permissionCheckerHelper.onPermissionDenied(applicationContext, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper.onNeverAskAgain(applicationContext, permissionText)
                        }

                        override fun onPermissionGranted() {
                            detectNFC()
                        }
                    }, getString(R.string.emoney_nfc_permission_rationale_message))
        } else {
            showErrorDeviceUnsupported(resources.getString(R.string.emoney_nfc_not_supported))
        }
    }

    private fun navigatePageToDigitalProduct(passData: DigitalCategoryDetailPassData) {
        val bundle = Bundle()
        bundle.putParcelable(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData)
        val intent = RouteManager.getIntent(this, ApplinkConsInternalDigital.DIGITAL_PRODUCT_FORM)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::permissionCheckerHelper.isInitialized) {
            permissionCheckerHelper.onRequestPermissionsResult(applicationContext, requestCode, permissions, grantResults)
        }
    }

    private fun detectNFC() {
        if (!brizziInstance.nfcAdapter.isEnabled) {
            eTollUpdateBalanceResultView.visibility = View.GONE
            tapETollCardView.visibility = View.GONE

            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.emoney_please_activate_nfc_from_settings))
                    .setPositiveButton(getString(R.string.emoney_activate)) { p0, p1 ->
                        emoneyAnalytics.onActivateNFCFromSetting()
                        navigateToNFCSettings()
                    }
                    .setNegativeButton(getString(R.string.emoney_cancel)) { p0, p1 ->
                        emoneyAnalytics.onCancelActivateNFCFromSetting()
                        nfcDisabledView.visibility = View.VISIBLE
                    }.show()
        } else {
            if (userSession.isLoggedIn) {
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
                brizziInstance.nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                        arrayOf<IntentFilter>(), null)
                nfcDisabledView.visibility = View.GONE

                if (eTollUpdateBalanceResultView.visibility == View.GONE) {
                    emoneyAnalytics.onEnableNFC()
                    tapETollCardView.visibility = View.VISIBLE
                }
            } else {
                navigateToLoginPage()
            }
        }
    }

    private fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_LOGIN) {
                if (userSession.isLoggedIn) {
                    data?.let {
                        processTagIntent(data)
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1980

        const val ISSUER_ID_EMONEY = 1
        const val ISSUER_ID_BRIZZI = 2

        const val ARCHITECTURE_ARM64 = "arm64-v8a"
        const val ARCHITECTURE_ARM32 = "armeabi-v7a"

        const val OPERATOR_NAME_EMONEY = "emoney"
        const val OPERATOR_NAME_BRIZZI = "brizzi"

        private val ETOLL_CATEGORY_ID = "34"

    }
}