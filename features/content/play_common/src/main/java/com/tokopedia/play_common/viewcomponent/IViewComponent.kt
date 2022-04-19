package com.tokopedia.play_common.viewcomponent

import android.view.View
import androidx.lifecycle.LifecycleObserver

/**
 * Created by jegul on 31/07/20
 */
interface IViewComponent : LifecycleObserver {

    val id: Int

    val rootView: View
}