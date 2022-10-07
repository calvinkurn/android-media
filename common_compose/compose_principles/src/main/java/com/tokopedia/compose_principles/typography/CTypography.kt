package com.tokopedia.compose_principles.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

/**
 * Created by yovi.putra on 28/09/22"
 * Project name: android-tokopedia-core
 **/

/**
 * Typography Compose Version
 * This class is Typography especially compose with Unify [Typography] behaviour
 * @param text the text value with String Type
 * @param type the FontType refer to [TextUnifyType]
 * @param weight the FontWeight refer to [TextUnifyWeight]
 * @param textStyle - the Style configuration for the text such as color, font, line height etc.
 * @param overflow - How visual overflow should be handled
 * @param isFontTypeOpenSauceOne - unify font new type if true
 * @param onTextLayout - Callback that is executed when a new text layout is calculated.
 * A TextLayoutResult object that callback provides contains paragraph information,
 * size of the text, baselines and other details.
 * The callback can be used to add additional decoration or functionality to the text.
 * For example, to draw selection around the text.
 **/
@Composable
fun CTypography(
    modifier: Modifier = Modifier,
    text: String,
    type: TextUnifyType,
    weight: TextUnifyWeight = TextUnifyWeight.Regular,
    textStyle: TextStyle = TextStyle(
        fontFamily = fontOpenSourceOneRegular,
        color = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN950)
    ),
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    isFontTypeOpenSauceOne: Boolean = true,
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
 * Typography Compose Version
 * This class is Typography especially compose with Unify [Typography] behaviour
 * @param text the text value with AnnotationString Type
 * @param type the FontType refer to [TextUnifyType]
 * @param weight the FontWeight refer to [TextUnifyWeight]
 * @param textStyle - the Style configuration for the text such as color, font, line height etc.
 * @param overflow - How visual overflow should be handled
 * @param isFontTypeOpenSauceOne - unify font new type if true
 * @param onTextLayout - Callback that is executed when a new text layout is calculated.
 * A TextLayoutResult object that callback provides contains paragraph information,
 * size of the text, baselines and other details.
 * The callback can be used to add additional decoration or functionality to the text.
 * For example, to draw selection around the text.
 **/
@Composable
fun CTypography(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    type: TextUnifyType,
    weight: TextUnifyWeight = TextUnifyWeight.Regular,
    textStyle: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    isFontTypeOpenSauceOne: Boolean = true,
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

@Preview
@Composable
fun CTypographyRegularPreview() {
    LazyColumn(modifier = Modifier
        .background(colorResource(com.tokopedia.unifyprinciples.R.color.Unify_Background))
    ) {
        item {
            CTypography(
                text = "Regular",
                type = TextUnifyType.Heading1,
                isFontTypeOpenSauceOne = false
            )
        }
        items(TextUnifyType.values()) {
            Column {
                CTypography(
                    text = it.name,
                    type = it,
                    isFontTypeOpenSauceOne = false
                )

                CTypography(
                    text = it.name,
                    type = it,
                    isFontTypeOpenSauceOne = true
                )
            }

            Divider()
        }
    }
}

@Preview
@Composable
fun CTypographyBoldPreview() {
    LazyColumn(modifier = Modifier
        .background(colorResource(com.tokopedia.unifyprinciples.R.color.Unify_Background))
    ) {
        item {
            CTypography(
                text = "Bold",
                type = TextUnifyType.Heading1,
                isFontTypeOpenSauceOne = false
            )
        }
        items(TextUnifyType.values()) {
            Column {
                CTypography(
                    text = it.name,
                    type = it,
                    weight = TextUnifyWeight.Bold,
                    isFontTypeOpenSauceOne = false
                )

                CTypography(
                    text = it.name,
                    type = it,
                    weight = TextUnifyWeight.Bold,
                    isFontTypeOpenSauceOne = true
                )
            }

            Divider()
        }
    }
}