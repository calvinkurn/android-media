package com.tokopedia.play.broadcaster.shorts.view.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.material.internal.FlowLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.components.NestLocalLoad
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import java.lang.Integer.max

/**
 * Created By : Jonathan Darwin on December 12, 2023
 */
@Composable
fun PlayShortsSummaryConfigLayout(
    tagsState: NetworkResult<PlayTagUiModel>,
    onRefreshTag: () -> Unit,
    onTagClick: (PlayTagItem) -> Unit,
) {
    NestTheme(
        isOverrideStatusBarColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                when (tagsState) {
                    is NetworkResult.Loading -> {

                    }
                    is NetworkResult.Success -> {
                        TagsSection(
                            tag = tagsState.data,
                            onTagClick = onTagClick,
                        )
                    }
                    is NetworkResult.Fail -> {
                        NestLocalLoad(
                            title = stringResource(id = R.string.play_bro_tag_recommendation_error_title),
                            description = stringResource(id = R.string.play_bro_tag_recommendation_error_desc),
                            isLoading = false,
                            onRefreshButtonClicked = { onRefreshTag() }
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun TagsSection(
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
            Spacer(modifier = Modifier.height(4.dp))

            NestTypography(
                text = subtitle,
                textStyle = NestTheme.typography.paragraph2.copy(
                    color = NestTheme.colors.NN._600,
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

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

@Composable
@Preview
private fun PlayShortsSummaryConfigLayoutPreview() {
    val tags = setOf(
        PlayTagItem("Tag 1", false, true),
        PlayTagItem("Tag 2", false, true),
        PlayTagItem("Tag 3", false, true),
        PlayTagItem("Tag 4", false, true),
        PlayTagItem("Tag 5", false, true),
        PlayTagItem("Tag 6", false, true),
        PlayTagItem("Tag 7", false, true),
        PlayTagItem("Tag 8", false, true),
        PlayTagItem("Tag 9", false, true),
        PlayTagItem("Tag 10", false, true),
    )
    PlayShortsSummaryConfigLayout(
        tagsState = NetworkResult.Success(
            PlayTagUiModel(tags, 1, 10)
        ),
        onRefreshTag = { /*TODO*/ },
        onTagClick = {

        }
    )
}


@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    val hGapPx = horizontalGap.roundToPx()
    val vGapPx = verticalGap.roundToPx()

    val rows = mutableListOf<MeasuredRow>()
    val itemConstraints = constraints.copy(minWidth = 0)

    for (measurable in measurables) {
        val lastRow = rows.lastOrNull()
        val placeable = measurable.measure(itemConstraints)

        if (lastRow != null && lastRow.width + hGapPx + placeable.width <= constraints.maxWidth) {
            lastRow.items.add(placeable)
            lastRow.width += hGapPx + placeable.width
            lastRow.height = max(lastRow.height, placeable.height)
        } else {
            val nextRow = MeasuredRow(
                items = mutableListOf(placeable),
                width = placeable.width,
                height = placeable.height
            )

            rows.add(nextRow)
        }
    }

    val width = rows.maxOfOrNull { row -> row.width } ?: 0
    val height = rows.sumBy { row -> row.height } + max(vGapPx.times(rows.size - 1), 0)

    val coercedWidth = width.coerceIn(constraints.minWidth, constraints.maxWidth)
    val coercedHeight = height.coerceIn(constraints.minHeight, constraints.maxHeight)

    layout(coercedWidth, coercedHeight) {
        var y = 0

        for (row in rows) {
            var x = when(alignment) {
                Alignment.Start -> 0
                Alignment.CenterHorizontally -> (coercedWidth - row.width) / 2
                Alignment.End -> coercedWidth - row.width

                else -> throw Exception("unsupported alignment")
            }

            for (item in row.items) {
                item.place(x, y)
                x += item.width + hGapPx
            }

            y += row.height + vGapPx
        }
    }
}

private data class MeasuredRow(
    val items: MutableList<Placeable>,
    var width: Int,
    var height: Int
)
