package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel

object ActionButtonsUiStateMapper {

    fun map(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        currentState: ActionButtonsUiState
    ): ActionButtonsUiState {
        val getBuyerOrderDetailRequestState = getBuyerOrderDetailDataRequestState
            .getP0DataRequestState
            .getBuyerOrderDetailRequestState
        return when (getBuyerOrderDetailRequestState) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnGetBuyerOrderDetailRequesting(currentState)
            }
            is GetBuyerOrderDetailRequestState.Complete.Error -> {
                mapOnGetBuyerOrderDetailError(getBuyerOrderDetailRequestState.throwable)
            }
            is GetBuyerOrderDetailRequestState.Complete.Success -> {
                mapOnGetBuyerOrderDetailSuccess(getBuyerOrderDetailRequestState.result)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailRequesting(
        currentState: ActionButtonsUiState
    ): ActionButtonsUiState {
        return if (currentState is ActionButtonsUiState.HasData) {
            mapOnReloading(currentState)
        } else {
            mapOnLoading()
        }
    }

    private fun mapOnGetBuyerOrderDetailSuccess(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): ActionButtonsUiState {
        return mapOnDataReady(buyerOrderDetailData)
    }

    private fun mapOnGetBuyerOrderDetailError(
        throwable: Throwable?
    ): ActionButtonsUiState {
        return mapOnError(throwable)
    }

    private fun mapOnLoading(): ActionButtonsUiState {
        return ActionButtonsUiState.Loading
    }

    private fun mapOnReloading(
        currentState: ActionButtonsUiState.HasData
    ): ActionButtonsUiState {
        return ActionButtonsUiState.HasData.Reloading(currentState.data)
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): ActionButtonsUiState {
        return ActionButtonsUiState.HasData.Showing(
            mapActionButtons(buyerOrderDetailData.button, buyerOrderDetailData.dotMenu)
        )
    }

    private fun mapOnError(
        throwable: Throwable?
    ): ActionButtonsUiState {
        return ActionButtonsUiState.Error(throwable)
    }

    private fun mapActionButtons(
        button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button,
        dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>
    ): ActionButtonsUiModel {
        return ActionButtonsUiModel(
            primaryActionButton = mapActionButton(button),
            secondaryActionButtons = mapSecondaryActionButtons(dotMenu)
        )
    }

    private fun mapActionButton(
        button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button?
    ): ActionButtonsUiModel.ActionButton {
        return ActionButtonsUiModel.ActionButton(
            key = button?.key.orEmpty(),
            label = button?.displayName.orEmpty(),
            popUp = mapPopUp(button?.popup),
            variant = button?.variant.orEmpty(),
            type = button?.type.orEmpty(),
            url = button?.url.orEmpty()
        )
    }

    private fun mapSecondaryActionButtons(
        dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>
    ): List<ActionButtonsUiModel.ActionButton> {
        return dotMenu.map {
            ActionButtonsUiModel.ActionButton(
                key = it.key,
                label = it.displayName,
                popUp = mapPopUp(it.popup),
                variant = "",
                type = "",
                url = it.url
            )
        }
    }

    private fun mapPopUp(
        popup: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup?
    ): ActionButtonsUiModel.ActionButton.PopUp {
        return ActionButtonsUiModel.ActionButton.PopUp(
            actionButton = mapPopUpButtons(popup?.actionButton.orEmpty()),
            body = popup?.body.orEmpty(),
            title = popup?.title.orEmpty()
        )
    }

    private fun mapPopUpButtons(
        popUpButtons: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton>
    ): List<ActionButtonsUiModel.ActionButton.PopUp.PopUpButton> {
        return popUpButtons.map {
            mapPopUpButton(it)
        }
    }

    private fun mapPopUpButton(
        popUpButton: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton
    ): ActionButtonsUiModel.ActionButton.PopUp.PopUpButton {
        return ActionButtonsUiModel.ActionButton.PopUp.PopUpButton(
            key = popUpButton.key,
            displayName = popUpButton.displayName,
            color = popUpButton.color,
            type = popUpButton.type,
            uriType = popUpButton.uriType,
            uri = popUpButton.uri
        )
    }
}
