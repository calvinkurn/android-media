package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.ekstension.gone
import com.tokopedia.flashsale.management.ekstension.visible
import com.tokopedia.flashsale.management.tracking.FlashSaleTracking
import com.tokopedia.flashsale.management.view.activity.CampaignDetailActivity
import com.tokopedia.flashsale.management.view.adapter.CampaignInfoAdapterTypeFactory
import com.tokopedia.flashsale.management.view.presenter.CampaignDetailInfoPresenter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoHeaderViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoViewModel
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_flash_sale_info.*
import javax.inject.Inject

class FlashSaleInfoFragment: BaseListFragment<CampaignInfoViewModel, CampaignInfoAdapterTypeFactory>() {
    lateinit var campaignUrl: String
    lateinit var campaignId: String
    lateinit var sellerStatus: SellerStatus
    lateinit var campaignType: String
    var flashSaleTracking: FlashSaleTracking? = null

    @Inject
    lateinit var presenter: CampaignDetailInfoPresenter

    override fun getAdapterTypeFactory() = CampaignInfoAdapterTypeFactory(sellerStatus){
        activity?.let {
            if (it is CampaignDetailActivity){
                it.moveToTabProduct()
            }
        }
    }

    override fun onItemClicked(t: CampaignInfoViewModel?) {}

    override fun loadData(page: Int) {
        presenter.getCampaignInfo(GraphqlHelper.loadRawString(resources, R.raw.gql_campaign_info),
                campaignUrl, ::onSuccessGetCampaignInfo, ::onErroGetCampaignInfo)
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let {
            GraphqlClient.init(it)
        }
        arguments?.let {
            campaignId = it.getLong(EXTRA_PARAM_CAMPAIGN_ID).toString()
            campaignUrl = it.getString(EXTRA_PARAM_CAMPAIGN_URL, "") ?: ""
            campaignType = it.getString(EXTRA_PARAM_CAMPAIGN_TYPE, "") ?: ""
            sellerStatus = it.getParcelable(EXTRA_PARAM_SELLER_STATUS) ?: SellerStatus()
        }
        super.onCreate(savedInstanceState)
        flashSaleTracking = FlashSaleTracking(activity?.application as AbstractionRouter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flash_sale_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::sellerStatus.isInitialized && sellerStatus.tickerMessage.isNotBlank()) {
            ticker_view.setListMessage(arrayListOf(sellerStatus.tickerMessage))
            ticker_view.buildView()
            ticker_view.visible()
        } else {
            ticker_view.gone()
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    private fun onSuccessGetCampaignInfo(items: List<CampaignInfoViewModel>){
        if (items.get(0) is CampaignInfoHeaderViewModel){
            (items.get(0) as CampaignInfoHeaderViewModel).campaign.campaignType = campaignType
        }
        super.renderList(items)
    }

    private fun onErroGetCampaignInfo(throwable: Throwable){}

    companion object {
        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
        private const val EXTRA_PARAM_CAMPAIGN_URL = "campaign_url"
        private const val EXTRA_PARAM_CAMPAIGN_TYPE = "campaign_type"
        private const val EXTRA_PARAM_SELLER_STATUS = "seller_status"

        fun createInstance(campaignId: Long,campaignUrl: String, sellerStatus: SellerStatus,
                           campaignType: String?) = FlashSaleInfoFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
                putString(EXTRA_PARAM_CAMPAIGN_URL, campaignUrl)
                putParcelable(EXTRA_PARAM_SELLER_STATUS, sellerStatus)
                putString(EXTRA_PARAM_CAMPAIGN_TYPE, campaignType)
            }
        }
    }
}