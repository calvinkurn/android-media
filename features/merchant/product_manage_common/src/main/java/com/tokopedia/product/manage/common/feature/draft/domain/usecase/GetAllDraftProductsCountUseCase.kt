package com.tokopedia.product.manage.common.feature.draft.domain.usecase

import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetAllDraftProductsCountUseCase @Inject constructor(
    private val draftRepository: AddEditProductDraftRepository
): UseCase<Long>() {

    override fun createObservable(params: RequestParams?): Observable<Long> {
        return draftRepository.getAllDraftsCount()
    }
}