package com.tokopedia.play.broadcaster.view.state

/**
 * Created by jegul on 28/05/20
 */
sealed class SelectableState

object Selectable : SelectableState()
data class NotSelectable(val reason: Throwable) : SelectableState()