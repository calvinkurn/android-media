package com.tokopedia.home_account.linkaccount.view

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FragmentPrivacyAccountBinding
import com.tokopedia.home_account.linkaccount.di.LinkAccountComponent
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class PrivacyAccountFragment : BaseDaggerFragment() {

    private val binding : FragmentPrivacyAccountBinding? by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_privacy_account, container, false)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(LinkAccountComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewDescPrivacyAccount()
    }

    private fun setViewDescPrivacyAccount() {
        binding?.txtDescPrivacyAccount?.show()
        val message = getString(R.string.opt_desc)
        val spannable = SpannableString(message)
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    //goToTokopediaCareWebview()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                }
            },
            message.indexOf(TEXT_LINK_DESC_PRIVACY_ACCOUNT),
            message.length,
            0
        )
        binding?.txtDescPrivacyAccount?.movementMethod = LinkMovementMethod.getInstance()
        binding?.txtDescPrivacyAccount?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    companion object {

        private const val TEXT_LINK_DESC_PRIVACY_ACCOUNT = "Cek Data yang Dipakai"
        private val SCREEN_NAME = PrivacyAccountFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = PrivacyAccountFragment()
    }
}