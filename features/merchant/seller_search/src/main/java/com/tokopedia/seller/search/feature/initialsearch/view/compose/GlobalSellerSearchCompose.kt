package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.nest.components.NestSearchBar
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEffect
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GlobalSellerSearchView(
    modifier: Modifier,
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEffect) -> Unit,
    onSearchBarTextChanged: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        val focusRequester = remember { FocusRequester() }

        Image(
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable {
                    uiEffect(GlobalSearchUiEffect.OnBackButtonClicked(uiState.searchBarKeyword))
                    keyboardController?.hide()
                },
            painter = painterResource(id = R.drawable.ic_back_searchbar),
            contentDescription = null
        )

        SearchBarUnify(
            modifier = Modifier
                .height(56.dp)
                .weight(1f)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusRequester.requestFocus()
                    }
                },
            uiState = uiState,
            uiEffect = uiEffect,
            onSearchBarTextChanged = onSearchBarTextChanged
        )
    }
}

@Composable
private fun SearchBarUnify(
    modifier: Modifier,
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEffect) -> Unit,
    onSearchBarTextChanged: (String) -> Unit
) {
    NestSearchBar(
        placeholderText = uiState.searchBarPlaceholder.ifBlank { stringResource(id = R.string.placeholder_search_seller) },
        modifier = modifier,
        onSearchBarCleared = {
            uiEffect(GlobalSearchUiEffect.OnSearchBarCleared)
            onSearchBarTextChanged(String.EMPTY)
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
