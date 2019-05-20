package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutofillMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.MapViewListener
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-17.
 */
class AutofillSubscriber(val view: MapViewListener,
                         val mapper: AutofillMapper): Subscriber<GraphqlResponse>() {

    override fun onNext(t: GraphqlResponse?) {
        val autofillResponseUiModel = mapper.map(t)
        view.onSuccessAutofill(autofillResponseUiModel.data)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }
}