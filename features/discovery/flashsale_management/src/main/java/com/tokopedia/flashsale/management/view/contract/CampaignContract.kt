package com.tokopedia.flashsale.management.view.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.flashsale.management.data.campaign_label.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaign_list.DataCampaignList

interface CampaignContract{

    interface View : CustomerView {

        fun onSuccessGetCampaignList(data: DataCampaignList)

        fun onErrorGetCampaignList(throwable: Throwable)

        fun onSuccessGetCampaignLabel(data: DataCampaignLabel)

        fun onErrorGetCampaignLabel(throwable: Throwable)

    }

    interface Presenter : CustomerPresenter<View> {

        fun getCampaignList(all: String, offset: Int, rows: Int, campaign_type: Int, q: String, status: String)

        fun getCampaignLabel()

    }

}