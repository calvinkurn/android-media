package com.tokopedia.flashsale.management.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.seller_status.SellerStatus
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.di.DaggerCampaignComponent
import com.tokopedia.flashsale.management.view.adapter.CampaignDetailFragmentPagerAdapter
import com.tokopedia.flashsale.management.view.presenter.CampaignInfoPresenter
import kotlinx.android.synthetic.main.activity_campaign_detail.*
import javax.inject.Inject

class CampaignDetailActivity: BaseSimpleActivity(), HasComponent<CampaignComponent> {
    override fun getNewFragment(): Fragment? = null

    private var campaignUrl: String = ""
    private var campaignId: Long = -1
    @Inject lateinit var presenter: CampaignInfoPresenter

    val titles by lazy {
        mutableListOf(getString(R.string.label_flash_sale_info))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        loadSellerStatus()
    }

    private fun loadSellerStatus(){
        presenter.getSellerStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_get_seller_status),
                campaignUrl, this::onSuccessGetSellerStatus, this::onErrorGetSellerStatus)
    }

    fun onSuccessGetSellerStatus(sellerStatus: SellerStatus){
        if (titles.size > 1) titles.removeAt(1)
        if (sellerStatus.isEligible){
            titles.add(getString(R.string.label_product_list))
        }
        val adapter = CampaignDetailFragmentPagerAdapter(supportFragmentManager, titles,
                campaignId, campaignUrl, sellerStatus)

        pager.adapter = adapter
        pager.currentItem = if (sellerStatus.joinStatus && titles.size == 2) TAB_POS_MY_PRODUCT else TAB_POS_CAMPAIGN_INFO
    }

    fun onErrorGetSellerStatus(throwable: Throwable){
        NetworkErrorHelper.showEmptyState(this, vgContent,
                ErrorHandler.getErrorMessage(this, throwable)) {
            loadSellerStatus()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun getComponent() = DaggerCampaignComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun setupLayout(savedInstanceState: Bundle?) {
        campaignId = intent.getLongExtra(EXTRA_PARAM_CAMPAIGN_ID, -1)
        campaignUrl = intent.getStringExtra(EXTRA_PARAM_CAMPAIGN_URL) ?: ""
        super.setupLayout(savedInstanceState)
        indicator.tabMode = TabLayout.MODE_SCROLLABLE
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(indicator))
        indicator.setupWithViewPager(pager)
    }

    override fun getLayoutRes(): Int  = R.layout.activity_campaign_detail

    companion object {
        @JvmStatic
        fun createIntent(context: Context, campaignId: Long, campaignUrl: String) =
                Intent(context, CampaignDetailActivity::class.java)
                        .putExtra(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
                        .putExtra(EXTRA_PARAM_CAMPAIGN_URL, campaignUrl)


        private const val TAB_POS_CAMPAIGN_INFO = 0
        private const val TAB_POS_MY_PRODUCT = 1
        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
        private const val EXTRA_PARAM_CAMPAIGN_URL = "campaign_url"
    }
}