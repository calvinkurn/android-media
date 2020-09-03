package com.tokopedia.tokopatch.domain.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Author errysuprayogi on 16,July,2020
 */
object DBMigration {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Result ADD COLUMN version_code TEXT")
        }
    }

}