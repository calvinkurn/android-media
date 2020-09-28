package com.tokopedia.topads.keyword.view.presenter

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant
import com.tokopedia.topads.dashboard.data.model.request.DataSuggestions
import com.tokopedia.topads.dashboard.data.model.request.MinimumBidRequest
import com.tokopedia.topads.dashboard.domain.model.MinimumBidDomain
import com.tokopedia.topads.keyword.domain.interactor.TopAdsKeyMinimumBidUseCase
import com.tokopedia.topads.keyword.domain.interactor.TopAdsKeywordAddUseCase
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewAddView
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase
import rx.Subscriber
import javax.inject.Inject

class TopAdsKeywordNewAddPresenter @Inject constructor(val useCase: TopAdsKeywordAddUseCase,
                                                       val minimumBidUseCase: TopAdsKeyMinimumBidUseCase,
                                                       val sourceTaggingUseCase: TopAdsGetSourceTaggingUseCase) :
        BaseDaggerPresenter<TopAdsKeywordNewAddView>() {
    var minBid:MutableLiveData<Int> = MutableLiveData()

    fun addKeywords(keywords: List<AddKeywordDomainModelDatum>) {
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

    fun getBidInfo(requestType: String?, dataSuggestions: List<DataSuggestions?>?, source: String?, shopId: Int, resources: Resources) {
        val request = MinimumBidRequest()
        request.source = source
        request.shopId = shopId
        request.requestType = requestType
        request.dataSuggestions = dataSuggestions
        minimumBidUseCase.setQuery(resources)
        minimumBidUseCase.execute(minimumBidUseCase.getBidParams(request),
                object : Subscriber<MinimumBidDomain.TopadsBidInfo?>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(data: MinimumBidDomain.TopadsBidInfo?) {
                        minBid.postValue(data?.data?.get(0)?.minBid)
                    }
                })
    }

    private fun getAddKeywordSubscriber(): Subscriber<AddKeywordDomainModel> {
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
        minimumBidUseCase.unsubscribe()
        useCase.unsubscribe()
    }
}