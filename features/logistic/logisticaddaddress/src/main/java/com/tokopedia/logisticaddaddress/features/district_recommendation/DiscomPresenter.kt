package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class DiscomPresenter @Inject constructor(
        private val restUsecase: GetDistrictRequestUseCase,
        private val revGeocodeUseCase: RevGeocodeUseCase,
        private val gqlUsecase: GetDistrictRecommendation,
        private val mapper: DistrictRecommendationMapper) : DiscomContract.Presenter {

    private var view: DiscomContract.View? = null

    override fun attach(view: DiscomContract.View) {
        this.view = view
    }

    override fun detach() {
        restUsecase.unsubscribe()
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
                        }, { SimpleIdlingResource.decrement() }
                )
    }

    /**
     * Loads district recommendations by rest api, it uses mandatory kero token to
     * request, if the token is not provided, please use the graphql version
     * @see DiscomPresenter.loadData
     */
    override fun loadData(query: String, page: Int, token: Token) {
        view?.setLoadingState(true)
        val params = RequestParams.create().apply {
            putString(GetDistrictRequestUseCase.PARAM_QUERY, query)
            putString(GetDistrictRequestUseCase.PARAM_PAGE, page.toString())
            putString(GetDistrictRequestUseCase.PARAM_TOKEN, token.districtRecommendation)
            putString(GetDistrictRequestUseCase.PARAM_UT, token.ut.toString())
        }
        restUsecase.execute(params, getLoadDataObserver())
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

    private fun getLoadDataObserver() = object : Subscriber<AddressResponse>() {
        override fun onNext(response: AddressResponse?) {
            response?.let { deliverToView(it) }
        }

        override fun onError(e: Throwable?) {
            view?.setLoadingState(false)
            view?.showGetListError(e ?: Throwable())
        }

        override fun onCompleted() {}
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
                                    view?.showToasterError()
                                }
                                else -> {
                                    val msg = it.messageError[0]
                                    when {
                                        msg.contains(GetDistrictUseCase.FOREIGN_COUNTRY_MESSAGE) -> {
                                            view?.showToasterError()
                                        }
                                        msg.contains(GetDistrictUseCase.LOCATION_NOT_FOUND_MESSAGE) -> {
                                            view?.showToasterError()
                                        }
                                    }
                                }
                            }
                        },
                        {
                            it?.printStackTrace()
                        }, {}
                )
    }

}
