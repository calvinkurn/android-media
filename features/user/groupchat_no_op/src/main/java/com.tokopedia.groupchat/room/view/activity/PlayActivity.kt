package com.tokopedia.groupchat.room.view.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 11/02/19
 */
open class PlayActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun getCallingIntent(context: Context, channelId: String): Intent {
            return Intent()
        }

        @JvmStatic
        fun getCallingIntent(context: Context, channelId: String, useGCP: Boolean): Intent {
            return Intent()
        }

        val KICK_THRESHOLD_TIME = TimeUnit.MINUTES.toMillis(5)
    }
}