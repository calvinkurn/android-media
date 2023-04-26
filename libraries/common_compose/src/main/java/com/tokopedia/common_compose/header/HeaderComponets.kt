package com.tokopedia.common_compose.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestNN
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 26/04/23"
 * Project name: android-tokopedia-core
 **/

/**
 * get color base on variant
 * background, content, sub-title, icon color
 */
@Composable
internal fun getHeaderBackgroundColor(variant: NestHeaderVariant) = when (variant) {
    is NestHeaderVariant.Transparent -> Color.Transparent
    else -> NestTheme.colors.NN._0
}

@Composable
internal fun getHeaderContentColor(variant: NestHeaderVariant) = when (variant) {
    is NestHeaderVariant.Transparent -> NestNN.light._0
    else -> NestTheme.colors.NN._950
}

@Composable
internal fun getHeaderContentSecondaryColor(variant: NestHeaderVariant) = when (variant) {
    is NestHeaderVariant.Transparent -> NestNN.light._0
    else -> NestTheme.colors.NN._600
}

@Composable
internal fun getHeaderIconColor(variant: NestHeaderVariant) = when (variant) {
    is NestHeaderVariant.Transparent -> NestNN.light._0
    else -> NestTheme.colors.NN._900
}

@Composable
fun HeaderMarginArea(
    modifier: Modifier = Modifier
) {
    Spacer(modifier = modifier.width(16.dp))
}

@Composable
fun HeaderContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

/**
 * Header Title Component
 */
@Composable
fun HeaderTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    NestTypography(
        modifier = modifier,
        text = title,
        textStyle = NestTheme.typography.display1.copy(
            fontWeight = FontWeight.Bold
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

/**
 * Header SubTitle Component
 */
@Composable
fun HeaderSubTitle(
    modifier: Modifier = Modifier,
    subTitle: String,
    contentSecondaryColor: Color
) {
    NestTypography(
        modifier = modifier,
        text = subTitle,
        textStyle = NestTheme.typography.display3.copy(
            color = contentSecondaryColor
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

/**
 * Header IconBack Component
 */
@Composable
fun HeaderIconBack(
    iconColor: Color,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .height(24.dp)
            .width(24.dp),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.iconunify_arrow_back),
            tint = iconColor,
            contentDescription = "BackButton"
        )
    }

    Spacer(modifier = Modifier.width(12.dp))
}
