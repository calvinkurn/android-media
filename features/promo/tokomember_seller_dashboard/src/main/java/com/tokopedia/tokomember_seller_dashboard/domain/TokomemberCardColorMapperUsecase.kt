package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.TokomemberCardMapper
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColorItem
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TokomemberCardColorMapperUsecase @Inject constructor() : UseCase<TokomemberCardColorItem>() {
    private lateinit var cardData: CardData

    fun getCardColorData(
        membershipData: CardData?,
        onSuccess: (TokomemberCardColorItem) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.cardData = membershipData ?: return
        execute({
            if (!it.tokoVisitableCardColor.isNullOrEmpty())
                onSuccess(it)
        }, { onError(it) })
    }

    override suspend fun executeOnBackground(): TokomemberCardColorItem {
        val gyroRecommendation = TokomemberCardMapper.getColorData(cardData)
        gyroRecommendation.let { return it }
        return TokomemberCardColorItem(arrayListOf())
    }
}



