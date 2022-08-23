package com.tokopedia.topchat.chatroom.view.onboarding

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt

class SrwOnBoarding {

    private var coachMarkOnBoarding: CoachMark2? = null

    fun show(context: Context, anchor: View) {
        if (!CoachMarkPreference.hasShown(context, SrwFrameLayout.TAG)) {
            coachMarkOnBoarding = CoachMark2(context)
            coachMarkOnBoarding?.let { coachMark2 ->
                anchor.post {
                    val coachMarkItems: ArrayList<CoachMark2Item> = ArrayList()
                    coachMarkItems.add(
                        CoachMark2Item(
                            anchor,
                            context.getString(R.string.coach_product_bundling_title),
                            context.getString(R.string.coach_product_bundling_desc),
                            CoachMark2.POSITION_TOP
                        )
                    )
                    coachMark2.showCoachMark(step = coachMarkItems)
                    CoachMarkPreference.setShown(context, SrwFrameLayout.TAG, true)
                    trackOnBoarding()
                }
            }
        }
    }

    private fun trackOnBoarding() {
        trackViewOnBoarding()
        coachMarkOnBoarding?.simpleCloseIcon?.setOnClickListener {
            trackDismissOnBoarding()
            dismiss()
        }
    }

    fun dismiss() {
        coachMarkOnBoarding?.dismissCoachMark()
    }

    private fun trackViewOnBoarding() {
        TopChatAnalyticsKt.eventViewSrwOnBoarding()
    }

    private fun trackDismissOnBoarding() {
        TopChatAnalyticsKt.eventClickCloseSrwOnBoarding()
    }
}