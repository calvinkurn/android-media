package com.tokopedia.play.broadcaster.view

import android.Manifest
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.play.broadcaster.R


/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcaster)

        dummyCheckPermission()
    }

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