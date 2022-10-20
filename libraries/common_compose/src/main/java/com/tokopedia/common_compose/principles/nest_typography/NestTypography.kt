package com.tokopedia.common_compose.principles.nest_typography

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

/**
 * Created by yovi.putra on 28/09/22"
 * Project name: android-tokopedia-core
 **/

/**
 * NestText Compose Version
 * This class is NestText especially compose with Unify [NestTypography] behaviour
 * @param text the text value with String Type
 * @param type the FontType refer to [NestTypographyType]
 * @param weight the FontWeight refer to [NestTypographyWeight]
 * @param textStyle - the Style configuration for the text such as color, font, line height etc.
 * @param overflow - How visual overflow should be handled
 * @param isFontTypeOpenSauceOne - unify font new type if true
 * @param onTextLayout - Callback that is executed when a new text layout is calculated.
 * A TextLayoutResult object that callback provides contains paragraph information,
 * size of the text, baselines and other details.
 * The callback can be used to add additional decoration or functionality to the text.
 * For example, to draw selection around the text.
 **/
@Deprecated("Please use new component", ReplaceWith("com.tokopedia.common_compose.principles: NestTypography"))
@Composable
fun NestTypography(
    modifier: Modifier = Modifier,
    text: String,
    type: NestTypographyType,
    weight: NestTypographyWeight = NestTypographyWeight.Regular,
    textStyle: TextStyle = TextStyle(
        fontFamily = fontOpenSourceOneRegular,
        color = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN950)
    ),
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    isFontTypeOpenSauceOne: Boolean = NestTextFontConfig.isFontTypeOpenSauceOne,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {

    val fontSize = getFontSize(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type,
        style = textStyle
    )
    val letterSpacing = getLetterSpacing(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type,
        default = textStyle.letterSpacing.value
    )
    val fontFamily = getFontFamily(
        type = type,
        weight = weight,
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
    )

    Text(
        modifier = modifier.setPaddingOpenSauceOne(type = type),
        text = text,
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

/**
 * NestText Compose Version
 * This class is NestText especially compose with Unify [NestTypography] behaviour
 * @param text the text value with AnnotationString Type
 * @param type the FontType refer to [NestTypographyType]
 * @param weight the FontWeight refer to [NestTypographyWeight]
 * @param textStyle - the Style configuration for the text such as color, font, line height etc.
 * @param overflow - How visual overflow should be handled
 * @param isFontTypeOpenSauceOne - unify font new type if true
 * @param onTextLayout - Callback that is executed when a new text layout is calculated.
 * A TextLayoutResult object that callback provides contains paragraph information,
 * size of the text, baselines and other details.
 * The callback can be used to add additional decoration or functionality to the text.
 * For example, to draw selection around the text.
 **/
@Deprecated("Please use new component", ReplaceWith("com.tokopedia.common_compose.principles: NestTypography"))
@Composable
fun NestTypography(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    type: NestTypographyType,
    weight: NestTypographyWeight = NestTypographyWeight.Regular,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    isFontTypeOpenSauceOne: Boolean = NestTextFontConfig.isFontTypeOpenSauceOne,
    onTextLayout: (TextLayoutResult) -> Unit = {}
) {

    val fontSize = getFontSize(
        isFontTypeOpenSauceOne = isFontTypeOpenSauceOne,
        type = type,
        style = textStyle
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