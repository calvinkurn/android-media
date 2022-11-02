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
            val toast = Toast.makeText(activityContext, text, duration)
            toast.show()
        }
    }

}