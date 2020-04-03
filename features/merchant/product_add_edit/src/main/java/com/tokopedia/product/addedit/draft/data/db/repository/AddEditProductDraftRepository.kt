package com.tokopedia.product.addedit.draft.data.db.repository

import com.tokopedia.product.addedit.common.domain.model.params.add.Product
import com.tokopedia.product.addedit.draft.data.db.model.AddEditProductDraftModel
import rx.Observable

interface AddEditProductDraftRepository {
    //it's temporary domainModel, it's not a real model
    fun saveDraft(domainModel: Product, isUploading: Boolean): Observable<Long>

    fun getDraft(productId: Long): Observable<Product>

    fun getAllDrafts(): Observable<List<AddEditProductDraftModel>>

    fun getAllDraftsCount(): Observable<Int>

    fun deleteDraft(productId: Long): Observable<Boolean>

    fun deleteAllDrafts(): Observable<Boolean>

    fun updateDraft(productId: Long, domainModel: Product, isUploading: Boolean): Observable<Long>

    fun updateLoadingStatus(productId: Long, isUploading: Boolean)
}