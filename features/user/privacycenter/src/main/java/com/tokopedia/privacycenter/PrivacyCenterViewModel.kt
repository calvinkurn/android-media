package com.tokopedia.privacycenter

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.domain.DarkModeUseCase
import javax.inject.Inject

class PrivacyCenterViewModel @Inject constructor(
    private val darkModeUseCase: DarkModeUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    init {
    }

    fun isUsingDarkMode(): Boolean {
        return darkModeUseCase.isDarkModeActivated()
    }

}
