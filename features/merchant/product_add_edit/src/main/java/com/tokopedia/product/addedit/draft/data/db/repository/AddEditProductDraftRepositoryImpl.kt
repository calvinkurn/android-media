package com.tokopedia.product.addedit.draft.data.db.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tokopedia.product.addedit.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.product.addedit.draft.mapper.AddEditProductDraftMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class AddEditProductDraftRepositoryImpl @Inject constructor(private val draftDataSource: AddEditProductDraftDataSource, val context: Context): AddEditProductDraftRepository {

    override fun insertDraft(productInputModel: ProductInputModel, isUploading: Boolean): Long {
        val draftJson = AddEditProductDraftMapper.mapProductInputToJsonString(productInputModel)
        val shopId = UserSession(context).shopId
        return draftDataSource.insertDraft(draftJson, isUploading, shopId)
    }

    override fun getDraft(productId: Long): ProductInputModel {
        return AddEditProductDraftMapper.mapDraftToProductInput(draftDataSource.getDraft(productId))
    }

    override fun getAllDrafts(): List<ProductInputModel> {
        val shopId = UserSession(context).shopId
        val listEntities = draftDataSource.getAllDrafts(shopId)
        return listEntities.map { AddEditProductDraftMapper.mapDraftToProductInput(it) }
    }

    override fun getAllDraftsCount(): Long {
        val shopId = UserSession(context).shopId
        return draftDataSource.getAllDraftsCount(shopId)
    }

    override fun deleteDraft(productId: Long): Boolean {
        return draftDataSource.deleteDraft(productId)
    }

    override fun deleteAllDrafts(): Boolean {
        val shopId = UserSession(context).shopId
        return draftDataSource.deleteAllDrafts(shopId)
    }

    override fun updateDraft(productId: Long, productInputModel: ProductInputModel, isUploading: Boolean): Long {
        val draftJson = AddEditProductDraftMapper.mapProductInputToJsonString(productInputModel)
        return draftDataSource.updateDraft(productId, draftJson, isUploading)
    }

    override fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean {
        return draftDataSource.updateLoadingStatus(productId,isUploading)
    }
}

