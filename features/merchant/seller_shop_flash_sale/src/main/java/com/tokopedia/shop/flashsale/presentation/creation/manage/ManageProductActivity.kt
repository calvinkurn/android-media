package com.tokopedia.shop.flashsale.presentation.creation.manage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode

class ManageProductActivity : BaseSimpleActivity() {

    companion object {
        const val BUNDLE_KEY_CAMPAIGN_ID = "campaignId"

        @JvmStatic
        fun start(context: Context, campaignId: String) {
            val intent = Intent(context, ManageProductActivity::class.java).apply {
                val extras = Bundle().apply {
                    putExtra(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                }
                putExtras(extras)
            }
            context.startActivity(intent)
        }
    }

    private val campaignId by lazy {
        intent?.getStringExtra(BUNDLE_KEY_CAMPAIGN_ID).orEmpty()
    }

    override fun getLayoutRes(): Int = R.layout.ssfs_activity_manage_product

    override fun getParentViewResourceID() = R.id.container

    override fun getNewFragment(): Fragment = ManageProductFragment.newInstance(campaignId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ssfs_activity_manage_product)
    }
}