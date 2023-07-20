package com.tokopedia.seller.search.feature.initialsearch.view.compose

import android.widget.FrameLayout
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiEffect
import com.tokopedia.seller.search.feature.initialsearch.view.model.compose.GlobalSearchUiState
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment
import kotlinx.coroutines.delay

const val DELAY_SHOW_KEYBOARD = 100L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InitialSearchActivityScreen(
    uiState: GlobalSearchUiState,
    uiEffect: (GlobalSearchUiEffect) -> Unit = {},
    supportFragmentManager: FragmentManager? = null
) {
    val context = LocalContext.current

    val searchKeyword = remember { mutableStateOf("") }
    val showSearchSuggestions =
        searchKeyword.value.length >= GlobalSearchSellerConstant.MIN_KEYWORD_SEARCH

    val searchBarFocusRequester = remember { FocusRequester() }

    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .background(NestNN.light._0)
            .fillMaxSize()
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
                    searchKeyword.value = it
                },
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                focusRequester = searchBarFocusRequester,
                keyboardController = softwareKeyboardController
            )
//            if (showSearchSuggestions) {
//                SuggestionSearchContainer(fragmentManager = fragmentManager)
//                fragmentManager?.showFragment(SuggestionSearchFragment::class.java)
//            } else {
//                InitialStateContainer(fragmentManager = fragmentManager)
//                fragmentManager?.showFragment(InitialSearchFragment::class.java)
//            }

            val fragmentContainerView = remember {
                FrameLayout(context).apply {
                    id = ViewCompat.generateViewId()
                }
            }

            val initialSearchContainer = remember {
                FrameLayout(context).apply {
                    id = ViewCompat.generateViewId()
                }
            }
            val suggestionSearchContainer = remember { FrameLayout(context) }

//            AndroidView(
//                factory = { context ->
//                    FragmentContainerView(context).apply {
//                        id = ViewCompat.generateViewId()
//                        layoutParams = ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT
//                        )
//                    }
//                },
//                update = { fragmentContainerView ->
//                    showFragmentSearch(supportFragmentManager, fragmentContainerView, showSearchSuggestions)
//                }
//            )
        }
    }
}

private fun showFragmentSearch(
    fragmentManager: FragmentManager?,
    fragmentContainerView: FrameLayout,
    showSearchSuggestions: Boolean
) {
    val suggestionSearchFragment =
        fragmentManager?.findFragmentByTag(SuggestionSearchFragment::class.java.simpleName)
    val initialSearchFragment =
        fragmentManager?.findFragmentByTag(InitialSearchFragment::class.java.simpleName)

    val transaction = fragmentManager?.beginTransaction()

    if (showSearchSuggestions) {
        if (initialSearchFragment != null) {
            transaction?.hide(initialSearchFragment)
        }

        if (suggestionSearchFragment == null) {
            transaction?.add(
                fragmentContainerView.id,
                SuggestionSearchFragment.newInstance(),
                SuggestionSearchFragment::class.java.simpleName
            )
        } else {
            transaction?.show(suggestionSearchFragment)
        }
    } else {
        if (suggestionSearchFragment != null) {
            transaction?.hide(suggestionSearchFragment)
        }

        if (initialSearchFragment == null) {
            transaction?.add(
                fragmentContainerView.id,
                InitialSearchFragment.newInstance(),
                InitialSearchFragment::class.java.simpleName
            )
        } else {
            transaction?.show(initialSearchFragment)
        }
    }

    transaction?.commit()
}

@Composable
fun InitialStateContainer(fragmentManager: FragmentManager?) {
    AndroidView(
        factory = { context ->
            FragmentContainerView(context).apply {
                id = ViewCompat.generateViewId()
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { fragmentContainerView ->
            // Check if the fragment is already added to the container
            showInitialState(fragmentManager, fragmentContainerView)
        }
    )
}

@Composable
fun SuggestionSearchContainer(fragmentManager: FragmentManager?) {
    AndroidView(
        factory = { context ->
            FragmentContainerView(context).apply {
                id = ViewCompat.generateViewId()
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { fragmentContainerView ->
            // Check if the fragment is already added to the container
            showSearchSuggestion(fragmentManager, fragmentContainerView)
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
        if (existingFragment.javaClass != InitialSearchFragment::class.java) {
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

@Preview(showBackground = true)
@Composable
fun PreviewInitialSearchActivityScreen() {
    InitialSearchActivityScreen(
        uiState = GlobalSearchUiState(
            searchBarKeyword = "",
            searchBarPlaceholder = "coba ketik pesan"
        )
    )
}
