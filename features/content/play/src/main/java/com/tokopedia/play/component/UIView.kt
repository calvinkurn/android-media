package com.tokopedia.play.component

import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Created by jegul on 02/12/19
 */
abstract class UIView(val container: ViewGroup) {
    /**
     * Get the XML id for the IUIView
     */
    @get:IdRes
    abstract val containerId: Int

    /**
     * UIView Visibility State
     */
    abstract val isVisible: Boolean

    /**
     * Show the UIView
     */
    abstract fun show()

    /**
     * Hide the UIView
     */
    abstract fun hide()
}