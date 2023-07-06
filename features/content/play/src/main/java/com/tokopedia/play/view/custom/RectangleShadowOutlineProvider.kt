package com.tokopedia.play.view.custom

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * @author by astidhiyaa on 11/12/21
 */
class RectangleShadowOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        outline?.setRect(0, 0, view?.width ?: 0, view?.height ?: 0)
    }
}
