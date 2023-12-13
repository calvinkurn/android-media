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
                        PlayShortsTagLayout(
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
@Preview
private fun PlayShortsSummaryConfigLayoutPreview() {
    val tags = setOf(
        PlayTagItem("Tag 1 Very Long", false, true),
        PlayTagItem("Tag 2 Long", false, true),
        PlayTagItem("Tag 3 Very Long", true, true),
        PlayTagItem("Tag 4 Short But Long", false, true),
    )

    PlayShortsSummaryConfigLayout(
        tagsState = NetworkResult.Success(
            PlayTagUiModel(tags, 1, 2)
        ),
        onRefreshTag = { /*TODO*/ },
        onTagClick = {

        }
    )
}
