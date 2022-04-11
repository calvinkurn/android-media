package com.tokopedia.tokofood.home.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class TokoFoodHomeViewModel @Inject constructor(val dispatchers: CoroutineDispatchers):
    BaseViewModel(dispatchers.main) {

}