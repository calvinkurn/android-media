package com.tokopedia.seller.search.feature.initialsearch.view.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEvent
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment
import kotlinx.coroutines.delay

const val DELAY_SHOW_KEYBOARD = 100L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InitialSearchActivityScreen(
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEvent) -> Unit = {},
    stateKeyword: MutableState<String>,
    showSearchSuggestions: Boolean,
    initialStateContainerId: Int,
    suggestionSearchContainerId: Int
) {
    val searchBarFocusRequester = remember { FocusRequester() }

    val softwareKeyboardController = LocalSoftwareKeyboardController.current

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
                onSearchBarTextChanged = {
                    stateKeyword.value = it
                },
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

fun showInitialState(
    fragmentManager: FragmentManager?,
    fragmentContainerView: FragmentContainerView
) {
    val fragmentTag = InitialSearchFragment::class.java.simpleName
    val fragment = fragmentManager?.findFragmentByTag(fragmentTag)
    if (fragment == null) {
        fragmentManager?.beginTransaction()
            ?.replace(fragmentContainerView.id, InitialSearchFragment(), fragmentTag)
            ?.commit()
    } else {
        // Show existing fragment
        fragmentManager.beginTransaction().show(fragment).commit()
    }

    // Hide other fragments if any
    fragmentManager?.fragments?.forEach { existingFragment ->
        if (existingFragment.javaClass != fragment?.javaClass) {
            fragmentManager.beginTransaction().hide(existingFragment).commit()
        }
    }
}

fun showSearchSuggestion(
    fragmentManager: FragmentManager?,
    fragmentContainerView: FragmentContainerView
) {
    val fragmentTag = SuggestionSearchFragment::class.java.simpleName
    val fragment = fragmentManager?.findFragmentByTag(fragmentTag)
    if (fragment == null) {
        fragmentManager?.beginTransaction()
            ?.replace(fragmentContainerView.id, SuggestionSearchFragment(), fragmentTag)
            ?.commit()
    } else {
        // Show existing fragment
        fragmentManager.beginTransaction().show(fragment).commit()
    }

    // Hide other fragments if any
    fragmentManager?.fragments?.forEach { existingFragment ->
        if (existingFragment.javaClass != SuggestionSearchFragment::class.java) {
            fragmentManager.beginTransaction().hide(existingFragment).commit()
        }
    }
}
