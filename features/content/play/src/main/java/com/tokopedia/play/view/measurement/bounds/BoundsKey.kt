package com.tokopedia.play.view.measurement.bounds

import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 05/08/20
 */
enum class BoundsKey {

    /**
     * {ScreenOrientation}{VideoOrientation}
     */
    PortraitVertical,
    PortraitHorizontal,
    LandscapeVertical,
    LandscapeHorizontal,
    Unknown;

    companion object {

        val values = values()

        fun getByOrientation(screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation): BoundsKey {
            return when {
                screenOrientation == ScreenOrientation.Portrait && videoOrientation == VideoOrientation.Vertical -> PortraitVertical
                screenOrientation == ScreenOrientation.Portrait && videoOrientation is VideoOrientation.Horizontal -> PortraitHorizontal
                screenOrientation == ScreenOrientation.Landscape && videoOrientation == VideoOrientation.Vertical -> LandscapeVertical
                screenOrientation == ScreenOrientation.Landscape && videoOrientation is VideoOrientation.Horizontal -> LandscapeHorizontal
                else -> Unknown
            }
        }
    }
}