package com.tokopedia.otp.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Ade Fulki on 25/09/20.
 */

class ActivePushNotifViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_push_notif_active

    var title: Typography? = null
    var subtitle: Typography? = null
    var listDevice: RecyclerView? = null
    var ticker: Ticker? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                title = findViewById(R.id.title_active)
                subtitle = findViewById(R.id.subtitle_active)
                listDevice = findViewById(R.id.list_device)
                ticker = findViewById(R.id.ticker_push_notif_active)
            }
}