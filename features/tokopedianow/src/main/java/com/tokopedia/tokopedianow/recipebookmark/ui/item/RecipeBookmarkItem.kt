package com.tokopedia.tokopedianow.recipebookmark.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.ListItemImpression
import com.tokopedia.tokopedianow.recipebookmark.analytics.RecipeBookmarkAnalytics
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipebookmark.ui.model.RecipeBookmarkEvent
import com.tokopedia.tokopedianow.recipecommon.ui.item.RecipeTagItem

@Composable
fun RecipeBookmarkItem(
    position: Int,
    recipe: RecipeUiModel,
    analytics: RecipeBookmarkAnalytics,
    lazyListState: LazyListState,
    onEvent: (RecipeBookmarkEvent) -> Unit
) {
    val id = recipe.id
    val title = recipe.title
    val duration = recipe.duration
    val picture = recipe.picture
    val portion = recipe.portion
    val tags = recipe.tags.orEmpty()

    val context = LocalContext.current

    ListItemImpression(key = recipe.getUniqueId(), lazyListState = lazyListState) {
        analytics.impressRecipeCard(id, title, position)
    }

    NestCard(
        modifier = Modifier.fillMaxWidth(),
        type = NestCardType.Border,
        onClick = {
            val appLink = UriUtil.buildUriAppendParam(
                ApplinkConstInternalTokopediaNow.RECIPE_DETAIL,
                mapOf(PARAM_RECIPE_ID to id)
            )
            RouteManager.route(context, appLink)
            analytics.clickRecipeCard(id, title, position)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            NestImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                source = ImageSource.Remote(source = picture),
                contentScale = ContentScale.Crop,
                type = NestImageType.Rect(0.dp)
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
                    textStyle = NestTheme.typography.display3
                        .copy(fontWeight = FontWeight.Bold)
                )
                NestIcon(
                    modifier = Modifier
                        .height(15.dp)
                        .width(15.dp)
                        .clickable {
                            onEvent.removeRecipeBookmark(position, recipe)
                            analytics.clickUnBookmark(id, title)
                        },
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
                textStyle = NestTheme.typography.small
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    start = 4.dp
                )
        ) {
            items(tags.count()) {
                RecipeTagItem(data = tags[it])
            }
        }
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
