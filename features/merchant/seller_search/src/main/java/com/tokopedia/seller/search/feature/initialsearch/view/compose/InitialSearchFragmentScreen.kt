package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.flowlayout.FlowRow
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsSize
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiState
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.HighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleInitialSearchUiModel

const val OPACITY_68 = 0.68f

@Composable
fun InitialSearchFragmentScreen(
    uiState: InitialSearchUiState?,
    uiEvent: (InitialSearchUiEvent) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(uiState?.initialStateList.orEmpty()) { index, item ->
            when (item) {
                is SellerSearchNoHistoryUiModel -> {
                    NoHistoryState()
                }

                is ItemTitleInitialSearchUiModel -> {
                    HistorySearchSectionTitle(uiState?.titleList.orEmpty(), uiEvent)
                }

                is ItemInitialSearchUiModel -> {
                    ItemHistorySearch(item, index, uiEvent)
                }

                is ItemTitleHighlightInitialSearchUiModel -> {
                    TitleSearchRecommendation()
                }

                is HighlightInitialSearchUiModel -> {
                    ItemHighlightChips(item.highlightInitialList, uiEvent)
                }
            }
        }
    }
}

@Composable
fun HistorySearchSectionTitle(
    titleList: List<String>,
    uiEvent: (InitialSearchUiEvent) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        val (tvLatestSearch, tvClearAll) = createRefs()

        NestTypography(
            text = stringResource(id = com.tokopedia.seller.search.R.string.latest_search_label).toUpperCase(
                Locale.current
            ),
            textStyle = NestTheme.typography.body3.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950.copy(
                    alpha = OPACITY_68
                )
            ),
            modifier = Modifier
                .padding(top = 24.dp, start = 16.dp)
                .constrainAs(tvLatestSearch) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        NestTypography(
            text = stringResource(id = com.tokopedia.seller.search.R.string.clear_all_label),
            textStyle = NestTheme.typography.body3.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.GN._500
            ),
            modifier = Modifier
                .padding(top = 24.dp, end = 16.dp)
                .constrainAs(tvClearAll) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clickable {
                    uiEvent(InitialSearchUiEvent.OnClearAllHistory(titleList))
                }
        )
    }
}

@Composable
fun ItemHistorySearch(
    item: ItemInitialSearchUiModel,
    position: Int,
    uiEvent: (InitialSearchUiEvent) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .clickable {
                uiEvent(InitialSearchUiEvent.OnItemHistoryClicked(item.title.orEmpty(), position))
            }
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        val (ivHistoryTime, tvTitleHistory, ivCloseHistory) = createRefs()

        NestImage(
            source = ImageSource.Painter(com.tokopedia.seller.search.R.drawable.ic_time_history),
            type = NestImageType.Rect(rounded = 0.dp),
            modifier = Modifier
                .constrainAs(ivHistoryTime) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .size(24.dp)
                .padding(end = 8.dp)
        )

        NestTypography(
            text = item.title.orEmpty(),
            textStyle = NestTheme.typography.body1,
            modifier = Modifier.constrainAs(tvTitleHistory) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(ivHistoryTime.end)
                end.linkTo(ivCloseHistory.start)
                width = Dimension.fillToConstraints
            }
        )

        NestImage(
            source = ImageSource.Painter(com.tokopedia.seller.search.R.drawable.ic_close_history),
            type = NestImageType.Rect(rounded = 0.dp),
            modifier = Modifier
                .constrainAs(ivCloseHistory) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .clickable {
                    uiEvent(
                        InitialSearchUiEvent.OnItemRemoveClicked(
                            item.title.orEmpty(),
                            position
                        )
                    )
                }
                .padding(start = 4.dp, end = 8.dp)
        )
    }
}

@Composable
fun TitleSearchRecommendation() {
    NestTypography(
        text = stringResource(id = com.tokopedia.seller.search.R.string.highlight_search_label).toUpperCase(
            Locale.current
        ),
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
fun ItemHighlightChips(
    chips: List<ItemHighlightInitialSearchUiModel>,
    uiEvent: (InitialSearchUiEvent) -> Unit
) {
    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .fillMaxWidth()
    ) {
        chips.forEachIndexed { index, item ->
            key(item.id.orEmpty()) {
                NestChips(
                    text = item.title.orEmpty(),
                    size = NestChipsSize.Medium,
                    state = NestChipsState.Default,
                    onClick = {
                        uiEvent(InitialSearchUiEvent.OnItemHighlightClicked(item, index))
                    }
                )
            }
        }
    }
}

@Composable
fun NoHistoryState() {
    ConstraintLayout(
        modifier = Modifier
            .padding(end = 16.dp, start = 16.dp, top = 16.dp)
            .fillMaxWidth()
    ) {
        val (ivNoHistory, tvTitleNoHistory, tvDescNoHistory) = createRefs()

        NestImage(
            modifier = Modifier
                .width(140.dp)
                .height(105.dp)
                .constrainAs(ivNoHistory) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            source = ImageSource.Remote(source = GlobalSearchSellerConstant.IC_NO_HISTORY_URL)
        )

        NestTypography(
            modifier = Modifier
                .constrainAs(tvTitleNoHistory) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(ivNoHistory.end, margin = 4.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            textStyle = NestTheme.typography.display3.copy(fontWeight = FontWeight.Bold),
            text = stringResource(id = R.string.title_no_history_label)
        )

        NestTypography(
            modifier = Modifier
                .constrainAs(tvDescNoHistory) {
                    top.linkTo(tvTitleNoHistory.bottom, margin = 4.dp)
                    start.linkTo(ivNoHistory.end, margin = 4.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            textStyle = NestTheme.typography.display3,
            text = stringResource(id = com.tokopedia.seller.search.R.string.desc_no_history_label)
        )
    }
}

@Preview
@Composable
fun TitleHistorySearchSectionPreview() {
    ItemHighlightChips(
        chips =
        listOf(
            ItemHighlightInitialSearchUiModel(title = "Cara Menjadi Power Merchant"),
            ItemHighlightInitialSearchUiModel(title = "Bebas Ongkir"),
            ItemHighlightInitialSearchUiModel(title = "Cara Menjadi Power Merchant"),
            ItemHighlightInitialSearchUiModel(title = "Cara Pakai Bebas Ongkir"),
            ItemHighlightInitialSearchUiModel(title = "Cara Menjadi Power Merchant"),
            ItemHighlightInitialSearchUiModel(title = "Cara Pakai Bebas Ongkir")
        )
    ) {}
}
