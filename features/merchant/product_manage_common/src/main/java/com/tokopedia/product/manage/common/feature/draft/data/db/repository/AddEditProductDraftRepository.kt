package com.tokopedia.product.manage.common.feature.draft.data.db.repository

import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import kotlinx.coroutines.flow.Flow

interface AddEditProductDraftRepository {

    fun insertDraft(productDraft: ProductDraft, isUploading: Boolean): Long

    fun getDraft(productId: Long): ProductDraft

    suspend fun getAllDraftsFlow(): Flow<List<ProductDraft>>

    fun getAllDrafts(): List<ProductDraft>

    fun getAllDraftsCountFlow(): Flow<Long>

    fun deleteDraft(productId: Long): Boolean

    fun deleteAllDrafts(): Boolean

    fun updateDraft(productId: Long, productDraft: ProductDraft, isUploading: Boolean): Long

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean
}