package com.tokopedia.product.detail.view.viewmodel.product_detail.base

import androidx.lifecycle.ViewModel
import java.io.Closeable

interface ViewModelScopeProvider : Closeable, ViewModelSlice {
    fun register(viewModel: ViewModel)
}
