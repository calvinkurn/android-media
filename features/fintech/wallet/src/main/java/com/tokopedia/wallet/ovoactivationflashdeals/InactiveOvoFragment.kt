package com.tokopedia.wallet.ovoactivationflashdeals

import android.app.Application
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wallet.R
import com.tokopedia.wallet.databinding.FragmentInactiveOvoBinding
import com.tokopedia.wallet.di.WalletComponentInstance
import javax.inject.Inject

class InactiveOvoFragment : BaseDaggerFragment() {

    @Inject
    lateinit var tracking: InactiveOvoAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentInactiveOvoBinding>()

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        val walletComponent = WalletComponentInstance.getComponent(activity?.application as Application)
        walletComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInactiveOvoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val registerApplink = it.getString(REGISTER_APPLINK)
            val helpApplink = it.getString(HELP_APPLINK)
            val tncApplink = it.getString(TNC_APPLINK, "")
            val productId = it.getString(PRODUCT_ID, "")

            activity?.run {
                binding?.btnTopupActivation?.setOnClickListener {
                    tracking.eventClickActivationOvoNow(productId, userSession.userId)
                    RouteManager.route(this, registerApplink)
                    this.finish()
                }

                binding?.btnLearnMore?.setOnClickListener {
                    tracking.eventClickOvoLearnMore(productId, userSession.userId)
                    RouteManager.route(this, helpApplink)
                }
            }
            setTncOvo(tncApplink, productId)
        }
    }

    private fun setTncOvo(tncApplink: String, productId: String) {
        activity?.let {
            val ss = SpannableString(it.getString(R.string.wallet_inactivate_ovo_text_tnc))
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    activity?.run {
                        tracking.eventClickTnc(productId, userSession.userId)
                        RouteManager.route(this, tncApplink)
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                }
            }
            ss.setSpan(ForegroundColorSpan(ContextCompat.getColor(it,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500)), 6, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ss.setSpan(clickableSpan, 6, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding?.tncOvo?.movementMethod = LinkMovementMethod.getInstance()
            binding?.tncOvo?.text = ss
        }
    }

    companion object {

        private const val REGISTER_APPLINK = "activation_applink"
        private const val HELP_APPLINK = "help_applink"
        private const val TNC_APPLINK = "tnc_applink"
        private const val PRODUCT_ID = "product_id"

        fun newInstance(registerApplink: String, helpApplink: String, tncApplink: String, productId: String): Fragment {
            val fragment = InactiveOvoFragment()
            val bundle = Bundle()
            bundle.putString(REGISTER_APPLINK, registerApplink)
            bundle.putString(HELP_APPLINK, helpApplink)
            bundle.putString(TNC_APPLINK, tncApplink)
            bundle.putString(PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}
