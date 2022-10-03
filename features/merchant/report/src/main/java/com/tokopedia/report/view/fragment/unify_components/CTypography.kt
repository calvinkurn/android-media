package com.tokopedia.report.view.fragment.unify_components

import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import com.tokopedia.report.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography.Companion.isFontTypeOpenSauceOne
import java.lang.Integer.min

/**
 * Created by yovi.putra on 28/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun CTypography(
    modifier: Modifier = Modifier,
    text: String,
    type: TextUnifyType,
    weight: TextUnifyWeight = TextUnifyWeight.Regular,
    fontStyle: FontStyle? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    isFontTypeOpenSauceOne: Boolean = true,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {

    val fontSize = getFontSize(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type
    )
    val letterSpacing = getLetterSpacing(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type,
        default = textStyle.letterSpacing.value
    )
    val fontFamily = getFontFamily(
        type = type,
        weight = weight,
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne
    )

    Text(
        modifier = modifier.setPaddingOpenSauceOne(type = type),
        text = text,
        fontStyle = fontStyle,
        style = textStyle.copy(
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            fontFamily = fontFamily
        ),
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = onTextLayout
    )
}


@Composable
fun CTypography(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    type: TextUnifyType,
    weight: TextUnifyWeight = TextUnifyWeight.Regular,
    fontStyle: FontStyle? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    isFontTypeOpenSauceOne: Boolean = true,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {

    val fontSize = getFontSize(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type
    )
    val letterSpacing = getLetterSpacing(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type,
        default = textStyle.letterSpacing.value
    )
    val fontFamily = getFontFamily(
        type = type,
        weight = weight,
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne
    )

    Text(
        modifier = modifier.setPaddingOpenSauceOne(type = type),
        text = text,
        fontStyle = fontStyle,
        style = textStyle.copy(
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            fontFamily = fontFamily
        ),
        maxLines = maxLines,
        overflow = overflow,
        onTextLayout = onTextLayout
    )
}

private fun getFontSize(
    isFontTypeOpenSauceOne: Boolean,
    type: TextUnifyType
): TextUnit = if (isFontTypeOpenSauceOne) {
    type.fontSize
} else {
    type.openSourceSize
}

private fun getLetterSpacing(
    isFontTypeOpenSauceOne: Boolean,
    type: TextUnifyType,
    default: Float
): TextUnit = if (isFontTypeOpenSauceOne) {
    when (type) {
        TextUnifyType.Heading1 -> (-0.0083f).sp
        TextUnifyType.Heading2 -> (-0.005f).sp
        TextUnifyType.Heading3 -> (-0.0055f).sp
        TextUnifyType.Display1 -> (0.00625f).sp
        TextUnifyType.Display2 -> (0.01428f).sp
        TextUnifyType.Display3 -> (0.00833f).sp
        TextUnifyType.Display3Uppercase -> (0.025f).sp
        TextUnifyType.Paragraph1 -> (0.00625f).sp
        TextUnifyType.Paragraph2 -> (0.01428f).sp
        TextUnifyType.Paragraph3 -> (0.00833f).sp
        TextUnifyType.Small -> (0.01f).sp
        else -> if (default.isNaN()) 0.sp else default.sp
    }
} else {
    when (type) {
        TextUnifyType.Heading1 -> (-0.013f).sp
        TextUnifyType.Heading2, TextUnifyType.Heading3 -> (-0.01f).sp
        else -> 0.sp
    }
}

private fun getFontFamily(
    type: TextUnifyType,
    weight: TextUnifyWeight,
    isFontTypeOpenSauceOne: Boolean
): FontFamily = if (isFontTypeOpenSauceOne) {
    val boldType = listOf(
        TextUnifyType.Heading1,
        TextUnifyType.Heading2,
        TextUnifyType.Heading3,
        TextUnifyType.Heading4,
        TextUnifyType.Heading5,
        TextUnifyType.Heading6,
        TextUnifyType.Display3
    )
    val isBold = boldType.contains(type) || weight is TextUnifyWeight.Bold

    if (isBold) {
        fontOpenSourceOneExtraBold
    } else {
        fontOpenSourceOneRegular
    }
} else {
    when (type) {
        TextUnifyType.Body1,
        TextUnifyType.Body2,
        TextUnifyType.Body3,
        TextUnifyType.Display1,
        TextUnifyType.Display2,
        TextUnifyType.Display3,
        TextUnifyType.Paragraph1,
        TextUnifyType.Paragraph2,
        TextUnifyType.Paragraph3,
        TextUnifyType.Small ->
            if (weight is TextUnifyWeight.Regular) {
                fontRobotoRegular
            } else {
                fontRobotoBold
            }
        else -> fontNunitoSansExtraBold
    }
}

private val fontOpenSourceOneExtraBold = FontFamily(Font(R.font.nunito_sans_extra_bold))
private val fontOpenSourceOneRegular = FontFamily(Font(R.font.open_sauce_one_regular))
private val fontRobotoRegular = FontFamily(Font(R.font.roboto_regular))
private val fontRobotoBold = FontFamily(Font(R.font.roboto_bold))
private val fontNunitoSansExtraBold = FontFamily(Font(R.font.nunito_sans_extra_bold))

private fun Modifier.setPaddingOpenSauceOne(type: TextUnifyType): Modifier {
    return when (type) {
        TextUnifyType.Paragraph1 -> padding(top = 3.dp, bottom = 3.dp)
        TextUnifyType.Paragraph2 -> padding(top = 2.dp, bottom = 2.dp)
        TextUnifyType.Paragraph3 -> padding(top = 1.5f.dp, bottom = 1.5f.dp)
        else -> this
    }
}

/**
 * Load a styled string resource with formatting.
 *
 * @param id the resource identifier
 * @param formatArgs the format arguments
 * @return the string data associated with the resource
 */
@Composable
fun annotatedStringResource(@StringRes id: Int, vararg formatArgs: Any): AnnotatedString {
    val text = stringResource(id, *formatArgs)
    val spanned = remember(text) {
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    return remember(spanned) {
        buildAnnotatedString {
            append(spanned.toString())
            spanned.getSpans(0, spanned.length, Any::class.java).forEach { span ->
                val start = spanned.getSpanStart(span)
                val end = spanned.getSpanEnd(span)
                when (span) {
                    is StyleSpan -> when (span.style) {
                        Typeface.BOLD ->
                            addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                        Typeface.ITALIC ->
                            addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                        Typeface.BOLD_ITALIC ->
                            addStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                ),
                                start,
                                end,
                            )
                    }
                    is UnderlineSpan ->
                        addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
                    is ForegroundColorSpan ->
                        addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)
                }
            }
        }
    }
}

@Preview
@Composable
fun CTypographyPreview() {

    Column {
        CTypography(
            text = "Hello World",
            type = TextUnifyType.Body1,
            isFontTypeOpenSauceOne = false
        )

        CTypography(
            text = "Hello World",
            type = TextUnifyType.Body1,
            isFontTypeOpenSauceOne = true
        )

        CTypography(
            text = annotatedStringResource(id = R.string.product_report_see_all_types),
            type = TextUnifyType.Body1,
            isFontTypeOpenSauceOne = true
        )
    }
}