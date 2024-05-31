package com.tokopedia.navigation.util

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.boolean
import javax.inject.Inject
import com.tokopedia.navigation.R as navigationR

class MePageCoachMark @Inject constructor(
    private val context: Context
) {

    private var mePageCoachMarkToggle by context
        .getSharedPreferences(PREF_KEY_ME_PAGE_COACH_MARK, Context.MODE_PRIVATE)
        .boolean(false, PREF_KEY_ME_PAGE_COACH_MARK_SHOWN)

    private val coachMark = CoachMark2(context)

    fun show(view: View) {
        val willShowCoachMark = !mePageCoachMarkToggle
        if (!willShowCoachMark) return

        if (coachMark.isShowing) return
        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    view,
                    context.getString(navigationR.string.me_page_icon_coach_mark_title),
                    context.getString(navigationR.string.me_page_icon_coach_mark_content),
                )
            )
        )

        coachMark.onDismissListener = {
            mePageCoachMarkToggle = true
        }
    }

    fun forceDismiss() {
        coachMark.onDismissListener = {}
        coachMark.dismissCoachMark()
    }

    companion object {
        private const val PREF_KEY_ME_PAGE_COACH_MARK = "me_page_coach_mark"
        private const val PREF_KEY_ME_PAGE_COACH_MARK_SHOWN = "me_page_coach_mark_shown"
    }
}
