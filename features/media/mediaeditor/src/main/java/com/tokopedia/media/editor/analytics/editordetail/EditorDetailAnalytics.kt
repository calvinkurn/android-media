package com.tokopedia.media.editor.analytics.editordetail

interface EditorDetailAnalytics {
    fun clickRotationFlip()
    fun clickRotationRotate()
    fun clickAddLogoUpload()
    fun clickAddLogoLoadRetry()
    fun clickAddTextFreeText()
    fun clickAddTextBackgroundText()
    fun clickAddTextTemplate()
    fun clickSave(
        editorText: String,
        brightnessValue: Int,
        contrastValue: Int,
        cropText: String,
        rotateValue: Int,
        watermarkText: String,
        removeBackgroundText: String,
        addLogoValue: String,
        addTextValue: String
    )
}
