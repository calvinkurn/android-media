package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.viewbinding.ActivePushNotifViewBinding
import javax.inject.Inject

/**
 * Created by Ade Fulki on 25/09/20.
 */

class ActivePushNotifFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewBound: ActivePushNotifViewBinding

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