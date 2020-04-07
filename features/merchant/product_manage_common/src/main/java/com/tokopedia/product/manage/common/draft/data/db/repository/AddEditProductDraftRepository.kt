package com.tokopedia.product.manage.common.draft.data.db.repository

import androidx.lifecycle.LiveData
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft

interface AddEditProductDraftRepository {

    fun insertDraft(productDraft: ProductDraft, isUploading: Boolean): Long

    fun getDraft(productId: Long): LiveData<ProductDraft>

    fun getAllDrafts(): List<ProductDraft>

    fun getAllDraftsCount(): LiveData<Int>

    fun deleteDraft(productId: Long): Boolean

    fun deleteAllDrafts(): Boolean

    fun updateDraft(productId: Long, productDraft: ProductDraft, isUploading: Boolean): Long

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean
}