package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.data.campaignlist.DataCampaignList
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.ekstension.toCampaignViewModel
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignStatusListViewHolder
import com.tokopedia.flashsale.management.view.contract.CampaignContract
import com.tokopedia.flashsale.management.view.presenter.CampaignPresenter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusListViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusViewModel
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

    override fun onSuccessGetCampaignList(data: DataCampaignList) {
        val listDummy = ArrayList<CampaignViewModel>()
        for(campaign : Campaign in data.list){
            listDummy.add(campaign.toCampaignViewModel())
        }
        renderList(listDummy)
    }

    override fun onErrorGetCampaignList(throwable: Throwable) {

    }

    override fun onSuccessGetCampaignLabel(data: DataCampaignLabel) {
        val campaignStatusListViewModel = CampaignStatusListViewModel()
        campaignStatusListViewModel.campaignStatusList = data.data
        adapter.addElement(0, campaignStatusListViewModel)
    }

    override fun onErrorGetCampaignLabel(throwable: Throwable) {

    }

    companion object {
        const val DEFAULT_PAGE = 0
        const val DEFAULT_ROWS = 5
        const val CAMPAIGN_TYPE = 1
    }
}