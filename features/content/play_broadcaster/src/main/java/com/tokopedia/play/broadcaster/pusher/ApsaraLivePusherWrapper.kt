package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import com.alivc.live.pusher.AlivcLivePushConfig
import com.alivc.live.pusher.AlivcLivePusher
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.util.deviceinfo.DeviceInfoUtil
import com.tokopedia.play.broadcaster.util.extension.sendCrashlyticsLog


/**
 * Created by mzennis on 15/03/21.
 */
class ApsaraLivePusherWrapper private constructor(
        private val context: Context,
        private val pusherConfig: AlivcLivePushConfig
) {

    class Builder(context: Context) {

        private val mContext: Context = context.applicationContext
        private var mApsaraLivePushConfig: AlivcLivePushConfig? = null

        fun setPushConfig(pushConfig: AlivcLivePushConfig): Builder {
            mApsaraLivePushConfig = pushConfig
            return this
        }

        fun build(): ApsaraLivePusherWrapper {
            return ApsaraLivePusherWrapper(mContext, mApsaraLivePushConfig ?: DefaultApsaraLivePusherConfig(mContext))
        }

    }

    private var aliVcLivePusher: AlivcLivePusher? = null

    private var listener: Listener? = null

    private var ingestUrl: String = ""

    init {
        if (DeviceInfoUtil.isDeviceSupported()) {
            if (aliVcLivePusher != null) {
                aliVcLivePusher?.destroy()
                aliVcLivePusher = null
            }
            try {
                aliVcLivePusher = AlivcLivePusher()
                aliVcLivePusher?.init(context, pusherConfig)
//                aliVcLivePusher?.setLivePushErrorListener(mAliVcLivePushErrorListener)
//                aliVcLivePusher?.setLivePushNetworkListener(mAliVcLivePushNetworkListener)
//                aliVcLivePusher?.setLivePushInfoListener(mAliVcLivePushInfoListener)
                aliVcLivePusher?.setAudioDenoise(true)
            } catch (e: Exception) {
                sendCrashlyticsLog(e)
            } catch (e: Error) {
                sendCrashlyticsLog(e)
            }
        }

    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun safeInit() {

    }

    interface Listener {
        fun onLivePusherStateChanged(pusherState: ApsaraLivePusherState)
    }
}