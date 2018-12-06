//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector

import java.util.concurrent.ExecutionException

interface InfoCollectService {
    fun getData(): Map<String, Any>

    fun add(var1: InfoCollector)
}
