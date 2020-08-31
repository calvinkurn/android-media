package com.tokopedia.home.test.di.module

import android.content.Context
import androidx.room.Room
import com.tokopedia.home.beranda.data.datasource.local.HomeDatabase
import com.tokopedia.home.beranda.di.module.HomeDatabaseModule

class HomeDatabaseModuleTest : HomeDatabaseModule(){
    override fun provideHomeDatabase(context: Context): HomeDatabase {
        return Room.inMemoryDatabaseBuilder(context.applicationContext, HomeDatabase::class.java).fallbackToDestructiveMigration().build()
    }
}