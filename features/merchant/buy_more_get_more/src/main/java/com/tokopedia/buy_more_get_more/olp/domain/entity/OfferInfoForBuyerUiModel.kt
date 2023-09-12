package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.ShopData
import com.tokopedia.buy_more_get_more.olp.domain.entity.enum.Status
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactory
import com.tokopedia.buy_more_get_more.olp.utils.constant.Constant
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class OfferInfoForBuyerUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val offeringJsonData: String = "",
    val offerings: List<Offering> = emptyList(),
    val nearestWarehouseIds: List<Long> = emptyList()
) : Visitable<OlpAdapterTypeFactory> {
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
        val tnc: List<String> = emptyList()
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
                val value: Int = 0
            )
        }
    }

    override fun type(typeFactory: OlpAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class OlpUiState(
        val offerIds: List<Long> = emptyList(),
        val shopData: ShopData = ShopData(),
        val productIds: List<Long> = emptyList(),
        val warehouseIds: List<Long> = emptyList(),
        val tnc: List<String> = emptyList(),
        val localCacheModel: LocalCacheModel? = null,
        val offeringJsonData: String = "",
        val startDate: String = "",
        val endDate: String = "",
        val sortId: String = Constant.DEFAULT_SORT_ID,
        val sortName: String = Constant.DEFAULT_SORT_NAME
    )

    sealed class OlpEvent {
        data class SetInitialUiState(
            val offerIds: List<Long> = emptyList(),
            val shopIds: Long,
            val productIds: List<Long> = emptyList(),
            val warehouseIds: List<Long> = emptyList(),
            val localCacheModel: LocalCacheModel?
        ) : OlpEvent()

        object GetOfferingInfo : OlpEvent()

        data class GetOffreringProductList(val page: Int, val pageSize: Int) : OlpEvent()

        data class SetSort(val sortId: String, val sortName: String) : OlpEvent()

        data class SetWarehouseIds(val warehouseIds: List<Long>) : OlpEvent()

        data class SetShopData(val shopData: ShopData?) : OlpEvent()

        data class SetOfferingJsonData(val offeringJsonData: String) : OlpEvent()

        data class SetTncData(val tnc: List<String>) : OlpEvent()

        object GetNotification : OlpEvent()

        data class AddToCart(val product: OfferProductListUiModel.Product) : OlpEvent()
    }
}
