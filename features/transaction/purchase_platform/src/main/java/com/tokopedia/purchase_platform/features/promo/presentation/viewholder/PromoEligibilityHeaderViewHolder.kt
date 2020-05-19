package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoEligibilityHeaderUiModel
import com.tokopedia.unifyprinciples.Typography

class PromoEligibilityHeaderViewHolder(private val view: View,
                                       private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEligibilityHeaderUiModel>(view) {

    private val labelPromoEligibilityHeaderTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_eligibility_header_title)
    }
    private val labelPromoEligibilityHeaderSubtitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_eligibility_header_subtitle)
    }
    private val imageChevron by lazy {
        view.findViewById<ImageView>(R.id.image_chevron)
    }

    companion object {
        val LAYOUT = R.layout.item_promo_eligibility_header
    }

    override fun bind(element: PromoEligibilityHeaderUiModel) {
        labelPromoEligibilityHeaderTitle.text = element.uiData.title
        if (element.uiData.subTitle.isNotBlank()) {
            labelPromoEligibilityHeaderSubtitle.text = element.uiData.subTitle
            labelPromoEligibilityHeaderSubtitle.show()
        } else {
            labelPromoEligibilityHeaderSubtitle.hide()
        }

        if (element.uiState.isEnabled) {
            imageChevron.gone()
            itemView.setOnClickListener { }
        } else {
            if (!element.uiState.isCollapsed) {
                imageChevron.rotation = 180f
            } else {
                imageChevron.rotation = 0f
            }
            imageChevron.show()
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) listener.onClickPromoEligibilityHeader(element)
            }
        }
    }

}