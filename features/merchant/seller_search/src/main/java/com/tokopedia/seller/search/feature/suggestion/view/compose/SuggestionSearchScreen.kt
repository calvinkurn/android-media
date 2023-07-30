package com.tokopedia.seller.search.feature.suggestion.view.compose

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.flowlayout.FlowRow
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
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ARTICLES
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.common.util.indexOfSearchQuery
import com.tokopedia.seller.search.common.util.safeSetSpan
import com.tokopedia.seller.search.feature.initialsearch.view.compose.OPACITY_68
import com.tokopedia.seller.search.feature.suggestion.view.model.compose.SuggestionSearchUiEvent
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ArticleSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.FaqSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ProductSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHeaderSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemTitleHighlightSuggestionSearchUiModel

@Composable
fun SuggestionSearchScreen() {
}

@Composable
fun SearchResultArticleItem(
    item: ArticleSellerSearchUiModel,
    uiEvent: (SuggestionSearchUiEvent) -> Unit = {},
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

        val context = LocalContext.current

        val titleTextSpan =
            getSpannableString(context, item.keyword.orEmpty(), item.title.orEmpty())

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
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold
            ),
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
            textStyle = NestTheme.typography.display3.copy(
                fontWeight = FontWeight.Bold
            ),
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

        val context = LocalContext.current

        val titleTextSpan =
            getSpannableString(context, item.keyword.orEmpty(), item.title.orEmpty())

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
fun SearchResultProductItem(
    item: ProductSellerSearchUiModel,
    uiEvent: (SuggestionSearchUiEvent) -> Unit,
    position: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uiEvent(SuggestionSearchUiEvent.OnProductItemClicked(item, position))
            }
            .padding(8.dp)
    ) {
        val (image, title, description) = createRefs()

        val context = LocalContext.current

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
            getSpannableString(context, item.keyword.orEmpty(), item.title.orEmpty())

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
        text = item.title.toUpperCase(Locale.current),
        textStyle = NestTheme.typography.display3.copy(
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
            .padding(top = 8.dp, start = 16.dp, end = 16.dp)
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

@Composable
fun DividerSellerSearch() {
    NestDivider(
        size = NestDividerSize.Small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    )
}

fun getSpannableString(context: Context, keyword: String, title: String): SpannableString {
    val startIndex = indexOfSearchQuery(title, keyword)
    val highlightedTitle = SpannableString(title)

    return if (startIndex == -Int.ONE) {
        highlightedTitle
    } else {
        highlightedTitle.safeSetSpan(
            TextAppearanceSpan(context, R.style.searchTextHiglight),
            Int.ZERO,
            startIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        highlightedTitle.safeSetSpan(
            TextAppearanceSpan(context, R.style.searchTextHiglight),
            startIndex + keyword.length,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        highlightedTitle
    }
}

@Preview
@Composable
fun PreviewSearchResultItem() {
}
