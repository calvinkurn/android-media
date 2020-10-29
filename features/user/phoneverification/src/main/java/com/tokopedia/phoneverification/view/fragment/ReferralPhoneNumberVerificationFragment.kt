package com.tokopedia.phoneverification.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.phoneverification.PhoneVerificationAnalytics
import com.tokopedia.phoneverification.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.phonenumber.PhoneNumberUtil.transform

/**
 * Created by ashwanityagi on 12/12/17.
 */
class ReferralPhoneNumberVerificationFragment : BaseDaggerFragment() {
    override fun initInjector() {
    }

    var tvPhoneNumber: EditText? = null
    var btnActivation: TextView? = null
    private var ivTokocash: ImageView? = null
    private var analytics: PhoneVerificationAnalytics? = null
    lateinit var userSession: UserSession


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = PhoneVerificationAnalytics.createInstance()
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
        tvPhoneNumber?.setText(transform(
                userSession.phoneNumber))
        btnActivation = view.findViewById<View>(R.id.btn_activation) as TextView
        ivTokocash = view.findViewById<View>(R.id.img_app_share) as ImageView
        ImageHandler.loadImage2(ivTokocash, URL_TOKOCASH_SHARE, R.drawable.loading_page)
        setViewListener()
    }

    private fun setViewListener() {
        btnActivation?.setOnClickListener {
            val rawPhone = tvPhoneNumber?.text.toString().replace("-", "")
            analytics?.eventReferralAndShare(
                    PhoneVerificationAnalytics.Action.CLICK_VERIFY_NUMBER,
                    tvPhoneNumber?.text.toString().replace("-", ""))
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.ADD_PHONE_WITH, rawPhone)
            startActivity(intent)
            activity?.finish()
        }
    }

    companion object {
        fun newInstance(): ReferralPhoneNumberVerificationFragment {
            return ReferralPhoneNumberVerificationFragment()
        }

        const val URL_TOKOCASH_SHARE = "https://ecs7.tokopedia.net/img/android/others/ic_referral_tokocash.png"
    }
}