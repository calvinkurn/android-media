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
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.emoney.R
import com.tokopedia.emoney.data.EmoneyInquiry
import com.tokopedia.emoney.data.NfcCardErrorTypeDef
import com.tokopedia.emoney.di.DaggerDigitalEmoneyComponent
import com.tokopedia.emoney.util.EmoneyAnalytics
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
import kotlinx.android.synthetic.main.fragment_emoney_nfc_check_balance.*
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class NfcCheckBalanceFragment : BaseDaggerFragment() {

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

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoney_nfc_check_balance, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            emoneyBalanceViewModel = viewModelProvider.get(EmoneyBalanceViewModel::class.java)
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

        view_update_balance_result.setListener(object : ETollUpdateBalanceResultView.OnTopupETollClickListener {
            override fun onClick(operatorId: String, issuerId: Int) {
                emoneyAnalytics.onClickTopupEmoney(getOperatorName(issuerId))

                val passData = DigitalCategoryDetailPassData.Builder()
                        .categoryId(ETOLL_CATEGORY_ID)
                        .operatorId(operatorId)
                        .clientNumber(view_update_balance_result.cardNumber)
                        .additionalETollLastBalance(view_update_balance_result.cardLastBalance)
                        .additionalETollLastUpdatedDate(view_update_balance_result.cardLastUpdatedDate)
                        .additionalETollOperatorName(getOperatorName(issuerId))
                        .build()
                activity?.let {
                    if (it.intent != null && it.intent.getStringExtra(ApplinkConsInternalDigital.PARAM_SMARTCARD) != null) {
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
        })

        view_tap_emoney_card.setListener(object : TapETollCardView.OnTapEtoll {
            override fun tryAgainTopup(issuerId: Int) {
                emoneyAnalytics.onClickTryAgainTapEmoney(getOperatorName(issuerId))
            }
        })

        view_nfc_disabled.setListener(object : NFCDisabledView.OnActivateNFCClickListener {
            override fun onClick() {
                emoneyAnalytics.onClickActivateNFC()
                navigateToNFCSettings()
            }
        })
    }

    override fun initInjector() {
        activity?.let {
            val emoneyComponent = DaggerDigitalEmoneyComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build()
            emoneyComponent.inject(this)
        }
    }

    private fun processTagIntent(intent: Intent) {
        if (intent != null && !TextUtils.isEmpty(intent.action) &&
                (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED ||
                        intent.action == NfcAdapter.ACTION_TAG_DISCOVERED)) {

            if (!isDigitalSmartcardEnabled()) {
                activity?.let {
                    Toast.makeText(it, getString(R.string.emoney_nfc_feature_unavailable), Toast.LENGTH_SHORT).show()
                    RouteManager.route(it, ApplinkConst.HOME_FEED)
                    it.finish()
                }
            } else {
                // nfc enabled and process Mandiri NFC as default
                showLoading()
                executeMandiri(intent)
            }
        }
    }

    private fun executeMandiri(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        try {
            if (tag != null) {
                val isodep = IsoDep.get(tag);
                emoneyBalanceViewModel.processEmoneyTagIntent(isodep,
                        GraphqlHelper.loadRawString(resources, R.raw.query_emoney_inquiry_balance),
                        0)
            }
        } catch (e: IOException) {
            showError(NfcCardErrorTypeDef.FAILED_READ_CARD)
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
            view_tap_emoney_card.setIssuerId(it)
        })

        emoneyBalanceViewModel.cardIsNotEmoney.observe(this, Observer {
            executeBrizzi(false, intent)
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
                view_tap_emoney_card.setIssuerId(it)
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
            Log.e(NfcCheckBalanceFragment.javaClass.simpleName, e.message)
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
        if (view_update_balance_result.visibility == View.VISIBLE) {
            view_update_balance_result.showError(errorMessage)
        } else {
            view_tap_emoney_card.visibility = View.VISIBLE
            view_tap_emoney_card.showInitialState()
            view_tap_emoney_card.showErrorState(errorMessage)
        }
    }

    private fun showErrorDeviceUnsupported(errorMessage: String) {
        view_tap_emoney_card.visibility = View.VISIBLE
        view_tap_emoney_card.showErrorDeviceUnsupportedState(errorMessage)
    }

    private fun showCardLastBalance(emoneyInquiry: EmoneyInquiry) {
        emoneyAnalytics.onShowLastBalance()
        view_tap_emoney_card.visibility = View.GONE
        view_update_balance_result.visibility = View.VISIBLE
        view_update_balance_result.showCardInfoFromApi(emoneyInquiry)
        activity?.let { activity ->
            emoneyInquiry.error?.let {
                NetworkErrorHelper.showGreenCloseSnackbar(activity, it.title)
            }
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
        if (view_update_balance_result.visibility == View.VISIBLE) {
            view_update_balance_result.showLoading()
        } else {
            view_tap_emoney_card.visibility = View.VISIBLE
            view_tap_emoney_card.showLoading()
            emoneyAnalytics.onTapEmoneyCardShowLoading()
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
                                detectNFC()
                            }
                        }, getString(R.string.emoney_nfc_permission_rationale_message))
            }
        } else {
            showErrorDeviceUnsupported(resources.getString(R.string.emoney_nfc_not_supported))
        }
    }

    private fun navigatePageToDigitalProduct(passData: DigitalCategoryDetailPassData) {
        activity?.let {
            val bundle = Bundle()
            bundle.putParcelable(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData)
            val intent = RouteManager.getIntent(it, ApplinkConsInternalDigital.DIGITAL_PRODUCT_FORM)
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

    private fun detectNFC() {
        activity?.let {
            if (!brizziInstance.nfcAdapter.isEnabled) {
                view_update_balance_result.visibility = View.GONE
                view_tap_emoney_card.visibility = View.GONE

                AlertDialog.Builder(it)
                        .setMessage(getString(R.string.emoney_please_activate_nfc_from_settings))
                        .setPositiveButton(getString(R.string.emoney_activate)) { p0, p1 ->
                            emoneyAnalytics.onActivateNFCFromSetting()
                            navigateToNFCSettings()
                        }
                        .setNegativeButton(getString(R.string.emoney_cancel)) { p0, p1 ->
                            emoneyAnalytics.onCancelActivateNFCFromSetting()
                            view_nfc_disabled.visibility = View.VISIBLE
                        }.show()
            } else {
                if (userSession.isLoggedIn) {
                    it.intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    val pendingIntent = PendingIntent.getActivity(it, 0, it.intent, 0)
                    brizziInstance.nfcAdapter.enableForegroundDispatch(it, pendingIntent,
                            arrayOf<IntentFilter>(), null)
                    view_nfc_disabled.visibility = View.GONE

                    if (view_update_balance_result.visibility == View.GONE) {
                        emoneyAnalytics.onEnableNFC()
                        view_tap_emoney_card.visibility = View.VISIBLE
                    } else {
                        //do nothing
                    }
                } else {
                    navigateToLoginPage()
                }
            }
        }
    }

    private fun navigateToLoginPage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
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

        const val ISSUER_ID_EMONEY = 1
        const val ISSUER_ID_BRIZZI = 2

        const val ARCHITECTURE_ARM64 = "arm64-v8a"
        const val ARCHITECTURE_ARM32 = "armeabi-v7a"

        const val OPERATOR_NAME_EMONEY = "emoney"
        const val OPERATOR_NAME_BRIZZI = "brizzi"

        private val ETOLL_CATEGORY_ID = "34"

        fun newInstance(): Fragment {
            return NfcCheckBalanceFragment()
        }

    }
}