package com.tokopedia.cachemanager.db

abstract class CacheDatabase{

    companion object {
        val persistentMap = HashMap<String, String?>()
        val saveInstanceMap = HashMap<String, String?>()
    }
}