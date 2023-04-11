package com.tokopedia.product.detail.view.viewmodel.product_detail.base

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

/***
 * [SubViewModel] is separating the UI logic in the main viewmodel into a smaller viewmodel by delegating the sub-viewmodel.
 * Separate event and state according to their respective contexts.
 */
abstract class SubViewModel(
    subViewModelScope: SubViewModelScope
) : SubViewModelScope by subViewModelScope
