package com.tokopedia.product.manage.common.draft.domain.usecase

import androidx.lifecycle.LiveData
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetAllProductsCountDraftUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<LiveData<Int>>() {

    fun execute(): LiveData<Int> = draftRepository.getAllDraftsCount()

    override suspend fun executeOnBackground(): LiveData<Int> {
        return draftRepository.getAllDraftsCount()
    }
}