package com.tokopedia.product.addedit.draft.data.db.source

import com.tokopedia.product.addedit.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.addedit.common.constant.AddEditProductDraftConstant
import com.tokopedia.product.addedit.draft.data.db.entity.AddEditProductDraftEntity
import rx.Observable
import javax.inject.Inject

class AddEditProductDraftDataManager @Inject constructor(private val draftDao: AddEditProductDraftDao) {

    fun saveDraft(json: String, isUploading: Boolean, shopId: String): Observable<Long> {
        return Observable.fromCallable {
            val draft = AddEditProductDraftEntity()
            draft.data = json
            draft.isUploading = isUploading
            draft.shopId = shopId
            draft.version = AddEditProductDraftConstant.DB_VERSION
            draftDao.insertDraft(draft)
        }
    }

    fun getDraft(productId: Long): Observable<AddEditProductDraftEntity> {
        return Observable.fromCallable {
            val draft = draftDao.getDraft(productId)
            draft
        }
    }

    fun getAllDrafts(shopId: String): Observable<List<AddEditProductDraftEntity>> {
        return Observable.fromCallable {
            draftDao.getAllDrafts(shopId)
        }
    }

    fun getAllDraftsCount(shopId: String): Observable<Int> {
        return Observable.fromCallable {
            draftDao.getAllDraftsCount(shopId)
        }
    }

    fun deleteAllDrafts(shopId: String): Observable<Boolean> {
        return Observable.fromCallable {
            draftDao.deleteAllDrafts(shopId)
            true
        }
    }

    fun deleteDraft(productId: Long): Observable<Boolean> {
        return Observable.fromCallable {
            val draft = AddEditProductDraftEntity()
            draft.id = productId
            draftDao.deleteDraft(draft)
            true
        }
    }

    fun updateDraft(productId: Long, data: String): Observable<Long> {
        return getDraft(productId).map { draft ->
            draft.data = data
            draft.version = AddEditProductDraftConstant.DB_VERSION
            draftDao.updateDraft(draft)
            productId
        }
    }

    fun updateDraft(productId: Long, data: String, isUploading: Boolean): Observable<Long> {
        return getDraft(productId).map { draft ->
            draft.data = data
            draft.version = AddEditProductDraftConstant.DB_VERSION
            draft.isUploading = isUploading
            draftDao.updateDraft(draft)
            productId
        }
    }

    fun updateBlankShopIdDraft(shopId: String): Observable<Boolean> {
        return Observable.fromCallable {
            draftDao.updateShopIdFromNullShopId(shopId)
            true
        }
    }

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Observable<Boolean> {
        return Observable.just(productId).switchMap { productId ->
            if (productId > 0) {
                return@switchMap getDraft(productId).map { draft ->
                    draft.isUploading = isUploading
                    draftDao.updateDraft(draft)
                    true
                }
            } else {
                return@switchMap Observable.fromCallable {
                    draftDao.updateLoadingForAll(!isUploading, isUploading)
                    true
                }
            }
        }
    }
}