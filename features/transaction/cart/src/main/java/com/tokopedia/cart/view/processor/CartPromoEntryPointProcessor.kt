package com.tokopedia.cart.view.processor

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.view.helper.CartDataHelper
import com.tokopedia.cart.view.mapper.PromoRequestMapper
import com.tokopedia.cart.view.uimodel.CartModel
import com.tokopedia.cart.view.uimodel.EntryPointInfoEvent
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promousage.data.request.GetPromoListRecommendationParam
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.usecase.PromoUsageGetPromoListRecommendationEntryPointUseCase
import com.tokopedia.promousage.util.PromoUsageRollenceManager
import com.tokopedia.promousage.view.mapper.PromoUsageGetPromoListRecommendationMapper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import javax.inject.Inject

class CartPromoEntryPointProcessor @Inject constructor(
    private val getPromoListRecommendationEntryPointUseCase: PromoUsageGetPromoListRecommendationEntryPointUseCase,
    private val getPromoListRecommendationMapper: PromoUsageGetPromoListRecommendationMapper,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
) {

    internal var isPromoRevamp: Boolean? = null

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

    private fun cleanPromoFromPromoRequest(promoRequest: PromoRequest): PromoRequest {
        return promoRequest.copy(
            codes = arrayListOf(),
            orders = promoRequest.orders.map {
                return@map it.copy(codes = mutableListOf())
            }
        )
    }

    suspend fun getEntryPointInfoFromLastApply(
        lastApply: LastApplyUiModel,
        cartModel: CartModel,
        cartDataList: ArrayList<Any>
    ): EntryPointInfoEvent {
        if (isPromoRevamp == null) {
            isPromoRevamp = PromoUsageRollenceManager().isRevamp(lastApply.userGroupMetadata)
        }
        val hasSelectedItemInCart = CartDataHelper.hasSelectedCartItem(cartDataList)

        if (isPromoRevamp == true) {
            if (!hasSelectedItemInCart) {
                return EntryPointInfoEvent.InactiveNew(
                    lastApply = lastApply,
                    isNoItemSelected = true,
                    recommendedPromoCodes = emptyList()
                )
            }
            val isUsingPromo = lastApply.additionalInfo.usageSummaries.isNotEmpty()
            val isUseBebasOngkirOnly = lastApply.additionalInfo
                .bebasOngkirInfo.isUseBebasOngkirOnly
            if (!isUsingPromo || isUseBebasOngkirOnly) {
                val param = GetPromoListRecommendationParam.create(
                    promoRequest = cleanPromoFromPromoRequest(generatePromoRequest(cartModel, cartDataList)),
                    chosenAddress = chosenAddressRequestHelper.getChosenAddress(),
                    isPromoRevamp = true
                )
                val response = getPromoListRecommendationEntryPointUseCase(param)
                return if (response.promoListRecommendation.data.resultStatus.success) {
                    val entryPointInfo = getPromoListRecommendationMapper
                        .mapPromoListRecommendationEntryPointResponseToEntryPointInfo(response)
                    val recommendedPromoCodes = response.promoListRecommendation.data.promoRecommendation.codes
                    if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                        EntryPointInfoEvent.ActiveNew(
                            lastApply = lastApply,
                            entryPointInfo = entryPointInfo,
                            recommendedPromoCodes = recommendedPromoCodes
                        )
                    } else {
                        EntryPointInfoEvent.InactiveNew(
                            lastApply = lastApply,
                            entryPointInfo = entryPointInfo,
                            recommendedPromoCodes = recommendedPromoCodes
                        )
                    }
                } else {
                    if (response.promoListRecommendation.data.resultStatus.code == ResultStatus.STATUS_COUPON_LIST_EMPTY ||
                        response.promoListRecommendation.data.resultStatus.code == ResultStatus.STATUS_USER_BLACKLISTED ||
                        response.promoListRecommendation.data.resultStatus.code == ResultStatus.STATUS_PHONE_NOT_VERIFIED
                    ) {
                        val entryPointInfo = getPromoListRecommendationMapper
                            .mapPromoListRecommendationEntryPointResponseToEntryPointInfo(
                                response
                            )
                        val recommendedPromoCodes = response.promoListRecommendation.data.promoRecommendation.codes
                        if (entryPointInfo.color == PromoEntryPointInfo.COLOR_GREEN) {
                            EntryPointInfoEvent.ActiveNew(
                                lastApply = lastApply,
                                entryPointInfo = entryPointInfo,
                                recommendedPromoCodes = recommendedPromoCodes
                            )
                        } else {
                            EntryPointInfoEvent.InactiveNew(
                                lastApply = lastApply,
                                entryPointInfo = entryPointInfo,
                                recommendedPromoCodes = recommendedPromoCodes
                            )
                        }
                    } else {
                        EntryPointInfoEvent.Error(lastApply)
                    }
                }
            } else {
                val message = when {
                    lastApply.additionalInfo.messageInfo.message.isNotBlank() -> {
                        lastApply.additionalInfo.messageInfo.message
                    }

                    lastApply.defaultEmptyPromoMessage.isNotBlank() -> {
                        lastApply.defaultEmptyPromoMessage
                    }

                    else -> {
                        ""
                    }
                }
                return EntryPointInfoEvent.AppliedNew(
                    lastApply = lastApply,
                    leftIconUrl = PromoEntryPointInfo.ICON_URL_ENTRY_POINT_APPLIED,
                    message = message,
                    recommendedPromoCodes = emptyList()
                )
            }
        } else {
            if (!hasSelectedItemInCart) {
                return EntryPointInfoEvent.Inactive(
                    isNoItemSelected = true
                )
            }
            val message = when {
                lastApply.additionalInfo.messageInfo.message.isNotBlank() -> {
                    lastApply.additionalInfo.messageInfo.message
                }

                lastApply.defaultEmptyPromoMessage.isNotBlank() -> {
                    lastApply.defaultEmptyPromoMessage
                }

                else -> ""
            }
            val detail = lastApply.additionalInfo.messageInfo.detail
            return if (detail.isNotBlank()) {
                EntryPointInfoEvent.Applied(
                    lastApply = lastApply,
                    message = message,
                    detail = detail
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

    fun getEntryPointInfoNoItemSelected(lastApply: LastApplyUiModel): EntryPointInfoEvent {
        return if (isPromoRevamp == true) {
            EntryPointInfoEvent.InactiveNew(
                lastApply = lastApply,
                isNoItemSelected = true,
                recommendedPromoCodes = emptyList()
            )
        } else {
            EntryPointInfoEvent.Inactive(
                isNoItemSelected = true
            )
        }
    }

    fun getEntryPointInfoActiveDefault(codes: List<String>): EntryPointInfoEvent {
        return EntryPointInfoEvent.ActiveDefault(codes)
    }
}
