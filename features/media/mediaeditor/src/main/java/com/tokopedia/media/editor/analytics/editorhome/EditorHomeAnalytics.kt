package com.tokopedia.media.editor.analytics.editorhome

interface EditorHomeAnalytics {
    fun clickUpload()
    fun clickBackButton()
    fun clickBrightness()
    fun clickContrast()
    fun clickCrop()
    fun clickRotate()
    fun clickWatermark()
    fun clickRemoveBackground()
    fun clickAddLogo()
    fun clickAddText()
    fun autoCropProcessTime(loadTime: Long, fileNumber: Int)
}
