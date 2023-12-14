package com.tokopedia.creation.common.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

/**
 * Created By : Jonathan Darwin on November 27, 2023
 */
@Composable
fun ContentInfoScreen(
    imageUrl: String,
    title: String,
    subtitle: String,
    primaryText: String,
    secondaryText: String,
    onPrimaryButtonClicked: () -> Unit,
    onSecondaryButtonClicked: () -> Unit,
) {
    val isSecondaryButtonShown = secondaryText.isNotEmpty()

    NestTheme(isOverrideStatusBarColor = false) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (
                imgHeader,
                txtTitle,
                txtSubtitle,
                btnPrimary,
                btnSecondary,
            ) = createRefs()

            NestImage(
                source = ImageSource.Remote(imageUrl),
                modifier = Modifier
                    .constrainAs(imgHeader) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
            )

            NestTypography(
                text = title,
                textStyle = NestTheme.typography.heading2.copy(
                    textAlign = TextAlign.Center,
                    color = NestTheme.colors.NN._950,
                ),
                modifier = Modifier
                    .constrainAs(txtTitle) {
                        top.linkTo(imgHeader.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
            )

            NestTypography(
                text = subtitle,
                textStyle = NestTheme.typography.paragraph2.copy(
                    textAlign = TextAlign.Center,
                    color = NestTheme.colors.NN._600,
                ),
                modifier = Modifier.constrainAs(txtSubtitle) {
                    top.linkTo(txtTitle.bottom, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
            )

            NestButton(
                text = primaryText,
                onClick = onPrimaryButtonClicked,
                modifier = Modifier.constrainAs(btnPrimary) {
                    top.linkTo(txtSubtitle.bottom, 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    if (!isSecondaryButtonShown) {
                        bottom.linkTo(parent.bottom, 16.dp)
                    }

                    width = Dimension.fillToConstraints
                }
            )

            if (isSecondaryButtonShown) {
                NestButton(
                    text = secondaryText,
                    variant = ButtonVariant.TEXT_ONLY,
                    onClick = onSecondaryButtonClicked,
                    modifier = Modifier.constrainAs(btnSecondary) {
                        top.linkTo(btnPrimary.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, 16.dp)

                        width = Dimension.fillToConstraints
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoriesCreationInfoLayoutPreview() {
    ContentInfoScreen(
        imageUrl = "https://images.tokopedia.net/img/android/content/content_creation/ic_content_too_much.png",
        title = "Oops, kamu sudah terlalu banyak upload story",
        subtitle = "Kamu tetap bisa upload lebih dari 30 Story, tapi yang terlama akan kamu hapus, ya.",
        primaryText = "Buat Story",
        secondaryText = "Kembali",
        onPrimaryButtonClicked = {},
        onSecondaryButtonClicked = {}
    )
}
