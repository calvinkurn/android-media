package com.tokopedia.stories.common

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item

/**
 * Created by kenny.hadisaputra on 31/07/23
 */
internal class StoriesAvatarCoachMark(
    private val context: Context,
    private val onClosedByUser: () -> Unit
) {

    private var mCoachMark: CoachMark2? = null

    fun show(view: View, text: String) {
        val coachMark = getOrCreateCoachMark()
        if (coachMark.isShowing) return

        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(view, text, "")
            )
        )
    }

    fun hide(view: View? = null) {
        val coachMark = mCoachMark ?: return

        if (view == null) {
            coachMark.hideInternal()
        } else {
            val items = coachMark.coachMarkItem
            val isSameAnchor = items.any { it.anchorView == view }

            if (isSameAnchor) coachMark.hideInternal()
        }
    }

    private fun CoachMark2.hideInternal() {
        val contentView = mCoachMark?.contentView ?: return
        contentView.apply { alpha = 0f }
        onDismissListener = {}
        dismissCoachMark()
    }

    private fun getOrCreateCoachMark(): CoachMark2 {
        val coachMark = mCoachMark
        return if (coachMark != null && !coachMark.isDismissed) {
            return coachMark
        } else {
            CoachMark2(context).apply {
                onDismissListener = onClosedByUser
            }.also {
                mCoachMark = it
            }
        }
    }
}
