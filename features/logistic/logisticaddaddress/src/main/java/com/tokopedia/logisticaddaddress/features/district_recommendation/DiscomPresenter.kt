package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class DiscomPresenter @Inject constructor(
        private val restUsecase: GetDistrictRequestUseCase,
        private val gqlUsecase: GetDistrictRecommendation) : DiscomContract.Presenter {

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
        gqlUsecase.execute(query, page)
                .doOnSubscribe { view?.setLoadingState(true) }
                .subscribe(getLoadDataObserver())
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

    private fun getLoadDataObserver() = object : Subscriber<AddressResponse>() {
        override fun onNext(response: AddressResponse?) {
            view?.let {
                it.setLoadingState(false)
                if (!response?.addresses.isNullOrEmpty()) {
                    it.renderData(response!!.addresses, response.isNextAvailable)
                } else {
                    it.showEmpty()
                }
            }
        }

        override fun onError(e: Throwable?) {
            view?.setLoadingState(false)
            view?.showGetListError(e ?: Throwable())
        }

        override fun onCompleted() {}
    }

}
