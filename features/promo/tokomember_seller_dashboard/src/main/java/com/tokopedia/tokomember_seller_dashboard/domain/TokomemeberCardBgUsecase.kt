package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplateImageListItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.TokomemberCardMapper
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TokomemeberCardBgUsecase @Inject constructor() : UseCase<TokomemberCardBgItem>() {
    private lateinit var cardData: CardData
    private lateinit var colorCOde: String
    private  var pattern = arrayListOf<String>()

    fun getCardBgDataN(
        cardData: CardData?,
        colorCOde: String,
        pattern: ArrayList<String>,
        onSuccess: (TokomemberCardBgItem) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.cardData = cardData ?: return
        this.colorCOde = colorCOde ?: return
        this.pattern.addAll(pattern)

        execute({
            if (!it.tokoVisitableCardBg.isNullOrEmpty())
                onSuccess(it)
            else
                onSuccess(TokomemberCardBgItem())
        }, { onError(it) })
    }


    override suspend fun executeOnBackground(): TokomemberCardBgItem {
        val gyroRecommendation = TokomemberCardMapper.getBackground(cardData.membershipGetCardForm?.cardTemplateImageList as ArrayList<CardTemplateImageListItem>, colorCOde, pattern)
        gyroRecommendation.let { return it }
        return TokomemberCardBgItem(arrayListOf())
    }
}



