package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.RedeemCouponResponse
import com.tokopedia.tokopedianow.home.domain.query.HachikoRedeem
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class RedeemCouponUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<RedeemCouponResponse>(graphqlRepository) {

    private companion object {
        const val CATALOG_ID = "catalog_id"
        const val IS_GIFT = "is_gift"
        const val GIFT_USER_ID = "gift_user_id"
        const val GIFT_EMAIL = "gift_email"
        const val NOTES = "notes"
    }

    init {
        setGraphqlQuery(HachikoRedeem())
        setTypeClass(RedeemCouponResponse::class.java)
    }

    suspend fun execute(
        catalogId: Int,
        isGift: Int,
        giftUserId: Int,
        giftEmail: String,
        notes: String
    ): RedeemCouponResponse {
        setRequestParams(RequestParams.create().apply {
            putInt(CATALOG_ID, catalogId)
            putInt(IS_GIFT, isGift)
            putInt(GIFT_USER_ID, giftUserId)
            putString(GIFT_EMAIL, giftEmail)
            putString(NOTES, notes)
        }.parameters)
        return executeOnBackground()
    }
}
