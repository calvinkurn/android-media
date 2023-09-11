package com.tokopedia.creation.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationMediaModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.creation.common.R as creationcommonR

/**
 * Created By : Muhammad Furqan on 07/09/23
 */
@Composable
fun ContentCreationComponent(
    creationItemList: Result<ContentCreationConfigModel>?,
    selectedItem: ContentCreationItemModel?,
    onSelectItem: (ContentCreationItemModel) -> Unit,
    onNextClicked: () -> Unit
) {
    NestTheme {
        when (creationItemList) {
            is Success -> ContentCreationSuccessView(
                creationItemList = creationItemList.data.creationItems,
                selectedItem = selectedItem,
                onSelectItem = onSelectItem,
                onNextClicked = onNextClicked
            )
            else -> ContentCreationLoadingView()
        }
    }
}

@Composable
private fun ContentCreationLoadingView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NestLoader(
            variant = NestLoaderType.Decorative(isWhite = false),
            modifier = Modifier.height(48.dp)
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
    ) {
        creationItemList.map {
            ExpandableContentCreationItem(
                isSelected = selectedItem?.type == it.type,
                data = it,
                onSelect = onSelectItem,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )
        }

        NestButton(
            text = stringResource(creationcommonR.string.content_creation_bottom_sheet_next_label),
            onClick = onNextClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ContentCreationComponentPreview() {
    ContentCreationComponent(
        creationItemList = Success(
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
                            mediaUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png",
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
                            mediaUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png",
                        ),
                        descriptionApplink = "tokopedia://any",
                        drawableIconId = IconUnify.VIDEO,
                        authorType = ContentCreationAuthorEnum.USER
                    ),
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
                mediaUrl = "https://cdn-icons-png.flaticon.com/512/25/25694.png",
            ),
            descriptionApplink = "tokopedia://any",
            drawableIconId = IconUnify.VIDEO,
            authorType = ContentCreationAuthorEnum.SHOP
        ),
        onSelectItem = {},
    ) {}
}
