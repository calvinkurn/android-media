package com.tokopedia.analyticsdebugger.debugger.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.data.repository.IrisSaveLogLocalRepository
import com.tokopedia.analyticsdebugger.debugger.data.repository.ServerLogLocalRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

class GetServerLogUseCase @Inject
internal constructor(private val repository: ServerLogLocalRepository) : UseCase<List<Visitable<*>>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return repository.get(requestParams)
    }
}
