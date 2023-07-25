package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsSize
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiState

const val OPACITY_68 = 0.68f

@Composable
fun InitialSearchFragmentScreen(
    uiState: InitialSearchUiState
) {
    LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(uiState.initialStateList) { index, item ->
        }
    }
}

@Composable
fun HistorySearchSectionTitle(
    uiEvent: (InitialSearchUiEvent) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val (tvLatestSearch, tvClearAll) = createRefs()

        NestTypography(
            text = stringResource(id = R.string.latest_search_label).toUpperCase(Locale.current),
            textStyle = NestTheme.typography.body3.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950.copy(
                    alpha = OPACITY_68
                )
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .constrainAs(tvLatestSearch) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        NestTypography(
            text = stringResource(id = R.string.clear_all_label),
            textStyle = NestTheme.typography.body3.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.GN._500
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .constrainAs(tvClearAll) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .clickable {
                    uiEvent(InitialSearchUiEvent.OnClearAllKeyword)
                }
        )
    }
}

@Composable
fun ItemHistorySearch(
    title: String,
    position: Int,
    uiEvent: (InitialSearchUiEvent) -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { /* Handle item click */ }
    ) {
        val (ivHistoryTime, tvTitleHistory, ivCloseHistory) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_time_history),
            contentDescription = "History Time",
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
            text = title,
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
            source = ImageSource.Painter(R.drawable.ic_close_history),
            type = NestImageType.Rect(rounded = 0.dp),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(ivCloseHistory) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .clickable {
                    uiEvent(InitialSearchUiEvent.OnItemRemoveClicked(title, position))
                }
                .padding(start = 4.dp, end = 8.dp)
        )
    }
}

@Composable
fun TitleSearchRecommendation() {
    NestTypography(
        text = stringResource(id = R.string.highlight_search_label).toUpperCase(Locale.current),
        textStyle = NestTheme.typography.body3.copy(
            fontWeight = FontWeight.Bold,
            color = NestTheme.colors.NN._950.copy(
                alpha = OPACITY_68
            )
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ItemHighlightChips() {
}

@Composable
fun ChipsLayoutManager(chips: List<String>) {
//    var availableWidth by remember { mutableStateOf(0.dp) }
//
//    BoxWithConstraints {
//        availableWidth = maxWidth
//
//        var chipsInRow by remember { mutableStateOf(emptyList<String>()) }
//
//        Column {
//            var currentRowWidth by remember { mutableStateOf(0.dp) }
//
//            chips.forEach { chipText ->
//                val chipWidth = with(LocalDensity.current) {
//                    (32 * chipText.length).dp
//                }
//                if (currentRowWidth + chipWidth <= availableWidth) {
//                    currentRowWidth += chipWidth + 8.dp
//                    chipsInRow = chipsInRow + chipText
//                } else {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        for (chip in chipsInRow) {
//                            NestChips(
//                                text = chip,
//                                size = NestChipsSize.Medium,
//                                state = NestChipsState.Default,
//                                modifier = Modifier.padding(top = 8.dp)
//                            )
//                        }
//                    }
//                    chipsInRow = listOf(chipText)
//                    currentRowWidth = chipWidth + 8.dp
//                }
//            }
//
//            if (chipsInRow.isNotEmpty()) {
//                Row(
//                    modifier = Modifier.fillMaxHeight(),
//                    horizontalArrangement = Arrangement.Start
//                ) {
//                    for (chip in chipsInRow) {
//                        NestChips(
//                            text = chip,
//                            size = NestChipsSize.Medium,
//                            state = NestChipsState.Default,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            chips.forEach { chip ->
                NestChips(
                    text = chip,
                    size = NestChipsSize.Medium,
                    state = NestChipsState.Default,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview()
@Composable
fun TitleHistorySearchSectionPreview() {
    ChipsLayoutManager(
        chips = listOf(
            "Dapatkan Gratis Broadcast Chat Disini",
            "TopAds",
            "Apa itu fitur Produk Unggulan?",
            "Wawasan Toko",
            "Apa itu Kupon Toko Saya?",
            "Gunakan bebas Ongkir"
        )
    )
}
