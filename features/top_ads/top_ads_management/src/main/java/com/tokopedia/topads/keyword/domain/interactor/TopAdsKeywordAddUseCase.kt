package com.tokopedia.topads.keyword.domain.interactor

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.domain.repository.TopAdsKeywordRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class TopAdsKeywordAddUseCase @Inject constructor(val topAdsKeywordRepository: TopAdsKeywordRepository):
        UseCase<AddKeywordDomainModel>() {

    override fun createObservable(requestParams: RequestParams?): Observable<AddKeywordDomainModel> {
        var source: String = TopAdsNetworkConstant.VALUE_SOURCE_ANDROID
        var keywords: List<AddKeywordDomainModelDatum> = listOf()

        requestParams?.run {
            source = getString(SOURCE, "")
            keywords = getObject(KEYWORD_LIST) as List<AddKeywordDomainModelDatum>
        }

        return topAdsKeywordRepository.addKeywords(keywords, source)
    }

    companion object {
        private const val SOURCE = "source"
        private const val KEYWORD_LIST = "key_list"

        fun createRequestParam(keywords: List<AddKeywordDomainModelDatum>, source: String): RequestParams{
            return RequestParams.create().apply {
                putString(SOURCE, source)
                putObject(KEYWORD_LIST, keywords)
            }
        }
    }
}