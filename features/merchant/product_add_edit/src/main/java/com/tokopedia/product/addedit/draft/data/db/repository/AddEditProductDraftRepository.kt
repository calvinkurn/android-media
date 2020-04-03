package com.tokopedia.product.addedit.draft.data.db.repository

import com.tokopedia.product.addedit.draft.data.db.model.AddEditProductDraftModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import rx.Observable

interface AddEditProductDraftRepository {

    fun saveDraft(productInputModel: ProductInputModel, isUploading: Boolean): Observable<Long>

    fun getDraft(productId: Long): Observable<ProductInputModel>

    fun getAllDrafts(): Observable<MutableList<AddEditProductDraftModel?>>?

    fun getAllDraftsCount(): Observable<Int>

    fun deleteDraft(productId: Long): Observable<Boolean>

    fun deleteAllDrafts(): Observable<Boolean>

    fun updateDraft(productId: Long, productInputModel: ProductInputModel, isUploading: Boolean): Observable<Long>

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Observable<Boolean>
}