package com.tokopedia.product.addedit.draft.domain.usecase

import androidx.lifecycle.LiveData
import com.tokopedia.product.addedit.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetAllProductsCountDraftUseCase @Inject constructor(private val draftRepository: AddEditProductDraftRepository): UseCase<LiveData<Int>>() {

    override suspend fun executeOnBackground(): LiveData<Int> = draftRepository.getAllDraftsCount()
}