package com.tokopedia.play.ui.videosettings.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 14/04/20
 */
sealed class VideoSettingsInteractionEvent : ComponentEvent {

    object EnterFullscreenClicked : VideoSettingsInteractionEvent()
    object ExitFullscreenClicked : VideoSettingsInteractionEvent()
}