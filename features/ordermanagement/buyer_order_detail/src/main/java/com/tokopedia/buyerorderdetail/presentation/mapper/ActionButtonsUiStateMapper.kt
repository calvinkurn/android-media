package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetP1DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState

object ActionButtonsUiStateMapper {

    private fun mapOnGetBuyerOrderDetailDataStarted(
        buyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState.Started
    ): ActionButtonsUiState {
        val p1DataRequestState = buyerOrderDetailDataRequestState.getP1DataRequestState
        return when (val p0DataRequestState = buyerOrderDetailDataRequestState.getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                mapOnP0Requesting(p0DataRequestState, p1DataRequestState)
            }
            is GetP0DataRequestState.Success -> {
                mapOnP0Success(p0DataRequestState, p1DataRequestState)
            }
            is GetP0DataRequestState.Error -> {
                mapOnP0Error(p0DataRequestState)
            }
        }
    }

    private fun mapOnGetBuyerOrderDetailIdling(): ActionButtonsUiState {
        return mapOnLoading()
    }

    private fun mapOnP0Requesting(
        p0DataRequestState: GetP0DataRequestState.Requesting,
        p1DataRequestState: GetP1DataRequestState
    ): ActionButtonsUiState {
        return when (
            val getBuyerOrderDetailRequestState = p0DataRequestState.getBuyerOrderDetailRequestState
        ) {
            is GetBuyerOrderDetailRequestState.Requesting -> {
                mapOnLoading()
            }
            is GetBuyerOrderDetailRequestState.Success -> {
                mapOnGetBuyerOrderDetailIsSuccess(
                    getBuyerOrderDetailRequestState,
                    p1DataRequestState
                )
            }
            is GetBuyerOrderDetailRequestState.Error -> {
                mapOnError(getBuyerOrderDetailRequestState.throwable)
            }
        }
    }

    private fun mapOnP0Success(
        p0DataRequestState: GetP0DataRequestState.Success,
        p1DataRequestState: GetP1DataRequestState
    ): ActionButtonsUiState {
        return mapOnGetBuyerOrderDetailIsSuccess(
            p0DataRequestState.getBuyerOrderDetailRequestState,
            p1DataRequestState
        )
    }

    private fun mapOnP0Error(
        p0DataRequestState: GetP0DataRequestState.Error
    ): ActionButtonsUiState {
        return mapOnError(p0DataRequestState.getThrowable())
    }

    private fun mapOnGetBuyerOrderDetailIsSuccess(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState
    ): ActionButtonsUiState {
        return when (p1DataRequestState) {
            is GetP1DataRequestState.Requesting -> {
                mapOnP1Requesting(buyerOrderDetailRequestState, p1DataRequestState)
            }
            is GetP1DataRequestState.Complete -> {
                mapOnP1Complete(buyerOrderDetailRequestState)
            }
        }
    }

    private fun mapOnP1Requesting(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
        p1DataRequestState: GetP1DataRequestState.Requesting
    ): ActionButtonsUiState {
        return when (p1DataRequestState.getOrderResolutionRequestState) {
            is GetOrderResolutionRequestState.Requesting -> {
                mapOnLoading()
            }
            else -> {
                mapOnDataReady(buyerOrderDetailRequestState.result)
            }
        }
    }

    private fun mapOnP1Complete(
        buyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success
    ): ActionButtonsUiState {
        return mapOnDataReady(buyerOrderDetailRequestState.result)
    }

    private fun mapOnLoading(): ActionButtonsUiState {
        return ActionButtonsUiState.Loading
    }

    private fun mapOnDataReady(
        buyerOrderDetailData: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): ActionButtonsUiState {
        return ActionButtonsUiState.Showing(
            mapActionButtons(buyerOrderDetailData.button, buyerOrderDetailData.dotMenu)
        )
    }

    private fun mapOnError(
        throwable: Throwable
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

    fun mapGetBuyerOrderDetailDataRequestStateToActionButtonsUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState
    ): ActionButtonsUiState {
        return when (getBuyerOrderDetailDataRequestState) {
            is GetBuyerOrderDetailDataRequestState.Started -> {
                mapOnGetBuyerOrderDetailDataStarted(getBuyerOrderDetailDataRequestState)
            }
            else -> {
                mapOnGetBuyerOrderDetailIdling()
            }
        }
    }
}
