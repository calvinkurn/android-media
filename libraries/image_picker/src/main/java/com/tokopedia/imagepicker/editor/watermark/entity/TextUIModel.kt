package com.tokopedia.imagepicker.editor.watermark.entity

import android.graphics.Paint

interface TextUIModel : BaseWatermark {
    var text: String
    var textSize: Int
    var textColor: Int
    var textAlpha: Int
    var backgroundColor: Int
    var textStyle: Paint.Style
    var fontName: String
    var textShadowBlurRadius: Float
    var textShadowXOffset: Float
    var textShadowYOffset: Float
    var textShadowColor: Int
}