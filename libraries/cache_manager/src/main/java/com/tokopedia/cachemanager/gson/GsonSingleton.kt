package com.tokopedia.cachemanager.gson

import com.google.gson.Gson

object GsonSingleton {
    private var instance: Gson? = null

    fun getInstance(): Gson {
        if (instance == null) {
            instance = Gson()
        }
        return instance!!
    }
}
