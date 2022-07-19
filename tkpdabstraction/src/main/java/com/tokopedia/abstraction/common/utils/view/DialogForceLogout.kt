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
        screenName: String,
        listener: ActionListener?,
        title: String = "",
        description: String = "",
        url: String = ""
    ) {
        val alertDialog = createDialogWithCustomContent(context, listener, screenName, title, description, url)
        alertDialog.show()
        setIsDialogShown(context, true)
    }

    fun create(
        context: Context, listener: ActionListener?,
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

    fun createDialogWithCustomContent(
        context: Context,
        listener: ActionListener?,
        screenName: String = "",
        title: String = "",
        description: String = "",
        url: String = ""
    ): DialogUnify {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(title)
        dialog.setDescription(description)
        dialog.setPrimaryCTAText("Cek Informasi Lengkap")
        dialog.setPrimaryCTAClickListener {
            listener?.onDialogClicked()
            dialog.dismiss()
            setIsDialogShown(context, false)
            TrackApp.getInstance().gtm.sendGeneralEvent(
                "clickLogout",
                "force logout",
                "get session expired pop up",
                screenName
            )
//            RouteManager.route(
//                context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
//            )
        }
        dialog.setCancelable(false)
        dialog.setSecondaryCTAText("Batal")
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
        }
        return dialog
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
}