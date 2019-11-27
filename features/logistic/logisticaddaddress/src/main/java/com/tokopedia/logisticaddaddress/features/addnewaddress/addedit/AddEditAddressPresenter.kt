package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetZipCodeUseCase
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.usecase.RequestParams
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

    fun saveAddress(saveAddressDataModel: SaveAddressDataModel?, typeForm: String) {
        saveAddressDataModel?.let {
            addAddressUseCase.setParams(it)
            addAddressUseCase.execute(RequestParams.create(), AddAddressSubscriber(view, addAddressMapper, it, typeForm))
        }
    }

    fun getZipCodes(districtId: String) {
        zipCodeUseCase.execute(districtId)
                .subscribe(
                        { response ->
                            if (response.keroDistrictRecommendation.district.isNotEmpty()) {
                                response.keroDistrictRecommendation.district[0].let {
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