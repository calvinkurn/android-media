package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

class TkpdViewModelScopeProvider : ViewModelScopeProvider {
    private var viewModel: ViewModel? = null

    override fun register(viewModel: ViewModel) {
        this.viewModel = viewModel
    }

    override val vmScope: CoroutineScope?
        get() = viewModel?.viewModelScope

    override fun close() {
        viewModel = null
    }
}
