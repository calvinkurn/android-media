package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.viewbinding.InactivePushNotifViewBinding

/**
 * Created by Ade Fulki on 25/09/20.
 */

class InactivePushNotifFragment : BaseOtpFragment() {

    override val viewBound = InactivePushNotifViewBinding()

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {

    }

    companion object {

        fun createInstance(bundle: Bundle): InactivePushNotifFragment {
            val fragment = InactivePushNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}