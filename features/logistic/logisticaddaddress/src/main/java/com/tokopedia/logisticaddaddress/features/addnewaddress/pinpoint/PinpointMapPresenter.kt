package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.common.AddressConstants.CIRCUIT_BREAKER_ON_CODE
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictBoundaryUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase.Companion.FOREIGN_COUNTRY_MESSAGE
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase.Companion.LOCATION_NOT_FOUND_MESSAGE
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-08.
 */
@AddNewAddressScope
class PinpointMapPresenter @Inject constructor(private val getDistrictUseCase: GetDistrictUseCase,
                                               private val revGeocodeUseCase: RevGeocodeUseCase,
                                               private val districtBoundaryUseCase: DistrictBoundaryUseCase,
                                               private val districtBoundaryMapper: DistrictBoundaryMapper) : BaseDaggerPresenter<PinpointMapView>() {

    private var saveAddressDataModel = SaveAddressDataModel()

    fun getDistrict(placeId: String) {
        SimpleIdlingResource.increment()
        getDistrictUseCase
                .execute(placeId)
                .subscribe(object : Subscriber<GetDistrictDataUiModel>() {
                    override fun onNext(model: GetDistrictDataUiModel) {
                        if (model.errorCode == CIRCUIT_BREAKER_ON_CODE) {
                            view.goToAddNewAddressNegative()
                        } else {
                            view.onSuccessPlaceGetDistrict(model)
                        }
                    }

                    override fun onCompleted() {
                        SimpleIdlingResource.decrement()
                    }

                    override fun onError(e: Throwable?) {
                        Timber.d(e)
                    }
                })
    }

    fun autoFill(lat: Double, long: Double, zoom: Float) {
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
                            if (it.messageError.isEmpty()) {
                                view?.onSuccessAutofill(it.data)
                            } else if (it.errorCode == CIRCUIT_BREAKER_ON_CODE){
                                view?.goToAddNewAddressNegative()
                            } else {
                                val msg = it.messageError[0]
                                when {
                                    msg.contains(FOREIGN_COUNTRY_MESSAGE) -> view?.showOutOfReachDialog()
                                    msg.contains(LOCATION_NOT_FOUND_MESSAGE) -> {
                                        saveAddressDataModel = SaveAddressDataModel()
                                        view?.showLocationNotFoundCTA()
                                    }
                                }
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

    fun getDistrictBoundary(districtId: Int, keroToken: String?, keroUt: Int) {
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

}