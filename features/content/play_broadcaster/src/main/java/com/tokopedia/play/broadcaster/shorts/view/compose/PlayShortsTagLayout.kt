package com.tokopedia.play.broadcaster.shorts.view.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel

/**
 * Created By : Jonathan Darwin on December 13, 2023
 */
@Composable
fun PlayShortsTagLayout(
    tag: PlayTagUiModel,
    onTagClick: (PlayTagItem) -> Unit,
) {
    val subtitle = if (tag.maxTags < 0) {
        ""
    } else {
        stringResource(
            R.string.play_shorts_content_tagging_description_template,
            tag.maxTags
        )
    }

    Column {
        NestTypography(
            text = stringResource(id = R.string.play_bro_select_tag_title),
            textStyle = NestTheme.typography.heading3
        )

        if (subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            NestTypography(
                text = subtitle,
                textStyle = NestTheme.typography.paragraph2.copy(
                    color = NestTheme.colors.NN._600,
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            horizontalGap = 8.dp,
            verticalGap = 8.dp,
        ) {
            for(item in tag.tags) {
                NestChips(
                    text = item.tag,
                    state = if (item.isChosen) NestChipsState.Selected
                    else if (item.isActive) NestChipsState.Default
                    else NestChipsState.Disabled,
                    onClick = { onTagClick(item) }
                )
            }
        }
    }
}
