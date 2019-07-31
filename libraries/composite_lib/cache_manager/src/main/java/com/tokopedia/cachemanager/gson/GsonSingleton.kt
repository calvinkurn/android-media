package com.tokopedia.cachemanager.gson

import com.google.gson.Gson

object GsonSingleton {
    val instance: Gson by lazy {
        Gson()
    }
}
