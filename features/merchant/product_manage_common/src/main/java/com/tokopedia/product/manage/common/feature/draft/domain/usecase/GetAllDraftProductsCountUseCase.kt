package com.tokopedia.product.manage.common.feature.draft.domain.usecase

import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDraftProductsCountFlowUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<Flow<Long>>() {

    override suspend fun executeOnBackground(): Flow<Long> {
        return draftRepository.getAllDraftsCountFlow()
    }
}
