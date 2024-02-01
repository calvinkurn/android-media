package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.app.Activity
import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.presentation.LandingPageRefreshCallback
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.notifications.inApp.ketupat.ActivityLifecycleHandler
import com.tokopedia.notifications.inApp.ketupat.KetupatSlashCallBack
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.getDayDiffFromToday
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.tokopedia.gamification.R as gamificationR

class KetupatCrackBannerVH(itemView: View) :
    AbstractViewHolder<KetupatCrackBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_crack_banner
    }

    fun showKetupatPopUp(
        activity: Activity,
        crackData: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem?,
        scratchCardId: String,
        endTime: String?,
        landingPageRefreshCallback: LandingPageRefreshCallback?
    ) {
        //show pop up and pass the callback for tracking slash
        var activityLifecycleHandler = ActivityLifecycleHandler()
        activityLifecycleHandler.getScratchCardData(activity,
            "scratch-card-landing-page",
            object : KetupatSlashCallBack {
                override fun ketuPatSlashed() {
                    //change button state
                    GamificationAnalytics.sendClickClaimButtonEvent(
                        "direct_reward_id: $scratchCardId", "gamification",
                        "tokopediamarketplace"
                    )
                    //refresh data
                    Handler().postDelayed({
                        landingPageRefreshCallback?.refreshLandingPage()
                    }, 2000)

                }
            })
    }

    override fun bind(element: KetupatCrackBannerVHModel?) {
        val scratchCardId = element?.scratchCard?.id.toString()
        val endTime = element?.scratchCard?.endTime

        element?.crack.let { crackData ->
            itemView.findViewById<Typography>(gamificationR.id.crack_banner_title)?.text =
                crackData?.title
            itemView.findViewById<ImageUnify>(gamificationR.id.crack_bg_img)
                ?.setImageUrl(crackData?.assets?.find { it?.key == "BACKGROUND_IMAGE" }?.value.toString())
            itemView.findViewById<Typography>(gamificationR.id.more_info_btn).apply {
                text = crackData?.cta?.find { it?.type == "redirection" }?.text
                this.setOnClickListener {
                    RouteManager.route(
                        context,
                        crackData?.cta?.find { it?.type == "redirection" }?.appLink
                    )
                    GamificationAnalytics.sendClickCaraMainEvent(
                        "direct_reward_id: $scratchCardId", "gamification",
                        "tokopediamarketplace"
                    )
                }
            }

            if (crackData?.cta?.find { it?.type == "crack" } == null) {
                itemView.findViewById<ImageUnify>(gamificationR.id.crack_icon_img)
                    ?.setImageUrl(crackData?.assets?.find { it?.key == "IMAGE_ICON_OPENED" }?.value.toString())
                itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).hide()

                itemView.findViewById<Typography>(gamificationR.id.more_info_btn).setBackground(
                    itemView.context.getResources()
                        .getDrawable(gamificationR.drawable.rect_stroke_white_solid)
                )

                if (lessThanADay(endTime)) {
                    itemView.findViewById<Typography>(gamificationR.id.crack_img_text)?.text =
                        crackData?.text?.find { it?.key == "LAST_DAY" }?.value
                } else {
                    itemView.findViewById<Typography>(gamificationR.id.crack_img_text)?.text =
                        crackData?.text?.find { it?.key == "OPENED" }?.value
                }
            } else {
                itemView.findViewById<ImageUnify>(gamificationR.id.crack_icon_img)
                    ?.setImageUrl(crackData?.assets?.find { it?.key == "IMAGE_ICON" }?.value.toString())
                itemView.findViewById<Typography>(gamificationR.id.crack_img_text)?.text =
                    crackData?.text?.find { it?.key == "DEFAULT" }?.value

            }
            itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).apply {
                this.setOnClickListener {
                    showKetupatPopUp(
                        (context as Activity), crackData, scratchCardId,
                        endTime, element?.landingPageRefreshCallback
                    )
                }
            }
            crackData?.cta?.get(0)?.imageURL?.let {
                if (it.isNotEmpty()) {
                    itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).setImageUrl(
                        it
                    )
                }
            }
            GamificationAnalytics.sendImpressManualClaimSectionEvent("direct_reward_id: $scratchCardId")
        }
    }

    private fun lessThanADay(endTime: String?): Boolean {
        val diffEndTime = getDateDiffFromToday(endTime)
        if (diffEndTime != null) {
            if (diffEndTime.toInt() == 0)
                return true
        }
        return false
    }

    private fun getDateDiffFromToday(date: String?): Long? {
        try {
            val localeID = Locale("in", "ID")
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", localeID)
            formatter.isLenient = false
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter.parse(date + "00")?.getDayDiffFromToday()
        } catch (e: ParseException) {
            Timber.e(e)
        }
        return -1
    }
}
