package com.tokopedia.sessioncommon.view.admin.dialog

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sessioncommon.R

class LocationAdminDialog(context: Context?, dismissListener: (() -> Unit)? = null) {

    companion object {
        private const val IMAGE_URL = "https://ecs7.tokopedia.net/android/others/ic_location_admin_info.png"
    }

    private var dialog: DialogUnify? = null

    init {
        context?.let {
            val gn500Color = com.tokopedia.unifyprinciples.R.color.Unify_GN500.toString()
            dialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION).apply {
                setTitle(it.getString(R.string.dialog_location_admin_title))
                setDescription(
                    MethodChecker.fromHtml(it.getString(R.string.dialog_location_admin_description, gn500Color)))
                setPrimaryCTAText(it.getString(R.string.dialog_location_admin_primary_cta))
                setPrimaryCTAClickListener { dismiss() }
                setOnDismissListener { dismissListener?.invoke() }
                setImageUrl(IMAGE_URL)
            }
        }
    }

    fun show() {
        dialog?.show()
    }
}
