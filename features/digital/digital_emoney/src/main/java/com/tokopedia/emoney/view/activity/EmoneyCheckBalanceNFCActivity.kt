package com.tokopedia.emoney.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
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
import com.tokopedia.applink.UriUtil
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
import com.tokopedia.emoney.view.electronicmoney.*
import com.tokopedia.emoney.viewmodel.BrizziViewModel
import com.tokopedia.emoney.viewmodel.EmoneyInquiryBalanceViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import id.co.bri.sdk.Brizzi
import javax.inject.Inject

class EmoneyCheckBalanceNFCActivity : BaseSimpleActivity(), MandiriActionListener, BrizziActionListener {

    private lateinit var tapETollCardView: TapETollCardView
    private lateinit var nfcDisabledView: NFCDisabledView
    private lateinit var eTollUpdateBalanceResultView: ETollUpdateBalanceResultView
    private lateinit var pendingIntent: PendingIntent
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var emoneyInquiryBalanceViewModel: EmoneyInquiryBalanceViewModel
    private lateinit var brizziViewModel: BrizziViewModel
    private lateinit var brizziInstance: Brizzi
    private lateinit var mandiriCheckBalance: MandiriElectronicMoney
    private lateinit var briBrizzi: BriElectronicMoney

    private var techListsArrayBrizzi: Array<Array<String>>? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig
    @Inject
    lateinit var emoneyAnalytics: EmoneyAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface


    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        emoneyInquiryBalanceViewModel = viewModelProvider.get(EmoneyInquiryBalanceViewModel::class.java)
        brizziViewModel = viewModelProvider.get(BrizziViewModel::class.java)

        eTollUpdateBalanceResultView = findViewById(R.id.view_update_balance_result)
        nfcDisabledView = findViewById(R.id.view_nfc_disabled)
        tapETollCardView = findViewById(R.id.view_tap_emoney_card)

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

                if (intent != null && intent.getStringExtra(DIGITAL_NFC_CALLING_TYPE) != null) {
                    if (intent.getStringExtra(DIGITAL_NFC_CALLING_TYPE) === DIGITAL_NFC) {
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
                directToNFCSettingsPage()
            }
        })

        brizziInstance = Brizzi.getInstance()
        brizziInstance.setNfcAdapter(this)
        handleIntent(intent)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_emoney_balance_nfc
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun getScreenName(): String? {
        return EmoneyCheckBalanceNFCActivity::class.simpleName
    }

    private fun initInjector() {
        val emoneyComponent = DaggerDigitalEmoneyComponent.builder()
                .baseAppComponent((this.application as BaseMainApplication).baseAppComponent)
                .build()
        emoneyComponent.inject(this)
    }

    public override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        //process intent filter
        if (intent != null && !TextUtils.isEmpty(intent.action) &&
                intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {

            if (!isDigitalSmartcardEnabled()) {
                Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
                RouteManager.route(this, ApplinkConst.HOME_FEED)
                finish()
            } else {
                // nfc enabled and process BRI NFC as default
                processGetBalanceBrizzi(false)
            }
        }
    }

    override fun processGetBalanceBrizzi(refresh: Boolean) {
        brizziViewModel.getTokenBrizzi(GraphqlHelper.loadRawString(resources, R.raw.query_token_brizzi), refresh)
        brizziViewModel.tokenBrizzi.observe(this, Observer { token ->
            brizziInstance.Init(token, AuthKey.BRIZZI_CLIENT_SECRET)
            brizziInstance.setUserName(AuthKey.BRIZZI_CLIENT_ID)

            briBrizzi = BrizziCheckBalance(brizziInstance, this)
            showLoading()
            briBrizzi.processTagIntent(intent)
        })
    }

    private fun executeMandiri() {
        mandiriCheckBalance = MandiriCheckBalance(this)
        showLoading()
        mandiriCheckBalance.processTagIntent(intent)
    }

    private fun getOperatorName(issuerId: Int): String {
        if (issuerId == ISSUER_ID_BRIZZI) {
            return OPERATOR_NAME_BRIZZI
        }
        return OPERATOR_NAME_EMONEY
    }

    override fun setIssuerId(issuerIdEmoney: Int) {
        tapETollCardView.setIssuerId(issuerIdEmoney)
    }

    override fun onSuccess(emoneyInquiry: EmoneyInquiry) {
        when (emoneyInquiry.attributesEmoneyInquiry?.issuer_id) {
            ISSUER_ID_BRIZZI -> {
                showCardLastBalance(emoneyInquiry)
            }
        }
    }

    override fun onSuccess(mapAttributes: HashMap<String, Any>) {
        emoneyInquiryBalanceViewModel.getEmoneyInquiryBalance(
                EmoneyInquiryBalanceViewModel.PARAM_INQUIRY,
                GraphqlHelper.loadRawString(resources, R.raw.query_emoney_inquiry_balance),
                0,
                mapAttributes,
                this::onSuccessInquiryBalance,
                this::onErrorInquiryBalance)
    }

    override fun onErrorCardNotFound(issuerIdEmoney: Int) {
        when (issuerIdEmoney) {
            ISSUER_ID_BRIZZI -> executeMandiri()
            ISSUER_ID_EMONEY -> {
                emoneyAnalytics.onErrorReadingCard()
                showError(resources.getString(R.string.emoney_card_isnot_supported))
            }
        }
    }

    override fun onError(stringResource: Int) {
        showError(resources.getString(stringResource))
    }

    private fun onSuccessInquiryBalance(mapAttributes: HashMap<String, Any>,
                                        emoneyInquiry: EmoneyInquiry) {
        emoneyInquiry.attributesEmoneyInquiry?.let { attributes ->
            when {
                attributes.status == 0 -> {
                    mandiriCheckBalance.sendCommandToCard(emoneyInquiry.attributesEmoneyInquiry.payload,
                            emoneyInquiry.id.toInt(), mapAttributes)
                }
                attributes.status == 1 -> {
                    showCardLastBalance(emoneyInquiry)
                }
                attributes.status == 2 -> {
                    emoneyInquiry.error?.let { error ->
                        showError(error.title)
                    }
                }
                else -> {
                    return
                }
            }
        }
    }

    override fun sendCommand(id: Int, mapAttributes: HashMap<String, Any>) {
        emoneyInquiryBalanceViewModel.getEmoneyInquiryBalance(
                EmoneyInquiryBalanceViewModel.PARAM_SEND_COMMAND,
                GraphqlHelper.loadRawString(resources, R.raw.query_emoney_inquiry_balance),
                id,
                mapAttributes,
                this::onSuccessInquiryBalance,
                this::onErrorInquiryBalance)
    }

    override fun logBrizziStatus(firstLogInquiry: Boolean, emoneyInquiry: EmoneyInquiry) {
        if (firstLogInquiry) {
            emoneyInquiry.attributesEmoneyInquiry?.let {
                logBrizzi(0, it.cardNumber, "success", it.lastBalance.toDouble())
            }
        } else {
            brizziViewModel.inquiryIdBrizzi.observe(this, Observer {
                if (it > -1) {
                    emoneyInquiry.attributesEmoneyInquiry?.let { attributeInquiry ->
                        logBrizzi(it, attributeInquiry.cardNumber, "success",
                                attributeInquiry.lastBalance.toDouble())
                    }
                }
            })
        }
    }

    private fun logBrizzi(inquiryId: Int, cardNumber: String, status: String, lastBalance: Double) {
        var mapParam = HashMap<String, Any>()
        mapParam.put(BrizziViewModel.ISSUER_ID, BrizziCheckBalance.ISSUER_ID_BRIZZI)
        mapParam.put(BrizziViewModel.INQUIRY_ID, inquiryId)
        mapParam.put(BrizziViewModel.CARD_NUMBER, cardNumber)
        mapParam.put(BrizziViewModel.RC, status)
        mapParam.put(BrizziViewModel.LAST_BALANCE, lastBalance)
        brizziViewModel.logDataBrizzi(GraphqlHelper.loadRawString(resources, R.raw.mutation_emoney_log_brizzi),
                mapParam)
    }

    private fun onErrorInquiryBalance(throwable: Throwable) {
        if (throwable is MessageErrorException) {
            showError(throwable.message ?: "")
        } else {
            showError(getString(R.string.emoney_update_balance_failed))
        }
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
//        return remoteConfig.getBoolean(DIGITAL_SMARTCARD, false)
        return true
    }

    private fun directToNFCSettingsPage() {
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
    }

    private fun navigatePageToDigitalProduct(passData: DigitalCategoryDetailPassData) {
        val bundle = Bundle()
        bundle.putParcelable(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData)
        val applink = UriUtil.buildUri(ApplinkConsInternalDigital.DIGITAL_PRODUCT, passData.categoryId, passData.operatorId)
        val intent = RouteManager.getIntent(this, applink)
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

    fun detectNFC() {
        if (!brizziInstance.nfcAdapter.isEnabled) {
            eTollUpdateBalanceResultView.visibility = View.GONE
            tapETollCardView.visibility = View.GONE

            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.emoney_please_activate_nfc_from_settings))
                    .setPositiveButton(getString(R.string.emoney_activate)) { p0, p1 ->
                        emoneyAnalytics.onActivateNFCFromSetting()
                        directToNFCSettingsPage()
                    }
                    .setNegativeButton(getString(R.string.emoney_cancel)) { p0, p1 ->
                        emoneyAnalytics.onCancelActivateNFCFromSetting()
                        nfcDisabledView.visibility = View.VISIBLE
                    }.show()
        } else {
            if (userSession.isLoggedIn) {
                pendingIntent = PendingIntent.getActivity(this, 0,
                        Intent(this, EmoneyCheckBalanceNFCActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
                val intentFilter = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
                techListsArrayBrizzi = arrayOf(arrayOf(IsoDep::class.java.name, NfcA::class.java.name))
                brizziInstance.nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null)
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

    fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == REQUEST_CODE_LOGIN) {
                if (userSession.isLoggedIn) {
                    processGetBalanceBrizzi(false)
                }
            }
        }
    }

    companion object {
        const val DIGITAL_NFC_CALLING_TYPE = "calling_page_check_saldo"
        const val DIGITAL_NFC_FROM_PDP = "calling_from_pdp"
        const val DIGITAL_NFC = "calling_from_nfc"
        const val REQUEST_CODE_LOGIN = 1980

        const val ISSUER_ID_EMONEY = 1
        const val ISSUER_ID_BRIZZI = 2

        private val ETOLL_CATEGORY_ID = "34"
        const val OPERATOR_NAME_EMONEY = "emoney"
        const val OPERATOR_NAME_BRIZZI = "brizzi"

    }
}