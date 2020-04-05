package com.tokopedia.product.addedit.draft.data.db.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tokopedia.product.addedit.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.product.addedit.draft.mapper.AddEditProductDraftListMapper
import com.tokopedia.product.addedit.draft.mapper.AddEditProductDraftMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class AddEditProductDraftRepositoryImpl @Inject constructor(private val draftDataSource: AddEditProductDraftDataSource, private val context: Context): AddEditProductDraftRepository {

    override fun saveDraft(productInputModel: ProductInputModel, isUploading: Boolean): Long? {
        val draftJson = AddEditProductDraftMapper.mapProductInputToJsonString(productInputModel)
        val shopId = UserSession(context).shopId
        return draftDataSource.saveDraft(draftJson, isUploading, shopId)
    }

    override fun getDraft(productId: Long): LiveData<ProductInputModel> {
        return Transformations.map(draftDataSource.getDraft(productId)) { draft ->
            AddEditProductDraftMapper.mapDraftToProductInput(draft)
        }
    }

    override fun getAllDrafts(): LiveData<List<ProductInputModel>> {
        val shopId = UserSession(context).shopId
        return Transformations.map(draftDataSource.getAllDrafts(shopId)) { listDrafts ->
            listDrafts.map { draft ->
                var productInputModel = ProductInputModel()
                draft.id?.let { productId ->
                    productInputModel = AddEditProductDraftMapper.mapDraftToProductInput(draft)
                    AddEditProductDraftListMapper.mapDomainToView(productInputModel, productId)
                }
                productInputModel
            }
        }
    }

    override fun getAllDraftsCount(): LiveData<Int> {
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

    override fun updateDraft(productId: Long, productInputModel: ProductInputModel, isUploading: Boolean): Long? {
        val draftJson = AddEditProductDraftMapper.mapProductInputToJsonString(productInputModel)
        return draftDataSource.updateDraft(productId, draftJson, isUploading)
    }

    override fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean {
        return draftDataSource.updateLoadingStatus(productId,isUploading)
    }
}

