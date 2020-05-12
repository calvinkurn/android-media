package com.tokopedia.play.ui.fragment.youtube.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 05/05/20
 */
sealed class FragmentYouTubeInteractionEvent : ComponentEvent {

    data class OnClicked(val isScaling: Boolean) : FragmentYouTubeInteractionEvent()
}