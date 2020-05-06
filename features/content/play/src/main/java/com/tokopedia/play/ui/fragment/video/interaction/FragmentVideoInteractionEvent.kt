package com.tokopedia.play.ui.fragment.video.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 05/05/20
 */
sealed class FragmentVideoInteractionEvent : ComponentEvent {

    object OnClicked : FragmentVideoInteractionEvent()
}