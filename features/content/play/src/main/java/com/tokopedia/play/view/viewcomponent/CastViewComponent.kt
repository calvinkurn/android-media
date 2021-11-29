package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent


/**
 * Created by jegul on 08/06/21
 */
class CastViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.btn_play_cast) {

    private val btnCast = rootView as MediaRouteButton

    init {
        CastButtonFactory.setUpMediaRouteButton(btnCast.context, btnCast)
        btnCast.setOnClickListener {
            listener.onCastClicked()
        }
    }

    interface Listener {
        fun onCastClicked()
    }
}