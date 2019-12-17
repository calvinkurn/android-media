package com.tokopedia.home.beranda.presentation.view.helper

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.ViewParent
import androidx.annotation.FloatRange
import com.google.android.exoplayer2.ui.PlayerView

object ExoUtil {
    @FloatRange(from = 0.0, to = 1.0) //
    fun visibleAreaOffset(player: PlayerView, container: ViewParent?): Float {
        if (container == null) return 0.0f
        val playerView: View = player
        val drawRect = Rect()
        playerView.getDrawingRect(drawRect)
        val drawArea = drawRect.width() * drawRect.height()
        val playerRect = Rect()
        val visible = playerView.getGlobalVisibleRect(playerRect, Point())
        var offset = 0f
        if (visible && drawArea > 0) {
            val visibleArea = playerRect.height() * playerRect.width()
            offset = visibleArea / drawArea.toFloat()
        }
        return offset
    }
}