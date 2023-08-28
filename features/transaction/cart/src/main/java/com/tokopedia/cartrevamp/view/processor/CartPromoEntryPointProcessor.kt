package com.tokopedia.cartrevamp.view.processor

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.mapper.PromoRequestMapper
import com.tokopedia.cartrevamp.view.uimodel.CartModel
import com.tokopedia.cartrevamp.view.uimodel.EntryPointInfoEvent
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.usecase.PromoUsageGetPromoListRecommendationEntryPointUseCase
import com.tokopedia.promousage.view.mapper.PromoUsageGetPromoListRecommendationMapper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import javax.inject.Inject

class CartPromoEntryPointProcessor @Inject constructor(
    private val getPromoListRecommendationEntryPointUseCase: PromoUsageGetPromoListRecommendationEntryPointUseCase,
    private val getCouponListRecommendationMapper: PromoUsageGetPromoListRecommendationMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) {

    private fun generatePromoRequest(
        cartModel: CartModel,
        cartDataList: ArrayList<Any>
    ): PromoRequest {
        return when {
            cartModel.isLastApplyResponseStillValid -> {
                val lastApplyPromo =
                    cartModel.cartListData?.promo?.lastApplyPromo ?: LastApplyPromo()
                PromoRequestMapper.generateCouponListRequestParams(
                    lastApplyPromo,
                    CartDataHelper.getAllAvailableShopGroupDataList(cartDataList),
                    null
                )
            }

            cartModel.lastValidateUseResponse != null -> {
                val promoUiModel =
                    cartModel.lastValidateUseResponse?.promoUiModel ?: PromoUiModel()
                PromoRequestMapper.generateCouponListRequestParams(
                    promoUiModel,
                    CartDataHelper.getAllAvailableShopGroupDataList(cartDataList),
                    cartModel.lastValidateUseRequest
                )
            }

            else -> {
                PromoRequestMapper.generateCouponListRequestParams(
                    null,
                    CartDataHelper.getAllAvailableShopGroupDataList(cartDataList),
                    null
                )
            }
        }
    }

    suspend fun getEntryPointInfoFromLastApply(
        lastApply: LastApplyUiModel,
        cartModel: CartModel,
        cartDataList: ArrayList<Any>
    ) : EntryPointInfoEvent {
        val hasSelectedItemInCart = CartDataHelper.hasSelectedCartItem(cartDataList)
        when (lastApply.userGroupPromoAbTest) {

            UserGroupMetadata.PROMO_USER_GROUP_A, UserGroupMetadata.PROMO_USER_GROUP_B -> {
                if (!hasSelectedItemInCart) {
                    return EntryPointInfoEvent.InactiveNew(
                        lastApply = lastApply,
                        isNoItemSelected = true
                    )
                }

                val isUsingPromo = lastApply.additionalInfo.usageSummaries.isNotEmpty()
                val isUseBebasOngkirOnly = lastApply.additionalInfo
                    .bebasOngkirInfo.isUseBebasOngkirOnly
                if (!isUsingPromo || isUseBebasOngkirOnly) {
                    val param = GetPromoListRecommendationParam.create(
                        promoRequest = generatePromoRequest(cartModel, cartDataList),
                        chosenAddress = chosenAddressRequestHelper.getChosenAddress(),
                        isPromoRevamp = true
                    )
                    val response = getPromoListRecommendationEntryPointUseCase(param)
                    return if (response.promoListRecommendation.data.resultStatus.success) {
                        val entryPointInfo = getCouponListRecommendationMapper
                            .mapPromoListRecommendationEntryPointResponseToEntryPointInfo(response)
                        if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                            EntryPointInfoEvent.ActiveNew(
                                lastApply = lastApply,
                                entryPointInfo = entryPointInfo
                            )
                        } else {
                            EntryPointInfoEvent.InactiveNew(
                                lastApply = lastApply,
                                entryPointInfo = entryPointInfo
                            )
                        }
                    } else {
                        if (response.promoListRecommendation.data.resultStatus.code == ResultStatus.STATUS_COUPON_LIST_EMPTY
                            || response.promoListRecommendation.data.resultStatus.code == ResultStatus.STATUS_USER_BLACKLISTED
                            || response.promoListRecommendation.data.resultStatus.code == ResultStatus.STATUS_PHONE_NOT_VERIFIED) {
                            val entryPointInfo = getCouponListRecommendationMapper
                                .mapPromoListRecommendationEntryPointResponseToEntryPointInfo(response)
                            if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                                EntryPointInfoEvent.ActiveNew(
                                    lastApply = lastApply,
                                    entryPointInfo = entryPointInfo
                                )
                            } else {
                                EntryPointInfoEvent.InactiveNew(
                                    lastApply = lastApply,
                                    entryPointInfo = entryPointInfo
                                )
                            }
                        } else {
                            EntryPointInfoEvent.Error(lastApply)
                        }
                    }
                } else {
                    return EntryPointInfoEvent.Error(lastApply)
                }
            }

            else -> {
                if (!hasSelectedItemInCart) {
                    return EntryPointInfoEvent.Inactive(
                        isNoItemSelected = true
                    )
                }

                val message = when {
                    lastApply.additionalInfo.messageInfo.message.isNotEmpty() -> {
                        lastApply.additionalInfo.messageInfo.message
                    }

                    lastApply.defaultEmptyPromoMessage.isNotBlank() -> {
                        lastApply.defaultEmptyPromoMessage
                    }

                    else -> ""
                }
                val hasAppliedPromo = lastApply.additionalInfo.messageInfo.detail.isNotEmpty()
                return if (hasAppliedPromo) {
                    EntryPointInfoEvent.Applied(
                        lastApply = lastApply,
                        message = message
                    )
                } else {
                    if (message.isNotBlank()) {
                        EntryPointInfoEvent.Active(
                            lastApply = lastApply,
                            message = message
                        )
                    } else {
                        EntryPointInfoEvent.ActiveDefault(
                            appliedPromos = emptyList()
                        )
                    }
                }
            }
        }
    }

    fun getEntryPointInfoNoItemSelected() : EntryPointInfoEvent {
        return EntryPointInfoEvent.Inactive(
            isNoItemSelected = true
        )
    }
}
