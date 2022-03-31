package com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class TokoFoodOrderTrackingViewModel @Inject constructor(
    coroutineDispatchers: CoroutineDispatchers
): BaseViewModel(coroutineDispatchers.io) {

}