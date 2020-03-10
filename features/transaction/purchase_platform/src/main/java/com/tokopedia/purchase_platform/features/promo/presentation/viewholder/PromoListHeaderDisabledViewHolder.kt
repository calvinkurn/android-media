package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.setImageFilterGrayScale
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel
import kotlinx.android.synthetic.main.item_promo_list_header_disabled.view.*

class PromoListHeaderDisabledViewHolder(private val view: View,
                                        private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_promo_list_header_disabled
    }

    override fun bind(element: PromoListHeaderUiModel) {
        if (element.uiData.iconUrl.isNotBlank()) {
            ImageHandler.loadImageRounded2(itemView.context, itemView.image_promo_list_header, element.uiData.iconUrl)
            itemView.image_promo_list_header.show()
        } else {
            itemView.image_promo_list_header.gone()
        }

        itemView.label_promo_list_header_title.text = element.uiData.title
        if (element.uiState.hasSelectedPromoItem) {
            itemView.label_promo_list_header_sub_title.text = "Promo dipilih"
        } else {
            itemView.label_promo_list_header_sub_title.text = element.uiData.subTitle
        }

        setImageFilterGrayScale(itemView.image_promo_list_header)
        itemView.label_promo_list_header_sub_title.gone()
        itemView.setOnClickListener {}
    }

}