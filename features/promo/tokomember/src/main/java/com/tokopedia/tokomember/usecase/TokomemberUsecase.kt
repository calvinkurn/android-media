package com.tokopedia.tokomember.usecase

import com.tokopedia.tokomember.model.MembershipOrderData
import com.tokopedia.tokomember.model.MembershipShopResponse
import com.tokopedia.tokomember.repository.TokomemberRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TokomemberUsecase @Inject constructor(private val tokomemberRepository: TokomemberRepository) :
    UseCase<MembershipShopResponse>() {

    var orderData : ArrayList<MembershipOrderData> = arrayListOf()

    override suspend fun executeOnBackground(): MembershipShopResponse {
        return tokomemberRepository.getTokomemberData(orderData)
    }

    fun setGqlParams(
        orderData: List<MembershipOrderData>,
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