package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.viewutil.invertIfDarkMode
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import com.tokopedia.gamification.R as gamificationR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class KetupatReferralBannerVH(itemView: View) :
    AbstractViewHolder<KetupatReferralBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_referral_banner
    }

    var referlButton: UnifyButton = itemView.findViewById(gamificationR.id.cta_referral)
    override fun bind(element: KetupatReferralBannerVHModel?) {

        referlButton.setTextColor(
            ContextCompat.getColor(itemView.context, unifyprinciplesR.color.Unify_NN950)
                .invertIfDarkMode(
                    itemView.context
                )
        )
        try {
            val percentCompletion: Int
            val scratchCardId = element?.scratchCard?.id.toString()

            element?.timeData.let { timeData ->
                itemView.findViewById<TimerUnifySingle>(gamificationR.id.referral_timer)?.apply {
                    isShowClockIcon = true
                    this.timerText = "Berakhir dalam"
                    this.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
                    val timeInMill =
                        timeData?.gameReferralEventContent?.eventContent?.remainingTime?.toLong()
                    val calendar = Calendar.getInstance(Locale.ENGLISH)

                    val currentTime = System.currentTimeMillis()
                    if (timeInMill != null) {
                        calendar.setTimeInMillis(timeInMill.times(1000) + currentTime)
                    }
                    this.targetDate = calendar
                }
                timeData?.gameReferralStamp.apply {
                    val currentProgress = this?.currentStampCount
                    val maxProgress = this?.maxStampCount!!
                    percentCompletion = (currentProgress?.times(100)?.div(maxProgress)) ?: 0

                    itemView.findViewById<ProgressBarUnify>(gamificationR.id.referral_progress_bar)
                        .setValue(percentCompletion, true)

                    this.stampLevelData.get(0).TotalStampNeeded.toString().let {
                        if (currentProgress != null) {
                            if (currentProgress >= it.toIntOrZero()) {
                                itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_2)
                                    .apply {
                                        this.text = ""
                                        background =
                                            resources.getDrawable(gamificationR.drawable.gami_referral_progress_indicator_check_bg)
                                    }

                            } else {
                                itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_2).text =
                                    it
                            }
                        }
                    }

                    (this.stampLevelData.get(0).TotalStampNeeded + this.stampLevelData.get(1).TotalStampNeeded).toString()
                        .also {
                            if (currentProgress != null) {
                                if (currentProgress >= it.toIntOrZero()) {
                                    itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_3)
                                        .apply {
                                            this.text = ""
                                            background =
                                                resources.getDrawable(gamificationR.drawable.gami_referral_progress_indicator_check_bg)
                                        }

                                } else {
                                    itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_3).text =
                                        it
                                }
                            }

                        }

                    (this.stampLevelData.get(0).TotalStampNeeded + this.stampLevelData.get(1).TotalStampNeeded + this.stampLevelData.get(
                        2
                    ).TotalStampNeeded).toString()
                        .also {
                            if (currentProgress != null) {
                                if (currentProgress >= it.toIntOrZero()) {
                                    itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_4)
                                        .apply {
                                            this.text = ""
                                            background =
                                                resources.getDrawable(gamificationR.drawable.gami_referral_progress_indicator_check_bg)
                                        }

                                } else {
                                    itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_4).text =
                                        it
                                }
                            }
                        }

                }
            }

            element?.referral.let { referralData ->
                val referralId = referralData?.jsonParameter?.let {
                    JSONObject(
                        it
                    ).getString("eventSlug")
                }

                itemView.findViewById<Typography>(gamificationR.id.referral_banner_title)?.text =
                    referralData?.title

                if (percentCompletion == 100) {
                    referralData?.cta?.find { it?.type == "after" }?.let { cta ->
                        referlButton.text =
                            cta.text

                        referlButton?.apply {
                            this.setOnClickListener {
                                RouteManager.route(
                                    context,
                                    cta.appLink
                                )
                                GamificationAnalytics.sendClickReferralSectionEvent(
                                    "{'direct_reward_id':'${scratchCardId}', 'referral_id':'${referralId}'}", "gamification",
                                    "tokopediamarketplace"
                                )
                            }
                        }
                    }

                    itemView.findViewById<ImageUnify>(gamificationR.id.referral_bg_img)
                        ?.setImageUrl(referralData?.assets?.find { it?.key == "BACKGROUND_IMAGE_AFTER" }?.value.toString())

                    itemView.findViewById<Typography>(gamificationR.id.referral_banner_text)?.text =
                        referralData?.text?.find { it?.key == "AFTER_FINISH" }?.value
                } else {
                    referralData?.cta?.find { it?.type == "before" }?.let { cta ->
                        referlButton.text =
                            cta.text

                        referlButton?.apply {
                            this.setOnClickListener {
                                RouteManager.route(
                                    context,
                                    cta.appLink
                                )
                                GamificationAnalytics.sendClickReferralSectionEvent(
                                    "{'direct_reward_id':'${scratchCardId}', 'referral_id':'${referralId}'}", "gamification",
                                    "tokopediamarketplace"
                                )
                            }
                        }

                    }

                    itemView.findViewById<Typography>(gamificationR.id.referral_banner_text)?.text =
                        referralData?.text?.find { it?.key == "BEFORE_FINISH" }?.value

                    itemView.findViewById<ImageUnify>(gamificationR.id.referral_bg_img)
                        ?.setImageUrl(referralData?.assets?.find { it?.key == "BACKGROUND_IMAGE_BEFORE" }?.value.toString())
                }
                GamificationAnalytics.sendImpressReferralSectionEvent("{'direct_reward_id':'${scratchCardId}', 'referral_id':'${referralId}'}")
            }

        } catch (_: Exception) {
        }
    }
}
