package com.tokopedia.statistic.presentation.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticViewModel(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

}