package com.tokopedia.emoney.view.fragment

import android.app.AlertDialog
import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.emoney.EmoneyAnalytics
import com.tokopedia.emoney.NFCUtils
import com.tokopedia.emoney.R
import com.tokopedia.emoney.data.RechargeEmoneyInquiry
import com.tokopedia.emoney.di.DaggerDigitalEmoneyComponent
import com.tokopedia.emoney.view.compoundview.ETollUpdateBalanceResultView
import com.tokopedia.emoney.view.compoundview.NFCDisabledView
import com.tokopedia.emoney.view.compoundview.TapETollCardView
import com.tokopedia.emoney.viewmodel.EmoneyInquiryBalanceViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.remoteconfig.GraphqlHelper
import com.tokopedia.remoteconfig.RemoteConfig
import id.co.bri.sdk.Brizzi
import id.co.bri.sdk.BrizziCardObject
import id.co.bri.sdk.Callback
import id.co.bri.sdk.exception.BrizziException
import java.io.IOException
import javax.inject.Inject

class EmoneyCheckBalanceNFCFragment : BaseDaggerFragment() {

    private lateinit var tapETollCardView: TapETollCardView
    private lateinit var nfcDisabledView: NFCDisabledView
    private lateinit var eTollUpdateBalanceResultView: ETollUpdateBalanceResultView
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var isoDep: IsoDep
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var emoneyInquiryBalanceViewModel: EmoneyInquiryBalanceViewModel
    private lateinit var brizziInstance: Brizzi

    private var intentFiltersArray: Array<IntentFilter>? = null
    private var techListsArray: Array<Array<String>>? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig
    @Inject
    lateinit var emoneyAnalytics: EmoneyAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            emoneyInquiryBalanceViewModel = viewModelProvider.get(EmoneyInquiryBalanceViewModel::class.java)
        }
    }

    override fun getScreenName(): String? {
        return EmoneyCheckBalanceNFCFragment::class.simpleName
    }

    override fun initInjector() {
        activity?.let {
            val emoneyComponent = DaggerDigitalEmoneyComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build()
            emoneyComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_emoney_nfc_check_balance, container, false)
        eTollUpdateBalanceResultView = view.findViewById(R.id.view_update_balance_result)
        nfcDisabledView = view.findViewById(R.id.view_nfc_disabled)
        tapETollCardView = view.findViewById(R.id.view_tap_emoney_card)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        brizziInstance = Brizzi.getInstance()
        brizziInstance.Init("R04XSUbnm1GXNmDiXx9ysWMpFWBr", "IlFDLgR31ACt7aqH")
        brizziInstance.setUserName("Tokopedia")

        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        pendingIntent = PendingIntent.getActivity(activity, 0, Intent(activity, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        eTollUpdateBalanceResultView.setListener(object : ETollUpdateBalanceResultView.OnTopupETollClickListener {
            override fun onClick() {
                emoneyAnalytics.onClickTopupEmoney()
                //TODO implement navigation page to digital product
            }
        })

        nfcDisabledView.setListener(object : NFCDisabledView.OnActivateNFCClickListener {
            override fun onClick() {
                emoneyAnalytics.onClickActivateNFC()
                directToNFCSettingsPage()
            }
        })

        //process intent filter
        val isodep = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        intentFiltersArray = arrayOf(isodep)
        techListsArray = arrayOf(arrayOf(IsoDep::class.java.name, NfcA::class.java.name))

        activity?.let {
            if (it.intent != null && !TextUtils.isEmpty(it.intent.action) &&
                    it.intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {

                if (!isDigitalSmartcardEnabled()) {
                    Toast.makeText(activity, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
                    RouteManager.route(activity, ApplinkConst.HOME_FEED)
                    activity?.finish()
                } else {
                    // nfc enabled
                    val tag = it.intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                    if (tag != null) {
                        handleTagFromIntent(tag)
                    }
                }
            }
        }
    }

    private fun handleTagFromIntent(tag: Tag) {
        showLoading()

        //do something with tagFromIntent
        isoDep = IsoDep.get(tag)

        try {
            isoDep.close()
            isoDep.connect()
            isoDep.setTimeout(TRANSCEIVE_TIMEOUT_IN_SEC) // 5 sec time out

            val commandSelectEMoney = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_SELECT_EMONEY))
            val commandCardAttribute = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_ATTRIBUTE))
            val commandCardInfo = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_INFO))
            val commandLastBalance = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_LAST_BALANCE))

            run {
                val cardUID = isoDep.getTag().getId()

                val responseSelectEMoney = NFCUtils.toHex(commandSelectEMoney)
                val responseCardAttribute = NFCUtils.toHex(commandCardAttribute)
                val responseCardUID = NFCUtils.toHex(cardUID)
                val responseCardInfo = NFCUtils.toHex(commandCardInfo)
                val responseCardLastBalance = NFCUtils.toHex(commandLastBalance)

                //success scan card e-money
                if (responseSelectEMoney == COMMAND_SUCCESSFULLY_EXECUTED) {
                    activity?.let {
                        val mapAttributes = HashMap<String, kotlin.Any>()
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_CARD_ATTRIBUTE] = responseCardAttribute
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_CARD_INFO] = responseCardInfo
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_ISSUER_ID] = ISSUER_ID_EMONEY
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_CARD_UUID] = responseCardUID
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_LAST_BALANCE] = responseCardLastBalance

                        emoneyInquiryBalanceViewModel.getEmoneyInquiryBalance(
                                EmoneyInquiryBalanceViewModel.PARAM_INQUIRY,
                                GraphqlHelper.loadRawString(it.resources, R.raw.query_emoney_inquiry_balance),
                                0,
                                mapAttributes,
                                this::onSuccessInquiryBalance,
                                this::onErrorInquiryBalance)
                    }
                } else {
                    isoDep.close()
                    activity?.let {
                        if (::brizziInstance.isInitialized) {
                            brizziInstance.getBalanceInquiry(it.intent, object : Callback {
                                override fun OnFailure(brizziException: BrizziException?) {
                                    //TODO log data failed
                                    Toast.makeText(activity, brizziException?.message, Toast.LENGTH_SHORT).show()
                                }

                                override fun OnSuccess(brizziCardObject: BrizziCardObject) {
                                    //TODO log data success
                                    Toast.makeText(activity, brizziCardObject.balance, Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            showError(resources.getString(R.string.emoney_card_isnot_supported))
                        }
//                    emoneyAnalytics.onErrorReadingCard()
//                    Toast.makeText(activity, "OTHER CODE", Toast.LENGTH_SHORT).show()
//                    showError(resources.getString(R.string.emoney_card_isnot_supported))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showError(resources.getString(R.string.emoney_failed_read_card))
        }

    }

    private fun onSuccessInquiryBalance(mapAttributes: HashMap<String, Any>,
                                        rechargeEmoneyInquiry: RechargeEmoneyInquiry) {
        rechargeEmoneyInquiry.attributesEmoneyInquiry?.let { attributes ->
            when {
                attributes.status == 0 -> {
                    sendCommand(rechargeEmoneyInquiry, mapAttributes)
                }
                attributes.status == 1 -> {
                    showCardLastBalance(rechargeEmoneyInquiry)
                }
                attributes.status == 2 -> {
                    rechargeEmoneyInquiry.error?.let { error ->
                        showError(error.title)
                    }
                }
                else -> {
                    return
                }
            }
        }
    }

    private fun sendCommand(rechargeEmoneyInquiry: RechargeEmoneyInquiry, mapAttributes: HashMap<String, Any>) {
        if (isoDep != null && isoDep.isConnected) {
            try {
                val responseInByte = isoDep.transceive(NFCUtils.hexStringToByteArray(
                        rechargeEmoneyInquiry.attributesEmoneyInquiry?.payload))

                run {
                    if (responseInByte != null) {
                        // to get card payload
                        val response = NFCUtils.toHex(responseInByte)
                        activity?.let {
                            mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_PAYLOAD] = response
                            emoneyInquiryBalanceViewModel.getEmoneyInquiryBalance(
                                    EmoneyInquiryBalanceViewModel.PARAM_SEND_COMMAND,
                                    GraphqlHelper.loadRawString(it.resources, R.raw.query_emoney_inquiry_balance),
                                    rechargeEmoneyInquiry.id.toInt(),
                                    mapAttributes,
                                    this::onSuccessInquiryBalance,
                                    this::onErrorInquiryBalance)
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                showError(resources.getString(R.string.emoney_update_balance_failed))
            }
        } else {
            showError(resources.getString(R.string.emoney_update_balance_failed))
        }
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

    private fun showCardLastBalance(rechargeEmoneyInquiry: RechargeEmoneyInquiry) {
        emoneyAnalytics.onShowLastBalance()
        tapETollCardView.visibility = View.GONE
        eTollUpdateBalanceResultView.visibility = View.VISIBLE
        eTollUpdateBalanceResultView.showCardInfoFromApi(rechargeEmoneyInquiry)
        rechargeEmoneyInquiry.error?.let {
            NetworkErrorHelper.showGreenCloseSnackbar(activity, it.title)
        }
    }

    private fun isDigitalSmartcardEnabled(): Boolean {
        return remoteConfig.getBoolean(DIGITAL_SMARTCARD, false)
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

    override fun onResume() {
        super.onResume()
        activity?.let {
            if (::nfcAdapter.isInitialized) {
                if (!::permissionCheckerHelper.isInitialized) {
                    permissionCheckerHelper = PermissionCheckerHelper()
                }
                permissionCheckerHelper.checkPermission(this,
                        PermissionCheckerHelper.Companion.PERMISSION_NFC,
                        object : PermissionCheckerHelper.PermissionCheckListener {

                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper.onPermissionDenied(it.applicationContext, permissionText)
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper.onNeverAskAgain(it.applicationContext, permissionText)
                            }

                            override fun onPermissionGranted() {
                                detectNFC()
                            }
                        }, getString(R.string.emoney_nfc_permission_rationale_message))
            } else {
                // show webview help page
                val intent = RouteManager.getIntent(activity, ApplinkConst.CONTACT_US_NATIVE)
                startActivity(intent)
                it.finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.let {
            if (::permissionCheckerHelper.isInitialized) {
                permissionCheckerHelper.onRequestPermissionsResult(it.applicationContext, requestCode, permissions, grantResults)
            }
        }
    }

    internal fun detectNFC() {
        if (!nfcAdapter.isEnabled) {
            eTollUpdateBalanceResultView.visibility = View.GONE
            tapETollCardView.visibility = View.GONE

            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.emoney_please_activate_nfc_from_settings))
                    .setPositiveButton(getString(R.string.emoney_activate), object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            emoneyAnalytics.onActivateNFCFromSetting()
                            directToNFCSettingsPage()
                        }
                    })
                    .setNegativeButton(getString(R.string.emoney_cancel), object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            emoneyAnalytics.onCancelActivateNFCFromSetting()
                            nfcDisabledView.visibility = View.VISIBLE
                        }
                    }).show()
        } else {
            intentFiltersArray?.let { intentFiltersArray ->
                nfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFiltersArray, techListsArray)
                nfcDisabledView.visibility = View.GONE

                if (eTollUpdateBalanceResultView.visibility == View.GONE) {
                    emoneyAnalytics.onEnableNFC()
                    tapETollCardView.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        private const val DIGITAL_SMARTCARD = "mainapp_digital_smartcard"
        private const val TRANSCEIVE_TIMEOUT_IN_SEC = 5000

        private const val COMMAND_SELECT_EMONEY = "00A40400080000000000000001"
        private const val COMMAND_CARD_ATTRIBUTE = "00F210000B"
        private const val COMMAND_CARD_INFO = "00B300003F"
        private const val COMMAND_LAST_BALANCE = "00B500000A"
        private const val COMMAND_SUCCESSFULLY_EXECUTED = "9000"
        private const val ISSUER_ID_EMONEY = 1

        fun newInstance(): EmoneyCheckBalanceNFCFragment {
            return EmoneyCheckBalanceNFCFragment()
        }
    }
}