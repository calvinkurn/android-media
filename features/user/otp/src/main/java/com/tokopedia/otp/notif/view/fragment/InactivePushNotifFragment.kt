package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import android.util.Log
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifData
import com.tokopedia.otp.notif.view.viewbinding.InactivePushNotifViewBinding
import com.tokopedia.unifycomponents.ticker.TickerCallback
import javax.inject.Inject

/**
 * Created by Ade Fulki on 25/09/20.
 */

class InactivePushNotifFragment : BaseOtpFragment() {

    @Inject
    lateinit var analytics: TrackingOtpUtil

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
        viewBound.ticker?.setHtmlDescription(getString(R.string.activate_push_notif_ticker))
        viewBound.ticker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                analytics.trackClickPushNotifSettingButton()
                val intent = RouteManager.getIntent(
                        activity,
                        ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING + PUSH_NOTIFICATION_NS_QUERY
                )
                startActivity(intent)
            }

            override fun onDismiss() {}
        })
    }

    companion object {

        private const val PUSH_NOTIFICATION_NS_QUERY = "?push_notification=true"

        fun createInstance(bundle: Bundle): InactivePushNotifFragment {
            val fragment = InactivePushNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}