package com.tokopedia.entertainment.pdp.listener

interface OnClickFormListener {
    fun clickBottomSheet(list: LinkedHashMap<String,String>, title: String, positionForm: Int, positionBottomSheet: String)
}