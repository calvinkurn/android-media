package com.tokopedia.otp.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding

/**
 * Created by Ade Fulki on 14/09/20.
 */

class SettingNotifViewBinding : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_notif_setting

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                // add view
            }
}