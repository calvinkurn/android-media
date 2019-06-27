package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.ekstension.gone
import com.tokopedia.flashsale.management.ekstension.visible
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.adapter.CampaignStatusListAdapter
import com.tokopedia.flashsale.management.view.viewmodel.EmptyMyCampaignViewModel
import kotlinx.android.synthetic.main.fragment_list_campaign.*

class MyCampaignFragment : BaseCampaignFragment(){
    private var selectedStatusIds = ""

    private val campaignStatusListAdapter: CampaignStatusListAdapter by lazy { CampaignStatusListAdapter{
        selectedStatusIds = it
        loadInitialData()
    }}

    override fun loadData(page: Int) {
        val offset = (page-1)* DEFAULT_ROWS
        presenter.getCampaignList(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_list),
                CAMPAIGN_LIST_TYPE, offset, DEFAULT_ROWS, CAMPAIGN_TYPE, searchInputView.searchText, selectedStatusIds,
                this::onSuccessGetCampaignList, this::onErrorGetCampaignList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chips.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        val dividerDrawable = context?.let {  ContextCompat.getDrawable(it, R.drawable.horizontal_divider_8dp) }
        dividerDrawable?.let { itemDecoration.setDrawable(it)
            chips.addItemDecoration(itemDecoration)}
        chips.adapter = campaignStatusListAdapter

        showFilterInput()

        presenter.getCampaignLabel(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_label),
                this::onSuccessGetCampaignLabel,this::onErrorGetCampaignLabel)
    }

    override fun getAdapterTypeFactory()= CampaignAdapterTypeFactory()

    override fun onSearchTextChanged(text: String) {

    }

    override fun onSearchSubmitted(text: String) {
        loadInitialData()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val searchText = searchInputView.searchText
        return if (searchText.isEmpty() && selectedStatusIds.isEmpty()) {
            hideSearchInputView()
            hideFilterInput()
            EmptyMyCampaignViewModel()
        } else {
            if (selectedStatusIds.isEmpty()){
                showSearchInputView()
            } else {
                hideSearchInputView()
            }
            showFilterInput()
            EmptyModel().apply {
                title = getString(R.string.fm_campaign_list_search_empty_title)
                description = getString(R.string.fm_campaign_list_search_empty_content)
                iconRes = R.drawable.ic_empty_search
                callback = object : BaseEmptyViewHolder.Callback{
                    override fun onEmptyContentItemTextClicked() {
                        searchInputView.searchText = ""
                        selectedStatusIds = ""
                        campaignStatusListAdapter.resetFilter()
                        loadInitialData()
                    }

                    override fun onEmptyButtonClicked() {
                        searchInputView.searchText = ""
                        selectedStatusIds = ""
                        campaignStatusListAdapter.resetFilter()
                        loadInitialData()
                    }

                }
            }
        }
    }

    private fun hideFilterInput() {
        chips.gone()
    }

    private fun showFilterInput(){
        chips.visible()
    }

    override fun onRetryClicked() {
        super.onRetryClicked()
        presenter.getCampaignLabel(GraphqlHelper.loadRawString(resources, R.raw.gql_get_campaign_label),
                this::onSuccessGetCampaignLabel,this::onErrorGetCampaignLabel)
    }

    override fun onSuccessGetCampaignLabel(data: DataCampaignLabel) {
        showSearchInputView()
        campaignStatusListAdapter.replaceData(data.data)
    }

    companion object {
        fun createInstance() = MyCampaignFragment()
        const val CAMPAIGN_LIST_TYPE = false
    }
}