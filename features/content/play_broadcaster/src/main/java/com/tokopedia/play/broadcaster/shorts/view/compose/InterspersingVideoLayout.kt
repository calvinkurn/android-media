package com.tokopedia.play.broadcaster.shorts.view.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.compose.NestSwitch

/**
 * Created By : Jonathan Darwin on December 13, 2023
 */
@Composable
fun InterspersingVideoLayout(
    isChecked: Boolean,
    onSwitchChanged: (isChecked: Boolean) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (
            tvTitle,
            tvDesc,
            switch,
        ) = createRefs()

        NestTypography(
            text = stringResource(id = R.string.play_shorts_interspersing_video_title),
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.constrainAs(tvTitle) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(switch.start, 24.dp)

                width = Dimension.fillToConstraints
            }
        )

        NestTypography(
            text = stringResource(R.string.play_shorts_interspersing_video_desc),
            textStyle = NestTheme.typography.paragraph2.copy(
                color = NestTheme.colors.NN._600,
            ),
            modifier = Modifier.constrainAs(tvDesc) {
                start.linkTo(tvTitle.start)
                end.linkTo(tvTitle.end)
                top.linkTo(tvTitle.bottom, 8.dp)

                width = Dimension.fillToConstraints
            }
        )

        NestSwitch(
            isChecked = isChecked,
            onCheckedChanged = onSwitchChanged,
            modifier = Modifier
                .testTag("interspersing_switch_test_tag")
                .constrainAs(switch) {
                    end.linkTo(parent.end)
                    start.linkTo(tvTitle.end)
                }
        )
    }
}

@Composable
@Preview
private fun InterspersingVideoLayoutPreview() {
    NestTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            InterspersingVideoLayout(
                isChecked = true,
                onSwitchChanged = {}
            )
        }
    }
}
