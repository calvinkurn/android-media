package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.state.PlayUpcomingState
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class UpcomingActionButtonViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    private val button = (rootView as UnifyButton)

    init {
        button.setOnClickListener { onButtonClick() }

        button.doOnApplyWindowInsets { view, insets, _, margin ->
            val marginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams

            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                view.parent.requestLayout()
            }
        }
    }

    fun onButtonClick() {
        listener.onClickActionButton()
    }

    fun setButtonStatus(status: PlayUpcomingState) {
        when (status) {
            is PlayUpcomingState.ReminderStatus -> {
                show()
                setButtonMode(isMain = !status.isReminded)
                button.text =
                    if (status.isReminded) getString(R.string.play_remind_me_cancel) else getString(
                        R.string.play_remind_me
                    )
            }
            PlayUpcomingState.WatchNow -> {
                setButtonMode(true)
                button.text = getString(R.string.play_watch_now)
                show()
            }
            PlayUpcomingState.Refresh -> {
                setButtonMode(false)
                button.text = getString(R.string.play_upcoming_refresh)
                show()
            }
            PlayUpcomingState.Loading -> {
                show()
                button.isLoading = true
            }
            else -> {
                invisible()
            }
        }
    }

    private fun setButtonMode(isMain: Boolean) {
        button.isLoading = false

        if (isMain) {
            button.buttonVariant = UnifyButton.Variant.FILLED
            button.buttonType = UnifyButton.Type.MAIN
            button.isInverse = false
        } else {
            button.buttonVariant = UnifyButton.Variant.GHOST
            button.buttonType = UnifyButton.Type.ALTERNATE
            button.isInverse = true
        }
    }

    interface Listener {
        fun onClickActionButton()
    }
}