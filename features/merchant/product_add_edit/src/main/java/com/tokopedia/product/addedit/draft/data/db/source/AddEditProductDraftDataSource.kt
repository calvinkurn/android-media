package com.tokopedia.product.addedit.draft.data.db.source

import androidx.lifecycle.LiveData
import com.tokopedia.product.addedit.draft.data.db.entity.AddEditProductDraftEntity
import javax.inject.Inject

class AddEditProductDraftDataSource @Inject constructor(private val productDraftDataManager: AddEditProductDraftDataManager) {

    private var isUpdateBlankShopId = false

    fun saveDraft(json: String, isUploading: Boolean, shopId: String): Long? {
        return productDraftDataManager.saveDraft(json, isUploading, shopId)
    }

    fun getDraft(productId: Long): LiveData<AddEditProductDraftEntity> {
        return productDraftDataManager.getDraft(productId)
    }

    fun getAllDrafts(shopId: String): LiveData<List<AddEditProductDraftEntity>> {
        checkUpdateBlankShopId(shopId)
        return productDraftDataManager.getAllDrafts(shopId)
    }

    fun getAllDraftsCount(shopId: String): LiveData<Int> {
        checkUpdateBlankShopId(shopId)
        return productDraftDataManager.getAllDraftsCount(shopId)
    }

    fun deleteDraft(productId: Long): Boolean {
        return productDraftDataManager.deleteDraft(productId)
    }

    fun deleteAllDrafts(shopId: String): Boolean {
        return productDraftDataManager.deleteAllDrafts(shopId)
    }

    fun updateDraft(productId: Long, data: String): Long? {
        return productDraftDataManager.updateDraft(productId, data)
    }

    fun updateDraft(productId: Long, data: String, isUploading: Boolean): Long? {
        return productDraftDataManager.updateDraft(productId, data, isUploading)
    }

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean {
        return productDraftDataManager.updateLoadingStatus(productId, isUploading)
    }

    private fun checkUpdateBlankShopId(shopId: String) {
        if(!isUpdateBlankShopId) {
            productDraftDataManager.updateBlankShopIdDraft(shopId)
            isUpdateBlankShopId = true
        }
    }


}