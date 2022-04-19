package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.analyticsdebugger.debugger.data.repository.IrisSendLogLocalRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetIrisSendCountLogUseCase @Inject
internal constructor(private val repository: IrisSendLogLocalRepository) : UseCase<Int>() {

    override fun createObservable(requestParams: RequestParams): Observable<Int> {
        return Observable.fromCallable {
            repository.getCount();
        }
    }
}
