package com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplateImageListItem
import com.tokopedia.tokomember_seller_dashboard.model.ColorTemplateListItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBg
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBgItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColor
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardColorItem

object TokomemberCardMapper {

    fun getColorData(cardData: CardData): TokomemberCardColorItem {
        val arrayList = arrayListOf<Visitable<*>>()
        val template = cardData.membershipGetCardForm?.colorTemplateList
        template?.forEach {
            arrayList.add(getColorTemplate(it ?: ColorTemplateListItem()))
        }
        return TokomemberCardColorItem(tokoVisitableCardColor = arrayList)
    }

    fun getBackgroundData(cardData: CardData): TokomemberCardBgItem {
        val arrayList = arrayListOf<Visitable<*>>()
        val template = cardData.membershipGetCardForm?.cardTemplateImageList
        template?.forEach {
            arrayList.add(getBackgroundTemplate(it ?: CardTemplateImageListItem()))
        }
        return TokomemberCardBgItem(tokoVisitableCardBg = arrayList)
    }

    private fun getBackgroundTemplate(template: CardTemplateImageListItem): TokomemberCardBg {
        return TokomemberCardBg(
            imageUrl = template.imageURL
        )
    }

    private fun getColorTemplate(template: ColorTemplateListItem): TokomemberCardColor {
        return TokomemberCardColor(
            colorCode = template.colorCode
        )
    }
}