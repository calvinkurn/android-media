package com.tokopedia.home_component.mapper

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import com.tokopedia.discovery_component.widgets.automatecoupon.TimeLimit
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.productcard.reimagine.LabelGroupStyle
import com.tokopedia.utils.htmltags.HtmlUtil

object CouponWidgetMapper {

    private const val MAX_COUPON_SIZE = 3

    fun map(model: ChannelModel): CouponWidgetDataModel {
        return CouponWidgetDataModel(
            channelModel = model,
            backgroundGradientColor = model.channelBanner.gradientColor,
            coupons = model.channelGrids
                .take(MAX_COUPON_SIZE)
                .mapToAutomationCouponWidgetModel()
        )
    }

    private fun List<ChannelGrid>.mapToAutomationCouponWidgetModel(): List<CouponWidgetDataItemModel> {
        val models = mutableListOf<CouponWidgetDataItemModel>()

        if (size == 1) return map { it.single() }
        if (size == 2) return map { it.grid() }

        forEachIndexed { index, model ->
            if (index == 0) {
                models.add(model.single())
            } else {
                models.add(model.grid())
            }
        }

        return models
    }

    private fun ChannelGrid.single() =
        CouponWidgetDataItemModel(toSingleCouponWidget(), labelGroup.getCtaState())

    private fun ChannelGrid.grid() =
        CouponWidgetDataItemModel(toGridCouponWidget(), labelGroup.getCtaState())

    private fun ChannelGrid.toSingleCouponWidget() =
        AutomateCouponModel.List(
            type = labelGroup.toText("benefit-type"),
            benefit = labelGroup.toText("benefit-value"),
            tnc = labelGroup.toText("tnc-text"),
            backgroundUrl = labelGroup.toUrl("background-image"),
            timeLimit = TimeLimit.Text(
                prefix = labelGroup.toText("expired-text"),
                endText = HtmlUtil
                    .fromHtml(labelGroup.toText("expired-value").value)
                    .toString()
            ),
            iconUrl = labelGroup.toUrl("icon-image"),
            shopName = null,
            badgeText = null,
        )

    private fun ChannelGrid.toGridCouponWidget() =
        AutomateCouponModel.Grid(
            type = labelGroup.toText("benefit-type"),
            benefit = labelGroup.toText("benefit-value"),
            tnc = labelGroup.toText("tnc-text"),
            backgroundUrl = labelGroup.toUrl("background-image"),
            iconUrl = labelGroup.toUrl("icon-image"),
            shopName = null,
            badgeText = null,
        )

    private fun List<LabelGroup>.getCtaState(): CouponCtaState {
        val data = CouponCtaState.Data(
            catalogId = findByPosition("catalog-id")?.title ?: "",
            url = findByPosition("cta-redirect-url")?.url ?: "",
            appLink = findByPosition("cta-redirect-applink")?.url ?: ""
        )

        return when (findByPosition("cta-text")?.type) {
            "claim" -> CouponCtaState.Claim(data)
            "redirect" -> CouponCtaState.Redirect(data)
            else -> CouponCtaState.OutOfStock
        }
    }

    private fun List<LabelGroup>.toText(positionType: String): DynamicColorText {
        val label = findByPosition(positionType) ?: return DynamicColorText("", "")
        val textColor = label.styles.first { it.key == LabelGroupStyle.TEXT_COLOR }
        return DynamicColorText(label.title, textColor.value)
    }

    private fun List<LabelGroup>.toUrl(position: String): String {
        return findByPosition(position)?.url ?: ""
    }

    private fun List<LabelGroup>.findByPosition(position: String) = firstOrNull { it.position == position }
}
