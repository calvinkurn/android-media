package com.tokopedia.chatbot.view.util

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.unifycomponents.Toaster

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

fun View?.showToaster(message: String, ctaText: String = "") {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToaster(message)
    } else {
        showToasterWithCta(message, ctaText)
    }
}

private fun View?.showToaster(message: String) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_NORMAL
    ).apply {
        anchorView = this@showToaster
        show()
    }
}

private fun View?.showToasterWithCta(message: String, ctaText: String) {
    Toaster.build(
        this ?: return,
        message,
        Toaster.LENGTH_LONG,
        Toaster.TYPE_NORMAL,
        ctaText
    ).apply {
        anchorView = this@showToasterWithCta
        show()
    }
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
fun LinearLayout.setContainerBackground(bg: Drawable?) {
    val pl = paddingLeft
    val pt = paddingTop
    val pr = paddingRight
    val pb = paddingBottom
    background = bg
    setPadding(pl, pt, pr, pb)
}
