package com.tokopedia.play.broadcaster.shorts.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.noRippleClickable
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.shorts.ui.model.ShortsCoverState
import com.tokopedia.nest.components.R as nestcomponentsR

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
@Composable
fun InterspersingConfirmationLayout(
    newCoverState: ShortsCoverState,
    oldCoverState: ShortsCoverState,
    onClickBack: () -> Unit,
    onClickNext: () -> Unit,
    onClickPdpVideo: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (
            tvTitle,
            newContentSection,
            tvSectionSeparator,
            oldContentSection,
            btnBack,
            btnNext,
        ) = createRefs()

        NestTypography(
            text = stringResource(id = R.string.play_shorts_interspersing_video_confirmation_title),
            textStyle = NestTheme.typography.heading2.copy(
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.constrainAs(tvTitle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                width = Dimension.fillToConstraints
            }
        )

        ContentPreviewSection(
            coverState = newCoverState,
            description = stringResource(id = R.string.play_shorts_interspersing_video_new_video_desc),
            modifier = Modifier.constrainAs(newContentSection) {
                top.linkTo(tvTitle.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                width = Dimension.fillToConstraints
            }
        )

        NestTypography(
            text = stringResource(id = R.string.play_shorts_interspersing_video_replace),
            textStyle = NestTheme.typography.paragraph2.copy(
                color = NestTheme.colors.NN._600
            ),
            modifier = Modifier.constrainAs(tvSectionSeparator) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(newContentSection.bottom, 20.dp)
            }
        )

        ContentPreviewSection(
            coverState = oldCoverState,
            description = stringResource(id = R.string.play_shorts_interspersing_video_old_video_desc),
            onClickCover = onClickPdpVideo,
            modifier = Modifier.constrainAs(oldContentSection) {
                top.linkTo(tvSectionSeparator.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

                width = Dimension.fillToConstraints
            }
        )
        
        NestButton(
            text = stringResource(id = R.string.play_back),
            variant = ButtonVariant.GHOST,
            onClick = onClickBack,
            modifier = Modifier.constrainAs(btnBack) {
                top.linkTo(oldContentSection.bottom, 28.dp)
                bottom.linkTo(parent.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(btnNext.start, 4.dp)

                width = Dimension.fillToConstraints
            }
        )

        NestButton(
            text = stringResource(id = R.string.play_bro_next_action),
            onClick = onClickNext,
            modifier = Modifier.constrainAs(btnNext) {
                top.linkTo(btnBack.top)
                bottom.linkTo(btnBack.bottom)
                end.linkTo(parent.end)
                start.linkTo(btnBack.end, 4.dp)

                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
private fun ContentPreviewSection(
    coverState: ShortsCoverState,
    description: String,
    modifier: Modifier = Modifier,
    onClickCover: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, NestTheme.colors.NN._50)
            .noRippleClickable {
                onClickCover?.invoke()
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .width(60.dp)
                    .height(60.dp),
            ) {
                val coverModifier = Modifier.fillMaxSize()
                when (coverState) {
                    is ShortsCoverState.Loading -> {
                        NestLoader(
                            variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(8.dp)),
                            modifier = coverModifier
                        )
                    }
                    is ShortsCoverState.Success -> {
                        NestImage(
                            source = ImageSource.Remote(coverState.uri),
                            modifier = coverModifier
                        )
                    }
                    is ShortsCoverState.Error -> {
                        Box(
                            modifier = coverModifier.background(NestTheme.colors.NN._100),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painterResource(id = nestcomponentsR.drawable.imagestate_error),
                                contentDescription = "Cover is not loaded"
                            )
                        }
                    }
                    else -> {}
                }

                if (onClickCover != null) {
                    Box(
                        modifier = coverModifier
                            .background(Color(0f, 0f, 0f, 0.4f))
                    )

                    NestIcon(
                        iconId = IconUnify.PLAY,
                        modifier = Modifier.align(Alignment.Center),
                        colorLightEnable = Color.White,
                        colorLightDisable = Color.White,
                        colorNightEnable = Color.White,
                        colorNightDisable = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            NestTypography(
                text = description,
                textStyle = NestTheme.typography.heading4
            )
        }
    }
}

@Composable
@Preview
private fun InterspersingConfirmationLayoutPreview() {
    NestTheme {
        Surface {
            InterspersingConfirmationLayout(
                newCoverState = ShortsCoverState.Success(""),
                oldCoverState = ShortsCoverState.Success(""),
                onClickBack = {  },
                onClickNext = {  },
                onClickPdpVideo = { },
            )
        }
    }
}
