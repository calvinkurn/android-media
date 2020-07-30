package com.tokopedia.fakeresponse.chuck.domain.repository

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.chuck.TransactionEntityColumn
import com.tokopedia.fakeresponse.chuck.Utils.cursorToTransactionEntity

class ChuckRepository(val database: SQLiteDatabase?) {

    val tableName = "transactions"

    fun search(responseBody: String?, url: String?, requestBody: String?, id: Long?): List<TransactionEntity> {
        val transactionEntities = arrayListOf<TransactionEntity>()
        var cursor: Cursor? = null
        try {
            val items = arrayListOf<String>()
            if (!responseBody.isNullOrEmpty())
                items.add(" ${TransactionEntityColumn.RESPONSE_BODY} LIKE '%$responseBody%' ")
            if (!url.isNullOrEmpty())
                items.add(" ${TransactionEntityColumn.URL} LIKE '%$url%' ")
            if (!requestBody.isNullOrEmpty())
                items.add(" ${TransactionEntityColumn.REQUEST_BODY} LIKE '%$requestBody%' ")
            if (id != null && id != 0L)
                items.add(" ${TransactionEntityColumn.ID} LIKE '%$id%' ")

            val sb = StringBuilder()
            items.forEach {
                if (!sb.isEmpty()) {
                    sb.append(" OR ")
                }
                sb.append(it)
            }

            val projection = "${TransactionEntityColumn.RESPONSE_BODY}," +
                    "${TransactionEntityColumn.REQUEST_BODY}," +
                    "${TransactionEntityColumn.REQUEST_DATE}," +
                    "${TransactionEntityColumn.URL}," +
                    "${TransactionEntityColumn.HOST}," +
                    "${TransactionEntityColumn.PATH}," +
                    "${TransactionEntityColumn.RESPONSE_CODE}," +
                    "${TransactionEntityColumn.ID}," +
                    TransactionEntityColumn.METHOD

            if(sb.isEmpty()){
                sb.append(" 1==1 ")
            }
            cursor = database?.rawQuery("SELECT $projection FROM $tableName WHERE $sb ORDER BY ${TransactionEntityColumn.REQUEST_DATE} DESC", null)

            cursor?.let {
                while (it.moveToNext()) {
                    transactionEntities.add(cursorToTransactionEntity(it))
                }
            }
        } catch (e: Exception) {
            cursor?.close()
            e.printStackTrace()
            throw e
        }
        cursor?.close()
        return transactionEntities
    }
}