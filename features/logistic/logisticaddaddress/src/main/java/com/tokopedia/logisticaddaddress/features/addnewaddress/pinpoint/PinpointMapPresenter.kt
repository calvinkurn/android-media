package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.places.Places
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutofillMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutofillUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.AutofillSubscriber
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.GetDistrictSubscriber
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
                                       private val autofillMapper: AutofillMapper): BaseDaggerPresenter<PinpointMapListener>() {
    var googleApiClient: GoogleApiClient? = null

    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }

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

    fun onResult(locationSettingResult: LocationSettingsResult) {
        /*val status: Status = locationSettingResult.status
        when(status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS -> {
                view.moveMap(PinpointMapUtils.generateLatLng(defaultLat, defaultLong))
            }
        }*/
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

    fun loadAddEdit(saveAddressDataModel: SaveAddressDataModel) {
        if (saveAddressDataModel.districtId == 0 && saveAddressDataModel.postalCode?.isEmpty()!!) {
            view.showFailedDialog()
        } else {
            val intent = Intent(context, AddEditAddressActivity::class.java)
            // intent.putExtra(AddressConstants.EXTRA_LAT, lat.toDouble())
            // intent.putExtra(AddressConstants.EXTRA_LONG, long.toDouble())
            // intent.putExtra(AddressConstants.EXTRA_DETAIL_ADDRESS, detailAddress)
            intent.putExtra(AddressConstants.EXTRA_IS_MISMATCH, false)
            intent.putExtra(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL, saveAddressDataModel)
            context.startActivity(intent)
        }
    }
}