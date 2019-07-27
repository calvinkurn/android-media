package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.AddressConstants.ANA_POSITIVE
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-28.
 */
class AddAddressSubscriber(val view: AddEditAddressListener,
                           val mapper: AddAddressMapper,
                           val saveAddressDataModel: SaveAddressDataModel,
                           val typeForm: String): Subscriber<GraphqlResponse>() {
    override fun onNext(t: GraphqlResponse?) {
        if (typeForm.equals(ANA_POSITIVE, true)) {
            AddNewAddressAnalytics.eventClickButtonSimpanSuccess()
        } else {
            AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess()
        }

        val addAddressResponseUiModel = mapper.map(t)
        saveAddressDataModel.id = addAddressResponseUiModel.data.addressId
        view.onSuccessAddAddress(saveAddressDataModel)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (typeForm.equals(ANA_POSITIVE, true)) {
            AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(e?.printStackTrace().toString())
        } else {
            AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(e?.printStackTrace().toString())
        }
        e?.printStackTrace()
    }
}