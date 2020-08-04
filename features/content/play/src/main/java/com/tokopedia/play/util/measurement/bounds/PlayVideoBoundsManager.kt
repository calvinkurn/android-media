package com.tokopedia.play.util.measurement.bounds

import android.view.ViewGroup
import com.tokopedia.play.util.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/08/20
 */
class PlayVideoBoundsManager(
        private val container: ViewGroup,
        private val dataSource: ScreenOrientationDataSource
) : VideoBoundsManager {

    private lateinit var portraitVideoBoundsManager: PortraitVideoBoundsManager
    private lateinit var landscapeVideoBoundsManager: LandscapeVideoBoundsManager

    override suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().getVideoTopBounds(videoOrientation)
        else getPortraitManager().getVideoTopBounds(videoOrientation)
    }

    override suspend fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        return if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight, hasQuickReply)
        else getPortraitManager().getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight, hasQuickReply)
    }

    /**
     * Getter
     */
    private fun getPortraitManager(): VideoBoundsManager = synchronized(this) {
        if (!::portraitVideoBoundsManager.isInitialized) {
            portraitVideoBoundsManager = PortraitVideoBoundsManager(container = container)
        }
        return portraitVideoBoundsManager
    }

    private fun getLandscapeManager(): VideoBoundsManager = synchronized(this) {
        if (!::landscapeVideoBoundsManager.isInitialized) {
            landscapeVideoBoundsManager = LandscapeVideoBoundsManager()
        }
        return landscapeVideoBoundsManager
    }
}