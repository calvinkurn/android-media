package com.tokopedia.search.utils.mvvm

interface RefreshableView<T> {

    fun refresh(state: T)
}
