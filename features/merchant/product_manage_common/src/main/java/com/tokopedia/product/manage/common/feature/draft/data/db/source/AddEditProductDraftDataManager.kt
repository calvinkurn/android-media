package com.tokopedia.product.manage.common.feature.draft.data.db.source

import com.tokopedia.product.manage.common.feature.draft.constant.AddEditProductDraftConstant
import com.tokopedia.product.manage.common.feature.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity
import rx.Observable
import javax.inject.Inject

class AddEditProductDraftDataManager @Inject constructor(private val draftDao: AddEditProductDraftDao) {

    fun insertDraft(json: String, isUploading: Boolean, shopId: String): Long {
        val draft = AddEditProductDraftEntity()
        draft.data = json
        draft.isUploading = isUploading
        draft.shopId = shopId
        draft.version = AddEditProductDraftConstant.DB_VERSION_9
        return draftDao.insertDraft(draft)
    }

    fun getDraft(productId: Long): AddEditProductDraftEntity? {
        return draftDao.getDraft(productId)
    }

    fun getAllDrafts(shopId: String): List<AddEditProductDraftEntity> {
        return draftDao.getAllDrafts(shopId)
    }

    fun getAllDraftsCount(shopId: String): Observable<Long> {
        return Observable.fromCallable { draftDao.getAllDraftsCount(shopId) }
    }

    fun deleteAllDrafts(shopId: String): Boolean {
        draftDao.deleteAllDrafts(shopId)
        return true
    }

    fun deleteDraft(productId: Long): Boolean {
        val draft = AddEditProductDraftEntity()
        draft.id = productId
        draftDao.deleteDraft(draft)
        return true
    }

    fun updateDraft(productId: Long, data: String): Long {
        val draft = getDraft(productId) ?: AddEditProductDraftEntity()
        draft.apply {
            this.data = data
            this.version =AddEditProductDraftConstant.DB_VERSION_9
        }
        draftDao.updateDraft(draft)
        return productId
    }

    fun updateDraft(productId: Long, data: String, isUploading: Boolean): Long {
        val draft = getDraft(productId) ?: AddEditProductDraftEntity()
        draft.apply {
            this.data = data
            this.isUploading = isUploading
            this.version = AddEditProductDraftConstant.DB_VERSION_9
        }
        draftDao.updateDraft(draft)
        return productId
    }

    fun updateBlankShopIdDraft(shopId: String): Boolean {
        draftDao.updateShopIdFromNullShopId(shopId)
        return true
    }

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean {
        return if (productId > 0) {
            val draft = getDraft(productId) ?: AddEditProductDraftEntity()
            draft.isUploading = isUploading
            draftDao.updateDraft(draft)
            true
        } else {
            draftDao.updateLoadingForAll(!isUploading, isUploading)
            true
        }
    }
}