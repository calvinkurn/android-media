package com.tokopedia.loginregister.login_sdk

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.FragmentConsentLayoutBinding
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.loginregister.login_sdk.LoginSdkAnalytics.LABEL_FAILED
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.CLIENT_ID
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.CODE_CHALLENGE
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.PACKAGE_NAME
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.REDIRECT_URI
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.SCOPES
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.SIGN_CERT
import com.tokopedia.loginregister.login_sdk.data.SdkConsentData
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.sessioncommon.util.LoginSdkUtils.ERR_CODE_API
import com.tokopedia.sessioncommon.util.LoginSdkUtils.ERR_CODE_UNKNOWN
import com.tokopedia.sessioncommon.util.LoginSdkUtils.getClientName
import com.tokopedia.sessioncommon.util.LoginSdkUtils.redirectToTargetUri
import com.tokopedia.sessioncommon.util.LoginSdkUtils.setAsLoginSdkFlow
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject
import com.tokopedia.loginregister.R as loginregisterR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class LoginSdkConsentFragment: BaseDaggerFragment() {

    var viewBinding by autoClearedNullable<FragmentConsentLayoutBinding>()

    private var redirectUrl = ""
    private var callerCert = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginEmailPhoneViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        redirectUrl = arguments?.getString(REDIRECT_URI) ?: ""
        callerCert = arguments?.getString(SIGN_CERT) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentConsentLayoutBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun initInjector() {
        getComponent(LoginComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(shouldShow = true)

        viewBinding?.btnLoginConsent?.setOnClickListener {
            viewBinding?.btnLoginConsent?.isLoading = true
            LoginSdkAnalytics.sendClickOnButtonLanjutDanIzinkanEvent(LoginSdkAnalytics.LABEL_CLICK)
            viewModel.authorizeSdk(
                clientId = arguments?.getString(CLIENT_ID) ?: "",
                redirectUri = redirectUrl,
                codeChallenge = arguments?.getString(CODE_CHALLENGE) ?: ""
            )
        }

        setupObserver()

        viewModel.validateClient(
            clientId = arguments?.getString(CLIENT_ID) ?: "",
            signature = callerCert,
            packageName = arguments?.getString(PACKAGE_NAME) ?: "",
            redirectUri = redirectUrl
        )
        setupToolbar()
    }

    fun setupToolbar() {
        activity?.findViewById<HeaderUnify>(R.id.unifytoolbar)?.apply {
            headerTitle = getString(loginregisterR.string.login_sdk_toolbar_login_title)
            actionTextView?.hide()
            isShowShadow = false
        }
    }

    private fun showLoading(shouldShow: Boolean) {
        viewBinding?.apply {
            pageLoader.showWithCondition(shouldShow)
            containerView.visibleWithCondition(!shouldShow)
        }
    }

    private fun setupObserver() {
        viewModel.sdkConsent.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess) {
                        if (it.data.showConsent) {
                            setUI(it.data)
                        } else {
                            LoginSdkAnalytics.sendClickOnButtonLanjutDanIzinkanEvent(LoginSdkAnalytics.LABEL_CLICK)
                            viewModel.authorizeSdk(
                                clientId = arguments?.getString(CLIENT_ID) ?: "",
                                redirectUri = redirectUrl,
                                codeChallenge = arguments?.getString(CODE_CHALLENGE) ?: ""
                            )
                        }
                    } else {
                        redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.data.error, errorCode = ERR_CODE_API)
                    }
                }
                is Fail -> {
                    // no op
                }
            }
        }

        viewModel.authorizeResponse.observe(viewLifecycleOwner) {
            viewBinding?.btnLoginConsent?.isLoading = false
            when (it) {
                is Success -> {
                    LoginSdkAnalytics.sendClickOnButtonLanjutDanIzinkanEvent(LoginSdkAnalytics.LABEL_SUCCESS)
                    redirectToTargetUri(requireActivity(), it.data.redirectUri, it.data.code)
                }
                is Fail -> {
                    LoginSdkAnalytics.sendClickOnButtonLanjutDanIzinkanEvent("$LABEL_FAILED - ${it.throwable.message}")
                    redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.throwable.message ?: ERR_CODE_UNKNOWN, errorCode = ERR_CODE_API)
                }
            }
        }

        viewModel.validateClient.observe(viewLifecycleOwner) {
            when (it ) {
                is Success -> {
                    if (it.data.status) {
                        requireContext().setAsLoginSdkFlow(it.data.appName)
                        viewModel.getConsent(
                            clientId = arguments?.getString(CLIENT_ID) ?: "",
                            scopes = arguments?.getString(SCOPES) ?: ""
                        )
                        LoginSdkAnalytics.sendViewTokopediaSsoPageEvent(it.data.appName)
                    } else {
                        redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.data.error, errorCode = ERR_CODE_API)
                    }
                }
                is Fail -> {
                    redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.throwable.message ?: ERR_CODE_UNKNOWN, errorCode = ERR_CODE_API)
                }
            }
        }
    }

    fun TextView.clickableText(
        context: Context,
        spanText: List<String>,
        color: Int,
        onSpanTextClicked: (String) -> Unit
    ) {
        val spannable = SpannableString(text)
        spanText.forEach {
            spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        onSpanTextClicked(it)
                    }
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = getColor(context, color)
                        ds.setTypeface(Typeface.DEFAULT_BOLD)
                    }
                },
                text.indexOf(it),
                text.indexOf(it) + it.length,
                0
            )
        }
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
        setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun setUI(consentData: SdkConsentData) {
        showLoading(shouldShow = false)
        viewBinding?.run {
            txtHeader.text = String.format(getString(loginregisterR.string.login_sdk_header_text), consentData.clientInfo.appName)
            consentData.consents.forEach {
                txtDataList.append(" •  $it\n")
            }
            // Remove last newline from text
            txtDataList.text = txtDataList.text.substring(0, txtDataList.text.length - 1)

            txtConsentInfo.text = String.format(getString(loginregisterR.string.login_sdk_consent_text), consentData.termPrivacy.purpose)
            txtFullName.text = consentData.userInfo.fullName
            txtPhoneNumber.text = consentData.userInfo.email.ifEmpty { consentData.userInfo.phone }
            imgProfile.loadImageCircle(consentData.userInfo.profilePicture)

            imgPartnerLogo.loadImage(consentData.clientInfo.imageUrl)

            txtTncPrivPartner.text = String.format(getString(loginregisterR.string.login_sdk_terms_privacy_txt), consentData.clientInfo.appName)

            txtTncPrivPartner.clickableText(
                requireContext(),
                listOf(getString(R.string.phone_shop_tnc_title), getString(R.string.login_sdk_privacy_txt)),
                unifyprinciplesR.color.Unify_GN500
            ) {
                if (it == getString(loginregisterR.string.phone_shop_tnc_title)) {
                    RouteManager.route(activity, String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, consentData.termPrivacy.url))
                    LoginSdkAnalytics.sendClickOnButtonSyaratDanKetentuanEvent(requireContext().getClientName())
                }
                if (it == getString(loginregisterR.string.login_sdk_privacy_txt)) {
                    RouteManager.route(activity, String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, consentData.termPrivacy.privacyUrl))
                    LoginSdkAnalytics.sendClickOnButtonKebijakanPrivasiEvent(requireContext().getClientName())
                }
            }
        }
    }

    override fun getScreenName(): String = ""

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginSdkConsentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
