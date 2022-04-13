package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.TokomemberCardMapper
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TokomemeberCardBgUsecase @Inject constructor() : UseCase<TokomemberCardBgItem>() {
    private lateinit var cardData: CardData

    fun getCardBgData(
        cardData: CardData?,
        onSuccess: (TokomemberCardBgItem) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.cardData = cardData ?: return
        execute({
            if (!it.tokoVisitableCardBg.isNullOrEmpty())
                onSuccess(it)
        }, { onError(it) })
    }

    override suspend fun executeOnBackground(): TokomemberCardBgItem {
        val gyroRecommendation = TokomemberCardMapper.getBackgroundData(cardData)
        gyroRecommendation.let { return it }
        return TokomemberCardBgItem(arrayListOf())
    }
}



