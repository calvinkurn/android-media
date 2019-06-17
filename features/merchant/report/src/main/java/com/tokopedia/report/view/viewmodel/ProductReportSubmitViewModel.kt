package com.tokopedia.report.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.report.domain.interactor.SubmitReportUseCase
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject

class ProductReportSubmitViewModel @Inject constructor(private val useCase: SubmitReportUseCase,
                                                       dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun submitReport(productId: Int, categoryId: Int, input: Map<String, Any>,
                     onSuccess: (Boolean) -> Unit, onFail: (Throwable?) -> Unit){
        useCase.execute(SubmitReportUseCase.createRequestParamn(categoryId, productId, input), object : Subscriber<Boolean>() {
            override fun onNext(t: Boolean) {
                onSuccess.invoke(t)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                onFail.invoke(e)
                e?.printStackTrace()
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        useCase.unsubscribe()
    }
}