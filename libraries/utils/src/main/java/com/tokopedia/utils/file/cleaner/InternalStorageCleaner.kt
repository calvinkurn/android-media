package com.tokopedia.utils.file.cleaner

import android.content.Context

object InternalStorageCleaner {
    const val INTERNAL_STORAGE_CLEAN_TIME_DEFAULT = 86_400_000L //1 day
    const val SHARED_PREF_STORAGE_CLEANER_NAME = "storage_cleaner"
    const val SHARED_PREF_TS_KEY = "ts"
    var lastClean: MutableMap<String, Long> = mutableMapOf()

    @JvmStatic
    fun cleanUpInternalStorageIfNeeded(context: Context, relativeDirectoryToClean: String) {
        if (lastClean[relativeDirectoryToClean] == 0L) {
            val sp = context.getSharedPreferences(SHARED_PREF_STORAGE_CLEANER_NAME, Context.MODE_PRIVATE)
            lastClean[relativeDirectoryToClean] = sp.getLong("$SHARED_PREF_TS_KEY-$relativeDirectoryToClean", 0L)
        }
        val now = System.currentTimeMillis()
        if (now - (lastClean[relativeDirectoryToClean]
                        ?: 0) > INTERNAL_STORAGE_CLEAN_TIME_DEFAULT) {
            InternalStorageWorker.scheduleWorker(context, relativeDirectoryToClean)
            setLastClean(context, now, relativeDirectoryToClean)
        }
    }

    fun setLastClean(context: Context, ts: Long, relativeDirectoryToClean: String) {
        val sp = context.getSharedPreferences(SHARED_PREF_STORAGE_CLEANER_NAME, Context.MODE_PRIVATE)
        sp.edit().putLong("$SHARED_PREF_TS_KEY-$relativeDirectoryToClean", ts).apply()
        lastClean[relativeDirectoryToClean] = ts
    }
}