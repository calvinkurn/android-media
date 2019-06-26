package com.tokopedia.groupchat.room.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
        val sourceMedia = "https://scontent-sin6-2.cdninstagram.com/vp/23547017e19e62618f3ae1cf42ba4e41/5D14312A/t50.12441-16/50237559_1201746266639073_6724633886427853004_n.mp4?_nc_ht=scontent-sin6-2.cdninstagram.com"
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