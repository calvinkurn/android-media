package com.tokopedia.autocompletecomponent.util.contextprovider

import android.content.Context
import java.lang.ref.WeakReference

class WeakReferenceContextProvider(
    context: Context?
) : ContextProvider {
    private val contextReference : WeakReference<Context> = WeakReference(context)
    override val context: Context?
        get() = contextReference.get()
}