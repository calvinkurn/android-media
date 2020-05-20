package com.tokopedia.play.broadcaster.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.play.broadcaster.R
import org.jetbrains.annotations.TestOnly


/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity: BaseActivity() {

    companion object {

        const val PLAY_KEY_CHANNEL_ID = "channelId"

        @TestOnly
        fun newIntent(context: Context, channelId: String): Intent {
            return Intent(context, PlayBroadcastActivity::class.java)
                    .apply {
                        // TODO("ask @Rafiando for applink url")
                        data = Uri.parse("tokopedia://play-broadcaster/$channelId")
                    }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcaster)

        dummyCheckPermission() // TODO("testing only")

        val channelId = intent?.data?.lastPathSegment
        setupView(channelId)
    }

    private fun setupView(channelId: String?) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, getFragment(channelId))
                .commit()
    }

    private fun getFragment(channelId: String?): Fragment {
        return PlayBroadcastFragment.newInstance(channelId)
    }

    // TODO("testing only")
    private fun dummyCheckPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO),
                2334)
    }
}