package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetAllProductsDraftUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<List<ProductDraft>>() {
    override suspend fun executeOnBackground(): List<ProductDraft> = draftRepository.getAllDrafts()
}