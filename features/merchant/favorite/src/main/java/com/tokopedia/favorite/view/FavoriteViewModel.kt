package com.tokopedia.favorite.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import javax.inject.Inject

class FavoriteViewModel
@Inject constructor(
    private val dispatcherProvider: DispatcherProvider
): BaseViewModel(dispatcherProvider.ui()) {



}