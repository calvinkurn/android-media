package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatCrackBannerVHModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.notifications.inApp.ketupat.ActivityLifecycleHandler
import com.tokopedia.notifications.inApp.ketupat.KetupatSlashCallBack
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatCrackBannerVH(itemView: View) :
    AbstractViewHolder<KetupatCrackBannerVHModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_crack_banner
    }

    fun showKetupatPopUp(activity: Activity,
                         crackData: KetupatLandingPageData.GamiGetScratchCardLandingPage.SectionItem?,
                         scratchCardId: String){
        //show pop up and pass the callback for tracking slash
        var activityLifecycleHandler = ActivityLifecycleHandler()
        activityLifecycleHandler.getScratchCardData(activity,
            "scratch-card-landing-page",
            object : KetupatSlashCallBack {
                override fun ketuPatSlashed() {
                    //change button state
                    itemView.findViewById<ImageUnify>(gamificationR.id.crack_icon_img)
                        ?.setImageUrl(crackData?.assets?.find { it?.key == "IMAGE_ICON_OPENED" }?.value.toString())
                    itemView.findViewById<Typography>(gamificationR.id.crack_img_text)?.text =
                        crackData?.text?.find { it?.key == "OPENED" }?.value
                    itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).hide()
                    GamificationAnalytics.sendClickClaimButtonEvent(
                        "direct_reward_id: $scratchCardId", "gamification",
                        "tokopediamarketplace"
                    )
                }
            })
    }

    override fun bind(element: KetupatCrackBannerVHModel?) {
        val scratchCardId = element?.scratchCard?.id.toString()

        element?.crack.let { crackData ->
            itemView.findViewById<Typography>(gamificationR.id.crack_banner_title)?.text =
                crackData?.title
            itemView.findViewById<ImageUnify>(gamificationR.id.crack_bg_img)
                ?.setImageUrl(crackData?.assets?.find { it?.key == "BACKGROUND_IMAGE" }?.value.toString())
            itemView.findViewById<ImageUnify>(gamificationR.id.crack_icon_img)
                ?.setImageUrl(crackData?.assets?.find { it?.key == "IMAGE_ICON" }?.value.toString())
            itemView.findViewById<Typography>(gamificationR.id.crack_img_text)?.text =
                crackData?.text?.find { it?.key == "DEFAULT" }?.value
            itemView.findViewById<UnifyButton>(gamificationR.id.more_info_btn).apply {
                text = "Baca Cara Main"
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
            itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).apply {
                this.setOnClickListener {
                    showKetupatPopUp((context as Activity), crackData, scratchCardId)
                }
            }
            crackData?.cta?.get(0)?.imageURL?.let {
                itemView.findViewById<ImageUnify>(gamificationR.id.open_btn_bg).setImageUrl(
                    it
                )
            }

            GamificationAnalytics.sendImpressManualClaimSectionEvent("direct_reward_id: $scratchCardId")
        }
    }
}
