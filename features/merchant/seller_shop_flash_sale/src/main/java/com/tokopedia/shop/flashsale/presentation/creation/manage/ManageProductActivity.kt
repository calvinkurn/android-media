package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class ManageProductActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaignId"
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"

        @JvmStatic
        fun start(context: Context, campaignId: Long, pageMode: PageMode) {
            val intent = Intent(context, ManageProductActivity::class.java).apply {
                val extras = Bundle().apply {
                    putLong(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                    putParcelable(BUNDLE_KEY_PAGE_MODE, pageMode)
                }
                putExtras(extras)
            }
            context.startActivity(intent)
        }
    }

    private val campaignId by lazy {
        intent?.extras?.getLong(BUNDLE_KEY_CAMPAIGN_ID).orZero()
    }

    private val pageMode by lazy {
        intent?.extras?.getParcelable(BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }

    override fun getLayoutRes(): Int = R.layout.ssfs_activity_manage_product

    override fun getParentViewResourceID() = R.id.container

    override fun getNewFragment(): Fragment = ManageProductFragment.newInstance(campaignId, pageMode)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ssfs_activity_manage_product)
    }
}