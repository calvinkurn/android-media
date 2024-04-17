package com.tokopedia.kotlin.util

import android.view.ViewStub
import androidx.viewbinding.ViewBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.internal.ViewBindingMethodBinder

/**
 * Created by kelvindo.sutan on 3/8/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */

class ViewStubDelegateImpl<T : ViewBinding>(
    private val viewStub: ViewStub,
    private val bindViewClass: Class<T>
) {
    private val inflateDelegate = lazyThreadSafetyNone { viewStub.inflate() }

    val binding: T by lazyThreadSafetyNone {
        ViewBindingMethodBinder.getBind(bindViewClass).bind(inflateDelegate.value)
    }

    val isInitialized get() = inflateDelegate.isInitialized() && viewStub.parent == null

    /**
     * @param custom is a callback to developer can hide their own components.
     * If this is null, this function should hide by [binding.root.hide()]
     */
    fun hide(custom: (() -> Unit)? = null) {
        if (isInitialized) {
            if (custom == null) {
                binding.root.hide()
            } else {
                custom()
            }
        }
    }

    /**
     * @param custom is a callback to developer can show their own components.
     * If this is null, this function should show by [binding.root.show()]
     */
    fun show(custom: (() -> Unit)? = null) {
        if (isInitialized) {
            if (custom == null) {
                binding.root.show()
            } else {
                custom()
            }
        }
    }
}

@JvmName("viewStubBindTo")
inline fun <reified T : ViewBinding> ViewStub.lazyBind() = lazyThreadSafetyNone {
    ViewStubDelegateImpl(this, T::class.java)
}
