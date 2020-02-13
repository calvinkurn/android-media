package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.common.AddressConstants.ANA_POSITIVE
import com.tokopedia.logisticaddaddress.common.AddressConstants.LOGISTIC_LABEL
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
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
            AddNewAddressAnalytics.eventClickButtonSimpanSuccess(eventLabel = LOGISTIC_LABEL)
        } else {
            AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess(eventLabel = LOGISTIC_LABEL)
        }

        val response = mapper.map(t)
        if (response.data.isSuccess == 1) {
            saveAddressDataModel.id = response.data.addressId
            view.onSuccessAddAddress(saveAddressDataModel)
        } else {
            view.showError(Throwable())
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (typeForm.equals(ANA_POSITIVE, true)) {
            AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(e?.printStackTrace().toString(), eventLabel = LOGISTIC_LABEL)
        } else {
            AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(e?.printStackTrace().toString(), eventLabel = LOGISTIC_LABEL)
        }
        e?.printStackTrace()
    }
}