package com.tokopedia.feedback_form.feedbackpage.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.LinearLayout
import com.tokopedia.feedback_form.R

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)

        window?.run {
            attributes.width = LinearLayout.LayoutParams.MATCH_PARENT
            attributes.height = LinearLayout.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}