package com.tokopedia.cmhomewidget.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.cmhomewidget.di.qualifier.CoroutineMainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

// todo delete cm home widget dummy things
class DummyTestCMHomeWidgetViewModel @Inject constructor(
    private val cmHomeWidgetDataUseCase: CMHomeWidgetDataUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

}