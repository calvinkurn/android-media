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
                .doOnSubscribe { view?.showLoading() }
                .subscribe(object : Subscriber<AddressResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.let {
                            it.hideLoading()
                            it.showGetListError(e)
                        }
                    }

                    override fun onNext(addressResponse: AddressResponse) {
                        view?.let {
                            it.hideLoading()
                            if (!addressResponse.addresses.isNullOrEmpty()) {
                                it.renderList(mapper.transformToViewModel(addressResponse),
                                        addressResponse.isNextAvailable)
                            } else {
                                it.showNoResultMessage()
                            }
                        }
                    }
                })
    }

    override fun loadData(query: String, page: Int, token: Token) {
        view?.let {
            it.showLoading()
            val params = RequestParams.create().apply {
                putString(GetDistrictRequestUseCase.PARAM_QUERY, query)
                putString(GetDistrictRequestUseCase.PARAM_PAGE, page.toString())
                putString(GetDistrictRequestUseCase.PARAM_TOKEN, token.districtRecommendation)
                putString(GetDistrictRequestUseCase.PARAM_UT, token.ut.toString())
            }
            restUsecase.execute(params, GetDistrictRecommendationSubscriber(it, mapper))
        }
    }

}
