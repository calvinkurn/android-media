//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector

import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.ExecutionException

class InfoCollectServiceImpl : InfoCollectService {
    private val infoCollectorList = ArrayList<InfoCollector>()

    private val phoneInfo: Map<String, Any>
        @Throws(ExecutionException::class, InterruptedException::class)
        get() {
            val phoneInfoMap = HashMap<String, Any>()
            for (infoCollector in infoCollectorList) {
                if (infoCollector == null) {
                    continue
                }
                phoneInfoMap.putAll(infoCollector.buildPhoneInfo())
            }

            return phoneInfoMap
        }

    @Throws(ExecutionException::class, InterruptedException::class)
    override fun getData(): Map<String, Any> {
        return this.phoneInfo
    }

    override fun add(infoCollector: InfoCollector) {
        this.infoCollectorList.add(infoCollector)
    }
}
