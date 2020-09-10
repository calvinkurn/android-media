package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 31/07/20
 */
class EmptyViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes)