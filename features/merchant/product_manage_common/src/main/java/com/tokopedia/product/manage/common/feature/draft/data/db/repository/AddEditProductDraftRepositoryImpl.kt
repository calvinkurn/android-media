package com.tokopedia.product.manage.common.feature.draft.data.db.repository

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.manage.common.feature.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.feature.draft.mapper.AddEditProductDraftMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getDraft(productId: Long): ProductDraft {
        return AddEditProductDraftMapper.mapDraftToProductInput(
                draftDataSource.getDraft(productId) ?: AddEditProductDraftEntity()
        )
    }

    override suspend fun getAllDraftsFlow(): Flow<List<ProductDraft>> =
        draftDataSource.getAllDraftsFlow(userSession.shopId).map { list ->
            list.map {
                try {
                    AddEditProductDraftMapper.mapDraftToProductInput(it)
                } catch (e: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    ProductDraft(
                        draftId = it.id,
                        isCorrupt = true,
                        corruptedData = it.data
                    )
                }
            }
        }

    override fun getAllDrafts(): List<ProductDraft> {
        val shopId = userSession.shopId
        val listEntities = draftDataSource.getAllDrafts(shopId)
        return listEntities.map {
            try {
                AddEditProductDraftMapper.mapDraftToProductInput(it)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                ProductDraft(
                    draftId = it.id,
                    isCorrupt = true,
                    corruptedData = it.data
                )
            }
        }
    }

    override fun getAllDraftsCountFlow(): Flow<Long> {
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

