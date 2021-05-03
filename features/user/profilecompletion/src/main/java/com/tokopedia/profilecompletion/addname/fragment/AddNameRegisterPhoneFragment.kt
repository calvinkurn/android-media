package com.tokopedia.profilecompletion.addname.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addname.AddNameRegisterPhoneAnalytics
import com.tokopedia.profilecompletion.addname.di.DaggerAddNameComponent
import com.tokopedia.profilecompletion.addname.listener.AddNameListener
import com.tokopedia.profilecompletion.addname.presenter.AddNamePresenter
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 22/04/19.
 */
open class AddNameRegisterPhoneFragment : BaseDaggerFragment(), AddNameListener.View {

    var phoneNumber: String? = ""
    var uuid: String? = ""

    private var bottomInfo: TextView? = null
    private var progressBar: ProgressBar? = null
    private var mainContent: View? = null
    private var textName: TextFieldUnify? = null
    private var btnNext: UnifyButton? = null

    private var isError = false

    @Inject
    lateinit var presenter: AddNamePresenter

    @Inject
    lateinit var analytics: AddNameRegisterPhoneAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        val MIN_NAME = 3
        val MAX_NAME = 35

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
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
        phoneNumber =  getParamString(ApplinkConstInternalGlobal.PARAM_PHONE, arguments, savedInstanceState, "")
        uuid =  getParamString(ApplinkConstInternalGlobal.PARAM_UUID, arguments, savedInstanceState, "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        splitCompatInstall()

        val view = inflater.inflate(com.tokopedia.profilecompletion.R.layout.fragment_add_name_register, container, false)
        bottomInfo = view.findViewById(R.id.bottom_info)
        progressBar = view.findViewById(R.id.progress_bar)
        mainContent = view.findViewById(R.id.main_content)
        textName = view.findViewById(R.id.et_name)
        btnNext = view.findViewById(R.id.btn_continue)
        return view
    }

    private fun splitCompatInstall() {
        activity?.let{
            SplitCompat.installActivity(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        setView()
        setViewListener()
        btnNext?.let { disableButton(it) }
    }

    private fun setViewListener() {
        textName?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isNotEmpty()) {
                    btnNext?.let { enableButton(it) }
                } else {
                    btnNext?.let { disableButton(it) }
                }
                if (isError) {
                    hideValidationError()
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        btnNext?.setOnClickListener { onContinueClick() }
    }

    private fun onContinueClick() {
        KeyboardHandler.DropKeyboard(activity, view)
        phoneNumber?.let{
            registerPhoneAndName(textName?.textFieldInput?.text.toString(), it)
            analytics.trackClickFinishAddNameButton()
        }
    }

    private fun registerPhoneAndName(name: String, phoneNumber: String) {
        if (isValidate(name)) {
            presenter.registerPhoneNumberAndName(name, phoneNumber)
        }
    }

    private fun isValidate(name: String): Boolean {
        context?.let{
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
        btnNext?.let { disableButton(it) }
        initTermPrivacyView()
    }

    private fun initTermPrivacyView() {
        context?.let {
            val termPrivacy = SpannableString(getString(R.string.detail_term_and_privacy))
            termPrivacy.setSpan(clickableSpan(PAGE_TERM_AND_CONDITION), 34, 54, 0)
            termPrivacy.setSpan(clickableSpan(PAGE_PRIVACY_POLICY), 61, 78, 0)
            termPrivacy.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), 34, 54, 0)
            termPrivacy.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), 61, 78, 0)

            bottomInfo?.setText(termPrivacy, TextView.BufferType.SPANNABLE)
            bottomInfo?.movementMethod = LinkMovementMethod.getInstance()
            bottomInfo?.isSelected = false
        }
    }

    private fun clickableSpan(page: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                context?.let {
                    startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.TERM_PRIVACY, ))
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }
    }

    private fun hideValidationError() {
        isError = false
        textName?.setError(false)
        textName?.setMessage("")
    }

    private fun showValidationError(errorMessage: String) {
        isError = true
        textName?.setError(true)
        textName?.setMessage(errorMessage)
    }

    private fun enableButton(button: UnifyButton) {
        button.isEnabled = true
    }

    private fun disableButton(button: TextView) {
        button.isEnabled = false
    }

    override fun showLoading() {
        mainContent?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    fun dismissLoading() {
        mainContent?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

    override fun onErrorRegister(throwable: Throwable) {
        userSession.clearToken()
        dismissLoading()
        showValidationError(ErrorHandler.getErrorMessage(context, throwable))
        analytics.trackErrorFinishAddNameButton(ErrorHandler.getErrorMessage(context, throwable))

    }

    override fun onSuccessRegister(registerInfo: RegisterInfo) {
        userSession.clearToken()
        userSession.setToken(registerInfo.accessToken, "Bearer", registerInfo.refreshToken)

        activity?.run {
            dismissLoading()
            analytics.trackSuccessRegisterPhoneNumber(registerInfo.userId)

            setResult(Activity.RESULT_OK, Intent().apply {
                putExtras(Bundle().apply {
                    putExtra(ApplinkConstInternalGlobal.PARAM_ENABLE_2FA, registerInfo.enable2Fa)
                    putExtra(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, registerInfo.enableSkip2Fa)
                })
            })

            finish()
        }
    }
}