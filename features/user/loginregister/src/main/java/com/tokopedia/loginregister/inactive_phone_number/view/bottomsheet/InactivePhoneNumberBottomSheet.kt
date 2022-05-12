package com.tokopedia.loginregister.inactive_phone_number.view.bottomsheet

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
import com.tokopedia.loginregister.common.analytics.InactivePhoneNumberAnalytics
import com.tokopedia.loginregister.databinding.LayoutNeedHelpBottomsheetBinding
import com.tokopedia.loginregister.inactive_phone_number.di.InactivePhoneNumberComponentBuilder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject

class InactivePhoneNumberBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var inactivePhoneNumberAnalytics: InactivePhoneNumberAnalytics

    private var _bindingChild: LayoutNeedHelpBottomsheetBinding? = null
    private val bindingChild get() = _bindingChild!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InactivePhoneNumberComponentBuilder.getComponent(activity?.application).inject(this)
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

        setTitle(context?.getString(R.string.ipn_what_help_do_you_need) ?: "")

        setListener()
    }

    private fun setListener() {

        initTokopediaCareTextNeedHelpBottomSheet(bindingChild.toNeedAnotherHelp)

        bindingChild.ubInactivePhoneNumber.setOnClickListener {
            inactivePhoneNumberAnalytics.trackPageBottomSheetClickInactivePhoneNumber()
            goToInactivePhoneNumber()
        }

        bindingChild.ubForgotPassword.setOnClickListener {
            inactivePhoneNumberAnalytics.trackPageBottomSheetClickForgotPassword()
            goToForgotPassword()
        }

        setCloseClickListener {
            inactivePhoneNumberAnalytics.trackPageBottomSheetClickClose()
            dismiss()
        }

    }

    private fun initTokopediaCareTextNeedHelpBottomSheet(typography: Typography) {
        val message = getString(R.string.ipn_need_another_help)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    inactivePhoneNumberAnalytics.trackPageBottomSheetClickTokopediaCare()
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
                "%s?url=%s",
                ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun goToForgotPassword() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
    }

    private fun goToInactivePhoneNumber() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INACTIVE_PHONE_NUMBER)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _bindingChild = null
    }

    companion object {
        private const val TOKOPEDIA_CARE_PATH = "help"
    }
}