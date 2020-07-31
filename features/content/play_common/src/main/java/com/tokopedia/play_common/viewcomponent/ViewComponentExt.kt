package com.tokopedia.play_common.viewcomponent

import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by jegul on 31/07/20
 */
fun <VC: IViewComponent> Fragment.viewComponent(creator: (ViewGroup) -> VC) : ViewComponentDelegate<VC> {
    return ViewComponentDelegate(creator)
}