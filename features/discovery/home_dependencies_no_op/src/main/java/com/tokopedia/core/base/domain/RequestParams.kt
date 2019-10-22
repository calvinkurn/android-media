package com.tokopedia.core.base.domain

class RequestParams {

    companion object {
        @kotlin.jvm.JvmField
        val EMPTY = create()

        fun create(): RequestParams {
            return RequestParams()
        }
    }

    fun getParameters(): Map<String, String> {
        return mapOf()
    }
}