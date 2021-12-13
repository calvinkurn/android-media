package com.tokopedia.campaignlist.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaignlist.R
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(@ApplicationContext val context: Context?) :
    ResourceProvider {

    override fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    override fun getShareTitle(): String? {
        return getString(R.string.campaign_list_share_bottom_sheet_title)
    }

    override fun getShareCampaignDescriptionWording(): String? {
        return getString(R.string.campaign_list_share_text)
    }

    override fun getShareOngoingCampaignDescriptionWording(): String? {
        return getString(R.string.campaign_list_ongoing_share_text)
    }

    override fun getShareOgTitle(): String? {
        return getString(R.string.campaign_list_share_title_og)
    }

    override fun getShareOgDescription(): String? {
        return getString(R.string.campaign_list_share_desc_og)
    }

    override fun getShareOngoingOgDescription(): String? {
        return getString(R.string.campaign_list_ongoing_share_desc_og)
    }
}