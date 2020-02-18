package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.common.AddressConstants.LOGISTIC_LABEL
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetZipCodeUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressPresenter
@Inject constructor(private val context: Context,
                    private val addAddressUseCase: AddAddressUseCase,
                    private val zipCodeUseCase: GetZipCodeUseCase,
                    private val addAddressMapper: AddAddressMapper)
    : BaseDaggerPresenter<AddEditAddressListener>() {
    var googleApiClient: GoogleApiClient? = null

    fun connectGoogleApi(addEditAddressFragment: AddEditAddressFragment) {
        this.googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(addEditAddressFragment)
                .addOnConnectionFailedListener(addEditAddressFragment)
                .build()

        this.googleApiClient?.connect()
    }

    fun disconnectGoogleApi() {
        googleApiClient?.let {
            if (it.isConnected) {
                it.disconnect()
            }
        }
    }

    fun saveAddress(model: SaveAddressDataModel, typeForm: String) {
        addAddressUseCase.setParams(model)
        addAddressUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse?) {
                if (typeForm.equals(AddressConstants.ANA_POSITIVE, true)) {
                    AddNewAddressAnalytics.eventClickButtonSimpanSuccess(eventLabel = LOGISTIC_LABEL)
                } else {
                    AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess(eventLabel = LOGISTIC_LABEL)
                }

                val response = addAddressMapper.map(t)
                if (response.data.isSuccess == 1) {
                    model.id = response.data.addressId
                    view.onSuccessAddAddress(model)
                } else {
                    view.showError(Throwable())
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (typeForm.equals(AddressConstants.ANA_POSITIVE, true)) {
                    AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(e?.printStackTrace().toString(), eventLabel = LOGISTIC_LABEL)
                } else {
                    AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(e?.printStackTrace().toString(), eventLabel = LOGISTIC_LABEL)
                }
                e?.printStackTrace()
            }
        })

    }

    fun getZipCodes(districtId: String) {
        zipCodeUseCase.execute(districtId)
                .subscribe(
                        { response ->
                            if (response.keroDistrictDetails.district.isNotEmpty()) {
                                response.keroDistrictDetails.district[0].let {
                                    if (it.zipCode.isNotEmpty()) {
                                        view.showZipCodes(it.zipCode)
                                    } else {
                                        view.showManualZipCodes()
                                    }
                                }
                            }
                        }, {}, {}
                )
    }

    override fun detachView() {
        super.detachView()
        addAddressUseCase.unsubscribe()
        zipCodeUseCase.unsubscribe()
    }
}