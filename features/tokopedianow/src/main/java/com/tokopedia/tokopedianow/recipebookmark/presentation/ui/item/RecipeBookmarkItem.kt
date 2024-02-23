package com.tokopedia.tokopedianow.recipebookmark.presentation.ui.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow.PARAM_RECIPE_ID
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.ListItemImpression
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics
import com.tokopedia.tokopedianow.recipebookmark.presentation.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.recipebookmark.presentation.ui.model.RecipeUiModel
import com.tokopedia.tokopedianow.recipecommon.ui.item.RecipeTagItem
import com.tokopedia.tokopedianow.recipecommon.ui.model.TagUiModel
import com.tokopedia.user.session.UserSession

@Composable
fun RecipeBookmarkItem(
    position: Int,
    recipe: RecipeUiModel,
    analytics: RecipeBookmarkAnalytics,
    lazyListState: LazyListState,
    onEvent: (RecipeBookmarkEvent) -> Unit
) {
    val context = LocalContext.current

    val id = recipe.id
    val title = recipe.title
    val duration = recipe.duration
    val imageUrl = recipe.picture
    val portion = recipe.portion
    val tags = remember { recipe.tags.orEmpty() }

    val iconModifier = remember(position) {
        Modifier
            .height(15.dp)
            .width(15.dp)
            .clickable {
                onEvent.removeRecipeBookmark(position, recipe)
                analytics.clickUnBookmark(id, title)
            }
    }

    val cardModifier = remember(position) {
        Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .clickable {
                val appLink = UriUtil.buildUriAppendParam(
                    ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
                    mapOf(PARAM_RECIPE_ID to id)
                )
                RouteManager.route(context, appLink)
                analytics.clickRecipeCard(id, title, position)
            }
    }

    Card(
        modifier = cardModifier,
        backgroundColor = getCardBackgroundColor(),
        border = BorderStroke(1.dp, NestTheme.colors.NN._200),
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            NestImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                source = ImageSource.Remote(imageUrl),
                type = NestImageType.Rect(rounded = 0.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NestTypography(
                    modifier = Modifier.weight(1f),
                    text = title,
                    textStyle = NestTheme.typography.display3.copy(
                        fontWeight = FontWeight.Bold,
                        color = NestTheme.colors.NN._1000
                    )
                )
                NestIcon(
                    modifier = iconModifier,
                    iconId = IconUnify.BOOKMARK_FILLED,
                    colorLightEnable = NestTheme.colors.NN._500,
                    colorNightEnable = NestTheme.colors.NN._500
                )
            }

            NestTypography(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 4.dp,
                        bottom = 12.dp
                    ),
                text = if (duration != null) {
                    stringResource(
                        R.string.tokopedianow_recipe_bookmark_item_subtitle,
                        duration.orZero(),
                        portion
                    )
                } else {
                    stringResource(
                        R.string.tokopedianow_recipe_bookmark_item_subtitle_without_duration,
                        portion
                    )
                },
                textStyle = NestTheme.typography.small.copy(
                    color = NestTheme.colors.NN._1000
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    end = 4.dp
                ),
            horizontalArrangement = Arrangement.End
        ) {
            tags.forEach {
                RecipeTagItem(data = it)
            }
        }
    }

    ListItemImpression(key = recipe.getUniqueId(), lazyListState = lazyListState) {
        analytics.impressRecipeCard(id, title, position)
    }
}

@Preview
@Composable
private fun RecipeBookmarkItemPreview() {
    val context = LocalContext.current
    val appLink = UriUtil.buildUriAppendParam(
        ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
        mapOf(PARAM_RECIPE_ID to "1501")
    )

    RecipeBookmarkItem(
        position = 0,
        recipe = RecipeUiModel(
            id = "1",
            title = "Nasi Goreng",
            duration = 30,
            portion = 1,
            tags = listOf(
                TagUiModel("Halal", false),
                TagUiModel("Nasi", false)
            ),
            picture = "https://media.istockphoto.com/id/1345298910/id/foto/nasi-goreng-spesial-atau-nasi-goreng-spesial.jpg",
            appUrl = appLink
        ),
        analytics = RecipeBookmarkAnalytics(TokoNowLocalAddress(context), UserSession(context)),
        lazyListState = rememberLazyListState(),
        onEvent = {}
    )
}

@Composable
private fun getCardBackgroundColor(): Color {
    return if (isSystemInDarkTheme()) {
        NestTheme.colors.NN._50
    } else {
        NestTheme.colors.NN._0
    }
}

private fun ((RecipeBookmarkEvent) -> Unit).removeRecipeBookmark(
    index: Int,
    recipe: RecipeUiModel
) {
    invoke(
        RecipeBookmarkEvent.RemoveRecipeBookmark(
            title = recipe.title,
            position = index,
            recipeId = recipe.id,
            isRemoving = true
        )
    )
}
