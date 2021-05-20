package com.tokopedia.brizzi.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.brizzi.di.DaggerDigitalBrizziComponent
import com.tokopedia.brizzi.util.DigitalBrizziGqlMutation
import com.tokopedia.brizzi.util.DigitalBrizziGqlQuery
import com.tokopedia.brizzi.viewmodel.BrizziBalanceViewModel
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.common_electronic_money.di.NfcCheckBalanceInstance
import com.tokopedia.common_electronic_money.fragment.NfcCheckBalanceFragment
import com.tokopedia.common_electronic_money.util.CardUtils
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.utils.permission.PermissionCheckerHelper
import id.co.bri.sdk.Brizzi
import java.net.UnknownHostException
import javax.inject.Inject

class BrizziCheckBalanceFragment : NfcCheckBalanceFragment() {

    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var brizziBalanceViewModel: BrizziBalanceViewModel

    @Inject
    lateinit var viewModelFactories: ViewModelProvider.Factory
    @Inject
    lateinit var brizziInstance: Brizzi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateViewModel()
    }

    fun initiateViewModel() {
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactories)
            brizziBalanceViewModel = viewModelProvider.get(BrizziBalanceViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInjector()
        activity?.let {
            try {
                brizziInstance.setNfcAdapter(it)
                processTagIntent(it.intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun processTagIntent(intent: Intent) {
        if (isTagNfcValid(intent)) {
            if (!isDigitalSmartcardEnabled()) {
                onNavigateToHome()
            } else {
                // nfc enabled and process Mandiri NFC as default
                showLoading()
                processBrizzi(intent)
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
            val brizziComponent = DaggerDigitalBrizziComponent.builder()
                    .nfcCheckBalanceComponent(NfcCheckBalanceInstance.getNfcCheckBalanceComponent(it.application))
                    .build()
            brizziComponent.inject(this)
        }
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

    fun processBrizzi(intent: Intent) {
        if (CardUtils.isEmoneyCard(intent)) {
            processEmoney(intent)
        } else if (CardUtils.isBrizziCard(intent)) {
            executeBrizzi(false, intent)
        } else {
            showError(resources.getString(com.tokopedia.brizzi.R.string.brizzi_card_is_not_supported),
                    resources.getString(com.tokopedia.brizzi.R.string.brizzi_device_is_not_supported),
                    resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_card_is_not_supported),
                    false
            )
        }
    }

    protected open fun processEmoney(intent: Intent) {
        activity?.let { context ->
            context.finish()
            val newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY,
                    DigitalExtraParam.EXTRA_NFC)
            startActivity(newIntent)
        }
    }

    private fun executeBrizzi(needRefreshToken: Boolean, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isSupportBrizzi()) {
            getBalanceBrizzi(needRefreshToken, intent)

            brizziBalanceViewModel.emoneyInquiry.observe(this, Observer {
                showCardLastBalance(it)
            })

            brizziBalanceViewModel.tokenNeedRefresh.observe(this, Observer {
                getBalanceBrizzi(true, intent)
            })

            brizziBalanceViewModel.issuerId.observe(this, Observer {
                tapETollCardView.setIssuerId(it)
            })

            brizziBalanceViewModel.errorCardMessage.observe(this, Observer {
                showError(it, resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_check_balance_problem_label),
                        resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_failed_read_card),
                        true)
            })

            brizziBalanceViewModel.errorCommonBrizzi.observeForever { throwable ->
                context?.let {
                    val errorMessage = ErrorHandler.getErrorMessage(it, throwable)
                    if((throwable is UnknownHostException) || errorMessage.equals(getString(com.tokopedia.network.R.string.default_request_error_unknown))){
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
            }

            brizziBalanceViewModel.cardIsNotBrizzi.observe(this, Observer {
                emoneyAnalytics.onErrorReadingCard()
                showError(resources.getString(com.tokopedia.brizzi.R.string.brizzi_card_is_not_supported),
                        resources.getString(com.tokopedia.brizzi.R.string.brizzi_device_is_not_supported),
                        resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_card_is_not_supported),
                        false
                )
            })
        } else {
            showError(resources.getString(com.tokopedia.common_electronic_money.R.string.emoney_nfc_device_not_support),
                    resources.getString(com.tokopedia.brizzi.R.string.brizzi_device_is_not_supported),
                    resources.getString(com.tokopedia.brizzi.R.string.emoney_nfc_not_found),
                    false
            )
        }
    }

    private fun getBalanceBrizzi(needRefreshToken: Boolean, intent: Intent) {
        try {
            brizziBalanceViewModel.processBrizziTagIntent(intent, brizziInstance,
                    DigitalBrizziGqlQuery.tokenBrizzi,
                    DigitalBrizziGqlMutation.emoneyLogBrizzi,
                    needRefreshToken)
        } catch (e: Exception) {
            Log.e(BrizziCheckBalanceFragment.javaClass.simpleName, e.message)
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            if (this::brizziInstance.isInitialized && brizziInstance.nfcAdapter != null) {
                brizziInstance.nfcAdapter.disableForegroundDispatch(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::brizziInstance.isInitialized && brizziInstance.nfcAdapter != null) {
            if (!::permissionCheckerHelper.isInitialized) {
                permissionCheckerHelper = PermissionCheckerHelper()
            }
            grantPermissionNfc()
        } else {
            showErrorDeviceUnsupported(resources.getString(com.tokopedia.brizzi.R.string.brizzi_device_is_not_supported))
        }
    }

    override fun detectNfc() {
        activity?.let {
            if (!brizziInstance.nfcAdapter.isEnabled) {
                eTollUpdateBalanceResultView.visibility = View.GONE
                tapETollCardView.visibility = View.GONE

                AlertDialog.Builder(it)
                        .setMessage(getString(com.tokopedia.brizzi.R.string.brizzi_please_activate_nfc_from_settings))
                        .setPositiveButton(getString(com.tokopedia.brizzi.R.string.brizzi_activate)) { p0, p1 ->
                            emoneyAnalytics.onActivateNFCFromSetting()
                            navigateToNFCSettings()
                        }
                        .setNegativeButton(getString(com.tokopedia.brizzi.R.string.brizzi_cancel)) { p0, p1 ->
                            emoneyAnalytics.onCancelActivateNFCFromSetting()
                            nfcDisabledView.visibility = View.VISIBLE
                        }.show()
            } else {
                if (userSession.isLoggedIn) {
                    it.intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val pendingIntent = PendingIntent.getActivity(it, 0, it.intent, 0)
                    brizziInstance.nfcAdapter.enableForegroundDispatch(it, pendingIntent,
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
            showLoading()
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

        const val ARCHITECTURE_ARM64 = "arm64-v8a"
        const val ARCHITECTURE_ARM32 = "armeabi-v7a"

        private val ETOLL_CATEGORY_ID = "34"

        fun newInstance(): Fragment {
            return BrizziCheckBalanceFragment()
        }

    }
}