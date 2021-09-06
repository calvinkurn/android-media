package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.R
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
    listener: Listener
) : ViewComponent(container, idRes) {

    private val button = (rootView as UnifyButton)

    init {
        button.setOnClickListener {
            setButtonStatus(Status.LOADING)
            listener.onClickActionButton()
        }

        button.doOnApplyWindowInsets { view, insets, _, margin ->
            val marginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams

            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                view.parent.requestLayout()
            }
        }
    }

    fun setButtonStatus(status: Status) {
        when(status) {
            Status.REMIND_ME -> {
                button.isLoading = false
                button.text = getString(R.string.play_remind_me)
                show()
            }
            Status.WATCH_NOW -> {
                button.isLoading = false
                button.text = getString(R.string.play_watch_now)
                show()
            }
            Status.HIDDEN -> {
                hide()
            }
            Status.LOADING -> {
                show()
                button.isLoading = true
            }
        }
    }

    enum class Status {
        REMIND_ME, WATCH_NOW, HIDDEN, LOADING
    }

    interface Listener {
        fun onClickActionButton()
    }
}