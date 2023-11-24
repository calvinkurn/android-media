package com.tokopedia.darkmodeconfig.view.screen

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.darkmodeconfig.model.UiMode
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.compose.NestRadioButton

/**
 * Created by @ilhamsuaib on 06/11/23.
 */

@Composable
internal fun DarkModeConfigScreen(
    modifier: Modifier = Modifier,
    options: List<UiMode>,
    onSelectedChanged: (UiMode) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = options, key = { it.nameResId }) {
            ThemeOptionComponent(it, onSelectedChanged)
        }
    }
}

@Composable
private fun ThemeOptionComponent(
    option: UiMode,
    onSelectedChanged: (UiMode) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                onSelectedChanged(option)
            }
    ) {
        val (title, description, radio, divider) = createRefs()
        NestTypography(
            text = stringResource(id = option.nameResId),
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            ),
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                top.linkTo(parent.top, margin = 12.dp)
                end.linkTo(radio.start, margin = 16.dp)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
        )
        NestTypography(
            text = stringResource(id = option.descriptionResId),
            textStyle = NestTheme.typography.paragraph3.copy(
                color = NestTheme.colors.NN._600
            ),
            modifier = Modifier.constrainAs(description) {
                start.linkTo(parent.start)
                top.linkTo(title.bottom, margin = 2.dp)
                end.linkTo(radio.start, margin = 16.dp)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
        )
        NestRadioButton(
            selected = option.isSelected,
            text = "",
            onSelected = {
                if (it) {
                    onSelectedChanged(option)
                }
            },
            modifier = Modifier.constrainAs(radio) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            }
        )
        NestDivider(
            size = NestDividerSize.Small,
            modifier = Modifier.constrainAs(divider) {
                start.linkTo(parent.start)
                top.linkTo(description.bottom, margin = 12.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DarkModeConfigScreenPreview() {
    DarkModeConfigScreen(
        options = UiMode.getOptionList(AppCompatDelegate.MODE_NIGHT_NO),
        onSelectedChanged = {}
    )
}