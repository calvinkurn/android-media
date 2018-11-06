package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.seller_status.SellerStatus
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.view.adapter.CampaignInfoAdapterTypeFactory
import com.tokopedia.flashsale.management.view.presenter.CampaignDetailInfoPresenter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoViewModel
import com.tokopedia.graphql.data.GraphqlClient
import javax.inject.Inject

class FlashSaleInfoFragment: BaseListFragment<CampaignInfoViewModel, CampaignInfoAdapterTypeFactory>() {
    lateinit var campaignUrl: String
    lateinit var sellerStatus: SellerStatus

    @Inject
    lateinit var presenter: CampaignDetailInfoPresenter

    override fun getAdapterTypeFactory() = CampaignInfoAdapterTypeFactory()

    override fun onItemClicked(t: CampaignInfoViewModel?) {}

    override fun loadData(page: Int) {
        presenter.getCampaignInfo(GraphqlHelper.loadRawString(resources, R.raw.gql_campaign_info),
                campaignUrl, {onSuccessGetCampaignInfo(it)}){ onErroGetCampaignInfo(it) }
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
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flash_sale_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            campaignUrl = it.getString(EXTRA_PARAM_CAMPAIGN_URL, "") ?: ""
            sellerStatus = it.getParcelable(EXTRA_PARAM_SELLER_STATUS) ?: SellerStatus()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    private fun onSuccessGetCampaignInfo(items: List<CampaignInfoViewModel>){
        super.renderList(items)
    }

    private fun onErroGetCampaignInfo(throwable: Throwable){}

    companion object {
        private const val EXTRA_PARAM_CAMPAIGN_URL = "campaign_url"
        private const val EXTRA_PARAM_SELLER_STATUS = "seller_status"

        fun createInstance(campaignUrl: String, sellerStatus: SellerStatus) = FlashSaleInfoFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_PARAM_CAMPAIGN_URL, campaignUrl)
                putParcelable(EXTRA_PARAM_SELLER_STATUS, sellerStatus)
            }
        }
    }
}