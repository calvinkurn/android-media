package com.tokopedia.gamification.giftbox.presentation.views

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography


object CustomToast {

    fun show(activityContext: Context?, @NonNull text: String, duration: Int = Toast.LENGTH_LONG, isError: Boolean = false) {
        if (activityContext is Activity) {

            val leftPadding = 16.toPx()
            val topPadding = 8.toPx()
            val textView = Typography(activityContext)
            textView.text = text
            textView.setPadding(leftPadding, topPadding, leftPadding, topPadding)
            textView.setTextColor(ContextCompat.getColor(activityContext, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            if (isError) {
                textView.setBackgroundResource(com.tokopedia.gamification.R.drawable.gf_custom_toast_error_bg)
            } else {
                textView.setBackgroundResource(com.tokopedia.gamification.R.drawable.gf_custom_toast_bg)
            }
            textView.fontType = Typography.BODY_3
            val toast = Toast(activityContext)
            toast.duration = duration
            toast.view = textView
            toast.show()
        }
    }

}