package com.tokopedia.tokofood.common.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

data class RemoveCartTokofoodParamNew (
    @SerializedName("business_data")
    val businessData: List<RemoveCartTokofoodBusinessData> = listOf()
) {

    fun getIsCartIdsEmpty(): Boolean {
        return businessData.firstOrNull()?.cartGroups?.firstOrNull()?.cartIds.isNullOrEmpty()
    }

    companion object {
        @JvmStatic
        fun getProductParamById(cartId: String): RemoveCartTokofoodParamNew {
            return RemoveCartTokofoodParamNew(
                businessData = listOf(
                    RemoveCartTokofoodBusinessData(
                        // TODO: Add BusinessId
                        businessId = "",
                        cartGroups = listOf(
                            RemoveCartTokofoodCartGroup(
                                cartGroupId = String.EMPTY,
                                cartIds = listOf(cartId)
                            )
                        )
                    )
                )
            )
        }
    }

}

data class RemoveCartTokofoodBusinessData(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("cart_groups")
    val cartGroups: List<RemoveCartTokofoodCartGroup> = listOf()
)

data class RemoveCartTokofoodCartGroup(
    @SerializedName("cart_group_id")
    val cartGroupId: String = String.EMPTY,
    @SerializedName("cart_ids")
    val cartIds: List<String> = listOf()
)
