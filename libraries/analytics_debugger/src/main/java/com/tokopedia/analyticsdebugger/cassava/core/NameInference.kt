package com.tokopedia.analyticsdebugger.cassava.core


interface NameInference {
    fun infer(data: Map<String, Any>, source: String): String
}

class DefaultNameInference: NameInference {

    override fun infer(data: Map<String, Any>, source: String): String {
        if (data.isEmpty()) return ""
        return data["event"].toString()
    }
}

class TkpdNameInference: NameInference {
    override fun infer(data: Map<String, Any>, source: String): String {
        return runCatching {
            when(source) {
                "gtm" -> data["event"].toString()
                "branch_io" -> data["eventName"].toString()
                "error" -> "ERROR GTM V5"
                else -> ""
            }
        }.getOrElse { "" }
    }
}