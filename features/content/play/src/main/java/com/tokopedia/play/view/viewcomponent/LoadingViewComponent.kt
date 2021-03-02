package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 29/01/21
 */
class LoadingViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes)