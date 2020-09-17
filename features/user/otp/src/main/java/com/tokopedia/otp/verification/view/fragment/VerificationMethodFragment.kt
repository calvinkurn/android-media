package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
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
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant
import com.tokopedia.otp.common.analytics.TrackingValidatorUtil
import com.tokopedia.otp.verification.common.IOnBackPressed
import com.tokopedia.otp.verification.common.di.VerificationComponent
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter
import com.tokopedia.otp.verification.view.viewbinding.VerificationMethodViewBinding
import com.tokopedia.otp.verification.viewmodel.VerificationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 02/06/20.
 */

class VerificationMethodFragment : BaseVerificationFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingValidatorUtil

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var otpData: OtpData
    private lateinit var adapter: VerificationMethodAdapter

    private val viewmodel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(VerificationViewModel::class.java)
    }

    override var viewBound = VerificationMethodViewBinding()

    override fun getScreenName(): String = TrackingValidatorConstant.Screen.SCREEN_VERIFICATION_METHOD

    override fun initInjector() = getComponent(VerificationComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
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
        (activity as VerificationActivity).title = "Verifikasi"

        adapter = VerificationMethodAdapter.createInstance(object : VerificationMethodAdapter.ClickListener {
            override fun onModeListClick(modeList: ModeListData, position: Int) {
                analytics.trackClickMethodOtpButton(otpData.otpType, modeList.modeText)

                if (modeList.modeText == OtpConstant.OtpMode.MISCALL && otpData.otpType == OtpConstant.OtpType.REGISTER_PHONE_NUMBER) {
                    (activity as VerificationActivity).goToOnboardingMiscallPage(modeList)
                } else {
                    (activity as VerificationActivity).goToVerificationPage(modeList)
                }
            }
        })
        viewBound.methodList?.adapter = adapter
        viewBound.methodList?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun getVerificationMethod() {
        showLoading()
        viewmodel.getVerificationMethod(
                otpType = otpData.otpType.toString(),
                userId = otpData.userId,
                msisdn = otpData.msisdn,
                email = otpData.email,
                userIdEnc = otpData.userIdEnc,
                accessToken = otpData.accessToken
        )
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
        when (otpModeListData.linkType) {
            TYPE_HIDE_LINK -> onTypeHideLink()
            TYPE_CHANGE_PHONE_UPLOAD_KTP -> onChangePhoneUploadKtpType()
            TYPE_PROFILE_SETTING -> onProfileSettingType()
            else -> onTypeHideLink()
        }
    }

    private fun skipView(modeListData: ModeListData) {
        if (modeListData.modeText == OtpConstant.OtpMode.MISCALL && otpData.otpType == OtpConstant.OtpType.REGISTER_PHONE_NUMBER) {
            (activity as VerificationActivity).goToOnboardingMiscallPage(modeListData)
        } else {
            (activity as VerificationActivity).goToVerificationPage(modeListData)
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

    private fun onTypeHideLink() {
        viewBound.phoneInactive?.hide()
    }

    private fun onChangePhoneUploadKtpType() {
        viewBound.phoneInactive?.visible()
        viewBound.phoneInactive?.text = getString(R.string.my_phone_number_is_inactive)
        viewBound.phoneInactive?.setOnClickListener {
            context?.let {
                analytics.trackClickInactivePhoneNumber(otpData.otpType.toString())
                val intent = RouteManager.getIntent(it,
                        ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE_FORM)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_CIPF_USER_ID,
                        userSession.temporaryUserId)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_CIPF_OLD_PHONE,
                        userSession.tempPhoneNumber)
                startActivity(intent)
            }
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
                        ds.color = MethodChecker.getColor(context, R.color.Green_G500)
                    }
                },
                message.indexOf(getString(R.string.setting)),
                message.indexOf(getString(R.string.setting)) + getString(R.string.setting).length,
                0)
        viewBound.phoneInactive?.visible()
        context?.let { ContextCompat.getColor(it, R.color.Neutral_N700_68) }?.let {
            viewBound.phoneInactive?.setTextColor(it)
        }
        viewBound.phoneInactive?.movementMethod = LinkMovementMethod.getInstance()
        viewBound.phoneInactive?.setText(spannable, TextView.BufferType.SPANNABLE)

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