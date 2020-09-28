package com.tokopedia.play.broadcaster.view.state

/**
 * Created by jegul on 26/06/20
 */
sealed class CoverChangeState

object Changeable : CoverChangeState()
data class NotChangeable(val reason: Throwable) : CoverChangeState()