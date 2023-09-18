package com.tokopedia.creation.common.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.applink.RouteManager
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationMediaModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify

/**
 * Created By : Muhammad Furqan on 06/09/23
 */

private const val clickableDescriptionTag = "applink"

@Composable
private fun buildDescriptionAnnotatedString(
    @StringRes descriptionTextId: Int,
    @StringRes clickTextId: Int
): AnnotatedString =
    buildAnnotatedString {
        append(stringResource(descriptionTextId))
        withStyle(
            SpanStyle(
                color = NestTheme.colors.GN._500,
                fontWeight = FontWeight.Bold
            )
        ) {
            pushStringAnnotation(clickableDescriptionTag, clickableDescriptionTag)
            append(stringResource(clickTextId))
        }
    }

@Composable
fun ExpandableContentCreationItem(
    isSelected: Boolean,
    data: ContentCreationItemModel,
    onSelect: (ContentCreationItemModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val description: AnnotatedString = when (data.type) {
        ContentCreationTypeEnum.LIVE -> buildDescriptionAnnotatedString(
            descriptionTextId = R.string.content_creation_live_description_text,
            clickTextId = R.string.content_creation_live_description_click_label
        )
        ContentCreationTypeEnum.POST -> buildDescriptionAnnotatedString(
            descriptionTextId = R.string.content_creation_post_description_text,
            clickTextId = R.string.content_creation_post_description_click_label
        )
        ContentCreationTypeEnum.SHORT -> buildDescriptionAnnotatedString(
            descriptionTextId = R.string.content_creation_short_description_text,
            clickTextId = R.string.content_creation_short_description_click_label
        )
        ContentCreationTypeEnum.STORY -> buildDescriptionAnnotatedString(
            descriptionTextId = R.string.content_creation_story_description_text,
            clickTextId = R.string.content_creation_story_description_click_label
        )
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, NestTheme.colors.NN._200, RoundedCornerShape(12.dp))
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        if (!isSelected) onSelect(data)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NestIcon(
                iconId = data.drawableIconId,
                contentDescription = data.title,
                colorLightEnable = NestTheme.colors.NN._900,
                colorNightEnable = NestTheme.colors.NN._900,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
            )

            NestTypography(
                text = data.title,
                textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            AndroidView(
                modifier = Modifier.wrapContentHeight(),
                factory = {
                    RadioButtonUnify(it).apply {
                        setOnCheckedChangeListener { _, isChecked ->
                            if (!isSelected && isChecked) {
                                onSelect(data)
                            }
                        }
                    }
                },
                update = {
                    it.isChecked = isSelected
                }
            )
        }

        AnimatedVisibility(visible = isSelected) {
            Column(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                NestTypography(text = description) { spannedRange: AnnotatedString.Range<String> ->
                    if (spannedRange.item.equals(clickableDescriptionTag, true)) {
                        RouteManager.route(context, data.descriptionApplink)
                    }
                }

                if (data.media.coverUrl.isNotEmpty() || data.media.mediaUrl.isNotEmpty()) {
                    NestImage(
                        source = ImageSource.Remote(
                            data.media.coverUrl.ifEmpty { data.media.mediaUrl }
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                            .aspectRatio(2f)
                            .clip(RoundedCornerShape(12.dp)),
                        contentDescription = description.toString()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ExpandableContentCreationItemPreview() {
    NestTheme {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            ExpandableContentCreationItem(
                isSelected = false,
                data = ContentCreationItemModel(
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
                onSelect = {
                }
            )
            Box(modifier = Modifier.height(16.dp))
            ExpandableContentCreationItem(
                isSelected = true,
                data = ContentCreationItemModel(
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
                    authorType = ContentCreationAuthorEnum.SHOP
                ),
                onSelect = {
                }
            )
        }
    }
}
