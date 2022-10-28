package com.tokopedia.product.addedit.draft.presentation.data.repository

import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

class MockedDraftRepositoryException : AddEditProductDraftRepository {
    override fun insertDraft(productDraft: ProductDraft, isUploading: Boolean): Long {
        return 0
    }

    override fun getDraft(productId: Long): ProductDraft {
        return ProductDraft()
    }

    @Suppress("UNREACHABLE_CODE", "UnusedEquals")
    override suspend fun getAllDraftsFlow(): Flow<List<ProductDraft>> {
        return flow {
            emit(throw NullPointerException())
        }
    }

    override fun getAllDrafts(): List<ProductDraft> {
        return emptyList()
    }

    override fun getAllDraftsCountFlow(): Flow<Long> {
        return emptyFlow()
    }

    override fun deleteDraft(productId: Long): Boolean {
        return false
    }

    override fun deleteAllDrafts(): Boolean {
        return false
    }

    override fun updateDraft(
        productId: Long,
        productDraft: ProductDraft,
        isUploading: Boolean
    ): Long {
        return 0
    }

    override fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean {
        return false
    }

}