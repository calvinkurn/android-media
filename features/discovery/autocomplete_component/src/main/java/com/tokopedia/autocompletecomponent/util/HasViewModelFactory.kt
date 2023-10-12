package com.tokopedia.autocompletecomponent.util

import androidx.lifecycle.ViewModelProvider

interface HasViewModelFactory {
    val viewModelFactory: ViewModelProvider.Factory?
}
