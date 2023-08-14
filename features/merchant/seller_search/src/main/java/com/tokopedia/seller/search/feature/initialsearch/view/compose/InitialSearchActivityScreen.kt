package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState
import kotlinx.coroutines.delay

const val DELAY_SHOW_KEYBOARD = 100L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InitialSearchActivityScreen(
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEvent) -> Unit = {},
    showSearchSuggestions: Boolean,
    initialStateContainerId: Int,
    suggestionSearchContainerId: Int,
    softwareKeyboardController: SoftwareKeyboardController?
) {
    val searchBarFocusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LaunchedEffect(true) {
                searchBarFocusRequester.requestFocus()
                delay(DELAY_SHOW_KEYBOARD)
                softwareKeyboardController?.show()
            }

            GlobalSellerSearchView(
                uiState = uiState,
                uiEffect = uiEffect,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                focusRequester = searchBarFocusRequester,
                keyboardController = softwareKeyboardController
            )

            if (showSearchSuggestions) {
                SearchSuggestionFragmentContainer(
                    modifier = Modifier.fillMaxSize(),
                    containerId = suggestionSearchContainerId
                )
            } else {
                InitialSearchFragmentContainer(
                    modifier = Modifier.fillMaxSize(),
                    containerId = initialStateContainerId
                )
            }
        }
    }
}

@Composable
fun SearchSuggestionFragmentContainer(
    modifier: Modifier = Modifier,
    containerId: Int
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            FragmentContainerView(context).apply { id = containerId }
        }
    )
}

@Composable
fun InitialSearchFragmentContainer(
    modifier: Modifier = Modifier,
    containerId: Int
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            FragmentContainerView(context).apply { id = containerId }
        }
    )
}
