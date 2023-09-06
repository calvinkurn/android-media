package com.tokopedia.loginHelper.presentation.addEditAccount.customview

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2.Companion.POSITION_BOTTOM
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.loginHelper.R
import javax.inject.Inject

class SaveToLocalCoachmark @Inject constructor() {

    private var context: Context? = null
    private var anchor: View? = null
    private var coachMark: CoachMark2? = null

    fun showReplyBubbleOnBoarding(
        anchor: View?,
        context: Context? = null
    ) {
        initializeFields(anchor, context)
        showReplyBubbleOnBoarding()
    }

    private fun initializeFields(
        anchor: View?,
        context: Context? = null
    ) {
        this.anchor = anchor
        this.context = context
    }

    private fun showReplyBubbleOnBoarding() {
        context?.let {
            if (anchor == null) return
            val description = it.getString(R.string.login_helper_local_storage)
            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMark = CoachMark2(it)
            coachMarkItem.add(
                CoachMark2Item(
                    anchor!!,
                    "",
                    description,
                    POSITION_BOTTOM
                )
            )
            coachMark?.showCoachMark(coachMarkItem, null)
        }
    }

    fun flush() {
        anchor = null
        context = null
    }

    fun dismiss() {
        coachMark?.dismissCoachMark()
    }
}
