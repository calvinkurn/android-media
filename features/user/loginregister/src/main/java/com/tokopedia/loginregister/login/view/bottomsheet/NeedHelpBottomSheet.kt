package com.tokopedia.loginregister.login.view.bottomsheet

import android.content.Context
import android.content.Intent
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.NeedHelpAnalytics
import com.tokopedia.loginregister.databinding.LayoutNeedHelpBottomsheetBinding
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject

class NeedHelpBottomSheet: BottomSheetUnify() {

    @Inject
    lateinit var needHelpAnalytics: NeedHelpAnalytics

    private var _bindingChild: LayoutNeedHelpBottomsheetBinding? = null
    private val bindingChild get() = _bindingChild!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.application?.let { ActivityComponentFactory.instance.createLoginComponent(it).inject(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingChild = LayoutNeedHelpBottomsheetBinding.inflate(layoutInflater, container, false)
        setChild(bindingChild.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(context?.getString(R.string.loginregister_what_help_do_you_need) ?: "")

        setListener()
    }

    private fun setListener() {

        initTokopediaCareText(bindingChild.toNeedAnotherHelp)

        bindingChild.ubInactivePhoneNumber.setOnClickListener {
            needHelpAnalytics.trackPageBottomSheetClickInactivePhoneNumber()
            goToInactivePhoneNumber()
        }

        bindingChild.ubForgotPassword.setOnClickListener {
            needHelpAnalytics.trackPageBottomSheetClickForgotPassword()
            goToForgotPassword()
        }

        setCloseClickListener {
            needHelpAnalytics.trackPageBottomSheetClickClose()
            dismiss()
        }

    }

    private fun initTokopediaCareText(typography: Typography) {
        val message = getString(R.string.loginregister_need_another_help)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    needHelpAnalytics.trackPageBottomSheetClickTokopediaCare()
                    goToTokopediaCare()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                    ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
            },
            message.indexOf(getString(R.string.call_tokopedia_care)),
            message.indexOf(getString(R.string.call_tokopedia_care)) + getString(R.string.call_tokopedia_care).length,
            0
        )
        typography.movementMethod = LinkMovementMethod.getInstance()
        typography.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            context,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun goToForgotPassword() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
    }

    private fun goToInactivePhoneNumber(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INPUT_OLD_PHONE_NUMBER)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _bindingChild = null
    }

    companion object {
        private const val TOKOPEDIA_CARE_PATH = LoginEmailPhoneFragment.TOKOPEDIA_CARE_PATH
        private const val TOKOPEDIA_CARE_STRING_FORMAT = LoginEmailPhoneFragment.TOKOPEDIA_CARE_STRING_FORMAT
    }
}