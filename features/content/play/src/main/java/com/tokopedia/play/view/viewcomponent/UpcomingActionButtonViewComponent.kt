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
        setButtonStatus(Status.LOADING)
        listener.onClickActionButton()
    }

    fun setButtonStatus(status: Status) {
        when(status) {
            Status.REMIND_ME -> {
                button.isLoading = false
                button.text = getString(R.string.play_remind_me)
                button.buttonVariant = UnifyButton.Variant.FILLED
                button.buttonType = UnifyButton.Type.MAIN
                button.isInverse = false
                show()
            }
            Status.WATCH_NOW -> {
                button.isLoading = false
                button.text = getString(R.string.play_watch_now)
                button.buttonVariant = UnifyButton.Variant.FILLED
                button.buttonType = UnifyButton.Type.MAIN
                button.isInverse = false
                show()
            }
            Status.HIDDEN -> {
                invisible()
            }
            Status.REFRESH -> {
                button.isLoading = false
                button.text = getString(R.string.play_upcoming_refresh)
                button.buttonVariant = UnifyButton.Variant.GHOST
                button.buttonType = UnifyButton.Type.ALTERNATE
                button.isInverse = true
                show()
            }
            Status.LOADING -> {
                show()
                button.isLoading = true
            }
        }
    }

    enum class Status {
        REMIND_ME, WATCH_NOW, HIDDEN, REFRESH, LOADING
    }

    interface Listener {
        fun onClickActionButton()
    }
}