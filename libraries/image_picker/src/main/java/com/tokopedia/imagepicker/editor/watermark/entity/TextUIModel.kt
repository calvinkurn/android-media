package com.tokopedia.imagepicker.editor.watermark.entity

import android.graphics.Paint

interface TextUIModel : BaseWatermark {
    var text: String
    var textColor: Int
    var backgroundColor: Int
    var textStyle: Paint.Style
    var typeFaceId: Int
    var textShadowBlurRadius: Float
    var textShadowXOffset: Float
    var textShadowYOffset: Float
    var textShadowColor: Int
}