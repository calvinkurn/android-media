package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatReferralBannerVHModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import com.tokopedia.gamification.R as gamificationR

class KetupatReferralBannerVH(itemView: View) :
    AbstractViewHolder<KetupatReferralBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_referral_banner
    }

    override fun bind(element: KetupatReferralBannerVHModel?) {
        element?.referral.let { referralData ->
            itemView.findViewById<Typography>(gamificationR.id.referral_banner_title)?.text =
                referralData?.title
            itemView.findViewById<ImageUnify>(gamificationR.id.referral_bg_img)
                ?.setImageUrl(referralData?.assets?.find { it?.key == "BACKGROUND_IMAGE_START" }?.value.toString())
            itemView.findViewById<ImageUnify>(gamificationR.id.referral_bg_img)?.apply {
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        referralData?.cta?.find { it?.type == "redirection" }?.appLink
                    )
                }
            }
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
                    val percentCompletion = (currentProgress?.times(100)?.div(maxProgress))

                    if (percentCompletion != null) {
                        itemView.findViewById<ProgressBarUnify>(gamificationR.id.referral_progress_bar)
                            .setValue(percentCompletion, true)
                    }
                    itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_2).text =
                        this.stampLevelData.get(0).TotalStampNeeded.toString()

                    (this.stampLevelData.get(0).TotalStampNeeded + this.stampLevelData.get(1).TotalStampNeeded).toString()
                        .also { itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_3).text = it }

                    (this.stampLevelData.get(0).TotalStampNeeded + this.stampLevelData.get(1).TotalStampNeeded + this.stampLevelData.get(
                        2
                    ).TotalStampNeeded).toString()
                        .also { itemView.findViewById<Typography>(gamificationR.id.referral_bar_text_4).text = it }

                }
            }
        }
    }
}
