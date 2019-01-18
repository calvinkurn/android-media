package com.tokopedia.instantloan.ddcollector

interface InfoCollector {

    fun getInfoType() : String
    fun buildPhoneInfo(): MutableMap<String, Any?>
}
