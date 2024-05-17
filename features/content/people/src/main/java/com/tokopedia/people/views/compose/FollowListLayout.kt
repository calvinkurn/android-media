package com.tokopedia.people.views.compose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.noRippleClickable
import com.tokopedia.people.utils.onLoadMore
import com.tokopedia.people.utils.resId
import com.tokopedia.people.views.uimodel.PeopleUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

/**
 * Created by kenny.hadisaputra on 22/11/23
 */
@Composable
internal fun FollowListLayout(
    people: ImmutableList<PeopleUiModel>,
    hasNextPage: Boolean,
    onLoadMore: () -> Unit,
    onPeopleClicked: (PeopleUiModel) -> Unit,
    onFollowClicked: (PeopleUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(modifier, listState) {
        items(
            people,
            key = {
                when (it) {
                    is PeopleUiModel.ShopUiModel -> "shop_${it.id}"
                    is PeopleUiModel.UserUiModel -> "user_${it.id}"
                }
            }
        ) {
            when (it) {
                is PeopleUiModel.ShopUiModel -> {
                    ShopFollowListItemRow(
                        it,
                        onFollowClicked = onFollowClicked,
                        onItemClicked = onPeopleClicked
                    )
                }

                is PeopleUiModel.UserUiModel -> {
                    UserFollowListItemRow(
                        it,
                        onFollowClicked = onFollowClicked,
                        onItemClicked = onPeopleClicked
                    )
                }
            }
        }

        if (hasNextPage) {
            item {
                Box(Modifier.fillParentMaxWidth()) {
                    NestLoader(
                        variant = NestLoaderType.Circular(),
                        Modifier
                            .padding(8.dp)
                            .requiredSize(40.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

    listState.onLoadMore { onLoadMore() }
}

@Composable
private fun ShopFollowListItemRow(
    item: PeopleUiModel.ShopUiModel,
    onFollowClicked: (PeopleUiModel) -> Unit,
    onItemClicked: (PeopleUiModel.ShopUiModel) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .noRippleClickable { onItemClicked(item) }
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        NestImage(
            source = ImageSource.Remote(item.logoUrl),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .requiredSize(48.dp)
                .clip(CircleShape)
                .resId("img_profile_image")
        )
        if (item.badgeUrl.isNotBlank()) {
            NestImage(
                source = ImageSource.Remote(item.badgeUrl),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .requiredSize(18.dp)
                    .clip(CircleShape)
                    .resId("img_badge")
            )
        }
        NestTypography(
            text = item.name,
            textStyle = NestTheme.typography.heading5.copy(
                color = NestTheme.colors.NN._950
            ),
            maxLines = 1,
            modifier = Modifier
                .padding(start = if (item.badgeUrl.isNotBlank()) 4.dp else 12.dp, end = 12.dp)
                .weight(1f)
                .resId("text_display_name")
        )
        NestButton(
            text = if (item.isFollowed) "Following" else "Follow",
            variant = if (item.isFollowed) ButtonVariant.GHOST_ALTERNATE else ButtonVariant.FILLED,
            size = ButtonSize.MICRO,
            onClick = { onFollowClicked(item) },
            modifier = Modifier.resId("btn_action_follow")
        )
    }
}

@Composable
private fun UserFollowListItemRow(
    item: PeopleUiModel.UserUiModel,
    onFollowClicked: (PeopleUiModel) -> Unit,
    onItemClicked: (PeopleUiModel.UserUiModel) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .noRippleClickable { onItemClicked(item) }
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        NestImage(
            source = ImageSource.Remote(item.photoUrl),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .requiredSize(48.dp)
                .clip(CircleShape)
                .resId("img_profile_image")
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(1f)
        ) {
            NestTypography(
                text = item.name,
                textStyle = NestTheme.typography.heading5.copy(
                    color = NestTheme.colors.NN._950
                ),
                maxLines = 1,
                modifier = Modifier.resId("text_display_name")
            )

            if (item.username.isNotBlank()) {
                NestTypography(
                    text = "@${item.username}",
                    textStyle = NestTheme.typography.body3.copy(
                        color = NestTheme.colors.NN._600
                    ),
                    modifier = Modifier.resId("text_user_name")
                )
            }
        }

        if (!item.isMySelf) {
            NestButton(
                text = if (item.isFollowed) "Following" else "Follow",
                variant = if (item.isFollowed) ButtonVariant.GHOST_ALTERNATE else ButtonVariant.FILLED,
                size = ButtonSize.MICRO,
                onClick = { onFollowClicked(item) },
                modifier = Modifier.resId("btn_action_follow")
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ShopFollowListItemRowPreview() {
    NestTheme {
        Row(
            Modifier.background(NestTheme.colors.NN._0)
        ) {
            ShopFollowListItemRow(
                item = PeopleUiModel.ShopUiModel(
                    id = "1",
                    logoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2oqJCpfJ9aoT_gZXlShxCAJ1wJX0YQS8X-g&usqp=CAU",
                    badgeUrl = "a",
                    name = "PS Enterprise",
                    isFollowed = true,
                    appLink = ""
                ),
                onFollowClicked = {},
                onItemClicked = {}
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun UserFollowListItemRowPreview() {
    NestTheme {
        Row(
            Modifier.background(NestTheme.colors.NN._0)
        ) {
            UserFollowListItemRow(
                item = PeopleUiModel.UserUiModel(
                    id = "1",
                    encryptedId = "1",
                    photoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2oqJCpfJ9aoT_gZXlShxCAJ1wJX0YQS8X-g&usqp=CAU",
                    name = "PS Enterprise",
                    username = "@psenterprise",
                    isFollowed = true,
                    appLink = "",
                    isMySelf = false
                ),
                onFollowClicked = {},
                onItemClicked = {}
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FollowListLayoutPreview() {
    NestTheme {
        FollowListLayout(
            List(6) {
                if (it % 2 == 0) {
                    PeopleUiModel.ShopUiModel(
                        id = "$it",
                        logoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2oqJCpfJ9aoT_gZXlShxCAJ1wJX0YQS8X-g&usqp=CAU",
                        badgeUrl = "a",
                        name = "PS Enterprise $it",
                        isFollowed = true,
                        appLink = ""
                    )
                } else {
                    PeopleUiModel.UserUiModel(
                        id = "$it",
                        encryptedId = "$it",
                        photoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2oqJCpfJ9aoT_gZXlShxCAJ1wJX0YQS8X-g&usqp=CAU",
                        name = "PS Enterprise $it",
                        username = "@psenterprise",
                        isFollowed = true,
                        appLink = "",
                        isMySelf = false
                    )
                }
            }.toPersistentList(),
            hasNextPage = false,
            {},
            {},
            {},
            Modifier.background(NestTheme.colors.NN._0)
        )
    }
}
