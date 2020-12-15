package com.tokopedia.gamification.giftbox.presentation.views

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.tokopedia.gamification.R
import com.tokopedia.unifyprinciples.Typography


class CustomToast {

    companion object {

        private fun dpToPx(context: Context, dp: Int): Float {
            return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
        }

        fun show(activityContext: Context, @NonNull text: String, duration: Int = Toast.LENGTH_LONG, isError: Boolean = false) {
            if (activityContext is Activity) {

                val leftPadding = dpToPx(activityContext, 16).toInt()
                val topPadding = dpToPx(activityContext, 8).toInt()
                val textView = Typography(activityContext)
                textView.text = text
                textView.setPadding(leftPadding, topPadding, leftPadding, topPadding)
                textView.setTextColor(ContextCompat.getColor(activityContext, R.color.Unify_N0))
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

}