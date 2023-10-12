package com.tokopedia.play.widget.ui.model.error

/**
 * Created by kenny.hadisaputra on 03/10/23
 */
open class PlayWidgetException(message: String) : Exception(message)

class InflateErrorException : PlayWidgetException("Error inflating PlayWidget")
