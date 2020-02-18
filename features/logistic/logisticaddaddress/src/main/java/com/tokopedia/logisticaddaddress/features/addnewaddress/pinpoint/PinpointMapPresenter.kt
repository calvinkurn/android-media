package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.app.Activity
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.LOGISTIC_LABEL
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.GetDistrictSubscriber
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.response.Data
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
                                               private val getDistrictMapper: GetDistrictMapper,
                                               private val revGeocodeUseCase: RevGeocodeUseCase,
                                               private val districtBoundaryUseCase: DistrictBoundaryUseCase,
                                               private val districtBoundaryMapper: DistrictBoundaryMapper) : BaseDaggerPresenter<PinpointMapListener>() {

    private var saveAddressDataModel = SaveAddressDataModel()
    private var permissionCheckerHelper: PermissionCheckerHelper? = null

    fun getDistrict(placeId: String) {
        getDistrictUseCase.clearCache()
        getDistrictUseCase.setParams(placeId)
        getDistrictUseCase.execute(RequestParams.create(), object: Subscriber<GraphqlResponse>() {
            override fun onNext(t: GraphqlResponse?) {
                val getDistrictResponseUiModel = getDistrictMapper.map(t)
                view.onSuccessPlaceGetDistrict(getDistrictResponseUiModel.data)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
               e?.printStackTrace()
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
                                var errMsg = ""
                                if (it.messageError.isNotEmpty()) errMsg = it.messageError[0]
                                view?.onSuccessAutofill(it.data, errMsg)
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

    fun loadAddEdit(isMismatchSolved: Boolean, isChangesRequested: Boolean) {
        if (saveAddressDataModel.districtId == 0 && saveAddressDataModel.postalCode.isEmpty()) {
            view.showFailedDialog()

            AddNewAddressAnalytics.eventClickButtonPilihLokasiIniNotSuccess(eventLabel = LOGISTIC_LABEL)
            AddNewAddressAnalytics.eventClickButtonTandaiLokasiChangeAddressNegativeFailed(eventLabel = LOGISTIC_LABEL)
        } else if (saveAddressDataModel.postalCode.isEmpty()) {
            view.goToAddEditActivity(true, isMismatchSolved, isUnnamedRoad = false, isZipCodeNull = true)
        } else {
            if (isChangesRequested) {
                view.finishBackToAddEdit(false, isMismatchSolved)
            } else {
                view.goToAddEditActivity(false, isMismatchSolved, false, false)
            }

            AddNewAddressAnalytics.eventClickButtonPilihLokasiIniSuccess(eventLabel = LOGISTIC_LABEL)
            AddNewAddressAnalytics.eventClickButtonTandaiLokasiChangeAddressNegativeSuccess(eventLabel = LOGISTIC_LABEL)
        }
    }

    fun convertGetDistrictToSaveAddressDataUiModel(getDistrictDataUiModel: GetDistrictDataUiModel, zipCodes: MutableList<String>?): SaveAddressDataModel {
        val saveAddressDataModel = SaveAddressDataModel()
        saveAddressDataModel.title = getDistrictDataUiModel.title
        saveAddressDataModel.formattedAddress = getDistrictDataUiModel.formattedAddress
        saveAddressDataModel.districtId = getDistrictDataUiModel.districtId
        saveAddressDataModel.provinceId = getDistrictDataUiModel.provinceId
        saveAddressDataModel.cityId = getDistrictDataUiModel.cityId
        saveAddressDataModel.postalCode = getDistrictDataUiModel.postalCode
        saveAddressDataModel.latitude = getDistrictDataUiModel.latitude
        saveAddressDataModel.longitude = getDistrictDataUiModel.longitude
        saveAddressDataModel.selectedDistrict = getDistrictDataUiModel.formattedAddress
        if (zipCodes != null) {
            saveAddressDataModel.zipCodes = zipCodes
        }
        this.saveAddressDataModel = saveAddressDataModel
        return saveAddressDataModel
    }

    fun convertAutofillToSaveAddressDataUiModel(autoFillModel: Data, zipCodes: MutableList<String>?): SaveAddressDataModel {
        val saveAddressDataModel = SaveAddressDataModel()
        saveAddressDataModel.title = autoFillModel.title
        saveAddressDataModel.formattedAddress = autoFillModel.formattedAddress
        saveAddressDataModel.districtId = autoFillModel.districtId
        saveAddressDataModel.provinceId = autoFillModel.provinceId
        saveAddressDataModel.cityId = autoFillModel.cityId
        saveAddressDataModel.postalCode = autoFillModel.postalCode
        saveAddressDataModel.latitude = autoFillModel.latitude
        saveAddressDataModel.longitude = autoFillModel.longitude
        saveAddressDataModel.selectedDistrict = autoFillModel.formattedAddress
        if (zipCodes != null) {
            saveAddressDataModel.zipCodes = zipCodes
        }
        this.saveAddressDataModel = saveAddressDataModel
        return saveAddressDataModel
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
        districtBoundaryUseCase.execute(RequestParams.create(), DistrictBoundarySubscriber(view, districtBoundaryMapper))
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