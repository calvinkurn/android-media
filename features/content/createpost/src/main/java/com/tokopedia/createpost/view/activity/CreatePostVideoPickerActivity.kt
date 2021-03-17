package com.tokopedia.createpost.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.createpost.createpost.R
import com.tokopedia.design.component.Dialog
import com.tokopedia.imagepicker.common.listener.VideoPickerListener

/**
 * Created by isfaaghyth on 20/03/19.
 * github: @isfaaghyth
 */
class CreatePostVideoPickerActivity: BaseSimpleActivity(), VideoPickerListener, VideoPickerCallback {

    override fun onVideoDoneClicked() {
        val isImageExist = intent?.getBooleanExtra(IMAGE_EXIST, false)?: false

        if (isImageExist) {
            val dialog = Dialog(this, Dialog.Type.PROMINANCE)
            dialog.setTitle(getString(R.string.cp_title_update_post))
            dialog.setDesc(
                    getString(R.string.cp_message_update_choosen_video))
            dialog.setBtnCancel(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
            dialog.setBtnOk(getString(R.string.cp_continue))
            dialog.setOnOkClickListener{
                dialog.dismiss()
                // TODO: harusnya ini panggil onFinishPicked, tp bingung file nya dari mana
                super.onVideoDoneClicked()
            }
            dialog.setOnCancelClickListener{
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
        } else {
            // TODO: harusnya ini panggil onFinishPicked, tp bingung file nya dari mana
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

    private fun onFinishPicked(file: String) {
        val videos = arrayListOf<String>()
        videos.add(file)

        val intent = Intent()
        intent.putStringArrayListExtra(VIDEOS_RESULT, videos)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun getNewFragment(): Fragment? = null

}