package com.tokopedia.flashsale.management.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flashsale.management.data.Data
import com.tokopedia.flashsale.management.domain.GetCampaignListUsecase
import com.tokopedia.flashsale.management.view.contract.CampaignContract
import rx.Subscriber
import javax.inject.Inject

class CampaignPresenter @Inject
constructor(private val getCampaignListUsecase: GetCampaignListUsecase) : BaseDaggerPresenter<CampaignContract.View>(), CampaignContract.Presenter {

    override fun getCampaignList() {
        getCampaignListUsecase.execute(GetCampaignListUsecase.createRequestParams("true", 1, 2, 3, "flash sale", "1,2,3"),
                object : Subscriber<Data>() {
                    override fun onCompleted() {}
                    override fun onError(throwable: Throwable) {
                        view.onErrorGetCampaignList(throwable)
                    }
                    override fun onNext(data: Data) {
                        view.onSuccessGetCampaignList(data)
                    }
                })
    }

    override fun detachView() {
        getCampaignListUsecase.unsubscribe()
        super.detachView()
    }
}