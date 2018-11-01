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
import com.tokopedia.flashsale.management.view.activity.CampaignDetailActivity
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.presenter.CampaignPresenter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusListViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import com.tokopedia.graphql.data.GraphqlClient
import javax.inject.Inject

abstract class BaseCampaignFragment : BaseSearchListFragment<CampaignViewModel, CampaignAdapterTypeFactory>() {

    @Inject
    lateinit var presenter: CampaignPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        GraphqlClient.init(context!!)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list_campaign, container, false)
    }

    override fun onItemClicked(t: CampaignViewModel) {
        activity?.let {
            startActivity(CampaignDetailActivity.createIntent(it, t.id, t.campaignUrl))
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): CampaignAdapterTypeFactory {
        return CampaignAdapterTypeFactory()
    }

    fun onSuccessGetCampaignList(data: DataCampaignList) {
        val listDummy = ArrayList<CampaignViewModel>()
        for(campaign : Campaign in data.list){
            listDummy.add(campaign.toCampaignViewModel())
        }
        renderList(listDummy)
    }

    fun onErrorGetCampaignList(throwable: Throwable) {

    }

    fun onSuccessGetCampaignLabel(data: DataCampaignLabel) {
        val campaignStatusListViewModel = CampaignStatusListViewModel()
        campaignStatusListViewModel.campaignStatusList = data.data
        adapter.addElement(0, campaignStatusListViewModel)
    }

    fun onErrorGetCampaignLabel(throwable: Throwable) {

    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    companion object {
        const val DEFAULT_PAGE = 0
        const val DEFAULT_ROWS = 5
        const val CAMPAIGN_TYPE = 1
    }
}