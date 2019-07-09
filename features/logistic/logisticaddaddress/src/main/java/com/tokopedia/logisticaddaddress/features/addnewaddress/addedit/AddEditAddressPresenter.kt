package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.data.AddressRepository
import com.tokopedia.logisticaddaddress.domain.mapper.AddAddressMapper
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AddAddressUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.AutocompleteSubscriber
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottomsheet_getdistrict.*
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressPresenter @Inject constructor(private val context: Context,
                                                  private val addAddressUseCase: AddAddressUseCase,
                                                  private val addAddressMapper: AddAddressMapper) : BaseDaggerPresenter<AddEditAddressListener>() {
    var googleApiClient: GoogleApiClient? = null

    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }

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
}