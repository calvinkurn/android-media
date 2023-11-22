package com.tokopedia.creation.common.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationMediaModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.creation.common.R as creationcommonR

/**
 * Created By : Muhammad Furqan on 07/09/23
 */
@Composable
fun ContentCreationView(
    creationConfig: Result<ContentCreationConfigModel>?,
    onImpressBottomSheet: () -> Unit,
    selectedItem: ContentCreationItemModel?,
    onSelectItem: (ContentCreationItemModel) -> Unit,
    onNextClicked: () -> Unit,
    onRetryClicked: () -> Unit
) {
    NestTheme(
        isOverrideStatusBarColor = false
    ) {
        when (creationConfig) {
            is Success -> {
                LaunchedEffect(Unit) {
                    onImpressBottomSheet()
                }

                ContentCreationSuccessView(
                    creationItemList = creationConfig.data.creationItems,
                    selectedItem = selectedItem,
                    onSelectItem = onSelectItem,
                    onNextClicked = onNextClicked
                )
            }

            is Fail -> ContentCreationFailView(onRetry = onRetryClicked)
            else -> ContentCreationLoadingView()
        }
    }
}

@Composable
private fun ContentCreationLoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NestLoader(
            variant = NestLoaderType.Decorative(isWhite = false, size = NestLoaderSize.Large)
        )
    }
}

@Composable
private fun ContentCreationSuccessView(
    creationItemList: List<ContentCreationItemModel>,
    selectedItem: ContentCreationItemModel?,
    onSelectItem: (ContentCreationItemModel) -> Unit,
    onNextClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .animateContentSize()
    ) {
        LazyColumn {
            items(creationItemList) {
                ExpandableContentCreationItem(
                    isSelected = selectedItem?.type == it.type,
                    data = it,
                    onSelect = onSelectItem,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                )
            }
        }

        NestButton(
            text = stringResource(creationcommonR.string.content_creation_bottom_sheet_next_label),
            onClick = onNextClicked,
            isEnabled = selectedItem != null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ContentCreationFailView(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp, top = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NestImage(
            source = ImageSource.Remote(stringResource(id = creationcommonR.string.content_creation_failed_network_image_url)),
            modifier = Modifier.fillMaxWidth(),
            contentDescription = stringResource(creationcommonR.string.content_creation_failed_network_title)
        )
        NestTypography(
            text = stringResource(creationcommonR.string.content_creation_failed_network_title),
            textStyle = NestTheme.typography.heading2.copy(
                textAlign = TextAlign.Center,
                color = NestTheme.colors.NN._950
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        NestTypography(
            text = stringResource(creationcommonR.string.content_creation_failed_network_description),
            textStyle = NestTheme.typography.paragraph2.copy(
                textAlign = TextAlign.Center,
                color = NestTheme.colors.NN._600
            ),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        NestButton(
            text = stringResource(creationcommonR.string.content_creation_retry_label),
            onClick = onRetry,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ContentCreationComponentFailedPreview() {
    ContentCreationView(
        creationConfig = Fail(Throwable("Failed")),
        selectedItem = null,
        onSelectItem = {},
        onNextClicked = {},
        onRetryClicked = {},
        onImpressBottomSheet = {}
    )
}

@Preview
@Composable
private fun ContentCreationComponentSuccessPreview() {
    ContentCreationView(
        creationConfig = Success(
            ContentCreationConfigModel(
                isActive = true,
                statisticApplink = "",
                authors = emptyList(),
                creationItems = listOf(
                    ContentCreationItemModel(
                        isActive = true,
                        type = ContentCreationTypeEnum.LIVE,
                        title = "Buat Live",
                        applink = "tokopedia://play-broadcaster?author_type=content-shop",
                        media = ContentCreationMediaModel(
                            type = "image",
                            id = "",
                            coverUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png",
                            mediaUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png"
                        ),
                        descriptionApplink = "tokopedia://any",
                        drawableIconId = IconUnify.VIDEO,
                        authorType = ContentCreationAuthorEnum.SHOP
                    ),
                    ContentCreationItemModel(
                        isActive = true,
                        type = ContentCreationTypeEnum.POST,
                        title = "Buat Post",
                        applink = "tokopedia://content/create_post",
                        media = ContentCreationMediaModel(
                            type = "image",
                            id = "",
                            coverUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png",
                            mediaUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png"
                        ),
                        descriptionApplink = "tokopedia://any",
                        drawableIconId = IconUnify.VIDEO,
                        authorType = ContentCreationAuthorEnum.USER
                    )
                )
            )
        ),
        selectedItem =
        ContentCreationItemModel(
            isActive = true,
            type = ContentCreationTypeEnum.LIVE,
            title = "Buat Live",
            applink = "tokopedia://play-broadcaster?author_type=content-shop",
            media = ContentCreationMediaModel(
                type = "image",
                id = "",
                coverUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png",
                mediaUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png"
            ),
            descriptionApplink = "tokopedia://any",
            drawableIconId = IconUnify.VIDEO,
            authorType = ContentCreationAuthorEnum.SHOP
        ),
        onSelectItem = {},
        onNextClicked = {},
        onRetryClicked = {},
        onImpressBottomSheet = {}
    )
}
