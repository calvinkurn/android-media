package com.tokopedia.abstraction.common.utils.view

import android.app.AlertDialog
import android.content.Context
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.track.TrackApp

/**
 * @author ricoharisin
 */

object DialogForceLogout {
    private val TAG = DialogForceLogout::class.java.simpleName
    private const val IS_DIALOG_SHOWN_STORAGE = "IS_DIALOG_SHOWN_STORAGE"
    private const val IS_DIALOG_SHOWN = "IS_DIALOG_SHOWN"

    @JvmStatic
    fun createShow(context: Context, screenName: String?, listener: ActionListener?) {
        val alertDialog = create(context, listener, screenName)
        alertDialog.show()
        setIsDialogShown(context, true)
    }

    @JvmStatic
    fun showDialogUnify(
        context: Context,
        screenName: String = "",
        title: String = "",
        description: String = "",
        listener: UnifyActionListener?,
    ) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(title)
        dialog.setDescription(description)
        dialog.setPrimaryCTAText("Cek Informasi Lengkap")
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            setIsDialogShown(context, false)
            TrackApp.getInstance().gtm.sendGeneralEvent(
                "clickLogout",
                "force logout",
                "get session expired pop up",
                screenName
            )
            listener?.onPrimaryBtnClicked()
        }
        dialog.setSecondaryCTAText("Batal")
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            listener?.onSecondaryBtnClicked()
        }
        dialog.setOnDismissListener { listener?.onDismiss() }
        dialog.show()
        setIsDialogShown(context, true)
    }

    fun create(
        context: Context,
        listener: ActionListener?,
        screenName: String?
    ): AlertDialog {
        val dialog = AlertDialog.Builder(context)
        dialog.setMessage(R.string.title_session_expired)
        dialog.setPositiveButton(
            context.getString(R.string.title_ok)
        ) { dialog, which ->
            listener!!.onDialogClicked()
            dialog.dismiss()
            setIsDialogShown(context, false)
            val screen = screenName ?: ""
            TrackApp.getInstance().gtm.sendGeneralEvent(
                "clickLogout",
                "force logout",
                "get session expired pop up",
                screen
            )
        }
        dialog.setCancelable(false)
        return dialog.create()
    }

    fun setIsDialogShown(context: Context?, status: Boolean?) {
        val cache = LocalCacheHandler(context, IS_DIALOG_SHOWN_STORAGE)
        cache.putBoolean(IS_DIALOG_SHOWN, status)
        cache.applyEditor()
    }

    @JvmStatic
    fun isDialogShown(context: Context?): Boolean {
        val cache = LocalCacheHandler(context, IS_DIALOG_SHOWN_STORAGE)
        return cache.getBoolean(IS_DIALOG_SHOWN, false)
    }

    interface ActionListener {
        fun onDialogClicked()
    }

    interface UnifyActionListener {
        fun onPrimaryBtnClicked()
        fun onSecondaryBtnClicked()
        fun onDismiss()
    }

}