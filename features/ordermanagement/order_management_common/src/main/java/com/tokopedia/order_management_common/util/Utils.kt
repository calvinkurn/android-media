package com.tokopedia.order_management_common.util

import android.graphics.Typeface
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.HapticFeedbackConstants
import android.view.View
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.order_management_common.constants.OrderManagementConstants.STRING_BUTTON_TYPE_ALTERNATE
import com.tokopedia.order_management_common.constants.OrderManagementConstants.STRING_BUTTON_TYPE_MAIN
import com.tokopedia.order_management_common.constants.OrderManagementConstants.STRING_BUTTON_TYPE_TRANSACTION
import com.tokopedia.order_management_common.constants.OrderManagementConstants.STRING_BUTTON_VARIANT_FILLED
import com.tokopedia.order_management_common.constants.OrderManagementConstants.STRING_BUTTON_VARIANT_GHOST
import com.tokopedia.order_management_common.constants.OrderManagementConstants.STRING_BUTTON_VARIANT_TEXT_ONLY
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

private const val ROTATE_ICON_DURATION = 250L
private const val ROTATE_0 = 0F
private const val ROTATE_180 = 180F

fun View.generateHapticFeedback() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}

fun mapButtonVariant(variantString: String): Int {
    return when (variantString) {
        STRING_BUTTON_VARIANT_FILLED -> UnifyButton.Variant.FILLED
        STRING_BUTTON_VARIANT_GHOST -> UnifyButton.Variant.GHOST
        STRING_BUTTON_VARIANT_TEXT_ONLY -> UnifyButton.Variant.TEXT_ONLY
        else -> UnifyButton.Variant.FILLED
    }
}

fun mapButtonType(typeString: String): Int {
    return when (typeString) {
        STRING_BUTTON_TYPE_ALTERNATE -> UnifyButton.Type.ALTERNATE
        STRING_BUTTON_TYPE_MAIN -> UnifyButton.Type.MAIN
        STRING_BUTTON_TYPE_TRANSACTION -> UnifyButton.Type.TRANSACTION
        else -> UnifyButton.Type.MAIN
    }
}


fun String.stripLastDot(): String {
    return removeSuffix(".")
}

fun composeItalicNote(productNote: String): SpannableString {
    return SpannableString(productNote).apply {
        setSpan(StyleSpan(Typeface.ITALIC), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

fun IconUnify.rotateBackIcon() {
    animate().rotation(ROTATE_0).duration = ROTATE_ICON_DURATION
}

fun IconUnify.rotateIcon() {
    animate().rotation(ROTATE_180).duration = ROTATE_ICON_DURATION
}

inline fun <T: Any> runSafely(runnable: () -> T?): T? {
    return try {
        runnable()
    } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
        null
    }
}

fun CardUnify.setupCardDarkMode(){
    cardType = if (context.isDarkMode()) {
        CardUnify.TYPE_BORDER
    } else {
        CardUnify.TYPE_SHADOW
    }
}
