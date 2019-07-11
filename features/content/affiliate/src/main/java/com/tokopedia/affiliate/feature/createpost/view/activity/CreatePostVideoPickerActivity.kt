package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.affiliate.R
import com.tokopedia.design.component.Dialog
import com.tokopedia.videorecorder.main.VideoPickerActivity

/**
 * Created by isfaaghyth on 20/03/19.
 * github: @isfaaghyth
 */
class CreatePostVideoPickerActivity: VideoPickerActivity() {

    companion object {
        private const val IMAGE_EXIST = "image_exist"
        fun getInstance(context: Context, isShouldDialog: Boolean): Intent {
            val intent = Intent(context, CreatePostVideoPickerActivity::class.java)
            intent.putExtra(IMAGE_EXIST, isShouldDialog)
            return intent
        }
    }

}