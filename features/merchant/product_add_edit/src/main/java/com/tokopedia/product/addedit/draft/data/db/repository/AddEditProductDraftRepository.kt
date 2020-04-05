package com.tokopedia.product.addedit.draft.data.db.repository

import androidx.lifecycle.LiveData
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel

interface AddEditProductDraftRepository {

    fun saveDraft(productInputModel: ProductInputModel, isUploading: Boolean): Long?

    fun getDraft(productId: Long): LiveData<ProductInputModel>

    fun getAllDrafts(): LiveData<List<ProductInputModel>>

    fun getAllDraftsCount(): LiveData<Int>

    fun deleteDraft(productId: Long): Boolean

    fun deleteAllDrafts(): Boolean

    fun updateDraft(productId: Long, productInputModel: ProductInputModel, isUploading: Boolean): Long?

    fun updateLoadingStatus(productId: Long, isUploading: Boolean): Boolean
}