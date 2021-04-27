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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.DEFAULT_ELECTRONICMONEY_MENU_ID
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
import com.tokopedia.utils.permission.PermissionCheckerHelper
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
                .menuId(DEFAULT_ELECTRONICMONEY_MENU_ID)
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
        if (CardUtils.cardIsEmoney(intent)) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                emoneyBalanceViewModel.processEmoneyTagIntent(IsoDep.get(tag),
                        DigitalEmoneyGqlQuery.rechargeEmoneyInquiryBalance,
                        0)
            } else {
                showError(NfcCardErrorTypeDef.FAILED_READ_CARD)
            }
        } else {
            processBrizzi(intent)
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

        fun newInstance(): Fragment {
            return EmoneyCheckBalanceFragment()
        }
    }
}