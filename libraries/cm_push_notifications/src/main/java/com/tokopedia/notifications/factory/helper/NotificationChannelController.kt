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
import androidx.core.app.NotificationCompat

class NotificationChannelController(private val context: Context) {


    private val vibratePattern: LongArray
        get() = longArrayOf(500, 500)


    private val ringtoneUri: Uri
        get() = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


    /**
     * This function is setting up Notification sound as silent for Pre-oreo devices
     * We use this to update notification on Pre-oreo device without playing any sound.
     *
     * */
    fun setPreOreoSilentSound(builder: NotificationCompat.Builder) {
        builder.setSound(null)
        builder.setVibrate(null)
    }

    /**
     * This function set sound and vibration properties for Pre-Oreo Android Notifications
     * If sound file is not present then Device default notification sound will be used.
     *
     * */
    fun setPreOreoSound(builder: NotificationCompat.Builder, soundFileName: String?) {
        if (soundFileName == null || isSoundFileNotExists(soundFileName)) {
            builder.setSound(ringtoneUri)
        } else {
            val soundUri = getSoundFileURI(soundFileName)
            builder.setSound(soundUri)
        }
        builder.setVibrate(vibratePattern)
    }

    /**
     * This function handles channel creation based on Android Build version
     * if Android Build version is lower than Oreo than it returns Default channel id
     * */
    fun getChannelID(channelName: String?, soundFileName: String?): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelName, soundFileName)
        } else {
            return NotificationChannelConstant.CHANNEL_DEFAULT_ID
        }
    }


    /*
     * Default Channel(Name : Notifikasi, Sound : Default System Sound)
     *
     * case 1: ChannelName or `Sound file` name is Null/Empty then create `Default Channel`
     * case 2: ChannelName is `Notifikasi` then create Default Channel ignore sound file
     * case 3: if SoundFile doesn't exists in system create use `Default Channel`
     * case 4: Channel Name and Sound file not belongs to `Default Channel`
     *          - create new channel
     * return channel id (channelName)
     *
     * In this we are protecting silent channel by redirecting it to default channel.
    * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel(channelName: String?, soundFileName: String?): String {
        if (channelName.isNullOrBlank() || soundFileName.isNullOrBlank())
            return createDefaultChannel()
        else if (channelName == NotificationChannelConstant.CHANNEL_DEFAULT_NAME ||
                channelName == NotificationChannelConstant.Channel_DefaultSilent_Name) {
            return createDefaultChannel()
        } else if (isSoundFileNotExists(soundFileName)) {
            return createDefaultChannel()
        } else {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                channelName,
                channelName, importance
            )
            channel.description = NotificationChannelConstant.CHANNEL_DESCRIPTION
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(getSoundFileURI(soundFileName), att)
            channel.vibrationPattern = vibratePattern
            channel.setShowBadge(true)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            return channelName
        }
    }

    /**
     * This function check sound file existence using URI of raw file
     *return true if Sound file not exists in App resource
     *
     * */
    private fun isSoundFileNotExists(soundFileName: String): Boolean {
        val checkExistence: Int = context.resources
            .getIdentifier(soundFileName, RAW_FOLDER, context.packageName)
        return checkExistence == 0
    }


    /**
     * This global app channel for Customer and Seller app...
     * Default Channel(Name : Notifikasi, Sound : Default System Sound)
     * channel description : Notifikasi
     * Channel ID: ANDROID_GENERAL_CHANNEL
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


    /**
     * This is silent channel and we use this channel to update exiting notification
     * This channel has no sound
     * Channel Name : Default
     * Channel ID : Default_Channel
     * Channel Description : Default Silent
    * */
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

    /**
     * Return Uri for sound file name for Raw resource folder
     * */
    private fun getSoundFileURI(soundFileName: String): Uri {
        return Uri.parse(
            RESOURCE_PATH + context.packageName
                    + "/" + RAW_FOLDER + "/" + soundFileName
        )
    }

    companion object {

        const val RAW_FOLDER = "raw"
        const val RESOURCE_PATH = "android.resource://"

        fun getNotificationController(context: Context) =
            NotificationChannelController(context.applicationContext)
    }

}

/**
 * Please don't change channel data in this file
 * */
object NotificationChannelConstant {

    var CHANNEL_DEFAULT_ID = "ANDROID_GENERAL_CHANNEL"
    val CHANNEL_DEFAULT_NAME = "Notifikasi"
    val CHANNEL_DEFAULT_DESCRIPTION = "Notifikasi"

    var CHANNEL_DESCRIPTION = "Tokopedia Ringtone"

    var Channel_DefaultSilent_Id = "Default_Channel"
    var Channel_DefaultSilent_Name = "Default"
    var Channel_DefaultSilent_DESCRIPTION = "Default Silent"


}

