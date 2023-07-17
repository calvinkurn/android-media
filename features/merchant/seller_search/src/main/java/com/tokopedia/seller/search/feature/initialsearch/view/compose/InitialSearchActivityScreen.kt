package com.tokopedia.seller.search.feature.initialsearch.view.compose

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEffect
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment

@Composable
fun InitialSearchActivityScreen(
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEffect) -> Unit = {},
    fragmentManager: FragmentManager? = null,
    initialSearchFragment: Fragment,
    suggestionSearchFragment: Fragment
) {
    val searchKeyword = remember { mutableStateOf("") }
    val showSearchSuggestions =
        searchKeyword.value.length >= GlobalSearchSellerConstant.MIN_KEYWORD_SEARCH

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
//            if (showSearchSuggestions) {
//                SuggestionSearchFragmentWrapper(fragmentManager = fragmentManager, fragment = suggestionSearchFragment)
//            } else {
//                InitialSearchFragmentWrapper(fragmentManager = fragmentManager, fragment = initialSearchFragment)
//            }
        }
    }
}

@Composable
fun SuggestionSearchFragmentWrapper(fragmentManager: FragmentManager?, fragment: Fragment) {
    LaunchedEffect(Unit) {
        fragmentManager?.beginTransaction()?.apply {
            replace(android.R.id.content, fragment)
            commit()
        }
    }
}

@Composable
fun InitialSearchFragmentWrapper(fragmentManager: FragmentManager?, fragment: Fragment) {
    LaunchedEffect(Unit) {
        fragmentManager?.beginTransaction()?.apply {
            replace(android.R.id.content, fragment)
            commit()
        }
    }
}

@Composable
fun detachViewFromParent(view: View) {
    val parent = view.parent
    if (parent is ViewGroup) {
        parent.removeView(view)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInitialSearchActivityScreen() {
    InitialSearchActivityScreen(
        uiState = GlobalSearchUiState(
            searchBarKeyword = "",
            searchBarPlaceholder = "coba ketik pesan"
        ),
        fragmentManager = null,
        initialSearchFragment = InitialSearchFragment(),
        suggestionSearchFragment = SuggestionSearchFragment()
    )
}
