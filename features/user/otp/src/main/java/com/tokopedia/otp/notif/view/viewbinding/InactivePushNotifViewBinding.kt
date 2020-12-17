package com.tokopedia.otp.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.ticker.Ticker
import javax.inject.Inject

/**
 * Created by Ade Fulki on 25/09/20.
 */

class InactivePushNotifViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_push_notif_inactive

    var ticker: Ticker? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                ticker = findViewById(R.id.ticker_push_notif_inactive)
            }
}