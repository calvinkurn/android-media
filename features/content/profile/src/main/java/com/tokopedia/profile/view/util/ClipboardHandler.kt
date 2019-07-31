package com.tokopedia.profile.view.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import com.tokopedia.profile.R

/**
 * @author by yfsx on 17/05/19.
 */
class ClipboardHandler {

    fun CreateMenuPopUp(context: Activity, v: View) {
        val popup = PopupMenu(context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.clipboard_copy, popup.menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            // TODO Auto-generated method stub
            if (item.itemId == R.id.action_copy) {
                CopyToClipboard(context, (v as TextView).text.toString())
                return@OnMenuItemClickListener false
            }
            false
        })
        popup.show()
    }

    fun CopyToClipboard(context: Activity, Text: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Tokopedia", Text)
        clipboard.primaryClip = clip
    }

}