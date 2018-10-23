package com.tokopedia.flashsale.management.view.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.ekstension.convertIdtoCommaString
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.adapter.viewholder.CampaignStatusListViewHolder
import com.tokopedia.flashsale.management.view.contract.CampaignContract
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusViewModel
import com.tokopedia.flashsale.management.view.viewmodel.EmptyMyCampaignViewModel

class MyCampaignFragment : BaseCampaignFragment(), CampaignStatusListViewHolder.OnCampaignStatusListViewHolderListener{

    override fun loadInitialData() {
        super.loadInitialData()
        reloadCampaignData()
        presenter.getCampaignLabel(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_label),
                {onSuccessGetCampaignLabel(it)},{onErrorGetCampaignLabel(it)})
    }

    override fun loadData(page: Int) {
        presenter.getCampaignList(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_list),
                CAMPAIGN_LIST_TYPE, page, DEFAULT_ROWS, CAMPAIGN_TYPE, "", "1,2,3",
                {onSuccessGetCampaignList(it)}, {onErrorGetCampaignList(it)})
    }

    override fun getAdapterTypeFactory(): CampaignAdapterTypeFactory {
        return CampaignAdapterTypeFactory(this)
    }

    private fun reloadCampaignData() {
        showLoading()
        loadData(defaultInitialPage)
    }

    override fun onSearchTextChanged(text: String) {

    }

    override fun onSearchSubmitted(text: String) {
        adapter.clearAllElements()
        adapter.notifyDataSetChanged()
        presenter.getCampaignList(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_list),
                CAMPAIGN_LIST_TYPE, DEFAULT_PAGE, DEFAULT_ROWS, CAMPAIGN_TYPE, text, "1,2,3",
                {onSuccessGetCampaignList(it)}, {onErrorGetCampaignList(it)})
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val searchText = searchInputView.searchText
        return if (searchText.isEmpty()) {
            hideSearchInputView()
            EmptyMyCampaignViewModel()
        } else {
            EmptyModel().apply {
                title = getString(R.string.fm_campaign_list_search_empty_title)
                description = getString(R.string.fm_campaign_list_search_empty_content)
                iconRes = R.drawable.ic_empty_search
            }
        }
    }

    override fun onCampaignStatusClicked(campaignStatusViewModel: CampaignStatusViewModel) {
        presenter.getCampaignList(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_list),
                CAMPAIGN_LIST_TYPE, DEFAULT_PAGE, DEFAULT_ROWS, CAMPAIGN_TYPE, searchInputView.searchText,
                campaignStatusViewModel.convertIdtoCommaString(),
                {onSuccessGetCampaignList(it)}, {onErrorGetCampaignList(it)})
    }

    companion object {
        fun createInstance() = MyCampaignFragment()
        const val CAMPAIGN_LIST_TYPE = "false"
    }
}