package com.tokopedia.broadcast.message.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller
import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData
import com.tokopedia.broadcast.message.common.domain.interactor.GetChatBlastSellerMetaDataUseCase
import com.tokopedia.broadcast.message.domain.interactor.GetChatBlastSellerUseCase
import com.tokopedia.broadcast.message.view.listener.BroadcastMessageListView
import rx.Subscriber
import javax.inject.Inject

class BroadcastMessageListPresenter @Inject
    constructor (private val getMetaDataUseCase: GetChatBlastSellerMetaDataUseCase,
                 private val getChatBlastSellerUseCase: GetChatBlastSellerUseCase): BaseDaggerPresenter<BroadcastMessageListView>(){

    fun getMetaData(){
        getMetaDataUseCase.execute(object : Subscriber<TopChatBlastSellerMetaData>() {
            override fun onNext(metaData: TopChatBlastSellerMetaData?) {
                view?.onSuccessGetMetaData(metaData)
            }

            override fun onCompleted() {}

            override fun onError(throwable: Throwable) {
                view?.onErrorGetMetaData(throwable)
            }

        })
    }

    fun getBlastMessage(page: Int, perPage: Int = 10){
        getChatBlastSellerUseCase.execute(GetChatBlastSellerUseCase.createRequestParams(page, perPage),
                object : Subscriber<TopChatBlastSeller.BlastSellerList>() {
                    override fun onNext(response: TopChatBlastSeller.BlastSellerList?) {
                        view?.onSuccessGetBlastMessage(response)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(throwable: Throwable?) {
                        view?.onErrorGetBlastMessage(throwable)
                    }

                })
    }

    override fun detachView() {
        super.detachView()
        getMetaDataUseCase.unsubscribe()
        getChatBlastSellerUseCase.unsubscribe()
    }
}