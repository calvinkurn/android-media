package com.tokopedia.loginregister.login_sdk

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.loginregister.databinding.FragmentConsentLayoutBinding
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.loginregister.login_sdk.data.SdkConsentData
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

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
        redirectUrl = arguments?.getString("redirect_uri") ?: ""
        callerCert = arguments?.getString("sign_cert") ?: ""
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

        viewBinding?.btnLoginConsent?.setOnClickListener {
            viewModel.authorizeSdk(
                clientId = arguments?.getString("client_id") ?: "",
                redirectUri = redirectUrl,
                state = "",
                codeChallenge = ""
            )
        }

        setupObserver()

        viewModel.validateClient(callerCert)
    }

    private fun setupObserver() {
        viewModel.sdkConsent.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess) {
                        if (it.data.showConsent) {
                            setUI(it.data)
                        } else {
                            viewModel.authorizeSdk(
                                clientId = arguments?.getString("client_id") ?: "",
                                redirectUri = redirectUrl,
                                state = "",
                                codeChallenge = ""
                            )
                        }
                    } else {
                        redirectToTargetUri(redirectUrl, authCode = "", it.data.error)
                    }
                }
                is Fail -> {
                    // no op
                }
            }
        }

        viewModel.authorizeResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    redirectToTargetUri(it.data.redirectUri, it.data.code)
                }
                is Fail -> {
                    redirectToTargetUri(redirectUrl, authCode = "", it.throwable.message ?: "Error")
                }
            }
        }

        viewModel.validateClient.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getConsent(
                    clientId = arguments?.getString("client_id") ?: "",
                    scopes = arguments?.getString("scopes") ?: ""
                )
            } else {
                redirectToTargetUri(redirectUrl, authCode = "", "Invalid Client Certificate")
            }
        }
    }

    fun getColor(context: Context, id: Int): Int {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.getColor(context, id)
            } else {
                context.resources.getColor(id)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            0
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
                        ds.isUnderlineText = true
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
        viewBinding?.run {

            txtHeader.text = "${consentData.clientInfo.appName} izin akses data dari akun Tokopedia berikut"

            consentData.consents.forEach {
                txtDataList.append(" •  $it\n")
            }

            // Remove last newline from text
            txtDataList.text = txtDataList.text.substring(0, txtDataList.text.length - 1)

            txtConsentInfo.text = "Dengan klik tombol Izinkan, kamu menyetujui pembagian data akun di atas ke ${consentData.clientInfo.appName}"
            txtFullName.text = consentData.userInfo.fullName
            txtPhoneNumber.text = consentData.userInfo.email.ifEmpty { consentData.userInfo.phone }
            imgProfile.loadImageCircle(consentData.userInfo.profilePicture)

            imgPartnerLogo.loadImage(consentData.clientInfo.imageUrl)

            txtTncPrivPartner.text = "Syarat dan ketentuan serta Kebijakan Privasi ${consentData.clientInfo.appName}"
            txtTncPrivTkpd.clickableText(
                requireContext(),
                listOf("Syarat dan ketentuan", "Kebijakan Privasi"),
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            ) {

            }

            txtTncPrivPartner.clickableText(
                requireContext(),
                listOf("Syarat dan ketentuan", "Kebijakan Privasi"),
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            ) {

            }
        }
    }

    private fun redirectToTargetUri(redirectUrl: String, authCode: String, error: String = "") {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
        if (authCode.isNotEmpty()) {
            intent.putExtra("auth_code", authCode)
        }
        if (error.isNotEmpty()) {
            intent.putExtra("error", error)
        }
        startActivity(intent)
        activity?.finish()
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
