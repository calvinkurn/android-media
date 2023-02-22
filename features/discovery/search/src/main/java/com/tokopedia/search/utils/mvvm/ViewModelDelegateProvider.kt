package com.tokopedia.search.utils.mvvm

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.LazyThreadSafetyMode.NONE

inline fun <T, reified VM, reified S: SearchUiState> T.fragmentViewModel(
    viewModelProviderFactory: ViewModelProvider.Factory?,
): Lazy<VM?> where
    T: Fragment,
    T: SearchView,
    VM: ViewModel,
    VM: SearchViewModel<S> {

    return lazy(NONE) {
        viewModelProviderFactory?.let {
            ViewModelProvider(this, it)[VM::class.java]
        }?.apply { observeState() }
    }
}
