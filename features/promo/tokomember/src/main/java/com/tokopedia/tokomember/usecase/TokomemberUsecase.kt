package com.tokopedia.tokomember.usecase

import com.tokopedia.tokomember.model.MembershipShopResponse
import com.tokopedia.tokomember.model.ShopParams
import com.tokopedia.tokomember.repository.TokomemberRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TokomemberUsecase @Inject constructor(private val tokomemberRepository: TokomemberRepository) :
    UseCase<MembershipShopResponse>() {

    var orderData : ArrayList<ShopParams> = arrayListOf()

    override suspend fun executeOnBackground(): MembershipShopResponse {
        return tokomemberRepository.getTokomemberData(orderData)
    }

    fun setGqlParams(
        orderData: List<ShopParams>,
    ) {
        this.orderData.clear()
        this.orderData.addAll(orderData)
    }
}

object TokomemberShopParams{
    const val SHOP_ID = "shopID"
    const val AMOUNT = "amount"
    const val ORDER_DATA = "orderData"

}