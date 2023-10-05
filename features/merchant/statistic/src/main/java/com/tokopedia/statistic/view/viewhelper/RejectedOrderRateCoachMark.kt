package com.tokopedia.statistic.view.viewhelper

import android.content.Context
import android.view.View
import androidx.core.view.doOnDetach
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.statistic.R
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 03/10/23.
 */

class RejectedOrderRateCoachMark @Inject constructor(){

    companion object {
        const val DATA_KEY = "plusCancellationRate"
        const val PREF_KEY_FORMAT = "${DATA_KEY}_%s"
    }

    private var userId: String = ""
    private var anchor: View? = null
    private var coachMark: CoachMark2? = null

    fun setAnchor(dataKey: String, view: View) {
        if (dataKey == DATA_KEY && anchor != view) {
            anchor = view
            show()
        }
    }

    fun show() {
        anchor?.let { anchor ->
            if (hasShown(anchor.context)) return

            val cm = coachMark ?: return

            val items = arrayListOf<CoachMark2Item>()
            items.add(
                CoachMark2Item(
                    anchorView = anchor,
                    title = anchor.context.getString(R.string.stc_rejected_card_coach_mark_title),
                    description = anchor.context.getString(R.string.stc_rejected_card_coach_mark_description),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
            cm.isDismissed = false
            cm.showCoachMark(items)

            anchor.doOnDetach {
                dismissCoachMark()
            }
        }
    }

    fun destroy() {
        coachMark = null
    }

    fun init(context: Context, userId: String) {
        this.userId = userId
        if (hasShown(context)) return

        if (coachMark == null) {
            coachMark = CoachMark2(context).apply {
                simpleCloseIcon?.setOnClickListener {
                    setHasShown(context)
                    dismissCoachMark()
                }
            }
        }
    }

    private fun dismissCoachMark() {
        coachMark?.dismissCoachMark()
    }

    private fun setHasShown(context: Context) {
        CoachMarkPreference.setShown(context, getPrefKey(), true)
    }

    private fun hasShown(context: Context): Boolean {
        return CoachMarkPreference.hasShown(context, getPrefKey())
    }

    private fun getPrefKey() = String.format(PREF_KEY_FORMAT, userId)
}