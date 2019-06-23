package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import android.app.Activity
import com.google.android.gms.location.LocationServices
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteGeocodeMapper
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteMapper
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteGeocodeUseCase
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.AutocompleteSubscriber
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */

@AddNewAddressScope
class AutocompleteBottomSheetPresenter @Inject constructor(private val autocompleteGeocodeUseCase: AutocompleteGeocodeUseCase,
                                                           private val autoCompleteGeocodeMapper: AutocompleteGeocodeMapper,
                                                           private val autocompleteUseCase: AutocompleteUseCase,
                                                           private val autoCompleteMapper: AutocompleteMapper)
    : BaseDaggerPresenter<AutocompleteBottomSheetListener>() {

    fun getAutocompleteGeocode(lat: Double?, long: Double?) {
        autocompleteGeocodeUseCase.setParams(lat, long)
        autocompleteGeocodeUseCase.execute(RequestParams.create(), AutocompleteGeocodeSubscriber(view, autoCompleteGeocodeMapper))
    }

    fun getAutocomplete(input: String) {
        autocompleteUseCase.setParams(input)
        autocompleteUseCase.execute(RequestParams.create(), AutocompleteSubscriber(view, autoCompleteMapper))
    }

    override fun detachView() {
        super.detachView()
        autocompleteGeocodeUseCase.unsubscribe()
        autocompleteUseCase.unsubscribe()
    }

    fun clearCacheAutocompleteGeocode() {
        autocompleteGeocodeUseCase.clearCache()
    }

    fun clearCacheAutocomplete() {
        autocompleteUseCase.clearCache()
    }
}