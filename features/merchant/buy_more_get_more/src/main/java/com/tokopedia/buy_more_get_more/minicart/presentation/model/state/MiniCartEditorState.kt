package com.tokopedia.buy_more_get_more.minicart.presentation.model.state

import com.tokopedia.bmsm_widget.presentation.model.GiftWidgetState
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

data class MiniCartEditorState(
    val state: State = State.LOADING,
    val data: BmgmMiniCartDataUiModel = BmgmMiniCartDataUiModel(),
    val throwable: Throwable? = null
) {

    enum class State { LOADING, PARTIALLY_LOADING, ERROR, SUCCESS }

    fun updateError(throwable: Throwable): MiniCartEditorState {
        return this.copy(state = State.ERROR, throwable = throwable)
    }

    fun updateSuccess(data: BmgmMiniCartDataUiModel): MiniCartEditorState {
        return this.copy(state = State.SUCCESS, data = data)
    }

    fun updateLoading(): MiniCartEditorState {
        val tiers = getTiersWithState(GiftWidgetState.SUCCESS)
        return this.copy(state = State.LOADING, throwable = null, data = data.copy(tiers = tiers))
    }

    fun updatePartiallyLoading(): MiniCartEditorState {
        val tiers = getTiersWithState(GiftWidgetState.LOADING)
        return this.copy(
            state = State.PARTIALLY_LOADING,
            throwable = null,
            data = data.copy(tiers = tiers)
        )
    }

    fun dismissPartiallyLoading(): MiniCartEditorState {
        val tiers = getTiersWithState(GiftWidgetState.SUCCESS)
        return this.copy(
            state = State.SUCCESS,
            throwable = null,
            data = data.copy(tiers = tiers)
        )
    }

    private fun getTiersWithState(state: GiftWidgetState): List<BmgmMiniCartVisitable> {
        return data.tiers.map {
            if (it is BmgmMiniCartVisitable.GwpGiftWidgetUiModel) {
                return@map it.copy(state = state)
            }
            return@map it
        }
    }
}