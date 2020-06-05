package com.tokopedia.talk_old.common.view

import android.content.Context
import android.view.View
import com.tokopedia.design.component.Dialog
import com.tokopedia.talk_old.R
import javax.inject.Inject

/**
 * @author by nisie on 9/15/18.
 */
class TalkDialog @Inject constructor() {

    fun createUnfollowTalkDialog(context: Context, alertDialog: Dialog, onOkListener:
    View.OnClickListener): Dialog {

        alertDialog.setTitle(context.getString(R.string.unfollow_talk_dialog_title))
        alertDialog.setDesc(context.getString(R.string.unfollow_talk_dialog_desc))
        alertDialog.setBtnCancel(context.getString(com.tokopedia.design.R.string.button_cancel))
        alertDialog.setBtnOk(context.getString(R.string.button_unfollow_talk))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener(onOkListener)

        return alertDialog
    }

    fun createFollowTalkDialog(context: Context, alertDialog: Dialog, onOkListener:
    View.OnClickListener): Dialog {

        alertDialog.setTitle(context.getString(R.string.follow_talk_dialog_title))
        alertDialog.setDesc(context.getString(R.string.follow_talk_dialog_desc))
        alertDialog.setBtnCancel(context.getString(com.tokopedia.design.R.string.button_cancel))
        alertDialog.setBtnOk(context.getString(R.string.button_follow_talk))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener(onOkListener)

        return alertDialog
    }

    fun createDeleteTalkDialog(context: Context, alertDialog: Dialog, onOkListener:
    View.OnClickListener): Dialog {

        alertDialog.setTitle(context.getString(R.string.delete_talk_dialog_title))
        alertDialog.setDesc(context.getString(R.string.delete_talk_dialog_desc))
        alertDialog.setBtnCancel(context.getString(com.tokopedia.design.R.string.button_cancel))
        alertDialog.setBtnOk(context.getString(com.tokopedia.design.R.string.button_delete))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener(onOkListener)


        return alertDialog
    }

    fun createDeleteCommentTalkDialog(context: Context, alertDialog: Dialog, onOkListener:
    View.OnClickListener): Dialog {


        alertDialog.setTitle(context.getString(R.string.delete_comment_talk_dialog_title))
        alertDialog.setDesc(context.getString(R.string.delete_comment_talk_dialog_desc))
        alertDialog.setBtnCancel(context.getString(com.tokopedia.design.R.string.button_cancel))
        alertDialog.setBtnOk(context.getString(com.tokopedia.design.R.string.button_delete))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener(onOkListener)


        return alertDialog
    }

}