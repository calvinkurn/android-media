package com.tokopedia.flashsale.management.view.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusViewModel

class UpcomingCampaignFragment : BaseCampaignFragment(){

    override fun loadData(page: Int) {
        presenter.getCampaignList(CAMPAIGN_LIST_TYPE, page, DEFAULT_ROWS, CAMPAIGN_TYPE, "", "1,2,3")
    }

    override fun onSearchTextChanged(text: String) {

    }

    override fun onSearchSubmitted(text: String) {
        adapter.clearAllElements()
        adapter.notifyDataSetChanged()
        presenter.getCampaignList(CAMPAIGN_LIST_TYPE, DEFAULT_PAGE, DEFAULT_ROWS, CAMPAIGN_TYPE, text, "1,2,3")
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val searchText = searchInputView.searchText
        return if (searchText.isEmpty()) {
            hideSearchInputView()
            EmptyModel().apply {
                title = getString(R.string.fm_campaign_list_upcoming_empty_title)
                description = getString(R.string.fm_campaign_list_upcoming_empty_content)
                iconRes = R.drawable.ic_empty_box
            }
        } else {
            EmptyModel().apply {
                title = getString(R.string.fm_campaign_list_search_empty_title)
                description = getString(R.string.fm_campaign_list_search_empty_content)
                iconRes = R.drawable.ic_empty_search
            }
        }
    }

    companion object {
        fun createInstance() = UpcomingCampaignFragment()
        const val CAMPAIGN_LIST_TYPE = "true"
    }
}