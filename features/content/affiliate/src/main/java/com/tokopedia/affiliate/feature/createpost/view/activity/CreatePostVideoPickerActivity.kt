package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.videorecorder.main.VideoPickerActivity

/**
 * Created by isfaaghyth on 20/03/19.
 * github: @isfaaghyth
 */
class CreatePostVideoPickerActivity: VideoPickerActivity() {

    override fun onVideoDoneClicked() {
        super.onVideoDoneClicked()
    }

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, VideoPickerActivity::class.java)
        }
    }

}