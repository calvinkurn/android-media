package com.tokopedia.usercomponents.explicit.view

interface ExplicitAction {
    fun onLoading()
    fun onQuestionShow()
    fun onButtonPositifClicked()
    fun onButtonNegatifClicked()
    fun onSubmitSuccessShow()
    fun onDismiss()
    fun onFailed()
    fun onCleared()
}