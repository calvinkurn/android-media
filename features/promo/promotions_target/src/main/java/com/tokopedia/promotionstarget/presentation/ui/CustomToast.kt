package com.tokopedia.promotionstarget.presentation.ui

import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.doOnPreDraw
import com.tokopedia.unifyprinciples.Typography


class CustomToast {

    companion object {

        private fun dpToPx(context: Context, dp: Int): Float {
            return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
        }

        fun show(activityContext: Context,
                 @NonNull text: String,
                 duration: Int = Toast.LENGTH_LONG,
                 bg: Int = R.drawable.t_promo_custom_toast_bg
        ) {

            val leftPadding = dpToPx(activityContext, 16).toInt()
            val topPadding = dpToPx(activityContext, 8).toInt()
            val topPaddingSingleLine = dpToPx(activityContext, 12).toInt()
            val bottomPaddingSingleLine = dpToPx(activityContext, 2).toInt()
            val textView = Typography(activityContext)
            textView.text = text
            textView.setPadding(leftPadding, topPadding, leftPadding, topPadding)

            textView.setTextColor(ContextCompat.getColor(activityContext, R.color.t_promo_toastColor))
            textView.setBackgroundResource(bg)
            textView.fontType = Typography.BODY_3
            val fm = FrameLayout(activityContext)
            fm.addView(textView)
            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.setMargins(leftPadding, 0, leftPadding, 0)
            textView.layoutParams = lp

            val toast = Toast(activityContext)
            toast.duration = duration
            toast.view = fm
            toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, leftPadding)
            textView.doOnPreDraw {
                if(textView.lineCount == 1){
                    textView.setPadding(leftPadding, topPaddingSingleLine, leftPadding, topPaddingSingleLine)
                    lp.setMargins(leftPadding, 0, leftPadding, bottomPaddingSingleLine)
                    textView.layoutParams = lp
                }
            }
            toast.show()
        }
    }

}