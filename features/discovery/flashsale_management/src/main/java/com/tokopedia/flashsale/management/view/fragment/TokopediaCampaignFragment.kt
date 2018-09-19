package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel

class TokopediaCampaignFragment : BaseSearchListFragment<CampaignViewModel, CampaignAdapterTypeFactory>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    }

    override fun getAdapterTypeFactory(): CampaignAdapterTypeFactory {
        return CampaignAdapterTypeFactory()
    }

    override fun loadData(page: Int) {
        val listDummy = ArrayList<CampaignViewModel>()
        val campaignViewModel =  CampaignViewModel()
        listDummy.add(campaignViewModel)
        listDummy.add(campaignViewModel)
        listDummy.add(campaignViewModel)
        listDummy.add(campaignViewModel)
        listDummy.add(campaignViewModel)
        listDummy.add(campaignViewModel)
        listDummy.add(campaignViewModel)
        renderList(listDummy)
    }

    override fun onSearchSubmitted(text: String?) {

    }

    override fun onSearchTextChanged(text: String?) {

    }

    companion object {
        fun createInstance() = TokopediaCampaignFragment()
    }
}