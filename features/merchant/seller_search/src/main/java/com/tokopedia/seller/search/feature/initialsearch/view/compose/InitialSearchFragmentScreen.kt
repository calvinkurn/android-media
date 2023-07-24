package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.InitialSearchUiState

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
fun TitleInitialSearchSection() {
    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()
    ) {
        val (tvLatestSearch, tvClearAll) = createRefs()

        NestTypography(
            text = stringResource(id = R.string.latest_search_label),
            textStyle = NestTheme.typography.body3.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            ),
//            color = Color(0xFF444444),
            modifier = Modifier.constrainAs(tvLatestSearch) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )

        NestTypography(
            text = stringResource(id = R.string.clear_all_label),
            textStyle = NestTheme.typography.body3.copy(
                fontWeight = FontWeight.Bold
//                color = NestTheme.colors.
            ),
//            typographyType = TypographyType.Body3,
            modifier = Modifier
                .constrainAs(tvClearAll) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
