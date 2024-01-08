package com.tokopedia.editor.util.delegate

import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

interface ScalableCanvasViewDelegate {

    fun scalableCanvasRegister(activity: AppCompatActivity, canvas: View?)
}

internal class ScalableCanvasViewDelegateImpl : ScalableCanvasViewDelegate, FoldableSupportManager.Listener {

    private var canvasContainer: View? = null

    override fun scalableCanvasRegister(activity: AppCompatActivity, canvas: View?) {
        canvasContainer = canvas

        // register the adaptive layout with lifecycle-aware
        FoldableSupportManager(this, activity)
    }

    override fun onLayoutChanged(info: ScreenInfo) {
        if (info.isFoldableDevice()) {
            setLargeScreenMode()
        } else {
            setCompactMode()
        }
    }

    private fun setCompactMode() {
        val layoutParams = canvasContainer?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.dimensionRatio = "H,9:16"
        layoutParams.height = 0
        canvasContainer?.layoutParams = layoutParams
    }

    private fun setLargeScreenMode() {
        val layoutParams = canvasContainer?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.dimensionRatio = "W,9:16"
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT
        canvasContainer?.layoutParams = layoutParams
    }
}
