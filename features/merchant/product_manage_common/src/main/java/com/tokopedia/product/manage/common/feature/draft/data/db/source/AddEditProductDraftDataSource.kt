package com.tokopedia.product.manage.common.feature.draft.data.db.source

import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity
import rx.Observable
import javax.inject.Inject

class AddEditProductDraftDataSource @Inject constructor(private val dataManager: AddEditProductDraftDataManager) {

    private var isUpdateBlankShopId = false

    fun insertDraft(json: String, isUploading: Boolean, shopId: String): Long {
        return dataManager.insertDraft(json, isUploading, shopId)
    }

    fun getDraft(productId: Long): AddEditProductDraftEntity? {
        return dataManager.getDraft(productId)
    }

    fun getAllDrafts(shopId: String): List<AddEditProductDraftEntity> {
        checkUpdateBlankShopId(shopId)
        return dataManager.getAllDrafts(shopId)
    }

    fun getAllDraftsCount(shopId: String): Observable<Long> {
        checkUpdateBlankShopId(shopId)
        return dataManager.getAllDraftsCount(shopId)
    }

    fun deleteDraft(productId: Long): Boolean {
        return dataManager.deleteDraft(productId)
    }

    fun deleteAllDrafts(shopId: String): Boolean {
        return dataManager.deleteAllDrafts(shopId)
    }

    fun updateDraft(productId: Long, data: String): Long? {
        return dataManager.updateDraft(productId, data)
    }

    fun updateDraft(productId: Long, data: String, isUploading: Boolean): Long {
        return dataManager.updateDraft(productId, data, isUploading)
    }

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean {
        return dataManager.updateLoadingStatus(productId, isUploading)
    }

    private fun checkUpdateBlankShopId(shopId: String) {
        if(!isUpdateBlankShopId) {
            dataManager.updateBlankShopIdDraft(shopId)
            isUpdateBlankShopId = true
        }
    }


}