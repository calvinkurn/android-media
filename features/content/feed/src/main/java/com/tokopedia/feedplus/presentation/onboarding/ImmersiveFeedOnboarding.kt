package com.tokopedia.feedplus.presentation.onboarding

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.feedplus.R
import com.tokopedia.play_common.util.extension.awaitLayout
import com.tokopedia.play_common.util.extension.awaitMeasured

/**
 * Created by kenny.hadisaputra on 06/03/23
 */
class ImmersiveFeedOnboarding private constructor(
    private val context: Context,
    private val createContentView: View?,
    private val profileEntryPointView: View?,
) {

    private val coachMark = CoachMark2(context)

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun show() {
        val coachMarkItems = buildList {
            if (createContentView != null) {
                add(createContentCoachMarkItem(createContentView))
            }

            if (profileEntryPointView != null) {
                add(profileEntryPointCoachMarkItem(profileEntryPointView))
            }
        }

        if (coachMarkItems.isEmpty()) return

        coachMarkItems.forEach {
            it.anchorView.awaitLayout()
        }
        coachMark.showCoachMark(ArrayList(coachMarkItems), null, 0)
    }

    class Builder(private val context: Context) {

        private var createContentView: View? = null
        private var profileEntryPointView: View? = null

        fun setCreateContentView(view: View?) = builder {
            createContentView = view
        }

        fun setProfileEntryPointView(view: View?) = builder {
            profileEntryPointView = view
        }

        fun build(): ImmersiveFeedOnboarding {
            return ImmersiveFeedOnboarding(
                context = context,
                createContentView = createContentView,
                profileEntryPointView = profileEntryPointView,
            )
        }

        private fun builder(handler: Builder.() -> Unit): Builder {
            handler()
            return this
        }
    }

//    fun show(flag: Int) {
//        buildList {
//            if (shouldShowCreateContent(flag)) {
//                //show create content
//            }
//
//        }
//
//
//
//        if (shouldShowProfileEntryPoint(flag)) {
//            //show profile entry point
//        }
//    }
//
    private fun createContentCoachMarkItem(view: View): CoachMark2Item {
        return CoachMark2Item(
            anchorView = view,
            title = context.getString(R.string.feed_onboarding_create_content_title),
            description = context.getString(R.string.feed_onboarding_create_content_subtitle),
            position = CoachMark2.POSITION_BOTTOM,
        )
    }

    private fun profileEntryPointCoachMarkItem(view: View): CoachMark2Item {
        return CoachMark2Item(
            anchorView = view,
            title = context.getString(R.string.feed_onboarding_profile_entry_point_title),
            description = context.getString(R.string.feed_onboarding_profile_entry_point_subtitle),
            position = CoachMark2.POSITION_BOTTOM,
        )
    }
//
//    private fun shouldShowCreateContent(flag: Int): Boolean {
//        return flag and COACH_MARK_CREATE_CONTENT == COACH_MARK_CREATE_CONTENT
//    }
//
//    private fun shouldShowProfileEntryPoint(flag: Int): Boolean {
//        return flag and COACH_MARK_PROFILE_ENTRY_POINT == COACH_MARK_PROFILE_ENTRY_POINT
//    }
//
//    companion object {
//        const val COACH_MARK_CREATE_CONTENT = 0x01
//        const val COACH_MARK_PROFILE_ENTRY_POINT = 0x02
//    }
}
