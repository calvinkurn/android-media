package com.tokopedia.navigation.util

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.kotlin.extensions.boolean
import javax.inject.Inject
import com.tokopedia.navigation.R as navigationR

class MePageCoachMark @Inject constructor(
    private val context: Context
) {

    private var mePageCoachMarkToggle by context
        .getSharedPreferences(PREF_KEY_ME_PAGE_COACH_MARK, Context.MODE_PRIVATE)
        .boolean(false, PREF_KEY_ME_PAGE_COACH_MARK_SHOWN)
    private var inboxCoachMarkToggle by context
        .getSharedPreferences(PREF_KEY_ME_PAGE_COACH_MARK, Context.MODE_PRIVATE)
        .boolean(false, PREF_KEY_INBOX_COACH_MARK_SHOWN)

    private val coachMark = CoachMark2(context)

    fun show(inboxView: View?, mePageView: View) {
        val inboxCoachMark = getInboxCoachMark(inboxView)
        val mePageCoachMark = getMePageCoachMark(mePageView)
        val coachMarkItems = arrayListOf(inboxCoachMark, mePageCoachMark)
        if (coachMark.isShowing) return
        coachMark.showCoachMark(ArrayList(coachMarkItems.filterNotNull()))

        coachMark.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                when (coachMarkItem) {
                    inboxCoachMark -> inboxCoachMarkToggle = true
                    mePageCoachMark -> mePageCoachMarkToggle = true
                }
            }
        })
        coachMark.onDismissListener = {
            mePageCoachMarkToggle = true
        }
    }

    private fun getMePageCoachMark(mePageView: View): CoachMark2Item? {
        val willShowCoachMark = !mePageCoachMarkToggle
        return if (willShowCoachMark) {
            CoachMark2Item(
                mePageView,
                context.getString(navigationR.string.me_page_icon_coach_mark_title),
                context.getString(navigationR.string.me_page_icon_coach_mark_content),
            )
        } else {
            null
        }
    }

    private fun getInboxCoachMark(inboxView: View?): CoachMark2Item? {
        val combineNative = HomeRollenceController.shouldCombineInboxNotif()
        val neverShowInboxCoachMark = !inboxCoachMarkToggle
        val isEligibleCoachMark = neverShowInboxCoachMark && combineNative
        return if (isEligibleCoachMark && inboxView != null) {
            CoachMark2Item(
                inboxView,
                context.getString(navigationR.string.home_inbox_coach_mark_title),
                context.getString(navigationR.string.home_inbox_coach_mark_description),
                position = CoachMark2.POSITION_BOTTOM
            )
        } else {
            null
        }
    }

    fun forceDismiss() {
        coachMark.onDismissListener = {}
        coachMark.dismissCoachMark()
    }

    companion object {
        private const val PREF_KEY_ME_PAGE_COACH_MARK = "me_page_coach_mark"
        private const val PREF_KEY_ME_PAGE_COACH_MARK_SHOWN = "me_page_coach_mark_shown"
        private const val PREF_KEY_INBOX_COACH_MARK_SHOWN = "inbox_coach_mark_shown"
    }
}
