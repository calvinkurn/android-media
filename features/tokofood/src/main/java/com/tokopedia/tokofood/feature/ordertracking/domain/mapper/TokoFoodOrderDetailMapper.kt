package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.AddonVariantItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.MerchantDataUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ToolbarLiveTrackingUiModel
import javax.inject.Inject

open class TokoFoodOrderDetailMapper @Inject constructor(
    private val orderLiveTrackingMapper: ITokoFoodOrderLiveTrackingMapper,
    private val orderCompletedMapper: ITokoFoodOrderCompletedMapper
) {

    fun mapToOrderDetailResultUiModel(orderDetailResponse: TokoFoodOrderDetailResponse.TokofoodOrderDetail): OrderDetailResultUiModel {
        val orderStatus = orderDetailResponse.orderStatus.status

        val orderDetailList = when (orderStatus) {
            in listOf(OrderStatusType.CANCELLED, OrderStatusType.COMPLETED) -> {
                orderCompletedMapper.mapToOrderDetailList(orderDetailResponse)
            }
            else -> {
                orderLiveTrackingMapper.mapToOrderDetailList(orderDetailResponse)
            }
        }

        val foodItems = mapFoodItemListUiModel(orderDetailResponse.items)

        return OrderDetailResultUiModel(
            orderDetailList,
            foodItems,
            mapMerchantToMerchantDataUiModel(orderDetailResponse.merchant),
            orderStatus,
            mapToActionButtons(orderDetailResponse),
            mapToToolbarLiveTrackingUiModel(orderDetailResponse)
        )
    }

    private fun mapMerchantToMerchantDataUiModel(merchant: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Merchant): MerchantDataUiModel {
        return MerchantDataUiModel(merchant.merchantId, merchant.displayName)
    }

    private fun mapFoodItemListUiModel(
        foodList:
            List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.Item>
    ): List<BaseOrderTrackingTypeFactory> {
        return mutableListOf<BaseOrderTrackingTypeFactory>().apply {
            addAll(
                foodList.map {
                    FoodItemUiModel(
                        cartId = it.cartId,
                        categoryId = it.categoryId,
                        categoryName = it.categoryName,
                        itemId = it.itemId,
                        foodName = it.displayName,
                        quantity = it.quantity,
                        priceStr = it.price,
                        notes = it.notes.orEmpty(),
                        addOnVariantList = mapToAddonVariantUiModel(it)
                    )
                }
            )
        }
    }

    private fun mapToAddonVariantUiModel(
        foodItem:
            TokoFoodOrderDetailResponse.TokofoodOrderDetail.Item
    ): List<AddonVariantItemUiModel> {
        return foodItem.variants?.map {
            AddonVariantItemUiModel(displayName = it.displayName, optionName = it.optionName)
        }.orEmpty()
    }

    private fun mapToActionButtons(orderDetailResponse: TokoFoodOrderDetailResponse.TokofoodOrderDetail): ActionButtonsUiModel {
        val primaryActionButton = orderDetailResponse.actionButtons.firstOrNull()
        val secondaryActionButtons = orderDetailResponse.dotMenus
        return ActionButtonsUiModel(
            primaryActionButton = ActionButtonsUiModel.ActionButton(
                label = primaryActionButton?.label.orEmpty(),
                appUrl = primaryActionButton?.appUrl.orEmpty(),
                type = primaryActionButton?.actionType.orEmpty()
            ),
            secondaryActionButton = secondaryActionButtons?.map {
                ActionButtonsUiModel.ActionButton(
                    label = it.label,
                    appUrl = it.appUrl,
                    type = it.actionType
                )
            }.orEmpty()
        )
    }

    private fun mapToToolbarLiveTrackingUiModel(
        orderDetailResponse:
            TokoFoodOrderDetailResponse.TokofoodOrderDetail
    ) = ToolbarLiveTrackingUiModel(
        merchantName = orderDetailResponse.merchant.displayName,
        orderStatusTitle = orderDetailResponse.orderStatus.title,
        composeEstimation = if (orderDetailResponse.eta != null) {
            StringBuilder().apply {
                append(orderDetailResponse.eta.label)
                append(" ")
                append(orderDetailResponse.eta.time)
            }.toString()
        } else {
            ""
        }
    )
}
