package com.tokopedia.usercomponents.explicit.view

import com.tokopedia.usercomponents.explicit.domain.model.OptionsItem
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewContract

interface ExplicitAction {
    /**
     * setupView should be called once at the first time.
     * This method is required to call for setting up the
     * ExplicitView component.
     */
    fun setupView(
        explicitViewContract: ExplicitViewContract,
        data: ExplicitData
    )
    fun isViewAttached(): Boolean

    fun onLoading()
    fun onQuestionShow()
    fun onButtonPositiveClicked()
    fun onButtonNegativeClicked()
    fun onSubmitSuccessShow(data: OptionsItem?)
    fun onDismiss()
    fun onFailed()
    fun onCleared()
}