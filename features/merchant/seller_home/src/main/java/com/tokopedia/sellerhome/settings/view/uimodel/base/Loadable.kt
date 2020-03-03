package com.tokopedia.sellerhome.settings.view.uimodel.base

interface Loadable<in T> {
    fun renderSuccessLayout(uiModel: T)
    fun renderLoadingLayout()
    fun renderNoDataLayout()
    fun renderErrorLayout()
}