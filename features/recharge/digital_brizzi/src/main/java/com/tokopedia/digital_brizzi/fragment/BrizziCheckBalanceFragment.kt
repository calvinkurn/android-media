package com.tokopedia.digital_brizzi.fragment

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
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.common_electronic_money.di.NfcCheckBalanceInstance
import com.tokopedia.common_electronic_money.fragment.NfcCheckBalanceFragment
import com.tokopedia.digital_brizzi.R
import com.tokopedia.digital_brizzi.di.DaggerDigitalBrizziComponent
import com.tokopedia.digital_brizzi.viewmodel.BrizziBalanceViewModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import id.co.bri.sdk.Brizzi
import javax.inject.Inject

class BrizziCheckBalanceFragment : NfcCheckBalanceFragment() {

    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var brizziBalanceViewModel: BrizziBalanceViewModel

    var brizziInstance: Brizzi = Brizzi.getInstance()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
//    @Inject
//    lateinit var brizziInstance: Brizzi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            brizziBalanceViewModel = viewModelProvider.get(BrizziBalanceViewModel::class.java)
        }
    }

    fun setOnNewIntent(intent: Intent) {
        processTagIntent(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInjector()

        activity?.let {
            brizziInstance.setNfcAdapter(it)
            processTagIntent(it.intent)
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

    private fun processTagIntent(intent: Intent) {
        if (isTagNfcValid(intent)) {
            if (!isDigitalSmartcardEnabled()) {
                onNavigateToHome()
            } else {
                showLoading()
                executeBrizzi(false, intent)
            }
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
                showError(it)
            })

            brizziBalanceViewModel.cardIsNotBrizzi.observe(this, Observer {
                emoneyAnalytics.onErrorReadingCard()
                showError(resources.getString(R.string.emoney_card_isnot_supported))
            })
        } else {
            showError(resources.getString(R.string.emoney_device_isnot_supported))
        }
    }

    private fun getBalanceBrizzi(needRefreshToken: Boolean, intent: Intent) {
        try {
            brizziBalanceViewModel.processBrizziTagIntent(intent, brizziInstance,
                    GraphqlHelper.loadRawString(resources, R.raw.query_token_brizzi),
                    GraphqlHelper.loadRawString(resources, R.raw.mutation_emoney_log_brizzi),
                    needRefreshToken)
        } catch (e: Exception) {
            Log.e(BrizziCheckBalanceFragment.javaClass.simpleName, e.message)
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            if (brizziInstance.nfcAdapter != null) {
                brizziInstance.nfcAdapter.disableForegroundDispatch(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (brizziInstance.nfcAdapter != null) {
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
            if (!brizziInstance.nfcAdapter.isEnabled) {
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

        const val ARCHITECTURE_ARM64 = "arm64-v8a"
        const val ARCHITECTURE_ARM32 = "armeabi-v7a"

        private val ETOLL_CATEGORY_ID = "34"

        fun newInstance(): Fragment {
            return BrizziCheckBalanceFragment()
        }

    }
}