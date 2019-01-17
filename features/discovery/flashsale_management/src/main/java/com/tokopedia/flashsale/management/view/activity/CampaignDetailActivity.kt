package com.tokopedia.flashsale.management.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.di.DaggerCampaignComponent
import com.tokopedia.flashsale.management.tracking.FlashSaleTracking
import com.tokopedia.flashsale.management.view.adapter.CampaignDetailFragmentPagerAdapter
import com.tokopedia.flashsale.management.view.presenter.CampaignInfoPresenter
import kotlinx.android.synthetic.main.activity_campaign_detail.*
import javax.inject.Inject

class CampaignDetailActivity: BaseSimpleActivity(), HasComponent<CampaignComponent> {
    override fun getNewFragment(): Fragment? = null

    private var campaignUrl: String = ""
    private var campaignName: String? = null
    private var campaignId: Long = -1
    private var campaignType: String? = null
    private var flashSaleTracking: FlashSaleTracking? = null
    private var tabProductUseButton: Boolean = false
    @Inject lateinit var presenter: CampaignInfoPresenter

    val titles by lazy {
        mutableListOf(getString(R.string.label_flash_sale_info))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        flashSaleTracking = FlashSaleTracking(application as AbstractionRouter)
        loadSellerStatus()
        campaignName?.let {
            title = it
            supportActionBar?.setTitle(it)
        }
    }

    private fun loadSellerStatus(){
        presenter.getSellerStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_get_seller_status),
                campaignUrl, this::onSuccessGetSellerStatus, this::onErrorGetSellerStatus)
    }

    fun moveToTabProduct(){
        this.tabProductUseButton = true
        pager.currentItem = TAB_POS_MY_PRODUCT
    }

    fun onSuccessGetSellerStatus(sellerStatus: SellerStatus){
        if (titles.size > 1) titles.removeAt(1)
        if (sellerStatus.isEligible){
            titles.add(getString(R.string.label_product_list))
        }
        val adapter = CampaignDetailFragmentPagerAdapter(supportFragmentManager, titles,
                campaignId, campaignUrl, campaignType, sellerStatus)

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
        campaignType = intent.getStringExtra(EXTRA_PARAM_CAMPAIGN_TYPE)
        campaignName = intent.getStringExtra(EXTRA_PARAM_CAMPAIGN_NAME)
        super.setupLayout(savedInstanceState)
        indicator.tabMode = TabLayout.MODE_SCROLLABLE
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                // position 0 is always Info Fragment
                if (position == 0) {
                    flashSaleTracking?.clickTabInfo(campaignId.toString())
                } else {
                    if (tabProductUseButton) {
                        flashSaleTracking?.clickInfoToProduct(campaignId.toString())
                        tabProductUseButton = false
                    } else {
                        flashSaleTracking?.clickTabProduct(campaignId.toString())
                    }
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        })
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(indicator))
        indicator.setupWithViewPager(pager)
    }

    override fun getLayoutRes(): Int  = R.layout.activity_campaign_detail

    companion object {
        @JvmStatic
        fun createIntent(context: Context, campaignId: Long, campaignUrl: String, campaignType: String?,
                         campaignName:String? = null) =
                Intent(context, CampaignDetailActivity::class.java)
                        .putExtra(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
                        .putExtra(EXTRA_PARAM_CAMPAIGN_URL, campaignUrl)
                        .putExtra(EXTRA_PARAM_CAMPAIGN_TYPE, campaignType)
                        .putExtra(EXTRA_PARAM_CAMPAIGN_NAME, campaignName)


        private const val TAB_POS_CAMPAIGN_INFO = 0
        private const val TAB_POS_MY_PRODUCT = 1
        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
        private const val EXTRA_PARAM_CAMPAIGN_URL = "campaign_url"
        private const val EXTRA_PARAM_CAMPAIGN_TYPE = "campaign_type"
        private const val EXTRA_PARAM_CAMPAIGN_NAME = "campaign_name"
    }
}