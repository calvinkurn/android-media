package com.tokopedia.product.addedit.draft.domain.usecase

import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DeleteAllProductsDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<Boolean>() {

    override suspend fun executeOnBackground(): Boolean = draftRepository.deleteAllDrafts()

}