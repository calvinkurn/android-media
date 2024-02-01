package com.tokopedia.referral.view.fragment

import com.tokopedia.imageassets.TokopediaImageUrl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.media.loader.loadImageWithError
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.referral.R
import com.tokopedia.referral.analytics.ReferralPhoneVerificationAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.phonenumber.PhoneNumberUtil.transform


/**
 */
class ReferralPhoneNumberVerificationFragment : BaseDaggerFragment() {
    override fun initInjector() {
    }

    var tvPhoneNumber: EditText? = null
    var btnActivation: TextView? = null
    private var ivTokocash: ImageView? = null
    private var analytics: ReferralPhoneVerificationAnalytics? = null
    lateinit var userSession: UserSession


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = ReferralPhoneVerificationAnalytics.createInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_referral_phone_number_verification,
                container, false)
        initView(view)
        return view
    }

    override fun getScreenName(): String {
        return ""
    }

    private fun initView(view: View) {
        userSession = UserSession(activity)
        tvPhoneNumber = view.findViewById<View>(R.id.tv_phone_number) as EditText
        tvPhoneNumber?.setText(transform(userSession.phoneNumber))
        btnActivation = view.findViewById<View>(R.id.btn_activation) as TextView
        ivTokocash = view.findViewById<View>(R.id.img_app_share) as ImageView
        ivTokocash?.loadImageWithError(URL_TOKOCASH_SHARE, com.tokopedia.design.R.drawable.loading_page)
        setViewListener()
    }

    private fun setViewListener() {
        btnActivation?.setOnClickListener {
            val rawPhone = tvPhoneNumber?.text.toString().replace("-", "")
            analytics?.eventReferralAndShare(
                    ReferralPhoneVerificationAnalytics.Action.CLICK_VERIFY_NUMBER,
                    tvPhoneNumber?.text.toString().replace("-", ""))
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.ADD_PHONE_WITH, rawPhone)
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            startActivity(intent)
            activity?.finish()
        }
    }

    companion object {
        fun newInstance(): ReferralPhoneNumberVerificationFragment {
            return ReferralPhoneNumberVerificationFragment()
        }
        const val EXTRA_PHONE = "phone"
        const val URL_TOKOCASH_SHARE = TokopediaImageUrl.URL_TOKOCASH_SHARE
    }
}
