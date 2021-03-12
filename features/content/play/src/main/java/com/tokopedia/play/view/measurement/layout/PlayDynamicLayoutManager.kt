package com.tokopedia.play.view.measurement.layout

import android.view.ViewGroup
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel

/**
 * Created by jegul on 04/08/20
 */
class PlayDynamicLayoutManager(
        private val container: ViewGroup,
        private val dataSource: ScreenOrientationDataSource
) : DynamicLayoutManager {

    private lateinit var portraitDynamicLayoutManager: PortraitDynamicLayoutManager
    private lateinit var landscapeDynamicLayoutManager: LandscapeDynamicLayoutManager

    override fun onVideoOrientationChanged(videoOrientation: VideoOrientation) {
        if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().onVideoOrientationChanged(videoOrientation)
        else getPortraitManager().onVideoOrientationChanged(videoOrientation)
    }

    override fun onVideoPlayerChanged(videoPlayer: PlayVideoPlayerUiModel, channelType: PlayChannelType) {
        if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().onVideoPlayerChanged(videoPlayer, channelType)
        else getPortraitManager().onVideoPlayerChanged(videoPlayer, channelType)
    }

    /**
     * Getter
     */
    private fun getPortraitManager(): DynamicLayoutManager = synchronized(this) {
        if (!::portraitDynamicLayoutManager.isInitialized) {
            portraitDynamicLayoutManager = PortraitDynamicLayoutManager(container)
        }
        return portraitDynamicLayoutManager
    }

    private fun getLandscapeManager(): DynamicLayoutManager = synchronized(this) {
        if (!::landscapeDynamicLayoutManager.isInitialized) {
            landscapeDynamicLayoutManager = LandscapeDynamicLayoutManager()
        }
        return landscapeDynamicLayoutManager
    }
}