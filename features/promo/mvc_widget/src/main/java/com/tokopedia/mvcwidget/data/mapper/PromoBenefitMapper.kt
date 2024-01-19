package com.tokopedia.mvcwidget.data.mapper

import com.tokopedia.mvcwidget.data.entity.PromoCatalogResponse
import com.tokopedia.mvcwidget.views.benefit.BenefitText
import com.tokopedia.mvcwidget.views.benefit.BenefitTnc
import com.tokopedia.mvcwidget.views.benefit.BenefitUiModel
import com.tokopedia.mvcwidget.views.benefit.UsablePromoModel

enum class PdpComponent(val id: String) {
    FinalPrice("pdp_bs_final_price"),
    NetPrice("pdp_bs_nett_price"),
    Cashback("pdp_bs_benefit_cashback"),
    Discount("pdp_bs_benefit_discount"),
    TnC("pdp_bs_benefit_tnc"),
    Background("pdp_bs_background")
}

enum class Style(val id: String) {
    BgColor("background_color"),
    BgImage("background_image"),
    Icon("icon_url"),
    Title("text_title_value"),
    TitleColor("text_title_color"),
    TitleFormat("text_title_format"),
    Text("text_value"),
    TextColor("text_color"),
    TextFormat("text_format"),
    TextHtml("text_html"),
}

private typealias Attribute = PromoCatalogResponse.PromoCatalogGetPDEBottomSheet.Result.Widget.Component.Attribute
private typealias Component = PromoCatalogResponse.PromoCatalogGetPDEBottomSheet.Result.Widget.Component

internal fun PromoCatalogResponse.toUiModel(): BenefitUiModel {
    val components = promoCatalogGetPDEBottomSheet.resultList.first().widgetList.first().componentList
    val headerComponent = components.componentOf(PdpComponent.Background)
    val estimatePriceComponent = components.componentOf(PdpComponent.FinalPrice)
    val basePriceComponent = components.componentOf(PdpComponent.NetPrice)
    val tncComponent = components.componentOf(PdpComponent.TnC)
    val cashback = components.componentOf(PdpComponent.Cashback).toTextWithIconModel()
    val discount = components.componentOf(PdpComponent.Discount).toTextWithIconModel()
    val listPromo = listOf(cashback, discount)

    return BenefitUiModel(
        headerComponent.attributeOf(Style.BgImage),
        headerComponent.attributeOf(Style.BgColor),
        estimatePriceComponent.toTextModel(),
        basePriceComponent.toTextModel(),
        listPromo,
        listOf(),
        BenefitTnc(
            tncComponent.attributeOf(Style.TextHtml),
            tncComponent.attributeOf(Style.TextColor)
        )
    )
}

private fun List<Attribute>.toTextModel() = BenefitText(
    attributeOf(Style.Title),
    attributeOf(Style.TitleColor),
    attributeOf(Style.TitleFormat),
    attributeOf(Style.Text),
    attributeOf(Style.TextColor),
    attributeOf(Style.TextFormat),
)

private fun List<Attribute>.toTextWithIconModel() = UsablePromoModel(
    attributeOf(Style.Icon),
    attributeOf(Style.Title),
    attributeOf(Style.Text),
    attributeOf(Style.TitleColor),
    attributeOf(Style.TitleFormat),
    attributeOf(Style.TextColor),
    attributeOf(Style.TextFormat)
)

private fun List<Attribute>.attributeOf(style: Style) =
    runCatching { first { it.type == style.id }.value }.getOrDefault("")

private fun List<Component>.componentOf(component: PdpComponent) =
    runCatching { first { it.componentType == component.id }.attributeList }.getOrDefault(emptyList())

