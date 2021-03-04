package com.tokopedia.product.manage.common.feature.draft.data.db

import androidx.room.*
import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity

@Dao
interface AddEditProductDraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDraft(draft: AddEditProductDraftEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDraft(draft: AddEditProductDraftEntity)

    @Query("SELECT * FROM ${AddEditProductDraftConstant.DB_TABLE} WHERE id = :productId LIMIT 1")
    fun getDraft(productId: Long): AddEditProductDraftEntity?

    @Query("SELECT * FROM ${AddEditProductDraftConstant.DB_TABLE} WHERE shopId LIKE :shopId AND ${AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING} = 0")
    fun getAllDrafts(shopId: String): List<AddEditProductDraftEntity>

    @Query("SELECT COUNT(*) FROM ${AddEditProductDraftConstant.DB_TABLE} WHERE shopId LIKE :shopId AND ${AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING} = 0")
    fun getAllDraftsCount(shopId: String): Long

    @Query("UPDATE ${AddEditProductDraftConstant.DB_TABLE} SET ${AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING} = :inverseIsUploading WHERE ${AddEditProductDraftConstant.DB_COLUMN_IS_UPLOADING} = :isUploading")
    fun updateLoadingForAll(isUploading: Boolean, inverseIsUploading: Boolean = !isUploading)

    @Query("UPDATE ${AddEditProductDraftConstant.DB_TABLE} SET shopId = :shopId WHERE shopId IS NULL")
    fun updateShopIdFromNullShopId(shopId: String)

    @Query("DELETE FROM ${AddEditProductDraftConstant.DB_TABLE} WHERE shopId LIKE :shopId")
    fun deleteAllDrafts(shopId: String)

    @Delete
    fun deleteDraft(draft: AddEditProductDraftEntity)
}