package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent


/**
 * Created by mzennis on 19/11/20.
 */
class PiPViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        private val listener: Listener
) : ViewComponent(container, idRes) {

    private val ivPipControl: ImageView = findViewById(R.id.iv_pip_control)

    init {
        rootView.setOnClickListener {
            listener.onPiPButtonClicked(this)
        }
    }

    interface Listener {
        fun onPiPButtonClicked(view: PiPViewComponent)
    }
}