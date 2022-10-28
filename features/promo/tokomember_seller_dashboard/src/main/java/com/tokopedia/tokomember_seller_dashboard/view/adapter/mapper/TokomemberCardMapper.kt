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
        val arrayListOfColor = arrayListOf<Visitable<*>>()
        val arrayListOfPattern = arrayListOf<String>()
        val template = cardData.membershipGetCardForm?.colorTemplateList
        val templatePattern = cardData.membershipGetCardForm?.patternList
        templatePattern?.forEach {
            arrayListOfPattern.add(it?:"")
        }
        template?.forEach {
            arrayListOfColor.add(getColorTemplate(it ?: ColorTemplateListItem(), templatePattern))
        }
        return TokomemberCardColorItem(tokoVisitableCardColor = arrayListOfColor )

    }

    fun getBackground(cardData: ArrayList<CardTemplateImageListItem>, colorCode:String, pattern: ArrayList<String>):TokomemberCardBgItem{
        val arr =arrayListOf<String>()
        pattern?.forEach {
            val p = "$it-$colorCode"
            cardData?.filter {it2->
                it2?.name?.equals(p) == true
            }?.forEach {it3->
                arr.add(it3?.imageURL ?: "")
            }
        }
        val arrayListOfBg = arrayListOf<Visitable<*>>()
        arr?.forEach {
            arrayListOfBg.add(getBackgroundTemplateN(it))
        }
        return TokomemberCardBgItem(tokoVisitableCardBg = arrayListOfBg)
        }
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

private fun getBackgroundTemplateN(template: String): TokomemberCardBg {
    return TokomemberCardBg(
        imageUrl = template
    )
}

    private fun getColorTemplate(template: ColorTemplateListItem, templatePattern: List<String?>?): TokomemberCardColor {
        return TokomemberCardColor(
            colorCode = template.colorCode,
            id = template.id,
            tokoCardPatternList = templatePattern as ArrayList<String>
        )
    }
