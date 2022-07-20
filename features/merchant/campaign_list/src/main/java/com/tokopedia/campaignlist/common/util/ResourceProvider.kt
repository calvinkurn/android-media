package com.tokopedia.campaignlist.common.util

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaignlist.R
import javax.inject.Inject

class ResourceProvider @Inject constructor(@ApplicationContext val context: Context?) {

    private fun getString(resId: Int): String? {
        return try {
            context?.getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getShareTitle(): String? {
        return getString(R.string.campaign_list_share_bottom_sheet_title)
    }

    fun getShareCampaignDescriptionWording(): String? {
        return getString(R.string.campaign_list_share_text)
    }

    fun getShareOngoingCampaignDescriptionWording(): String? {
        return getString(R.string.campaign_list_ongoing_share_text)
    }

    fun getShareOgTitle(): String? {
        return getString(R.string.campaign_list_share_title_og)
    }

    fun getShareDescription(): String? {
        return getString(R.string.campaign_list_share_desc_og)
    }

    fun getShareOngoingDescription(): String? {
        return getString(R.string.campaign_list_ongoing_share_desc_og)
    }
}