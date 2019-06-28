package com.tokopedia.core.common.category.data.source.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface CategoryDao{

    @Query("DELETE FROM ${DBMetaData.DB_TABLE}")
    fun clearTables()

    @Query("SELECT * FROM ${DBMetaData.DB_TABLE} WHERE parentId = :parentId ORDER BY weight ASC")
    fun getCategoryListByParent(parentId: Long): List<CategoryDataBase>

    @Query("SELECT * FROM ${DBMetaData.DB_TABLE}")
    fun getAllCategories(): List<CategoryDataBase>

    @Query("SELECT name FROM ${DBMetaData.DB_TABLE} WHERE id = :categoryId LIMIT 1")
    fun getCategoryName(categoryId: Long): String?

    @Query("SELECT * FROM ${DBMetaData.DB_TABLE} WHERE id = :categoryId LIMIT 1")
    fun getSingleCategory(categoryId: Long): CategoryDataBase?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultiple(categories: List<CategoryDataBase>)
}