package com.tokopedia.play.broadcaster.shorts.view.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.components.NestLocalLoad
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.shorts.view.compose.helper.FlowRow
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created By : Jonathan Darwin on December 13, 2023
 */
@Composable
fun PlayShortsTagLayout(
    tagState: NetworkResult<PlayTagUiModel>,
    onTagClick: (PlayTagItem) -> Unit,
    onRefreshTag: () -> Unit,
) {
    if (tagState is NetworkResult.Fail) {
        NestLocalLoad(
            title = stringResource(id = R.string.play_bro_tag_recommendation_error_title),
            description = stringResource(id = R.string.play_bro_tag_recommendation_error_desc),
            isLoading = false,
            onRefreshButtonClicked = { onRefreshTag() }
        )

        return
    }

    Column {
        NestTypography(
            text = stringResource(id = R.string.play_bro_select_tag_title),
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        if (tagState is NetworkResult.Success) {
            Spacer(modifier = Modifier.height(8.dp))

            NestTypography(
                text = stringResource(
                    R.string.play_shorts_content_tagging_description_template,
                    tagState.data.maxTags
                ),
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
            when (tagState) {
                is NetworkResult.Success -> {
                    for(item in tagState.data.tags) {
                        NestChips(
                            text = item.tag,
                            state = if (item.isChosen) NestChipsState.Selected
                            else if (item.isActive) NestChipsState.Default
                            else NestChipsState.Disabled,
                            onClick = { onTagClick(item) }
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    val chipPlaceholderWidth = listOf(80, 60, 100, 80, 80, 60, 80, 100, 60, 80, 100, 80)
                    for(width in chipPlaceholderWidth) {
                        NestChips(
                            text = "",
                            state = NestChipsState.Disabled,
                            modifier = Modifier.width(width.dp)
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
