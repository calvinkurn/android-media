package com.tokopedia.utils.view.binding.internal

sealed class MethodType {
    object Bind: MethodType()
    object Inflate: MethodType()
}