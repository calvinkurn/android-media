package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.view.custom.PlayUnifyLoader
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 29/01/21
 */
class LoadingViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val ivLoading = rootView as PlayUnifyLoader

    override fun hide() {
        super.hide()
        ivLoading.pause()
    }

    override fun show() {
        ivLoading.start()
        super.show()
    }
}