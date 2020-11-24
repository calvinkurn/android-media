package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.common.AddressConstants.CIRCUIT_BREAKER_ON_CODE
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteGeocodeMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutoCompleteUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteGeocodeUseCase
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */

@AddNewAddressScope
class AutocompleteBottomSheetPresenter @Inject constructor(private val autocompleteGeocodeUseCase: AutocompleteGeocodeUseCase,
                                                           private val autoCompleteGeocodeMapper: AutocompleteGeocodeMapper,
                                                           private val autoCompleteUseCase: AutoCompleteUseCase)
    : BaseDaggerPresenter<AutocompleteBottomSheetListener>() {

    private var saveAddressDataModel = SaveAddressDataModel()

    fun getAutocompleteGeocode(lat: Double?, long: Double?) {
        autocompleteGeocodeUseCase.setParams(lat, long)
        autocompleteGeocodeUseCase.execute(RequestParams.create(), AutocompleteGeocodeSubscriber(view, autoCompleteGeocodeMapper))
    }

    fun getAutocomplete(input: String) {
        autoCompleteUseCase
                .execute(input)
                .subscribe(object : Subscriber<Place>() {
                    override fun onNext(t: Place) {
                        if (t.errorCode == CIRCUIT_BREAKER_ON_CODE) {
                            view.goToAddNewAddressNegative()
                        } else {
                            view.onSuccessGetAutocomplete(t)
                        }
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        Timber.d(e)
                        view.hideListPointOfInterest()
                    }
                })
    }

    override fun detachView() {
        super.detachView()
        autocompleteGeocodeUseCase.unsubscribe()
        autoCompleteUseCase.unsubscribe()
    }

    fun clearCacheAutocompleteGeocode() {
        autocompleteGeocodeUseCase.clearCache()
    }

    fun getUnnamedRoadModelFormat(): SaveAddressDataModel {
        val fmt = this.saveAddressDataModel.formattedAddress.replace("Unnamed Road, ", "")
        return this.saveAddressDataModel.copy(formattedAddress = fmt, selectedDistrict = fmt)
    }

}