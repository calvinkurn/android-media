package com.tokopedia.fakeresponse.data.diProvider

import android.content.Context
import com.tokopedia.fakeresponse.db.AppDatabase

interface DiProvider<in T>{
    fun getDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    fun inject(t:T)
}