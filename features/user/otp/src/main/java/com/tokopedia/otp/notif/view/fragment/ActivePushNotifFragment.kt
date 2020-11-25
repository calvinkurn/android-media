package com.tokopedia.otp.notif.view.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpFragment
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifData
import com.tokopedia.otp.notif.view.adapter.ActiveDevicesAdapter
import com.tokopedia.otp.notif.view.viewbinding.ActivePushNotifViewBinding
import com.tokopedia.unifycomponents.ticker.TickerCallback
import javax.inject.Inject

/**
 * Created by Ade Fulki on 25/09/20.
 */

class ActivePushNotifFragment : BaseOtpFragment() {

    @Inject
    lateinit var analytics: TrackingOtpUtil

    private lateinit var deviceStatus: DeviceStatusPushNotifData

    override val viewBound = ActivePushNotifViewBinding()

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
        if (deviceStatus.isTrusted) {
            viewBound.ticker?.setHtmlDescription(getString(R.string.remove_device_ticker))
            viewBound.ticker?.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    analytics.trackClickChangePasswordSettingButton()
                    context?.startActivity(RouteManager.getIntent(context, ApplinkConstInternalGlobal.HAS_PASSWORD))
                }

                override fun onDismiss() {}
            })
            viewBound.listDevice?.setHasFixedSize(true)
            viewBound.listDevice?.layoutManager = LinearLayoutManager(context)

            val activeDevicesAdapter = ActiveDevicesAdapter(deviceStatus.listDevices)
            activeDevicesAdapter.notifyDataSetChanged()
            viewBound.listDevice?.adapter = activeDevicesAdapter
        } else {
            viewBound.ticker?.setHtmlDescription(getString(R.string.activate_push_notif_ticker))
            viewBound.ticker?.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    analytics.trackClickPushNotifSettingButton()
                    context?.startActivity(RouteManager.getIntent(
                            context,
                            "$USER_NOTIFICATION_SETTING$PUSH_NOTIFICATION_NS_QUERY"
                    ))
                }

                override fun onDismiss() {}
            })
        }
    }

    companion object {

        private const val PUSH_NOTIFICATION_NS_QUERY = "?push_notification=true"

        fun createInstance(bundle: Bundle): ActivePushNotifFragment {
            val fragment = ActivePushNotifFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}