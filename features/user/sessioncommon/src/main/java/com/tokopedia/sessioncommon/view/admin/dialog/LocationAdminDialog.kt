package com.tokopedia.sessioncommon.view.admin.dialog

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sessioncommon.R

class LocationAdminDialog(context: Context?, dismissListener: (() -> Unit)? = null) {

    companion object {
        private const val IMAGE_URL = TokopediaImageUrl.LocationAdminDialog_IMAGE_URL
    }

    private var dialog: DialogUnify? = null

    init {
        context?.let {
            val gn500Color = getColorHexString(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
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

    private fun getColorHexString(context: Context?, idColor: Int): String {
        return try {
            val colorHexInt = context?.let { ContextCompat.getColor(it, idColor) }
            val startIndexHexString = 2

            val colorToHexString = colorHexInt?.let {
                Integer.toHexString(it).uppercase().substring(startIndexHexString)
            }
            return "#$colorToHexString"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
