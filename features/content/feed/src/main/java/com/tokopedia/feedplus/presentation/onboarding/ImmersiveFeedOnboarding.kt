package com.tokopedia.feedplus.presentation.onboarding

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.feedplus.R
import com.tokopedia.play_common.util.extension.awaitLayout

/**
 * Created by kenny.hadisaputra on 06/03/23
 */
class ImmersiveFeedOnboarding private constructor(
    private val context: Context,
    private val createContentView: View?,
    private val profileEntryPointView: View?,
    private val browseIconView: View?,
    private val listener: Listener,
) {

    private val coachMark = CoachMark2(context)

    suspend fun show() {
        val coachMarkItems = buildList {
            if (profileEntryPointView != null) {
                add(profileEntryPointCoachMarkItem(profileEntryPointView))
            }

            if (createContentView != null) {
                add(createContentCoachMarkItem(createContentView))
            }

            if (browseIconView != null) {
                add(browseEntryPointCoachMarkItem(browseIconView))
            }
        }

        if (coachMarkItems.isEmpty()) {
            listener.onFinished(isForcedDismiss = false)
            return
        }

        if (coachMarkItems.size > 1) {
            coachMark.setStepListener(object : CoachMark2.OnStepListener {
                var mPrevIndex = 0

                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    if (mPrevIndex >= currentIndex) {
                        coachMark.onDismissListener = {
                            listener.onFinished(isForcedDismiss = false)
                        }
                    } else {
                        val prevItem = coachMarkItems[currentIndex - 1]
                        triggerListenerForItem(prevItem)

                        coachMark.onDismissListener = {
                            triggerListenerForItem(coachMarkItem)
                            listener.onFinished(isForcedDismiss = false)
                        }
                    }

                    mPrevIndex = currentIndex
                }
            })
        }

        coachMark.onDismissListener = {
            triggerListenerForItem(coachMarkItems.first())
            listener.onFinished(isForcedDismiss = false)
        }

        coachMarkItems.forEach {
            it.anchorView.awaitLayout()
        }

        coachMark.showCoachMark(ArrayList(coachMarkItems), null, 0)
        listener.onStarted()
    }

    fun dismiss() {
        coachMark.onDismissListener = {
            listener.onFinished(isForcedDismiss = true)
        }
        coachMark.dismissCoachMark()
    }

    fun isShowing(): Boolean {
        return coachMark.isShowing
    }

    private fun triggerListenerForItem(item: CoachMark2Item) {
        when (item.anchorView) {
            createContentView -> {
                listener.onCompleteCreateContentOnboarding()
            }
            profileEntryPointView -> {
                listener.onCompleteProfileEntryPointOnboarding()
            }
            browseIconView -> {
                listener.onCompleteBrowseEntryPointOnboarding()
            }
        }
    }

    class Builder(private val context: Context) {

        private var createContentView: View? = null
        private var profileEntryPointView: View? = null
        private var browseIconView: View? = null
        private var listener: Listener = object : Listener {
            override fun onStarted() {}

            override fun onCompleteCreateContentOnboarding() {}

            override fun onCompleteProfileEntryPointOnboarding() {}

            override fun onCompleteBrowseEntryPointOnboarding() {}

            override fun onFinished(isForcedDismiss: Boolean) {}
        }

        fun setCreateContentView(view: View?) = builder {
            createContentView = view
        }

        fun setProfileEntryPointView(view: View?) = builder {
            profileEntryPointView = view
        }

        fun setBrowseIconView(view: View?) = builder {
            browseIconView = view
        }

        fun setListener(listener: Listener) = builder {
            this.listener = listener
        }

        fun build(): ImmersiveFeedOnboarding {
            return ImmersiveFeedOnboarding(
                context = context,
                createContentView = createContentView,
                profileEntryPointView = profileEntryPointView,
                browseIconView = browseIconView,
                listener = listener,
            )
        }

        private fun builder(handler: Builder.() -> Unit): Builder {
            handler()
            return this
        }
    }

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

    private fun browseEntryPointCoachMarkItem(view: View): CoachMark2Item {
        return CoachMark2Item(
            anchorView = view,
            title = context.getString(R.string.feed_onboarding_browse_entry_point_title),
            description = context.getString(R.string.feed_onboarding_browse_entry_point_subtitle),
            position = CoachMark2.POSITION_BOTTOM,
        )
    }

    interface Listener {
        fun onStarted()
        fun onCompleteCreateContentOnboarding()
        fun onCompleteProfileEntryPointOnboarding()
        fun onCompleteBrowseEntryPointOnboarding()
        fun onFinished(isForcedDismiss: Boolean)
    }
}
