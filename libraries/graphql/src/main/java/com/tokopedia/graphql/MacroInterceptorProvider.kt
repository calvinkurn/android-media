package com.tokopedia.graphql

import android.content.Context

object MacroInterceptorProvider {
    fun get(name: String, context: Context): Any? {
        val kClass = Class.forName(name).kotlin
        val constructor = kClass.constructors
            .minByOrNull { it.parameters.size }
        constructor?.let {
            return it.call(context)
        }
        return null
    }
}
