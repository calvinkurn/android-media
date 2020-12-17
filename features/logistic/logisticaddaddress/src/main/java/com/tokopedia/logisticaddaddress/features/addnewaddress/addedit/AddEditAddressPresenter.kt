package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.domain.model.add_address.AddAddressResponse
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.AutoCompleteUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetZipCodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressPresenter
@Inject constructor(
        private val addAddressUseCase: AddAddressUseCase,
        private val zipCodeUseCase: GetZipCodeUseCase,
        private val getDistrictUseCase: GetDistrictUseCase,
        private val autoCompleteUseCase: AutoCompleteUseCase)
    : BaseDaggerPresenter<AddEditView>() {

    override fun detachView() {
        super.detachView()
        addAddressUseCase.unsubscribe()
        zipCodeUseCase.unsubscribe()
        autoCompleteUseCase.unsubscribe()
        getDistrictUseCase.unsubscribe()
    }

    fun saveAddress(model: SaveAddressDataModel, typeForm: String, isFullFlow: Boolean, isLogisticLabel: Boolean) {
        val formType = if (typeForm == AddressConstants.ANA_POSITIVE) "1" else "0"
        SimpleIdlingResource.increment()
        addAddressUseCase
                .execute(model, formType)
                .subscribe(object : Subscriber<AddAddressResponse>() {
                    override fun onNext(response: AddAddressResponse) {
                        if (typeForm.equals(AddressConstants.ANA_POSITIVE, true)) {
                            AddNewAddressAnalytics.eventClickButtonSimpanSuccess(isFullFlow, isLogisticLabel)
                        } else {
                            AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess(isFullFlow, isLogisticLabel)
                        }
                        response.keroAddAddress.data.run {
                            if (isSuccess == 1) {
                                model.id = this.addrId
                                view?.onSuccessAddAddress(model)
                            } else {
                                view?.showError(null)
                            }
                        }
                    }

                    override fun onCompleted() {
                        SimpleIdlingResource.decrement()
                    }

                    override fun onError(e: Throwable) {
                        if (typeForm.equals(AddressConstants.ANA_POSITIVE, true)) {
                            AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(e.printStackTrace().toString(), isFullFlow, isLogisticLabel)
                        } else {
                            AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(e.printStackTrace().toString(), isFullFlow, isLogisticLabel)
                        }
                        view?.showError(e)
                    }
                })

    }

    fun getZipCodes(districtId: String) {
        SimpleIdlingResource.increment()
        zipCodeUseCase.execute(districtId)
                .subscribe(
                        { response ->
                            if (response.keroDistrictDetails.district.isNotEmpty()) {
                                response.keroDistrictDetails.district[0].let {
                                    if (it.zipCode.isNotEmpty()) {
                                        view?.showZipCodes(it.zipCode)
                                    } else {
                                        view?.showManualZipCodes()
                                    }
                                }
                            }
                        }, {}, { SimpleIdlingResource.decrement() }
                )
    }

    fun getAutoComplete(query: String) {
        autoCompleteUseCase.execute(query)
                .subscribe(
                        { modelList ->
                            getDistrict(modelList.data.first().placeId)
                        }, { t -> Timber.d(t) }, {})
    }

    private fun getDistrict(placeId: String) {
        getDistrictUseCase.execute(placeId)
                .subscribe(
                        { model ->
                            val lat = model.latitude.toDouble()
                            val long = model.longitude.toDouble()
                            view?.moveMap(lat, long)
                        },
                        {
                            Timber.d(it)
                        }, {}
                )
    }
}