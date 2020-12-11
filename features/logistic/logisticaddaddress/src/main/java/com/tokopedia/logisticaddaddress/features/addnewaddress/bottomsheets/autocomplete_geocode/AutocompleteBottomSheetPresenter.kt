package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteGeocodeMapper
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */

@AddNewAddressScope
class AutocompleteBottomSheetPresenter @Inject constructor(private val autoCompleteGeocodeMapper: AutocompleteGeocodeMapper)
    : BaseDaggerPresenter<AutocompleteBottomSheetListener>() {

    /*fun getAutocompleteGeocode(lat: Double?, long: Double?) {
        autocompleteGeocodeUseCase.setParams(lat, long)
        autocompleteGeocodeUseCase.execute(RequestParams.create(), AutocompleteGeocodeSubscriber(view, autoCompleteGeocodeMapper))
    }
*/
//    override fun detachView() {
//        super.detachView()
//        autocompleteGeocodeUseCase.unsubscribe()
//    }
//
//    fun clearCacheAutocompleteGeocode() {
//        autocompleteGeocodeUseCase.clearCache()
//    }

}