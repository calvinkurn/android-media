package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEffect
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment

@Composable
fun InitialSearchActivityScreen(
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEffect) -> Unit = {},
    initialSearchFragment: InitialSearchFragment?,
    searchSuggestionFragment: SuggestionSearchFragment?
) {
    val searchKeyword = remember { mutableStateOf("") }
    val showSearchSuggestions = searchKeyword.value.length >= 3

    Surface(
        modifier = Modifier
            .background(NestNN.light._0)
            .fillMaxSize()
    ) {
        Column {
            GlobalSellerSearchView(
                uiState = uiState,
                uiEffect = uiEffect,
                onSearchBarTextChanged = {
                    searchKeyword.value = it
                },
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            )
            if (showSearchSuggestions) {
                val suggestionView = searchSuggestionFragment?.view
                suggestionView?.let {
                    AndroidView(
                        factory = { context ->
                            suggestionView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                val initialSearchView = initialSearchFragment?.view
                initialSearchView?.let {
                    AndroidView(
                        factory = { context ->
                            it
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInitialSearchActivityScreen() {
    InitialSearchActivityScreen(
        uiState = GlobalSearchUiState(
            searchBarKeyword = "baju",
            searchBarPlaceholder = "coba ketik pesan"
        ),
        initialSearchFragment = InitialSearchFragment(),
        searchSuggestionFragment = SuggestionSearchFragment()
    )
}
