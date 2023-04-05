package com.tokopedia.common_compose.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.getSpans

/**
 * This is a sealed class that contains all of the different ways text can be presented to the UI.
 */
sealed class UiText {
    data class StringText(val value: String?) : UiText()

    data class ResourceText(@StringRes val value: Int?) : UiText()
}

/**
 * Evaluates the value of this [UiText] based on its type.
 *
 * @param[context] If necessary, use this to evaluate a string resource.
 */
fun UiText.getString(context: Context): String? {
    return when (this) {
        is UiText.StringText -> this.value
        is UiText.ResourceText -> this.value?.let { context.getString(it) }
    }
}

/**
 * A helper function that allows to get strings from a [Composable] context.
 */
@Composable
fun UiText.getString(): String? {
    return this.getString(LocalContext.current)
}

/**
 * A helper function to convert html tags to [AnnotatedString].
 */
@Composable
fun CharSequence.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline
    ),
    colorMapping: Map<Color, Color> = emptyMap()
): AnnotatedString {
    return if (this is Spanned) {
        this.toAnnotatedString(urlSpanStyle, colorMapping)
    } else {
        buildAnnotatedString {
            append(this@toAnnotatedString.toString())
        }
    }
}

/**
 * A helper function to convert html tags to [AnnotatedString].
 */
@Composable
fun Spanned.toAnnotatedString(
    urlSpanStyle: SpanStyle = SpanStyle(
        color = Color.Blue,
        textDecoration = TextDecoration.Underline
    ),
    colorMapping: Map<Color, Color> = emptyMap()
): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString.toString())
        val urlSpans = getSpans<URLSpan>()
        val styleSpans = getSpans<StyleSpan>()
        val colorSpans = getSpans<ForegroundColorSpan>()
        val underlineSpans = getSpans<UnderlineSpan>()
        val strikethroughSpans = getSpans<StrikethroughSpan>()
        urlSpans.forEach { urlSpan ->
            val start = getSpanStart(urlSpan)
            val end = getSpanEnd(urlSpan)
            addStyle(urlSpanStyle, start, end)
            addStringAnnotation("url", urlSpan.url, start, end) // NON-NLS
        }
        colorSpans.forEach { colorSpan ->
            val start = getSpanStart(colorSpan)
            val end = getSpanEnd(colorSpan)
            addStyle(
                SpanStyle(
                    color = colorMapping.getOrElse(Color(colorSpan.foregroundColor)) {
                        Color(
                            colorSpan.foregroundColor
                        )
                    }
                ),
                start,
                end
            )
        }
        styleSpans.forEach { styleSpan ->
            val start = getSpanStart(styleSpan)
            val end = getSpanEnd(styleSpan)
            when (styleSpan.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    start,
                    end
                )
            }
        }
        underlineSpans.forEach { underlineSpan ->
            val start = getSpanStart(underlineSpan)
            val end = getSpanEnd(underlineSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
        }
        strikethroughSpans.forEach { strikethroughSpan ->
            val start = getSpanStart(strikethroughSpan)
            val end = getSpanEnd(strikethroughSpan)
            addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), start, end)
        }
    }
}
