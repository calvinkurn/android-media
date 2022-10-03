package com.tokopedia.buyerorderdetail.presentation.mapper

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState

object ActionButtonsUiStateMapper {

    private fun mapActionButtons(
        button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button,
        dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>
    ): ActionButtonsUiModel {
        return ActionButtonsUiModel(
            primaryActionButton = mapActionButton(button),
            secondaryActionButtons = mapSecondaryActionButtons(dotMenu)
        )
    }

    private fun mapActionButton(button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button?): ActionButtonsUiModel.ActionButton {
        return ActionButtonsUiModel.ActionButton(
            key = button?.key.orEmpty(),
            label = button?.displayName.orEmpty(),
            popUp = mapPopUp(button?.popup),
            variant = button?.variant.orEmpty(),
            type = button?.type.orEmpty(),
            url = button?.url.orEmpty()
        )
    }

    private fun mapSecondaryActionButtons(dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>): List<ActionButtonsUiModel.ActionButton> {
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

    private fun mapPopUp(popup: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup?): ActionButtonsUiModel.ActionButton.PopUp {
        return ActionButtonsUiModel.ActionButton.PopUp(
            actionButton = mapPopUpButtons(popup?.actionButton.orEmpty()),
            body = popup?.body.orEmpty(),
            title = popup?.title.orEmpty()
        )
    }

    private fun mapPopUpButtons(popUpButtons: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton>): List<ActionButtonsUiModel.ActionButton.PopUp.PopUpButton> {
        return popUpButtons.map {
            mapPopUpButton(it)
        }
    }

    private fun mapPopUpButton(popUpButton: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton): ActionButtonsUiModel.ActionButton.PopUp.PopUpButton {
        return ActionButtonsUiModel.ActionButton.PopUp.PopUpButton(
            key = popUpButton.key,
            displayName = popUpButton.displayName,
            color = popUpButton.color,
            type = popUpButton.type,
            uriType = popUpButton.uriType,
            uri = popUpButton.uri
        )
    }

    fun mapGetP0DataRequestStateToActionButtonsUiState(
        getP0DataRequestState: GetP0DataRequestState
    ): ActionButtonsUiState {
        return when (getP0DataRequestState) {
            is GetP0DataRequestState.Requesting -> {
                when (val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState) {
                    is GetBuyerOrderDetailRequestState.Requesting -> {
                        ActionButtonsUiState.Loading
                    }
                    is GetBuyerOrderDetailRequestState.Success -> {
                        ActionButtonsUiState.Showing(
                            mapActionButtons(
                                getBuyerOrderDetailRequestState.result.button,
                                getBuyerOrderDetailRequestState.result.dotMenu
                            )
                        )
                    }
                    is GetBuyerOrderDetailRequestState.Error -> {
                        ActionButtonsUiState.Error(getBuyerOrderDetailRequestState.throwable)
                    }
                }
            }
            is GetP0DataRequestState.Success -> {
                val getBuyerOrderDetailRequestState = getP0DataRequestState.getBuyerOrderDetailRequestState
                ActionButtonsUiState.Showing(
                    mapActionButtons(
                        getBuyerOrderDetailRequestState.result.button,
                        getBuyerOrderDetailRequestState.result.dotMenu
                    )
                )
            }
            else -> {
                ActionButtonsUiState.Loading
            }
        }
    }
}
