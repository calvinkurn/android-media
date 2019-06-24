package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-28.
 */
class AddAddressSubscriber(val view: AddEditAddressListener,
                           val mapper: AddAddressMapper,
                           val saveAddressDataModel: SaveAddressDataModel): Subscriber<GraphqlResponse>() {
    override fun onNext(t: GraphqlResponse?) {
        val addAddressResponseUiModel = mapper.map(t)
        view.onSuccessAddAddress(addAddressResponseUiModel.data, saveAddressDataModel)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }
}