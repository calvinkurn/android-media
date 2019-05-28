package com.tokopedia.settingbank.choosebank.data.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.tokopedia.settingbank.choosebank.data.database.BankTable.Companion.BANK_ID
import com.tokopedia.settingbank.choosebank.data.database.BankTable.Companion.TABLE_NAME

/**
 * Created by Ade Fulki on 2019-05-15.
 * ade.hadian@tokopedia.com
 */

@Entity(indices = [Index(value = [BANK_ID], unique = true)], tableName = TABLE_NAME)
data class BankTable(
        @PrimaryKey(autoGenerate = true) @NonNull @ColumnInfo(name = ID) var id: Long,
        @ColumnInfo(name = BANK_ID) var bankId: String,
        @ColumnInfo(name = BANK_NAME) var bankName: String,
        @ColumnInfo(name = BANK_CLEARING_CODE) var bankClearingCode: String,
        @ColumnInfo(name = BANK_ABBREVATION) var bankAbbrevation: String,
        @ColumnInfo(name = BANK_PAGE) var bankPage: Int
){
    constructor(): this(0, "", "", "", "", 0)

    companion object {
        const val TABLE_NAME = "bank_table"
        const val ID: String = "id"
        const val BANK_ID = "bank_id"
        const val BANK_NAME = "bank_name"
        const val BANK_CLEARING_CODE = "bank_clearing_code"
        const val BANK_ABBREVATION = "bank_abbrevation"
        const val BANK_PAGE = "bank_page"
    }
}