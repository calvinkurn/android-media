package com.tokopedia.stories.creation.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.content.common.ui.toolbar.ContentAccountToolbar
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.creation.common.util.RATIO_9_16
import com.tokopedia.creation.common.util.isMediaRatioSame
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.stories.creation.R
import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.stories.creation.view.model.StoriesMediaCover
import com.tokopedia.stories.creation.view.model.state.StoriesCreationUiState
import com.tokopedia.nest.components.R as nestcomponentsR
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
@Composable
fun StoriesCreationScreen(
    uiState: StoriesCreationUiState,
    onImpressScreen: () -> Unit,
    onLoadMediaPreview: suspend (filePath: String) -> StoriesMediaCover,
    onBackPressed: () -> Unit,
    onClickChangeAccount: () -> Unit,
    onClickAddProduct: () -> Unit,
    onClickUpload: () -> Unit,
) {
    if (uiState == StoriesCreationUiState.Empty) {
        Box(modifier = Modifier.fillMaxSize()) {
            NestLoader(
                modifier = Modifier.align(Alignment.Center),
                variant = NestLoaderType.Circular(
                    size = NestLoaderSize.Large
                ),
            )
        }

        return
    }

    if (uiState.media.filePath.isEmpty()) {
        return
    }

    LaunchedEffect(Unit) {
        onImpressScreen()
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (
            header,
            headerDivider,

            mediaCover,
            mediaCoverDivider,

            addProductSection,
            showDurationSection,

            uploadButtonDivider,
            uploadButton,
        ) = createRefs()

        StoriesCreationHeader(
            imageUrl = uiState.selectedAccount.iconUrl,
            authorName = uiState.selectedAccount.name,
            isEligibleToSwitchAccount = uiState.accountList.size > 1,
            onBackPressed = onBackPressed,
            onClickChangeAccount = onClickChangeAccount,
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        NestDivider(
            size = NestDividerSize.Small,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(headerDivider) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        StoriesCreationMediaCover(
            media = uiState.media,
            onLoadMediaPreview = onLoadMediaPreview,
            modifier = Modifier.constrainAs(mediaCover) {
                top.linkTo(headerDivider.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )


        NestDivider(
            size = NestDividerSize.Small,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(mediaCoverDivider) {
                    top.linkTo(mediaCover.bottom, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        StoriesCreationAddProductSection(
            selectedProductSize = uiState.productList.size,
            onClickAddProduct = onClickAddProduct,
            modifier = Modifier.constrainAs(addProductSection) {
                top.linkTo(mediaCoverDivider.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        StoriesCreationStoryDurationSection(
            storyDuration = uiState.config.storyDuration,
            modifier = Modifier.constrainAs(showDurationSection) {
                top.linkTo(addProductSection.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        NestDivider(
            size = NestDividerSize.Small,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(uploadButtonDivider) {
                    bottom.linkTo(uploadButton.top, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        NestButton(
            text = stringResource(id = R.string.stories_creation_upload),
            onClick = onClickUpload,
            modifier = Modifier
                .testTag(stringResource(R.string.stories_creation_upload_test_tag))
                .constrainAs(uploadButton) {
                    bottom.linkTo(parent.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)

                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
private fun StoriesCreationHeader(
    imageUrl: String,
    authorName: String,
    isEligibleToSwitchAccount: Boolean,
    onBackPressed: () -> Unit,
    onClickChangeAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {
            ContentAccountToolbar(it).apply {
                title = context.getString(R.string.stories_creation_upload_as)
                this.setOnBackClickListener {
                    onBackPressed()
                }
            }
        },
        update = {
            it.subtitle = authorName
            it.icon = imageUrl

            if (isEligibleToSwitchAccount) {
                it.setOnAccountClickListener {
                    onClickChangeAccount()
                }
            } else {
                it.setOnAccountClickListener(null)
            }
        }
    )
}

@Composable
private fun StoriesCreationMediaCover(
    media: StoriesMedia,
    onLoadMediaPreview: suspend (filePath: String) -> StoriesMediaCover,
    modifier: Modifier = Modifier,
) {
    val storiesMediaPreviewModifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .width(108.dp)
        .height(192.dp)
        .background(Color.Black)

    var storiesMediaCover: StoriesMediaCover by remember {
        mutableStateOf(StoriesMediaCover.Loading)
    }

    LaunchedEffect(media.filePath) {
        storiesMediaCover = onLoadMediaPreview(media.filePath)
    }

    when (val mediaCover = storiesMediaCover) {
        is StoriesMediaCover.Loading -> {
            NestLoader(
                variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(8.dp)),
                modifier = storiesMediaPreviewModifier
            )
        }
        is StoriesMediaCover.Success -> {
            NestImage(
                source = ImageSource.Remote(mediaCover.localFilePath),
                modifier = storiesMediaPreviewModifier,
                contentScale = if (media.type.isImage && !isMediaRatioSame(mediaCover.localFilePath, RATIO_9_16)) {
                    ContentScale.Fit
                } else {
                    ContentScale.Crop
                }
            )
        }
        is StoriesMediaCover.Error -> {
            Box(
                modifier = storiesMediaPreviewModifier.background(NestTheme.colors.NN._100),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = nestcomponentsR.drawable.imagestate_error),
                    contentDescription = "Stories Cover Not Loaded"
                )
            }
        }
    }
}

@Composable
private fun StoriesCreationAddProductSection(
    selectedProductSize: Int,
    onClickAddProduct: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .testTag(stringResource(R.string.stories_creation_add_product_test_tag))
            .fillMaxWidth()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
            ) {
                onClickAddProduct()
            }
    ) {
        val (
            icon,
            label,
            selectedProductText,
            chevronRight,
        ) = createRefs()

        NestIcon(
            iconId = IconUnify.PRODUCT,
            modifier = Modifier
                .size(28.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 16.dp)
                    bottom.linkTo(parent.bottom)
                }
        )

        NestTypography(
            text = stringResource(id = R.string.stories_creation_add_product),
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.constrainAs(label) {
                top.linkTo(icon.top)
                bottom.linkTo(icon.bottom)
                start.linkTo(icon.end, 8.dp)
            }
        )

        if (selectedProductSize > 0) {
            NestTypography(
                text = stringResource(id = R.string.stories_creation_selected_product_template, selectedProductSize),
                textStyle = NestTheme.typography.display3.copy(
                    color = colorResource(id = unifycomponentsR.color.Unify_NN600)
                ),
                modifier = Modifier.constrainAs(selectedProductText) {
                    top.linkTo(icon.top)
                    bottom.linkTo(icon.bottom)
                    end.linkTo(chevronRight.start, 8.dp)
                }
            )
        }

        NestIcon(
            iconId = IconUnify.CHEVRON_RIGHT,
            modifier = Modifier
                .size(28.dp)
                .constrainAs(chevronRight) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun StoriesCreationStoryDurationSection(
    storyDuration: String,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val (
            icon,
            label,
            tvShowDuration,
        ) = createRefs()

        NestIcon(
            iconId = IconUnify.CALENDAR_TIME,
            modifier = Modifier
                .size(28.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 16.dp)
                    bottom.linkTo(parent.bottom)
                }
        )

        NestTypography(
            text = stringResource(id = R.string.stories_creation_show_duration),
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.constrainAs(label) {
                top.linkTo(icon.top)
                bottom.linkTo(icon.bottom)
                start.linkTo(icon.end, 8.dp)
            }
        )

        NestTypography(
            text = storyDuration,
            textStyle = NestTheme.typography.display3.copy(
                color = colorResource(id = unifycomponentsR.color.Unify_NN600)
            ),
            modifier = Modifier
                .constrainAs(tvShowDuration) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
@Preview
private fun StoriesCreationScreenPreview() {
    NestTheme {
        Surface {
            val uiState = StoriesCreationUiState.Empty.copy(
                media = StoriesMedia(
                    filePath = "media_file_path",
                    type = ContentMediaType.Video,
                )
            )

            StoriesCreationScreen(
                uiState = uiState,
                onImpressScreen = {},
                onLoadMediaPreview = { StoriesMediaCover.Loading },
                onBackPressed = {},
                onClickChangeAccount = {},
                onClickAddProduct = {},
                onClickUpload = {}
            )
        }
    }
}
