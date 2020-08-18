package com.tokopedia.pushnotif.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.pushnotif.data.constant.TRANSACTION_TABLE
import com.tokopedia.pushnotif.data.db.model.TransactionNotification

@Dao
interface TransactionNotificationDao {

    @Insert
    fun storeNotification(data: TransactionNotification): Long

    @Query("SELECT COUNT(id) FROM $TRANSACTION_TABLE WHERE transaction_id=:transId")
    fun isRenderable(transId: String): Int

}