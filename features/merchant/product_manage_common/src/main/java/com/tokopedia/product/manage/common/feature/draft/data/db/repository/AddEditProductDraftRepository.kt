package com.tokopedia.product.manage.common.feature.draft.data.db.repository

import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import rx.Observable

interface AddEditProductDraftRepository {

    fun insertDraft(productDraft: ProductDraft, isUploading: Boolean): Long

    fun getDraft(productId: Long): ProductDraft

    fun getAllDrafts(): List<ProductDraft>

    fun getAllDraftsCount(): Observable<Long>

    fun deleteDraft(productId: Long): Boolean

    fun deleteAllDrafts(): Boolean

    fun updateDraft(productId: Long, productDraft: ProductDraft, isUploading: Boolean): Long

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean
}