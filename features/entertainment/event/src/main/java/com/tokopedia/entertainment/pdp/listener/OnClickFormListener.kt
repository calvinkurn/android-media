package com.tokopedia.entertainment.pdp.listener

import java.util.*
import kotlin.collections.LinkedHashMap

interface OnClickFormListener {
    fun clickBottomSheet(list: LinkedHashMap<String, String>, title: String, positionForm: Int)
    fun getDate(): Calendar?
    fun clickDatePicker(title: String, helpText: String,  position: Int)
}