package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.nest.components.NestSearchBar
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEffect
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState

@Composable
fun GlobalSellerSearchView(
    modifier: Modifier,
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEffect) -> Unit,
    onSearchBarTextChanged: (String) -> Unit
) {
    ConstraintLayout(
        modifier = modifier,
        content = {
            val (backBtn, searchBarUnify) = createRefs()

            Image(
                modifier = Modifier
                    .constrainAs(backBtn) {
                        top.linkTo(searchBarUnify.top)
                        bottom.linkTo(searchBarUnify.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 12.dp)
                    .clickable { uiEffect(GlobalSearchUiEffect.OnBackButtonClicked(uiState.searchBarKeyword)) },
                painter = painterResource(id = R.drawable.ic_back_searchbar),
                contentDescription = null
            )

            SearchBarUnify(
                modifier = Modifier.constrainAs(searchBarUnify) {
                    top.linkTo(parent.top)
                    start.linkTo(backBtn.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
                uiState = uiState,
                uiEffect = uiEffect,
                onSearchBarTextChanged = onSearchBarTextChanged
            )
        }
    )
}

@Composable
private fun SearchBarUnify(
    modifier: Modifier,
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEffect) -> Unit,
    onSearchBarTextChanged: (String) -> Unit
) {
    NestSearchBar(
        placeholderText = uiState.searchBarKeyword.ifBlank { stringResource(id = R.string.placeholder_search_seller) },
        modifier = modifier,
        onSearchBarCleared = {
            uiEffect(GlobalSearchUiEffect.OnSearchBarCleared)
            onSearchBarTextChanged(String.EMPTY)
            uiEffect(GlobalSearchUiEffect.OnKeywordTextChanged(String.EMPTY))
        },
        onKeyboardSearchAction = {
            uiEffect(GlobalSearchUiEffect.OnKeyboardSearchSubmit(it))
        },
        onTextChanged = {
            onSearchBarTextChanged(it)
            uiEffect(GlobalSearchUiEffect.OnKeywordTextChanged(it))
        }
    )
}
