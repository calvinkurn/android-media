package com.tokopedia.usercomponents.explicit.view

interface ExplicitAction {
    /**
     * setupView should be called once at the first time.
     * This method is required to call for setting up the
     * ExplicitView component.
     */
    fun setupView(
        viewModel: ExplicitViewContract,
        data: ExplicitData? = null,
    )

    fun onLoading()
    fun onQuestionShow()
    fun onButtonPositiveClicked()
    fun onButtonNegativeClicked()
    fun onSubmitSuccessShow()
    fun onDismiss()
    fun onFailed()
    fun onCleared()
}