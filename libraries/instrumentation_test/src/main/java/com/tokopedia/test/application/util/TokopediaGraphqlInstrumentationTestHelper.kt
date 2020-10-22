package com.tokopedia.test.application.util

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.graphql.data.db.DbMetadata

object TokopediaGraphqlInstrumentationTestHelper {

    fun deleteAllDataInDb() {
        val db: SQLiteOpenHelper = object : SQLiteOpenHelper(
                InstrumentationRegistry.getInstrumentation().context,
                DbMetadata.NAME,
                null,
                DbMetadata.VERSION
        ) {
            override fun onCreate(p0: SQLiteDatabase?) {}
            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
        }

        //delete all data in all tables in gql database
        try {
            val dbWrite1 = db.writableDatabase
            dbWrite1.execSQL("DELETE FROM tokopedia_graphql")
            dbWrite1.close()
        } catch (e: Exception) { }
    }

}