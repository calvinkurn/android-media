package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.view.viewbinding.ResultNotifViewBinding

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ResultNotifFragment : BaseOtpFragment(), IOnBackPressed {

    override var viewBound = ResultNotifViewBinding()

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onBackPressed(): Boolean = true

    companion object {
        fun createInstance(bundle: Bundle): ResultNotifFragment {
            val fragment = ResultNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}