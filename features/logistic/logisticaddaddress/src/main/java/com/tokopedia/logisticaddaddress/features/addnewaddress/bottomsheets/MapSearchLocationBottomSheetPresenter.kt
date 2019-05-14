package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.usecase.AutocompleteGeocodeUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */

@AddNewAddressScope
class MapSearchLocationBottomSheetPresenter @Inject constructor(private val autocompleteGeocodeUseCase: AutocompleteGeocodeUseCase)
    : BaseDaggerPresenter<MapSearchLocationBottomSheetView>() {

    override fun detachView() {
        super.detachView()
        if (autocompleteGeocodeUseCase != null) {
            autocompleteGeocodeUseCase.unsubscribe()
        }
    }

    fun getPointOfInterest(lat: Double, long: Double) {
        autocompleteGeocodeUseCase.setParams(lat, long)
        autocompleteGeocodeUseCase.execute(RequestParams.create(), MapSearchLocationBottomSheetSubscriber(view))
    }
}