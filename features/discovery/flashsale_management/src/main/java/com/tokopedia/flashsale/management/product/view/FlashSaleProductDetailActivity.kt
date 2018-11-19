package com.tokopedia.flashsale.management.product.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.di.DaggerCampaignComponent
import com.tokopedia.flashsale.management.product.data.FlashSalePostProductItem
import com.tokopedia.flashsale.management.product.data.FlashSaleProductItem
import com.tokopedia.flashsale.management.product.data.FlashSaleSubmissionProductItem

class FlashSaleProductDetailActivity : BaseSimpleActivity(), HasComponent<CampaignComponent>,
        FlashSaleProductDetailFragment.OnFlashSaleProductDetailFragmentListener {
    var flashSaleProductItem: FlashSaleProductItem? = null

    override fun getProduct(): FlashSaleProductItem {
        return flashSaleProductItem ?: FlashSaleSubmissionProductItem()
    }

    override fun getComponent() = DaggerCampaignComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getNewFragment(): Fragment {
        return FlashSaleProductDetailFragment.createInstance(
                intent.getIntExtra(EXTRA_PARAM_CAMPAIGN_ID, 0),
                intent.getBooleanExtra(EXTRA_CAN_SUBMIT, false))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        flashSaleProductItem = intent.extras.getParcelable(EXTRA_PARAM_PRODUCT)
        super.onCreate(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context,
                         campaignId: Int,
                         flashSaleProductItem: FlashSaleProductItem,
                         canEdit:Boolean): Intent {
            return Intent(context, FlashSaleProductDetailActivity::class.java)
                    .putExtra(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
                    .putExtra(EXTRA_PARAM_PRODUCT, flashSaleProductItem)
                    .putExtra(EXTRA_CAN_SUBMIT, canEdit)
        }

        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
        private const val EXTRA_PARAM_PRODUCT = "product"
        private const val EXTRA_CAN_SUBMIT = "can_edit"
    }
}