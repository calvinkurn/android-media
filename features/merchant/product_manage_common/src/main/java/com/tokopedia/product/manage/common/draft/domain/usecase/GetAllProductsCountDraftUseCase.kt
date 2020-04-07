package com.tokopedia.product.manage.common.draft.domain.usecase

import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetAllProductsCountDraftUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<Long>() {

    fun execute(): Long = draftRepository.getAllDraftsCount()

    override suspend fun executeOnBackground(): Long {
        return draftRepository.getAllDraftsCount()
    }
}