package com.tokopedia.mvc.presentation.redirection.uimodel

sealed class MvcRedirectionPageAction{
    data class RedirectTo(val redirectionAppLink: String): MvcRedirectionPageAction()
    data class ShowError(val error: Throwable) : MvcRedirectionPageAction()
}
