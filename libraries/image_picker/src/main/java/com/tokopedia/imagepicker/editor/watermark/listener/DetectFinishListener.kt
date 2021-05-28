package com.tokopedia.imagepicker.editor.watermark.listener

import com.tokopedia.imagepicker.editor.watermark.uimodel.DetectionReturnValue

interface DetectFinishListener {
    fun onSuccess(result: DetectionReturnValue)
    fun onError(message: String)
}