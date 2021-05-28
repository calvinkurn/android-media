package com.tokopedia.imagepicker.editor.watermark.listener

interface BuildFinishListener<T> {
    fun onSuccess(result: T?)
    fun onError(message: String)
}