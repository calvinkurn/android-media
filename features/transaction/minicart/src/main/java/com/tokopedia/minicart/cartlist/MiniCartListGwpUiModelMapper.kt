package com.tokopedia.minicart.cartlist

import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.cartcommon.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.minicart.cartlist.uimodel.MiniCartGwpGiftUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProgressiveInfoUiModel

object MiniCartListGwpUiModelMapper {
    private fun updateMiniCartProgressiveInfoUiModel(
        response: BmGmGetGroupProductTickerResponse,
        uiModel: MiniCartProgressiveInfoUiModel
    ): MiniCartProgressiveInfoUiModel? {
        return response.getGroupProductTicker.data.multipleData.find { data -> data.bmgmData.offerId == uiModel.offerId }?.run {
            uiModel.copy(
                message = bmgmData.offerMessage.firstOrNull().orEmpty(),
                icon = bmgmData.offerIcon,
                appLink = bmgmData.offerLandingPageLink,
                state = MiniCartProgressiveInfoUiModel.State.LOADED
            )
        }
    }

    private fun updateMiniCartProgressiveInfoUiModel(
        uiModel: MiniCartProgressiveInfoUiModel,
        state: MiniCartProgressiveInfoUiModel.State
    ): MiniCartProgressiveInfoUiModel {
        return uiModel.copy(
            message = String.EMPTY,
            icon = String.EMPTY,
            appLink = String.EMPTY,
            state = state
        )
    }

    private fun updateMiniCartGwpGiftUiModel(
        response: BmGmGetGroupProductTickerResponse,
        uiModel: MiniCartGwpGiftUiModel
    ): MiniCartGwpGiftUiModel {
        response.getGroupProductTicker.data.multipleData.find { data ->
            return data.bmgmData.tierProductList.first().run {
                uiModel.copy(
                    tierId = tierId,
                    ribbonText = benefitWording,
                    ctaText = actionWording,
                    giftList = productsBenefit.map { productBenefit ->
                        ProductGiftUiModel(
                            id = productBenefit.productId,
                            name = productBenefit.productName,
                            imageUrl = productBenefit.productImage,
                            qty = productBenefit.quantity,
                            isUnlocked = data.bmgmData.isTierAchieved
                        )
                    }
                )
            }
        }
        return uiModel.copy(giftList = emptyList())
    }

    fun getGwpSuccessState(
        uiModel: MiniCartListUiModel?,
        response: BmGmGetGroupProductTickerResponse
    ) = uiModel?.copy(
        visitables = uiModel.visitables.mapNotNull { uiModel ->
            when (uiModel) {
                is MiniCartProgressiveInfoUiModel -> updateMiniCartProgressiveInfoUiModel(
                    response = response,
                    uiModel = uiModel
                )
                is MiniCartGwpGiftUiModel -> updateMiniCartGwpGiftUiModel(
                    response = response,
                    uiModel = uiModel
                )
                else -> uiModel
            }
        }.toMutableList()
    )

    fun getGwpLoadingState(
        uiModel: MiniCartListUiModel?,
        offerId: Long
    ) = uiModel?.copy(
        visitables = uiModel.visitables.map { uiModel ->
            if (uiModel is MiniCartProgressiveInfoUiModel && uiModel.offerId == offerId) updateMiniCartProgressiveInfoUiModel(uiModel, MiniCartProgressiveInfoUiModel.State.LOADING) else uiModel
        }.toMutableList()
    )

    fun getGwpErrorState(
        uiModel: MiniCartListUiModel?,
        offerId: Long
    ) = uiModel?.copy(
        visitables = uiModel.visitables.map { uiModel ->
            if (uiModel is MiniCartProgressiveInfoUiModel && uiModel.offerId == offerId) updateMiniCartProgressiveInfoUiModel(uiModel, MiniCartProgressiveInfoUiModel.State.ERROR) else uiModel
        }.toMutableList()
    )
}
