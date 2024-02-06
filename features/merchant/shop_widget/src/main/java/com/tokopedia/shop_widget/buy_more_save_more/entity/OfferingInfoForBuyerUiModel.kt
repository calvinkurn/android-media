package com.tokopedia.shop_widget.buy_more_save_more.entity

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.shop_widget.buy_more_save_more.entity.enums.Status

data class OfferingInfoForBuyerUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val offeringJsonData: String = "",
    val offerings: List<Offering> = emptyList(),
    val nearestWarehouseIds: List<Long> = emptyList()
) {
    data class ResponseHeader(
        val success: Boolean = true,
        val status: Status = Status.SUCCESS,
        val processTime: String = ""
    )

    data class Offering(
        val id: Long = 0,
        val offerName: String = "",
        val offerTypeId: Long = 0,
        val startDate: String = "",
        val endDate: String = "",
        val maxAppliedTier: Int = 0,
        val tierList: List<Tier> = emptyList(),
        val shopData: ShopData = ShopData(),
        val tnc: List<String> = emptyList(),
        val olpLink: String = "",
        val olpAppLink: String = "",
        val upsellWording: String = ""
    ) {
        data class ShopData(
            val shopId: Long = 0,
            val shopName: String = "",
            val badge: String = ""
        )
        data class Tier(
            val tierId: Long = 0,
            val level: Int = 0,
            val tierWording: String = "",
            val rules: List<Rule> = emptyList(),
            val benefits: List<Benefit> = emptyList(),
            val attributes: String = ""
        ) {
            data class Rule(
                val typeId: Long = 0,
                val operation: String = "",
                val value: Int = 0
            )

            data class Benefit(
                val typeId: Long = 0,
                val value: Int = 0,
                val products: List<ProductBenefit>
            ) {
                data class ProductBenefit(
                    val productId: Long = 0,
                    val image: String = "",
                    val priority: Int = 0
                )
            }
        }
    }

    data class BmsmWidgetUiState(
        val isNeedToRefreshOfferingData: Boolean = true,
        val isShowLoading: Boolean = true,
        val offerIds: List<Long> = emptyList(),
        val shopId: Long = 0,
        val offeringInfo: OfferingInfoForBuyerUiModel = OfferingInfoForBuyerUiModel(),
        val miniCartData: MiniCartSimplifiedData = MiniCartSimplifiedData(),
        val currentAppliedId: Long = 0,
        val isUpdateGiftImage: Boolean = false,
        val localCacheModel: LocalCacheModel? = null
    )

    sealed class BmsmWidgetEvent {
        data class SetInitialUiState(
            val offerIds: List<Long> = emptyList(),
            val offerTypeId: Long = 0,
            val shopIds: Long,
            val productIds: List<Long> = emptyList(),
            val warehouseIds: List<Long> = emptyList(),
            val localCacheModel: LocalCacheModel?
        ) : BmsmWidgetEvent()

        object GetOfferingInfo : BmsmWidgetEvent()

        data class GetOffreringProductList(val page: Int, val pageSize: Int) : BmsmWidgetEvent()

        data class SetSort(val sortId: String, val sortName: String) : BmsmWidgetEvent()

        data class SetWarehouseIds(val warehouseIds: List<Long>) : BmsmWidgetEvent()

        data class SetOfferingJsonData(val offeringJsonData: String) : BmsmWidgetEvent()

        data class SetTncData(val tnc: List<String>) : BmsmWidgetEvent()

        data class SetEndDate(val endDate: String) : BmsmWidgetEvent()

        object GetNotification : BmsmWidgetEvent()

        object GetSharingData : BmsmWidgetEvent()

        data class AddToCart(val product: Product) : BmsmWidgetEvent()

        data class SetOfferTypeId(val offerTypeId: Long) : BmsmWidgetEvent()
    }
}
