package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.components.NestSearchBar
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GlobalSellerSearchView(
    modifier: Modifier,
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEvent) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NestImage(
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable {
                    uiEffect(GlobalSearchUiEvent.OnBackButtonClicked(""))
                    keyboardController?.hide()
                },
            source = ImageSource.Painter(R.drawable.ic_back_searchbar),
            contentDescription = null,
            type = NestImageType.Rect(rounded = 0.dp)
        )

        SearchBarUnify(
            modifier = Modifier
                .height(56.dp)
                .weight(1f)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusRequester.requestFocus()
                    }
                },
            searchBarKeyword = uiState.searchBarKeyword,
            searchBarPlaceholder = uiState.searchBarPlaceholder,
            uiEffect = uiEffect
        )
    }
}

@Composable
private fun SearchBarUnify(
    modifier: Modifier,
    searchBarKeyword: String,
    searchBarPlaceholder: String,
    uiEffect: (GlobalSearchUiEvent) -> Unit
) {
    val nn950Color = NestTheme.colors.NN._950

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = nn950Color)) {
                        append(searchBarKeyword)
                    }
                },
                selection = TextRange(searchBarKeyword.length)
            )
        )
    }

    NestSearchBar(
        value = textFieldValue,
        placeholderText = searchBarPlaceholder.ifBlank { stringResource(id = R.string.placeholder_search_seller) },
        modifier = modifier,
        onSearchBarCleared = {
            uiEffect(GlobalSearchUiEvent.OnSearchBarCleared)
        },
        onKeyboardSearchAction = {
            uiEffect(GlobalSearchUiEvent.OnKeyboardSearchSubmit(searchBarKeyword))
        },
        onTextChanged = {
            textFieldValue = it
            uiEffect(GlobalSearchUiEvent.OnKeywordTextChanged(it.text))
        }
    )
}
