package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import javax.inject.Inject

@Deprecated("Please use MVVM with DiscomViewModel")
class DiscomPresenter @Inject constructor(
    private val revGeocodeUseCase: RevGeocodeUseCase,
    private val gqlUsecase: GetDistrictRecommendation,
    private val mapper: DistrictRecommendationMapper
) : DiscomContract.Presenter {

    private var view: DiscomContract.View? = null
    private var isMapsAvailable: Boolean = true

    override fun attach(view: DiscomContract.View) {
        this.view = view
    }

    override fun detach() {
        gqlUsecase.unsubscribe()
        view = null
    }

    /**
     * New method to load district recommendations using graphql, token is not required anymore, one
     * thing to notice: this has not been tested for massive hits, it was meant only just for the
     * NANA feature, thus the hit load is still divided with REST loadData
     */
    override fun loadData(query: String, page: Int) {
        SimpleIdlingResource.increment()
        gqlUsecase.execute(query, page)
            .doOnSubscribe { view?.setLoadingState(true) }
            .subscribe(
                {
                    val model = mapper.transform(it)
                    deliverToView(model)
                },
                {
                    view?.setLoadingState(false)
                    view?.showGetListError(it)
                },
                { SimpleIdlingResource.decrement() }
            )
    }

    private fun deliverToView(response: AddressResponse) {
        view?.let {
            it.setLoadingState(false)
            if (!response.addresses.isEmpty()) {
                it.renderData(response.addresses, response.isNextAvailable)
            } else {
                it.showEmpty()
            }
        }
    }

    override fun autoFill(lat: Double, long: Double) {
        val param = "$lat,$long"
        revGeocodeUseCase.clearCache()
        revGeocodeUseCase.execute(param)
            .subscribe(
                {
                    when {
                        it.messageError.isEmpty() -> {
                            view?.setResultDistrict(it.data, lat, long)
                        }
                        it.errorCode == AddressConstants.CIRCUIT_BREAKER_ON_CODE -> {
                            view?.showToasterError("")
                        }
                        else -> {
                            val msg = it.messageError[0]
                            when {
                                msg.contains(GetDistrictUseCase.FOREIGN_COUNTRY_MESSAGE) -> {
                                    view?.showToasterError(GetDistrictUseCase.FOREIGN_COUNTRY_MESSAGE)
                                }
                                msg.contains(GetDistrictUseCase.LOCATION_NOT_FOUND_MESSAGE) -> {
                                    view?.showToasterError(GetDistrictUseCase.LOCATION_NOT_FOUND_MESSAGE)
                                }
                            }
                        }
                    }
                },
                {
                    it?.printStackTrace()
                },
                {}
            )
    }

    override fun setLocationAvailability(available: Boolean) {
        isMapsAvailable = available
    }

    override fun getLocationAvailability(): Boolean {
        return isMapsAvailable
    }
}
