package com.tokopedia.chatbot.view.util

import android.content.res.Configuration
import android.view.View
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