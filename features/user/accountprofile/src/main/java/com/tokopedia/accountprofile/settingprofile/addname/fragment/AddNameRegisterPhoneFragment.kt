package com.tokopedia.accountprofile.settingprofile.addname.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.common.ColorUtils
import com.tokopedia.accountprofile.databinding.FragmentAddNameRegisterBinding
import com.tokopedia.accountprofile.settingprofile.addname.AddNameRegisterPhoneAnalytics
import com.tokopedia.accountprofile.settingprofile.addname.di.DaggerAddNameComponent
import com.tokopedia.accountprofile.settingprofile.addname.viewmodel.AddNameViewModel
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by nisie on 22/04/19.
 */
open class AddNameRegisterPhoneFragment : BaseDaggerFragment() {

    private var _binding: FragmentAddNameRegisterBinding? = null
    private val binding get() = _binding
    var phoneNumber: String? = ""
    var uuid: String = ""

    private var isError = false

    @Inject
    lateinit var analytics: AddNameRegisterPhoneAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val addNameViewModel by lazy {
        viewModelProvider.get(AddNameViewModel::class.java)
    }

    companion object {
        val MIN_NAME = 3
        val MAX_NAME = 35

        private const val SPAN_34 = 34
        private const val SPAN_54 = 54
        private const val SPAN_61 = 61
        private const val SPAN_78 = 80
        private const val FLAG_0 = 0

        private const val BEARER = "Bearer"

        fun createInstance(bundle: Bundle): AddNameRegisterPhoneFragment {
            val fragment = AddNameRegisterPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        if (activity != null && activity?.application != null) {
            DaggerAddNameComponent.builder().baseAppComponent(
                ((activity as Activity).application as BaseMainApplication).baseAppComponent
            )
                .build()
                .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
        phoneNumber = getParamString(
            ApplinkConstInternalGlobal.PARAM_PHONE,
            arguments,
            savedInstanceState,
            ""
        )
        uuid =
            getParamString(ApplinkConstInternalGlobal.PARAM_UUID, arguments, savedInstanceState, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splitCompatInstall()
        _binding = FragmentAddNameRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun splitCompatInstall() {
        activity?.let {
            SplitCompat.installActivity(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setObserver()
        setViewListener()
        binding?.btnContinue?.let { disableButton(it) }
    }

    private fun setViewListener() {
        binding?.etName?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isNotEmpty()) {
                    binding?.btnContinue?.let { enableButton(it) }
                } else {
                    binding?.btnContinue?.let { disableButton(it) }
                }
                if (isError) {
                    hideValidationError()
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        binding?.btnContinue?.setOnClickListener { onContinueClick() }
    }

    private fun onContinueClick() {
        KeyboardHandler.DropKeyboard(activity, view)
        phoneNumber?.let {
            registerPhoneAndName(binding?.etName?.textFieldInput?.text.toString(), it)
            analytics.trackClickFinishAddNameButton()
        }
    }

    private fun registerPhoneAndName(name: String, phoneNumber: String) {
        if (isValidate(name)) {
            addNameViewModel.registerPhoneNumberAndName(name, phoneNumber, uuid)
            showLoading()
        }
    }

    private fun isValidate(name: String): Boolean {
        context?.let {
            if (name.length < MIN_NAME) {
                showValidationError(it.resources.getString(R.string.error_name_too_short))
                analytics.trackErrorFinishAddNameButton(it.resources.getString(R.string.error_name_too_short))
                return false
            }
            if (name.length > MAX_NAME) {
                showValidationError(it.resources.getString(R.string.error_name_too_long))
                analytics.trackErrorFinishAddNameButton(it.resources.getString(R.string.error_name_too_long))
                return false
            }
            hideValidationError()
        }
        return true
    }

    private fun setView() {
        binding?.btnContinue?.let { disableButton(it) }
        initTermPrivacyView()
    }

    private fun setObserver() {
        viewLifecycleOwner.observe(addNameViewModel.registerLiveData) {
            when (it) {
                is Success -> {
                    onSuccessRegister(it.data)
                }
                is Fail -> {
                    onErrorRegister(it.throwable)
                }
            }
        }
    }

    private fun initTermPrivacyView() {
        context?.let {
            val msg = getString(R.string.profile_completion_detail_term_and_privacy)
            if (msg.isNotEmpty()) {
                val termPrivacy =
                    SpannableString(getString(R.string.profile_completion_detail_term_and_privacy))
                termPrivacy.setSpan(
                    clickableSpan(PAGE_TERM_AND_CONDITION),
                    SPAN_34,
                    SPAN_54,
                    FLAG_0
                )
                termPrivacy.setSpan(clickableSpan(PAGE_PRIVACY_POLICY), SPAN_61, SPAN_78, FLAG_0)
                termPrivacy.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            it,
                            unifyprinciplesR.color.Unify_GN500
                        )
                    ),
                    SPAN_34,
                    SPAN_54,
                    FLAG_0
                )
                termPrivacy.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            it,
                            unifyprinciplesR.color.Unify_GN500
                        )
                    ),
                    SPAN_61,
                    82,
                    FLAG_0
                )

                binding?.bottomInfo?.setText(termPrivacy, TextView.BufferType.SPANNABLE)
                binding?.bottomInfo?.movementMethod = LinkMovementMethod.getInstance()
                binding?.bottomInfo?.isSelected = false
            }
        }
    }

    private fun clickableSpan(page: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                context?.let {
                    startActivity(
                        RouteManager.getIntent(
                            it,
                            ApplinkConstInternalUserPlatform.TERM_PRIVACY,
                            page
                        )
                    )
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(
                    requireContext(),
                    unifyprinciplesR.color.Unify_GN500
                )
            }
        }
    }

    private fun hideValidationError() {
        isError = false
        binding?.etName?.setError(false)
        binding?.etName?.setMessage("")
    }

    private fun showValidationError(errorMessage: String) {
        isError = true
        binding?.etName?.setError(true)
        binding?.etName?.setMessage(errorMessage)
    }

    private fun enableButton(button: UnifyButton) {
        button.isEnabled = true
    }

    private fun disableButton(button: TextView) {
        button.isEnabled = false
    }

    fun showLoading() {
        binding?.mainContent?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE
    }

    fun dismissLoading() {
        binding?.mainContent?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
    }

    fun onErrorRegister(throwable: Throwable) {
        userSession.clearToken()
        dismissLoading()
        showValidationError(ErrorHandler.getErrorMessage(context, throwable))
        analytics.trackErrorFinishAddNameButton(ErrorHandler.getErrorMessage(context, throwable))
    }

    private fun saveTokens(data: RegisterInfo) {
        userSession.setToken(
            data.accessToken,
            BEARER,
            EncoderDecoder.Encrypt(data.refreshToken, userSession.refreshTokenIV)
        )
    }

    fun onSuccessRegister(registerInfo: RegisterInfo) {
        userSession.clearToken()
        saveTokens(registerInfo)

        activity?.run {
            dismissLoading()
            analytics.trackSuccessRegisterPhoneNumber(registerInfo.userId)

            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtras(
                        Bundle().apply {
                            putExtra(ApplinkConstInternalGlobal.PARAM_ENABLE_2FA, registerInfo.enable2Fa)
                            putExtra(
                                ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA,
                                registerInfo.enableSkip2Fa
                            )
                        }
                    )
                }
            )

            finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
