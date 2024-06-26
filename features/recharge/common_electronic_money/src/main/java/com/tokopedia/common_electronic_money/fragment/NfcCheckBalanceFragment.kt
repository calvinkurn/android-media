package com.tokopedia.common_electronic_money.fragment

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.common_electronic_money.R
import com.tokopedia.common_electronic_money.compoundview.ETollUpdateBalanceResultView
import com.tokopedia.common_electronic_money.compoundview.NFCDisabledView
import com.tokopedia.common_electronic_money.compoundview.TapETollCardView
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.di.NfcCheckBalanceInstance
import com.tokopedia.common_electronic_money.util.EmoneyAnalytics
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

open abstract class NfcCheckBalanceFragment : BaseDaggerFragment() {

    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    protected lateinit var eTollUpdateBalanceResultView: ETollUpdateBalanceResultView
    protected lateinit var nfcDisabledView: NFCDisabledView
    protected lateinit var tapETollCardView: TapETollCardView

    private var statusCloseBtn = "";
    private var operatorName = "";

    @Inject
    lateinit var remoteConfig: RemoteConfig
    @Inject
    lateinit var emoneyAnalytics: EmoneyAnalytics
    @Inject
    lateinit var userSession: UserSessionInterface

    private val irisSessionId by lazy {
        context?.let { IrisSession(it).getSessionId() } ?: ""
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusCloseBtn = INITIAL_CLOSE_BTN
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun initInjector() {
        activity?.let {
            val nfcCheckBalanceComponent = NfcCheckBalanceInstance.getNfcCheckBalanceComponent(it.application)
            nfcCheckBalanceComponent.inject(this)
        }
    }

    fun sendTrackingCloseButton() {
        if (statusCloseBtn.isNotEmpty()) {
            var action = ""
            var screenName = ""
            when (statusCloseBtn) {
                INITIAL_CLOSE_BTN -> {
                    action = EmoneyAnalytics.Action.CLICK_CLOSE_INITAL_PAGE
                    screenName = EmoneyAnalytics.Screen.INITIAL_NFC
                }
                SUCCESS_CLOSE_BTN -> {
                    action = EmoneyAnalytics.Action.SUCCESS_CLICK_CLOSE_PAGE
                    screenName = EmoneyAnalytics.Screen.SUCCESS_NFC
                }
                FAILED_CLOSE_BTN -> {
                    action = EmoneyAnalytics.Action.FAILED_CLICK_CLOSE_PAGE
                    screenName = EmoneyAnalytics.Screen.FAILED_NFC
                }
                NOT_SUPPORT_CLOSE_BTN -> {
                    action = EmoneyAnalytics.Action.FAILED_CLICK_CLOSE_PAGE_NFC_NOT_SUPPORTED
                    screenName = EmoneyAnalytics.Screen.FAILED_NFC
                }
            }
            emoneyAnalytics.clickBtnCloseCheckSaldoNFC(action, screenName, ETOLL_CATEGORY_ID,
                    operatorName, userSession.userId, irisSessionId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eTollUpdateBalanceResultView = view.findViewById(R.id.view_update_balance_result)
        tapETollCardView = view.findViewById(R.id.view_tap_emoney_card)
        nfcDisabledView = view.findViewById(R.id.view_nfc_disabled)

        eTollUpdateBalanceResultView.setListener(object : ETollUpdateBalanceResultView.OnTopupETollClickListener {
            override fun onClick(operatorId: String, issuerId: Int, isBcaGenOne: Boolean) {
                emoneyAnalytics.clickTopupEmoney(userSession.userId, irisSessionId, getOperatorName(issuerId))
                onProcessTopupNow(getPassData(operatorId, issuerId, isBcaGenOne))
            }

            override fun onClickTickerTapcash() {
                emoneyAnalytics.onShowErrorTapcashClickContactCS(irisSessionId)
            }

            override fun onClickExtraPendingBalance() {
                if (eTollUpdateBalanceResultView.visibility == View.VISIBLE) {
                    eTollUpdateBalanceResultView.hide()
                }
                tapETollCardView.visibility = View.VISIBLE
                showInitialState()
            }
        })

        tapETollCardView.setListener(object : TapETollCardView.OnTapEtoll {
            override fun tryAgainTopup(issuerId: Int) {
                emoneyAnalytics.clickTryAgainTapEmoney(ETOLL_CATEGORY_ID, userSession.userId, irisSessionId, operatorName)
            }

            override fun goToHome() {
                RouteManager.route(context, ApplinkConst.HOME)
            }

            override fun goToHelpTapcash() {
                emoneyAnalytics.onShowErrorTapcashClickUpdateBalanceCardFailed(irisSessionId)
                RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${HELP_TAPCASH}")
            }
        })

        nfcDisabledView.buttonActivateNFC.setOnClickListener {
            emoneyAnalytics.onClickActivateNFC()
            navigateToNFCSettings()
        }

        emoneyAnalytics.openScreenNFC(operatorName, userSession.userId, irisSessionId)
    }

    protected fun onProcessTopupNow(passData: DigitalCategoryDetailPassData) {
        activity?.let {
            if (it.intent != null && !it.intent.getStringExtra(ApplinkConsInternalDigital.PARAM_SMARTCARD).isNullOrEmpty()) {
                //todo check
                if (it.intent.getStringExtra(ApplinkConsInternalDigital.PARAM_SMARTCARD) == DigitalExtraParam.EXTRA_NFC) {
                    navigatePageToDigitalProduct(passData)
                } else {
                    val intentReturn = Intent()
                    intentReturn.putExtra(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData)
                    it.setResult(Activity.RESULT_OK, intentReturn)
                }
            } else {
                navigatePageToDigitalProduct(passData)
            }
            it.finish()
        }
    }

    protected fun isTagNfcValid(intent: Intent): Boolean {
        return intent != null && !TextUtils.isEmpty(intent.action) &&
                (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED ||
                        intent.action == NfcAdapter.ACTION_TAG_DISCOVERED)
    }

    protected fun onNavigateToHome() {
        activity?.let {
            Toast.makeText(it, getString(R.string.emoney_nfc_feature_unavailable), Toast.LENGTH_SHORT).show()
            RouteManager.route(it, ApplinkConst.HOME_FEED)
            it.finish()
        }
    }

    protected fun getOperatorName(issuerId: Int): String {
        if (issuerId == ISSUER_ID_BRIZZI) {
            return OPERATOR_NAME_BRIZZI
        } else if(issuerId == ISSUER_ID_TAP_CASH){
            return OPERATOR_NAME_TAPCASH
        } else if (issuerId == ISSUER_ID_JAKCARD) {
            return OPERATOR_NAME_JAKCARD
        } else if (issuerId == ISSUER_ID_FLAZZ) {
            return OPERATOR_NAME_FLAZZ
        }
        return OPERATOR_NAME_EMONEY
    }

    protected open fun getLayout(): Int {
        return R.layout.fragment_emoney_nfc_check_balance
    }

    protected abstract fun detectNfc()

    protected abstract fun getPassData(operatorId: String, issuerId: Int, isBCAGenOne: Boolean): DigitalCategoryDetailPassData

    protected fun showError(errorMessage: String, errorMessageLabel: String = "",
                            imageUrl: String = "",
                            isButtonShow: Boolean = true,
                            isGlobalErrorShow: Boolean = false,
                            mandiriGetSocketTimeout: Boolean = false,
                            tapCashWriteFailed: Boolean = false
    ) {
        statusCloseBtn = FAILED_CLOSE_BTN
        emoneyAnalytics.onShowErrorTracking(userSession.userId, irisSessionId, operatorName)

        if (eTollUpdateBalanceResultView.visibility == View.VISIBLE) {
            eTollUpdateBalanceResultView.hide()
        }

        tapETollCardView.visibility = View.VISIBLE

        if(isGlobalErrorShow){
            tapETollCardView.showGlobalError(errorMessage, errorMessageLabel)
        } else {
            tapETollCardView.showInitialState()
            tapETollCardView.showErrorState(errorMessage, errorMessageLabel,
                    imageUrl, isButtonShow, mandiriGetSocketTimeout, tapCashWriteFailed)
        }

        if(tapCashWriteFailed){
            emoneyAnalytics.onShowErrorTapcashImpressionUpdateBalanceCardFailed(irisSessionId)
        }

        emoneyAnalytics.openScreenFailedReadCardNFC(userSession.userId, irisSessionId)
    }

    protected fun showErrorDeviceUnsupported(errorMessage: String) {
        emoneyAnalytics.onShowErrorTrackingNFCNotSupproted(userSession.userId, irisSessionId)
        statusCloseBtn = NOT_SUPPORT_CLOSE_BTN
        tapETollCardView.visibility = View.VISIBLE
        tapETollCardView.showErrorDeviceUnsupportedState(errorMessage)
        emoneyAnalytics.openScreenFailedReadCardNFC(userSession.userId, irisSessionId)
    }

    protected fun showCardLastBalance(emoneyInquiry: EmoneyInquiry) {
        emoneyInquiry.attributesEmoneyInquiry?.let { operatorName = getOperatorName(it.issuer_id) }
        tapETollCardView.visibility = View.GONE
        eTollUpdateBalanceResultView.visibility = View.VISIBLE
        eTollUpdateBalanceResultView.showCardInfoFromApi(emoneyInquiry)

        statusCloseBtn = SUCCESS_CLOSE_BTN
        emoneyAnalytics.onShowLastBalance(operatorName, emoneyInquiry.attributesEmoneyInquiry?.cardNumber,
                emoneyInquiry.attributesEmoneyInquiry?.lastBalance,
                userSession.userId, irisSessionId)
        emoneyAnalytics.openScreenSuccessReadCardNFC(operatorName, userSession.userId, irisSessionId)

        emoneyInquiry.error?.let { eMoneyInquiryError ->
            view?.let {
                Toaster.build(it, eMoneyInquiryError.title, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
            }
        }
    }

    abstract fun processTagIntent(intent: Intent)

    protected fun navigateToNFCSettings() {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        startActivity(intent)
    }

    protected fun showLoading(operatorNameFromIssuer: String) {
        operatorName = operatorNameFromIssuer
        emoneyAnalytics.openScreenReadingCardNFC(operatorName, userSession.userId, irisSessionId)
        if (eTollUpdateBalanceResultView.visibility == View.VISIBLE) {
            eTollUpdateBalanceResultView.showLoading()
        } else {
            tapETollCardView.visibility = View.VISIBLE
            tapETollCardView.showLoading()
            emoneyAnalytics.onTapEmoneyCardShowLoading(userSession.userId, irisSessionId, operatorName)
        }
    }

    protected fun grantPermissionNfc() {
        if (!::permissionCheckerHelper.isInitialized) {
            permissionCheckerHelper = PermissionCheckerHelper()
        }
        activity?.let {
            permissionCheckerHelper.checkPermission(this,
                    PermissionCheckerHelper.Companion.PERMISSION_NFC,
                    object : PermissionCheckerHelper.PermissionCheckListener {

                        override fun onPermissionDenied(permissionText: String) {
                            permissionCheckerHelper.onPermissionDenied(it, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                        }

                        override fun onPermissionGranted() {
                            detectNfc()
                        }
                    }, getString(R.string.emoney_nfc_permission_rationale_message))
        }
    }

    private fun navigatePageToDigitalProduct(passData: DigitalCategoryDetailPassData) {
        activity?.let {
            val bundle = Bundle()
            bundle.putParcelable(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData)
            val intent = RouteManager.getIntent(it, ApplinkConsInternalDigital.PRODUCT_TEMPLATE_WITH_CATEGORY_ID,
                    DeeplinkMapperDigitalConst.CATEGORY_ID_ELECTRONIC_MONEY,
                    DeeplinkMapperDigitalConst.MENU_ID_ELECTRONIC_MONEY,
                    DeeplinkMapperDigitalConst.TEMPLATE_ID_ELECTRONIC_MONEY)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::permissionCheckerHelper.isInitialized) {
            activity?.let {
                permissionCheckerHelper.onRequestPermissionsResult(it, requestCode, permissions, grantResults)
            }
        }
    }

    protected fun navigateToLoginPage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
    }

    protected fun showInitialState(){
        tapETollCardView.showInitialState()
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1980

        const val ISSUER_ID_EMONEY = 1
        const val ISSUER_ID_BRIZZI = 2
        const val ISSUER_ID_TAP_CASH = 3
        const val ISSUER_ID_JAKCARD = 4
        const val ISSUER_ID_FLAZZ = 5

        const val OPERATOR_NAME_EMONEY = "emoney"
        const val OPERATOR_NAME_BRIZZI = "brizzi"
        const val OPERATOR_NAME_TAPCASH = "tapcash"
        const val OPERATOR_NAME_JAKCARD = "jakcard"
        const val OPERATOR_NAME_FLAZZ = "flazz"

        const val ETOLL_CATEGORY_ID = "34"

        private const val INITIAL_CLOSE_BTN = "initial"
        private const val SUCCESS_CLOSE_BTN = "success"
        private const val FAILED_CLOSE_BTN = "failed"
        private const val NOT_SUPPORT_CLOSE_BTN = "not support"

        private const val HELP_TAPCASH = "https://www.tokopedia.com/help"

    }
}
