package com.tokopedia.product.manage.item.main.draft.data.db

import android.arch.persistence.room.*

@Dao
interface ProductDraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingle(draft: ProductDraft): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSingle(draft: ProductDraft): Int

    @Query("SELECT * FROM ${DBMetaData.DB_TABLE} WHERE id = :productId LIMIT 1")
    fun getSingleDraft(productId: Long): ProductDraft?

    @Query("SELECT * FROM ${DBMetaData.DB_TABLE} WHERE shopId LIKE :shopID AND ${ProductDraft.COLUMN_IS_UPLOADING} = 0")
    fun getMyDrafts(shopID: String): List<ProductDraft>

    @Query("SELECT COUNT(*) FROM ${DBMetaData.DB_TABLE} WHERE shopId LIKE :shopID AND ${ProductDraft.COLUMN_IS_UPLOADING} = 0")
    fun getMyDraftsCount(shopID: String): Int

    @Query("DELETE FROM ${DBMetaData.DB_TABLE} WHERE shopId LIKE :shopID")
    fun deleteMyDrafts(shopID: String)

    @Query("UPDATE ${DBMetaData.DB_TABLE} SET ${ProductDraft.COLUMN_IS_UPLOADING} = :inverseIsUploading WHERE ${ProductDraft.COLUMN_IS_UPLOADING} = :isUploading")
    fun updateLoadingForAll(isUploading: Boolean, inverseIsUploading: Boolean = !isUploading)

    @Query("UPDATE ${DBMetaData.DB_TABLE} SET shopId = :shopID WHERE shopId IS NULL")
    fun updateShopIdFromNullShopId(shopID: String)

    @Delete
    fun deleteDraft(draft: ProductDraft)
}