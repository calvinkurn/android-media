package com.tokopedia.groupchat.room.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.groupchat.R
import com.tokopedia.videoplayer.utils.RepeatMode
import com.tokopedia.videoplayer.utils.sendViewToBack
import com.tokopedia.videoplayer.view.player.TkpdVideoPlayer
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        sendViewToBack(playerView)

        val sourceMedia = "https://scontent-sin6-1.cdninstagram.com/vp/cb4297650b392eab52095d911a1a17dc/5D1C8FA4/t50.12441-16/53306725_332584844027284_3716503313000746737_n.mp4?_nc_ht=scontent-sin6-1.cdninstagram.com"
        Toast.makeText(this, intent?.extras?.getString(EXTRA_CHANNEL_URL), Toast.LENGTH_LONG).show()
        TkpdVideoPlayer.Builder()
                .transaction(R.id.playerView, supportFragmentManager)
                .videoSource(sourceMedia)
                .repeatMode(RepeatMode.REPEAT_MODE_ALL)
                .build()
    }

    companion object {

        val EXTRA_CHANNEL_URL = "CHANNEL_URL"

        @JvmStatic
        fun getCallingIntent(context: Context, url: String): Intent {
            val intent = Intent(context, TestActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_CHANNEL_URL, url)
            intent.putExtras(bundle)
            return intent
        }

    }

    object DeepLickIntents {

        @JvmStatic
        @DeepLink("tokopedia://groupchat_test/{url}")
        fun getCallingTaskStack(context: Context, extras: Bundle): Intent {
            val url = extras.getString("url", "")
            return getCallingIntent(context, url)
        }

    }

}