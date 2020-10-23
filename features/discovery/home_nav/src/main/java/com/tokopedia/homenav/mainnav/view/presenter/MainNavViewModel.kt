package com.tokopedia.homenav.mainnav.view.presenter

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import dagger.Lazy
import javax.inject.Inject

class MainNavViewModel @Inject constructor(
    private val baseDispatcher: Lazy<NavDispatcherProvider>
): BaseViewModel(baseDispatcher.get().io()) {


}