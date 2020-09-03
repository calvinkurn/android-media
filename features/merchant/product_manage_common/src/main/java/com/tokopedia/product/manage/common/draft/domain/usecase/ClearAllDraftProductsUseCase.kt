package com.tokopedia.product.manage.common.draft.domain.usecase

import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class ClearAllDraftProductsUseCase @Inject constructor(private val addEditProductDraftRepository: AddEditProductDraftRepository) : UseCase<Boolean?>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean?>? {
        return Observable.just<Boolean>(addEditProductDraftRepository.deleteAllDrafts())
    }

}
