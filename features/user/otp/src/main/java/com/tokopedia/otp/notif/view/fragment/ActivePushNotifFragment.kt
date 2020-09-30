package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.viewbinding.ActivePushNotifViewBinding

/**
 * Created by Ade Fulki on 25/09/20.
 */

class ActivePushNotifFragment : BaseOtpFragment() {

    override val viewBound = ActivePushNotifViewBinding()

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {

    }

    companion object {

        fun createInstance(bundle: Bundle): ActivePushNotifFragment {
            val fragment = ActivePushNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}