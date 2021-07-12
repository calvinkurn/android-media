package com.tokopedia.play.view.type

import com.tokopedia.play.view.uimodel.OpenApplinkUiModel

/**
 * Created by jegul on 02/12/20
 */
sealed class PiPMode {

    object WatchInPiP : PiPMode()
    data class BrowsingOtherPage(val applinkModel: OpenApplinkUiModel) : PiPMode()
}