package com.tokopedia.autocompletecomponent.unify.compose_component

import android.text.TextUtils
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyCta
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyImage
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyLabel
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyTitle
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import java.util.*

@Composable
internal fun AutoCompleteLeftIcon(item: SuggestionUnifyImage) {
    ConstraintLayout(
        modifier = Modifier.size(32.dp)
    ) {
        val (iconTitle, iconSubtitle) = createRefs()
        var imageModifier = Modifier
            .constrainAs(iconTitle) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .clip(RoundedCornerShape(item.radius.toIntOrZero().dp))
        if (item.isBorder) {
            imageModifier = imageModifier.border(
                1.dp,
                NestTheme.colors.NN._300,
                RoundedCornerShape(item.radius.toIntOrZero().dp)
            )
        }
        NestImage(
            source = ImageSource.Remote(
                source = item.imageUrl
            ),
            modifier = imageModifier
        )
        if (item.iconImageUrl.isNotBlank()) {
            NestImage(
                source = ImageSource.Remote(
                    source = item.iconImageUrl
                ),
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(iconSubtitle) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Composable
internal fun AutoCompleteLeftIconEducation(item: SuggestionUnifyImage) {
    ConstraintLayout(
        modifier = Modifier.size(24.dp)
    ) {
        val (iconTitle, iconSubtitle) = createRefs()
        var imageModifier = Modifier
            .constrainAs(iconTitle) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .clip(RoundedCornerShape(item.radius.toIntOrZero().dp))
        if (item.isBorder) {
            imageModifier = imageModifier.border(
                1.dp,
                NestTheme.colors.NN._300,
                RoundedCornerShape(item.radius.toIntOrZero().dp)
            )
        }
        NestImage(
            source = ImageSource.Remote(
                source = item.imageUrl
            ),
            modifier = imageModifier
        )
        if (item.iconImageUrl.isNotBlank()) {
            NestImage(
                source = ImageSource.Remote(
                    source = item.iconImageUrl
                ),
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(iconSubtitle) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Composable
internal fun AutoCompleteDescription(
    title: SuggestionUnifyTitle,
    searchTerm: String,
    isAds: Boolean,
    subtitle: SuggestionUnifyTitle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        AutoCompleteTitle(title, searchTerm, isAds)
        if (subtitle.text.isNotBlank()) {
            AutoCompleteSubtitle(subtitle)
        }
    }
}

@Composable
private fun AutoCompleteTitle(
    title: SuggestionUnifyTitle,
    searchTerm: String,
    isAds: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isAds) {
            NestTypography(
                text = "Ad",
                textStyle = NestTheme.typography.small.copy(
                    color = NestTheme.colors.NN._600,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.width(4.dp))

            NestTypography(
                text = "•",
                textStyle = NestTheme.typography.small.copy(
                    color = NestTheme.colors.NN._500,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.width(4.dp))
        }

        if (title.iconImageUrl.isNotBlank()) {
            NestImage(
                source = ImageSource.Remote(
                    source = title.iconImageUrl
                ),
                modifier = Modifier
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        val annotatedTitle = getAnnotatedTitle(
            title = title,
            searchTerm = searchTerm
        )
        NestTypography(
            text = annotatedTitle,
            textStyle = NestTheme.typography.display2.copy(color = NestTheme.colors.NN._950)
        )
    }
}

@Composable
private fun getAnnotatedTitle(
    title: SuggestionUnifyTitle,
    searchTerm: String
): AnnotatedString {
    val searchQueryStartIndexInKeyword = setSearchQueryStartIndexInKeyword(title.text, searchTerm)
    val boldSpanStyle = SpanStyle(
        fontStyle = NestTheme.typography.heading5.fontStyle,
        fontFamily = NestTheme.typography.heading5.fontFamily,
        fontWeight = NestTheme.typography.heading5.fontWeight,
        fontSize = NestTheme.typography.heading5.fontSize,
        color = NestTheme.typography.heading5.color
    )
    if (searchQueryStartIndexInKeyword == -1 || title.typeIsBold()) {
        return getAnnotatedTitleFullBold(title, boldSpanStyle)
    }
    return getAnnotatedTitleAdaptive(
        title,
        searchTerm,
        searchQueryStartIndexInKeyword,
        boldSpanStyle
    )
}

@Composable
private fun getAnnotatedTitleFullBold(
    title: SuggestionUnifyTitle,
    boldSpanStyle: SpanStyle
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            boldSpanStyle
        ) {
            append(title.text)
        }
    }
}

@Composable
private fun getAnnotatedTitleAdaptive(
    title: SuggestionUnifyTitle,
    searchTerm: String,
    searchQueryStartIndexInKeyword: Int,
    boldSpanStyle: SpanStyle
): AnnotatedString {
    val starTitle = title.text.substring(0, searchQueryStartIndexInKeyword)
    val unboldedTitle = title.text.substring(
        searchQueryStartIndexInKeyword,
        searchQueryStartIndexInKeyword + searchTerm.length
    )
    val endTitle = title.text.substring(searchQueryStartIndexInKeyword + searchTerm.length)
    return buildAnnotatedString {
        withStyle(
            boldSpanStyle
        ) {
            append(starTitle)
        }

        append(unboldedTitle)

        withStyle(
            boldSpanStyle
        ) {
            append(endTitle)
        }
    }
}

private fun setSearchQueryStartIndexInKeyword(titleText: String, searchTerm: String): Int {
    return if (!TextUtils.isEmpty(searchTerm)) {
        titleText.lowercase(Locale.getDefault())
            .indexOf(searchTerm.lowercase(Locale.getDefault()))
    } else {
        -1
    }
}

@Composable
private fun AutoCompleteSubtitle(subtitle: SuggestionUnifyTitle) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (subtitle.iconImageUrl.isNotBlank()) {
            NestImage(
                source = ImageSource.Remote(
                    source = subtitle.iconImageUrl
                ),
                modifier = Modifier
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        NestTypography(
            text = subtitle.text,
            textStyle = NestTheme.typography.display3.copy(color = NestTheme.colors.NN._600)
        )
    }
}

@Composable
internal fun AutoCompleteRightIconCta(item: SuggestionUnifyCta, onItemClicked: () -> Unit = {}) {
    NestImage(
        source = ImageSource.Remote(
            source = item.imageUrl
        ),
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable {
                onItemClicked()
            },
        alignment = Alignment.Center
    )
}

@Composable
internal fun AutoCompleteRightLabel(item: SuggestionUnifyLabel, onItemClicked: () -> Unit = {}) {
    val textColor: Color = if (item.textColor.isNotBlank()) {
        Color(android.graphics.Color.parseColor(item.textColor))
    } else {
        NestTheme.colors.NN._600
    }

    val backgroundColor = if (item.bgColor.isNotBlank()) {
        Color(android.graphics.Color.parseColor(item.bgColor))
    } else {
        Color.Transparent
    }
    NestTypography(
        text = item.text,
        textStyle = NestTheme.typography.display3.copy(color = textColor),
        modifier = Modifier.clickable {
            onItemClicked()
        }
    )
}

@Composable
internal fun AutocompleteRightLabelEducation(
    item: SuggestionUnifyLabel,
    onItemClicked: () -> Unit = {}
) {
    val textColor: Color = if (item.textColor.isNotBlank()) {
        Color(android.graphics.Color.parseColor(item.textColor))
    } else {
        NestTheme.colors.GN._500
    }

    val backgroundColor = if (item.bgColor.isNotBlank()) {
        Color(android.graphics.Color.parseColor(item.bgColor))
    } else {
        Color.Transparent
    }
    NestTypography(
        text = item.text,
        textStyle = NestTheme.typography.display2.copy(
            color = textColor,
            fontWeight = FontWeight.ExtraBold
        ),
        modifier = Modifier.clickable {
            onItemClicked()
        }
    )
}
