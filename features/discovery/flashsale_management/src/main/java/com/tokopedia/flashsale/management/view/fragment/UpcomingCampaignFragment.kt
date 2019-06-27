package com.tokopedia.flashsale.management.view.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.view.activity.FlashsaleWebViewActivity

class UpcomingCampaignFragment : BaseCampaignFragment(){

    override fun loadData(page: Int) {
        val offset = (page-1)* DEFAULT_ROWS
        presenter.getCampaignList(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_list),
                CAMPAIGN_LIST_TYPE, offset, DEFAULT_ROWS, CAMPAIGN_TYPE, searchInputView.searchText,
                STATUS_IN_SUBMISSION,::onSuccessGetCampaignList, ::onErrorGetCampaignList)
    }

    override fun onSearchTextChanged(text: String) {
    }

    override fun onSearchSubmitted(text: String) {
        loadInitialData()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val searchText = searchInputView.searchText
        return if (searchText.isEmpty()) {
            hideSearchInputView()
            EmptyModel().apply {
                title = getString(R.string.fm_campaign_list_upcoming_empty_title)
                description = getString(R.string.fm_campaign_list_upcoming_empty_content)
                iconRes = R.drawable.ic_empty_box
                callback = object : BaseEmptyViewHolder.Callback{
                    override fun onEmptyContentItemTextClicked() {
                        activity?.let {
                            startActivity(FlashsaleWebViewActivity.createIntent(it, FlashSaleConstant.FLASHSALE_ABOUT_URL))
                        }
                    }

                    override fun onEmptyButtonClicked() {
                        // no op
                    }
                }
            }
        } else {
            EmptyModel().apply {
                title = getString(R.string.fm_campaign_list_search_empty_title)
                description = getString(R.string.fm_campaign_list_search_empty_content)
                iconRes = R.drawable.ic_empty_search
                callback = object : BaseEmptyViewHolder.Callback{
                    override fun onEmptyContentItemTextClicked() {
                        searchInputView.searchText = ""
                        loadInitialData()
                    }

                    override fun onEmptyButtonClicked() {
                        searchInputView.searchText = ""
                        loadInitialData()
                    }

                }
            }
        }
    }

    companion object {
        fun createInstance() = UpcomingCampaignFragment()
        const val CAMPAIGN_LIST_TYPE = true
        private const val STATUS_IN_SUBMISSION = "4"
    }
}