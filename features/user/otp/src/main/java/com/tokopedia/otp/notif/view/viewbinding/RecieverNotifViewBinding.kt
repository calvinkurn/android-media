package com.tokopedia.otp.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding

/**
 * Created by Ade Fulki on 14/09/20.
 */

class RecieverNotifViewBinding : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_verification_method

    var containerView: View? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
            }
}