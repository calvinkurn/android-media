package com.tokopedia.flashsale.management.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flashsale.management.data.campaign_label.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaign_list.DataCampaignList
import com.tokopedia.flashsale.management.domain.GetCampaignLabelUsecase
import com.tokopedia.flashsale.management.domain.GetCampaignListUsecase
import com.tokopedia.flashsale.management.view.contract.CampaignContract
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class CampaignPresenter @Inject
constructor(private val getCampaignListUsecase: GetCampaignListUsecase,
            private val getCampaignLabelUsecase: GetCampaignLabelUsecase) : BaseDaggerPresenter<CampaignContract.View>(), CampaignContract.Presenter {

    override fun getCampaignList(all: String, offset: Int, rows: Int, campaign_type: Int, q: String, status: String) {
        getCampaignListUsecase.execute(GetCampaignListUsecase.createRequestParams(all, offset, rows, campaign_type, q, status),
                object : Subscriber<DataCampaignList>() {
                    override fun onCompleted() {}
                    override fun onError(throwable: Throwable) {
                        view.onErrorGetCampaignList(throwable)
                    }
                    override fun onNext(data: DataCampaignList) {
                        view.onSuccessGetCampaignList(data)
                    }
                })
    }

    override fun getCampaignLabel() {
        getCampaignLabelUsecase.execute(RequestParams.EMPTY,
                object : Subscriber<DataCampaignLabel>() {
                    override fun onCompleted() {}
                    override fun onError(throwable: Throwable) {
                        view.onErrorGetCampaignLabel(throwable)
                    }
                    override fun onNext(data: DataCampaignLabel) {
                        view.onSuccessGetCampaignLabel(data)
                    }
                })
    }

    override fun detachView() {
        getCampaignListUsecase.unsubscribe()
        super.detachView()
    }
}