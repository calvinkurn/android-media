package com.tokopedia.tokofood.common.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class RemoveCartTokofoodParam (
    @SerializedName("business_data")
    val businessData: List<RemoveCartTokofoodBusinessData> = listOf()
) {

    fun getIsCartIdsEmpty(): Boolean {
        return businessData.firstOrNull()?.cartGroups?.firstOrNull()?.cartIds.let {
            it.isNullOrEmpty() || it.all { cartId -> cartId.isBlank() }
        }
    }

    companion object {
        @JvmStatic
        fun getProductParamById(cartId: String): RemoveCartTokofoodParam {
            return RemoveCartTokofoodParam(
                businessData = listOf(
                    RemoveCartTokofoodBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
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
