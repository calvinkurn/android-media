package com.tokopedia.instantloan.ddcollector

interface InfoCollectService {
    fun getData(): Map<String, Any?>

    fun add(var1: InfoCollector)
}
