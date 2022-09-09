package com.tokopedia.tokopedianow.home.presentation.view.coachmark

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.tokopedianow.R

class SwitcherCoachMark(private val context: Context, private val onDismiss: () -> Unit) {

    private var coachMark: CoachMark2? = null
    private var listItem: ArrayList<CoachMark2Item> = arrayListOf()

    fun show() {
        coachMark = CoachMark2(context)
        coachMark?.onDismissListener = {
            onDismiss()
        }
        coachMark?.stepButtonTextLastChild = context.getString(R.string.tokopedianow_on_boarding_step_button_text_last_child)
        coachMark?.showCoachMark(listItem)
    }

    fun set20mCoachMark(tpTitle: View) {
        listItem = arrayListOf(
            CoachMark2Item(
                anchorView = tpTitle,
                title = tpTitle.context?.getString(R.string.tokopedianow_20m_coachmark_title).orEmpty(),
                description = tpTitle.context?.getString(R.string.tokopedianow_20m_coachmark_description).orEmpty(),
                position = CoachMark2.POSITION_BOTTOM
            )
        )
    }

    fun set2hCoachMark(tpTitle: View, tpSubtitle: View) {
        listItem = arrayListOf(
            CoachMark2Item(
                anchorView = tpTitle,
                title = tpTitle.context?.getString(R.string.tokopedianow_home_on_boarding_20m_title_first_section_coachmark).orEmpty(),
                description = tpTitle.context?.getString(R.string.tokopedianow_home_on_boarding_20m_description_first_section_coachmark).orEmpty(),
                position = CoachMark2.POSITION_BOTTOM
            ),
            CoachMark2Item(
                anchorView = tpSubtitle,
                title = tpSubtitle.context?.getString(R.string.tokopedianow_home_on_boarding_20m_title_second_section_coachmark).orEmpty(),
                description = tpSubtitle.context?.getString(R.string.tokopedianow_home_on_boarding_20m_description_second_section_coachmark).orEmpty(),
                position = CoachMark2.POSITION_BOTTOM
            )
        )
    }

    fun hide() {
        coachMark?.dismissCoachMark()
    }
}