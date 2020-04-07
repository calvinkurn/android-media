package com.tokopedia.product.manage.common.draft.data.db.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tokopedia.product.manage.common.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.draft.mapper.AddEditProductDraftMapper
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddEditProductDraftRepositoryImpl @Inject constructor(
    private val draftDataSource: AddEditProductDraftDataSource,
    private val userSession: UserSessionInterface
): AddEditProductDraftRepository {

    override fun insertDraft(productDraft: ProductDraft, isUploading: Boolean): Long {
        val draftJson = AddEditProductDraftMapper.mapProductInputToJsonString(productDraft)
        val shopId = userSession.shopId
        return draftDataSource.insertDraft(draftJson, isUploading, shopId)
    }

    override fun getDraft(productId: Long): ProductInputModel {
        return AddEditProductDraftMapper.mapDraftToProductInput(draftDataSource.getDraft(productId))
    }

    override fun getAllDrafts(): List<ProductDraft> {
        val shopId = userSession.shopId
        val listEntities = draftDataSource.getAllDrafts(shopId)
        return listEntities.map { AddEditProductDraftMapper.mapDraftToProductInput(it) }
    }

    override fun getAllDraftsCount(): Long {
        val shopId = userSession.shopId
        return draftDataSource.getAllDraftsCount(shopId)
    }

    override fun deleteDraft(productId: Long): Boolean {
        return draftDataSource.deleteDraft(productId)
    }

    override fun deleteAllDrafts(): Boolean {
        val shopId = userSession.shopId
        return draftDataSource.deleteAllDrafts(shopId)
    }

    override fun updateDraft(productId: Long, productDraft: ProductDraft, isUploading: Boolean): Long {
        val draftJson = AddEditProductDraftMapper.mapProductInputToJsonString(productDraft)
        return draftDataSource.updateDraft(productId, draftJson, isUploading)
    }

    override fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean {
        return draftDataSource.updateLoadingStatus(productId,isUploading)
    }
}

