package com.tokopedia.emoney.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.common_electronic_money.di.NfcCheckBalanceInstance
import com.tokopedia.common_electronic_money.fragment.NfcCheckBalanceFragment
import com.tokopedia.common_electronic_money.util.CardUtils
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.R
import com.tokopedia.emoney.di.DaggerDigitalEmoneyComponent
import com.tokopedia.emoney.util.DigitalEmoneyGqlQuery
import com.tokopedia.emoney.viewmodel.EmoneyBalanceViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class EmoneyCheckBalanceFragment : NfcCheckBalanceFragment() {

    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    protected lateinit var emoneyBalanceViewModel: EmoneyBalanceViewModel
    private lateinit var nfcAdapter: NfcAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateViewModel()
    }

    protected open fun initiateViewModel() {
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            emoneyBalanceViewModel = viewModelProvider.get(EmoneyBalanceViewModel::class.java)
        }
    }

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

    override fun getPassData(operatorId: String, issuerId: Int): DigitalCategoryDetailPassData {
        return DigitalCategoryDetailPassData.Builder()
                .categoryId(ETOLL_CATEGORY_ID)
                .operatorId(operatorId)
                .clientNumber(eTollUpdateBalanceResultView.cardNumber)
                .additionalETollLastBalance(eTollUpdateBalanceResultView.cardLastBalance)
                .additionalETollLastUpdatedDate(eTollUpdateBalanceResultView.cardLastUpdatedDate)
                .additionalETollOperatorName(getOperatorName(issuerId))
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
            if (!isDigitalSmartcardEnabled()) {
                onNavigateToHome()
            } else {
                // nfc enabled and process Mandiri NFC as default
                showLoading()
                executeMandiri(intent)
            }
        }
    }

    private fun executeMandiri(intent: Intent) {
        if (CardUtils.isEmoneyCard(intent)) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                emoneyBalanceViewModel.processEmoneyTagIntent(IsoDep.get(tag),
                        DigitalEmoneyGqlQuery.rechargeEmoneyInquiryBalance,
                        0)
            } else {
                showError(ErrorHandler.getErrorMessage(context, MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD)),
                        resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_check_balance_problem_label),
                        resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_failed_read_card),
                        true)
            }
        } else if(CardUtils.isBrizziCard(intent)) {
            processBrizzi(intent)
        } else {
            showError(resources.getString(com.tokopedia.emoney.R.string.emoney_nfc_card_isnot_supported),
                    resources.getString(com.tokopedia.emoney.R.string.emoney_nfc_not_supported),
                    resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_card_is_not_supported),
                    false
            )
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
                val errorMessage = ErrorHandler.getErrorMessage(it, throwable)
                if((throwable is SocketTimeoutException)){
                    showError(resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_timeout_socket_error),
                            resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_timeout_socket_error_title),
                            resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_socket_time_out),
                            isButtonShow = true,
                            isGlobalErrorShow = false,
                            mandiriGetSocketTimeout = true
                    )
                } else if((throwable is UnknownHostException) || errorMessage.equals(getString(com.tokopedia.network.R.string.default_request_error_unknown))){
                    showError(resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_grpc_label_error),
                            resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_error_title),
                            "",
                            true)
                } else {
                    showError(errorMessage,
                            resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_error_title),
                            "",
                            true, true)
                }
            }

        })

        emoneyBalanceViewModel.errorCardMessage.observe(this, Observer {
            showError(ErrorHandler.getErrorMessage(context, it), resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_check_balance_problem_label),
                    resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_failed_read_card),
                    true
            )
        })

        emoneyBalanceViewModel.issuerId.observe(this, Observer {
            tapETollCardView.setIssuerId(it)
        })

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
            showErrorDeviceUnsupported(resources.getString(R.string.emoney_nfc_not_supported))
        }
    }

    override fun detectNfc() {
        activity?.let {
            if (!nfcAdapter.isEnabled) {
                eTollUpdateBalanceResultView.visibility = View.GONE
                tapETollCardView.visibility = View.GONE

                AlertDialog.Builder(it)
                        .setMessage(getString(R.string.emoney_nfc_please_activate_nfc_from_settings))
                        .setPositiveButton(getString(R.string.emoney_nfc_activate)) { p0, p1 ->
                            emoneyAnalytics.onActivateNFCFromSetting()
                            navigateToNFCSettings()
                        }
                        .setNegativeButton(getString(R.string.emoney_nfc_cancel)) { p0, p1 ->
                            emoneyAnalytics.onCancelActivateNFCFromSetting()
                            nfcDisabledView.visibility = View.VISIBLE
                        }.show()
            } else {
                if (userSession.isLoggedIn) {
                    it.intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val pendingIntent = PendingIntent.getActivity(it, 0, it.intent, 0)
                    nfcAdapter.enableForegroundDispatch(it, pendingIntent,
                            arrayOf<IntentFilter>(), null)
                    nfcDisabledView.visibility = View.GONE

                    if (eTollUpdateBalanceResultView.visibility == View.GONE) {
                        emoneyAnalytics.onEnableNFC()
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

    companion object {
        const val REQUEST_CODE_LOGIN = 1980
        const val ETOLL_CATEGORY_ID = "34"

        fun newInstance(): Fragment {
            return EmoneyCheckBalanceFragment()
        }
    }
}