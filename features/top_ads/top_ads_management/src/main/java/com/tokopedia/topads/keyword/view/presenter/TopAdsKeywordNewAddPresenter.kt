package com.tokopedia.topads.keyword.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant
import com.tokopedia.topads.keyword.domain.interactor.TopAdsKeywordAddUseCase
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewAddView
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase
import rx.Subscriber
import javax.inject.Inject

class TopAdsKeywordNewAddPresenter @Inject constructor(val useCase: TopAdsKeywordAddUseCase,
                                                       val sourceTaggingUseCase: TopAdsGetSourceTaggingUseCase):
        BaseDaggerPresenter<TopAdsKeywordNewAddView>(){

    fun addKeywords(keywords: List<AddKeywordDomainModelDatum>){
        sourceTaggingUseCase.execute(object : Subscriber<TopAdsSourceTaggingModel>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {}

            override fun onNext(topAdsSourceTaggingModel: TopAdsSourceTaggingModel?) {
                var source = TopAdsNetworkConstant.VALUE_SOURCE_ANDROID
                if (topAdsSourceTaggingModel != null) {
                    source = topAdsSourceTaggingModel.source
                }
                useCase.execute(TopAdsKeywordAddUseCase.createRequestParam(keywords, source),
                        getAddKeywordSubscriber())
            }
        })
    }

    private fun getAddKeywordSubscriber():Subscriber<AddKeywordDomainModel>{
        return object : Subscriber<AddKeywordDomainModel>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onFailedSaveKeyword(e)
            }

            override fun onNext(addKeywordDomainModel: AddKeywordDomainModel) {
                view?.onSuccessSaveKeyword()
            }
        }
    }

    override fun detachView() {
        super.detachView()
        sourceTaggingUseCase.unsubscribe()
        useCase.unsubscribe()
    }
}