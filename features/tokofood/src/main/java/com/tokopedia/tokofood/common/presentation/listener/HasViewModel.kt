package com.tokopedia.tokofood.common.presentation.listener

interface HasViewModel<T> {
    fun viewModel(): T
}