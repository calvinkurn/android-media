package com.tokopedia.editor.util.delegate

import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.foldable.FoldableInfo
import com.tokopedia.foldable.FoldableSupportManager

interface ScalableCanvasViewDelegate {

    fun scalableCanvasRegister(activity: AppCompatActivity, canvas: RelativeLayout?)
}

internal class ScalableCanvasViewDelegateImpl : ScalableCanvasViewDelegate, FoldableSupportManager.FoldableInfoCallback {

    private var canvasContainer: RelativeLayout? = null

    override fun scalableCanvasRegister(activity: AppCompatActivity, canvas: RelativeLayout?) {
        canvasContainer = canvas

        FoldableSupportManager(this, activity)
    }

    override fun onChangeLayout(foldableInfo: FoldableInfo) {
        if (foldableInfo.isFoldableDevice()) {
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
