package com.tokopedia.play.view.measurement.bounds.provider.videobounds

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/08/20
 */
class PlayVideoBoundsProvider(
        private val container: ViewGroup,
        private val dataSource: ScreenOrientationDataSource
) : VideoBoundsProvider {

    private lateinit var portraitVideoBoundsProvider: PortraitVideoBoundsProvider
    private lateinit var landscapeVideoBoundsProvider: LandscapeVideoBoundsProvider

    override suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().getVideoTopBounds(videoOrientation)
        else getPortraitManager().getVideoTopBounds(videoOrientation)
    }

    override suspend fun getVideoBottomBoundsOnKeyboardShown(
        view: View,
        estimatedKeyboardHeight: Int,
        videoOrientation: VideoOrientation
    ): Int {
        return if (dataSource.getScreenOrientation().isLandscape) {
            getLandscapeManager().getVideoBottomBoundsOnKeyboardShown(
                view,
                estimatedKeyboardHeight,
                videoOrientation,
            )
        }
        else {
            getPortraitManager().getVideoBottomBoundsOnKeyboardShown(
                view,
                estimatedKeyboardHeight,
                videoOrientation,
            )
        }
    }

    /**
     * Getter
     */
    private fun getPortraitManager(): VideoBoundsProvider = synchronized(this) {
        if (!::portraitVideoBoundsProvider.isInitialized) {
            portraitVideoBoundsProvider = PortraitVideoBoundsProvider(container = container)
        }
        return portraitVideoBoundsProvider
    }

    private fun getLandscapeManager(): VideoBoundsProvider = synchronized(this) {
        if (!::landscapeVideoBoundsProvider.isInitialized) {
            landscapeVideoBoundsProvider = LandscapeVideoBoundsProvider()
        }
        return landscapeVideoBoundsProvider
    }
}