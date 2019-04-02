package com.tokopedia.broadcast.message.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.broadcast.message.data.model.BlastMessageMutation
import com.tokopedia.broadcast.message.data.model.BlastMessageResponse
import com.tokopedia.broadcast.message.domain.interactor.SaveChatBlastSellerUseCase
import com.tokopedia.broadcast.message.view.listener.BroadcastMessagePreviewView
import rx.Subscriber
import javax.inject.Inject

class BroadcastMessagePreviewPresenter @Inject constructor(private val useCase: SaveChatBlastSellerUseCase)
    : BaseDaggerPresenter<BroadcastMessagePreviewView>(){

    fun sendBlastMessage(model: BlastMessageMutation){
        view?.showLoading()
        useCase.execute(SaveChatBlastSellerUseCase.createRequestParams(model), object : Subscriber<BlastMessageResponse>() {
            override fun onNext(response: BlastMessageResponse) {
                view?.onSuccessSubmitBlastMessage(response)
                view?.hideLoading()
            }

            override fun onCompleted() {}

            override fun onError(t: Throwable?) {
                view?.onErrorSubmitBlastMessage(t)
                view?.hideLoading()
            }
        })
    }

    override fun detachView() {
        super.detachView()
        useCase.unsubscribe()
    }
}