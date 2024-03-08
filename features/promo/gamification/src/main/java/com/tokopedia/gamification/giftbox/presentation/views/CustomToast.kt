package com.tokopedia.gamification.giftbox.presentation.views

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull

object CustomToast {

    fun show(activityContext: Context?, @NonNull text: String, duration: Int = Toast.LENGTH_LONG, isError: Boolean = false) {
        if (activityContext is Activity) {
            val toast = Toast.makeText(activityContext, text, duration)
            toast.show()
        }
    }

}
