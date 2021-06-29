package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 18/02/21
 */
class BottomActionNextViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.bottom_action_next) {

    private val btnNext: UnifyButton = findViewById(R.id.btn_next)

    init {
        btnNext.setOnClickListener {
            listener.onNextButtonClicked(this)
        }
    }

    fun setEnabled(isEnabled: Boolean) {
        btnNext.isEnabled = isEnabled
        btnNext.isClickable = isEnabled
    }

    fun setLoading(isLoading: Boolean) {
        btnNext.isLoading = isLoading
    }

    interface Listener {

        fun onNextButtonClicked(view: BottomActionNextViewComponent)
    }
}