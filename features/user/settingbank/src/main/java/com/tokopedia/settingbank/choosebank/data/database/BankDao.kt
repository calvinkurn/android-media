package com.tokopedia.settingbank.choosebank.data.database

import android.arch.persistence.room.*

/**
 * Created by Ade Fulki on 2019-05-15.
 * ade.hadian@tokopedia.com
 */

@Dao
interface BankDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(bankTables: List<BankTable>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bankTable: BankTable): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(bankTable: BankTable): Int

    @Query("SELECT * FROM bank_table")
    fun findAllBank(): List<BankTable>

    @Query("SELECT * FROM bank_table WHERE bank_name LIKE :name")
    fun findBankByName(name: String): List<BankTable>

    @Delete
    fun delete(bankTable: BankTable)

    @Query("DELETE FROM bank_table")
    fun deleteAll(): Int

    @Query("DELETE FROM bank_table WHERE id = :id")
    fun deleteById(id: Long): Int
}