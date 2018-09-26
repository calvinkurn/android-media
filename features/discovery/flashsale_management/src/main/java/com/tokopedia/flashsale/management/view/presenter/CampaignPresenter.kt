package com.tokopedia.flashsale.management.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flashsale.management.data.Data
import com.tokopedia.flashsale.management.domain.GetCampaignListUsecase
import com.tokopedia.flashsale.management.view.contract.CampaignContract
import rx.Subscriber
import javax.inject.Inject

class CampaignPresenter @Inject
constructor(private val getCampaignListUsecase: GetCampaignListUsecase) : BaseDaggerPresenter<CampaignContract.View>(), CampaignContract.Presenter {

    override fun getCampaignList(all: String, offset: Int, rows: Int, campaign_type: Int, q: String, status: String) {
        getCampaignListUsecase.execute(GetCampaignListUsecase.createRequestParams(all, offset, rows, campaign_type, q, status),
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