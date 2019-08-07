package com.tokopedia.logisticaddaddress.features.district_recommendation

import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class DiscomPresenter @Inject
constructor(private val restUsecase: GetDistrictRequestUseCase,
            private val gqlUsecase: GetDistrictRecommendation,
            private val mapper: AddressViewModelMapper)
    : DiscomContract.Presenter {

    private var view: DiscomContract.View? = null

    override fun attach(view: DiscomContract.View) {
        this.view = view
    }

    override fun detach() {
        restUsecase.unsubscribe()
        gqlUsecase.unsubscribe()
        view = null
    }

    override fun loadData(query: String, page: Int) {
        gqlUsecase.execute(query, page)
                .doOnSubscribe { view?.setLoadingState(true) }
                .subscribe(
                        { response: AddressResponse ->
                            view?.let {
                                it.setLoadingState(false)
                                if (!response.addresses.isNullOrEmpty()) {
                                    it.renderData(mapper.transform(response), response.isNextAvailable)
                                } else {
                                    it.showEmpty()
                                }
                            }
                        },
                        { error: Throwable ->
                            view?.let {
                                it.setLoadingState(false)
                                it.showGetListError(error)
                            }
                        }, {})
    }

    override fun loadData(query: String, page: Int, token: Token) {
        view?.setLoadingState(true)
        val params = RequestParams.create().apply {
            putString(GetDistrictRequestUseCase.PARAM_QUERY, query)
            putString(GetDistrictRequestUseCase.PARAM_PAGE, page.toString())
            putString(GetDistrictRequestUseCase.PARAM_TOKEN, token.districtRecommendation)
            putString(GetDistrictRequestUseCase.PARAM_UT, token.ut.toString())
        }
        restUsecase.execute(params, object : Subscriber<AddressResponse>() {
            override fun onNext(response: AddressResponse?) {
                view?.let {
                    it.setLoadingState(false)
                    if (!response?.addresses.isNullOrEmpty()) {
                        it.renderData(mapper.transform(response), response!!.isNextAvailable)
                    } else {
                        it.showEmpty()
                    }
                }
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                view?.setLoadingState(false)
                view?.showGetListError(e?: Throwable())
            }

            override fun onCompleted() {}
        })
    }

}
