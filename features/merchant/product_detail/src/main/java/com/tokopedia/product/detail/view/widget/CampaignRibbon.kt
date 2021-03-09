package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductNotifyMeViewHolder
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.widget_campaign_ribbon_layout.view.*
import kotlinx.android.synthetic.main.widget_campaign_ribbon_type_1_layout.view.*
import kotlinx.android.synthetic.main.widget_campaign_ribbon_type_2_layout.view.*
import kotlinx.android.synthetic.main.widget_campaign_ribbon_type_3_layout.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class CampaignRibbon @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {

        @IntDef(ONGOING, UPCOMING)
        @Retention(AnnotationRetention.SOURCE)
        annotation class CampaignPeriod

        const val ONGOING = 0
        const val UPCOMING = 1

        // campaign types
        const val FLASH_SALE = 1
        const val SLASH_PRICE = 2
        const val NPL = 3
        const val NEW_USER = 4
        const val THEMATIC_CAMPAIGN = 5
    }

    private var listener: DynamicProductDetailListener? = null
    private var trackDataModel: ComponentTrackDataModel? = null

    init {
        addView(LayoutInflater.from(context).inflate(
                R.layout.widget_campaign_ribbon_layout,
                this,
                false))
    }

    fun setDynamicProductDetailListener(listener: DynamicProductDetailListener) {
        this.listener = listener
    }

    fun setComponentTrackDataModel(trackDataModel: ComponentTrackDataModel) {
        this.trackDataModel = trackDataModel
    }

    fun renderOnGoingCampaign(onGoingData: ProductContentMainData) {
        when (onGoingData.campaign.campaignIdentifier) {
            FLASH_SALE -> renderFlashSaleCampaignRibbon(campaignPeriod = ONGOING, onGoingData = onGoingData)
            SLASH_PRICE -> renderSlashPriceCampaignRibbon(campaign = onGoingData.campaign)
            NPL -> renderNplCampaignRibbon(campaignPeriod = ONGOING, onGoingData = onGoingData)
            NEW_USER -> renderNewUserCampaignRibbon(onGoingData = onGoingData)
            THEMATIC_CAMPAIGN -> renderThematicCampaignRibbon(onGoingData = onGoingData)
        }
    }

    fun renderUpComingCampaign(upComingData: ProductNotifyMeDataModel) {
        when (upComingData.campaignIdentifier) {
            FLASH_SALE -> renderFlashSaleCampaignRibbon(campaignPeriod = UPCOMING, upComingData = upComingData)
            NPL -> renderNplCampaignRibbon(campaignPeriod = UPCOMING, upComingData = upComingData)
        }
    }

    // FLASH SALE
    private fun renderFlashSaleCampaignRibbon(@CampaignPeriod campaignPeriod: Int,
                                              onGoingData: ProductContentMainData? = null,
                                              upComingData: ProductNotifyMeDataModel? = null) {
        when (campaignPeriod) {
            UPCOMING -> {
                upComingData?.let {
                    renderUpComingCampaignRibbon(upComingData)
                    showCampaignRibbonType1()
                }
            }
            ONGOING -> {
                onGoingData?.let {
                    renderOnGoingCampaignRibbon(onGoingData)
                    showCampaignRibbonType2()
                }
            }
        }
    }

    // SLASH PRICE - not eligible for thematic campaign
    private fun renderSlashPriceCampaignRibbon(campaign: CampaignModular) {
        // set campaign name
        tpg_campaign_name_s3.text = campaign.campaignTypeName
        // set gradient for background
        val gradientHexCodes = campaign.background
        val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
        campaign_ribbon_layout_s3.background = gradientDrawable
        // hide irrelevant views
        regulatory_info_layout_s3.hide()
        tpg_ends_in_s3.hide()
        count_down_view_s3.hide()
        // show campaign ribbon type 3
        showCampaignRibbonType3()
    }

    // NPL
    private fun renderNplCampaignRibbon(@CampaignPeriod campaignPeriod: Int,
                                        onGoingData: ProductContentMainData? = null,
                                        upComingData: ProductNotifyMeDataModel? = null) {
    }

    // NEW USER
    private fun renderNewUserCampaignRibbon(onGoingData: ProductContentMainData) {
        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign
        // set campaign name
        val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
        tpg_campaign_name_s3.text = campaignName
        // campaign ribbon background
        val gradientHexCodes = onGoingData.campaign.background
        val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
        campaign_ribbon_layout_s3.background = gradientDrawable
        // hide irrelevant views
        regulatory_info_layout_s3.hide()
        tpg_ends_in_s3.hide()
        count_down_view_s3.hide()
        // show campaign ribbon type 3 hide the rest
        showCampaignRibbonType3()
    }

    // THEMATIC ONLY
    private fun renderThematicCampaignRibbon(onGoingData: ProductContentMainData) {
        renderOnGoingCampaignRibbon(onGoingData)
        showCampaignRibbonType3()
    }

    //==========================================================================================================================================

    // show upcoming structure
    private fun showCampaignRibbonType1() {
        campaign_ribbon_type_1.show()
        campaign_ribbon_type_2.hide()
        campaign_ribbon_type_3.hide()
    }

    // show ongoing structure
    private fun showCampaignRibbonType2() {
        campaign_ribbon_type_1.hide()
        campaign_ribbon_type_2.show()
        campaign_ribbon_type_3.hide()
    }

    // show thematic only, new user, slash price structure
    private fun showCampaignRibbonType3() {
        campaign_ribbon_type_1.hide()
        campaign_ribbon_type_2.hide()
        campaign_ribbon_type_3.show()
    }

    private fun renderUpComingCampaignRibbon(upComingData: ProductNotifyMeDataModel) {

        val thematicCampaign = upComingData.thematicCampaign

        // campaign ribbon background
        val gradientHexCodes = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else upComingData.background
        val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
        campaign_ribbon_layout_s1.background = gradientDrawable

        // campaign name
        val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else upComingData.campaignTypeName
        tpg_campaign_name_s1.text = if (campaignName.isNotBlank()) campaignName else context.getString(R.string.notify_me_title)

        // count down timer
        renderUpComingCountDownTimer(upComingData, count_down_view_s1)

        // remind me button
        renderUpComingRemindMeButton(listener?.isOwner() ?: false, upComingData, remind_me_button_s1)

        // regulatory info
        if (thematicCampaign.additionalInfo.isNotBlank()) {
            regulatory_info_layout_s1.show()
            tgp_regulatory_info_s1.text = thematicCampaign.additionalInfo
        } else {
            regulatory_info_layout_s1.hide()
        }
    }

    private fun renderUpComingRemindMeButton(isOwner: Boolean,
                                             upComingData: ProductNotifyMeDataModel,
                                             remindMeButton: Typography) {
        remindMeButton.showWithCondition(isOwner)
        when (upComingData.notifyMe) {
            true -> {
                remindMeButton.text = context.getString(R.string.notify_me_active)
                remindMeButton.setTextColor(ContextCompat.getColor(context, R.color.Unify_N100))
                remindMeButton.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_active)
            }
            false -> {
                remindMeButton.text = context.getString(R.string.notify_me_inactive)
                remindMeButton.setTextColor(ContextCompat.getColor(context, R.color.Unify_Static_White))
                remindMeButton.background = ContextCompat.getDrawable(context, R.drawable.bg_remind_me_btn_inactive)
            }
        }
        remindMeButton.setOnClickListener {
            listener?.onNotifyMeClicked(upComingData, this.trackDataModel
                    ?: ComponentTrackDataModel())
        }
    }

    private fun renderUpComingCountDownTimer(upComingData: ProductNotifyMeDataModel, countDownView: CountDownView) {
        try {
            val now = System.currentTimeMillis()
            val startTime = upComingData.startDate.toLongOrZero() * ProductNotifyMeViewHolder.SECOND
            val startDate = Date(startTime)
            val dayLeft = TimeUnit.MILLISECONDS.toDays(startTime - now)
            val delta = startDate.time - startTime

            this.rootView.visible()
            when {
                dayLeft < 0 -> {
                    this.rootView.hide()
                }
                dayLeft < 1 -> {
                    countDownView.setup(delta, startDate) {
                        listener?.refreshPage()
                    }
                    countDownView.visible()
                    tv_start_in_s1.text = context.getString(R.string.notify_me_subtitle_main)
                }
                else -> {
                    countDownView.gone()
                    tv_start_in_s1.text = HtmlLinkHelper(context,
                            context.getString(R.string.notify_me_subtitle, dayLeft.toInt().toString())
                    ).spannedString
                }
            }
        } catch (ex: Exception) {
            this.rootView.hide()
        }
    }

    fun updateRemindMeButton(listener: DynamicProductDetailListener, upComingData: ProductNotifyMeDataModel) {
        renderUpComingRemindMeButton(listener.isOwner(), upComingData, remind_me_button_s1)
    }

    private fun renderOnGoingCampaignRibbon(onGoingData: ProductContentMainData) {

        val campaign = onGoingData.campaign
        val thematicCampaign = onGoingData.thematicCampaign

        // render campaign ribbon background
        val gradientHexCodes = if (thematicCampaign.background.isNotBlank()) thematicCampaign.background else campaign.background
        val gradientDrawable = getGradientDrawableForBackGround(gradientHexCodes)
        campaign_ribbon_layout_s2.background = gradientDrawable

        // render campaign logo
        if (thematicCampaign.icon.isNotBlank()) {
            iu_campaign_logo_s2.loadImage(thematicCampaign.icon)
            iu_campaign_logo_s2.show()
        } else iu_campaign_logo_s2.hide()

        // render campaign name
        val campaignName = if (thematicCampaign.campaignName.isNotBlank()) thematicCampaign.campaignName else campaign.campaignTypeName
        tpg_campaign_name_s2.text = campaignName

        // render ongoing count down
        renderOnGoingCountDownTimer(campaign = campaign, countDownView = count_down_view_s2)

        // render stock bar
        renderStockBar(campaign.stockSoldPercentage)

        // render stock wording
        // TODO try catch
        tgp_stock_copy_s2.text = MethodChecker.fromHtml(onGoingData.stockWording)

        // render regulatory info
        if (thematicCampaign.additionalInfo.isNotBlank()) {
            tgp_regulatory_info_s2.text = thematicCampaign.additionalInfo
            regulatory_info_layout_s2.background = gradientDrawable
            regulatory_info_layout_s2.show()
        } else {
            regulatory_info_layout_s2.hide()
        }
    }

    private fun renderStockBar(stockSoldPercentage: Int) {
        if (stockSoldPercentage != 1) {
            pbu_stock_bar_s2.setValue(stockSoldPercentage)
            pbu_stock_bar_s2.show()
        } else {
            pbu_stock_bar_s2.hide()
            tgp_sold_out_s2.show()
        }
    }

    private fun renderOnGoingCountDownTimer(campaign: CampaignModular, countDownView: CountDownView) {
        try {
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            val endDateTimeMs = campaign.getEndDateLong * com.tokopedia.product.detail.view.fragment.partialview.PartialContentView.ONE_SECOND
            val now = java.lang.System.currentTimeMillis()
            val endDate = dateFormat.parse(campaign.endDate)
            val delta = endDate.time - endDateTimeMs

            if (java.util.concurrent.TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1) {
                countDownView.show()
                countDownView.setup(delta, endDate) {
                    this.listener?.refreshPage()
                }
            } else {
                tgp_count_down_wording_s2.hide()
                countDownView.hide()
            }
        } catch (ex: Exception) {
            this.hide()
        }
    }

    private fun getGradientDrawableForBackGround(gradientHexCodes: String): GradientDrawable {
        val gradientColors = gradientHexCodes.split(",")
        val firstColor = Color.parseColor(gradientColors[0])
        val secondColor = Color.parseColor(gradientColors[1])
        return GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(firstColor, secondColor))
    }
}