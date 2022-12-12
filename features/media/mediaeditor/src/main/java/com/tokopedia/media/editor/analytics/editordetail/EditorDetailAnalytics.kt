package com.tokopedia.media.editor.analytics.editordetail

interface EditorDetailAnalytics {
    fun clickRotationFlip()
    fun clickRotationRotate()
    fun clickAddLogoUpload()
    fun clickSave(
        editorText: String,
        brightnessValue: Int,
        contrastValue: Int,
        cropText: String,
        rotateValue: Int,
        watermarkText: String,
        removeBackgroundText: String,
        addLogoValue: String
    )
}
