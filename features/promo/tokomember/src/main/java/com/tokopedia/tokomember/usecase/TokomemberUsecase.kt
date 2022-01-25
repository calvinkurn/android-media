package com.tokopedia.tokomember.usecase

import com.tokopedia.tokomember.model.MembershipGetShopRegistrationWidget
import com.tokopedia.tokomember.model.ShopRegisterResponse
import com.tokopedia.tokomember.repository.TokomemberRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import kotlin.properties.Delegates

class TokomemberUsecase @Inject constructor(private val tokomemberRepository: TokomemberRepository) :
    UseCase<ShopRegisterResponse>() {

    var shopId by Delegates.notNull<Int>()
    var amount by Delegates.notNull<Float>()
    fun getTokomemberData(
        shopId: Int?,
        amount: Float?,
        success: (MembershipGetShopRegistrationWidget?) -> Unit,
        onFail: (Throwable) -> Unit
    ) {
        if (shopId != null) {
            this.shopId = shopId
        }
        if (amount != null) {
            this.amount = amount
        }
        execute({
            success(it.data?.membershipGetShopRegistrationWidget)
        }, {
            onFail(it)
        })
    }

    override suspend fun executeOnBackground(): ShopRegisterResponse {
        return tokomemberRepository.getTokomemberData(shopId, amount)
    }
}

object TokomemberShopParams{
    const val SHOP_ID = "shopID"
    const val AMOUNT = "amount"
}