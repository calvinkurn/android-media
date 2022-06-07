package com.tokopedia.shop.flashsale.common.resource

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject
import com.tokopedia.seller_shop_flash_sale.R

class ResourceProvider @Inject constructor(@ApplicationContext  private val context: Context) {

    fun getUpcomingCampaignWording(): String {
        return context.getString(R.string.sfs_share_title_upcoming)
    }

    fun getOngoingCampaignWording() : String {
        return context.getString(R.string.sfs_share_title_ongoing)
    }

    fun getOutgoingTitleWording(): String {
        return context.getString(R.string.sfs_share_outgoing_title)
    }

    fun getOutgoingUpcomingCampaignWording(): String {
        return context.getString(R.string.sfs_share_outgoing_description_upcoming)
    }

    fun getOutgoingOngoingCampaignWording() : String {
        return context.getString(R.string.sfs_share_outgoing_description_ongoing)
    }
}