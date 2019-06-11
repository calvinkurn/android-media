package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutofillMapper
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutofillUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.AutofillSubscriber
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.GetDistrictSubscriber
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */

@AddNewAddressScope
class PinpointMapPresenter @Inject constructor(private val context: Context,
                                               private val getDistrictUseCase: GetDistrictUseCase,
                                               private val getDistrictMapper: GetDistrictMapper,
                                               private val autofillUseCase: AutofillUseCase,
                                               private val autofillMapper: AutofillMapper,
                                               private val districtBoundaryUseCase: DistrictBoundaryUseCase,
                                               private val districtBoundaryMapper: DistrictBoundaryMapper): BaseDaggerPresenter<PinpointMapListener>() {
    var googleApiClient: GoogleApiClient? = null

    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }
    private var saveAddressDataModel = SaveAddressDataModel()

    fun connectGoogleApi(mapFragment: PinpointMapFragment) {
        this.googleApiClient = GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(mapFragment)
                .addOnConnectionFailedListener(mapFragment)
                .build()

        this.googleApiClient?.connect()
    }

    fun disconnectGoogleApi() {
        if (googleApiClient?.isConnected!!) {
            googleApiClient?.disconnect()
        }
    }

    fun getDistrict(placeId: String) {
        getDistrictUseCase.setParams(placeId)
        getDistrictUseCase.execute(RequestParams.create(), GetDistrictSubscriber(view, getDistrictMapper))
    }

    fun autofill(latlng: String) {
        autofillUseCase.setParams(latlng)
        autofillUseCase.execute(RequestParams.create(), AutofillSubscriber(view, autofillMapper))
    }

    override fun detachView() {
        super.detachView()
        getDistrictUseCase.unsubscribe()
        autofillUseCase.unsubscribe()
    }

    fun clearCacheGetDistrict() {
        getDistrictUseCase.clearCache()
    }

    fun clearCacheAutofill() {
        autofillUseCase.clearCache()
    }

    fun loadAddEdit(isOriginMismatched: Boolean?) {
        if (saveAddressDataModel.districtId == 0 && saveAddressDataModel.postalCode.isEmpty()) {
            view.showFailedDialog()
        } else {
            isOriginMismatched?.let { view.goToAddEditActivity(false, it) }
        }
    }

    fun convertGetDistrictToSaveAddressDataUiModel(getDistrictDataUiModel: GetDistrictDataUiModel) : SaveAddressDataModel {
        val saveAddressDataModel = SaveAddressDataModel()
        saveAddressDataModel.title = getDistrictDataUiModel.title
        saveAddressDataModel.formattedAddress = getDistrictDataUiModel.formattedAddress
        saveAddressDataModel.districtId = getDistrictDataUiModel.districtId
        saveAddressDataModel.provinceId = getDistrictDataUiModel.provinceId
        saveAddressDataModel.cityId = getDistrictDataUiModel.cityId
        saveAddressDataModel.postalCode = getDistrictDataUiModel.postalCode
        saveAddressDataModel.latitude = getDistrictDataUiModel.latitude
        saveAddressDataModel.longitude = getDistrictDataUiModel.longitude
        this.saveAddressDataModel = saveAddressDataModel
        return saveAddressDataModel
    }

    fun convertAutofillToSaveAddressDataUiModel(autofillDataUiModel: AutofillDataUiModel) : SaveAddressDataModel {
        val saveAddressDataModel = SaveAddressDataModel()
        saveAddressDataModel.title = autofillDataUiModel.title
        saveAddressDataModel.formattedAddress = autofillDataUiModel.formattedAddress
        saveAddressDataModel.districtId = autofillDataUiModel.districtId
        saveAddressDataModel.provinceId = autofillDataUiModel.provinceId
        saveAddressDataModel.cityId = autofillDataUiModel.cityId
        saveAddressDataModel.postalCode = autofillDataUiModel.postalCode
        saveAddressDataModel.latitude = autofillDataUiModel.latitude
        saveAddressDataModel.longitude = autofillDataUiModel.longitude
        this.saveAddressDataModel = saveAddressDataModel
        return saveAddressDataModel
    }

    fun getSaveAddressDataModel() : SaveAddressDataModel {
        return this.saveAddressDataModel
    }

    fun getDistrictBoundary(districtId: Int, keroToken: String, keroUt: Int) {
        districtBoundaryUseCase.setParams(districtId, keroToken, keroUt)
        districtBoundaryUseCase.execute(RequestParams.create(), DistrictBoundarySubscriber(view, districtBoundaryMapper))
    }
}