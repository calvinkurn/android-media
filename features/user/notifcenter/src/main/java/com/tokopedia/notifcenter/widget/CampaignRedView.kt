package com.tokopedia.notifcenter.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.Campaign
import com.tokopedia.unifycomponents.Label

class CampaignRedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val campaignContainer: LinearLayout
    private val campaignPrice: TextView
    private val campaignDiscount: Label

    init {
        View.inflate(getContext(), R.layout.widget_campaign_price_drop, this)
        campaignContainer = findViewById(R.id.container)
        campaignPrice = findViewById(R.id.txt_price_campaign)
        campaignDiscount = findViewById(R.id.txt_discount)
        campaignPrice.paintFlags = campaignPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    @SuppressLint("SetTextI18n")
    fun setCampaign(campaign: Campaign?) {
        if (campaign != null && campaign.discountPercentage != 0) {
            show()
            campaignPrice.text = campaign.originalPriceFormat
            campaignDiscount.text = "${campaign.discountPercentage}%"
        } else {
            hide()
        }
    }

}