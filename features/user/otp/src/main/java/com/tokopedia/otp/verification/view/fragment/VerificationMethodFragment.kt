package com.tokopedia.otp.verification.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.silentverification.view.dialog.SilentVerificationDialogUtils
import com.tokopedia.otp.silentverification.view.fragment.SilentVerificationFragment.Companion.RESULT_DELETE_METHOD
import com.tokopedia.otp.verification.common.VerificationPref
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpConstant.OtpMode.SILENT_VERIFICATION
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.activity.VerificationActivity.Companion.REQUEST_SILENT_VERIF
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter
import com.tokopedia.otp.verification.view.viewbinding.VerificationMethodViewBinding
import com.tokopedia.otp.verification.viewmodel.VerificationViewModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.sessioncommon.util.AuthenticityUtils
import com.tokopedia.sessioncommon.util.ConnectivityUtils
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as RUnify

/**
 * Created by Ade Fulki on 02/06/20.
 */

open class VerificationMethodFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingOtpUtil

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var verificationPref: VerificationPref

    protected lateinit var otpData: OtpData
    private lateinit var adapter: VerificationMethodAdapter

    protected var isMoreThanOneMethod: Boolean = true
    private var clear: Boolean = false
    private var done = false
    private var isLoginRegisterFlow = false

    protected val viewmodel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(VerificationViewModel::class.java)
    }

    override var viewBound = VerificationMethodViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = TrackingOtpConstant.Screen.SCREEN_VERIFICATION_METHOD

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    fun createBundle(modeListData: ModeListData? = null, isMoreThanOne: Boolean = true): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, isLoginRegisterFlow)
        modeListData?.let {
            bundle.putParcelable(OtpConstant.OTP_MODE_EXTRA, it)
        }
        bundle.putBoolean(OtpConstant.IS_MORE_THAN_ONE_EXTRA, isMoreThanOne)
        return bundle
    }

    open fun goToSilentVerificationpage(modeListData: ModeListData) {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.SILENT_VERIFICAITON)
        val bundle = createBundle(modeListData)
        bundle.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_SILENT_VERIF)
    }

    private fun isEnableSilentVerif(): Boolean {
        return remoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_SILENT_VERIFICATION, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
        viewmodel.isLoginRegisterFlow = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW)?: false
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initView()
        getVerificationMethod()
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
    }

    override fun onDestroy() {
        viewmodel.getVerificationMethodResult.removeObservers(this)
        viewmodel.flush()
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean = true

    private fun initView() {
        setTitle()
        setBackground()
        setMethodListAdapter()
    }

    private fun setTitle() {
        (activity as VerificationActivity).title = TITLE
    }

    private fun setBackground() {
        context?.let {
            viewBound.parentContainerView?.setBackgroundColor(ContextCompat.getColor(it, RUnify.color.Unify_Background))
        }
    }

    private fun gotoSilentVerificationPage(modeListData: ModeListData) {
        if(activity != null && isEnableSilentVerif()) {
            goToSilentVerificationpage(modeListData)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_SILENT_VERIF -> {
                if(resultCode == Activity.RESULT_OK) {
                    activity?.setResult(Activity.RESULT_OK, data)
                    activity?.finish()
                } else if(resultCode == RESULT_DELETE_METHOD) {
                    val list = adapter.listData.filter { it.modeText != SILENT_VERIFICATION }.toMutableList()
                    adapter.setList(list)
                }
            }
            INACTIVE_PHONE_CODE -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    val message = data?.getStringExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY).orEmpty()
                    if (message.isNotEmpty()) {
                        view?.let {
                            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun onSilentVerificationClicked(modeListData: ModeListData) {
        if (ConnectivityUtils.isSilentVerificationPossible(activity)){
            gotoSilentVerificationPage(modeListData)
        } else {
            activity?.run {
                SilentVerificationDialogUtils.showCellularDataDialog(
                    this,
                    onPrimaryButtonClicked = {
                        gotoSilentVerificationPage(modeListData)
                        analytics.trackCellularDialogButton(TrackingOtpConstant.Label.LABEL_MENGERTI)
                    },
                    onSecondaryButtonClicked = {
                        analytics.trackCellularDialogButton(TrackingOtpConstant.Label.LABEL_BATAL)
                    })
            }
        }
    }

    private fun isSameIdentifier(): Boolean {
        val identifier = otpData.email.ifEmpty { otpData.msisdn }
        return identifier.isNotEmpty() && verificationPref.userIdentifier == identifier
    }

    open fun setMethodListAdapter() {
        adapter = VerificationMethodAdapter.createInstance(object : VerificationMethodAdapter.ClickListener {
            override fun onModeListClick(modeList: ModeListData, position: Int) {
                if(!isSameIdentifier()) {
                    verificationPref.resetByMode(modeList.modeText)
                }
                viewmodel.done = true
                analytics.trackClickMethodOtpButton(otpData.otpType, modeList.modeText)
                try {
                    when (modeList.modeText) {
                        OtpConstant.OtpMode.MISCALL -> {
                            (activity as VerificationActivity).goToOnboardingMiscallPage(modeList)
                        }
                        SILENT_VERIFICATION -> {
                            // Goto silent verification page
                            onSilentVerificationClicked(modeList)
                        }
                        else -> {
                            (activity as VerificationActivity).goToVerificationPage(
                                modeList,
                                isMoreThanOneMethod
                            )
                        }
                    }
                } catch (e: Exception) { }
            }
        })
        viewBound.methodList?.adapter = adapter
        viewBound.methodList?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    open fun getVerificationMethod() {
        showLoading()
        val otpType = otpData.otpType.toString()
        if ((otpType == OtpConstant.OtpType.AFTER_LOGIN_PHONE.toString() || otpType == OtpConstant.OtpType.RESET_PIN.toString())
                && otpData.userIdEnc.isNotEmpty()) {
            viewmodel.getVerificationMethod2FA(
                    otpType = otpType,
                    validateToken = otpData.accessToken,
                    userIdEnc = otpData.userIdEnc)
        } else {
            val timeUnix = System.currentTimeMillis().toString()
            viewmodel.getVerificationMethod(
                    otpType = otpType,
                    userId = otpData.userId,
                    msisdn = otpData.msisdn,
                    email = otpData.email,
                    authenticity = AuthenticityUtils.generateAuthenticity(otpData.msisdn, timeUnix, requireContext()),
                    timeUnix = timeUnix
            )
        }
    }

    private fun initObserver() {
        viewmodel.getVerificationMethodResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetVerificationMethod().invoke(it.data)
                is Fail -> onFailedGetVerificationMethod().invoke(it.throwable)
            }
        })
    }

    private fun onSuccessGetVerificationMethod(): (OtpModeListData) -> Unit {
        return { otpModeListData ->
            isMoreThanOneMethod = otpModeListData.modeList.size > 1
            if (otpModeListData.success && otpModeListData.modeList.isNotEmpty()) {
                hideLoading()

                if (!otpData.isShowChooseMethod) {
                    val modeList = otpModeListData.modeList.singleOrNull {
                        it.modeText == otpData.otpMode
                    }

                    if (modeList != null) {
                        skipView(modeList)
                    } else {
                        showListView(otpModeListData)
                    }
                } else {
                    showListView(otpModeListData)
                }

                if(otpModeListData.defaultMode == DEFAULT_MODE_SILENT_VERIF) {
                    val silentVerifModeData = otpModeListData.modeList.find { it.modeCode == DEFAULT_MODE_SILENT_VERIF }
                    if(silentVerifModeData != null) {
                        gotoSilentVerificationPage(silentVerifModeData)
                    }
                }
            } else if (otpModeListData.errorMessage.isEmpty()) {
                onFailedGetVerificationMethod().invoke(MessageErrorException(otpModeListData.errorMessage))
            } else {
                onFailedGetVerificationMethod().invoke(Throwable())
            }
        }
    }

    private fun showListView(otpModeListData: OtpModeListData) {
        if(context != null) {
            if(!ConnectivityUtils.isSilentVerificationPossible(requireContext()) || !isEnableSilentVerif() || GlobalConfig.isSellerApp()) {
                otpModeListData.modeList.removeAll { it.modeText == SILENT_VERIFICATION }
            }
        }
        adapter.setList(otpModeListData.modeList)
        loadTickerTrouble(otpModeListData)
        setFooter(otpModeListData.linkType)
    }

    private fun skipView(modeListData: ModeListData) {
        viewmodel.done = true
        if (modeListData.modeText == OtpConstant.OtpMode.MISCALL && otpData.otpType == OtpConstant.OtpType.REGISTER_PHONE_NUMBER) {
            (activity as VerificationActivity).goToOnboardingMiscallPage(modeListData)
        } else {
            (activity as VerificationActivity).goToVerificationPage(modeListData, isMoreThanOneMethod)
        }
    }

    private fun onFailedGetVerificationMethod(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
            hideLoading()
            val message = ErrorHandler.getErrorMessage(context, throwable)
            NetworkErrorHelper.showEmptyState(context, viewBound.containerView, message) {
                getVerificationMethod()
            }
        }
    }

    open fun setFooter(linkType: Int) {
        when (linkType) {
            TYPE_CHANGE_PHONE_UPLOAD_KTP -> onDefaultFooterType()
            TYPE_PROFILE_SETTING -> onProfileSettingFooterType()
            else -> onTypeHideLink()
        }
    }

    private fun inactivePhoneFooterWithTicker() {
        viewBound.phoneInactive?.hide()
        viewBound.phoneInactiveTicker?.visible()
        viewBound.phoneInactiveTicker?.setHtmlDescription(String.format(getString(R.string.change_inactive_phone_number_html), ""))
        viewBound.phoneInactiveTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                onGoToInactivePhoneNumber()
            }

            override fun onDismiss() {}
        })
    }

    private fun inactivePhoneFooter(message: String, clickableMessage: String, onClick: () -> Unit) {
        context?.let {
            val spannable = SpannableString(message).apply {
                setSpan(clickableSpan { onClick.invoke() },
                    message.indexOf(clickableMessage),
                    message.indexOf(clickableMessage) + clickableMessage.length,
                    0
                )
            }
            viewBound.phoneInactive?.setTextColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            viewBound.phoneInactive?.setText(spannable, TextView.BufferType.SPANNABLE)
            viewBound.phoneInactive?.movementMethod = LinkMovementMethod.getInstance()
            viewBound.phoneInactive?.visible()
        }
    }

    private fun onTypeHideLink() {
        viewBound.phoneInactive?.hide()
    }

    open fun onGoToInactivePhoneNumber() {
        context?.let {
            analytics.trackClickInactivePhoneNumber(otpData.otpType.toString())
            analytics.trackClickInactivePhoneLink()
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE)
            if (otpData.email.isEmpty() && otpData.msisdn.isEmpty()) {
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, userSession.tempPhoneNumber)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.tempEmail)
            } else {
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, otpData.msisdn)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, otpData.email)
            }
            startActivityForResult(intent, INACTIVE_PHONE_CODE)
        }
    }

    private fun onDefaultFooterType() {
        val message = getString(R.string.change_inactive_phone_number).orEmpty()
        val clickableMessage = getString(R.string.otp_change_phone_number).orEmpty()
        inactivePhoneFooter(message, clickableMessage) {
            onGoToInactivePhoneNumber()
        }
    }

    private fun onProfileSettingFooterType() {
        val message = getString(R.string.my_phone_inactive_change_at_setting).orEmpty()
        val clickableMessage = getString(R.string.setting).orEmpty()
        inactivePhoneFooter(message, clickableMessage) {
            gotoSettingProfile()
        }
    }

    private fun gotoSettingProfile() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.SETTING_PROFILE)
            startActivity(intent)
            it.finish()
        }
    }

    private fun loadTickerTrouble(otpModeListData: OtpModeListData) {
        if (otpModeListData.enableTicker) {
            viewBound.ticker?.show()
            viewBound.ticker?.setHtmlDescription(otpModeListData.tickerTrouble)
        } else {
            viewBound.ticker?.hide()
        }
    }

    private fun clickableSpan(callback: () -> Unit): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                callback.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                context?.let {
                    ds.isUnderlineText = false
                    ds.color = MethodChecker.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_G400
                    )
                }
            }
        }
    }

    open fun showLoading() {
        viewBound.loader?.show()
        viewBound.containerView?.hide()
    }

    open fun hideLoading() {
        viewBound.loader?.hide()
        viewBound.containerView?.show()
    }

    companion object {

        private const val TITLE = "Verifikasi"

        private const val TYPE_HIDE_LINK = 0
        private const val TYPE_CHANGE_PHONE_UPLOAD_KTP = 1
        private const val TYPE_PROFILE_SETTING = 2

        private const val DEFAULT_MODE_SILENT_VERIF = 16
        private const val INACTIVE_PHONE_CODE = 1000

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = VerificationMethodFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}