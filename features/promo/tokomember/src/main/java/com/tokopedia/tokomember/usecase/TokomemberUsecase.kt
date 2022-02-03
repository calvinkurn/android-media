package com.tokopedia.tokomember.usecase

import com.tokopedia.tokomember.model.MembershipShopResponse
import com.tokopedia.tokomember.repository.TokomemberRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import kotlin.properties.Delegates

class TokomemberUsecase @Inject constructor(private val tokomemberRepository: TokomemberRepository) :
    UseCase<MembershipShopResponse>() {

    var shopId by Delegates.notNull<Int>()
    var amount by Delegates.notNull<Float>()

    override suspend fun executeOnBackground(): MembershipShopResponse {
        return tokomemberRepository.getTokomemberData(shopId, amount)
    }

    fun setGqlParams(queryParamTokomember: Triple<Int, Float, Any?>) {
       this.shopId = queryParamTokomember.first
        this.amount = queryParamTokomember.second
    }
}

object TokomemberShopParams{
    const val SHOP_ID = "shopID"
    const val AMOUNT = "amount"
}