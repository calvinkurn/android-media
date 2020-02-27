package com.tokopedia.updateinactivephone.usecase

import com.tokopedia.core.base.domain.RequestParams
import com.tokopedia.core.base.domain.UseCase
import com.tokopedia.core.base.domain.executor.PostExecutionThread
import com.tokopedia.core.base.domain.executor.ThreadExecutor
import com.tokopedia.updateinactivephone.data.repository.UploadImageRepository
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel

import rx.Observable

class GetUploadHostUseCase(threadExecutor: ThreadExecutor,
                           postExecutionThread: PostExecutionThread,
                           private val uploadImageRepository: UploadImageRepository)
    : UseCase<UploadHostModel>(threadExecutor, postExecutionThread) {

    override fun createObservable(requestParams: RequestParams): Observable<UploadHostModel> {
        return uploadImageRepository.getUploadHost(requestParams.parameters)
    }

    companion object {

        const val PARAM_NEW_ADD = "new_add"
        const val DEFAULT_NEW_ADD = "2"
    }
}