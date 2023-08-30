package com.tokopedia.emoney.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Bundle
import android.os.DeadObjectException
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.MENU_ID_ELECTRONIC_MONEY
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.common_electronic_money.data.RechargeEmoneyInquiryLogRequest
import com.tokopedia.common_electronic_money.di.NfcCheckBalanceInstance
import com.tokopedia.common_electronic_money.fragment.NfcCheckBalanceFragment
import com.tokopedia.common_electronic_money.util.CardUtils
import com.tokopedia.common_electronic_money.util.KeyLogEmoney.LOG_TYPE
import com.tokopedia.common_electronic_money.util.KeyLogEmoney.TAPCASH_TAG
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.R
import com.tokopedia.emoney.di.DaggerDigitalEmoneyComponent
import com.tokopedia.emoney.domain.request.JakCardStatus
import com.tokopedia.emoney.domain.response.BCAResponseMapper
import com.tokopedia.emoney.integration.BCAConstResult.GEN_1_CARD
import com.tokopedia.emoney.integration.BCAConstResult.GEN_2_CARD
import com.tokopedia.emoney.integration.BCALibrary
import com.tokopedia.emoney.util.DigitalEmoneyGqlQuery
import com.tokopedia.emoney.viewmodel.BCABalanceViewModel
import com.tokopedia.emoney.viewmodel.EmoneyBalanceViewModel
import com.tokopedia.emoney.viewmodel.JakCardBalanceViewModel
import com.tokopedia.emoney.viewmodel.TapcashBalanceViewModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class EmoneyCheckBalanceFragment : NfcCheckBalanceFragment() {

    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var nfcAdapter: NfcAdapter

    val errorHanlderBuilder = ErrorHandler.Builder().apply {
        sendToScalyr = true
        className = CLASS_NAME
    }

    var issuerActive = ISSUER_ID_EMONEY

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var bcaLibrary: BCALibrary

    private val emoneyBalanceViewModel by viewModels<EmoneyBalanceViewModel> { viewModelFactory }
    private val tapcashBalanceViewModel by viewModels<TapcashBalanceViewModel> { viewModelFactory }
    private val jakcardBalanceViewModel by viewModels<JakCardBalanceViewModel> { viewModelFactory }
    private val bcaBalanceViewModel by viewModels<BCABalanceViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInjector()

        activity?.let {
            try {
                nfcAdapter = NfcAdapter.getDefaultAdapter(it)
                processTagIntent(it.intent)
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    override fun getPassData(operatorId: String, issuerId: Int, isBCAGenOne: Boolean): DigitalCategoryDetailPassData {
        return DigitalCategoryDetailPassData.Builder()
                .categoryId(ETOLL_CATEGORY_ID)
                .operatorId(operatorId)
                .menuId(MENU_ID_ELECTRONIC_MONEY)
                .clientNumber(eTollUpdateBalanceResultView.cardNumber)
                .additionalETollLastBalance(eTollUpdateBalanceResultView.cardLastBalance)
                .additionalETollLastUpdatedDate(eTollUpdateBalanceResultView.cardLastUpdatedDate)
                .additionalETollOperatorName(getOperatorName(issuerId))
                .isBCAGenOne(isBCAGenOne)
                .build()
    }

    override fun initInjector() {
        activity?.let {
            val emoneyComponent = DaggerDigitalEmoneyComponent.builder()
                    .nfcCheckBalanceComponent(NfcCheckBalanceInstance.getNfcCheckBalanceComponent(it.application))
                    .build()
            emoneyComponent.inject(this)
        }
    }

    override fun processTagIntent(intent: Intent) {
        if (isTagNfcValid(intent)) {
            // nfc enabled and process Mandiri NFC as default
            try {
                executeCard(intent)
            } catch (e: DeadObjectException) {
                showCommonMessageError()
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    private fun executeCard(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val rawPublicKey = getPublicKey()
        val rawPrivateKey = getPrivateKey()
        if (CardUtils.isTapcashCard(intent)) {
            issuerActive = ISSUER_ID_TAP_CASH
            showLoading(getOperatorName(issuerActive))
            tapcashBalanceViewModel.processTapCashTagIntent(IsoDep.get(tag),
                    DigitalEmoneyGqlQuery.rechargeBniTapcashQuery)
        } else if(CardUtils.isJakCard(intent) ) {
            issuerActive = ISSUER_ID_JAKCARD
            showLoading(getOperatorName(issuerActive))
            jakcardBalanceViewModel.processJakCardTagIntent(IsoDep.get(tag), false, rawPublicKey, rawPrivateKey)
        } else if(CardUtils.isJakCardDev(intent)) {
            issuerActive = ISSUER_ID_JAKCARD
            showLoading(getOperatorName(issuerActive))
            jakcardBalanceViewModel.processJakCardTagIntent(IsoDep.get(tag), true,  rawPublicKey, rawPrivateKey)
        } else if (CardUtils.isEmoneyCard(intent)){
            if (tag != null) {
                issuerActive = ISSUER_ID_EMONEY
                showLoading(getOperatorName(issuerActive))
                emoneyBalanceViewModel.processEmoneyTagIntent(IsoDep.get(tag),
                        DigitalEmoneyGqlQuery.rechargeEmoneyInquiryBalance,
                        0)
            } else {
                showCommonMessageError()
            }
        } else if(CardUtils.isBrizziCard(intent)) {
            processBrizzi(intent)
        } else {
            initializeBCALibs(tag)
            val bcaIsMyCard = bcaLibrary.C_BCAIsMyCard()
            if(bcaIsMyCard.strLogRsp.startsWith(GEN_2_CARD)) {
                bcaBalanceViewModel.processBCATagBalance(IsoDep.get(tag), TEMP_M_ID, TEMP_T_ID)
            } else if(bcaIsMyCard.strLogRsp.startsWith(GEN_1_CARD)){
                val dataBalance = bcaLibrary.C_BCACheckBalance()
                showCardLastBalance(BCAResponseMapper.bcaMapper(dataBalance, true))
            } else {
                context?.let { context ->
                    showError(
                        context.resources.getString(com.tokopedia.emoney.R.string.emoney_nfc_card_isnot_supported),
                        context.resources.getString(com.tokopedia.emoney.R.string.emoney_nfc_not_supported),
                        context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_card_is_not_supported),
                        false
                    )
                }
            }
        }
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

        emoneyBalanceViewModel.errorInquiryBalance.observe(this, Observer {  throwable ->
            context?.let {
                var errorThrowable = throwable
                if ((throwable.message ?: "").contains(it.resources.getString(com.tokopedia.common_digital.R.string.digital_common_grpc_error_msg), true)) {
                    errorThrowable = MessageErrorException(it.resources.getString(com.tokopedia.common_digital.R.string.digital_common_grpc_full_page_title))
                }
                val errorMessage = ErrorHandler.getErrorMessagePair(it, errorThrowable, errorHanlderBuilder)
                if((throwable is SocketTimeoutException)){
                    showError(it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_timeout_socket_error),
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_timeout_socket_error_title)+" "+ errorMessage.second,
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_socket_time_out),
                            isButtonShow = true,
                            isGlobalErrorShow = false,
                            mandiriGetSocketTimeout = true
                    )
                } else if((throwable is UnknownHostException) || errorMessage.first.equals(it.resources.getString(com.tokopedia.network.R.string.default_request_error_unknown))){
                    showError(it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_grpc_label_error),
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_error_title)+ " "+ errorMessage.second,
                            "",
                            true)
                } else {
                    showError(errorMessage.first.orEmpty(),
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_error_title)+ " "+ errorMessage.second,
                            "",
                            true, true)
                }
            }

        })

        emoneyBalanceViewModel.errorCardMessage.observe(this, Observer {
            context?.let { context ->
                val errorMessage = ErrorHandler.getErrorMessagePair(context, it, errorHanlderBuilder)
                showError(errorMessage.first.orEmpty(),
                    context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_check_balance_problem_label)+" "+errorMessage.second,
                    context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_failed_read_card_link),
                    true
                )
            }
        })

        emoneyBalanceViewModel.issuerId.observe(viewLifecycleOwner, Observer {
            tapETollCardView.setIssuerId(it)
        })

        tapcashBalanceViewModel.errorCardMessage.observe(viewLifecycleOwner, Observer {
            context?.let { context ->
                val errorMessage =
                    ErrorHandler.getErrorMessagePair(context, it, errorHanlderBuilder)
                showError(
                    errorMessage.first.orEmpty(),
                    context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_check_balance_problem_label) + " " + errorMessage.second,
                    context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_failed_read_card_link),
                    true
                )
            }
        })

        tapcashBalanceViewModel.errorInquiry.observe(viewLifecycleOwner, Observer { pair ->
            context?.let {
                sendLogDebugErrorNetworkTapcash(pair.second)
                val errorMessage = ErrorHandler.getErrorMessagePair(it, pair.first, errorHanlderBuilder)
                if((pair.first is SocketTimeoutException)){
                    showError(it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_tapcash_error_title),
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_timeout_socket_error_title_tapcash)+" "+errorMessage.second,
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_socket_time_out),
                            isButtonShow = true,
                            isGlobalErrorShow = false,
                            tapCashWriteFailed = true
                    )
                } else if((pair.first is UnknownHostException) || errorMessage.first.equals(it.resources.getString(com.tokopedia.network.R.string.default_request_error_unknown))){
                    showError(it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_grpc_label_error),
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_error_title)+" "+errorMessage.second,
                            "",
                            true)
                } else {
                    showError(errorMessage.first.orEmpty(),
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_error_title)+" "+errorMessage.second,
                            it.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_socket_time_out),
                            true,
                            true)
                }
            }
        })

        tapcashBalanceViewModel.errorWrite.observe(viewLifecycleOwner, Observer { pair ->
            context?.let { context ->
                updateLogErrorTapcash(pair.second)
                val errorMessage = ErrorHandler.getErrorMessagePair(context, pair.first, errorHanlderBuilder)
                showError(errorMessage.first.orEmpty(),
                        context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_tapcash_write_error_desc)+" "+errorMessage.second,
                        context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_socket_time_out),
                        isButtonShow = true,
                        isGlobalErrorShow = false,
                        tapCashWriteFailed = true
                )
            }
        })

        tapcashBalanceViewModel.tapcashLogError.observe(viewLifecycleOwner, Observer {
            when(it.first) {
                is Success -> {
                    // do nothing
                }
                is Fail -> {
                    sendLogDebugTapcash(it.second)
                }
            }
        })

        tapcashBalanceViewModel.tapcashInquiry.observe(viewLifecycleOwner, Observer {
            it.error?.let { error ->
                when (error.status) {
                    0 -> showCardLastBalance(it)
                    1 -> it.error?.let { error ->
                        context?.let { context ->
                            showError(
                                errorMessage = context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_tapcash_error_title),
                                errorMessageLabel =  error.title,
                                context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_socket_time_out),
                                isButtonShow = true,
                                isGlobalErrorShow = false,
                                tapCashWriteFailed = true
                            )
                        }
                    }
                    else -> return@let
                }
            }
        })

        jakcardBalanceViewModel.jakCardInquiry.observe(viewLifecycleOwner, Observer { jakCardInquiry ->
            jakCardInquiry.attributesEmoneyInquiry?.let { attribute ->
                when(attribute.status) {
                    JakCardStatus.DONE.status -> showCardLastBalance(jakCardInquiry)
                    JakCardStatus.ERROR.status -> jakCardInquiry.error?.let { error ->
                        showError(error.title)
                    }
                }
            }
        })

        jakcardBalanceViewModel.errorCardMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            context?.let { context ->
                val errorMessage = ErrorHandler.getErrorMessagePair(context, errorMessage, errorHanlderBuilder)
                showError(errorMessage.first.orEmpty())
            }
        })

        bcaBalanceViewModel.bcaInquiry.observe(viewLifecycleOwner, Observer{ bcaInquiry ->
            bcaInquiry.attributesEmoneyInquiry?.let {attribute ->
               //TODO add condition error
               showCardLastBalance(bcaInquiry)
            }
        })

        bcaBalanceViewModel.errorCardMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            context?.let { context ->
                val errorMessage = ErrorHandler.getErrorMessagePair(context, errorMessage, errorHanlderBuilder)
                showError(errorMessage.first.orEmpty())
            }
        })
    }

    private fun updateLogErrorTapcash(param: RechargeEmoneyInquiryLogRequest) {
        tapcashBalanceViewModel.tapcashErrorLogging(param)
    }

    protected open fun processBrizzi(intent: Intent) {
        activity?.let { context ->
            context.finish()
            val newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_BRIZZI,
                    DigitalExtraParam.EXTRA_NFC)
            startActivity(newIntent)
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            if (this::nfcAdapter.isInitialized && nfcAdapter != null) {
                nfcAdapter.disableForegroundDispatch(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::nfcAdapter.isInitialized && nfcAdapter != null) {
            if (!::permissionCheckerHelper.isInitialized) {
                permissionCheckerHelper = PermissionCheckerHelper()
            }
            grantPermissionNfc()
        } else {
            context?.let { context ->
                showErrorDeviceUnsupported(context.resources.getString(R.string.emoney_nfc_not_supported))
            }
        }
    }

    override fun detectNfc() {
        activity?.let {
            if (!nfcAdapter.isEnabled) {
                eTollUpdateBalanceResultView.visibility = View.GONE
                tapETollCardView.visibility = View.GONE

                AlertDialog.Builder(it)
                        .setMessage(it.resources.getString(R.string.emoney_nfc_please_activate_nfc_from_settings))
                        .setPositiveButton(it.resources.getString(R.string.emoney_nfc_activate)) { p0, p1 ->
                            emoneyAnalytics.onActivateNFCFromSetting()
                            navigateToNFCSettings()
                        }
                        .setNegativeButton(it.resources.getString(R.string.emoney_nfc_cancel)) { p0, p1 ->
                            emoneyAnalytics.onCancelActivateNFCFromSetting()
                            nfcDisabledView.visibility = View.VISIBLE
                        }.show()
            } else {
                if (userSession.isLoggedIn) {
                    it.intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getActivity(it, 0, it.intent, PendingIntent.FLAG_MUTABLE)
                    } else {
                        PendingIntent.getActivity(it, 0, it.intent, 0)
                    }
                    nfcAdapter.enableForegroundDispatch(it, pendingIntent,
                            arrayOf<IntentFilter>(), null)
                    nfcDisabledView.visibility = View.GONE

                    if (eTollUpdateBalanceResultView.visibility == View.GONE) {
                        emoneyAnalytics.onEnableNFC(getOperatorName(issuerActive))
                        tapETollCardView.visibility = View.VISIBLE
                    } else {
                        //do nothing
                    }
                } else {
                    navigateToLoginPage()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_LOGIN){
            showInitialState()
            data?.let {
                if (userSession.isLoggedIn) {
                    data?.let {
                        processTagIntent(data)
                    }
                }
            }
        } else if(resultCode == Activity.RESULT_CANCELED && requestCode == REQUEST_CODE_LOGIN){
            activity?.finish()
        }
    }

    private fun sendLogDebugTapcash(param: RechargeEmoneyInquiryLogRequest) {
        val map = HashMap<String, String>()
        map.put(ISSUER_KEY, param.log.issueId.toString())
        map.put(CARD_NUMBER_KEY, param.log.cardNumber)
        map.put(RC_KEY, param.log.rc)
        map.put(LOG_TYPE, TAPCASH_ERROR_LOGGER)
        ServerLogger.log(Priority.P2, TAPCASH_TAG, map)
    }

    private fun sendLogDebugErrorNetworkTapcash(param: RechargeEmoneyInquiryLogRequest) {
        val map = HashMap<String, String>()
        map.put(ISSUER_KEY, param.log.issueId.toString())
        map.put(CARD_NUMBER_KEY, param.log.cardNumber)
        map.put(RC_KEY, param.log.rc)
        map.put(LOG_TYPE, TAPCASH_NETWORK_ERROR_LOGGER)
        ServerLogger.log(Priority.P2, TAPCASH_TAG, map)
    }

    private fun getPublicKey(): String {   
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            getString(com.tokopedia.keys.R.string.rfvtgbyhn)
        } else {
            getString(com.tokopedia.keys.R.string.ijnuhbygv)
        }
    }

    private fun getPrivateKey(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            getString(com.tokopedia.keys.R.string.qazwsxedc)
        } else {
            getString(com.tokopedia.keys.R.string.plmoknijb)
        }
    }

    private fun showCommonMessageError() {
        context?.let { context ->
            val errorMessage = ErrorHandler.getErrorMessagePair(context, MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD), errorHanlderBuilder)
            showError(errorMessage.first.orEmpty(),
                context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_check_balance_problem_label)+" "+errorMessage.second,
                context.resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_failed_read_card_link),
                true)
        }
    }

    private fun initializeBCALibs(tag: Tag?) {
        if (tag != null) {
            bcaLibrary.myTag = tag
            val isoDep = IsoDep.get(tag)
            if (!isoDep.isConnected) {
                isoDep.connect()
            }
        }
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1980
        const val CLASS_NAME = "EmoneyCheckBalanceFragment"
        private const val ISSUER_KEY = "issuer_id"
        private const val CARD_NUMBER_KEY = "card_number"
        private const val RC_KEY = "rc"
        private const val TAPCASH_ERROR_LOGGER = "TAPCASH_ERROR_LOGGER"
        private const val TAPCASH_NETWORK_ERROR_LOGGER = "TAPCASH_NETWORK_ERROR_LOGGER"
        private const val TEMP_M_ID = "000885000015999"
        private const val TEMP_T_ID = "ETES0067"

        fun newInstance(): Fragment {
            return EmoneyCheckBalanceFragment()
        }
    }
}
