package com.tokopedia.otp.verification.view.fragment

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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter
import com.tokopedia.otp.verification.view.viewbinding.VerificationMethodViewBinding
import com.tokopedia.otp.verification.viewmodel.VerificationViewModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 02/06/20.
 */

class VerificationMethodFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingOtpUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var otpData: OtpData
    private lateinit var adapter: VerificationMethodAdapter

    private var isMoreThanOneMethod: Boolean = true

    private val viewmodel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(VerificationViewModel::class.java)
    }

    private val abTestPlatform by lazy {
        RemoteConfigInstance.getInstance().abTestPlatform
    }

    override var viewBound = VerificationMethodViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = TrackingOtpConstant.Screen.SCREEN_VERIFICATION_METHOD

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

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
            viewBound.parentContainerView?.setBackgroundColor(ContextCompat.getColor(it, R.color.Unify_N0))
        }
    }

    private fun setMethodListAdapter() {
        adapter = VerificationMethodAdapter.createInstance(object : VerificationMethodAdapter.ClickListener {
            override fun onModeListClick(modeList: ModeListData, position: Int) {
                analytics.trackClickMethodOtpButton(otpData.otpType, modeList.modeText)
                viewmodel.done = true
                if (modeList.modeText == OtpConstant.OtpMode.MISCALL) {
                    (activity as VerificationActivity).goToOnboardingMiscallPage(modeList)
                } else {
                    (activity as VerificationActivity).goToVerificationPage(modeList, isMoreThanOneMethod)
                }
            }
        })
        viewBound.methodList?.adapter = adapter
        viewBound.methodList?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun getVerificationMethod() {
        showLoading()
        val otpType = otpData.otpType.toString()
        if ((otpType == OtpConstant.OtpType.AFTER_LOGIN_PHONE.toString() || otpType == OtpConstant.OtpType.RESET_PIN.toString())
                && otpData.userIdEnc.isNotEmpty()) {
            viewmodel.getVerificationMethod2FA(otpType, otpData.accessToken, otpData.userIdEnc)
        } else {
            viewmodel.getVerificationMethod(
                    otpType = otpType,
                    userId = otpData.userId,
                    msisdn = otpData.msisdn,
                    email = otpData.email
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

            } else if (otpModeListData.errorMessage.isEmpty()) {
                onFailedGetVerificationMethod().invoke(MessageErrorException(otpModeListData.errorMessage))
            } else {
                onFailedGetVerificationMethod().invoke(Throwable())
            }
        }
    }

    private fun showListView(otpModeListData: OtpModeListData) {
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

    private fun setFooter(linkType: Int) {
        when (linkType) {
            TYPE_CHANGE_PHONE_UPLOAD_KTP -> setAbTestFooter()
            TYPE_PROFILE_SETTING -> onProfileSettingType()
            else -> onTypeHideLink()
        }

    }

    private fun setAbTestFooter() {
        val abTestKeyInactivePhone1 = abTestPlatform.getString(AB_TEST_KEY_INACTIVE_PHONE_1, "")

        if (abTestKeyInactivePhone1 == AB_TEST_KEY_INACTIVE_PHONE_1) {
            onVariant1InactivePhone()
        } else {
            onInactivePhoneNumber(getString(R.string.my_phone_number_is_inactive))
        }
    }

    private fun onVariant1InactivePhone() {
        onInactivePhoneNumber(getString(R.string.cellphone_number_has_changed))
    }

    private fun onVariant2InactivePhone() {
        viewBound.phoneInactiveTicker?.visible()
        viewBound.phoneInactiveTicker?.setHtmlDescription(String.format(getString(R.string.change_inactive_phone_number_html), ""))
        viewBound.phoneInactiveTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                onGoToInactivePhoneNumber()
            }

            override fun onDismiss() {}
        })
    }

    private fun onVariant3InactivePhone() {
        val message = getString(R.string.change_inactive_phone_number)
        val clickableMessage = getString(R.string.change_phone_number)
        val spannable = SpannableString(message)
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        onGoToInactivePhoneNumber()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
                    }
                },
                message.indexOf(clickableMessage),
                message.indexOf(clickableMessage) + clickableMessage.length,
                0)
        viewBound.phoneInactive?.visible()
        context?.let { ContextCompat.getColor(it, R.color.Unify_N700_68) }?.let {
            viewBound.phoneInactive?.setTextColor(it)
        }
        viewBound.phoneInactive?.movementMethod = LinkMovementMethod.getInstance()
        viewBound.phoneInactive?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun onTypeHideLink() {
        viewBound.phoneInactive?.hide()
    }

    private fun onInactivePhoneNumber(text: String) {
        viewBound.phoneInactive?.visible()
        viewBound.phoneInactive?.text = text
        viewBound.phoneInactive?.setOnClickListener {
            onGoToInactivePhoneNumber()
        }
    }

    private fun onGoToInactivePhoneNumber() {
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
            startActivity(intent)
        }
    }

    private fun onProfileSettingType() {
        val message = getString(R.string.my_phone_inactive_change_at_setting)
        val spannable = SpannableString(message)
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        activity?.let {
                            val intent = RouteManager.getIntent(it, ApplinkConst.SETTING_PROFILE)
                            startActivity(intent)
                            it.finish()
                        }
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
                    }
                },
                message.indexOf(getString(R.string.setting)),
                message.indexOf(getString(R.string.setting)) + getString(R.string.setting).length,
                0)
        viewBound.phoneInactive?.visible()
        context?.let { ContextCompat.getColor(it, R.color.Unify_N700_68) }?.let {
            viewBound.phoneInactive?.setTextColor(it)
        }
        viewBound.phoneInactive?.movementMethod = LinkMovementMethod.getInstance()
        viewBound.phoneInactive?.setText(spannable, TextView.BufferType.SPANNABLE)

    }

    private fun loadTickerTrouble(otpModeListData: OtpModeListData) {
        if (otpModeListData.enableTicker) {
            viewBound.ticker?.show()
            viewBound.ticker?.setHtmlDescription(otpModeListData.tickerTrouble)
        } else {
            viewBound.ticker?.hide()
        }
    }

    private fun showLoading() {
        viewBound.loader?.show()
        viewBound.containerView?.hide()
    }

    private fun hideLoading() {
        viewBound.loader?.hide()
        viewBound.containerView?.show()
    }

    companion object {

        private const val TITLE = "Verifikasi"
        private const val AB_TEST_KEY_INACTIVE_PHONE_1 = "inactive_pn_11"

        private const val TYPE_HIDE_LINK = 0
        private const val TYPE_CHANGE_PHONE_UPLOAD_KTP = 1
        private const val TYPE_PROFILE_SETTING = 2

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = VerificationMethodFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}