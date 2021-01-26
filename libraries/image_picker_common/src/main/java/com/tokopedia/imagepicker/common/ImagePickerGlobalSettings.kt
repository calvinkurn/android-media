package com.tokopedia.imagepicker.common

import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference

object ImagePickerGlobalSettings {
    @JvmField
    var onImagePickerOpen: ImagePickerCallback? = null

    @JvmField
    var onImagePickerContinue: ImagePickerCallback? = null

    @JvmField
    var onImagePickerBack: ImagePickerCallback? = null

    @JvmField
    var onImageEditorOpen: ImagePickerCallback? = null

    @JvmField
    var onImageEditorContinue: ImagePickerCallback? = null

    @JvmField
    var onImageEditorBack: ImagePickerCallback? = null

    @JvmStatic
    fun clearAllGlobalSettings() {
        onImagePickerOpen = null
        onImagePickerContinue = null
        onImagePickerBack = null

        onImageEditorOpen = null
        onImageEditorContinue = null
        onImageEditorBack = null
    }
}

open class ImagePickerCallback(context: Context, val params: Bundle? = null,
                               val callback: ((context:Context, bundle: Bundle?) -> Unit)? = null) {
    var contextRef: WeakReference<Context>? = null

    init {
        contextRef = WeakReference(context)
    }

    open fun invoke() {
        val context = contextRef?.get()
        if (context != null) {
            callback?.invoke(context, params)
        }
    }
}