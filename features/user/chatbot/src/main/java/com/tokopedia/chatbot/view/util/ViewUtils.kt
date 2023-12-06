package com.tokopedia.chatbot.view.util

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.listener.ChatbotContract

fun View.isInDarkMode() : Boolean {
    val nightModeFlags: Int =  this.context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
}

fun ChatbotContract.View.isInDarkMode() : Boolean {
    val nightModeFlags: Int =  this.context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
}

object ChatBackground {
    fun bindBackground(view: LinearLayout?, isSend: Boolean = true): Drawable? {
        if (isSend)
        return ViewUtil.generateBackgroundWithShadow(
            view,
            R.color.chatbot_dms_right_message_bg,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
        ) else {
            return ViewUtil.generateBackgroundWithShadow(
                view,
                R.color.chatbot_dms_left_message_bg,
                R.dimen.dp_chatbot_0,
                R.dimen.dp_chatbot_20,
                R.dimen.dp_chatbot_20,
                R.dimen.dp_chatbot_20,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
                R.dimen.dp_chatbot_2,
                R.dimen.dp_chatbot_1,
                Gravity.CENTER
            )
        }

    }
}
