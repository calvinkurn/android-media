package com.tokopedia.settingbank.choosebank.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by Ade Fulki on 2019-05-15.
 * ade.hadian@tokopedia.com
 */

@Database(entities = [BankTable::class], version = 1)
abstract class BankDatabase: RoomDatabase() {

    abstract fun bankDao(): BankDao

    companion object {
        var INSTANCE: BankDatabase? = null

        fun getDataBase(context: Context): BankDatabase? {
            if (INSTANCE == null){
                synchronized(BankDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            BankDatabase::class.java, "Bank.db")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}