package com.tokopedia.play.broadcaster.util.coachmark

import android.content.Context
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.play.broadcaster.ui.model.LiveMenuCoachMarkType

/**
 * Created by Jonathan Darwin on 19 March 2024
 */
class LiveMenuCoachMark(
    private val context: Context,
) {
    private var mListener: Listener? = null

    private val mCoachMarkTypes = mutableListOf<LiveMenuCoachMarkType>()

    private val coachMark: CoachMark2 = CoachMark2(context)

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(coachMarkTypes: List<LiveMenuCoachMarkType>) {
        mCoachMarkTypes.clear()
        mCoachMarkTypes.addAll(coachMarkTypes)

        if (coachMarkTypes.isEmpty()) return

        coachMark.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                mCoachMarkTypes.getOrNull(currentIndex)?.let {
                    mListener?.onImpress(it)
                }
            }
        })

        coachMark.showCoachMark(
            ArrayList(
                mCoachMarkTypes.map {
                    CoachMark2Item(
                        anchorView = it.view,
                        title = if (it.titleRes != 0) context.getString(it.titleRes) else "",
                        description = if (it.descriptionRes != 0) context.getString(it.descriptionRes) else "",
                    )
                }
            )
        )

        mListener?.onImpress(mCoachMarkTypes.first())
    }

    fun dismiss() {
        coachMark.dismissCoachMark()
    }

    interface Listener {
        fun onImpress(coachMarkType: LiveMenuCoachMarkType)
    }
}
