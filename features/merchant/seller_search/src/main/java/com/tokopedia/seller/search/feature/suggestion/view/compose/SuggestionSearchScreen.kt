package com.tokopedia.seller.search.feature.suggestion.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.flowlayout.FlowRow
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsSize
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ARTICLES
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.common.util.indexOfSearchQuery
import com.tokopedia.seller.search.feature.initialsearch.view.compose.OPACITY_68
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.compose.SuggestionSearchUiEvent
import com.tokopedia.seller.search.feature.suggestion.view.model.compose.SuggestionSearchUiState
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ArticleSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.DividerSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.FaqSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchSubItemUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ProductSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHeaderSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.HighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemTitleHighlightSuggestionSearchUiModel

@Composable
fun SuggestionSearchScreen(
    uiState: SuggestionSearchUiState?,
    uiEvent: (SuggestionSearchUiEvent) -> Unit
) {
    if (uiState?.isLoadingState == true) {
        SellerSearchShimmerCompose()
    } else {
        LazyColumn(
            Modifier
                .padding(bottom = 8.dp)
                .fillMaxSize()
        ) {
            itemsIndexed(uiState?.suggestionSellerSearchList.orEmpty()) { index, item ->
                when (item) {
                    is TitleHeaderSellerSearchUiModel -> {
                        TitleHeaderSellerSearch(item)
                    }

                    is TitleHasMoreSellerSearchUiModel -> {
                        TitleHasMoreSellerSearch(item, uiEvent)
                    }

                    is DividerSellerSearchUiModel -> {
                        DividerSellerSearch()
                    }

                    is OrderSellerSearchUiModel -> {
                        SearchResultOrderItem(item, index, uiEvent)
                    }

                    is ProductSellerSearchUiModel -> {
                        SearchResultProductItem(item, index, uiEvent)
                    }

                    is NavigationSellerSearchUiModel -> {
                        SearchResultNavigationItem(item, index, uiEvent)
                    }

                    is FaqSellerSearchUiModel -> {
                        SearchResultFaqItem(item = item, uiEvent = uiEvent, position = index)
                    }

                    is SellerSearchNoResultUiModel -> {
                        SellerSuggestionNoResult(uiEvent)
                    }

                    is ItemTitleHighlightSuggestionSearchUiModel -> {
                        TitleHighlightSuggestionSearch(item = item)
                    }

                    is HighlightSuggestionSearchUiModel -> {
                        ItemHighlightSuggestionChips(
                            chips = item.highlightSuggestionSearch,
                            uiEvent = uiEvent
                        )
                    }

                    is ArticleSellerSearchUiModel -> {
                        SearchResultArticleItem(item = item, uiEvent = uiEvent, position = index)
                    }
                }
            }
        }
    }
}

@Composable
fun SellerSuggestionNoResult(uiEvent: (SuggestionSearchUiEvent) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        val (tvTitleNoResult, tvDescNoResult) = createRefs()

        NestTypography(
            text = stringResource(id = com.tokopedia.seller.search.R.string.message_initial_search_no_result),
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.constrainAs(tvTitleNoResult) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        NestTypography(
            text = stringResource(id = com.tokopedia.seller.search.R.string.message_initial_search_try_another_keyword),
            textStyle = NestTheme.typography.display2,
            modifier = Modifier.constrainAs(tvDescNoResult) {
                top.linkTo(tvTitleNoResult.bottom, margin = 4.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
    uiEvent(SuggestionSearchUiEvent.OnSellerSearchNoResult)
}

@Composable
fun SearchResultArticleItem(
    item: ArticleSellerSearchUiModel,
    uiEvent: (SuggestionSearchUiEvent) -> Unit,
    position: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uiEvent(SuggestionSearchUiEvent.OnArticleItemClicked(item, position))
            }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        val (image, titleText, descText) = createRefs()

        val titleTextSpan =
            getSpannableAnnotatedString(item.keyword.orEmpty(), item.title.orEmpty())

        NestImage(
            source = ImageSource.Remote(item.imageUrl.orEmpty()),
            type = NestImageType.Rect(rounded = 4.dp),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .constrainAs(image) {
                    top.linkTo(titleText.top)
                    bottom.linkTo(descText.bottom)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        NestTypography(
            text = titleTextSpan,
            textStyle = NestTheme.typography.display2,
            maxLines = 2,
            modifier = Modifier.constrainAs(titleText) {
                start.linkTo(image.end, margin = 8.dp)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )

        NestTypography(
            text = item.desc.orEmpty(),
            textStyle = NestTheme.typography.display3,
            maxLines = 1,
            modifier = Modifier.constrainAs(descText) {
                start.linkTo(image.end, margin = 8.dp)
                end.linkTo(parent.end)
                top.linkTo(titleText.bottom, margin = 4.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun SearchResultFaqItem(
    item: FaqSellerSearchUiModel,
    uiEvent: (SuggestionSearchUiEvent) -> Unit,
    position: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uiEvent(SuggestionSearchUiEvent.OnFaqItemClicked(item, position))
            }
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        val (tvTitle, ivClose) = createRefs()

        val titleTextSpan =
            getSpannableAnnotatedString(item.keyword.orEmpty(), item.title.orEmpty())

        NestTypography(
            text = titleTextSpan,
            textStyle = NestTheme.typography.display2,
            modifier = Modifier.constrainAs(tvTitle) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(ivClose.start)
                width = Dimension.fillToConstraints
            }
        )

        NestImage(
            source = ImageSource.Painter(source = com.tokopedia.seller.search.R.drawable.ic_to_top),
            type = NestImageType.Rect(rounded = 0.dp),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(ivClose) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun SearchResultNavigationItem(
    item: NavigationSellerSearchUiModel,
    position: Int,
    uiEvent: (SuggestionSearchUiEvent) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uiEvent(SuggestionSearchUiEvent.OnNavigationItemClicked(item, position))
            }
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
    ) {
        val (ivSearchResultNav, tvTitleSearchResultNav, tvDescSearchResultNav, ivCloseHistory, rvSearchSubItem) = createRefs()

        NestImage(
            source = ImageSource.Remote(item.imageUrl.orEmpty()),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(ivSearchResultNav) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(32.dp)
                .padding(top = 4.dp)
        )

        val titleTextSpan =
            getSpannableAnnotatedString(item.keyword.orEmpty(), item.title.orEmpty())

        NestTypography(
            text = titleTextSpan,
            textStyle = NestTheme.typography.display2,
            modifier = Modifier.constrainAs(tvTitleSearchResultNav) {
                top.linkTo(ivSearchResultNav.top)
                start.linkTo(ivSearchResultNav.end, margin = 8.dp)
                width = Dimension.fillToConstraints
            }
        )

        NestTypography(
            text = item.desc.orEmpty(),
            textStyle = NestTheme.typography.display3,
            modifier = Modifier.constrainAs(tvDescSearchResultNav) {
                top.linkTo(tvTitleSearchResultNav.bottom, margin = 2.dp)
                start.linkTo(ivSearchResultNav.end, margin = 8.dp)
                end.linkTo(ivCloseHistory.start, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
        )

        NestImage(
            source = ImageSource.Painter(source = com.tokopedia.seller.search.R.drawable.ic_to_top),
            modifier = Modifier
                .constrainAs(ivCloseHistory) {
                    top.linkTo(ivSearchResultNav.top, margin = 4.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }
        )

        LazyRow(
            modifier = Modifier.constrainAs(rvSearchSubItem) {
                top.linkTo(tvDescSearchResultNav.bottom, margin = 12.dp)
                start.linkTo(tvDescSearchResultNav.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        ) {
            items(item.subItems) { subItem ->
                SearchSubItemChip(subItem, uiEvent)
            }
        }
    }
}

@Composable
fun SearchSubItemChip(
    item: NavigationSellerSearchSubItemUiModel,
    uiEvent: (SuggestionSearchUiEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(end = 8.dp)
            .drawBackgroundWithBorder(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.Transparent,
                borderColor = NestTheme.colors.NN._50,
                borderWidth = 1.dp
            )
            .clickable {
                uiEvent(SuggestionSearchUiEvent.OnNavigationSellerSearchSubItemClicked(item))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestTypography(
            text = item.title,
            textStyle = NestTheme.typography.display2,
            modifier = Modifier
                .padding(start = 12.dp, bottom = 10.dp, top = 10.dp)
        )

        NestIcon(
            iconId = IconUnify.CHEVRON_RIGHT,
            modifier = Modifier
                .height(24.dp)
                .padding(end = 2.dp),
            colorLightEnable = NestTheme.colors.NN._900
        )
    }
}

fun Modifier.drawBackgroundWithBorder(
    shape: Shape,
    backgroundColor: Color,
    borderColor: Color,
    borderWidth: Dp
): Modifier {
    return this.then(
        Modifier
            .background(
                color = backgroundColor,
                shape = shape
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
    )
}

@Composable
fun SearchResultOrderItem(
    item: OrderSellerSearchUiModel,
    position: Int,
    uiEvent: (SuggestionSearchUiEvent) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uiEvent(SuggestionSearchUiEvent.OnOrderItemClicked(item, position))
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val (tvSearchResultOrderTitle, tvSearchResultOrderDesc) = createRefs()

        val spannedAnnotatedString = getSpannableAnnotatedString(keyword = item.keyword.orEmpty(), title = item.title.orEmpty())

        NestTypography(
            text = spannedAnnotatedString,
            textStyle = NestTheme.typography.display2,
            modifier = Modifier.constrainAs(tvSearchResultOrderTitle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        NestTypography(
            text = item.desc.orEmpty(),
            textStyle = NestTheme.typography.display3,
            modifier = Modifier.constrainAs(tvSearchResultOrderDesc) {
                top.linkTo(tvSearchResultOrderTitle.bottom, margin = 4.dp)
                start.linkTo(tvSearchResultOrderTitle.start)
                end.linkTo(tvSearchResultOrderTitle.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun SearchResultProductItem(
    item: ProductSellerSearchUiModel,
    position: Int,
    uiEvent: (SuggestionSearchUiEvent) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uiEvent(SuggestionSearchUiEvent.OnProductItemClicked(item, position))
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val (image, title, description) = createRefs()

        NestImage(
            source = ImageSource.Remote(source = item.imageUrl.orEmpty()),
            type = NestImageType.Rect(rounded = 4.dp),
            modifier = Modifier
                .size(48.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        val textTitleSpan =
            getSpannableAnnotatedString(item.keyword.orEmpty(), item.title.orEmpty())

        NestTypography(
            text = textTitleSpan,
            textStyle = NestTheme.typography.display2,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(image.end, margin = 8.dp)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        NestTypography(
            text = item.desc.orEmpty(),
            textStyle = NestTheme.typography.display3,
            modifier = Modifier.constrainAs(description) {
                start.linkTo(image.end, margin = 8.dp)
                top.linkTo(title.bottom, margin = 4.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun TitleHighlightSuggestionSearch(
    item: ItemTitleHighlightSuggestionSearchUiModel
) {
    NestTypography(
        text = item.title.toUpperCase(Locale.current),
        textStyle = NestTheme.typography.display3.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950.copy(
                alpha = OPACITY_68
            )
        ),
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun TitleHasMoreSellerSearch(
    item: TitleHasMoreSellerSearchUiModel,
    uiEvent: (SuggestionSearchUiEvent) -> Unit
) {
    NestTypography(
        text = item.title,
        textStyle = NestTheme.typography.display2.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.GN._500
        ),
        modifier = Modifier
            .clickable {
                when (item.id) {
                    ORDER -> uiEvent(SuggestionSearchUiEvent.OnOrderMoreClicked(item))
                    PRODUCT -> uiEvent(SuggestionSearchUiEvent.OnProductMoreClicked(item))
                    FAQ -> uiEvent(SuggestionSearchUiEvent.OnFaqMoreClicked(item))
                    ARTICLES -> uiEvent(SuggestionSearchUiEvent.OnArticleMoreClicked(item))
                }
            }
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun TitleHeaderSellerSearch(
    item: TitleHeaderSellerSearchUiModel
) {
    NestTypography(
        text = item.title.toUpperCase(Locale.current),
        textStyle = NestTheme.typography.display3.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950.copy(
                alpha = OPACITY_68
            )
        ),
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ItemHighlightSuggestionChips(
    chips: List<ItemHighlightSuggestionSearchUiModel>,
    uiEvent: (SuggestionSearchUiEvent) -> Unit
) {
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .fillMaxWidth()
    ) {
        chips.forEachIndexed { index, item ->
            key(item.id.orEmpty()) {
                NestChips(
                    text = item.title.orEmpty(),
                    size = NestChipsSize.Medium,
                    state = NestChipsState.Default,
                    onClick = {
                        uiEvent(SuggestionSearchUiEvent.OnHighlightItemClicked(item, index))
                    }
                )
            }
        }
    }
}

@Composable
fun DividerSellerSearch() {
    NestDivider(
        size = NestDividerSize.Small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    )
}

@Composable
fun getSpannableAnnotatedString(keyword: String, title: String): AnnotatedString {
    val startIndex = indexOfSearchQuery(title, keyword)
    val style = SpanStyle(
        color = NestTheme.colors.NN._950,
        fontWeight = FontWeight.Bold
    )
    return buildAnnotatedString {
        append(title)
        if (startIndex != -Int.ONE) {
            addStyle(style, Int.ZERO, startIndex)
            addStyle(style, startIndex + keyword.length, title.length)
        }
    }
}

@Preview
@Composable
fun PreviewSearchResultItem() {
}
