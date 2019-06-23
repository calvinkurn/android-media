package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutofillMapper
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutofillUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.AutofillSubscriber
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.GetDistrictSubscriber
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */

@AddNewAddressScope
class PinpointMapPresenter @Inject constructor(private val getDistrictUseCase: GetDistrictUseCase,
                                               private val getDistrictMapper: GetDistrictMapper,
                                               private val autofillUseCase: AutofillUseCase,
                                               private val autofillMapper: AutofillMapper,
                                               private val districtBoundaryUseCase: DistrictBoundaryUseCase,
                                               private val districtBoundaryMapper: DistrictBoundaryMapper): BaseDaggerPresenter<PinpointMapListener>() {

    private val defaultLat: Double by lazy { -6.175794 }
    private val defaultLong: Double by lazy { 106.826457 }
    private var saveAddressDataModel = SaveAddressDataModel()
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper

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

    fun loadAddEdit(isMismatchSolved: Boolean?, isChangesRequested: Boolean?) {
        if (saveAddressDataModel.districtId == 0 && saveAddressDataModel.postalCode.isEmpty()) {
            view.showFailedDialog()

            AddNewAddressAnalytics.eventClickButtonPilihLokasiIniNotSuccess()
            AddNewAddressAnalytics.eventClickButtonTandaiLokasiChangeAddressNegativeFailed()
        } else {
            isChangesRequested?.let {
                if (it) {
                    view.finishBackToAddEdit(false, it)
                } else {
                    isMismatchSolved?.let { view.goToAddEditActivity(false, it) }
                }
            }

            AddNewAddressAnalytics.eventClickButtonPilihLokasiIniSuccess()
            AddNewAddressAnalytics.eventClickButtonTandaiLokasiChangeAddressNegativeSuccess()
        }
    }

    fun convertGetDistrictToSaveAddressDataUiModel(getDistrictDataUiModel: GetDistrictDataUiModel, zipCodes: MutableList<String>?) : SaveAddressDataModel {
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

    fun convertAutofillToSaveAddressDataUiModel(autofillDataUiModel: AutofillDataUiModel, zipCodes: MutableList<String>?) : SaveAddressDataModel {
        val saveAddressDataModel = SaveAddressDataModel()
        saveAddressDataModel.title = autofillDataUiModel.title
        saveAddressDataModel.formattedAddress = autofillDataUiModel.formattedAddress
        saveAddressDataModel.districtId = autofillDataUiModel.districtId
        saveAddressDataModel.provinceId = autofillDataUiModel.provinceId
        saveAddressDataModel.cityId = autofillDataUiModel.cityId
        saveAddressDataModel.postalCode = autofillDataUiModel.postalCode
        saveAddressDataModel.latitude = autofillDataUiModel.latitude
        saveAddressDataModel.longitude = autofillDataUiModel.longitude
        saveAddressDataModel.selectedDistrict = autofillDataUiModel.formattedAddress
        if (zipCodes != null) {
            saveAddressDataModel.zipCodes = zipCodes
        }
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

    fun requestLocation(activity: Activity) {
        val locationDetectorHelper = activity.let {
            LocationDetectorHelper(
                    permissionCheckerHelper,
                    LocationServices.getFusedLocationProviderClient(it),
                    it) }

        this.let {
            locationDetectorHelper.getLocation(onGetLocation(), activity,
                    LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                    activity.getString(R.string.rationale_need_location))
        }
    }

    private fun onGetLocation(): (DeviceLocation) -> Unit {
        return {
            view.showAutoComplete(it.latitude, it.longitude)
        }
    }

    fun setPermissionChecker(permissionCheckerHelper: PermissionCheckerHelper) {
        this.permissionCheckerHelper = permissionCheckerHelper
    }
}