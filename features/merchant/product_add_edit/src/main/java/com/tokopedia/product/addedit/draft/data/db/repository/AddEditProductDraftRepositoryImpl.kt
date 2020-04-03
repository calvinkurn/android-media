package com.tokopedia.product.addedit.draft.data.db.repository

import android.content.Context
import com.tokopedia.product.addedit.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.addedit.draft.data.db.model.AddEditProductDraftModel
import com.tokopedia.product.addedit.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.product.addedit.draft.mapper.AddEditProductDraftListMapper
import com.tokopedia.product.addedit.draft.mapper.AddEditProductDraftMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.user.session.UserSession
import rx.Observable

class AddEditProductDraftRepositoryImpl(private val draftDataSource: AddEditProductDraftDataSource, private val context: Context): AddEditProductDraftRepository {

    override fun saveDraft(productInputModel: ProductInputModel, isUploading: Boolean): Observable<Long> {
        val draftJson = AddEditProductDraftMapper.mapProductToJsonString(productInputModel) ?: ""
        val shopId = UserSession(context).shopId
        return draftDataSource.saveDraft(draftJson, isUploading, shopId)
    }

    override fun getDraft(productId: Long): Observable<ProductInputModel> {
        return draftDataSource.getDraft(productId).map(AddEditProductDraftMapper())
    }

    override fun getAllDrafts(): Observable<MutableList<AddEditProductDraftModel?>>? {
        val shopId = UserSession(context).shopId
        return draftDataSource.getAllDrafts(shopId)
                .flatMap { iterable: List<AddEditProductDraftEntity?>? ->
                    Observable.from(iterable)
                }
                .map{ draft ->
                    Observable.just<AddEditProductDraftEntity>(draft)
                            .map(AddEditProductDraftMapper())
                            .map { productInputModel ->
                                draft?.id?.let {
                                    AddEditProductDraftListMapper.mapDomainToView(productInputModel, it)
                                }
                            }.toBlocking().first()
                }.toSortedList { t1, t2 ->
                    t1?.let {
                        t2?.let {
                            (t1.productId.minus(t2.productId)).toInt()
                        }
                    }
                }
    }

    override fun getAllDraftsCount(): Observable<Int> {
        val shopId = UserSession(context).shopId
        return draftDataSource.getAllDraftsCount(shopId)
    }

    override fun deleteDraft(productId: Long): Observable<Boolean> {
        return draftDataSource.deleteDraft(productId)
    }

    override fun deleteAllDrafts(): Observable<Boolean> {
        val shopId = UserSession(context).shopId
        return draftDataSource.deleteAllDrafts(shopId)
    }

    override fun updateDraft(productId: Long, productInputModel: ProductInputModel, isUploading: Boolean): Observable<Long> {
        val draftJson = AddEditProductDraftMapper.mapProductToJsonString(productInputModel) ?: ""
        return draftDataSource.updateDraft(productId, draftJson, isUploading)
    }

    override fun updateLoadingStatus(productId: Long, isUploading: Boolean): Observable<Boolean> {
        return draftDataSource.updateLoadingStatus(productId,isUploading)
    }
}

