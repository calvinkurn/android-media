package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteGeocodeMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutoCompleteUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteGeocodeUseCase
import com.tokopedia.logisticdata.data.autocomplete.SuggestedPlace
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

    fun getAutocompleteGeocode(lat: Double?, long: Double?) {
        autocompleteGeocodeUseCase.setParams(lat, long)
        autocompleteGeocodeUseCase.execute(RequestParams.create(), AutocompleteGeocodeSubscriber(view, autoCompleteGeocodeMapper))
    }

    fun getAutocomplete(input: String) {
        autoCompleteUseCase
                .execute(input)
                .subscribe(object : Subscriber<List<SuggestedPlace>>() {
                    override fun onNext(t: List<SuggestedPlace>) {
                        view.onSuccessGetAutocomplete(t)
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

}