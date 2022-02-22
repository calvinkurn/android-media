package com.tokopedia.notifications.factory.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationChannelController(private val context: Context) {


    private val vibratePattern: LongArray
        get() = longArrayOf(500, 500)


    /*
     * Default Channel(Name : Notifikasi, Sound : Default System Sound)
     *
     * case 1: ChannelName or `Sound file` name is Null/Empty then create `Default Channel`
     * case 2: ChannelName is `Notifikasi` then create Default Channel ignore sound file
     * case 3: if SoundFile doesn't exists in system create use `Default Channel`
     * case 4: Channel Name and Sound file not belongs to `Default Channel`
     *         a. Check for validity if channel and get Channel ID (every new channel will have channel id end with _v1)
     *         b. create new channel
     * return channel id
    * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel(channelName: String?, soundFileName: String?): String {
        if (channelName.isNullOrBlank() || soundFileName.isNullOrBlank())
            return createDefaultChannel()
        else if (channelName == NotificationChannelConstant.CHANNEL_DEFAULT_NAME) {
            return createDefaultChannel()
        } else if (isSoundFileNotExists(soundFileName)) {
            return createDefaultChannel()
        } else {
            val channelId = resetAndGetChannelID(channelName, soundFileName)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(
                channelId,
                channelName, importance
            )

            channel.description = NotificationChannelConstant.CHANNEL_DESCRIPTION

            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            channel.setSound(
                Uri.parse(
                    "android.resource://" + context.packageName + "/" +
                            "raw/" + soundFileName
                ), att
            )

            channel.vibrationPattern = vibratePattern
            channel.setShowBadge(true)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            return channelId
        }
    }

    /*
    * return true if Sound file not exists in App resource
    * */
    private fun isSoundFileNotExists(soundFileName: String): Boolean {
        val checkExistence: Int = context.resources
            .getIdentifier(soundFileName, "raw", context.packageName)
        return checkExistence == 0
    }


    /**
     * - if channel not found then new Channel id will ends with _v1
     * - if channel is created
     *          case 1: Channel id ends with _v1 - return same channel id
     *          case 2: sound file is different - delete channel and create Channel id with post fix _v1 and return it
     *          case 3: return old channel id
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun resetAndGetChannelID(channelName: String, soundFileName: String): String {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channel = getNotificationChannelByName(channelName)
        if (channel == null)
            return channelName + "_v1"
        else {
            return when {
                channel.id.endsWith("_v1") -> return channel.id
                channel.sound.lastPathSegment != soundFileName -> {
                    notificationManager.deleteNotificationChannel(channel.id)
                    channelName + "_v1"
                }
                else -> channel.id
            }

        }
    }


    /*Search Channel Name in All existing Channels*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getNotificationChannelByName(channelName: String): NotificationChannel? {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channels = notificationManager.notificationChannels
        channels?.forEach { channel: NotificationChannel? ->
            if (channel != null && channel.name == channelName)
                return channel
        }
        return null
    }

    /*
     * Default Channel(Name : Notifikasi, Sound : Default System Sound)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createDefaultChannel(): String {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            NotificationChannelConstant.CHANNEL_DEFAULT_ID,
            NotificationChannelConstant.CHANNEL_DEFAULT_NAME,
            importance
        )
        val att = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        channel.setSound(ringtoneUri, att)
        channel.setShowBadge(true)
        channel.description = NotificationChannelConstant.CHANNEL_DEFAULT_DESCRIPTION
        channel.vibrationPattern = vibratePattern
        notificationManager.createNotificationChannel(channel)
        return NotificationChannelConstant.CHANNEL_DEFAULT_ID;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createAndGetSilentChannel(): String {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notificationChannel = NotificationChannel(
            NotificationChannelConstant.Channel_DefaultSilent_Id,
            NotificationChannelConstant.Channel_DefaultSilent_Name,
            importance
        )
        notificationChannel.description =
            NotificationChannelConstant.Channel_DefaultSilent_DESCRIPTION
        notificationChannel.setSound(null, null)
        notificationChannel.enableLights(false)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)
        return NotificationChannelConstant.Channel_DefaultSilent_Id
    }

    companion object {
        fun getNotificationController(context: Context) =
            NotificationChannelController(context.applicationContext)
    }

}

object NotificationChannelConstant {

    var CHANNEL_DEFAULT_ID = "ANDROID_GENERAL_CHANNEL"
    val CHANNEL_DEFAULT_NAME = "Notifikasi"
    val CHANNEL_DEFAULT_DESCRIPTION = "Notifikasi"

    var CHANNEL_DESCRIPTION = "Tokopedia Ringtone"

    var Channel_DefaultSilent_Id = "Default_Channel"
    var Channel_DefaultSilent_Name = "Default"
    var Channel_DefaultSilent_DESCRIPTION = "Dafault Silent"


}