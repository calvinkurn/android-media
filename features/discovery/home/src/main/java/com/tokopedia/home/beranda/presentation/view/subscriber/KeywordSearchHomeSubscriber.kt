package com.tokopedia.home.beranda.presentation.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.presentation.view.HomeContract
import rx.Subscriber

class KeywordSearchHomeSubscriber(private val presenter: HomeContract.Presenter) : Subscriber<GraphqlResponse>() {
    override fun onNext(response: GraphqlResponse?) {
        response?.let {
            val search = response.getData<KeywordSearchData>(KeywordSearchData::class.java)
            presenter.updateKeywordSearch(search)
        }
    }

    override fun onCompleted() {
        //do nothing
    }

    override fun onError(e: Throwable?) {
        //do nothing
    }

}