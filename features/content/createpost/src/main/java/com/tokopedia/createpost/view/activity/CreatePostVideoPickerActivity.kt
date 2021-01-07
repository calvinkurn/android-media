package com.tokopedia.createpost.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.createpost.createpost.R
import com.tokopedia.design.component.Dialog
import com.tokopedia.videorecorder.main.VideoPickerActivity

/**
 * Created by isfaaghyth on 20/03/19.
 * github: @isfaaghyth
 */
class CreatePostVideoPickerActivity: VideoPickerActivity() {

    override fun onVideoDoneClicked() {
        val isImageExist = intent?.getBooleanExtra(IMAGE_EXIST, false)?: false

        if (isImageExist) {
            val dialog = Dialog(this, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.cp_title_update_post))
            dialog.setDesc(
                    getString(R.string.cp_message_update_choosen_video))
            dialog.setBtnCancel(getString(R.string.cp_cancel))
            dialog.setBtnOk(getString(R.string.cp_continue))
            dialog.setOnOkClickListener{
                dialog.dismiss()
                super.onVideoDoneClicked()
            }
            dialog.setOnCancelClickListener{
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
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