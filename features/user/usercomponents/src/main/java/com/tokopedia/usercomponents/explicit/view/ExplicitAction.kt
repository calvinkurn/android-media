package com.tokopedia.usercomponents.explicit.view

interface ExplicitAction {
    fun onLoading()
    fun onQuestionShow()
    fun onButtonPositiveClicked()
    fun onButtonNegativeClicked()
    fun onSubmitSuccessShow()
    fun onDismiss()
    fun onFailed()
    fun onCleared()
}