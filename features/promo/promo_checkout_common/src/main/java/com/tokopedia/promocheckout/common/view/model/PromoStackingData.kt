package com.tokopedia.promocheckout.common.view.model

import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.uimodel.TrackingDetailUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoStackingData(var typePromo: Int = 0,
                             var promoCode: String = "",
                             var description: String = "",
                             var title: String = "",
                             var counterLabel: String = "",
                             var amount: Int = 0,
                             var state: TickerPromoStackingCheckoutView.State = TickerPromoStackingCheckoutView.State.EMPTY,
                             var variant: TickerPromoStackingCheckoutView.Variant = TickerPromoStackingCheckoutView.Variant.GLOBAL,
                             var titleDefault: String = "",
                             var counterLabelDefault: String = "",
                             var trackingDetailUiModels: List<TrackingDetailUiModel> = emptyList()
) : Parcelable {

    fun getPromoCodeSafe(): String {
        if (state != TickerPromoStackingCheckoutView.State.EMPTY) {
            return promoCode
        } else {
            return ""
        }
    }

    class Builder {
        private var typePromo: Int = 0
        var promoCode: String = ""
        var description: String = ""
        var title: String = ""
        var counterLabel: String = ""
        var amount: Int = 0
        var state: TickerPromoStackingCheckoutView.State = TickerPromoStackingCheckoutView.State.EMPTY
        var variant: TickerPromoStackingCheckoutView.Variant = TickerPromoStackingCheckoutView.Variant.GLOBAL
        var titleDefault: String = ""
        var counterLabelDefault: String = ""
        var trackingDetailUiModels: List<TrackingDetailUiModel> = emptyList()

        fun typePromo(typePromo: Int) = apply { this.typePromo = typePromo }
        fun promoCode(promoCode: String) = apply { this.promoCode = promoCode }
        fun description(description: String) = apply { this.description = description }
        fun title(title: String) = apply { this.title = title }
        fun counterLabel(counterLabel: String) = apply { this.counterLabel = counterLabel }
        fun amount(amount: Int) = apply { this.amount = amount }
        fun state(state: TickerPromoStackingCheckoutView.State) = apply { this.state = state }
        fun variant(variant: TickerPromoStackingCheckoutView.Variant) = apply { this.variant = variant }
        fun titleDefault(titleDefault: String) = apply { this.titleDefault = titleDefault }
        fun counterLabelDefault(counterLabelDefault: String) = apply { this.counterLabelDefault = counterLabelDefault }
        fun trackingDetailUiModels(trackingDetailUiModel: List<TrackingDetailUiModel>) = apply { this.trackingDetailUiModels = trackingDetailUiModel }

        fun build() = PromoStackingData(
                typePromo,
                promoCode,
                description,
                title,
                counterLabel,
                amount,
                state,
                variant,
                titleDefault,
                counterLabelDefault,
                trackingDetailUiModels
        )
    }
}