package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.Campaign
import com.tokopedia.flashsale.management.data.Data
import com.tokopedia.flashsale.management.data.SellerInfo
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.contract.CampaignContract
import com.tokopedia.flashsale.management.view.presenter.CampaignPresenter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import com.tokopedia.graphql.data.GraphqlClient
import javax.inject.Inject

abstract class BaseCampaignFragment : BaseSearchListFragment<CampaignViewModel, CampaignAdapterTypeFactory>(), CampaignContract.View{

    @Inject
    lateinit var presenter: CampaignPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        GraphqlClient.init(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list_campaign, container, false)
    }

    override fun onItemClicked(t: CampaignViewModel?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun getAdapterTypeFactory(): CampaignAdapterTypeFactory {
        return CampaignAdapterTypeFactory()
    }

    override fun loadData(page: Int) {
        presenter.getCampaignList()
    }

    override fun onSearchTextChanged(text: String?) {

    }

    override fun onSearchSubmitted(text: String?) {
        adapter.clearAllElements()
        adapter.notifyDataSetChanged()
        presenter.getCampaignList()
    }

    override fun onSuccessGetCampaignList(data: Data) {
        val listDummy = ArrayList<CampaignViewModel>()
        for(campaign : Campaign in data.list){
            val campaignViewModel = CampaignViewModel()
            campaignViewModel.campaign_id = campaign.campaign_id
            campaignViewModel.campaign_id = campaign.campaign_id
            campaignViewModel.name = campaign.name
            campaignViewModel.campaign_period = campaign.campaign_period
            campaignViewModel.submission_start_date = campaign.submission_start_date
            campaignViewModel.submission_end_date = campaign.submission_end_date
            campaignViewModel.status = campaign.status
            campaignViewModel.campaign_type = campaign.campaign_type
            campaignViewModel.cover = campaign.cover
            campaignViewModel.is_joined = campaign.is_joined
            campaignViewModel.dashboard_url = campaign.dashboard_url
            campaignViewModel.product_number = campaign.product_number
            campaignViewModel.seller_number = campaign.seller_number
            campaignViewModel.seller_info = campaign.seller_info
            listDummy.add(campaignViewModel)
        }
        renderList(listDummy)
    }

    override fun onErrorGetCampaignList(throwable: Throwable) {

    }
}