package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.get_district

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressScope
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-16.
 */

@AddNewAddressScope
class GetDistrictBottomSheetPresenter @Inject constructor(private val getDistrictUseCase: GetDistrictUseCase,
                                                          private val getDistrictMapper: GetDistrictMapper)
    : BaseDaggerPresenter<GetDistrictBottomSheetView>() {

    fun getDistrict(placeId: String) {
        getDistrictUseCase.setParams(placeId)
        getDistrictUseCase.execute(RequestParams.create(), GetDistrictBottomSheetSubscriber(view, getDistrictMapper))
    }

    override fun detachView() {
        super.detachView()
        getDistrictUseCase.unsubscribe()
    }

    fun clearCache() {
        getDistrictUseCase.clearCache()
    }
}