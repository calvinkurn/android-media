package com.tokopedia.promousage.util.composite

interface DelegateAdapterItem {

    val id: String

    fun getChangePayload(other: Any): Any? = null
}
