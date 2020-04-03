package com.tokopedia.product.addedit.draft.data.db.source

import com.tokopedia.product.addedit.draft.data.db.entity.AddEditProductDraftEntity
import rx.Observable
import javax.inject.Inject

class AddEditProductDraftDataSource @Inject constructor(private val productDraftDataManager: AddEditProductDraftDataManager) {

    private var isUpdateBlankShopId = false

    fun saveDraft(json: String, isUploading: Boolean, shopId: String): Observable<Long> {
        return productDraftDataManager.saveDraft(json, isUploading, shopId)
    }

    fun getDraft(productId: Long): Observable<AddEditProductDraftEntity> {
        return productDraftDataManager.getDraft(productId)
    }

    fun getAllDrafts(shopId: String): Observable<List<AddEditProductDraftEntity>> {
        checkUpdateBlankShopId(shopId)
        return productDraftDataManager.getAllDrafts(shopId)
    }

    fun getAllDraftsCount(shopId: String): Observable<Int> {
        checkUpdateBlankShopId(shopId)
        return productDraftDataManager.getAllDraftsCount(shopId)
    }

    fun deleteDraft(productId: Long): Observable<Boolean> {
        return productDraftDataManager.deleteDraft(productId)
    }

    fun deleteAllDrafts(shopId: String): Observable<Boolean> {
        return productDraftDataManager.deleteAllDrafts(shopId)
    }

    fun updateDraft(productId: Long, data: String): Observable<Long> {
        return productDraftDataManager.updateDraft(productId, data)
    }

    fun updateDraft(productId: Long, data: String, isUploading: Boolean): Observable<Long> {
        return productDraftDataManager.updateDraft(productId, data, isUploading)
    }

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Observable<Boolean> {
        return productDraftDataManager.updateLoadingStatus(productId, isUploading)
    }

    private fun checkUpdateBlankShopId(shopId: String) {
        if(!isUpdateBlankShopId) {
            productDraftDataManager.updateBlankShopIdDraft(shopId)
            isUpdateBlankShopId = true
        }
    }


}