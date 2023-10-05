package com.tokopedia.people.views.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.header.compose.NestHeaderVariant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.people.R
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify

/**
 * Created By : Jonathan Darwin on June 13, 2023
 */

@Composable
fun ProfileSettingsScreen(
    reviewSettings: ProfileSettingsUiModel,
    onBackPressed: () -> Unit,
    onCheckedChanged: (isChecked: Boolean) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            NestHeader(
                variant = NestHeaderVariant.Default,
                type = NestHeaderType.SingleLine(
                    onBackClicked = onBackPressed,
                    title = stringResource(id = R.string.up_profile_settings_title),
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                val (
                    icReviewSettings,
                    tvReviewSettings,
                    switchReviewSettings,
                ) = createRefs()

                ReviewSettingsIcon(
                    modifier = Modifier.constrainAs(icReviewSettings) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                )

                ReviewSettingsText(
                    text = reviewSettings.title,
                    modifier = Modifier
                        .constrainAs(tvReviewSettings) {
                            top.linkTo(icReviewSettings.top)
                            bottom.linkTo(icReviewSettings.bottom)
                            start.linkTo(icReviewSettings.end, 16.dp)
                            end.linkTo(switchReviewSettings.start, 16.dp)
                            width = Dimension.fillToConstraints
                    },
                )

                ReviewSettingsToggle(
                    isChecked = reviewSettings.isEnabled,
                    onCheckedChanged = onCheckedChanged,
                    modifier = Modifier.constrainAs(switchReviewSettings) {
                        top.linkTo(icReviewSettings.top)
                        bottom.linkTo(icReviewSettings.bottom)
                        end.linkTo(parent.end)
                    }
                )
            }
        }
    }
}

@Composable
private fun ReviewSettingsIcon(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            IconUnify(
                context,
                IconUnify.STAR
            )
        },
    )
}

@Composable
private fun ReviewSettingsText(
    text: String,
    modifier: Modifier = Modifier
) {
    NestTypography(
        modifier = modifier,
        textStyle = NestTheme.typography.paragraph2,
        text = text,
    )
}

@Composable
private fun ReviewSettingsToggle(
    isChecked: Boolean,
    onCheckedChanged: (isChecked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    AndroidView(
        modifier = modifier.wrapContentHeight(),
        factory = { context ->
            SwitchUnify(context).apply {
                setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChanged(isChecked)
                }
            }
        },
        update = {
            it.isChecked = isChecked
        },
    )
}

@Composable
@Preview
private fun ProfileSettingsScreenPreview() {
    NestTheme {
        ProfileSettingsScreen(
            reviewSettings = ProfileSettingsUiModel(
                settingID = ProfileSettingsUiModel.SETTING_ID_REVIEW,
                title = "Show / hide review",
                isEnabled = true,
            ),
            onBackPressed = {},
            onCheckedChanged = {},
        )
    }
}
