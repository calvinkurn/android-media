package com.tokopedia.abstraction.common.utils.view

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.config.GlobalConfig
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
        screenName: String? = "",
        title: String = "",
        description: String = "",
        listener: UnifyActionListener?,
    ) {
        val dialog = DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(title)
        dialog.setDescription(description)
        dialog.setPrimaryCTAText(context.getString(com.tokopedia.abstraction.R.string.force_logout_primary_btn_text))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            TrackApp.getInstance().gtm.sendGeneralEvent(
                "clickLogout",
                "force logout",
                "get session expired pop up",
                screenName
            )
            listener?.onPrimaryBtnClicked()
        }
        dialog.setSecondaryCTAText(context.getString(com.tokopedia.abstraction.R.string.force_logout_secondary_btn_text))
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            listener?.onSecondaryBtnClicked()
        }
        dialog.setOnDismissListener {
            setIsDialogShown(context, false)
            listener?.onSecondaryBtnClicked()
        }
        dialog.show()
        setIsDialogShown(context, true)
    }

    fun create(
        context: Context,
        listener: ActionListener?,
        screenName: String?
    ): DialogUnify {
        val dialog = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(context.getString(com.tokopedia.abstraction.R.string.force_logout_general_title))
        dialog.setDescription(context.getString(com.tokopedia.abstraction.R.string.force_logout_general_description))
        val ctaText = if(GlobalConfig.isSellerApp()) {
            context.getString(com.tokopedia.abstraction.R.string.force_logout_general_seller_btn_text)
        } else {
            context.getString(com.tokopedia.abstraction.R.string.force_logout_general_btn_text)
        }
        dialog.setPrimaryCTAText(ctaText)
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            TrackApp.getInstance().gtm.sendGeneralEvent(
                "clickLogout",
                "force logout",
                "get session expired pop up",
                screenName
            )
        }
        dialog.setOnDismissListener {
            setIsDialogShown(context, false)
            listener?.onDialogClicked()
        }
        dialog.setCancelable(false)
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

    interface UnifyActionListener {
        fun onPrimaryBtnClicked()
        fun onSecondaryBtnClicked()
    }

}