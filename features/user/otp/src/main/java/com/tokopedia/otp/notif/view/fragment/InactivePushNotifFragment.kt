package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import android.util.Log
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifData
import com.tokopedia.otp.notif.view.viewbinding.InactivePushNotifViewBinding
import com.tokopedia.unifycomponents.ticker.TickerCallback

/**
 * Created by Ade Fulki on 25/09/20.
 */

class InactivePushNotifFragment : BaseOtpFragment() {

    private lateinit var deviceStatus: DeviceStatusPushNotifData

    override val viewBound = InactivePushNotifViewBinding()

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initVar() {
        arguments?.let {
            deviceStatus = it.getSerializable(SettingNotifFragment.PARAM_DEVICE_STATUS) as? DeviceStatusPushNotifData
                    ?: DeviceStatusPushNotifData()
        }
    }

    private fun initView() {
        viewBound.ticker?.setHtmlDescription(String.format(getString(R.string.activate_push_notif_ticker), ApplinkConstInternalGlobal.HAS_PASSWORD))
        viewBound.ticker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.HAS_PASSWORD)
                startActivity(intent)
            }

            override fun onDismiss() {}
        })
    }

    companion object {

        fun createInstance(bundle: Bundle): InactivePushNotifFragment {
            val fragment = InactivePushNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}