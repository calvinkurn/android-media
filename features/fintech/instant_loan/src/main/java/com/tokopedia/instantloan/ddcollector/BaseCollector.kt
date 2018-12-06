//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector

import java.util.HashMap

abstract class BaseCollector : InfoCollector {
    abstract fun getType(): String

    abstract fun getData(): Any

    override fun buildPhoneInfo(): Map<String, Any> {
        val type = this.getInfoType()
        val phoneInfo = HashMap<String, Any>()

        try {
            phoneInfo[type] = this.getData()
        } catch (e: Exception) {
            phoneInfo[type] = null as Any
        }

        return phoneInfo
    }

    override fun getInfoType(): String {
        return this.getType()
    }
}
