package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetAllProductsDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<List<ProductInputModel>>() {

    override suspend fun executeOnBackground(): List<ProductInputModel> = draftRepository.getAllDrafts()
}