package com.tokopedia.play.data.ssemapper

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.data.UpcomingChannelUpdateLive
import com.tokopedia.play.data.UpcomingChannelUpdateActive
import com.tokopedia.play_common.sse.model.SSEResponse

/**
 * Created By : Jonathan Darwin on September 08, 2021
 */
class PlaySSEMapper(private val response: SSEResponse) {

    private val gson = Gson()

    fun mapping(): Any? {
        return when(response.event) {
            PlaySSEType.UpcomingChannelUpdateLive.type -> mapUpcomingChannelUpdateLive()
            PlaySSEType.UpcomingChannelUpdateActive.type -> mapUpcomingChannelUpdateActive()
            else -> null
        }
    }

    private fun mapUpcomingChannelUpdateLive(): UpcomingChannelUpdateLive? {
        return convertToModel(response.message, UpcomingChannelUpdateLive::class.java)
    }

    private fun mapUpcomingChannelUpdateActive(): UpcomingChannelUpdateActive? {
        return convertToModel(response.message, UpcomingChannelUpdateActive::class.java)
    }

    private fun <T> convertToModel(message: String, classOfT: Class<T>): T? {
        try {
            return gson.fromJson(message, classOfT)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().log("E/${TAG}: ${e.localizedMessage}")
            }
        }
        return null
    }

    companion object {
        private const val TAG = "PlaySSEMapper"
    }
}