package com.tokopedia.common_compose.header

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 18/04/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun HeaderSearchType(
    properties: NestHeaderType.Search,
    contentSecondaryColor: Color,
    iconColor: Color
) {
    HeaderContainer {
        HeaderMarginArea()

        if (properties.showBackButton) {
            HeaderIconBack(iconColor = iconColor, onClick = properties.onBackClicked)
        }

        SearchTextField(
            properties = properties,
            contentSecondaryColor = contentSecondaryColor
        )

        HeaderOptionsButton(optionsButton = properties.optionsButton, iconColor = iconColor)

        HeaderMarginArea()
    }
}

@Composable
private fun RowScope.SearchTextField(
    properties: NestHeaderType.Search,
    contentSecondaryColor: Color
) {
    val leadingIcon: @Composable () -> Unit = {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = com.tokopedia.iconunify.R.drawable.iconunify_search),
            contentDescription = "search icon ",
            tint = NestTheme.colors.NN._500
        )
    }

    val placeholder: @Composable () -> Unit = {
        NestTypography(
            text = properties.hint,
            textStyle = NestTheme.typography.display2.copy(
                color = NestTheme.colors.NN._300
            ),
            maxLines = 1
        )
    }

    BasicTextField(
        modifier = Modifier
            .weight(1f)
            .height(36.dp),
        value = properties.value,
        onValueChange = properties.onSearchChanges,
        textStyle = NestTheme.typography.display2.copy(
            color = contentSecondaryColor
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { properties.onSearchKeyPressed.invoke() }
        ),
        decorationBox = @Composable { textField ->
            SearchTextFieldLayout(
                properties = properties,
                leadingIcon = leadingIcon,
                placeholder = placeholder,
                textField = textField
            )
        }
    )
}

@Composable
private fun SearchTextFieldLayout(
    properties: NestHeaderType.Search,
    leadingIcon: @Composable () -> Unit,
    placeholder: @Composable () -> Unit,
    textField: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(NestTheme.colors.NN._0)
            .border(
                border = BorderStroke(1.dp, color = NestTheme.colors.NN._200),
                shape = RoundedCornerShape(8.dp)
            )
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))

        // leading icon
        leadingIcon()

        Box(modifier = Modifier.weight(1f)) {
            // placeholder
            if (properties.value.isBlank()) {
                placeholder()
            }

            textField()
        }

        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Preview
@Composable
private fun HeaderSearchTypePreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderSearchType(
            properties = NestHeaderType.Search().copy(
                hint = "",
                value = "Fashion Wanita"
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}

@Preview
@Composable
private fun HeaderSearchTypeNonBackButtonPreview() {
    Surface(
        contentColor = NestTheme.colors.NN._950
    ) {
        HeaderSearchType(
            properties = NestHeaderType.Search().copy(
                hint = "Cari di Tokopedia",
                value = "",
                showBackButton = false,
                optionsButton = listOf(
                    HeaderActionButton(icon = HeaderIconSource.Painter(painterResource(id = com.tokopedia.iconunify.R.drawable.iconunify_bell))),
                    HeaderTextButton(text = "Action"),
                    HeaderTextButton(text = "Add")
                )
            ),
            iconColor = NestTheme.colors.NN._900,
            contentSecondaryColor = NestTheme.colors.NN._600
        )
    }
}
