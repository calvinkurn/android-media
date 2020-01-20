package com.tokopedia.officialstore.reactnative

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Arguments
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.officialstore.R
import com.tokopedia.tkpdreactnative.react.ReactConst
import com.tokopedia.tkpdreactnative.react.ReactUtils
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment

class ReactNativeOfficialStoreFragment : ReactNativeFragment(), AllNotificationListener {

    override fun getModuleName(): String {
        return ReactConst.Screen.OFFICIAL_STORE_HOME
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ReactUtils.startTracing(MP_OFFICIAL_STORE) // start trace when view created
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (activity != null && view != null) {
            // set background color of react root view
            view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.white))
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            view.invalidate()
        }
        return view
    }

    override fun getInitialBundle(): Bundle {
        return arguments ?: Bundle()
    }

    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int) {
        if (reactInstanceManager != null && reactInstanceManager.currentReactContext != null) {
            val param = Arguments.createMap()
            param.putInt(TOTAL_NOTIFICATION, notificationCount)
            param.putInt(TOTAL_INBOX, inboxCount)
            reactInstanceManager
                    .currentReactContext!!
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    .emit(REFRESH_NOTIFICATION, param)
        }
    }

    companion object {

        private val TOTAL_INBOX = "totalInbox"
        private val TOTAL_NOTIFICATION = "totalNotif"
        private val MP_OFFICIAL_STORE = "mp_official_store"
        private val REFRESH_NOTIFICATION = "refreshNotification"

        fun createInstance(): ReactNativeOfficialStoreFragment {
            return ReactNativeOfficialStoreFragment()
        }
    }
}