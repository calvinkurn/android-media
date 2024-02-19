package com.tokopedia.home_component.mapper

import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import com.tokopedia.discovery_component.widgets.automatecoupon.TimeLimit
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.visitable.CouponCtaState
import com.tokopedia.home_component.visitable.CouponTrackerModel
import com.tokopedia.home_component.visitable.CouponWidgetDataItemModel
import com.tokopedia.home_component.visitable.CouponWidgetDataModel
import com.tokopedia.kotlin.extensions.view.toDate
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.productcard.reimagine.LabelGroupStyle

object CouponWidgetMapper {

    private const val MAX_COUPON_SIZE = 3

    fun map(model: ChannelModel): CouponWidgetDataModel {
        return CouponWidgetDataModel(
            channelModel = model,
            backgroundGradientColor = model.channelBanner.gradientColor,
            coupons = model.channelGrids
                .take(MAX_COUPON_SIZE)
                .newMapToAutomationCouponWidgetModel()
        )
    }

    private fun List<ChannelGrid>.newMapToAutomationCouponWidgetModel(): List<CouponWidgetDataItemModel> {
        return mapIndexed { index, channelGrid ->
            val labelGroupMap = channelGrid.labelGroup.associateBy { it.position }

            val couponTrackerModel = CouponTrackerModel(
                gridId = channelGrid.id,
                attribution = channelGrid.attribution
            )

            if (size == 1) return@mapIndexed labelGroupMap.single(couponTrackerModel)
            if (size == 2) return@mapIndexed labelGroupMap.grid(couponTrackerModel)

            if (index == 0) {
                labelGroupMap.single(couponTrackerModel)
            } else {
                labelGroupMap.grid(couponTrackerModel)
            }
        }
    }

    private fun Map<String, LabelGroup>.single(trackerModel: CouponTrackerModel) =
        CouponWidgetDataItemModel(
            trackerModel = trackerModel,
            coupon = toSingleCouponWidget(),
            button = getCtaState()
        )

    private fun Map<String, LabelGroup>.grid(trackerModel: CouponTrackerModel) =
        CouponWidgetDataItemModel(
            trackerModel = trackerModel,
            coupon = toGridCouponWidget(),
            button = getCtaState()
        )

    private fun Map<String, LabelGroup>.toSingleCouponWidget() =
        AutomateCouponModel.List(
            type = toText(LabelGroupConst.benefitType),
            benefit = toText(LabelGroupConst.benefitValue),
            tnc = toText(LabelGroupConst.tncText),
            backgroundUrl = toUrl(LabelGroupConst.backgroundImage),
            timeLimit = TimeLimit.Timer(
                prefix = toText(LabelGroupConst.expiredText),
                endDate = (get(LabelGroupConst.expiredTextUnix)
                    ?.title
                    .toLongOrZero() * 1000)
                    .toDate()
            ),
            iconUrl = toUrl(LabelGroupConst.iconImage),
            shopName = null,
            badgeText = null,
        )

    private fun Map<String, LabelGroup>.toGridCouponWidget() =
        AutomateCouponModel.Grid(
            type = toText(LabelGroupConst.benefitType),
            benefit = toText(LabelGroupConst.benefitValue),
            tnc = toText(LabelGroupConst.tncText),
            backgroundUrl = toUrl(LabelGroupConst.backgroundImage),
            iconUrl = toUrl(LabelGroupConst.iconImage),
            shopName = null,
            badgeText = null,
        )

    private fun Map<String, LabelGroup>.getCtaState(): CouponCtaState {
        val data = CouponCtaState.Data(
            catalogId = get(LabelGroupConst.catalogId)?.title ?: "",
            url = get(LabelGroupConst.ctaRedirectUrl)?.url ?: "",
            appLink = get(LabelGroupConst.ctaRedirectAppLink)?.url ?: ""
        )

        return when (get(LabelGroupConst.ctaText)?.type) {
            LabelGroupConst.ctaClaim -> CouponCtaState.Claim(data)
            LabelGroupConst.ctaRedirect -> CouponCtaState.Redirect(data)
            else -> CouponCtaState.OutOfStock
        }
    }

    private fun Map<String, LabelGroup>.toText(position: String): DynamicColorText {
        val label = get(position) ?: return DynamicColorText("", "")
        val textColor = label.styles.firstOrNull { it.key == LabelGroupStyle.TEXT_COLOR }
        return DynamicColorText(label.title, textColor?.value ?: "")
    }

    private fun Map<String, LabelGroup>.toUrl(position: String): String {
        return get(position)?.url ?: ""
    }

    internal object LabelGroupConst {
        // Common
        const val benefitType = "benefit-type"
        const val benefitValue = "benefit-value"
        const val tncText = "tnc-text"
        const val expiredText = "expired-text"
        const val expiredTextUnix = "expired-time-unix"
        const val backgroundImage = "background-image"
        const val iconImage = "icon-image"

        // Coupon
        const val catalogId = "catalog-id"

        // CTA
        const val ctaText = "cta-text"
        const val ctaRedirectUrl = "cta-redirect-url"
        const val ctaRedirectAppLink = "cta-redirect-applink"

        // CTA: Type
        const val ctaClaim = "claim"
        const val ctaRedirect = "redirect"
    }
}
