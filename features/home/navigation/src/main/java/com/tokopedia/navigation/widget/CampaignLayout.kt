package com.tokopedia.navigation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.pojo.Campaign

class CampaignLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val campaignContainer: LinearLayout
    private val campaignPrice: TextView
    private val campaignDiscount: TextView

    init {
        View.inflate(getContext(), R.layout.widget_campaign, this)
        campaignContainer = findViewById(R.id.ll_campaign)
        campaignPrice = findViewById(R.id.tv_campaign)
        campaignDiscount = findViewById(R.id.tv_discount)
    }

    fun setupCampaign(campaign: Campaign) {
        if (campaign.discountPercentage == 0) {
            hide()
            return
        }

        campaignPrice.text = campaign.originalPriceFormat.toString()
        campaignDiscount.text = "${campaign.discountPercentage}%"
    }
}