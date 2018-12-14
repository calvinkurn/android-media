package com.tokopedia.instantloan.ddcollector

abstract class BaseCollector : InfoCollector {
    abstract fun getType(): String

    abstract fun getData(): Any

    override fun buildPhoneInfo(): MutableMap<String, Any?> {
        val type = this.getInfoType()
        val phoneInfo = mutableMapOf<String, Any?>()

        try {
            phoneInfo[type] = this.getData()
        } catch (e: Exception) {
            phoneInfo[type] = null as? Any?
        }

        return phoneInfo
    }

    override fun getInfoType() = this.getType()

}
