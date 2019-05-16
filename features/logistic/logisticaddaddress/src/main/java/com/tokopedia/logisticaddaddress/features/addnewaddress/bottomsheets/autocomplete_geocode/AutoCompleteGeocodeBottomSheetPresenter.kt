package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteGeocodeMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteGeocodeUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */

@AddNewAddressScope
class AutoCompleteGeocodeBottomSheetPresenter @Inject constructor(private val autocompleteGeocodeUseCase: AutocompleteGeocodeUseCase,
                                                                  private val autoCompleteGeocodeMapper: AutoCompleteGeocodeMapper)
    : BaseDaggerPresenter<AutoCompleteGeocodeBottomSheetView>() {

    fun getPointOfInterest(lat: Double?, long: Double?) {
        autocompleteGeocodeUseCase.setParams(lat, long)
        autocompleteGeocodeUseCase.execute(RequestParams.create(), AutoCompleteGeocodeBottomSheetSubscriber(view, autoCompleteGeocodeMapper))
    }

    override fun detachView() {
        super.detachView()
        autocompleteGeocodeUseCase.unsubscribe()
    }

    fun clearCache() {
        autocompleteGeocodeUseCase.clearCache()
    }
}