package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductDraftFlowUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<Flow<List<ProductDraft>>>() {
    override suspend fun executeOnBackground(): Flow<List<ProductDraft>> = draftRepository.getAllDraftsFlow()
}