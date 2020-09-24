package com.tokopedia.screenshot_observer.dialog

import android.content.Context
import android.view.View
import com.example.screenshot_observer.R
import com.tokopedia.dialog.DialogUnify
import kotlinx.android.synthetic.main.dialog_screenshot.view.*

class DialogAfterScreenshot {

    fun show(context: Context) {
        val dialog = DialogUnify(context, 0, DialogUnify.WITH_ILLUSTRATION)
        val child = View.inflate(context, R.layout.dialog_screenshot, null)
        setupChild(dialog, context, child)
        dialog.setContentView(child)
        dialog.setUnlockVersion()
        dialog.setChild(child)
    }

    private fun setupChild(dialog: DialogUnify, context: Context, child: View) {
        child.apply {




            btn_dismiss.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

}