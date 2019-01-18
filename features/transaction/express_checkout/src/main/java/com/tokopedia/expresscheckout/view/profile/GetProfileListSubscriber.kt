package com.tokopedia.expresscheckout.view.profile

import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 18/12/19.
 */

class GetProfileListSubscriber(val view: CheckoutProfileContract.View?, val presenter: CheckoutProfileContract.Presenter) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
    }

    override fun onNext(response: GraphqlResponse?) {
        view?.hideLoading()
        // Todo : convert to view model
    }

}