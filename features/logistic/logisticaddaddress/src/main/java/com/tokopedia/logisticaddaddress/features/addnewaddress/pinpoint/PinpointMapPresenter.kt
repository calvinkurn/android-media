package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.app.Activity
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
const val FOREIGN_COUNTRY_MESSAGE = "Lokasi di luar Indonesia."

@AddNewAddressScope
class PinpointMapPresenter @Inject constructor(private val getDistrictUseCase: GetDistrictUseCase,
                                               private val revGeocodeUseCase: RevGeocodeUseCase,
                                               private val districtBoundaryUseCase: DistrictBoundaryUseCase,
                                               private val districtBoundaryMapper: DistrictBoundaryMapper) : BaseDaggerPresenter<PinpointMapListener>() {

    private var saveAddressDataModel = SaveAddressDataModel()
    private var permissionCheckerHelper: PermissionCheckerHelper? = null

    fun getDistrict(placeId: String) {
        getDistrictUseCase
                .execute(placeId)
                .subscribe(object : Subscriber<GetDistrictDataUiModel>() {
                    override fun onNext(model: GetDistrictDataUiModel) {
                        view.onSuccessPlaceGetDistrict(model)
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        Timber.d(e)
                    }
                })
    }

    fun autofill(lat: Double, long: Double, zoom: Float) {
        Timber.d("Current zoom level : $zoom")
        if (AddNewAddressUtils.hasDefaultCoordinate(lat, long)) {
            view.showUndetectedDialog()
            return
        }
        val param = "$lat,$long"
        view.showLoading()
        revGeocodeUseCase.clearCache()
        revGeocodeUseCase.execute(param)
                .subscribe(
                        {
                            if (it.messageError.isNotEmpty() && it.messageError[0].equals(FOREIGN_COUNTRY_MESSAGE, true)) {
                                view.showOutOfReachDialog()
                            } else {
                                view?.onSuccessAutofill(it.data)
                            }
                        },
                        {
                            it?.printStackTrace()
                        }, {}
                )
    }

    override fun detachView() {
        super.detachView()
        getDistrictUseCase.unsubscribe()
        revGeocodeUseCase.unsubscribe()
        districtBoundaryUseCase.unsubscribe()
    }

    fun setAddress(address: SaveAddressDataModel) {
        this.saveAddressDataModel = address
    }

    fun getSaveAddressDataModel(): SaveAddressDataModel {
        return this.saveAddressDataModel
    }

    fun getUnnamedRoadModelFormat(): SaveAddressDataModel {
        val fmt = this.saveAddressDataModel.formattedAddress.replace("Unnamed Road, ", "")
        return this.saveAddressDataModel.copy(formattedAddress = fmt, selectedDistrict = fmt)
    }

    fun getDistrictBoundary(districtId: Int, keroToken: String, keroUt: Int) {
        districtBoundaryUseCase.setParams(districtId, keroToken, keroUt)
        districtBoundaryUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse) {
                val districtBoundaryResponseUiModel = districtBoundaryMapper.map(t)
                view.showBoundaries(districtBoundaryResponseUiModel.geometry.listCoordinates)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Timber.d(e)
            }
        })
    }

    fun requestLocation(activity: Activity) {
        permissionCheckerHelper?.let { permission ->
            val locationDetectorHelper = activity.let { act ->
                LocationDetectorHelper(
                        permission,
                        LocationServices.getFusedLocationProviderClient(act),
                        act)
            }

            locationDetectorHelper.getLocation(onGetLocation(), activity,
                    LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                    activity.getString(R.string.rationale_need_location))
        }
    }

    fun setPermissionChecker(permissionCheckerHelper: PermissionCheckerHelper?) {
        if (permissionCheckerHelper != null) {
            this.permissionCheckerHelper = permissionCheckerHelper
        }
    }

    private fun onGetLocation(): (DeviceLocation) -> Unit {
        return {
            view.showAutoComplete(it.latitude, it.longitude)
        }
    }
}