package com.tokopedia.shop.flashsale.presentation.creation.highlight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class ManageHighlightedProductActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"

        @JvmStatic
        fun start(context: Context, campaignId: Long, pageMode: PageMode) {
            val starter = Intent(context, ManageHighlightedProductActivity::class.java)

            val bundle = Bundle()
            bundle.putLong(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            bundle.putParcelable(BUNDLE_KEY_PAGE_MODE, pageMode)
            starter.putExtras(bundle)

            context.startActivity(starter)
        }
    }
    private val campaignId by lazy {
        intent?.extras?.getLong(BUNDLE_KEY_CAMPAIGN_ID).orZero()
    }


    private val pageMode by lazy {
        intent?.extras?.getParcelable(BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_manage_highlighted_product
    override fun getNewFragment() = ManageHighlightedProductFragment.newInstance(campaignId, pageMode)
    override fun getParentViewResourceID() = R.id.container
}