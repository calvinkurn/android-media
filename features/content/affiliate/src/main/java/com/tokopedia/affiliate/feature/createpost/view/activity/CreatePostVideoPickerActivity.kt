package com.tokopedia.affiliate.feature.createpost.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.videorecorder.main.VideoPickerActivity

/**
 * Created by isfaaghyth on 20/03/19.
 * github: @isfaaghyth
 */
class CreatePostVideoPickerActivity: VideoPickerActivity() {

    override fun onVideoDoneClicked() {
        val isImageExist = intent?.getBooleanExtra(CreatePostVideoPickerActivity.IMAGE_EXIST, false)?: false
        if (isImageExist) {
            //TODO(show dialog)
        } else {
            super.onVideoDoneClicked()
        }
    }

    companion object {
        private const val IMAGE_EXIST = "image_exist"
        fun getInstance(context: Context, isShouldDialog: Boolean): Intent {
            val intent = Intent(context, CreatePostVideoPickerActivity::class.java)
            intent.putExtra(IMAGE_EXIST, isShouldDialog)
            return intent
        }
    }

}