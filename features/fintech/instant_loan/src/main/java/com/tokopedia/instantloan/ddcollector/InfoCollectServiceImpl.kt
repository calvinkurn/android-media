package com.tokopedia.instantloan.ddcollector

import java.util.*
import java.util.concurrent.ExecutionException

class InfoCollectServiceImpl : InfoCollectService {
    private val infoCollectorList = ArrayList<InfoCollector>()

    private val phoneInfo: Map<String, Any?>
        @Throws(ExecutionException::class, InterruptedException::class)
        get() {
            val phoneInfoMap = HashMap<String, Any?>()

            infoCollectorList.filter { true }
                    .forEach { it.buildPhoneInfo()?.let { it1 -> phoneInfoMap.putAll(it1) } }

            return phoneInfoMap
        }

    @Throws(ExecutionException::class, InterruptedException::class)
    override fun getData(): Map<String, Any?> {
        return this.phoneInfo
    }

    override fun add(infoCollector: InfoCollector) {
        this.infoCollectorList.add(infoCollector)
    }
}
