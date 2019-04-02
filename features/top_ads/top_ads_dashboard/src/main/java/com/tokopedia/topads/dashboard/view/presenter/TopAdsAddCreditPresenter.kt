package com.tokopedia.topads.dashboard.view.presenter


import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDataCreditUseCase
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditView

import javax.inject.Inject

import rx.Subscriber

/**
 * Created by Nisie on 5/9/16.
 */
class TopAdsAddCreditPresenter @Inject
constructor(private var useCase: TopAdsGetDataCreditUseCase) : BaseDaggerPresenter<TopAdsAddCreditView>() {

    fun populateCreditList() {
        useCase.execute(object : Subscriber<List<DataCredit>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                view?.onLoadCreditListError()
            }

            override fun onNext(dataCredits: List<DataCredit>) {
                view?.onCreditListLoaded(dataCredits)
            }
        })
    }

    override fun detachView() {
        super.detachView()
        useCase.unsubscribe()
    }
}