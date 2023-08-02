package com.tokopedia.chatbot.chatbot2.view.util

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.util.view.ViewUtil
import com.tokopedia.unifycomponents.Toaster

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

fun View?.showToasterError(errorMessage: String, ctaText: String = "") {
    if (this == null) return

    if (ctaText.isEmpty()) {
        showToasterError(errorMessage)
    } else {
        showToasterErrorWithCta(errorMessage, ctaText)
    }
}

private fun View?.showToasterError(errorMessage: String) {
    Toaster.build(
        this ?: return,
        errorMessage,
        Toaster.LENGTH_SHORT,
        Toaster.TYPE_ERROR
    ).apply {
        show()
    }
}

private fun View?.showToasterErrorWithCta(errorMessage: String, ctaText: String) {
    Toaster.build(
        this ?: return,
        errorMessage,
        Toaster.LENGTH_SHORT,
        Toaster.TYPE_ERROR,
        ctaText
    ).apply {
        show()
    }
}

fun generateLeftMessageBackgroundWithoutCorner(view: View?): Drawable? {
    return ViewUtil.generateBackgroundWithShadow(
        view,
        com.tokopedia.unifyprinciples.R.color.Unify_GN100,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER,
        R.color.chatbot_dms_stroke,
        R.dimen.dp_chatbot_3
    )
}

fun generateRightMessageBackgroundWithoutCorner(view: View?): Drawable? {
    return ViewUtil.generateBackgroundWithShadow(
        view,
        com.tokopedia.unifyprinciples.R.color.Unify_GN100,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER,
        R.color.chatbot_dms_stroke,
        R.dimen.dp_chatbot_3
    )
}

fun generateLeftMessageBackground(
    view: View?,
    @ColorRes backgroundColor: Int = com.tokopedia.unifyprinciples.R.color.Unify_NN0,
    @ColorRes shadowColor: Int = com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
): Drawable? {
    return ViewUtil.generateBackgroundWithShadow(
        view,
        backgroundColor,
        R.dimen.dp_chatbot_0,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        shadowColor,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER
    )
}

fun generateRightMessageBackground(
    view: View?,
    @ColorRes backgroundColor: Int = R.color.chatbot_dms_right_chat_message_bg,
    @ColorRes shadowColor: Int = com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
): Drawable? {
    return ViewUtil.generateBackgroundWithShadow(
        view,
        backgroundColor,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_0,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        shadowColor,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER
    )
}



object ChatBackground {
    fun bindBackground(view: LinearLayout?, isSend: Boolean = true): Drawable? {
        if (isSend)
            return com.tokopedia.chatbot.util.ViewUtil.generateBackgroundWithShadow(
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
            return com.tokopedia.chatbot.util.ViewUtil.generateBackgroundWithShadow(
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
