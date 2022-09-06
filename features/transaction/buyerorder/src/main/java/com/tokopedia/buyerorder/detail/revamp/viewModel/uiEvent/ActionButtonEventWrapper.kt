package com.tokopedia.buyerorder.detail.revamp.viewModel.uiEvent

import com.tokopedia.buyerorder.detail.data.ActionButton

/**
 * created by @bayazidnasir on 25/8/2022
 */

sealed class ActionButtonEventWrapper{
    data class TapActionButton(val position: Int, val list: List<ActionButton>): ActionButtonEventWrapper()
    data class SetActionButton(val position: Int, val list: List<ActionButton>): ActionButtonEventWrapper()
    data class RenderActionButton(val list: List<ActionButton>): ActionButtonEventWrapper()
}
