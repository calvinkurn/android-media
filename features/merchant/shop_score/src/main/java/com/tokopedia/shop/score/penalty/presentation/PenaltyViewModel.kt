package com.tokopedia.shop.score.penalty.presentation

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class PenaltyViewModel @Inject constructor(
        private val coroutineDispatchers: CoroutineDispatchers): BaseViewModel(coroutineDispatchers.main) {

}